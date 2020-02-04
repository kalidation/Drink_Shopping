package com.example.drinkshop.Model;

import com.google.gson.annotations.SerializedName;

public class Store {

    @SerializedName("ID")
    private String ID;
    @SerializedName("Store_Name")
    private String Name;
    @SerializedName("Lat")
    private String lat;
    @SerializedName("Ling")
    private String ling;
    @SerializedName("distance_in_km")
    private String distance;


    public Store(String ID, String name, String lat, String ling, String distance) {
        this.ID = ID;
        Name = name;
        this.lat = lat;
        this.ling = ling;
        this.distance = distance;
    }

    public Store() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLing() {
        return ling;
    }

    public void setLing(String ling) {
        this.ling = ling;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
