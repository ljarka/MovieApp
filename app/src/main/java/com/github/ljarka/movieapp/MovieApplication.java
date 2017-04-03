package com.github.ljarka.movieapp;

import android.app.Application;

import com.facebook.stetho.Stetho;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieApplication extends Application implements RetrofitProvider {
    private Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        retrofit = new Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://www.omdbapi.com")
                .build();
    }

    @Override
    public Retrofit provideRetrofit() {
        return retrofit;
    }
}
