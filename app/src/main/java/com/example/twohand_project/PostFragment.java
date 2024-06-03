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

import java.util.Objects;

public class PostFragment extends Fragment {

    FragmentPostBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPostBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        String id = PostFragmentArgs.fromBundle(getArguments()).getId();



        Model.instance().getPostById(id, (post) -> {
            binding.Username.setText(post.owner);
            Picasso.get().load(post.postImg).into(binding.postImage);
            Picasso.get().load(post.ownerImg).into(binding.userImage);
            binding.Description.setText(post.description);
            binding.Price.setText(post.price+"$");

            if (!post.sold) {
                binding.sold.setVisibility(View.GONE);
            }

            binding.ringingPhone.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + post.number));
                startActivity(intent);
            });

            User user=UserViewModel.getUser().getValue();
                if (user != null) {
                    ImageButton addToFavoriteBtn = binding.favoriteBtn;
                    if (user.favorites.contains(post.id)) {
                        addToFavoriteBtn.setImageResource(R.drawable.red_heart);
                    }

                    addToFavoriteBtn.setOnClickListener(v -> {
                        if (!user.favorites.contains(post.id)) {
                            user.favorites.add(post.id);
                            Model.instance().updateFavorites(user);
                            addToFavoriteBtn.setImageResource(R.drawable.red_heart);
                        } else {
                            user.favorites.remove(post.id);
                            Model.instance().updateFavorites(user);
                            addToFavoriteBtn.setImageResource(R.drawable.heart);
                        }
                    });

                    if (Objects.equals(post.owner, user.username)) {
                        binding.editBtn.setVisibility(View.VISIBLE);
                        binding.editBtn.setOnClickListener(Navigation.createNavigateOnClickListener(PostFragmentDirections.actionPostFragmentToEditPostFragment(id)));
                    } else {
                        binding.editBtn.setVisibility(View.GONE);
                    }
                    binding.deleteBtn.setOnClickListener((v)->{
                        new AlertDialog.Builder(getContext())
                                .setTitle("Warning!")
                                .setMessage("Are you sure you want to delete the post? ")
                                .setPositiveButton("yes", (dialog,which)->{
                                        Model.instance().deletePost(post,(unused)->{
                                            Navigation.findNavController(v).popBackStack(R.id.feedListFragment,false);
                                    });
                                }).setNegativeButton("No",(dialog,which)->{})
                                .create().show();

                    });

                }
            });


        return view;
    }
}
