package com.mosis.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class MapDialog extends AppCompatDialogFragment {


    public String title;
    public double longitude;
    public double latitude;
    public boolean showDetails = false;
    public String id;
    public PlaceModel selectedPlace; // for editing only


    private FirebaseAuth mAuth;

    private EditText tbxTitle;
    private EditText tbxLongitude;
    private EditText tbxLatitude;
    private EditText tbxNumber;
    private TextView averageRating;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =  getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_map_dialog, null);

        builder.setView(view).setTitle("Add Place").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        })
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                if(showDetails == false){

                    PlaceModel placeModel = new PlaceModel();
                    placeModel.setTitle(tbxTitle.getText().toString());
                    placeModel.setLatitude(Double.parseDouble(tbxLatitude.getText().toString()));
                    placeModel.setLongitude(Double.parseDouble(tbxLongitude.getText().toString()));
                    placeModel.setCreatedBy(FirebaseAuth.getInstance().getUid());

                    // Connect to Firebase

                    AddPlace(placeModel);

                    dialog.dismiss();

                }else{

                    // you can only update if add_numbers is populated

                    if(NumberValidator(Integer.parseInt(tbxNumber.getText().toString()))){

                        PlaceModel placeModel = new PlaceModel();

                        placeModel.setRate(selectedPlace.getRate());

                        placeModel.setTitle(tbxTitle.getText().toString());
                        placeModel.setLatitude(Double.parseDouble(tbxLatitude.getText().toString()));
                        placeModel.setLongitude(Double.parseDouble(tbxLongitude.getText().toString()));
                        placeModel.setId(id);
                        placeModel.addRating(Integer.parseInt(tbxNumber.getText().toString()));
                        placeModel.setCreatedBy(selectedPlace.getCreatedBy());

                        UpdatePlace(placeModel);

                    }
                }


            }
        })
        .setNeutralButton("Add Photo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(showDetails == false){

                   dispatchTakePictureIntent();

                }else{
                    return;
                }

            }
        });


        tbxTitle = view.findViewById(R.id.add_title);
        tbxLatitude = view.findViewById(R.id.add_lat);
        tbxLongitude = view.findViewById(R.id.add_long);

        tbxLatitude.setText(Double.toString(latitude));
        tbxLongitude.setText(Double.toString(longitude));

        if(showDetails){

            tbxTitle.setText(title);

            tbxLongitude.setEnabled(false);
            tbxLatitude.setEnabled(false);
            tbxTitle.setEnabled(false);

            tbxNumber = view.findViewById(R.id.add_number);
            tbxNumber.setVisibility(View.VISIBLE);

            averageRating = view.findViewById(R.id.lbl_currentRating);
            averageRating.setVisibility(View.VISIBLE);
            averageRating.setText("Average rating : " + selectedPlace.CalculateRating());

        }else{
            tbxNumber = view.findViewById(R.id.add_number);
            tbxNumber.setVisibility(View.INVISIBLE);

            averageRating = view.findViewById(R.id.lbl_currentRating);
            averageRating.setVisibility(View.INVISIBLE);

            tbxLongitude.setEnabled(true);
            tbxLatitude.setEnabled(true);
            tbxTitle.setEnabled(true);

            tbxTitle.setText("");

        }

        return builder.create();
    }

    // ADD PLACE TO FIREBASE


    private void AddPlace(PlaceModel placeModel){

        FirebaseUser user = mAuth.getCurrentUser();

        PlaceModel placeModel1 = placeModel;

        UUID uuid = UUID.randomUUID();

        placeModel1.setId(uuid.toString());

        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("places").child(placeModel1.getId()).setValue(placeModel1);
    }

    private  void UpdatePlace(final PlaceModel placeModel){


        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("places").child(placeModel.getId()).setValue(placeModel);

        // add score to the user

        // get score value
        final String userID = placeModel.getCreatedBy();


        DatabaseReference mDatabase2;
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("users").child(userID);

        mDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


               final UserModel user = dataSnapshot.getValue(UserModel.class);
                user.setScore(placeModel.GetTotalScore());

                AddUserScore(user,userID);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }



    // IMAGE CODE

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);//zero can be replaced with any action code

        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 1);//one can be replaced with any action code

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        ImageView imageView = getView().findViewById(R.id.imageViewIcon);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);

            //imageView.setRotation(-90);
        }
    }

    private void uploadImage(){

        ImageView imageView = getView().findViewById(R.id.imageViewIcon);

        // Get the data from an ImageView as bytes
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();


    }

    private void AddUserScore(UserModel user, String userID){

        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(userID).setValue(user);

    }

    private boolean NumberValidator(int number){

        // find add_number

        if(number <= 0 || number > 5){

            return false;

        }else{
            return true;
        }

    }



}
