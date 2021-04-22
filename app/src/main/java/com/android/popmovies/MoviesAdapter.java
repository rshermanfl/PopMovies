package com.android.popmovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.popmovies.entities.Popmovie;
import com.squareup.picasso.Picasso;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Popmovie item);
    }

    private Popmovie[] data_set;
    private Context context;
    private String imageSizeURL;
    private APIHelper apiHelper;
    private final OnItemClickListener listener;

    public MoviesAdapter(String url, Popmovie[] ds, Context ctx, OnItemClickListener listener) {
        data_set = ds;
        context = ctx;
        imageSizeURL = url;
        apiHelper = APIHelper.getInstance();
        this.listener = listener;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView movieTitle;
        private final TextView movieOverview;
        private final TextView movieDetails;
        private final ImageView poster;

        public ViewHolder(View view) {
            super(view);
            movieTitle = view.findViewById(R.id.movie_title);
            movieOverview = view.findViewById(R.id.overview);
            movieDetails = view.findViewById(R.id.movie_detials);
            poster = view.findViewById(R.id.movie_poster);
        }

        public TextView getMovieTitle() { return movieTitle; }

        public TextView getMovieOverview() { return movieOverview; }

        public TextView getMovieDetails() { return movieDetails; }

        public ImageView getPoster() { return poster; }

        public void bind(final Popmovie item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getMovieTitle().setText( data_set[position].title );

        holder.getMovieOverview().setText(data_set[position].overview);

        String url = imageSizeURL.concat(data_set[position].poster_path);
        final String path = imageSizeURL.concat(data_set[position].poster_path).replace("http","https");
        Log.d("IMAGE PATH", path);
        Picasso.with(context)
                .load(path)
                .into(holder.getPoster());

        String grs = "";
        for(int id: data_set[position].genre_ids) {
            grs += apiHelper.getByGenreId(id).name.concat(", ");
        }
        grs = grs.substring(0, grs.length()-2);

        holder.getMovieDetails().setText(grs);

        holder.bind(data_set[position], listener);
    }

    @Override
    public int getItemCount() {
        return data_set.length;
    }
}
