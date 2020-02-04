package com.example.drinkshop.Storage.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.drinkshop.Model.Category;
import com.example.drinkshop.Model.User;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "shared_user";
    private static final String SHARED_PREF_NAME2 = "shared_category";
    private static final String SHARED_PREF_NAME3 = "Dark_mode";
    private static SharedPrefManager mInstance;
    private Context context;

    public SharedPrefManager(Context context) {
        this.context = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void saveUser(User user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("Phone", user.getPhone());
        editor.putString("Name", user.getName());
        editor.putString("Birthdate", user.getBirthdate());
        editor.putString("Address", user.getAddress());
        editor.putString("Avatar",user.getAvatar());

        editor.apply();
    }

    public boolean isLogin() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (sharedPreferences.getString("Phone", "").isEmpty()) {
            return false;
        }
        return true;

    }

    public User getUser() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        User user = new User(
                sharedPreferences.getString("Phone", ""),
                sharedPreferences.getString("Name", ""),
                sharedPreferences.getString("Birthdate", ""),
                sharedPreferences.getString("Address", ""),
                sharedPreferences.getString("Avatar","")
        );
        return user;
    }

    public void saveCategory(Category category) {
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(SHARED_PREF_NAME2, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences1.edit();

        editor.putInt("ID", category.getID());
        editor.putString("Name_category", category.getName());
        editor.putString("Link", category.getLink());

        editor.apply();
    }

    public Category getCategory(){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(SHARED_PREF_NAME2, Context.MODE_PRIVATE);

        Category category = new Category(
                sharedPreferences1.getInt("ID", -1),
                sharedPreferences1.getString("Name_category", ""),
                sharedPreferences1.getString("Link", "")
        );
        return category;
    }

    public void logOut(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.apply();
    }

    public void isDark(Boolean state){
        SharedPreferences sharedPreferences2 = context.getSharedPreferences(SHARED_PREF_NAME3,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences2.edit();

        editor.putBoolean("Theme",state);
        editor.commit();
    }

    public boolean getDark(){
        SharedPreferences sharedPreferences2 = context.getSharedPreferences(SHARED_PREF_NAME3,Context.MODE_PRIVATE);
        Boolean state = sharedPreferences2.getBoolean("Theme",false);
        return state;
    }
}
