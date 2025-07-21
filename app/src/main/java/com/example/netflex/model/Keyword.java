package com.example.netflex.model;

import com.google.gson.annotations.SerializedName;

public class Keyword {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    // Constructors
    public Keyword() {
    }

    public Keyword(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
