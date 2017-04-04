package com.github.ljarka.movieapp.dagger;

import com.github.ljarka.movieapp.detail.DetailService;
import com.github.ljarka.movieapp.detail.MovieItem;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    @Provides
    public Retrofit providesRetrofit() {
        return new Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://www.omdbapi.com")
                .build();
    }

    @Provides
    public DetailService providesDetilService(Retrofit retrofit) {
        return retrofit.create(DetailService.class);
    }
}
