package com.example.twohand_project.Adapters;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.example.twohand_project.Model.Model;

import java.util.ArrayList;
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

    public static void setLocationSpinner(String location, Context context, Model.Listener<ArrayAdapter<String>> listener) {
        Model.instance().getLocations(cities->{
            if(location!=null)
                cities.add(0,location);
            ArrayAdapter<String> locationSpinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,cities);
            locationSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            listener.onComplete(locationSpinnerAdapter);
        });


    }
    public static ArrayAdapter<String> setSoldSpinner(Context context) {
        List<String> options=new ArrayList<>();
        options.add("Sold");
        options.add("Back To Stock");
        ArrayAdapter<String> adapter= new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
    public static ArrayAdapter<String> fakeAdapter(Context context){
        List<String> fakeList=new ArrayList<>();
        fakeList.add("Location");
        ArrayAdapter<String> locationSpinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,fakeList);
        locationSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return locationSpinnerAdapter;
    }

}
