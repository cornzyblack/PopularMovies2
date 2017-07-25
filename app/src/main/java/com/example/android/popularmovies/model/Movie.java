package com.example.android.popularmovies.model;

/**
 * Created by Precious on 15/04/2017.
 */

public class Movie {
    private String mMovieVoteCount;
    private String mMoviePosterPath;
    private String mMovieOverview;
    private String mMovieTitle;
    private String mMovieReleaseDate;
    private int mMovieId;

    /**
     * @param movieVoteCount   the Rating of the Movie
     * @param moviePosterPath  the Poster Image path of the Movie
     * @param movieOverview    the Synopsis of the Movie
     * @param movieTitle       the Title for the Movie
     * @param movieReleaseDate the Release Date for the Movie
     * @param movieId the Id for the Movie
     */
    public Movie(String movieVoteCount, String moviePosterPath, String movieOverview, String movieTitle, String movieReleaseDate, int movieId) {
        this.mMovieVoteCount = movieVoteCount;
        this.mMoviePosterPath = moviePosterPath;
        this.mMovieOverview = movieOverview;
        this.mMovieTitle = movieTitle;
        this.mMovieReleaseDate = movieReleaseDate;
        this.mMovieId = movieId;
    }

    /**
     * return the Movie Synopsis
     */
    public String getMovieSynopsis() {
        return mMovieOverview;
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
        return mMovieVoteCount;
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

    /**
     * @return the movieId for the Movie
     */
    public int getMovieId() {
        return mMovieId;
    }

}
