package com.example.twohand_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.twohand_project.Adapters.ListAdapter;
import com.example.twohand_project.databinding.FragmentChoosePhotoBinding;


public class ChoosePhotoFragment extends Fragment {
    FragmentChoosePhotoBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityResultLauncher<Void> cameraAppLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap o) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentChoosePhotoBinding.inflate(inflater,container,false);
        View view=binding.getRoot();

        RecyclerView list=binding.RecyclerList;
        list.setHasFixedSize(true);
        list.setLayoutManager(new GridLayoutManager(getContext(),4));

        ListAdapter adapter = new ListAdapter(null, getLayoutInflater());
        list.setAdapter(adapter);

        ImageButton btn=binding.takePhotoBtn;


        return view;
    }
}