package com.example.drinkshop.Retrofit;

import com.example.drinkshop.Model.AvatarResponse;
import com.example.drinkshop.Model.BannerResponse;
import com.example.drinkshop.Model.CategoryResponse;
import com.example.drinkshop.Model.DrinkResponse;
import com.example.drinkshop.Model.OrderResponse;
import com.example.drinkshop.Model.OrdersResponse;
import com.example.drinkshop.Model.RegisterResponse;
import com.example.drinkshop.Model.Store;
import com.example.drinkshop.Model.Tocken;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DrinkShopApi {

    @FormUrlEncoded
    @POST("register.php")
    Call<RegisterResponse> register(
            @Field("Phone") String Phone,
            @Field("Password") String Password,
            @Field("Name") String Name,
            @Field("Birthdate") String Birthdate,
            @Field("Address") String Address
    );

    @FormUrlEncoded
    @POST("login.php")
    Call<RegisterResponse> login(
            @Field("Phone") String Phone,
            @Field("Password") String Password
    );

    @FormUrlEncoded
    @POST("getDrink.php")
    Observable<DrinkResponse> getDrink(@Field("MenuID") String MenuID);

    @GET("getBanner.php")
    Observable<BannerResponse> getBanner();

    @GET("getMenu.php")
    Observable<CategoryResponse> getCategory();

    //@Multipart
    //@POST("avatar.php")
    //Call<AvatarResponse> uploadavatar(@Part MultipartBody.Part Phone , @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("avatar.php")
    Call<AvatarResponse> uploadavatar(
            @Field("Phone") String Phone,
            @Field("Avatar") String Password
    );

    @GET("getAllDrink.php")
    Observable<DrinkResponse> getAlldrink();

    @FormUrlEncoded
    @POST("insertOrder.php")
    Call<OrderResponse> insertOrder(
            @Field("Detail") String detail,
            @Field("Price") float price,
            @Field("Comment") String comment,
            @Field("Address") String address,
            @Field("Phone") String Phone,
            @Field("PaymentMethod") String Payment
    );

    @FormUrlEncoded
    @POST("baintree/checkout.php")
    Call<String> payment(
            @Field("nonce") String nonce,
            @Field("amount") String amount
    );

    @FormUrlEncoded
    @POST("getOrders.php")
    Observable<OrdersResponse> getOrders(
            @Field("Phone") String phone,
            @Field("Status") String status
    );

    @FormUrlEncoded
    @POST("insertToken.php")
    Call<String> isertToken(
            @Field("Phone") String phone,
            @Field("Token") String Token,
            @Field("isServer") String isServer
    );

    @FormUrlEncoded
    @POST("cancelOrder.php")
    Call<String> cancelOrder(
            @Field("Phone") String phone,
            @Field("OrderID") String OrderID
    );

    @FormUrlEncoded
    @POST("getNearbyStores.php")
    Observable<List<Store>> getNearbyStores(
            @Field("lat") String lat,
            @Field("ling") String ling
    );


    @FormUrlEncoded
    @POST("getToken.php")
    Call<Tocken> getToken(
            @Field("Phone") String phone
    );

}
