package com.example.android.popularmovies.Model;

/**
 * Created by Precious on 7/22/2017.
 * Class that models the Trailer Class
 */

public class Trailer {
    private String mName;
    private String mKey;


    /**
     *
     * @param name the name for the Trailer
     * @param key for the Trailer
     */
    public Trailer(String name, String key) {
        this.mName = name;
        this.mKey = "https://www.youtube.com/watch?v=" + key;
    }

    // Get the name of Trailer
    public String getName() {
        return this.mName;
    }

    // Get the Youtube String url for the Trailer
    public String getKey() {
        return this.mKey;
    }
}
