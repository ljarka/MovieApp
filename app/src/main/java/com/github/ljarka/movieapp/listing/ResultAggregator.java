package com.github.ljarka.movieapp.listing;


import java.util.ArrayList;
import java.util.List;

public class ResultAggregator {

    private String response;
    private List<MovieListingItem> movieItems = new ArrayList<>();
    private int totalItemsResult;

    public void setTotalItemsResult(int totalItemsResult) {
        this.totalItemsResult = totalItemsResult;
    }

    public void addNewItems(List<MovieListingItem> newItems) {
        movieItems.addAll(newItems);
    }

    public List<MovieListingItem> getMovieItems() {
        return movieItems;
    }

    public int getTotalItemsResult() {
        return totalItemsResult;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
