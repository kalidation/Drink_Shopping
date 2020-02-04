package com.example.drinkshop.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrdersResponse  {

    @SerializedName("error")
    private String message;
    @SerializedName("Orders")
    private List<Order> orders;

    public OrdersResponse(String message, List<Order> orders) {
        this.message = message;
        this.orders = orders;
    }

    public OrdersResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}

