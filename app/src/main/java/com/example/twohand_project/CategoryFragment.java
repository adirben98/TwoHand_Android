package com.example.twohand_project;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import java.util.stream.Collectors;


public class CategoryFragment extends Fragment {
    String color;
    String clothKind;
    String location;
    Spinner clothKindSpinner;
    RecyclerView list;
    CategoryViewModel viewModel;
    UserViewModel userViewModel;
    FragmentCategoryBinding binding;
    ListAdapter adapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel=new ViewModelProvider(this).get(CategoryViewModel.class);
        userViewModel=new ViewModelProvider(this).get(UserViewModel.class);


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
        SpinnersAdapters.setLocationSpinner(null,getContext(),(adapter)->{
            binding.location.setAdapter(adapter);
        });

        binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.instance().getPostsByCategory(clothKind,color,location,(data)->{
                    viewModel.setData(data);
                    adapter.setData(data);
                });
            }
        });
        binding.location.setAdapter(SpinnersAdapters.fakeAdapter(getContext()));
        binding.clothKind.setAdapter(SpinnersAdapters.setClothKindsSpinner(getContext()));
        binding.colorSpinner.setAdapter( SpinnersAdapters.setColorsSpinner(getContext()));
        binding.colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                color=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        list=binding.recyclerList;
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));

        userViewModel.getUser().observe(getViewLifecycleOwner(),(user)->{
            adapter = new ListAdapter(user,viewModel.getData(), getLayoutInflater(),getContext());
            list.setAdapter(adapter);


        });
        return view;
    }





}
