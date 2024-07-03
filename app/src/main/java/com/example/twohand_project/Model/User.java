package com.example.twohand_project.Model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.twohand_project.MyApplication;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Entity
@TypeConverters(ListConverter.class)
public class User {
    @PrimaryKey
    @NonNull
    public String email;
    public String username;

    public String userImg;
    public String location;
    public String number;
    public List<String> favorites;



    public Long lastUpdated;


    public User( String username,String email, String userImg, String location, String number, List<String> favorites) {
        this.email = email;
        this.username = username;
        this.userImg = userImg;
        this.location = location;
        this.number = number;
        this.favorites = favorites;
    }
    public User(){}

    static void setUserlastUpdate(Long localLastUpdated){
        MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).edit().putLong("USER_Last_Update",localLastUpdated).commit();
    }
    static Long getUserlastUpdate(){

        long time = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("USER_Last_Update", 0);
        return time;
    }

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
    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public static User fromJson(Map<String, Object> data) {
        String username=(String)data.get("username");
        String email=(String)data.get("email");
        String userImg=(String)data.get("userImg");
        String location=(String)data.get("location");
        String number=(String) data.get("number");
        List<String> favorites=(List<String>) data.get("favorites");

        User user=new User(username,email,userImg,location,number,favorites);

        Timestamp ts=(Timestamp)data.get("lastUpdated");
        assert ts != null;
        user.setLastUpdated(ts.getSeconds());
        return user;
    }

    public static Map<String, Object> toJson(User user){
        Map<String, Object> json=new HashMap<>();
        json.put("username",user.username);
        json.put("email",user.email);
        json.put("userImg",user.userImg);
        json.put("location",user.location);
        json.put("number",user.number);
        json.put("favorites",user.favorites);
        json.put("lastUpdated", FieldValue.serverTimestamp());
        return json;
    }
}
