package com.example.twohand_project;

import androidx.lifecycle.ViewModel;

import com.example.twohand_project.Model.Post;

import java.util.List;

public class FavoriteViewModel extends ViewModel {
    private List<Post> favorites;
    public List<Post> getFavorites(){
        return favorites;
    }

    public void setFavorites(List<Post> favorites) {
        this.favorites = favorites;
    }
}
