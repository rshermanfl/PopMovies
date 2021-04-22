package com.android.popmovies;
import com.android.popmovies.entities.Credits;
import com.android.popmovies.entities.Genres;
import com.android.popmovies.entities.MovieDetailsData;
import com.android.popmovies.entities.PopMovies;
import com.android.popmovies.entities.genre;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import okhttp3.HttpUrl;
import okhttp3.Request;


public class APIHelper {

    private final String API_KEY = "a371f045b3ab77663a5c1143a9fb9da1";
    private final String BASE_URL = "https://api.themoviedb.org/3/";
    private final String API = "api_key";
    private JSONObject config = null;
    private JSONObject images = null;
    public boolean hasConfig, hasGenres;
    private Genres GenreParent = null;
    private PopMovies PopResults = null;
    private static APIHelper   apiHelper;
    private MovieDetailsData movieDetailsData = null;
    private Credits movieCast = null;

    private APIHelper() { }

    public static APIHelper getInstance() {
        if (apiHelper == null) apiHelper = new APIHelper();
        return apiHelper;
    }

    public Request getMGenreRequest() {
        String m_GENRE_PATH = "genre/movie/list";
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(BASE_URL + m_GENRE_PATH))
                .newBuilder()
                .addQueryParameter(API, API_KEY);
        return new Request.Builder().url( urlBuilder.build().toString() ).build();
    }

    public Request getMPopularRequest(int page) {
        if (page <= 0) page = 1;
        final String m_POPULAR_PATH = "movie/popular";
        final String PG = "page";
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(BASE_URL + m_POPULAR_PATH))
                .newBuilder()
                .addQueryParameter(API, API_KEY)
                .addQueryParameter(PG, String.valueOf(page));
        return new Request.Builder().url( urlBuilder.build().toString() ).build();
    }

    public Request getMConfigurationRequest() {
        final String m_CONFIGURATION_PATH = "configuration";
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(BASE_URL + m_CONFIGURATION_PATH))
                .newBuilder()
                .addQueryParameter(API, API_KEY);
        return new Request.Builder().url( urlBuilder.build().toString() ).build();
    }

    public Request getMovieDetailsRequest(long movieId) {
        final String m_MOVIE_DETAIL_PATH = "movie/";
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(
                    HttpUrl.parse(BASE_URL + m_MOVIE_DETAIL_PATH + movieId)
                )
                .newBuilder()
                .addQueryParameter(API, API_KEY);
        return new Request.Builder().url( urlBuilder.build().toString() ).build();
    }

    public Request getMovieCastRequest(long movieId) {
        final String m_MOVIE_DETAIL_PATH = "movie/";
        final String m_CAST = "/credits";
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(
                HttpUrl.parse(BASE_URL + m_MOVIE_DETAIL_PATH + movieId + m_CAST)
        )
                .newBuilder()
                .addQueryParameter(API, API_KEY);
        return new Request.Builder().url( urlBuilder.build().toString() ).build();
    }

    public void parseConfigs(JSONObject conf) {
        if (conf!=null) {
            config = conf;
            try {
                images = config.getJSONObject("images");
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                hasConfig = (config != null && images != null);
            }
        }
    }

    public String backdropSize(int idx) throws JSONException {
        JSONArray tmp = images.getJSONArray("backdrop_sizes");
        idx = safeBounds(idx, tmp);
        return tmp.getString(idx);
    }

    public String LogoSize(int idx) throws JSONException {
        JSONArray tmp = images.getJSONArray("logo_sizes");
        idx = safeBounds(idx, tmp);
        return tmp.getString(idx);
    }

    public String posterSize(int idx) throws JSONException {
        JSONArray tmp = images.getJSONArray("poster_sizes");
        idx = safeBounds(idx, tmp);
        return tmp.getString(idx);
    }

    public String profileSize(int idx) throws JSONException {
        JSONArray tmp = images.getJSONArray("profile_sizes");
        idx = safeBounds(idx, tmp);
        return tmp.getString(idx);
    }

    public String stillSize(int idx) throws JSONException {
        JSONArray tmp = images.getJSONArray("still_sizes");
        idx = safeBounds(idx, tmp);
        return tmp.getString(idx);
    }

    public String baseUrl() throws JSONException {
        return images.getString("base_url");
    }

    public String secureBaseUrl() throws JSONException {
        return config.getString("secure_base_url");
    }

    public String changeKey(int idx) throws JSONException {
        JSONArray tmp = config.getJSONArray("change_keys");
        idx = safeBounds(idx, tmp);
        return tmp.getString(idx);
    }

    public void parseGenres(String json) {
        Gson gson = new Gson();
        GenreParent = gson.fromJson(json, Genres.class);
        hasGenres = (GenreParent != null);
    }

    public genre getByGenreId(int id) {
        if (!GenreParent.genres.isEmpty()) {
            for (genre g:GenreParent.genres) {
                if (g.id == id) {
                    return g;
                }
            }
        }
        return null;
    }

    public genre getGenreByIndex(int idx) {
        if (!GenreParent.genres.isEmpty()) {
            if (idx > GenreParent.genres.size()-1)
                idx = GenreParent.genres.size()-1;
            return GenreParent.genres.get(idx);
        }
        return null;
    }

    public void parsePopMovieResults(String json) {
        Gson gson = new Gson();
        PopResults = gson.fromJson(json, PopMovies.class);
    }

    public void parseMovieDetails(String json) {
        Gson gson = new Gson();
        movieDetailsData = gson.fromJson(json, MovieDetailsData.class);
    }

    public void parseMovieCast(String json) {
        Gson gson = new Gson();
        movieCast = gson.fromJson(json, Credits.class);
    }

    public Credits getMovieCast() { return movieCast; }

    public PopMovies getPopResults() {
        return PopResults;
    }

    public MovieDetailsData getMovieDetailsData() { return movieDetailsData; }

    private int safeBounds (int idx, JSONArray ary) {
        // pages are one based 0 throws error
        if (idx < 1)
            idx = 1;
        if (idx > ary.length())
            idx = ary.length();
        return idx;
    }
}
