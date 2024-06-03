package com.example.twohand_project;

import androidx.lifecycle.ViewModel;

import com.example.twohand_project.Model.Post;

import java.util.List;

public class OtherProfileViewModel extends ViewModel {
    private List<Post> data;

    public List<Post> getData() {
        return data;
    }

    public void setData(List<Post> data) {
        this.data = data;
    }
}
