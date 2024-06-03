package com.example.twohand_project.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Entity
public class User {

    public String email;
    public String username;

    public String userImg;
    public String location;
    public String number;
    public List<String> favorites;

    public User( String username,String email, String userImg, String location, String number, List<String> favorites) {
        this.email = email;
        this.username = username;
        this.userImg = userImg;
        this.location = location;
        this.number = number;
        this.favorites = favorites;
    }
    public User(){}

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<String> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<String> favorites) {
        this.favorites = favorites;
    }

    public static User fromJson(Map<String, Object> data) {
        String username=(String)data.get("username");
        String email=(String)data.get("email");
        String userImg=(String)data.get("userImg");
        String location=(String)data.get("location");
        String number=(String) data.get("number");
        List<String> favorites=(List<String>) data.get("favorites");
        return new User(username,email,userImg,location,number,favorites);
    }
    public static Map<String, Object> toJson(User user){
        Map<String, Object> json=new HashMap<>();
        json.put("username",user.username);
        json.put("email",user.email);
        json.put("userImg",user.userImg);
        json.put("location",user.location);
        json.put("number",user.number);
        json.put("favorites",user.favorites);

        return json;

    }
}
