package com.example.netflex.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.netflex.R;
import com.example.netflex.model.Genre;
import com.example.netflex.model.Movie;

import java.util.List;
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {
    private List<Movie> movieList;
    private final OnMovieClickListener listener;

    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    public MovieListAdapter(List<Movie> movieList, OnMovieClickListener listener) {
        this.movieList = movieList;
        this.listener = listener;
    }

    public void setMovieList(List<Movie> list) {
        this.movieList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_grid, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.bind(movie, listener);
    }

    @Override
    public int getItemCount() {
        return movieList != null ? movieList.size() : 0;
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageMovie);
            title = itemView.findViewById(R.id.textMovieTitle);
        }

        public void bind(Movie movie, OnMovieClickListener listener) {
            title.setText(movie.getTitle());
            Glide.with(itemView.getContext()).load(movie.getPosterPath()).into(image);
            itemView.setOnClickListener(v -> listener.onMovieClick(movie));
        }
    }
}

