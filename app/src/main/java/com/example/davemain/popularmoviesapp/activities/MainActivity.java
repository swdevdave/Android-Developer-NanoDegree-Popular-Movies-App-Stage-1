package com.example.davemain.popularmoviesapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.davemain.popularmoviesapp.R;
import com.example.davemain.popularmoviesapp.adapters.MovieRecyclerViewAdapter;
import com.example.davemain.popularmoviesapp.model.Movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "MainActivity";

    private RequestQueue mQueue;
    private List<Movies> movieList;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ImageView noConnection;
    private TextView errorText;
    private String mUrl;
    private Button retryButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // No Network icon
        noConnection = findViewById(R.id.no_network_iv);
        noConnection.setVisibility(View.GONE);
        // Error Text
        errorText = findViewById(R.id.error_text);
        errorText.setVisibility(View.GONE);

        progressBar = findViewById(R.id.progress_bar);
        movieList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);

        // Retry button for Errors
        retryButton = findViewById(R.id.reset_button);
        retryButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                urlBuilder();
            }
        });


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);


        mQueue = Volley.newRequestQueue(this);
        urlBuilder();


    }

    private void jsonParse() {

        Log.d(TAG, "jsonParse: Started");
        progressBar.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.GONE);
        noConnection.setVisibility(View.GONE);
        retryButton.setVisibility(View.GONE);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, mUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject results = jsonArray.getJSONObject(i);

                        Movies movies = new Movies();

                        // Extract movie poster Url and create new url
                        String imgThumbUrl = "https://image.tmdb.org/t/p/w185";
                        String imgPosterUrl = "https://image.tmdb.org/t/p/w300";
                        String baseImgUrl = results.getString("poster_path");
                        // Small img for grid view
                        movies.setmThumbnailImgUrl(imgThumbUrl + baseImgUrl);
                        // large img for Details page
                        movies.setmPosterUrl(imgPosterUrl + baseImgUrl);

                        movies.setmTitle(results.getString("title"));
                        movies.setmOverview(results.getString("overview"));
                        movies.setmUserRating(results.getString("vote_average"));

                        // Takes yyyy-MM-dd converts to MM/dd/yyyy
                        String baseDate = results.getString("release_date");
                        String[] parts = baseDate.split("-");
                        String releaseDate = parts[1] + "/" + parts[2] + "/" + parts[0];
                        movies.setmReleaseDate(releaseDate);

                        movieList.add(movies);
                        Log.d(TAG, "onResponse: Received the following " + "\n"
                                + movies.getmTitle() + "\n"
                                + movies.getmThumbnailImgUrl() + "\n"
                                + movies.getmPosterUrl() + "\n"
                                + movies.getmOverview() + "\n"
                                + movies.getmReleaseDate() + "\n"
                                + movies.getmUserRating() + "\n");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.GONE);
                startRecyclerView(movieList);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                // Volley error handler
                errorText.setText("");
                errorText.setVisibility(View.VISIBLE);
                retryButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

                if (volleyError instanceof NetworkError) {
                    errorText.setText(R.string.network_error);
                    noConnection.setVisibility(View.VISIBLE);
                } else if (volleyError instanceof ServerError) {
                    errorText.setText(R.string.server_error);
                    noConnection.setVisibility(View.VISIBLE);
                } else if (volleyError instanceof AuthFailureError) {
                    errorText.setText(R.string.auth_failure);
                    noConnection.setVisibility(View.VISIBLE);
                } else if (volleyError instanceof ParseError) {
                    errorText.setText(R.string.parse_error);
                } else if (volleyError instanceof NoConnectionError) {
                    errorText.setText(R.string.no_connection);
                    noConnection.setVisibility(View.VISIBLE);
                } else if (volleyError instanceof TimeoutError) {
                    errorText.setText(R.string.time_out);
                    noConnection.setVisibility(View.VISIBLE);
                }
            }
        });
        mQueue = Volley.newRequestQueue(MainActivity.this);
        mQueue.add(request);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {

    }

    private void urlBuilder() {
        // (1) TODO - Enter your API KEY from https://www.themoviedb.org
        String apiKey = "";
        if (apiKey.length() > 1) {

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

            String contentSelection = sharedPrefs.getString(
                    getString(R.string.settings_order_by_key),
                    getString(R.string.settings_order_by_default));

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(contentSelection)
                    //.appendPath("top_rated")
                    .appendQueryParameter("api_key", apiKey)
                    .appendQueryParameter("language", "en-US");

            mUrl = builder.build().toString();
            Log.d(TAG, "urlBuilder: " + mUrl);
            jsonParse();

        } else {
            progressBar.setVisibility(View.GONE);
            errorText.setVisibility(View.VISIBLE);
            errorText.setText(R.string.no_api_key);
            retryButton.setVisibility(View.GONE);

        }
    }

    private void startRecyclerView(List<Movies> movieList) {

        MovieRecyclerViewAdapter movieAdapter = new MovieRecyclerViewAdapter(this, movieList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        recyclerView.setAdapter(movieAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_sort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
