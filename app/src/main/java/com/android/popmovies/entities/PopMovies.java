package com.android.popmovies.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PopMovies {
    @SerializedName("page") public Integer page;
    @SerializedName("results") public List<Popmovie> results = new ArrayList<>();
    @SerializedName("total_pages") public Integer total_pages;
    @SerializedName("total_results") public Integer total_results;
}
