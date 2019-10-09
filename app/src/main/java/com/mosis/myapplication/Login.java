package com.mosis.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
    }

    // methods used on Login Activity

    public void GoToRegister(View view){

        // this method will switch user to Register activity

        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);

    }

    public void Login(View view){


        // get two textboxes and label

        EditText email = findViewById(R.id.tbxEmail);
        EditText pass = findViewById(R.id.tbxPassword);
        TextView error = findViewById(R.id.lblError);

        UserModel user = new UserModel();

        user.setEmailAddress(email.getText().toString());
        user.setPassword(pass.getText().toString());

        if(user.LoginValidation() == false){


            error.setText(getString(R.string.error_fill));
        }else{


            mAuth.signInWithEmailAndPassword(user.getEmailAddress(), user.getPassword())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information

                                FirebaseUser user = mAuth.getCurrentUser();
                                GoToDisplayMessage();

                            } else {
                                // If sign in fails, display a message to the user.

                                TextView error = findViewById(R.id.lblError);
                                error.setText("Authentication failed");


                            }
                        }
                    });
        }
    }


    private void GoToDisplayMessage(){

        Intent intent = new Intent(this,DisplayMessageActivity.class);
        startActivity(intent);

    }

    private void GoToMaps(){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }


}
