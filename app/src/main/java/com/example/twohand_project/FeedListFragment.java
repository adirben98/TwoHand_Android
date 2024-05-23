package com.example.twohand_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.twohand_project.Adapters.ListAdapter;
import com.example.twohand_project.Model.Model;
import com.example.twohand_project.Model.Post;
import com.example.twohand_project.databinding.FragmentFeedListBinding;

import java.util.List;

public class FeedListFragment extends Fragment {

    FragmentFeedListBinding binding;
    public FeedListFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentFeedListBinding.inflate(inflater,container,false);
        View view=binding.getRoot();

        binding.recyclerList.setHasFixedSize(true);
        binding.recyclerList.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Post> data= Model.instance().getAllPosts();
        ListAdapter adapter = new ListAdapter(data, getLayoutInflater());
        binding.recyclerList.setAdapter(adapter);
        adapter.SetItemClickListener(pos -> {
            Post post=data.get(pos);
            FeedListFragmentDirections.ActionFeedListFragmentToPostFragment action = FeedListFragmentDirections.actionFeedListFragmentToPostFragment(post.id);
            Navigation.findNavController(view).navigate(action);




        });
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}