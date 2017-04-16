package com.example.android.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements MovieAdapter.ListItemClickListener {

    private final static String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie";
    private final static String QUERY = "api_key";
    private final static String MOVIE_POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    List<Movie> movies;
    RecyclerView recyclerView;

    //Insert your Api key here
    private final static String api_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //on default load the most popular movie
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        loadMovieData();
    }

    //Inflates the Menu in MainActivity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_main; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void loadMovieData() {
        new MovieQueryTask().execute(MOVIE_BASE_URL, "popular");
    }

    //Handles what happens when an Item is selected in the Settings Group
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle the ActionBar
        int id = item.getItemId();

        //Choose any selected Item from the Settings group
        if (id == R.id.action_most_popular) {
            item.setChecked(true);
            new MovieQueryTask().execute(MOVIE_BASE_URL, "popular");
            Toast.makeText(this, "popular", Toast.LENGTH_SHORT).show();
            return true;

        } else if (id == R.id.action_sort_highest_rated) {
            item.setChecked(false);
            new MovieQueryTask().execute(MOVIE_BASE_URL, "top_rated");
            Toast.makeText(this, "highest Sorted", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class MovieQueryTask extends AsyncTask<String, Void, List<Movie>> {
        @Override
        protected List<Movie> doInBackground(String... params) {
            // If there's no URL
            if (params.length == 0) {
                return null;
            }

            String baseUrl = params[0];
            String folder = params[1];
            String movieJsonResponse;
            List<Movie> movieResponse = null;

            try {
                movieJsonResponse = NetworkUtils.getMoviesResponse(baseUrl, folder, QUERY, api_key);
                movieResponse = getMovies(movieJsonResponse);

            } catch (IOException e) {
                Log.e("MainActivity", "Error from NetworkUtils.getMoviesResponse");
            }
            return movieResponse;
        }

        @Override
        protected void onPostExecute(List<Movie> movieList) {
            MovieAdapter adapter = new MovieAdapter(movieList, MainActivity.this);
            recyclerView.setAdapter(adapter);
        }
    }

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

                //Create a new Movie Object and append it tp the List of Movies
                movies.add(new Movie(movieRating, moviePosterPath, movieSynopsis, movieTitle, movieReleaseDate));
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


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);

        intent.putExtra("title", movies.get(position).getMovieTitle());
        intent.putExtra("releaseDate", movies.get(position).getMovieReleaseDate());
        intent.putExtra("posterPath", movies.get(position).getMoviePosterPath());
        intent.putExtra("synopsis", movies.get(position).getMovieSynopsis());
        intent.putExtra("rating", movies.get(position).getMovieRating());

        //startActivity(intent);
        startActivity(intent);
    }
}
