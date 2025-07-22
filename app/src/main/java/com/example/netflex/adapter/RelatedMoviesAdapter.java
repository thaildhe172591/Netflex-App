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

import java.util.ArrayList;
import java.util.List;

public class RelatedMoviesAdapter extends RecyclerView.Adapter<RelatedMoviesAdapter.MovieViewHolder> {
    private List<Movie> movies = new ArrayList<>();
    private Context context;

    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    private OnMovieClickListener listener;

    public void setOnMovieClickListener(OnMovieClickListener listener) {
        this.listener = listener;
    }

    public RelatedMoviesAdapter(Context context) {
        this.context = context;
    }

    public void setMovies(List<Movie> list) {
        this.movies = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_related_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.textTitle.setText(movie.getTitle());
        Glide.with(context).load(movie.getPosterPath()).into(holder.imagePoster);

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
        ImageView imagePoster;
        TextView textTitle;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePoster = itemView.findViewById(R.id.imagePoster);
            textTitle = itemView.findViewById(R.id.textTitle);
        }
    }
}
