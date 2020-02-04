package com.example.drinkshop.Utils;

import com.example.drinkshop.Model.Category;
import com.example.drinkshop.Model.Drink;
import com.example.drinkshop.Model.User;
import com.example.drinkshop.Storage.RoomDataBase.DataSource.CartRepository;
import com.example.drinkshop.Storage.RoomDataBase.DataSource.FavoriteRepositry;
import com.example.drinkshop.Storage.RoomDataBase.Local.DrinkShopDataBase;

import java.util.ArrayList;
import java.util.List;


public class Common {

    public static final String BASE_URL ="http://www.drinkshop.com.eu.loclx.io/drinkShop/";
    public static final String  FCM_URL ="https://fcm.googleapis.com/";
    public static final String API_TOKEN_URL ="http://www.drinkshop.com.eu.loclx.io/drinkShop/baintree/main.php";

    public static User currentUser = null  ;
    public static Category currentCategory = null ;

    public static List<Drink> toppingList;
    public static List<String> toppingNames = new ArrayList<>();
    public static double toppingPrice = 0.0;
    public static int sugar = -1 ;
    public static int ice = -1 ;
    public static int size = -1 ;

    public static CartRepository cartRepository;
    public static DrinkShopDataBase dataBase;
    public static FavoriteRepositry favoriteRepositry;


}
