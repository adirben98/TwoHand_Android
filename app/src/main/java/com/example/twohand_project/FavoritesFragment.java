package com.example.twohand_project;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.twohand_project.Adapters.ListAdapter;
import com.example.twohand_project.Model.Model;
import com.example.twohand_project.databinding.FragmentFavoritesBinding;


public class FavoritesFragment extends Fragment {
FragmentFavoritesBinding binding;
FavoriteViewModel viewModel;
UserViewModel userViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentFavoritesBinding.inflate(inflater,container,false);
        viewModel=new ViewModelProvider(this).get(FavoriteViewModel.class);



        RecyclerView list=binding.list;
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));


        userViewModel.getUser().observe(getViewLifecycleOwner(),(user)->{
            ListAdapter adapter = new ListAdapter(user,viewModel.getFavorites(), getLayoutInflater(), getContext());
            list.setAdapter(adapter);

            Model.instance().getFavoritesPosts(user.favorites,(posts)->{
                viewModel.setFavorites(posts);
                adapter.setData(posts);

            });
        });
        return binding.getRoot();
    }
}