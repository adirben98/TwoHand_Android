package com.example.twohand_project;

import androidx.lifecycle.LiveData;

import com.example.twohand_project.Model.Model;
import com.example.twohand_project.Model.Post;

import java.util.List;

public class ProfileViewModel {
    LiveData<List<Post>> data= Model.instance().getUserPosts();
}
