package com.mosis.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class PlaceModel {


    // ATTRIBUTES

    private String id;
    private String title;
    private double longitude;
    private double latitude;
    private boolean hasPhoto;
    private List<Integer> rate;
    private String[] comments;
    private String description;
    private String createdBy;



    // CONSTRUCTORS

    public PlaceModel(){

        rate = new ArrayList<Integer>();

    }



    // PROPERTIES


    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }


    public List<Integer> getRate() {
        return rate;
    }

    public double getLatitude() {
        return latitude;
    }


    public double getLongitude() {
        return longitude;
    }

    public String getDescription() {
        return description;
    }

    public String[] getComments() {
        return comments;
    }

    public void setComments(String[] comments) {
        this.comments = comments;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHasPhoto(boolean hasPhoto) {
        this.hasPhoto = hasPhoto;
    }


    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setRate(List<Integer> rate) {
        this.rate = rate;
    }

    public void addRating(int rate){
       this.rate.add(rate);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    // METHODS

    public float CalculateRating(){

        // calculate

        float result = 0;

        for(int i=0; i<this.rate.size(); i++){

            result += this.rate.get(i);

        }

        return (result / this.rate.size());

    }

    public int GetTotalScore(){

        int result = 0;

        for(int i=0; i<this.rate.size(); i++){

            result += this.rate.get(i);

        }
        return result;


    }


}
