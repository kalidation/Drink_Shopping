package com.example.drinkshop.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BannerResponse {

    @SerializedName("error")
    private String error;
    @SerializedName("banners")
    private List<Banner> banner;

    public BannerResponse(String error, List<Banner> banner) {
        this.error = error;
        this.banner = banner;
    }

    public BannerResponse() {
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<Banner> getBanner() {
        return banner;
    }

    public void setBanner(List<Banner> banner) {
        this.banner = banner;
    }
}
