package com.oneflow.tryskysdk.model;

import com.oneflow.tryskysdk.utils.Helper;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroBaseService {

    private static Retrofit retrofit = null;

    public static String urlPrefix = "https://";
    public static String baseDomain = "dev.1flow.app/api/";

    public static final String BASE_URL = urlPrefix+ baseDomain;

    public static Retrofit getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        Helper.v("APIClient","BaseUrl ["+BASE_URL+"]");
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)

                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();


        return retrofit;
    }

}
