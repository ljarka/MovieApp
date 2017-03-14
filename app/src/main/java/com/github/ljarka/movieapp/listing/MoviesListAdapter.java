package com.github.ljarka.movieapp.listing;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.ljarka.movieapp.R;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;

import static butterknife.ButterKnife.findById;

public class MoviesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int GAMES_VIEW_HOLDER = 1;
    private static final int MY_VIEW_HOLDER = 2;


    private List<MovieListingItem> items = Collections.emptyList();
    private OnMovieItemClickListener onMovieItemClickListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == GAMES_VIEW_HOLDER) {
            View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_list_item, parent, false);
            return new GamesViewHolder(layout);
        } else {
            View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new MyViewHolder(layout);
        }
    }

    public void setOnMovieItemClickListener(OnMovieItemClickListener onMovieItemClickListener) {
        this.onMovieItemClickListener = onMovieItemClickListener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MovieListingItem movieListingItem = items.get(position);

        if (getItemViewType(position) == MY_VIEW_HOLDER) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            Glide.with(myViewHolder.poster.getContext()).load(movieListingItem.getPoster()).into(myViewHolder.poster);
            myViewHolder.titleAndYear.setText(movieListingItem.getTitle() + " (" + movieListingItem.getYear() + ")");
            myViewHolder.type.setText("typ: " + movieListingItem.getType());
            myViewHolder.itemView.setOnClickListener(v -> {
                if (onMovieItemClickListener != null) {
                    onMovieItemClickListener.onMovieItemClick(movieListingItem.getImdbID());
                }
            });
        } else {
            GamesViewHolder gamesViewHolder = (GamesViewHolder) holder;
            Glide.with(gamesViewHolder.poster.getContext())
                    .load(movieListingItem.getPoster()).into(gamesViewHolder.poster);
            gamesViewHolder.title.setText(movieListingItem.getTitle());
            gamesViewHolder.itemView.setOnClickListener(v -> {
                if (onMovieItemClickListener != null) {
                    onMovieItemClickListener.onMovieItemClick(movieListingItem.getImdbID());
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if ("Game".equalsIgnoreCase(items.get(position).getType())) {
            return GAMES_VIEW_HOLDER;
        } else {
            return MY_VIEW_HOLDER;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<MovieListingItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void addItems(List<MovieListingItem> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    class GamesViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView title;

        public GamesViewHolder(View itemView) {
            super(itemView);
            poster = findById(itemView, R.id.game_poster);
            title = findById(itemView, R.id.game_title);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView poster;
        TextView titleAndYear;
        TextView type;

        public MyViewHolder(View itemView) {
            super(itemView);
            poster = (ImageView) itemView.findViewById(R.id.poster);
            titleAndYear = (TextView) itemView.findViewById(R.id.title_and_year);
            type = (TextView) itemView.findViewById(R.id.type);
        }
    }
}
