package com.github.ljarka.movieapp.search;

public class SimpleMovieItem {

    private String imdbID;
    private String poster;

    public SimpleMovieItem(String imdbID, String poster) {
        this.imdbID = imdbID;
        this.poster = poster;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getPoster() {
        return poster;
    }
}
