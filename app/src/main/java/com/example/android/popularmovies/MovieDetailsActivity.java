package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Precious on 16/04/2017.
 */

public class MovieDetailsActivity extends AppCompatActivity {
    private ImageView moviePosterImage;
    private TextView movieSynopsis, movieRelease;
    private TextView movieTitle, movieRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details_activity);

        moviePosterImage = (ImageView) findViewById(R.id.movie_poster_image);
        movieRating = (TextView) findViewById(R.id.movie_rating);
        movieRelease = (TextView) findViewById(R.id.movie_release);
        movieSynopsis = (TextView) findViewById(R.id.movie_synopsis);
        movieTitle = (TextView) findViewById(R.id.movie_title);

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
        }
    }
}
