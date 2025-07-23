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
import com.example.netflex.model.Actor;

import java.util.List;

public class ActorAdapter extends RecyclerView.Adapter<ActorAdapter.ActorViewHolder> {

    private List<Actor> actors;
    private final OnActorClickListener listener;

    // Interface giống OnMovieClickListener
    public interface OnActorClickListener {
        void onActorClick(Actor actor);
    }

    public ActorAdapter(List<Actor> actors, OnActorClickListener listener) {
        this.actors = actors;
        this.listener = listener;
    }


    public void setActors(List<Actor> list) {
        this.actors = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ActorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_actor, parent, false);
        return new ActorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActorViewHolder holder, int position) {
        Actor actor = actors.get(position);
        holder.bind(actor, listener);
    }

    @Override
    public int getItemCount() {
        return actors != null ? actors.size() : 0;
    }

    static class ActorViewHolder extends RecyclerView.ViewHolder {
        ImageView imageActor;
        TextView textActorName;

        ActorViewHolder(View itemView) {
            super(itemView);
            imageActor = itemView.findViewById(R.id.imageActor);
            textActorName = itemView.findViewById(R.id.textActorName);
        }

        void bind(Actor actor, OnActorClickListener listener) {
            textActorName.setText(actor.getName());
            Glide.with(itemView.getContext()).load(actor.getImage()).into(imageActor);
            itemView.setOnClickListener(v -> listener.onActorClick(actor));
        }
    }
}
