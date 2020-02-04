package com.example.drinkshop.Retrofit;

import com.example.drinkshop.Utils.Common;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitScalarsClient {

    private static final String BASE_URL = Common.BASE_URL;
    private static RetrofitScalarsClient mInstance;
    private Retrofit retrofit;

    public RetrofitScalarsClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitScalarsClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitScalarsClient();
        }
        return mInstance;
    }

    public DrinkShopApi getApi() {
        return retrofit.create(DrinkShopApi.class);
    }

}


