package com.example.twohand_project;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.twohand_project.Adapters.ListAdapter;
import com.example.twohand_project.Model.Post;
import com.example.twohand_project.databinding.FragmentFeedListBinding;

public class FeedListFragment extends Fragment {
    ListAdapter adapter;

    FragmentFeedListBinding binding;
    FeedListViewModel viewModel;
    public FeedListFragment(){

    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(FeedListViewModel.class);

        binding=FragmentFeedListBinding.inflate(inflater,container,false);
        View view=binding.getRoot();

        binding.recyclerList.setHasFixedSize(true);
        binding.recyclerList.setLayoutManager(new LinearLayoutManager(getContext()));
        UserViewModel.getUser().observe(getViewLifecycleOwner(),(user)->{
            adapter = new ListAdapter(user,viewModel.getList().getValue(), getLayoutInflater(),getContext());
            binding.recyclerList.setAdapter(adapter);
            viewModel.getList().observe(getViewLifecycleOwner(),list->{
                adapter.setData(list);
            });
            adapter.SetItemClickListener(pos -> {
                Post post=viewModel.getList().getValue().get(pos);
                FeedListFragmentDirections.ActionFeedListFragmentToPostFragment action = FeedListFragmentDirections.actionFeedListFragmentToPostFragment(post.id);
                Navigation.findNavController(view).navigate(action);

            });
        });






        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}