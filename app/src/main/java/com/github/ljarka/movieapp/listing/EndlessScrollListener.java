package com.github.ljarka.movieapp.listing;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public class EndlessScrollListener extends RecyclerView.OnScrollListener {

    private static final double PAGE_SIZE = 10;

    private int totalItemsNumber;

    private LinearLayoutManager layoutManager;
    private boolean isLoading;
    private OnLoadNextPageListener listener;
    private CurrentItemListener currentItemListener;
    private ShowOrHideCounter showOrHideCounter;
    private boolean isCounterShown;

    public EndlessScrollListener(LinearLayoutManager layoutManager,
                                 OnLoadNextPageListener listener) {
        this.layoutManager = layoutManager;
        this.listener = listener;
    }

    public void setShowOrHideCounter(ShowOrHideCounter showOrHideCounter) {
        this.showOrHideCounter = showOrHideCounter;
    }

    public void setCurrentItemListener(CurrentItemListener currentItemListener) {
        this.currentItemListener = currentItemListener;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int alreadyLoadedItems = layoutManager.getItemCount();
        int currentPage = (int) Math.ceil(alreadyLoadedItems / PAGE_SIZE);
        double numberAllOfPages = Math.ceil(totalItemsNumber / PAGE_SIZE);
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition() + 1;

        if (currentPage < numberAllOfPages && lastVisibleItemPosition == alreadyLoadedItems && !isLoading) {
            loadNextPage(++currentPage);
            isLoading = true;
        }

        if (currentItemListener != null) {
            currentItemListener.onNewCurrentItem(lastVisibleItemPosition, totalItemsNumber);
        }
        Log.d("result", "lastVisibleItemPostion " + lastVisibleItemPosition);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if (showOrHideCounter != null) {

            if (isCounterShown && newState == RecyclerView.SCROLL_STATE_IDLE) {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showOrHideCounter.hideCounter();
                        isCounterShown = false;
                    }
                }, 3000);

            } else if (!isCounterShown && newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                showOrHideCounter.showCounter();
                isCounterShown = true;
            }
        }
    }

    private void loadNextPage(int pageNumber) {
        listener.loadNextPage(pageNumber);
    }

    public void setTotalItemsNumber(int totalItemsNumber) {
        this.totalItemsNumber = totalItemsNumber;
        isLoading = false;
    }
}
