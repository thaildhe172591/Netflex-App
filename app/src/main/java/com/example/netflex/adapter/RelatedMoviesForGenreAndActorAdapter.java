package com.example.netflex.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.netflex.R;
import com.example.netflex.model.Movie;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class RelatedMoviesForGenreAndActorAdapter extends RecyclerView.Adapter<RelatedMoviesForGenreAndActorAdapter.MovieViewHolder> {

    private List<Movie> movies = new ArrayList<>();
    private final Context context;
    private OnMovieClickListener listener;

    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    public void setOnMovieClickListener(OnMovieClickListener listener) {
        this.listener = listener;
    }

    public RelatedMoviesForGenreAndActorAdapter(Context context) {
        this.context = context;
    }

    public void setMovies(List<Movie> list) {
        this.movies = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_genre_detail, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);

        String formattedDate = formatDate(movie.getReleaseDate());
        String info = formattedDate + " | " + movie.getCountryIso() + " | " + movie.getRuntime() + " min";

        holder.textTitle.setText(movie.getTitle());
        holder.txtMovieInfo.setText(info);
        holder.txtMovieDescription.setText(movie.getOverview());

        Glide.with(context)
                .load(movie.getPosterPath())
                .placeholder(R.drawable.default_poster_placeholder)
                .into(holder.imgMoviePoster);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMovieClick(movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMoviePoster;
        TextView textTitle;
        TextView txtMovieInfo;
        TextView txtMovieDescription;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMoviePoster = itemView.findViewById(R.id.imgMoviePoster);
            textTitle = itemView.findViewById(R.id.txtMovieTitle);
            txtMovieInfo = itemView.findViewById(R.id.txtMovieInfo);
            txtMovieDescription = itemView.findViewById(R.id.txtMovieDescription);
        }
    }

    private String formatDate(String isoDate) {
        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date date = isoFormat.parse(isoDate);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            return outputFormat.format(date);
        } catch (ParseException | NullPointerException e) {
            return "Unknown";
        }
    }
}
