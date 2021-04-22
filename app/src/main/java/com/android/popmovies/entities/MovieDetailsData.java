package com.android.popmovies.entities;

import com.google.gson.annotations.SerializedName;

public class MovieDetailsData {
    @SerializedName("adult") public Boolean adult;
    @SerializedName("backdrop_path") public String backdrop_path;
    @SerializedName("belongs_to_collection") public BelongsToCollection belongs_to_collection;
    @SerializedName("budget") public Long budget;
    @SerializedName("genres") public genre[] genres;
    @SerializedName("homepage") public String homepage;
    @SerializedName("id") public Long id;
    @SerializedName("imdb_id") public String imdb_id;
    @SerializedName("original_language") public String original_language;
    @SerializedName("original_title") public String original_title;
    @SerializedName("overview") public String overview;
    @SerializedName("popularity") public float popularity;
    @SerializedName("poster_path") public String poster_path;
    @SerializedName("production_countries") public production_country[] production_countries;
    @SerializedName("release_date") public String release_date;
    @SerializedName("revenue") public long revenue;
    @SerializedName("runtime") public int runtime;
    @SerializedName("spoken_languages") public spoken_language[] spoken_languages;
    @SerializedName("status") public String status;
    @SerializedName("tagline") public String tagline;
    @SerializedName("title") public String title;
    @SerializedName("video") public Boolean video;
    @SerializedName("vote_average") public double vote_average;
    @SerializedName("vote_count") public long vote_count;
}
