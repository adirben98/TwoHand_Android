package com.example.twohand_project.Model;

import com.google.gson.annotations.SerializedName;

public class City {
    @SerializedName("toponymName")
    private String name;

    public String getName() {
        return name;
    }
}
