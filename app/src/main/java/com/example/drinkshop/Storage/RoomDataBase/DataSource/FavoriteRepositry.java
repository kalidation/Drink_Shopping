package com.example.drinkshop.Storage.RoomDataBase.DataSource;

import com.example.drinkshop.Storage.RoomDataBase.Model.Favorite;

import java.util.List;

import io.reactivex.Flowable;

public class FavoriteRepositry implements IFavoriteDataSource {

    private IFavoriteDataSource favoriteDataSource;
    private static FavoriteRepositry  instance;

    public FavoriteRepositry(IFavoriteDataSource favoriteDataSource) {
        this.favoriteDataSource = favoriteDataSource;
    }

    public static FavoriteRepositry getInstance(IFavoriteDataSource favoriteDataSource){
        if(instance == null){
            instance = new FavoriteRepositry(favoriteDataSource);
        }
        return instance;
    }

    @Override
    public Flowable<List<Favorite>> getFavoriteItem() {
        return favoriteDataSource.getFavoriteItem();
    }

    @Override
    public int isFavorite(int itemId) {
        return favoriteDataSource.isFavorite(itemId);
    }

    @Override
    public void delete(Favorite favorite) {
        favoriteDataSource.delete(favorite);
    }

    @Override
    public void insertToCart(Favorite... favorites) {
        favoriteDataSource.insertToCart(favorites);
    }
}
