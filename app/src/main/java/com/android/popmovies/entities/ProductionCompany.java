package com.android.popmovies.entities;

import com.google.gson.annotations.SerializedName;

public class ProductionCompany {
    @SerializedName("id") public int id;
    @SerializedName("logo_path") public String logo_path;
    @SerializedName("name") public String name;
    @SerializedName("origin_country") public String origin_country;
}
