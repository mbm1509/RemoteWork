package com.mosis.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

import org.w3c.dom.Text;

public class UserDialog extends AppCompatDialogFragment {


    public UserModel selectedUser;

    // controls

    private TextView tbxUsername;
    private TextView tbxEmail;
    private TextView tbxName;
    private TextView tbxPhoneNumber;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =  getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_user_dialog, null);


        builder.setView(view).setTitle("User Profile").setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {




            }
        });

        // Populate user details


        tbxUsername = (TextView) view.findViewById(R.id.lbl_username);
        tbxEmail = (TextView) view.findViewById(R.id.lbl_email);
        tbxName = (TextView) view.findViewById(R.id.lbl_name);
        tbxPhoneNumber = (TextView) view.findViewById(R.id.lbl_phone);

        tbxUsername.setText("UserName : " + selectedUser.getUserName());
        tbxPhoneNumber.setText("Phone Number : " + selectedUser.getPhoneNumber());
        tbxName.setText("Name : " + selectedUser.getFirstName() + " " + selectedUser.getLastName());
        tbxEmail.setText("Email Address : " + selectedUser.getEmailAddress());


        return builder.create();
    }
}
