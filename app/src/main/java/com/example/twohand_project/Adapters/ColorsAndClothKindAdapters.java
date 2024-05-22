package com.example.twohand_project.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.twohand_project.Model.Model;

import java.util.List;

public class ColorsAndClothKindAdapters {
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
}
