package com.example.drinkshop.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DrinkResponse {

    @SerializedName("error")
    private String error;
    @SerializedName("drinks")
    private List<Drink> drinks;

    public DrinkResponse(String error, List<Drink> drinks) {
        this.error = error;
        this.drinks = drinks;
    }

    public DrinkResponse() {
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<Drink> getDrinks() {
        return drinks;
    }

    public void setDrinks(List<Drink> drinks) {
        this.drinks = drinks;
    }
}
