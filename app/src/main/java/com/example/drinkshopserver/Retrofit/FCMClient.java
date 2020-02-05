package com.example.drinkshopserver.Retrofit;

import com.example.drinkshopserver.utils.Common;

import retrofit2.Retrofit;
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

    public IFCMApi getApi() {
        return retrofit.create(IFCMApi.class);
    }

}
