package com.jge.popularmovies;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class NetworkUtils {

    private static final String API_KEY = "api_key";
    private static final String BASE_URL = "https://api.themoviedb.org/";
    private static final String NUM_PARAM = "3/";
    private static final String MEDIA_TYPE_PATH = "movie/";
    private static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/";
    private static final String SIZE_IMAGE_PATH ="w500";


    public static URL buildBaseUrl(String key, String encodedPath) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(NUM_PARAM)
                .appendEncodedPath(MEDIA_TYPE_PATH)
                .appendEncodedPath(encodedPath)
                .appendQueryParameter(API_KEY,key)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.e("URL", builtUri.toString());

        return url;
    }

    public static Uri buildImageUrl(String backdropPath){
        Uri builtUri = Uri.parse(BASE_IMAGE_URL).buildUpon()
                .appendEncodedPath(SIZE_IMAGE_PATH)
                .appendEncodedPath(backdropPath)
                .build();

        Log.v(TAG, "Built IMAGE URI " + builtUri);

        return builtUri;
    }

}
