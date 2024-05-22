package com.example.twohand_project.Model;

import java.util.LinkedList;
import java.util.List;

public class Model {

    private final static Model _instance=new Model();
    List<Post> data=new LinkedList<>();
    private Model(){};

    public static Model instance(){return _instance;}
    public List<Post> getAllPosts(){return data;}
    public void addPost(Post post){}

    public List<Post> getCategoryPosts(String clothKind, String color) {
        return null;
    }

    public List<String> getAllClothesKinds() {
        return null;
    }

    public List<String> getAllColors() {
        return null;
    }
}
