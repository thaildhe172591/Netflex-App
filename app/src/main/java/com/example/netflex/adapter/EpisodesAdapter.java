package com.example.netflex.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netflex.R;
import com.example.netflex.model.Episode;

import java.util.List;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.EpisodeViewHolder> {

    private List<Episode> episodeList;
    private OnEpisodeClickListener listener;

    public interface OnEpisodeClickListener {
        void onEpisodeClick(Episode episode);
    }

    public void setOnEpisodeClickListener(OnEpisodeClickListener listener) {
        this.listener = listener;
    }

    public EpisodesAdapter(List<Episode> episodeList) {
        this.episodeList = episodeList;
    }

    @NonNull
    @Override
    public EpisodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_episode, parent, false);
        return new EpisodeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeViewHolder holder, int position) {
        Episode episode = episodeList.get(position);
        holder.bind(episode);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEpisodeClick(episode);
            }
        });
    }

    @Override
    public int getItemCount() {
        return episodeList != null ? episodeList.size() : 0;
    }

    public void setEpisodeList(List<Episode> newList) {
        this.episodeList = newList;
        notifyDataSetChanged();
    }

    static class EpisodeViewHolder extends RecyclerView.ViewHolder {
        private final TextView textEpisodeTitle;

        public EpisodeViewHolder(@NonNull View itemView) {
            super(itemView);
            textEpisodeTitle = itemView.findViewById(R.id.textEpisodeTitle);
        }

        public void bind(Episode episode) {
            String displayTitle = "Episode " + episode.getEpisodeNumber() + ": " + episode.getName();
            textEpisodeTitle.setText(displayTitle);
        }
    }
}

