package com.github.ljarka.movieapp;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.github.ljarka.movieapp.dagger.AppComponent;
import com.github.ljarka.movieapp.dagger.DaggerAppComponent;

import retrofit2.Retrofit;

public class MovieApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        appComponent = DaggerAppComponent.builder()
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
