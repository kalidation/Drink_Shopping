package com.example.drinkshop.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryResponse {

    @SerializedName("error")
    private String error;
    @SerializedName("menus")
    private List<Category> categories;

    public CategoryResponse(String error, List<Category> categories) {
        this.error = error;
        this.categories = categories;
    }

    public CategoryResponse() {
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
