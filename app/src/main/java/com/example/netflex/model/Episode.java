package com.example.netflex.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Episode {
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

    public Episode() {
    }

    public Episode(String posterPath, String seriesName, Long seriesId, Date airDate, Integer runtime, String videoUrl, String overview, Integer episodeNumber, String name, Long id) {
        this.posterPath = posterPath;
        this.seriesName = seriesName;
        this.seriesId = seriesId;
        this.airDate = airDate;
        this.runtime = runtime;
        this.videoUrl = videoUrl;
        this.overview = overview;
        this.episodeNumber = episodeNumber;
        this.name = name;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public Date getAirDate() {
        return airDate;
    }

    public void setAirDate(Date airDate) {
        this.airDate = airDate;
    }

    public Long getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(Long seriesId) {
        this.seriesId = seriesId;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
}
