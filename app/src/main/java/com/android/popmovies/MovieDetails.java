package com.android.popmovies;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.android.popmovies.entities.Actor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.view.View.inflate;

public class MovieDetails extends AppCompatActivity {

    private FloatingActionButton btnBack;
    private ImageView backdrop;
    private ImageView poster;
    private APIHelper apiHelper;
    private TextView title;
    private TextView releaseDate;
    private TextView runTime;
    private TextView budget;
    private TextView gross;
    private LinearLayout cast;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiHelper = APIHelper.getInstance();
        setContentView(R.layout.activity_movie_details);
        title = findViewById(R.id.d_movie_title);
        title.setText(apiHelper.getMovieDetailsData().title);
        btnBack = findViewById(R.id.fabBack);
        btnBack.setAlpha(0.75f);
        btnBack.setOnClickListener(view -> MovieDetails.this.finish());

        backdrop = findViewById(R.id.backdrop);
        String imageURLBase = null;
        try {
            imageURLBase = apiHelper.baseUrl()
                    .concat(apiHelper.backdropSize(1))
                    .replace("http", "https")
                    .concat(apiHelper.getMovieDetailsData().backdrop_path);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        poster = findViewById(R.id.d_movie_poster);
        Picasso.with(MovieDetails.this).load(imageURLBase).into(backdrop);
        try {
            imageURLBase = apiHelper.baseUrl()
                    .concat(apiHelper.profileSize(1))
                    .replace("http", "https")
                    .concat(apiHelper.getMovieDetailsData().poster_path);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Picasso.with(MovieDetails.this).load(imageURLBase).into(poster);
        releaseDate = findViewById(R.id.d_release_date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date relDate = dateFormat.parse(apiHelper.getMovieDetailsData().release_date);
            if (relDate != null) {
                dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                releaseDate.setText(dateFormat.format(relDate));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        runTime = findViewById(R.id.d_runtime);
        runTime.setText("Runtime: ".concat(String.valueOf(apiHelper.getMovieDetailsData().runtime)));
        budget = findViewById(R.id.d_budget);
        gross = findViewById(R.id.d_gross);

        NumberFormat format = new DecimalFormat("#,###");
        long mBudget = apiHelper.getMovieDetailsData().budget;
        String budgetText = "Budget: ";
        budgetText += (mBudget > 0) ? "$".concat(format.format(mBudget)) : "N/A";
        budget.setText(budgetText);

        long mGross = apiHelper.getMovieDetailsData().revenue;
        String boxOfficeText = "Box Office: ";
        boxOfficeText += (mGross > 0) ? "$".concat(format.format(mBudget)) : "N/A";
        gross.setText(boxOfficeText);

        cast = findViewById(R.id.d_castholder);
        int i = 0;
        if (apiHelper.getMovieCast().cast.length > 0) {
            for (Actor A : apiHelper.getMovieCast().cast) {
                i++;
                LinearLayout credit = (LinearLayout) inflate(this, R.layout.item_credits, null);
                TextView actor = credit.findViewById(R.id.actor);
                TextView character = credit.findViewById(R.id.character_name);
                actor.setText(A.name);
                character.setText(A.character);
                if (i % 2 == 0) { // (A.order % 2 == 0) // order is not consistent
                    credit.setBackgroundColor(Color.parseColor("#333333"));
                }
                cast.addView(credit);
            }
        }
    }
}