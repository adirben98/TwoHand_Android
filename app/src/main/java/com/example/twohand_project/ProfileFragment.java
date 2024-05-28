package com.example.twohand_project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.twohand_project.Adapters.ListAdapter;
import com.example.twohand_project.Model.Model;
import com.example.twohand_project.databinding.FragmentProfileBinding;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment {
FragmentProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentProfileBinding.inflate(inflater,container,false);

        binding.recyclerList.setHasFixedSize(true);
        binding.recyclerList.setLayoutManager(new GridLayoutManager(getContext(),3));
        //binding.recyclerList.setAdapter(new ListAdapter());


        Model.instance().getLoggedUser((user)->{
            binding.usernameTextView.setText(user.username);
            binding.locationTextView.setText(user.location);
            binding.numberTv.setText(user.number);
            Picasso.get().load(user.userImg).into(binding.avatarImageView);
            ProfileFragmentDirections. ActionProfileFragmentToEditProfileFragment action = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment(user.username);
            binding.editButton.setOnClickListener(Navigation.createNavigateOnClickListener(action));

        });
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}