package com.example.drinkshop.Model;

import com.google.gson.annotations.SerializedName;

public class Tocken  {

    @SerializedName("Phone")
    private String phone;

    @SerializedName("Token")
    private String token;

    @SerializedName("isServer")
    private String isServer;


    public Tocken(String phone, String token, String isServer) {
        this.phone = phone;
        this.token = token;
        this.isServer = isServer;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIsServer() {
        return isServer;
    }

    public void setIsServer(String isServer) {
        this.isServer = isServer;
    }
}
