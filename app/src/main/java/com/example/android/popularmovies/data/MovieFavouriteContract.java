package com.example.android.popularmovies.data;

import android.provider.BaseColumns;

/**
 * Created by Precious on 7/25/2017.
 */


// Contract class for the Movie Favourites
// -------------------------------
//  ID | movie_name| movie_ID
// ----|-----------|--------------
//     |           |
//     |           |
// -------------------------------

public final class MovieFavouriteContract {

    private MovieFavouriteContract() {
    }

    /*
    Inner class that represents the Table contents of the DB
     */
    public static class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "favourite";
        public static final String COLUMN_MOVIE_NAME = "movie_name";
        public static final String COLUMN_MOVIE_ID = "movie_id";

    }
}
