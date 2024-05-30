package com.example.twohand_project;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.twohand_project.Model.Model;
import com.example.twohand_project.Model.Post;

import java.util.List;

public class CategoryViewModel extends ViewModel {

    private List<Post> data;

    public List<Post> getData() {
        return data;
    }
    void setData(List<Post> data){this.data=data;}


}
