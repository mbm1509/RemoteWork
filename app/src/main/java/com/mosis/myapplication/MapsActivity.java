package com.mosis.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseAuth mAuth;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location onlyOneLocation;
    private final int REQUEST_FINE_LOCATION = 1234;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                UpdateUserLocation(location.getLatitude(), location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates("gps", 5000, 3, locationListener);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationViewMap);

        bottomNav.setOnNavigationItemSelectedListener(navListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    Fragment selectedFragment = null;

                    boolean homeOpened = false;

                    switch (menuItem.getItemId()){

                        case R.id.nav_home :
                        GoToHome();
                        homeOpened = true;
                        break;

                        case R.id.nav_map:
                           GoToMaps();
                           homeOpened = true;
                            break;

                        case R.id.nav_map_list :
                            selectedFragment = new MapFragment();
                            break;

                    }


                    if(homeOpened){
                        return true;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                    return true;
                }
            };

    private void GoToHome(){

        Intent intent = new Intent(this, DisplayMessageActivity.class);
        startActivity(intent);

    }

    private void GoToMaps(){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    private int counter = 0;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        updateLocationUI();

        // add markers from DB

        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("places");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


              for(DataSnapshot ds : dataSnapshot.getChildren()){

                 final PlaceModel model = ds.getValue(PlaceModel.class);

                 // create marker
                  AddMarker(model);

              }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference mDatabase2;
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("users");

        mDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    final UserModel user = ds.getValue(UserModel.class);


                    AddUserMarker(user);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mMap.setMyLocationEnabled(true);


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                       @Override
                                       public void onMapClick(LatLng latLng) {

                                           // instead of adding marker
                                           // create modal with data that user needs to fill in

                                           OpenMapDialog(latLng.latitude, latLng.longitude);

                                           // modal fill handle DB calls



                                       }
                                   }

        );

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                try{


                    PlaceModel place = (PlaceModel)marker.getTag();


                }catch(Exception e){

                    // open User Details Dialog

                    OpenUserDetailsDilog((UserModel) marker.getTag());


                    return false;
                }

                OpenPlaceDetailsDialog(marker.getTitle(), marker.getPosition().latitude, marker.getPosition().longitude, (PlaceModel)marker.getTag());

                return false;
            }
        });

    }



    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }else{

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

        }

    }

    public void OpenMapDialog(double lat, double lng){


        MapDialog mapDialog = new MapDialog();
        mapDialog.latitude = lat;
        mapDialog.longitude = lng;
        mapDialog.show(getSupportFragmentManager(), "Add Place");

    }

    public void OpenPlaceDetailsDialog(String title,double lat, double lng, PlaceModel place){

        MapDialog mapDialog = new MapDialog();
        mapDialog.latitude = lat;
        mapDialog.longitude = lng;
        mapDialog.showDetails = true;
        mapDialog.title = title;
        mapDialog.id = place.getId();
        mapDialog.selectedPlace = place;
        mapDialog.show(getSupportFragmentManager(), "Details");

    }


    public void OpenUserDetailsDilog(UserModel user){


        UserDialog ud = new UserDialog();
        ud.selectedUser = user;
        ud.show(getSupportFragmentManager(), "User Details");

    }


    private void AddMarker(PlaceModel place){


       Marker marker = mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(place.getLatitude(),place.getLongitude()))
                                        .title(place.getTitle())
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                                        );

       marker.setTag(place);
    }

    private void AddUserMarker(UserModel user){

        Marker marker = mMap.addMarker(new MarkerOptions()
                                       .position(new LatLng(user.getLatitude(), user.getLongitude()))
                                       .title(user.getUserName())
                                       .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        marker.setTag(user);

    }

    // LOCATION

    private void UpdateUserLocation(final double lat, final double lng){

        DatabaseReference mDatabase2;
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid());

        mDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final UserModel user = dataSnapshot.getValue(UserModel.class);
                user.setLatitude(lat);
                user.setLongitude(lng);

                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("users").child(FirebaseAuth.getInstance().getUid()).setValue(user);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
