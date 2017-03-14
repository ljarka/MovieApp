package com.github.ljarka.movieapp.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.ljarka.movieapp.R;
import com.github.ljarka.movieapp.listing.OnMovieItemClickListener;

import java.util.Collections;
import java.util.List;

import static butterknife.ButterKnife.findById;

public class PosterRecyclerViewAdapter extends RecyclerView.Adapter<PosterRecyclerViewAdapter.ViewHolder> {

    private List<SimpleMovieItem> simpleMovieItems = Collections.emptyList();
    private OnMovieItemClickListener onMovieItemClickListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.poster_layout, parent, false);
        return new ViewHolder(layout);
    }

    public void setOnMovieItemClickListener(OnMovieItemClickListener onMovieItemClickListener) {
        this.onMovieItemClickListener = onMovieItemClickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(holder.posterImageView.getContext()).load(simpleMovieItems.get(position).getPoster()).into(holder.posterImageView);
        holder.posterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onMovieItemClickListener != null) {
                    onMovieItemClickListener.onMovieItemClick(simpleMovieItems.get(position).getImdbID());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return simpleMovieItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView posterImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            posterImageView = findById(itemView, R.id.search_poster);
        }
    }

    public void setSimpleMovieItems(List<SimpleMovieItem> simpleMovieItems) {
        this.simpleMovieItems = simpleMovieItems;
        notifyDataSetChanged();
    }
}
