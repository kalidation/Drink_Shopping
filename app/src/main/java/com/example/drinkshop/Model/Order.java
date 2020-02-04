package com.example.drinkshop.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Order  {

    @SerializedName("OrderId")
    private String OrderId;

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

    public Order(String OrderId, String status, String detail, String price, String comment, String address, String phone) {
        this.OrderId = OrderId;
        Status = status;
        Detail = detail;
        Price = price;
        Comment = comment;
        Address = address;
        Phone = phone;
    }

    public String getOrderid() {
        return OrderId;
    }

    public String getStatus() {
        return Status;
    }

    public String getDetail() {
        return Detail;
    }

    public String getPrice() {
        return Price;
    }

    public String getComment() {
        return Comment;
    }

    public String getAddress() {
        return Address;
    }

    public String getPhone() {
        return Phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(OrderId, order.OrderId) &&
                Objects.equals(Status, order.Status) &&
                Objects.equals(Detail, order.Detail) &&
                Objects.equals(Price, order.Price) &&
                Objects.equals(Comment, order.Comment) &&
                Objects.equals(Address, order.Address) &&
                Objects.equals(Phone, order.Phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(OrderId, Status, Detail, Price, Comment, Address, Phone);
    }
}
