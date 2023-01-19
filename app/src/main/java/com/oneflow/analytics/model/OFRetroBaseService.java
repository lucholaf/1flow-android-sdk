/*
 *  Copyright 2021 1Flow, Inc.
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.oneflow.analytics.model;

import com.oneflow.analytics.utils.OFConstants;
import com.oneflow.analytics.utils.OFHelper;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OFRetroBaseService {

    private static Retrofit retrofit = null;

    public static String urlPrefix = "https://";


    public static String baseDomainDev = "ez37ppkkcs.eu-west-1.awsapprunner.com/api/2021-06-15/";
    //public static String baseDomainDev = "api-sdk.1flow.app";
   // public static String baseDomainProd = "1flow.app/api/";
    //public static String baseDomainProd = "api.1flow.app/";
    //public static String baseDomainProd = "api.1flow.app/";
    //public static String baseDomainProd = "y33xx6sddf.eu-west-1.awsapprunner.com/api/2021-06-15/";
    public static String baseDomainProd = "api-sdk.1flow.app/api/2021-06-15/";

    public static final String BASE_URL = urlPrefix+ (OFConstants.MODE.equalsIgnoreCase("dev") ?baseDomainDev:baseDomainProd);

    public static Retrofit getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //.readTimeout(120, TimeUnit.SECONDS)
        //.addInterceptor(interceptor)
        OkHttpClient clientDev = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        OkHttpClient clientProd = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        OFHelper.v("APIClient","BaseUrl ["+BASE_URL+"]");
        //.client(OFConstants.MODE.equalsIgnoreCase("dev")?clientDev:clientProd)
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(OFConstants.MODE.equalsIgnoreCase("prod")?clientProd:clientDev)
                .build();


        return retrofit;
    }

}
