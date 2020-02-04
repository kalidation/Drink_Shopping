package com.example.drinkshop.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Detail  {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("link")
    private String link;
    @SerializedName("amount")
    private int amount;
    @SerializedName("price")
    private double price;
    @SerializedName("size")
    private int size;
    @SerializedName("sugar")
    private int sugar ;
    @SerializedName("ice")
    private int ice ;
    @SerializedName("toppingExtras")
    private String toppingExtras;

    public Detail(int id, String name, String link, int amount, double price, int size, int sugar, int ice, String toppingExtras) {
        this.id = id;
        this.name = name;
        this.link = link;
        this.amount = amount;
        this.price = price;
        this.size = size;
        this.sugar = sugar;
        this.ice = ice;
        this.toppingExtras = toppingExtras;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSugar() {
        return sugar;
    }

    public void setSugar(int sugar) {
        this.sugar = sugar;
    }

    public int getIce() {
        return ice;
    }

    public void setIce(int ice) {
        this.ice = ice;
    }

    public String getToppingExtras() {
        return toppingExtras;
    }

    public void setToppingExtras(String toppingExtras) {
        this.toppingExtras = toppingExtras;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Detail detail = (Detail) o;
        return id == detail.id &&
                amount == detail.amount &&
                Double.compare(detail.price, price) == 0 &&
                size == detail.size &&
                sugar == detail.sugar &&
                ice == detail.ice &&
                Objects.equals(name, detail.name) &&
                Objects.equals(link, detail.link) &&
                Objects.equals(toppingExtras, detail.toppingExtras);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, link, amount, price, size, sugar, ice, toppingExtras);
    }
}
