package com.example.twohand_project.Model;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.twohand_project.MyApplication;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity
public class Post {
    @PrimaryKey
    @NonNull
    public String id;
    public String owner;
    public String ownerImg;
    public String location;
    public String price;
    public String description;
    public String color;
    public String kind;
    public String postImg;
    public Boolean sold;
    public Long lastUpdated;



    public Post(){}

    public Post(String id,String owner, String ownerImg, String location,String kind,String color, String price, String description, String postImg,Boolean sold) {
        this.id=id;
        this.owner = owner;
        this.ownerImg = ownerImg;
        this.location = location;
        this.kind=kind;
        this.color=color;
        this.price = price;
        this.description = description;
        this.postImg = postImg;
        this.sold=sold;
    }
    static void setPOSTlastUpdate(Long localLastUpdated){
        MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).edit().putLong("POST_last_update",localLastUpdated).commit();
    }
    static Long getPOSTlastUpdate(){
        return MyApplication.getMyContext().getSharedPreferences("TAG",Context.MODE_PRIVATE).getLong("POST_last_update",0);
    }

    public static Post fromJson(Map<String, Object> document) {
        String id=(String) document.get("id");
        String owner=(String) document.get("owner");
        String ownerImg=(String) document.get("ownerImg");
        String location=(String) document.get("location");
        String kind=(String) document.get("kind");
        String color=(String) document.get("color");
        String price=(String) document.get("price");
        String description=(String) document.get("description");
        String postImg=(String) document.get("postImg");
        Boolean sold=(Boolean) document.get("sold");
        Post post=new Post(id,owner,ownerImg,location,kind,color,price,description,postImg,sold);
        Timestamp ts=(Timestamp)document.get("lastUpdated");
        post.setLastUpdated(ts.getSeconds());
        return post;

        
    }

    public static Map<String,Object> toJson(Post post) {
        Map<String,Object> json=new HashMap<>();
        json.put("id",post.id);
        json.put("owner",post.owner);
        json.put("ownerImg",post.ownerImg);
        json.put("location",post.location);
        json.put("kind",post.kind);
        json.put("color",post.color);
        json.put("price",post.price);
        json.put("description",post.description);
        json.put("postImg",post.postImg);
        json.put("sold",post.sold);
        json.put("lastUpdated", FieldValue.serverTimestamp());
        return json;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwnerImg() {
        return ownerImg;
    }

    public void setOwnerImg(String ownerImg) {
        this.ownerImg = ownerImg;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPostImg() {
        return postImg;
    }

    public void setPostImg(String postImg) {
        this.postImg = postImg;
    }

    public Boolean getSold() {
        return sold;
    }

    public void setSold(Boolean sold) {
        this.sold = sold;
    }

    public void setImageUrl(String url) {
        postImg=url;
    }
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
