package com.example.drinkshop.Model;

import com.google.gson.annotations.SerializedName;

public class OrderResponse {

    @SerializedName("OrderId")
    private String OrderId;

    @SerializedName("OrderDate")
    private String OrderDate;

    @SerializedName("Status")
    private String Status;

    @SerializedName("Detail")
    private String Detail ;

    @SerializedName("Price")
    private String Price ;

    @SerializedName("Comment")
    private String Comment;

    @SerializedName("Address")
    private String Address;

    @SerializedName("Phone")
    private String Phone;

    @SerializedName("PaymentMethod")
    private String Payment;

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
