package com.mosis.myapplication;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

public class MapFragment extends Fragment {

    private EditText tbxRange;
    private ArrayList originalList ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_map, container, false);

        final ListView listView = (ListView) view.findViewById(R.id.listview_mapList);
        final ArrayList placeList = new ArrayList<String>();

        originalList = new ArrayList<String>();



        final View view2 = view;

        DatabaseReference mDatabase;
        Query query = FirebaseDatabase.getInstance().getReference().child("places");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    final PlaceModel model = ds.getValue(PlaceModel.class);

                    placeList.add(model.getTitle() + " --  Avg Rate : " + model.CalculateRating());

                }

                Collections.reverse(placeList);

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, placeList);

                listView.setAdapter(arrayAdapter);

                originalList = placeList;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        CheckBox myLocationsCheckbox = ( CheckBox ) view.findViewById(R.id.cb_myLocations);
        myLocationsCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                    placeList.clear();

                    DatabaseReference mDatabase;
                   mDatabase = FirebaseDatabase.getInstance().getReference().child("places");

                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for(DataSnapshot ds : dataSnapshot.getChildren()){


                                final PlaceModel model = ds.getValue(PlaceModel.class);

                                String userID = FirebaseAuth.getInstance().getUid();
                                String currentUserID = model.getCreatedBy();

                                if(userID.equalsIgnoreCase(currentUserID)){

                                    placeList.add(model.getTitle() + " --  Avg Rate : " + model.CalculateRating());

                                }

                            }

                            //Collections.reverse(placeList);

                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, placeList);

                            listView.setAdapter(arrayAdapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else{


                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, originalList);

                    listView.setAdapter(arrayAdapter);

                }

            }
        });



        CheckBox rangeCheckbox = ( CheckBox ) view.findViewById(R.id.cb_Raidus);
        rangeCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {

                    // get user current location

                    final Location location = new Location("");

                    DatabaseReference mDatabase2;
                    mDatabase2 = FirebaseDatabase.getInstance().getReference().child("users");

                    mDatabase2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for(DataSnapshot ds : dataSnapshot.getChildren()){

                                final UserModel model = ds.getValue(UserModel.class);

                                String userID = FirebaseAuth.getInstance().getUid();
                                String currentUserID = ds.getKey();

                                if(userID.equalsIgnoreCase(currentUserID)){

                                    location.setLatitude(model.getLatitude());
                                    location.setLongitude(model.getLongitude());

                                }

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                    tbxRange = (EditText) view2.findViewById(R.id.add_range);

                    final int range = Integer.parseInt(tbxRange.getText().toString()) * 1000;



                    placeList.clear();


                    DatabaseReference mDatabase;
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("places");

                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for(DataSnapshot ds : dataSnapshot.getChildren()){

                                final PlaceModel model = ds.getValue(PlaceModel.class);

                                Location placeLocation = new Location("");

                                placeLocation.setLatitude(model.getLatitude());
                                placeLocation.setLongitude(model.getLongitude());

                                // check if place location is 100 KM away




                                if(location.distanceTo(placeLocation) <= range){

                                    placeList.add(model.getTitle() + " --  Avg Rate : " + model.CalculateRating());

                                }

                            }

                           // Collections.reverse(placeList);

                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, placeList);

                            listView.setAdapter(arrayAdapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else{

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, originalList);

                    listView.setAdapter(arrayAdapter);
                }

            }
        });


        return view;

    }


}
