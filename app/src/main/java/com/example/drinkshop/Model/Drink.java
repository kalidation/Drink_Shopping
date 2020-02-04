package com.example.drinkshop.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Drink {

    @SerializedName("ID")
    private String ID;

    @SerializedName("Name")
    private String Name;

    @SerializedName("Link")
    private String Link;

    @SerializedName("Price")
    private String Price;

    @SerializedName("MenuID")
    private String MenuID;

    public Drink(String ID, String name, String link, String price, String menuID) {
        this.ID = ID;
        Name = name;
        Link = link;
        Price = price;
        MenuID = menuID;
    }

    public Drink() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getMenuID() {
        return MenuID;
    }

    public void setMenuID(String menuID) {
        MenuID = menuID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Drink drink = (Drink) o;
        return Objects.equals(ID, drink.ID) &&
                Objects.equals(Name, drink.Name) &&
                Objects.equals(Link, drink.Link) &&
                Objects.equals(Price, drink.Price) &&
                Objects.equals(MenuID, drink.MenuID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, Name, Link, Price, MenuID);
    }
}
