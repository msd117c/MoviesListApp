package com.vp.list.model;

import com.google.gson.annotations.SerializedName;

public class ListItem {
    @SerializedName("Title")
    private String title;
    @SerializedName("Year")
    private String year;
    // Task-6: Fix the issue by adding serializedName annotation (other option
    // would be not obfuscate this model)
    @SerializedName("imdbID")
    private String imdbID;
    @SerializedName("Poster")
    private String poster;

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getPoster() {
        return poster;
    }
}
