package com.mosis.myapplication;

public class FriendModel {

    private String firstName;
    private String lastName;
    private String emailAddress;
    private boolean hasPhoto;


    public FriendModel(){}

    public String getFirstName(){
        return this.firstName;
    }

    public String getLastName(){

        return  this.lastName;
    }

    public String getEmailAddress(){

        return this.emailAddress;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public void setEmailAddress(String emailAddress){
        this.lastName = lastName;
    }


}
