package com.github.ljarka.movieapp.detail;

import io.reactivex.Observable;
import nucleus.presenter.Presenter;
import retrofit2.Retrofit;

public class DetailPresenter extends Presenter<DetailActivity> {
    private Retrofit retrofit;

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public Observable<MovieItem> loadDetail(String imdbId) {
        DetailService detailService = retrofit.create(DetailService.class);
        return detailService.getDetailInfo(imdbId);
    }
}
