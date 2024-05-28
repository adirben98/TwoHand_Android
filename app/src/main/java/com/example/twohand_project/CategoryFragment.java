package com.example.twohand_project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.twohand_project.Adapters.SpinnersAdapters;
import com.example.twohand_project.Adapters.ListAdapter;
import com.example.twohand_project.Model.Model;
import com.example.twohand_project.Model.Post;
import com.example.twohand_project.databinding.FragmentCategoryBinding;

import java.util.LinkedList;
import java.util.List;


public class CategoryFragment extends Fragment {
    String color;
    String clothKind;
    Spinner clothKindSpinner;
    ImageButton search;
    RecyclerView list;
    List<Post> data=new LinkedList<>();

    FragmentCategoryBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentCategoryBinding.inflate(inflater,container,false);
        View view=binding.getRoot();


        clothKindSpinner=binding.clothKind;
        clothKindSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clothKind=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.clothKind
                .setAdapter(SpinnersAdapters.setClothKindsSpinner(getContext()));


        binding.colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                color=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.colorSpinner
                .setAdapter( SpinnersAdapters.setColorsSpinner(getContext()));


        ListAdapter adapter = new ListAdapter(data, getLayoutInflater());
        list=binding.recyclerList;
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(adapter);


        search=binding.searchBtn;
        search.
    setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 Model.instance().getPostsByCategories(clothKind,color,(data)->{adapter.setData(data);});

            }
        });
        return view;
    }
}