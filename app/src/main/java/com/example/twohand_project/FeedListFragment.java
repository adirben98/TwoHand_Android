package com.example.twohand_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.twohand_project.Adapters.ListAdapter;
import com.example.twohand_project.Model.Model;
import com.example.twohand_project.Model.Post;
import com.example.twohand_project.databinding.FragmentFeedListBinding;

import java.util.List;

public class FeedListFragment extends Fragment {

    FragmentFeedListBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentFeedListBinding.inflate(inflater,container,false);
        View view=binding.getRoot();

        binding.RecyclerView.setHasFixedSize(true);
        binding.RecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Post> data= Model.instance().getAllPosts();
        data.add(new Post("adir","","tel aviv","5$","nana",""));
        ListAdapter adapter = new ListAdapter(data, getLayoutInflater());
        binding.RecyclerView.setAdapter(adapter);
        adapter.SetItemClickListener(pos -> {

        });
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}