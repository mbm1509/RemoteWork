package com.mosis.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        // Get the intent
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);


        //TextView textView = findViewById(R.id.textView);
       // textView.setText(message);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);

        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    boolean mapsOpened = false;

                    switch (menuItem.getItemId()){
                        case R.id.nav_friends:
                            selectedFragment =  new FriendsFragment();
                            break;
                        case R.id.nav_home:
                            selectedFragment =  new HomeFragment();
                            break;
                        case R.id.nav_map:

                            GoToMaps();
                            mapsOpened = true;
                            break;
                        case R.id.nav_notifications:
                            selectedFragment =  new FriendsFragment();
                            break;
                    }

                    if(mapsOpened){
                        return true;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                    return true;

                }
            };

    private void GoToMaps(){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

}
