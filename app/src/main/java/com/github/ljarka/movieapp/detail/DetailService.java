package com.github.ljarka.movieapp.detail;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DetailService {
    @GET("/")
    Observable<MovieItem> getDetailInfo(@Query("i") String id);
}
