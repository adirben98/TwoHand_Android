package com.example.twohand_project;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.twohand_project.Model.Model;
import com.example.twohand_project.Model.User;

public class UserViewModel extends ViewModel {
    static private LiveData<User> user=Model.instance().getLoggedUser();
    static public LiveData<User> getUser() {
        return user;
    }
}
