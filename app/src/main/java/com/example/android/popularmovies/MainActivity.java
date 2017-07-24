package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.Adapters.MovieAdapter;
import com.example.android.popularmovies.Model.Movie;
import com.example.android.popularmovies.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.popularmovies.utilities.NetworkUtils.MOVIE_BASE_URL;

public class MainActivity extends AppCompatActivity
        implements MovieAdapter.ListItemClickListener {

    private final static String MOVIE_POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    private final static int POPULAR_MOVIES_LOADER = 12;

    private final static String QUERY = "api_key";

    private ProgressBar mLoadingIndicator;
    private List<Movie> movies;
    private Parcelable mSave;

    private TextView mNetworkMsgView;
    private RecyclerView moviesRecyclerView;

    GridLayoutManager layoutManager;
    public MovieAdapter movieAdapter;

    //Insert your Api key here
    // private final static String api_key = "9e34a591a62d6273d144c4c0347d0587";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //on default load the most popular movie
        setContentView(R.layout.activity_main);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mNetworkMsgView = (TextView) findViewById(R.id.tv_network_msg);

        moviesRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);

        // Check if Internet is connected
        if (isNetworkAvailable()) {
            mNetworkMsgView.setVisibility(View.INVISIBLE);
            layoutManager = new GridLayoutManager(this, 2);
            moviesRecyclerView.setLayoutManager(layoutManager);
            moviesRecyclerView.setHasFixedSize(true);

            getPopularMovies();

        } else {
            // If Network is unavailable
            mNetworkMsgView.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Network is unavailable!", Toast.LENGTH_LONG).show();
        }

    }


    //Inflates the Menu in MainActivity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_main; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        mSave = layoutManager.onSaveInstanceState();
        state.putParcelable("SAVED", mSave);
    }

    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);

        // Retrieve list state and list/item positions
        if (state != null)
            mSave = state.getParcelable("SAVED");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSave != null) {
            layoutManager.onRestoreInstanceState(mSave);

        }
    }

    //Handles what happens when an Item is selected in the Settings Group
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle the ActionBar
        int id = item.getItemId();

        //Choose any selected Item from the Settings group
        if (id == R.id.action_most_popular) {
            item.setChecked(true);
            getPopularMovies();
            Toast.makeText(this, "popular", Toast.LENGTH_SHORT).show();
            return true;

        } else if (id == R.id.action_sort_highest_rated) {
            item.setChecked(false);
            getHighestRated();
            Toast.makeText(this, "highest Sorted", Toast.LENGTH_SHORT).show();
            return true;
        }

        // Open favourites Activity
        else {
            item.setChecked(false);
            Intent favouritesIntent = new Intent(this, FavouriteMoviesActivity.class);
            startActivity(favouritesIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void getPopularMovies() {
        Bundle folderBundle = new Bundle();
        folderBundle.putString("FOLDER", "popular");
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(POPULAR_MOVIES_LOADER, folderBundle, movieLoader);
    }

    public void getHighestRated() {
        Bundle folderBundle = new Bundle();
        folderBundle.putString("FOLDER", "top_rated");
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(POPULAR_MOVIES_LOADER, folderBundle, movieLoader);
    }


    // For Movie Loader
    private LoaderManager.LoaderCallbacks<List<Movie>> movieLoader = new LoaderManager.LoaderCallbacks<List<Movie>>() {
        @Override
        public Loader<List<Movie>> onCreateLoader(int id, final Bundle args) {
            return new AsyncTaskLoader<List<Movie>>(MainActivity.this) {
                @Override
                public List<Movie> loadInBackground() {
                    String movieJsonResponse;
                    String folder = args.getString("FOLDER");
                    String baseUrl = MOVIE_BASE_URL;
                    List<Movie> movieResponse;
                    try {
                        movieJsonResponse = NetworkUtils.getMoviesResponse(baseUrl, folder, QUERY, NetworkUtils.API_KEY);
                        movieResponse = getMovies(movieJsonResponse);
                        return movieResponse;
                    } catch (IOException e) {
                        Log.d("MainActivity", "Error from NetworkUtils.getMoviesResponse");
                        return null;
                    }

                }

                @Override
                protected void onStartLoading() {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            movieAdapter = new MovieAdapter(data, MainActivity.this);
            moviesRecyclerView.setAdapter(movieAdapter);
        }

        @Override
        public void onLoaderReset(Loader<List<Movie>> loader) {

        }
    };


    /*
    *Parses the JSON Response into a List of Movies
    */
    private List<Movie> getMovies(String response) {
        movies = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(response);
            JSONArray results = root.getJSONArray("results");

            //loop through the results array and get movie objects
            for (int i = 0; i < results.length(); i++) {
                JSONObject movie = results.getJSONObject(i);
                //get the Movie Rating
                String movieRating = movie.getDouble("vote_average") + "/10";

                //Get and store the URL of the Movie poster path
                String moviePosterPath = NetworkUtils
                        .posterUrlImageBuilder(MOVIE_POSTER_BASE_URL, "w185", slashRemover(movie.getString("poster_path")));

                //Get and store the Movie Synopsis
                String movieSynopsis = movie.getString("overview");

                //Get and store the Movie Title
                String movieTitle = movie.getString("original_title");

                //Get and store the Movie release date
                String movieReleaseDate = movie.getString("release_date");

                //Get and store the Movie id
                int movieId = movie.getInt("id");

                //Create a new Movie Object and append it to the List of Movies
                movies.add(new Movie(movieRating, moviePosterPath, movieSynopsis, movieTitle, movieReleaseDate, movieId));
            }
        } catch (JSONException e) {
            Log.e("MainActivity", "Error Parsing JSON");
        }

        return movies;
    }

    @NonNull
    /*
     * helps to remove unnecessary "/" in poster path
     *
     * @param posterPath the original posterPath
     * @return the new posterPath
     */
    private String slashRemover(String posterPath) {
        StringBuilder sb = new StringBuilder(posterPath);
        sb.deleteCharAt(0);
        return sb.toString();
    }


    // For when a Movie Poster is clicked
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);

        intent.putExtra("title", movies.get(position).getMovieTitle());
        intent.putExtra("releaseDate", movies.get(position).getMovieReleaseDate());
        intent.putExtra("posterPath", movies.get(position).getMoviePosterPath());
        intent.putExtra("synopsis", movies.get(position).getMovieSynopsis());
        intent.putExtra("rating", movies.get(position).getMovieRating());
        intent.putExtra("movie_id", movies.get(position).getMovieId());
        //startActivity(intent);
        startActivity(intent);
    }


    // Check if Network is Available
    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }
}
