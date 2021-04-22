package com.android.popmovies.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Popmovie {
    @SerializedName("adult") public Boolean adult;
    @SerializedName("backdrop_path") public String backdrop_path;
    @SerializedName("genre_ids") public List<Integer> genre_ids = new ArrayList<>();
    @SerializedName("id") public Long id;
    @SerializedName("original_language") public String original_language;
    @SerializedName("original_title") public String original_title;
    @SerializedName("overview") public String overview;
    @SerializedName("popularity") public Float popularity;
    @SerializedName("poster_path") public String poster_path;
    @SerializedName("release_date") public String release_date;
    @SerializedName("title") public String title;
    @SerializedName("video") public Boolean video;
    @SerializedName("vote_average") public Double vote_average;
    @SerializedName("vote_count") public Long vote_count;
}
