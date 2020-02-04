package com.example.drinkshop.Storage.RoomDataBase.Local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.drinkshop.Storage.RoomDataBase.Model.Cart;
import com.example.drinkshop.Storage.RoomDataBase.Model.Favorite;


@Database(entities = {Cart.class,Favorite.class}, version = 3 , exportSchema = false)
public abstract class DrinkShopDataBase extends RoomDatabase {

    public abstract CartDAO cartDAO();
    public abstract FavoriteDAO favoriteDAO();

    private static DrinkShopDataBase instance;

    public static DrinkShopDataBase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context,DrinkShopDataBase.class,"Khaled_DrinkShop")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
