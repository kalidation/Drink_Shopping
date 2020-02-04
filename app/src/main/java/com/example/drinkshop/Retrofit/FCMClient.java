package com.example.drinkshop.Retrofit;

import com.example.drinkshop.Utils.Common;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class FCMClient {

    private static final String BASE_URL = Common.FCM_URL;
    private static FCMClient mInstance;
    private Retrofit retrofit;

    public FCMClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized FCMClient getInstance() {
        if (mInstance == null) {
            mInstance = new FCMClient();
        }
        return mInstance;
    }

    public IFCService getApi() {
        return retrofit.create(IFCService.class);
    }

}
