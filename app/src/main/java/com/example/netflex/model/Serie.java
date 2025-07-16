package com.example.netflex.model;

public class Serie {
    private int id;
    private String name;
    private String overview;
    private String posterPath;
    private String backdropPath;
    private String countryIso;
    private String firstAirDate;
    private String lastAirDate;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getCountryIso() {
        return countryIso;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public String getLastAirDate() {
        return lastAirDate;
    }
}
