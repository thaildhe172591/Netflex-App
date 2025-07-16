package com.example.netflex.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netflex.R;
import com.example.netflex.model.Genre;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GenreFilterAdapter extends RecyclerView.Adapter<GenreFilterAdapter.GenreViewHolder> {
    private List<Genre> genres;
    private Set<Genre> selectedGenres = new HashSet<>();

    private final OnGenreClickListener listener;

    public interface OnGenreClickListener {
        void onGenreChange(List<Genre> selectedGenres);
    }

    public GenreFilterAdapter(List<Genre> genreList, OnGenreClickListener listener) {
        this.genres = new ArrayList<>();
        this.genres.add(new Genre(0, "All Genre"));
        this.genres.addAll(genreList);
        this.listener = listener;
        this.selectedGenres.add(this.genres.get(0));
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genre_button, parent, false);
        return new GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        Genre genre = genres.get(position);
        holder.btnGenre.setText(genre.getName());

        boolean isSelected = selectedGenres.contains(genre);
        holder.btnGenre.setBackgroundTintList(ColorStateList.valueOf(
                isSelected ? Color.parseColor("#FFCC00") : Color.parseColor("#303040")
        ));

        holder.btnGenre.setOnClickListener(v -> {
            if (genre.getId() == 0) {
                selectedGenres.clear();
                selectedGenres.add(genre);
            } else {
                selectedGenres.removeIf(g -> g.getId() == 0);

                if (selectedGenres.contains(genre)) {
                    selectedGenres.remove(genre);
                } else {
                    selectedGenres.add(genre);
                }

                if (selectedGenres.isEmpty()) {
                    selectedGenres.add(genres.get(0));
                }
            }

            listener.onGenreChange(new ArrayList<>(selectedGenres));
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    static class GenreViewHolder extends RecyclerView.ViewHolder {
        Button btnGenre;
        GenreViewHolder(View view) {
            super(view);
            btnGenre = view.findViewById(R.id.btnGenre);
        }
    }
}
