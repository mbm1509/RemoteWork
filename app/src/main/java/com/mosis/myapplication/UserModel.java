package com.mosis.myapplication;

public class UserModel {

    // constructors

    public UserModel(){ }


    // attributes

    private String ID;
    private String FirstName;
    private String LastName;
    private String Password;
    private String EmailAddress;
    private String UserName;
    private String PhoneNumber;
    private boolean HasPhoto;
    private int score;
    private double latitude;
    private double longitude;


    // properties


    public String getID() {
        return ID;
    }


    public void setID(String ID) {
        this.ID = ID;
    }

    public String getFirstName(){
        return this.FirstName;
    }

    public void setFirstName(String FirstName){
        this.FirstName = FirstName;
    }

    public String getLastName(){
        return this.LastName;
    }

    public void setLastName(String LastName){
        this.LastName = LastName;
    }

    public String getPassword(){
        return this.Password;
    }

    public void setPassword(String Password){
        this.Password = Password;
    }

    public String getEmailAddress(){
        return this.EmailAddress;
    }

    public void setEmailAddress(String EmailAddress){
        this.EmailAddress = EmailAddress;
    }

    public String getUserName(){
        return this.UserName;
    }

    public void setUserName(String UserName){
        this.UserName = UserName;
    }

    public String getPhoneNumber(){
        return this.EmailAddress;
    }

    public void setPhoneNumber(String PhoneNumber){
        this.PhoneNumber = PhoneNumber;
    }

    public boolean getHasPhoto(){
        return this.HasPhoto;
    }

    public void setHasPhoto(boolean HasPhoto){
        this.HasPhoto = HasPhoto;
    }

    public int getScore() { return score; }

    public void setScore(int score) { this.score = score; }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    // methods

    public boolean LoginValidation(){

        if(EmailAddress.isEmpty()){

            return false;
        }

        if(Password.isEmpty()){
            return false;
        }

        return true;

    }

    public boolean RegisterValidation(){

        if(FirstName.isEmpty()){

            return false;
        }

        if(LastName.isEmpty()){
            return false;
        }
        if(EmailAddress.isEmpty()){

            return false;
        }

        if(Password.isEmpty()){
            return false;
        }

        if(UserName.isEmpty()){

            return false;
        }

        if(PhoneNumber.isEmpty()){
            return false;
        }

        return true;
    }

}
