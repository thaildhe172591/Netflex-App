package com.example.netflex.model;

import java.util.List;

public class Movie {
    private int id;
    private String title;
    private String overview;
    private String posterPath;
    private String backdropPath;
    private String videoUrl;
    private String countryIso;
    private int runtime;
    private String releaseDate;
    private List<Actor> actors;
    private List<Integer> actorIds;

    public Movie(int id, String title, String overview, String posterPath, String backdropPath, String videoUrl, String countryIso, int runtime, String releaseDate, List<Actor> actors, List<Integer> actorIds) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.videoUrl = videoUrl;
        this.countryIso = countryIso;
        this.runtime = runtime;
        this.releaseDate = releaseDate;
        this.actors = actors;
        this.actorIds = actorIds;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public List<Integer> getActorIds() {
        return actorIds;
    }

    public void setActorIds(List<Integer> actorIds) {
        this.actorIds = actorIds;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getOverview() { return overview; }
    public void setOverview(String overview) { this.overview = overview; }

    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }

    public String getBackdropPath() { return backdropPath; }
    public void setBackdropPath(String backdropPath) { this.backdropPath = backdropPath; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public String getCountryIso() { return countryIso; }
    public void setCountryIso(String countryIso) { this.countryIso = countryIso; }

    public int getRuntime() { return runtime; }
    public void setRuntime(int runtime) { this.runtime = runtime; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }
}
