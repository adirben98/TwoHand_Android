package com.example.twohand_project.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GeoNamesResponse {
    @SerializedName("geonames")
    private List<City> geonames;

    public List<City> getGeonames() {
        return geonames;
    }
}
