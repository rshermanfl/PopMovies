package com.android.popmovies.entities;

import com.google.gson.annotations.SerializedName;

public class BelongsToCollection {
    @SerializedName("id") public long id;
    @SerializedName("name") public String name;
    @SerializedName("poster_path") public String poster_path;
    @SerializedName("backdrop_path") public String backdrop_path;
}
