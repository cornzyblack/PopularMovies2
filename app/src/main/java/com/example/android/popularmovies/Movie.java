package com.example.android.popularmovies;

/**
 * Created by Precious on 15/04/2017.
 */

public class Movie {
    private String mMovieRating;
    private String mMoviePosterPath;
    private String mMovieSynopsis;
    private String mMovieTitle;
    private String mMovieReleaseDate;

    /**
     * @param movieRating      the Rating of the Movie
     * @param moviePosterPath  the Poster Image path of the Movie
     * @param movieSynopsis    the Synopsis of the Movie
     * @param movieTitle       the Title for the Movie
     * @param movieReleaseDate the Release Date for the Movie
     */
    public Movie(String movieRating, String moviePosterPath, String movieSynopsis, String movieTitle, String movieReleaseDate) {
        this.mMovieRating = movieRating;
        this.mMoviePosterPath = moviePosterPath;
        this.mMovieSynopsis = movieSynopsis;
        this.mMovieTitle = movieTitle;
        this.mMovieReleaseDate = movieReleaseDate;
    }

    /**
     * return the Movie Synopsis
     */
    public String getMovieSynopsis() {
        return mMovieSynopsis;
    }

    /**
     * return the Poster Image path for the Movie
     */
    public String getMoviePosterPath() {
        return mMoviePosterPath;
    }

    /**
     * @return the Movie Rating
     */
    public String getMovieRating() {
        return mMovieRating;
    }

    /**
     * @return the Movie Title
     */
    public String getMovieTitle() {
        return mMovieTitle;
    }

    /**
     * @return the Release date for the Movie
     */
    public String getMovieReleaseDate() {
        return mMovieReleaseDate;
    }
}
