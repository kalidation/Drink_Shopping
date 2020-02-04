package com.example.drinkshop.Storage.RoomDataBase.Local;

import com.example.drinkshop.Storage.RoomDataBase.DataSource.IFavoriteDataSource;
import com.example.drinkshop.Storage.RoomDataBase.Model.Favorite;

import java.util.List;

import io.reactivex.Flowable;

public class FavoriteDataSource implements IFavoriteDataSource {

    private FavoriteDAO favoriteDAO;
    private static FavoriteDataSource instance;

    public FavoriteDataSource(FavoriteDAO favoriteDAO) {
        this.favoriteDAO = favoriteDAO;
    }

    public static FavoriteDataSource getInstance(FavoriteDAO favoriteDAO){
        if(instance == null){
            instance = new FavoriteDataSource(favoriteDAO);
        }
        return instance;
    }

    @Override
    public Flowable<List<Favorite>> getFavoriteItem() {
        return favoriteDAO.getFavoriteItem();
    }

    @Override
    public int isFavorite(int itemId) {
        return favoriteDAO.isFavorite(itemId);
    }

    @Override
    public void delete(Favorite favorite) {
        favoriteDAO.delete(favorite);
    }

    @Override
    public void insertToCart(Favorite... favorites) {
        favoriteDAO.insertToCart(favorites);
    }


}
