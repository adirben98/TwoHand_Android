package com.example.twohand_project;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.twohand_project.Model.Model;
import com.example.twohand_project.Model.Post;

import java.util.List;

public class FeedListViewmodel extends ViewModel {
    private LiveData<List<Post>> list= Model.instance().getAllPosts();
    public LiveData<List<Post>> getList(){return list;}
}
