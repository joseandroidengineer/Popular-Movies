package com.jge.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private JSONObject mJSONObjectResponse;
    private JSONArray jsonArray;
    private String error;
    private Gson gson;
    private List<Movie> movies;
    private RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerview_list_of_movies);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);
        volleyRequest();

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        loadMovieData();
        mMovieAdapter.notifyDataSetChanged();

    }

    private void loadMovieData() {
        showMovieDataView();
    }

    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void gsonMap(JSONArray jsonArray) {
        String volleyError = error;
        if(mJSONObjectResponse == null){
            showErrorMessage();
        }else{
            GsonBuilder gsonBuilder = new GsonBuilder();
            gson = gsonBuilder.create();
            if (jsonArray.length() > 0) {
                movies = Arrays.asList(gson.fromJson(jsonArray.toString(), Movie[].class));
                mMovieAdapter.setMovieData(movies);

            }
        }
    }

    private void volleyRequest(){
        queue = Volley.newRequestQueue(getBaseContext());
        String url = String.valueOf(NetworkUtils.buildBaseUrl(getString(R.string.movie_api_key)));
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        mJSONObjectResponse = response;
                        try {
                            jsonArray = new JSONArray(response.getJSONArray("results"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            try {
                                gsonMap(response.getJSONArray("results"));
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        error = volleyError.toString();
                        Log.e("VOLLEY", volleyError.toString());
                        showErrorMessage();
                    }
                });
        queue.add(jsonObjectRequest);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.sort){
            Toast.makeText(this,"Replace Toast with sort of movies by Popularity", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(Movie movieNameIndexClicked) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("title",movieNameIndexClicked.getTitle());
        intent.putExtra("overview", movieNameIndexClicked.getDescription());
        intent.putExtra("imgUrl", movieNameIndexClicked.getPosterImagePath());
        intent.putExtra("rating",movieNameIndexClicked.getVoteAverage());
        intent.putExtra("release",movieNameIndexClicked.getReleaseDate());
        startActivity(intent);

    }
}
