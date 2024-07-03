package com.example.twohand_project;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.twohand_project.Adapters.OnItemClickListener;
import com.example.twohand_project.Adapters.ProfileListAdapter;
import com.example.twohand_project.Model.Model;
import com.example.twohand_project.Model.User;
import com.example.twohand_project.databinding.FragmentOtherUserProfileBinding;
import com.example.twohand_project.databinding.FragmentProfileBinding;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class OtherUserProfileFragment extends Fragment {

    FragmentOtherUserProfileBinding binding;
    User user;
    OtherProfileViewModel viewModel;
    View view;
    ProfileListAdapter adapter;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel=new ViewModelProvider(this).get(OtherProfileViewModel.class);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOtherUserProfileBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        binding.recyclerList.setHasFixedSize(true);
        binding.recyclerList.setLayoutManager(new GridLayoutManager(getContext(), 3));
        String otherUsername=OtherUserProfileFragmentArgs.fromBundle(getArguments()).getUsername();

            Model.instance().getOtherUser(otherUsername).observe(getViewLifecycleOwner(), otherUser-> {
                if (otherUser != null) {
                    user = otherUser;
                    setProfile();
                }

            });
        return view;
    }

    public void setProfile(){
        binding.usernameTextView.setText(user.username);
        binding.locationTextView.setText(user.location);
        binding.numberTv.setText(user.number);
        if (Objects.equals(user.userImg, "")) {
            binding.avatarImageView.setImageResource(R.drawable.avatar);
        }
        else{
            Picasso.get().load(user.userImg).into(binding.avatarImageView);

        }


        viewModel.getData(user.username).observe(getViewLifecycleOwner(), (posts) -> {
            adapter=new ProfileListAdapter(posts, getLayoutInflater());
            binding.recyclerList.setAdapter(adapter);

            adapter.SetItemClickListener(new OnItemClickListener() {
                @Override
                public void onClick(int pos) {
                    OtherUserProfileFragmentDirections.ActionOtherUserProfileFragmentToPostFragment action = OtherUserProfileFragmentDirections.actionOtherUserProfileFragmentToPostFragment(posts.get(pos).id);
                    Navigation.findNavController(view).navigate(action);
                }
            });



        });


    }
}