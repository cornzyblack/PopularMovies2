package com.example.android.popularmovies;

/**
 * Created by Precious on 15/04/2017.
 */

import android.util.Log;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {

    public static String getMoviesResponse(String baseUrl, String folder, String query, String apiKey) throws IOException {
        /**
         * This method returns the entire result from the HTTP response.
         */
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = urlBuilder(baseUrl, query, folder, apiKey);

        String url = urlBuilder.build().toString();
        Log.e("url", url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute();) {
            return response.body().string();
        }
    }

    /**
     * Builds and returns a built Url
     *
     * @param baseUrl the base URL
     * @param query   the query value
     * @param folder  the folder path to the file
     * @param apiKey  the user's Api key
     * @return the built Url
     */
    public static HttpUrl.Builder urlBuilder(String baseUrl, String query, String folder, String apiKey) {
        HttpUrl.Builder builtUrl = HttpUrl.parse(baseUrl).newBuilder()
                .addPathSegment(folder)
                .addQueryParameter(query, apiKey);
        return builtUrl;
    }

    /**
     * Builds and returns a built Url
     *
     * @param baseUrlPosterImage the base URL for the Image
     * @param size               the Size for the image
     * @param posterPath         the path for the Image
     * @return the built Url for the Image
     */
    public static String posterUrlImageBuilder(String baseUrlPosterImage, String size, String posterPath) {
        HttpUrl.Builder builtUrl = HttpUrl.parse(baseUrlPosterImage).newBuilder()
                .addPathSegment(size)
                .addPathSegment(posterPath);
        String posterUrl = builtUrl.build().toString();
        return posterUrl;
    }
}

