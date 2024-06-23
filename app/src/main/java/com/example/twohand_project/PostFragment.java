package com.example.twohand_project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.twohand_project.Model.Model;
import com.example.twohand_project.Model.Post;
import com.example.twohand_project.Model.User;
import com.example.twohand_project.databinding.FragmentPostBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PostFragment extends Fragment {

    FragmentPostBinding binding;
    UserViewModel userViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPostBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        String id = PostFragmentArgs.fromBundle(getArguments()).getId();



        Model.instance().getPostById(id).observe(getViewLifecycleOwner(), (post) -> {
            binding.Username.setText(post.owner);
            binding.Location.setText(post.location);
            Picasso.get().load(post.postImg).into(binding.postImage);
            if (Objects.equals(post.ownerImg, "")) {
                binding.userImage.setImageResource(R.drawable.avatar);
            }
            else{
                Picasso.get().load(post.ownerImg).into(binding.userImage);

            }
            binding.Description.setText(post.description);
            binding.Price.setText(post.price+"$");

            if (!post.sold) {
                binding.sold.setVisibility(View.GONE);
            }
            else{
                binding.sold.setVisibility(View.VISIBLE);

            }

            binding.ringingPhone.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + post.number));
                startActivity(intent);
            });

            userViewModel.getUser().observe(getViewLifecycleOwner(),(user)->{
                if (user != null) {

                    if (Objects.equals(post.owner, user.username)) {



                        binding.ringingPhone.setVisibility(View.GONE);
                        binding.favoriteBtn.setVisibility(View.GONE);
                        binding.editBtn.setVisibility(View.VISIBLE);
                        binding.deleteBtn.setVisibility(View.VISIBLE);
                        binding.editBtn.setOnClickListener(Navigation.createNavigateOnClickListener(PostFragmentDirections.actionPostFragmentToEditPostFragment(id)));
                    } else {
                        binding.favoriteBtn.setVisibility(View.VISIBLE);
                        binding.ringingPhone.setVisibility(View.VISIBLE);
                        binding.editBtn.setVisibility(View.GONE);
                        binding.deleteBtn.setVisibility(View.GONE);
                        ImageButton addToFavoriteBtn = binding.favoriteBtn;
                        if (user.favorites.contains(post.id)) {
                            addToFavoriteBtn.setImageResource(R.drawable.red_heart);
                        }

                        addToFavoriteBtn.setOnClickListener(v -> {
                            List<String> mutableFavorites = new ArrayList<>(user.getFavorites());

                            if (!user.favorites.contains(post.id)) {
                                mutableFavorites.add(post.id);
                                user.setFavorites(mutableFavorites);

                                Model.instance().updateFavorites(user);
                                addToFavoriteBtn.setImageResource(R.drawable.red_heart);
                            }
                            else{
                                mutableFavorites.remove(post.id);
                                user.setFavorites(mutableFavorites);

                                Model.instance().updateFavorites(user);
                                addToFavoriteBtn.setImageResource(R.drawable.heart);
                            }
                        });


                    }




                    binding.deleteBtn.setOnClickListener((v)->{
                        new AlertDialog.Builder(getContext())
                                .setTitle("Warning!")
                                .setMessage("Are you sure you want to delete the post? ")
                                .setPositiveButton("yes", (dialog,which)->{
                                    Model.instance().deletePost(post,(unused)->{
                                        Navigation.findNavController(v).popBackStack(R.id.profileFragment,false);
                                    });
                                }).setNegativeButton("No",(dialog,which)->{})
                                .create().show();

                    });

                }

            });

            });


        return view;
    }
}
