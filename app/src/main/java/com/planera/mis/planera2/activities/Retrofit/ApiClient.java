package com.planera.mis.planera2.activities.Retrofit;

import com.planera.mis.planera2.activities.utils.AppConstants;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static Retrofit instance;
    public static Retrofit instanceForPalces;

    public static ApiInterface getInstance(){
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().
                    readTimeout(AppConstants.REQUEST_TIMEOUT, TimeUnit.SECONDS).
                    writeTimeout(AppConstants.REQUEST_TIMEOUT, TimeUnit.SECONDS).
                    connectTimeout(AppConstants.REQUEST_TIMEOUT, TimeUnit.SECONDS);

            OkHttpClient okHttpClient = clientBuilder.build();
            instance = new Retrofit.Builder()
                    .baseUrl(AppConstants.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        return  instance.create(ApiInterface.class);
    }

    public static ApiInterface getInstanceForLocation(){

            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().
                    readTimeout(AppConstants.REQUEST_TIMEOUT, TimeUnit.SECONDS).
                    writeTimeout(AppConstants.REQUEST_TIMEOUT, TimeUnit.SECONDS).
                    connectTimeout(AppConstants.REQUEST_TIMEOUT, TimeUnit.SECONDS);

            OkHttpClient okHttpClient = clientBuilder.build();
            instanceForPalces = new Retrofit.Builder()
                    .baseUrl(AppConstants.GOOGLE_PLACES_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        return  instanceForPalces.create(ApiInterface.class);
    }
}
