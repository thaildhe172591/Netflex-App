package com.example.netflex.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class EpisodeDetail {
    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String name;

    @SerializedName("episodeNumber")
    private Integer episodeNumber;

    @SerializedName("overview")
    private String overview;

    @SerializedName("videoUrl")
    private String videoUrl;

    @SerializedName("runtime")
    private Integer runtime;

    @SerializedName("airDate")
    private Date airDate;

    @SerializedName("seriesId")
    private Long seriesId;

    @SerializedName("seriesName")
    private String seriesName;

    @SerializedName("posterPath")
    private String posterPath;

    @SerializedName("actors")
    private List<Actor> actors;

    public EpisodeDetail() {
    }

    public EpisodeDetail(Long id, String name, Integer episodeNumber, String overview, String videoUrl, Integer runtime, Date airDate, Long seriesId, String seriesName, String posterPath, List<Actor> actors) {
        this.id = id;
        this.name = name;
        this.episodeNumber = episodeNumber;
        this.overview = overview;
        this.videoUrl = videoUrl;
        this.runtime = runtime;
        this.airDate = airDate;
        this.seriesId = seriesId;
        this.seriesName = seriesName;
        this.posterPath = posterPath;
        this.actors = actors;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getEpisodeNumber() { return episodeNumber; }
    public void setEpisodeNumber(Integer episodeNumber) { this.episodeNumber = episodeNumber; }
    public String getOverview() { return overview; }
    public void setOverview(String overview) { this.overview = overview; }
    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    public Integer getRuntime() { return runtime; }
    public void setRuntime(Integer runtime) { this.runtime = runtime; }
    public Date getAirDate() { return airDate; }
    public void setAirDate(Date airDate) { this.airDate = airDate; }
    public Long getSeriesId() { return seriesId; }
    public void setSeriesId(Long seriesId) { this.seriesId = seriesId; }
    public String getSeriesName() { return seriesName; }
    public void setSeriesName(String seriesName) { this.seriesName = seriesName; }
    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }
    public List<Actor> getActors() { return actors; }
    public void setActors(List<Actor> actors) { this.actors = actors; }
}
