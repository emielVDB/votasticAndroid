package com.vandenbussche.emiel.projectsbp.api;

import android.content.Context;

import com.vandenbussche.emiel.projectsbp.auth.Contract;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Stijn on 21/09/2016.
 */
public class ApiHelper {

    private static Retrofit RETROFIT_INSTANCE;
    private static final String BASE_URL = Contract.BASEURL;
    private static IVotasticApiService API_SERVICE;

    private static Object lock1 = new Object();
    private static Object lock2 = new Object();
    private static Object lock4 = new Object();
    private static Object lock5 = new Object();

    public static IVotasticApiService getApiService() {
        if(API_SERVICE == null) {
            synchronized (lock2) {
                if(API_SERVICE == null) {
                    API_SERVICE = ApiHelper
                            .getRetrofitInstance()
                            .create(IVotasticApiService.class);
                }
            }
        }
        return API_SERVICE;
    }

    private static Retrofit getRetrofitInstance() {
        if(RETROFIT_INSTANCE == null) {

            synchronized (lock1) {
                if(RETROFIT_INSTANCE == null) {
                    OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new LoggingInterceptor()).build();

                    RETROFIT_INSTANCE = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(okHttpClient)
                            .build();
                }
            }

        }
        return RETROFIT_INSTANCE;
    }

    //nieuw
    public static IVotasticApiService getApiService(Context context) {
        if(API_SERVICE == null) {
            synchronized (lock5) {
                if(API_SERVICE == null) {
                    API_SERVICE = ApiHelper
                            .getRetrofitInstance(context)
                            .create(IVotasticApiService.class);
                }
            }
        }
        return API_SERVICE;
    }

    public static Retrofit getRetrofitInstance(Context context) {
        if(RETROFIT_INSTANCE == null) {
            synchronized (lock4) {
                if(RETROFIT_INSTANCE == null) {
                    OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
                    okBuilder.addInterceptor(new SyncInterceptor(context));
                    okBuilder.authenticator(new TokenAuthenticator(context));
                    okBuilder.connectTimeout(160, TimeUnit.SECONDS);
                    okBuilder.readTimeout(160, TimeUnit.SECONDS);
                    okBuilder.writeTimeout(160, TimeUnit.SECONDS);
                    OkHttpClient okHttpClient = okBuilder.build();


                    RETROFIT_INSTANCE = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(okHttpClient)
                            .build();
                }
            }
        }
        return RETROFIT_INSTANCE;
    }
}
