package com.example.drinkshop.Model;

import com.google.gson.annotations.SerializedName;

public class AvatarResponse {

    @SerializedName("message")
    private String message;

    @SerializedName("AvatarName")
    private String AvatarName;

    public AvatarResponse(String message, String avatarName) {
        this.message = message;
        AvatarName = avatarName;
    }


    public AvatarResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAvatarName() {
        return AvatarName;
    }

    public void setAvatarName(String avatarName) {
        AvatarName = avatarName;
    }
}
