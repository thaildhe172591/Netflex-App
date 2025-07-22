package com.example.netflex.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date; // Assuming BirthDate will be parsed into a Date object

public class Actor {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image; // URL to the actor's image

    @SerializedName("gender")
    private String gender;

    @SerializedName("birthDate")
    private String birthDate; // Or Date, if you parse it. String is safer for raw API data.

    @SerializedName("biography")
    private String biography;

    // Constructors
    public Actor() {
    }

    public Actor(int id, String name, String image, String gender, String birthDate, String biography) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.gender = gender;
        this.birthDate = birthDate;
        this.biography = biography;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
}
