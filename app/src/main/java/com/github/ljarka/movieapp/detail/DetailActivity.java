package com.github.ljarka.movieapp.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.ljarka.movieapp.MovieApplication;
import com.github.ljarka.movieapp.R;
import com.github.ljarka.movieapp.detail.gallery.GalleryActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;
import retrofit2.Retrofit;

@RequiresPresenter(DetailPresenter.class)
public class DetailActivity extends NucleusAppCompatActivity<DetailPresenter> {
    private static final String ID_KEY = "id_key";
    private Disposable subscribe;

    @BindView(R.id.poster)
    ImageView poster;

    @Inject
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        String imdbId = getIntent().getStringExtra(ID_KEY);

        MovieApplication movieApplication = (MovieApplication) getApplication();
        movieApplication.getAppComponent().inject(this);
        getPresenter().setRetrofit(retrofit);

        subscribe = getPresenter().loadDetail(imdbId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::success, this::error);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscribe != null) {
            subscribe.dispose();
        }
    }

    private void success(MovieItem movieItem) {
        Glide.with(this).load(movieItem.getPoster()).into(poster);

        poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GalleryActivity.startActivity(DetailActivity.this, movieItem.getPoster(), poster);
            }
        });
    }

    private void error(Throwable throwable) {

    }

    public static Intent createIntent(Context context, String imdID) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(ID_KEY, imdID);
        return intent;
    }
}
