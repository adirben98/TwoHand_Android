package com.example.twohand_project.Model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CitiesApi {
    @GET("searchJSON")
    Call<GeoNamesResponse> getCities(
            @Query("country") String country,
            @Query("featureClass") String featureClass,
            @Query("maxRows") int maxRows,
            @Query("username") String username
    );
}
