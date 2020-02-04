package com.example.drinkshop.Model;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse {

    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message ;
    @SerializedName("user")
    private User user;

    public RegisterResponse(boolean error, String message, User user) {
        this.error = error;
        this.message = message;
        this.user = user;
    }

    public RegisterResponse() {
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
