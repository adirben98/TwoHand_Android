package com.example.twohand_project;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.twohand_project.Model.Model;
import com.example.twohand_project.Model.User;

public class UserViewModel extends ViewModel {
    private  LiveData<User> user;

    public  LiveData<User> getUser() {
        user=Model.instance().getLoggedUser();
        return user;
    }

    private  void loadUser() {
    }

    public  void refreshUser() {
        loadUser();
    }
}
