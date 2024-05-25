package com.example.twohand_project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.twohand_project.Model.Model;
import com.example.twohand_project.Model.Post;
import com.example.twohand_project.databinding.FragmentEditPostBinding;


public class EditPostFragment extends Fragment {

    FragmentEditPostBinding binding;
    public EditPostFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentEditPostBinding.inflate(inflater,container,false);
        View view=binding.getRoot();
        String id=EditPostFragmentArgs.fromBundle(getArguments()).getId();
        Model.instance().getPostById(id,(post)->{//To_Do
             });
        binding.saveBtn.setOnClickListener(v -> {
            String price=binding.priceEt.getText().toString();
            String description=binding.descriptionEt.getText().toString();
            Model.instance().updatePost(price,description);
            Navigation.findNavController(v).popBackStack(R.id.feedListFragment,false);
        });

        return view;
    }
}