package com.example.netflex.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.netflex.R;
import com.example.netflex.model.Serie;

import java.util.List;

public class SerieListAdapter extends RecyclerView.Adapter<SerieListAdapter.SerieViewHolder> {
    private List<Serie> serieList;
    private final OnSerieClickListener listener;

    public interface OnSerieClickListener {
        void onClick(Serie serie);
    }

    public SerieListAdapter(List<Serie> serieList, OnSerieClickListener listener) {
        this.serieList = serieList;
        this.listener = listener;
    }

    public void setSerieList(List<Serie> list) {
        this.serieList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SerieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_serie_grid, parent, false);
        return new SerieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SerieViewHolder holder, int position) {
        Serie serie = serieList.get(position);
        holder.bind(serie, listener);
    }

    @Override
    public int getItemCount() {
        return serieList != null ? serieList.size() : 0;
    }

    static class SerieViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;

        public SerieViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageSerie);
            title = itemView.findViewById(R.id.textSerieTitle);
        }

        public void bind(Serie serie, OnSerieClickListener listener) {
            title.setText(serie.getName());
            Glide.with(itemView.getContext()).load(serie.getPosterPath()).into(image);
            itemView.setOnClickListener(v -> listener.onClick(serie));
        }
    }
}
