package com.example.drinkshop.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Category {

    @SerializedName("ID")
    private int ID;
    @SerializedName("Name")
    private String Name ;
    @SerializedName("Link")
    private String Link ;

    public Category(int ID, String name, String link) {
        this.ID = ID;
        Name = name;
        Link = link;
    }

    public Category() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(Name, category.Name) &&
                Objects.equals(Link, category.Link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Name, Link);
    }
}
