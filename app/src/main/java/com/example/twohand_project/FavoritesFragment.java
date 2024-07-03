package com.example.twohand_project;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.twohand_project.Adapters.ListAdapter;
import com.example.twohand_project.Model.Model;
import com.example.twohand_project.Model.Post;
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
        userViewModel=new ViewModelProvider(this).get(UserViewModel.class);
        View view=binding.getRoot();

        binding.list.setHasFixedSize(true);
        binding.list.setLayoutManager(new LinearLayoutManager(getContext()));

        userViewModel.getUser().observe(getViewLifecycleOwner(),(user)->{
            ListAdapter adapter = new ListAdapter(user,viewModel.getFavorites(), getLayoutInflater(), getContext());

            binding.list.setAdapter(adapter);

            Model.instance().getFavoritesPosts(user.favorites,(posts)->{
                adapter.SetOnPhotoClickListener(pos -> {
                    Post post=posts.get(pos);
                    FavoritesFragmentDirections.ActionFavoritesFragmentToPostFragment action = FavoritesFragmentDirections.actionFavoritesFragmentToPostFragment(post.id);
                    Navigation.findNavController(view).navigate(action);

                });
                adapter.SetOnUsernameClickListener(pos->{
                    Post post=posts.get(pos);
                    FavoritesFragmentDirections.ActionFavoritesFragmentToOtherUserProfileFragment action = FavoritesFragmentDirections.actionFavoritesFragmentToOtherUserProfileFragment(post.owner);
                    Navigation.findNavController(view).navigate(action);
                });
                viewModel.setFavorites(posts);
                adapter.setData(posts);

            });
        });
        return view;
    }
}