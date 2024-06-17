package com.example.twohand_project.Model;

import androidx.room.TypeConverter;

import java.util.Arrays;
import java.util.List;

public class ListConverter {
    @TypeConverter
    public String fromFavoritesList(List<String> favorites) {
        StringBuilder sb = new StringBuilder();
        for (String favorite : favorites) {
            sb.append(favorite);
            sb.append(",");
        }
        return sb.toString();
    }

    @TypeConverter
    public List<String> toFavoritesList(String data) {
        return Arrays.asList(data.split(","));
    }
}

