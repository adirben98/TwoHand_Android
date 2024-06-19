package com.example.twohand_project;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.twohand_project.Model.Model;
import com.example.twohand_project.Model.Post;

import java.util.List;

public class FeedListViewModel extends ViewModel {
    private LiveData<List<Post>> list;
    public LiveData<List<Post>> getList(String username){
        if (list==null){
            list= Model.instance().getAllPosts(username);
        }

        return list;
    }
}
