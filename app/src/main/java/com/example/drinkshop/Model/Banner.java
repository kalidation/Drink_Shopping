package com.example.drinkshop.Model;

import com.google.gson.annotations.SerializedName;

public class Banner  {

    @SerializedName("ID")
    private int ID ;
    @SerializedName("Name")
    private String Name;
    @SerializedName("Link")
    private String Link;

    public Banner(int ID, String name, String link) {
        this.ID = ID;
        Name = name;
        Link = link;
    }

    public Banner() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
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
}
