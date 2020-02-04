package com.example.drinkshop.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("message_id")
    @Expose
    public String message_id;

    public String getMessage_id() {
        return message_id;
    }
}
