package com.example.twohand_project;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.twohand_project.Model.Model;
import com.example.twohand_project.Model.Post;

import java.util.List;

public class OtherProfileViewModel extends ViewModel {
    private LiveData<List<Post>> data;

    public LiveData<List<Post>> getData(String username) {
        data= Model.instance().getUserPosts(username);
        return data;
    }
}
