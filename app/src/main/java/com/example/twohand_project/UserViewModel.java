package com.example.twohand_project;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.twohand_project.Model.Model;
import com.example.twohand_project.Model.User;

public class UserViewModel extends ViewModel {
    private static MutableLiveData<User> user;

    public static LiveData<User> getUser() {
        if (user == null) {
            user = new MutableLiveData<>();
            loadUser();
        }
        return user;
    }

    private static void loadUser() {
        Model.instance().getLoggedUser(new Model.Listener<User>() {
            @Override
            public void onComplete(User data) {
                user.setValue(data);
            }
        });
    }

    public static void refreshUser() {
        loadUser();
    }
}
