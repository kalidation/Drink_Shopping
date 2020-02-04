package com.example.drinkshop.Model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("Phone")
    private String Phone ;
    @SerializedName("Name")
    private String Name ;
    @SerializedName("Birthdate")
    private String Birthdate ;
    @SerializedName("Address")
    private String Address  ;
    @SerializedName("Avatar")
    private String Avatar;


    public User(String phone, String name, String birthdate, String address ,String avatar) {
        Phone = phone;
        Name = name;
        Birthdate = birthdate;
        Address = address;
        Avatar = avatar;
    }

    public User() {
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBirthdate() {
        return Birthdate;
    }

    public void setBirthdate(String birthdate) {
        Birthdate = birthdate;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        this.Avatar = avatar;
    }
}
