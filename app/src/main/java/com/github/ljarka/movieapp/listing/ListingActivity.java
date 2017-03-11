package com.github.ljarka.movieapp.listing;

import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;
import static io.reactivex.schedulers.Schedulers.io;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.github.ljarka.movieapp.R;
import com.github.ljarka.movieapp.RetrofitProvider;
import com.github.ljarka.movieapp.search.SearchResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;

@RequiresPresenter(ListingPresenter.class)
public class ListingActivity extends NucleusAppCompatActivity<ListingPresenter> implements CurrentItemListener, ShowOrHideCounter {

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

    private EndlessScrollListener endlessScrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            RetrofitProvider retrofitProvider = (RetrofitProvider) getApplication();
            getPresenter().setRetrofit(retrofitProvider.provideRetrofit());
        }

        String title = getIntent().getStringExtra(SEARCH_TITLE);
        int year = getIntent().getIntExtra(SEARCH_YEAR, NO_YEAR_SELECTED);
        String type = getIntent().getStringExtra(SEARCH_TYPE);

        adapter = new MoviesListAdapter();
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        endlessScrollListener = new EndlessScrollListener(layoutManager, getPresenter());
        recyclerView.addOnScrollListener(endlessScrollListener);
        endlessScrollListener.setCurrentItemListener(this);
        endlessScrollListener.setShowOrHideCounter(this);

        getPresenter().getDataAsync(title, year, type)
                .subscribeOn(io())
                .observeOn(mainThread())
                .subscribe(this::success, this::error);
    }

    private void error(Throwable throwable) {
        viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(noInternetImage));
    }

    public void appendItems(SearchResult searchResult) {
        adapter.addItems(searchResult.getItems());
        endlessScrollListener.setTotalItemsNumber(Integer.parseInt(searchResult.getTotalResults()));

    }

    private void success(SearchResult searchResult) {
        if ("false".equalsIgnoreCase(searchResult.getResponse())) {
            viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(noResults));
        } else {
            viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(recyclerView));
            adapter.setItems(searchResult.getItems());
            endlessScrollListener.setTotalItemsNumber(Integer.parseInt(searchResult.getTotalResults()));
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
}
