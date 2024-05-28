package com.example.twohand_project.Model;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {

    private final static Model _instance=new Model();
    AppLocalDbRepository localDb=AppLocalDb.getDb();
    FirebaseModel firebaseModel=new FirebaseModel();
    Executor executor= Executors.newSingleThreadExecutor();
    public Handler mainHandler= HandlerCompat.createAsync(Looper.getMainLooper());
    private Model(){};

    public static Model instance(){return _instance;}

    public void register(User newUser,String password,Listener<Void> listener){
        firebaseModel.register(newUser,password,listener);
    }

    public void isEmailTaken(String email,Listener<Boolean> listener) {
         firebaseModel.isEmailTaken(email,listener);

    }

    public void isUsernameTaken(String username, Listener<Boolean> listener) {
        firebaseModel.isUsernameTaken(username,listener);
    }

    public void logIn(String username, String password, Listener<Boolean> listener) {

        firebaseModel.logIn(username,password,listener);
    }


    public interface Listener<T>{
        void onComplete(T data);
    }
    private LiveData<List<Post>> postList;
    public LiveData<List<Post>> getAllPosts() {
        if(postList == null){
            postList = localDb.postDao().getAll();
            refreshAllPosts();
        }
        return postList;
    }
    public void refreshAllPosts(){
        Long localLastUpdated= Post.getPOSTlastUpdate();
        firebaseModel.getAllPostsSince(localLastUpdated,(posts)->{
            executor.execute(()->{
                helperFunc(localLastUpdated,posts);

            });

        });
    }
    public void helperFunc(Long localLastUpdated,List<Post> posts){
        Long time=localLastUpdated;
        for (Post post : posts) {
            localDb.postDao().insert(post);
            if (post.lastUpdated > time) {
                time=post.lastUpdated;
            }
        }
        Post.setPOSTlastUpdate(time);
    }
    public boolean isLoggedIn(){
        return firebaseModel.isLoggedIn();
    }
    public void getLoggedUser(Listener<User> listener){
        firebaseModel.getLoggedUser(listener);
    }

    public void addPost(Post post,Listener<Void> listener){
        firebaseModel.addPost(post,listener);
    }


    public void getPostsByCategories(String clothKind, String color,Listener<List<Post>> listener) {
        Long localLastUpdate=Post.getPOSTlastUpdate();
        firebaseModel.getPostsByCategories(localLastUpdate,clothKind,color,(posts)->{
            executor.execute(()->{
                helperFunc(localLastUpdate,posts);
                List<Post> data=localDb.postDao().getPostsByCategories(color,clothKind);
                mainHandler.post(()->listener.onComplete(data));}
            );
        });


    }

    public List<String> getAllClothesKinds() {
        List<String> kinds=new ArrayList<>();
        kinds.add("Shoes");
        kinds.add("Jeans");
        kinds.add("T-Shirt");
        kinds.add("Pants");
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
    public List<String> getLocations() {
        List<String> cities=new ArrayList<>();
        cities.add("Tel-Aviv");
        return cities;
    }


    public void getPostById(String id,Listener<Post> listener) {
        firebaseModel.getPostById(id, listener);

    }

    public void updatePost(String price, String description) {


    }




    public void uploadImage(String id, Bitmap bitmap, Listener<String> listener) {
        firebaseModel.uploadPhoto(id,bitmap,listener);
    }

}
