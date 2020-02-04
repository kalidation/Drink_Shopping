package com.example.drinkshop.Storage.RoomDataBase.Local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.drinkshop.Storage.RoomDataBase.Model.Cart;
import com.example.drinkshop.Storage.RoomDataBase.Model.Favorite;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface FavoriteDAO {

    @Query("SELECT * FROM Favorite")
    Flowable<List<Favorite>> getFavoriteItem();

    @Query("SELECT EXISTS (SELECT 1 FROM Favorite WHERE id=:itemId)")
    int isFavorite(int itemId);

    @Delete
    void delete(Favorite favorite);

    @Insert
    void insertToCart(Favorite...favorites);

}
