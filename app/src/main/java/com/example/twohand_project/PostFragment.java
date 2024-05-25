package com.example.twohand_project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.twohand_project.Model.Model;
import com.example.twohand_project.Model.Post;
import com.example.twohand_project.databinding.FragmentPostBinding;
import com.squareup.picasso.Picasso;


public class PostFragment extends Fragment {

    FragmentPostBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentPostBinding.inflate(inflater,container,false);
        View view=binding.getRoot();
        String id=PostFragmentArgs.fromBundle(getArguments()).getId();
        Model.instance().getPostById(id,(post)->{
            binding.Username.setText(post.owner);

            Picasso.get().load(post.postImg).into(binding.postImage);
            Picasso.get().load(post.ownerImg).into(binding.userImage);

            binding.Description.setText(post.description);
            binding.Price.setText(post.price);
            if (!post.sold){
                binding.sold.setVisibility(View.GONE);
            }
            //check if the owner of the post is he one who watching the post and set visibility
            binding.editBtn.setOnClickListener(Navigation.createNavigateOnClickListener(PostFragmentDirections.actionPostFragmentToEditPostFragment(id)));
        });



        return view;


    }
}