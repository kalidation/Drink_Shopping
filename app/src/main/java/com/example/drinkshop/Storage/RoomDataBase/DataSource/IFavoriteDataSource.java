package com.example.drinkshop.Storage.RoomDataBase.DataSource;

import androidx.room.Delete;
import androidx.room.Query;

import com.example.drinkshop.Storage.RoomDataBase.Model.Favorite;

import java.util.List;

import io.reactivex.Flowable;

public interface IFavoriteDataSource {

    Flowable<List<Favorite>> getFavoriteItem();
    int isFavorite(int itemId);
    void delete(Favorite favorite);
    void insertToCart(Favorite...favorites);

}
