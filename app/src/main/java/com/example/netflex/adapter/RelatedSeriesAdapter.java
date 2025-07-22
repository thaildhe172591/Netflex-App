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
import com.example.netflex.model.Serie;

import java.util.ArrayList;
import java.util.List;

public class RelatedSeriesAdapter extends RecyclerView.Adapter<RelatedSeriesAdapter.SerieViewHolder> {
    private List<Serie> series = new ArrayList<>();
    private Context context;

    public interface OnSerieClickListener {
        void onSerieClick(Serie serie);
    }

    private OnSerieClickListener listener;

    public void setOnSerieClickListener(OnSerieClickListener listener) {
        this.listener = listener;
    }

    public RelatedSeriesAdapter(Context context) {
        this.context = context;
    }

    public void setSeries(List<Serie> list) {
        this.series = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SerieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_related_serie, parent, false);
        return new SerieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SerieViewHolder holder, int position) {
        Serie serie = series.get(position);
        holder.textTitle.setText(serie.getName());
        Glide.with(context).load(serie.getPosterPath()).into(holder.imagePoster);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSerieClick(serie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return series.size();
    }

    static class SerieViewHolder extends RecyclerView.ViewHolder {
        ImageView imagePoster;
        TextView textTitle;

        public SerieViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePoster = itemView.findViewById(R.id.imagePoster);
            textTitle = itemView.findViewById(R.id.textTitle);
        }
    }
}
