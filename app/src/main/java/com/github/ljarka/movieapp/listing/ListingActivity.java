package com.github.ljarka.movieapp.listing;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.github.ljarka.movieapp.MovieApplication;
import com.github.ljarka.movieapp.MovieDatabaseOpenHelper;
import com.github.ljarka.movieapp.MovieTableContract;
import com.github.ljarka.movieapp.R;
import com.github.ljarka.movieapp.detail.DetailActivity;
import com.github.ljarka.movieapp.search.SearchResult;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;
import retrofit2.Retrofit;

@RequiresPresenter(ListingPresenter.class)
public class ListingActivity extends NucleusAppCompatActivity<ListingPresenter> implements CurrentItemListener, ShowOrHideCounter, OnMovieItemClickListener, OnLikeButtonClickListener {

    private static final String SEARCH_TITLE = "search_title";
    private static final String SEARCH_YEAR = "search_year";
    private static final String SEARCH_TYPE = "search_type";
    public static final int NO_YEAR_SELECTED = -1;

    private MoviesListAdapter adapter;

    @BindView(R.id.view_flipper)
    ViewFlipper viewFlipper;

    @BindView(R.id.no_internet_image_view)
    ImageView noInternetImage;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.no_results)
    FrameLayout noResults;

    @BindView(R.id.counter)
    TextView counter;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private EndlessScrollListener endlessScrollListener;
    private MovieDatabaseOpenHelper movieDatabaseOpenHelper;

    @Inject
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);
        ButterKnife.bind(this);
        MovieApplication movieApplication = (MovieApplication) getApplication();
        movieApplication.getAppComponent().inject(this);

        movieDatabaseOpenHelper = new MovieDatabaseOpenHelper(this);
        getPresenter().setRetrofit(retrofit);

        String title = getIntent().getStringExtra(SEARCH_TITLE);
        int year = getIntent().getIntExtra(SEARCH_YEAR, NO_YEAR_SELECTED);
        String type = getIntent().getStringExtra(SEARCH_TYPE);

        adapter = new MoviesListAdapter();
        adapter.setOnMovieItemClickListener(this);
        adapter.setOnLikeButtonClickListener(this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        endlessScrollListener = new EndlessScrollListener(layoutManager, getPresenter());
        recyclerView.addOnScrollListener(endlessScrollListener);
        endlessScrollListener.setCurrentItemListener(this);
        endlessScrollListener.setShowOrHideCounter(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startLoading(title, year, type);
            }
        });

        startLoading(title, year, type);
    }

    private void startLoading(String title, int year, String type) {
        getPresenter().startLoadingItems(title, year, type);
    }

    private void error(Throwable throwable) {
        swipeRefreshLayout.setRefreshing(false);
        viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(noInternetImage));
    }

    public void appendItems(SearchResult searchResult) {
        adapter.addItems(searchResult.getItems());
        endlessScrollListener.setTotalItemsNumber(Integer.parseInt(searchResult.getTotalResults()));
    }

    private void success(ResultAggregator resultAggregator) {
        swipeRefreshLayout.setRefreshing(false);
        if ("false".equalsIgnoreCase(resultAggregator.getResponse())) {
            viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(noResults));
        } else {
            viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(swipeRefreshLayout));
            adapter.setItems(resultAggregator.getMovieItems());
            endlessScrollListener.setTotalItemsNumber(resultAggregator.getTotalItemsResult());
        }
    }

    public static Intent createIntent(Context context, String title, int year, String type) {
        Intent intent = new Intent(context, ListingActivity.class);
        intent.putExtra(SEARCH_TITLE, title);
        intent.putExtra(SEARCH_YEAR, year);
        intent.putExtra(SEARCH_TYPE, type);
        return intent;
    }

    @Override
    public void onNewCurrentItem(int currentItem, int totalItemsCount) {
        counter.setText(currentItem + "/" + totalItemsCount);
    }

    @Override
    public void showCounter() {
        counter.setVisibility(View.VISIBLE);
        counter.animate().translationX(0).start();
    }

    @Override
    public void hideCounter() {
        counter.animate().translationX(counter.getWidth() * 2).start();
    }

    @Override
    public void onMovieItemClick(String imdbID) {
        startActivity(DetailActivity.createIntent(this, imdbID));
    }

    public void setNewAggregatorResult(ResultAggregator newAggregatorResult) {
        swipeRefreshLayout.setRefreshing(false);
        success(newAggregatorResult);
    }

    @Override
    public void onLikeButtonClick(MovieListingItem movieItem) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieTableContract.COLUMN_TITLE, movieItem.getTitle());
        contentValues.put(MovieTableContract.COLUMN_YEAR, movieItem.getYear());
        contentValues.put(MovieTableContract.COLUMN_POSTER, movieItem.getPoster());
        contentValues.put(MovieTableContract.COLUMN_TYPE, movieItem.getType());
        movieDatabaseOpenHelper.getWritableDatabase()
                .insert(MovieTableContract.TABLE_NAME, null, contentValues);
    }
}
