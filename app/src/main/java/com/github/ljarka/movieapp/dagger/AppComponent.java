package com.github.ljarka.movieapp.dagger;

import com.github.ljarka.movieapp.detail.DetailActivity;
import com.github.ljarka.movieapp.listing.ListingActivity;
import com.github.ljarka.movieapp.search.SearchActivity;

import dagger.Component;

@Component(modules = NetworkModule.class)
public interface AppComponent {

    void inject(ListingActivity listingActivity);

    void inject(DetailActivity detailActivity);

    void inject(SearchActivity searchActivity);
}
