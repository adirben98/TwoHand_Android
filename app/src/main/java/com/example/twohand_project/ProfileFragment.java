package com.example.twohand_project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.twohand_project.Adapters.ListAdapter;
import com.example.twohand_project.Adapters.OnItemClickListener;
import com.example.twohand_project.Adapters.ProfileListAdapter;
import com.example.twohand_project.Model.Model;
import com.example.twohand_project.Model.User;
import com.example.twohand_project.databinding.FragmentProfileBinding;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    User user;
    ProfileViewModel viewModel;
    View view;
    ProfileListAdapter adapter;
    UserViewModel userViewModel;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel=new ViewModelProvider(this).get(ProfileViewModel.class);
        userViewModel=new ViewModelProvider(this).get(UserViewModel.class);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        binding.recyclerList.setHasFixedSize(true);
        binding.recyclerList.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter=new ProfileListAdapter(viewModel.getData(), getLayoutInflater());
        adapter.SetItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int pos) {
                ProfileFragmentDirections.ActionProfileFragmentToPostFragment action = ProfileFragmentDirections.actionProfileFragmentToPostFragment(viewModel.getData().get(pos).id);
                Navigation.findNavController(view).navigate(action);
            }
        });
        binding.recyclerList.setAdapter(adapter);

        userViewModel.getUser().observe(getViewLifecycleOwner(),newUser->{
            if (newUser!=null) {
                user = newUser;
                setProfile();
            }
        }
        );

        return view;
    }
    public void setProfile(){
        binding.usernameTextView.setText(user.username);
        binding.locationTextView.setText(user.location);
        binding.numberTv.setText(user.number);
        if(!user.userImg.equals(""))
            Picasso.get().load(user.userImg).into(binding.avatarImageView);
        else{
            binding.avatarImageView.setImageResource(R.drawable.avatar);
        }

        Model.instance().getUserPosts(user.username, (posts) -> {
            viewModel.setData(posts);
            adapter.setData(posts);
        });

            binding.editButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_profileFragment_to_editProfileFragment));
            binding.logOutTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Logout")
                            .setMessage("Are you sure you want to log out? ")
                            .setPositiveButton("yes", (dialog,which)->{
                                Model.instance().logout();
                                    Intent intent=new Intent(getActivity(),RegisterLoginActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();

                            }).setNegativeButton("No",(dialog,which)->{})
                            .create().show();
                }
            });

    }
    }



