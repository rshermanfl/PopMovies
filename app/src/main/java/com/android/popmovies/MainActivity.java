package com.android.popmovies;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.popmovies.entities.Popmovie;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private APIHelper apiHelper;
    private OkHttpClient client;
    private MoviesAdapter movies;
    private RecyclerView popularResults;
    private BottomNavigationView bottomNavigationView;
    private TextView pageView;
    private int page = 1;
    ProgressBar spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiHelper = APIHelper.getInstance();
        client = new OkHttpClient();

        spinner = findViewById(R.id.progress_loader);
        spinner.setVisibility(View.VISIBLE);

        popularResults = findViewById(R.id.popular_movies_list);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        popularResults.setLayoutManager(llm);
        popularResults.setAdapter(null);

        pageView = findViewById(R.id.page);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                switch (item.getItemId()) {
                    case R.id.back:
                        page--;
                        if (page < 1) page = 1;
                        getPopMoviesPage(page);
                        break;
                    case R.id.forward:
                        page++;
                        if (page > apiHelper.getPopResults().total_pages) page = apiHelper.getPopResults().total_pages;
                        getPopMoviesPage(page);
                        break;
                }
                    return false;
            }
        );
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getConfigurations();
    }

    private void getConfigurations() {
        client.newCall(apiHelper.getMConfigurationRequest())
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        runOnUiThread(() -> {
                            Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        final String myResponse = response.body().string();
                        try {
                            apiHelper.parseConfigs(new JSONObject(myResponse));
                            if (apiHelper.hasConfig) {
                                Log.d("CONFIGURATIONS::BASE URL::", apiHelper.baseUrl());
                                getGenres();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    private void getGenres() {
        client.newCall(apiHelper.getMGenreRequest())
                .enqueue(new Callback() {

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        final String myResponse = response.body().string();
                        apiHelper.parseGenres(myResponse);
                        if (apiHelper.hasGenres) {
                            Log.d("GENRES::878", apiHelper.getByGenreId(878).toString());
                            getPopMoviesPage(page);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        runOnUiThread(() -> {
                            Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }
                });
    }

    private void getPopMoviesPage(int idx) {
        // check idx bounds
        // expected spinner fail onresume
        try {
            spinner.setVisibility(View.VISIBLE);
        } catch (Exception err) {
            err.printStackTrace();
        }
        client.newCall(apiHelper.getMPopularRequest(idx))
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        runOnUiThread(() -> {
                            Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            spinner.setVisibility(View.GONE);
                        });
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        final String myResponse = response.body().string();
                        apiHelper.parsePopMovieResults(myResponse);
                        Log.d("TOTAL PAGES::", apiHelper.getPopResults().total_pages.toString());
                        Log.d("TOTAL RECORDS::", apiHelper.getPopResults().total_results.toString());
                        if (apiHelper.hasConfig) {
                            String imageURLBase = "";
                            try {
                                imageURLBase = apiHelper.baseUrl().concat(apiHelper.posterSize(1));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            final String finalImageURLBase = imageURLBase;
                            runOnUiThread(() -> {
                                movies = new MoviesAdapter(finalImageURLBase,
                                        apiHelper.getPopResults().results.toArray( new Popmovie[0]),
                                        MainActivity.this,
                                        item -> {
                                            spinner.setVisibility(View.VISIBLE);
                                            // Toast.makeText(MainActivity.this, "Item Clicked " + item.id, Toast.LENGTH_LONG).show();
                                            getMovieDetails(item.id);
                                        });
                                popularResults.setAdapter(movies);
                                pageView.setText("Page " + idx);
                                spinner.setVisibility(View.GONE);
                            });
                        }
                    }
                });
    }

    private void getMovieDetails(long movieId) {
        client.newCall(apiHelper.getMovieDetailsRequest(movieId)).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();
                apiHelper.parseMovieDetails(myResponse);
                Log.d("MOVIE DETAILS::", apiHelper.getMovieDetailsData().id.toString());
                Log.d("MOVIE DETAILS::", apiHelper.getMovieDetailsData().title);
                getCastDetail(movieId);
            }
        });
    }

    private void getCastDetail(long movieId) {
        client.newCall(apiHelper.getMovieCastRequest(movieId)).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();
                apiHelper.parseMovieCast(myResponse);
                runOnUiThread(()-> spinner.setVisibility(View.GONE));
                Intent detailScreen = new Intent(MainActivity.this, MovieDetails.class);
                startActivity(detailScreen);
            }
        });
    }
}