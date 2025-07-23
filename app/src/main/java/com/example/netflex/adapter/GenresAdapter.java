package com.example.netflex.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netflex.R;
import com.example.netflex.model.Genre;

import java.util.List;

public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.GenreViewHolder> {

    private final List<Genre> genres;
    private final OnGenreClickListener listener;

    // Giao diện để xử lý sự kiện click
    public interface OnGenreClickListener {
        void onGenreClick(Genre genre);
    }

    public GenresAdapter(List<Genre> genres, OnGenreClickListener listener) {
        this.genres = genres;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_genre, parent, false);
        return new GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        Genre genre = genres.get(position);
        holder.bind(genre, listener);
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    static class GenreViewHolder extends RecyclerView.ViewHolder {
        TextView textGenre;

        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);
            textGenre = itemView.findViewById(R.id.textGenre);
        }

        public void bind(Genre genre, OnGenreClickListener listener) {
            textGenre.setText(genre.getName());
            itemView.setOnClickListener(v -> listener.onGenreClick(genre));
        }
    }
}
