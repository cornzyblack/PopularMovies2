package com.example.android.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Precious on 7/25/2017.
 */


// Contract class for the Movie Favourites
// -------------------------------
//  _ID | movie_name| movie_ID
// ----|-----------|--------------
//     |           |
//     |           |
// -------------------------------

public final class FavouriteMovieContract {

    public final static String CONTENT_SCHEME = "content://";
    // package name
    public final static String AUTHORITY = "com.example.android.popularmovies";
    public final static Uri BASE_CONTENT_URI = Uri.parse(CONTENT_SCHEME + AUTHORITY);

    // Access Favourite Table
    public static final String PATH_FAVOURITE = "favourite";

    private FavouriteMovieContract() {
    }

    /*
    Inner class that represents the Table contents of the DB
     */
    public static class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVOURITE)
                .build();

        public static final String TABLE_NAME = "favourite";
        public static final String COLUMN_MOVIE_NAME = "movie_name";
        public static final String COLUMN_MOVIE_ID = "movie_id";

    }
}
