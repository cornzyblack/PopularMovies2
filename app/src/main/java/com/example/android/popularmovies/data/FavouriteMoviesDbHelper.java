package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Precious on 7/25/2017.
 */

public class FavouriteMoviesDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "favourite.db";
    private static final int DATABASE_VERSION = 3;

    /**
     * @param context
     */
    public FavouriteMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVOURITE_TABLE = "CREATE TABLE" +
                FavouriteMovieContract.MovieEntry.TABLE_NAME + " (" +
                FavouriteMovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavouriteMovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL" +
                FavouriteMovieContract.MovieEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL" +
                ");";

        db.execSQL(SQL_CREATE_FAVOURITE_TABLE);
    }

    // Drop table if there is a change in schema
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavouriteMovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
