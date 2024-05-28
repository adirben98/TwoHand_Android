package com.example.twohand_project.Adapters;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.example.twohand_project.Model.Model;

import java.util.List;

public class SpinnersAdapters {
    public static ArrayAdapter<String> setClothKindsSpinner(Context context){
        List<String> clothesKind=Model.instance().getAllClothesKinds();
        ArrayAdapter<String> clothKindSpinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,clothesKind);
        clothKindSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return  clothKindSpinnerAdapter;
    };
    public static ArrayAdapter<String> setColorsSpinner( Context context){

        List<String> colors= Model.instance().getAllColors();
        ArrayAdapter<String> colorSpinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,colors);
        colorSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return  colorSpinnerAdapter;
    };

    public static ArrayAdapter<String> setLocationSpinner(Context context) {
        List<String> locations=Model.instance().getLocations();
        ArrayAdapter<String> locationSpinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,locations);
        locationSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return locationSpinnerAdapter;

    }
}
