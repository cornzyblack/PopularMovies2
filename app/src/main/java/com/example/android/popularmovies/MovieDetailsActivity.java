package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.adapters.ReviewsAdapter;
import com.example.android.popularmovies.adapters.TrailersAdapter;
import com.example.android.popularmovies.data.FavouriteMovieContract;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.model.Trailer;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.popularmovies.utilities.NetworkUtils.MOVIE_BASE_URL;

/**
 * Created by Precious on 16/04/2017.
 */

public class MovieDetailsActivity extends AppCompatActivity implements TrailersAdapter.ListItemClickListener {
    private ImageView moviePosterImage;
    private TextView movieSynopsis, movieRelease;
    private TextView movieTitle, movieRating;

    private ImageButton mFavouriteButton;

    private final static int REVIEWS_LOADER = 13;
    private final static int TRAILERS_LOADER = 14;

    private final static String QUERY = "api_key";
    private int movieIdPass;


    private List<Trailer> trailers;
    private List<Review> reviews;

    private RecyclerView reviewRecyclerView;
    private RecyclerView trailerRecyclerView;
    private ProgressBar mReviewLoadingIndicator;
    private ProgressBar mTrailerLoadingIndicator;
    private LinearLayoutManager reviewLayoutManager;
    private LinearLayoutManager trailerLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details_activity);

        moviePosterImage = (ImageView) findViewById(R.id.movie_poster_image);
        movieRating = (TextView) findViewById(R.id.movie_rating);
        movieRelease = (TextView) findViewById(R.id.movie_release);
        movieSynopsis = (TextView) findViewById(R.id.movie_synopsis);
        movieTitle = (TextView) findViewById(R.id.movie_title);
        mFavouriteButton = (ImageButton) findViewById(R.id.fav_button);

        mReviewLoadingIndicator = (ProgressBar) findViewById(R.id.pb_reviews_loading_indicator);
        mTrailerLoadingIndicator = (ProgressBar) findViewById(R.id.pb_trailers_loading_indicator);

        //Get Intent and Data from MainActivity Intent
        Intent movieDetailsIntent = getIntent();

        //Check that there is an Intent
        if (movieDetailsIntent != null) {
            String title = movieDetailsIntent.getStringExtra("title");
            movieTitle.setText(title);

            String posterImagePath = movieDetailsIntent.getStringExtra("posterPath");
            Picasso.with(MovieDetailsActivity.this).load(posterImagePath).into(moviePosterImage);

            String releaseDate = movieDetailsIntent.getStringExtra("releaseDate");
            movieRelease.setText(releaseDate);

            String synopsis = movieDetailsIntent.getStringExtra("synopsis");
            movieSynopsis.setText(synopsis);

            String rating = movieDetailsIntent.getStringExtra("rating");
            movieRating.setText(rating);

            movieIdPass = movieDetailsIntent.getIntExtra("movie_id", 0);
        }


        mFavouriteButton.setOnClickListener(new View.OnClickListener() {

            // Retrieve information to be put inside DB from Intent
            Intent movieIntent = getIntent();
            String title = movieIntent.getStringExtra("title");

            // Button to add Movie to Favourites
            @Override
            public void onClick(View v) {

                ContentValues contentValues = new ContentValues();
                contentValues.put(FavouriteMovieContract.MovieEntry.COLUMN_MOVIE_NAME, title);
                contentValues.put(FavouriteMovieContract.MovieEntry.COLUMN_MOVIE_ID, movieIdPass);

                getContentResolver()
                        .insert(FavouriteMovieContract.MovieEntry.CONTENT_URI, contentValues);

                Toast.makeText(MovieDetailsActivity.this, R.string.added_to_favourite, Toast.LENGTH_SHORT).show();
            }
        });

        reviewRecyclerView = (RecyclerView) findViewById(R.id.rv_reviews);
        reviewRecyclerView.setHasFixedSize(true);

        reviewLayoutManager = new LinearLayoutManager(this);
        reviewRecyclerView.setLayoutManager(reviewLayoutManager);
        // If network is available show reviews
        if (isNetworkAvailable())
            getReviewLoader();


        trailerRecyclerView = (RecyclerView) findViewById(R.id.rv_trailers);
        trailerRecyclerView.setHasFixedSize(true);

        trailerLayoutManager = new LinearLayoutManager(this);
        trailerRecyclerView.setLayoutManager(trailerLayoutManager);
        // If network is available show trailers
        if (isNetworkAvailable())
            getTrailerLoader();

    }

    // For Trailer Loader
    private LoaderManager.LoaderCallbacks<List<Trailer>> trailerLoader = new LoaderManager.LoaderCallbacks<List<Trailer>>() {
        @Override
        public Loader<List<Trailer>> onCreateLoader(int id, final Bundle args) {
            return new AsyncTaskLoader<List<Trailer>>(MovieDetailsActivity.this) {
                @Override
                protected void onStartLoading() {
                    mTrailerLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }

                @Override
                public List<Trailer> loadInBackground() {
                    String trailerJsonResponse;
                    String baseUrl = MOVIE_BASE_URL;
                    int mId = args.getInt("MOVIE_ID");
                    List<Trailer> trailerResponse;
                    try {
                        trailerJsonResponse = NetworkUtils.getTrailersResponse(baseUrl, mId, QUERY, NetworkUtils.API_KEY);
                        trailerResponse = getTrailers(trailerJsonResponse);
                        return trailerResponse;
                    } catch (IOException e) {
                        Log.d("MainActivity", "Error from NetworkUtils.getReviewsResponse");
                        return null;
                    }
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<List<Trailer>> loader, List<Trailer> data) {
            mTrailerLoadingIndicator.setVisibility(View.INVISIBLE);
            TrailersAdapter trailersAdapter = new TrailersAdapter(data, MovieDetailsActivity.this);
            trailerRecyclerView.setAdapter(trailersAdapter);
        }

        @Override
        public void onLoaderReset(Loader<List<Trailer>> loader) {

        }
    };


    // For Review Loader
    private LoaderManager.LoaderCallbacks<List<Review>> reviewLoader = new LoaderManager.LoaderCallbacks<List<Review>>() {
        @Override
        public Loader<List<Review>> onCreateLoader(int id, final Bundle args) {
            return new AsyncTaskLoader<List<Review>>(MovieDetailsActivity.this) {
                @Override
                protected void onStartLoading() {
                    mReviewLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }

                @Override
                public List<Review> loadInBackground() {
                    String reviewJsonResponse;
                    String baseUrl = MOVIE_BASE_URL;
                    int mId = args.getInt("MOVIE_ID");
                    List<Review> reviewResponse;
                    try {
                        reviewJsonResponse = NetworkUtils.getReviewsResponse(baseUrl, mId, QUERY, NetworkUtils.API_KEY);
                        reviewResponse = getReviews(reviewJsonResponse);
                        return reviewResponse;
                    } catch (IOException e) {
                        Log.d("MainActivity", "Error from NetworkUtils.getReviewsResponse");
                        return null;
                    }
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<List<Review>> loader, List<Review> data) {
            mReviewLoadingIndicator.setVisibility(View.INVISIBLE);
            ReviewsAdapter reviewsAdapter = new ReviewsAdapter(data);
            reviewRecyclerView.setAdapter(reviewsAdapter);
        }

        @Override
        public void onLoaderReset(Loader<List<Review>> loader) {

        }
    };

    public void getTrailerLoader() {
        Bundle trailerBundle = new Bundle();
        trailerBundle.putInt("MOVIE_ID", movieIdPass);
        LoaderManager trailerLoaderManager = getSupportLoaderManager();
        trailerLoaderManager.restartLoader(TRAILERS_LOADER, trailerBundle, trailerLoader);
    }

    public void getReviewLoader() {
        Bundle reviewBundle = new Bundle();
        reviewBundle.putInt("MOVIE_ID", movieIdPass);
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(REVIEWS_LOADER, reviewBundle, reviewLoader);
    }

    /*
    *Parses the JSON Response into a List of Trailers
    */
    private List<Trailer> getTrailers(String response) {
        trailers = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(response);
            JSONArray results = root.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject trailer = results.getJSONObject(i);
                String name = trailer.getString("name");
                String key = trailer.getString("key");
                this.trailers.add(new Trailer(name, key));
            }
        } catch (JSONException e) {
            Log.d("Error", "trailers");
        }
        //loop through the results array and get Trailer objects

        return trailers;
    }

    /*
    *Parses the JSON Response into a List of Reviews
    */

    private List<Review> getReviews(String response) {
        reviews = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(response);
            JSONArray results = root.getJSONArray("results");

            //loop through the results array and get Review objects
            for (int i = 0; i < results.length(); i++) {
                JSONObject review = results.getJSONObject(i);

                //get the Author name
                String reviewAuthorName = review.getString("author");

                //Get the Review Content
                String reviewContent = review.getString("content");

                //Create a new Review Object and append it to the List of Reviews
                this.reviews.add(new Review(reviewAuthorName, reviewContent));
            }
        } catch (JSONException e) {
            Log.e("MainActivity", "Error Parsing JSON");
        }

        return reviews;
    }


    @Override
    public void onListItemClick(int clickedItemIndex) {
        // Open video in Youtube App
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailers.get(clickedItemIndex).getKey())));
    }

    // Insert a New row in the Table
  //  public long addNewMovies(String movieName, int movieId) {
   //     ContentValues cv = new ContentValues();
     //   cv.put(FavouriteMovieContract.MovieEntry.COLUMN_MOVIE_ID, movieId);
       // cv.put(FavouriteMovieContract.MovieEntry.COLUMN_MOVIE_NAME, movieName);
        //return mDb.insert(FavouriteMovieContract.MovieEntry.TABLE_NAME, null, cv);
    //}

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
