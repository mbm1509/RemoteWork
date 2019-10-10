package com.mosis.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class RegisterActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();



    }

    public void Register(View view){


        // get all textboxes

        EditText firstName = findViewById(R.id.tbxFirstName);
        EditText lastName = findViewById(R.id.tbxLastName);
        EditText email = findViewById(R.id.tbxEmail);
        EditText password = findViewById(R.id.tbxPassword);
        EditText username = findViewById(R.id.tbxUsername);
        EditText phone = findViewById(R.id.tbxPhone);

        // create User object

        UserModel user = new UserModel();

        user.setFirstName(firstName.getText().toString());
        user.setLastName(lastName.getText().toString());
        user.setEmailAddress(email.getText().toString());
        user.setPassword(password.getText().toString());
        user.setUserName(username.getText().toString());
        user.setPhoneNumber(phone.getText().toString());
        user.setScore(0);


        ImageView imageView = (ImageView) findViewById(R.id.imageViewIcon);

        if(imageView.getDrawable() != null){

            user.setHasPhoto(true);

        }else{
            user.setHasPhoto(false);
        }

        // get error label

        if(user.RegisterValidation()){

            CreateAccount(user);

        }else{
            TextView error = findViewById(R.id.lblError);
            error.setText("Authentication failed!" + "Please fill all fields.");
        }

    }

    public void GoToLogin(View view){

        Intent intent = new Intent(this,Login.class);
        startActivity(intent);

    }

    public void OpenCamera(View view){

        dispatchTakePictureIntent();

    }

    private void CreateAccount(final UserModel userModel){


        mAuth.createUserWithEmailAndPassword(userModel.getEmailAddress(),userModel.getPassword()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    // create user model

                    FirebaseUser user = mAuth.getCurrentUser();

                    UserModel user2 = userModel;


                    DatabaseReference mDatabase;
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user2);

                   // uploadImage();


                }else{

                    TextView error = findViewById(R.id.lblError);
                    error.setText("Authentication failed!" + task.getException());

                }
            }
        });



    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        ImageView imageView = findViewById(R.id.imageViewIcon);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);

            imageView.setRotation(-90);
        }
    }

    private void uploadImage(){

        ImageView imageView = findViewById(R.id.imageViewIcon);

         // Get the data from an ImageView as bytes
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference riversRef = mStorageRef.child("images/");
        UploadTask uploadTask = riversRef.putBytes(data);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });

    }


}
