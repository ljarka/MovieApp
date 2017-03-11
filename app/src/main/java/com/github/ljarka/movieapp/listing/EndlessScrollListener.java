package com.github.ljarka.movieapp.listing;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public class EndlessScrollListener extends RecyclerView.OnScrollListener {

    private LinearLayoutManager layoutManager;

    public EndlessScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        Log.d("result", "lastVisibleItemPostion " + lastVisibleItemPosition);
    }
}
