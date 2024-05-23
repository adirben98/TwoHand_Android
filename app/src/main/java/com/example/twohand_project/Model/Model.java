package com.example.twohand_project.Model;

import android.graphics.Bitmap;

import java.util.ArrayList;
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
        List<String> kinds=new ArrayList<>();
        kinds.add("red");
        kinds.add("yellow");
        kinds.add("blue");
        kinds.add("green");
        return kinds;
    }

    public List<String> getAllColors() {
        List<String> colors=new ArrayList<>();
        colors.add("red");
        colors.add("yellow");
        colors.add("blue");
        colors.add("green");
        return colors;
    }
    public interface uploadImageListener{
        void onComplete(String uri);
    }

    public void uploadImage(String id, Bitmap bitmap, uploadImageListener listener) {
    }
}
