package com.example.twohand_project;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.twohand_project.Adapters.SpinnersAdapters;
import com.example.twohand_project.Adapters.ListAdapter;
import com.example.twohand_project.Model.Model;
import com.example.twohand_project.Model.Post;
import com.example.twohand_project.databinding.FragmentCategoryBinding;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


public class CategoryFragment extends Fragment {
    String color;
    String clothKind;
    String location;
    Spinner clothKindSpinner;
    ImageButton search;
    RecyclerView list;
    CategoryViewModel viewModel;

    FragmentCategoryBinding binding;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel=new ViewModelProvider(this).get(CategoryViewModel.class);

    }

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
        binding.location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                location=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.location.setAdapter(SpinnersAdapters.setLocationSpinner(getContext()));

        binding.clothKind.setAdapter(SpinnersAdapters.setClothKindsSpinner(getContext()));


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

        list=binding.recyclerList;
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));

        UserViewModel.getUser().observe(getViewLifecycleOwner(),(user)->{
            ListAdapter adapter = new ListAdapter(user,viewModel.getData(), getLayoutInflater(),getContext());
            list.setAdapter(adapter);

            binding.searchBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (validateInput()) {
                                Model.instance().getPostsByCategory(clothKind,color,(data)->{
                                    viewModel.setData(data);
                                    adapter.setData(data);
                                });
                            }
                        }
                    });
        });





        return view;
    }

    public void makeAToast(String text){
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }
    public boolean validateInput(){
        if (Objects.equals(clothKind, "Kind")) {
            makeAToast("Please choose a the kind of cloth");
            return false;
        }
        else if (Objects.equals(color, "Color")) {
            makeAToast("Please choose a color");
            return false;
        }


        return true;
    }
}
