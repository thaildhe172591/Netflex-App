package com.example.netflex.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class SerieDetail {
    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String name;

    @SerializedName("overview")
    private String overview;

    @SerializedName("posterPath")
    private String posterPath;

    @SerializedName("backdropPath")
    private String backdropPath;

    @SerializedName("countryIso")
    private String countryIso;

    @SerializedName("firstAirDate")
    private Date firstAirDate;

    @SerializedName("lastAirDate")
    private Date lastAirDate;

    public int getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(int totalReviews) {
        this.totalReviews = totalReviews;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    @SerializedName("averageRating")
    private double averageRating;

    @SerializedName("totalReviews")
    private int totalReviews;

    @SerializedName("keywords")
    private List<Keyword> keywords;

    @SerializedName("genres")
    private List<Genre> genres;

    public SerieDetail() {
    }

    public SerieDetail(long id, String name, String overview, String posterPath, String backdropPath, String countryIso, Date firstAirDate, Date lastAirDate, double averageRating, int totalReviews, List<Keyword> keywords, List<Genre> genres) {
        this.id = id;
        this.name = name;
        this.overview = overview;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.countryIso = countryIso;
        this.firstAirDate = firstAirDate;
        this.lastAirDate = lastAirDate;
        this.averageRating = averageRating;
        this.totalReviews = totalReviews;
        this.keywords = keywords;
        this.genres = genres;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getCountryIso() {
        return countryIso;
    }

    public void setCountryIso(String countryIso) {
        this.countryIso = countryIso;
    }

    public Date getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(Date firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public Date getLastAirDate() {
        return lastAirDate;
    }

    public void setLastAirDate(Date lastAirDate) {
        this.lastAirDate = lastAirDate;
    }

    public List<Keyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<Keyword> keywords) {
        this.keywords = keywords;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
}
