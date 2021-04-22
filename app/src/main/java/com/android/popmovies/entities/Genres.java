package com.android.popmovies.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Genres {
    @SerializedName("genres") public List<genre> genres = new ArrayList<>();
}
