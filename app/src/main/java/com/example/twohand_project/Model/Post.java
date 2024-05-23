package com.example.twohand_project.Model;

public class Post {
    public final String id;
    public String owner;
    public String ownerImg;
    public String location;
    public String price;
    public String description;
    public String postImg;

    public Post(String id,String owner, String ownerImg, String location, String price, String description, String postImg) {
        this.id=id;
        this.owner = owner;
        this.ownerImg = ownerImg;
        this.location = location;
        this.price = price;
        this.description = description;
        this.postImg = postImg;
    }


    public void setImageUrl(String url) {
        postImg=url;
    }
}
