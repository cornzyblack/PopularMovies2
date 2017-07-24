package com.example.android.popularmovies.Model;

/**
 * Created by Precious on 7/22/2017.
 * Class that Models the Review Object
 */

public class Review {
    private String mAuthor;
    private String mContent;

    /**
     *
     * @param author the Reviewer's name
     * @param content the content for a review
     */
    public Review(String author, String content) {
        this.mAuthor = author;
        this.mContent = content;
    }

    //get Review Author
    public String getAuthor() {
        return this.mAuthor;
    }

    //get Review Content
    public String getContent() {
        return this.mContent;
    }

}

