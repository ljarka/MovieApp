package com.github.ljarka.movieapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDatabaseOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "my_movies.db";

    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + MovieTableContract.TABLE_NAME;

    private static final String SQL_CREATE_MOVIE_TABLE =
            "CREATE TABLE " + MovieTableContract.TABLE_NAME +
                    " ("
                    + MovieTableContract.COLUMN_TITLE + " TEXT, "
                    + MovieTableContract.COLUMN_TYPE + " TEXT, "
                    + MovieTableContract.COLUMN_POSTER + " TEXT, "
                    + MovieTableContract.COLUMN_YEAR + " TEXT)";

    public MovieDatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL(SQL_DROP_TABLE);
            onCreate(db);
        }
    }
}
