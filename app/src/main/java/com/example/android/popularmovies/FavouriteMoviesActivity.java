package com.example.android.popularmovies;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.popularmovies.adapters.FavouriteMoviesAdapter;
import com.example.android.popularmovies.data.FavouriteMovieContract;
import com.example.android.popularmovies.data.FavouriteMovieDbHelper;

/**
 * Created by Precious on 7/22/2017.
 */

public class FavouriteMoviesActivity extends AppCompatActivity {
    private SQLiteDatabase mDb;
    private FavouriteMoviesAdapter mAdapter;
    private RecyclerView favouriteRecyclerView;
    private LinearLayoutManager favouriteLayoutManager;
    private Cursor mCursor;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourite_activity);

        FavouriteMovieDbHelper dbHelper = new FavouriteMovieDbHelper(this);

        // Start to create a database that can be readable
        mDb = dbHelper.getWritableDatabase();
        // Get the Cursor object for the Favourite movies
        mCursor = getAllFavouriteMovies();

        favouriteRecyclerView = (RecyclerView) findViewById(R.id.rv_fav_movies);


        favouriteLayoutManager = new LinearLayoutManager(this);
        favouriteRecyclerView.setLayoutManager(favouriteLayoutManager);
        favouriteRecyclerView.setHasFixedSize(true);

        mAdapter = new FavouriteMoviesAdapter(this, mCursor);
        favouriteRecyclerView.setAdapter(mAdapter);


    }

    /**
     * Display the Favourite Movies in the order of their ID not MOVIE_ID
     *
     * @return a Cursor object for Favourite Movies
     */
    private Cursor getAllFavouriteMovies() {
        return mDb.query(FavouriteMovieContract.MovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                FavouriteMovieContract.MovieEntry._ID
        );
    }

}
