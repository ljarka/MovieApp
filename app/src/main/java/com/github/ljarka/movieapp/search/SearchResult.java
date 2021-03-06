package com.github.ljarka.movieapp.search;

import com.github.ljarka.movieapp.listing.MovieListingItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResult {

    @SerializedName("Search")
    private List<MovieListingItem> items;

    private String totalResults;

    @SerializedName("Response")
    private String response;

    public List<MovieListingItem> getItems() {
        return items;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public String getResponse() {
        return response;
    }

    public void setItems(List<MovieListingItem> items) {
        this.items = items;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }
}
