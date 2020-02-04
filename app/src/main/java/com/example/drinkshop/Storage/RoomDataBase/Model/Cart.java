package com.example.drinkshop.Storage.RoomDataBase.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "Cart")
public class Cart {

    @NonNull
    @PrimaryKey(autoGenerate = true )
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "link")
    public String link;

    @ColumnInfo(name = "amount")
    public int amount;

    @ColumnInfo(name = "price")
    public double price;

    @ColumnInfo(name = "size")
    public int size;

    @ColumnInfo(name = "sugar")
    public int sugar ;

    @ColumnInfo(name = "ice")
    public int ice ;

    @ColumnInfo(name = "toppingExtras")
    public String toppingExtras;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return id == cart.id &&
                amount == cart.amount &&
                Double.compare(cart.price, price) == 0 &&
                size == cart.size &&
                sugar == cart.sugar &&
                ice == cart.ice &&
                Objects.equals(name, cart.name) &&
                Objects.equals(link, cart.link) &&
                Objects.equals(toppingExtras, cart.toppingExtras);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, link, amount, price, size, sugar, ice, toppingExtras);
    }
}
