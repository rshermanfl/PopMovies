package com.android.popmovies.entities;

import com.google.gson.annotations.SerializedName;

public class Credits {
    @SerializedName("id") public long id;
    @SerializedName("cast") public Actor[] cast;
}
