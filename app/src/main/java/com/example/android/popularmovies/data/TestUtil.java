package com.example.android.popularmovies.data;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Precious on 7/25/2017.
 */

public class TestUtil {

    public static void insertFakeData(SQLiteDatabase db) {
        if (db == null) {
            return;
        }
        //create a list of fake favourite Movies
        List<ContentValues> list = new ArrayList<>();

        ContentValues cv = new ContentValues();
        cv.put(FavouriteMovieContract.MovieEntry.COLUMN_MOVIE_NAME, "John Wick");
        cv.put(FavouriteMovieContract.MovieEntry.COLUMN_MOVIE_ID, 12);
        list.add(cv);

        cv = new ContentValues();
        cv.put(FavouriteMovieContract.MovieEntry.COLUMN_MOVIE_NAME, "Timmy");
        cv.put(FavouriteMovieContract.MovieEntry.COLUMN_MOVIE_ID, 2);
        list.add(cv);

        cv = new ContentValues();
        cv.put(FavouriteMovieContract.MovieEntry.COLUMN_MOVIE_NAME, "Jessica returns Home");
        cv.put(FavouriteMovieContract.MovieEntry.COLUMN_MOVIE_ID, 99);
        list.add(cv);

        cv = new ContentValues();
        cv.put(FavouriteMovieContract.MovieEntry.COLUMN_MOVIE_NAME, "Larry Page is the Boss");
        cv.put(FavouriteMovieContract.MovieEntry.COLUMN_MOVIE_ID, 1);
        list.add(cv);

        cv = new ContentValues();
        cv.put(FavouriteMovieContract.MovieEntry.COLUMN_MOVIE_NAME, "Kim Jong Un");
        cv.put(FavouriteMovieContract.MovieEntry.COLUMN_MOVIE_ID, 45);
        list.add(cv);

        //insert all guests in one transaction
        try {
            db.beginTransaction();
            //clear the table first
            db.delete(FavouriteMovieContract.MovieEntry.TABLE_NAME, null, null);
            //go through the list and add one by one
            for (ContentValues c : list) {
                db.insert(FavouriteMovieContract.MovieEntry.TABLE_NAME, null, c);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            //too bad :(
        } finally {
            db.endTransaction();
        }

    }
}

