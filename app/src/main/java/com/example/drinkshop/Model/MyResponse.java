package com.example.drinkshop.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyResponse {

    @SerializedName("multicast_id")
    public long multicast_id;
    @SerializedName("success")
    public int success;
    @SerializedName("failure")
    public int failure;
    @SerializedName("canonical_ids")
    public int canonical_ids;
    @SerializedName("results")
    public List<Result> results;

}
