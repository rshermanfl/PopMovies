package com.android.popmovies.entities;

import com.google.gson.annotations.SerializedName;

public class Actor {
    @SerializedName("adult") public Boolean adult;
    @SerializedName("gender") public int gender;
    @SerializedName("id") public int id;
    @SerializedName("known_for_department") public String known_for_department;
    @SerializedName("name") public String name;
    @SerializedName("original_name") public String original_name;
    @SerializedName("popularity") public float popularity;
    @SerializedName("profile_path") public String profile_path;
    @SerializedName("cast_id") public int cast_id;
    @SerializedName("character") public String character;
    @SerializedName("credit_id") public String credit_id;
    @SerializedName("order") public int order;
}
