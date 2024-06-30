package com.example.twohand_project.Model;


import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Model {

    private final static Model _instance=new Model();
    private AppLocalDbRepository localDb=AppLocalDb.getDb();
    private FirebaseModel firebaseModel=new FirebaseModel();
    private Executor executor= Executors.newSingleThreadExecutor();
    private Handler mainHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    final public MutableLiveData<LoadingState> EventFeedLoadingState=new MutableLiveData<>(LoadingState.Not_Loading);

    public String username;

    public enum LoadingState{
        Loading,
        Not_Loading
    }




    public interface Listener<T>{
        void onComplete(T data);
    }
    private Model(){
    };

    public static Model instance(){return _instance;}

    private LiveData<User> user;

    public LiveData<User> getLoggedUser(){

        String username=firebaseModel.getLoggedUserUsername();
        if (username!=null){
            this.username=username;
        }


        if (user==null){
            user=localDb.userDao().getUserByUsername(this.username);
            refreshAllUsers();

        }

        return user;

    }
    private LiveData<List<Post>> postList;
    public LiveData<List<Post>> getAllPosts() {
        String username= firebaseModel.getLoggedUserUsername();
        if(postList == null){

            postList = localDb.postDao().getAllWithoutUser(username);
            refreshAllPosts();
        }
        return postList;
    }
    public void refreshAllUsers(){
        Long localLastUpdated= User.getUserlastUpdate();
        firebaseModel.getAllUsersSince(localLastUpdated,(users)->{
            executor.execute(()->{
                Long time=localLastUpdated;
                for (User user : users) {
                    localDb.userDao().insert(user);
                    if (user.lastUpdated > time) {
                        time=user.lastUpdated;
                    }
                }
                User.setUserlastUpdate(time);

            });

        });
    }
    public void refreshAllPosts(){
        EventFeedLoadingState.setValue(LoadingState.Loading);
        Long localLastUpdated= Post.getPOSTlastUpdate();
        firebaseModel.getAllPostsSince(localLastUpdated,(posts)->{
            executor.execute(()->{
                helperFunc(localLastUpdated,posts);
                EventFeedLoadingState.postValue(LoadingState.Not_Loading);

            });

        });
    }
    public void refreshAllPostsNoProgressBar(){
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
    public void getPostsByCategory(String kind,String color,String location,Listener<List<Post>> listener){
        refreshAllPosts();
            executor.execute(()->{
                StringBuilder queryBuilder = new StringBuilder("SELECT * FROM Post WHERE 1=1");

                List<Object> args = new ArrayList<>();

                if (!Objects.equals(kind,"Kind")) {
                    queryBuilder.append(" AND kind = ?");
                    args.add(kind);
                }

                if (!Objects.equals(color,"Color")) {
                    queryBuilder.append(" AND color = ?");
                    args.add(color);
                }

                if (!Objects.equals(location,"Location")) {
                    queryBuilder.append(" AND location = ?");
                    args.add(location);
                }

                SimpleSQLiteQuery query = new SimpleSQLiteQuery(queryBuilder.toString(), args.toArray());
                List<Post> data= localDb.postDao().getPostsByQuery(query);
                mainHandler.post(()->listener.onComplete(data));
            });


    }
    LiveData<List<Post>> userPosts;
    public LiveData<List<Post>> getUserPosts(String username){
        userPosts=localDb.postDao().getUserPosts(username);
        return userPosts;



    }

    public void addPost(Post post,Listener<Void> listener){
        firebaseModel.addPost(post,listener);

    }
    LiveData<Post> post;
    public LiveData<Post> getPostById(String id) {


        post=localDb.postDao().getPostById(id);

        return post;


    }
    public void getFavoritesPosts(List<String> favorites,Listener<List<Post>> listener){


                executor.execute(()->{
                    List<Post> data=localDb.postDao().getFavorites(favorites);
                    mainHandler.post(()->{listener.onComplete(data);});
                });


    }

    public void updateFavorites(User user){
        firebaseModel.updateFavorites(user);
        refreshAllPostsNoProgressBar();

    }



    public List<String> getAllClothesKinds() {
        List<String> kinds=new ArrayList<>();
        kinds.add("Kind");
        kinds.add("Shoes");
        kinds.add("Jeans");
        kinds.add("T-Shirt");
        kinds.add("Pants");
        kinds.add("Other");
        return kinds;
    }

    public List<String> getAllColors() {
        List<String> colors=new ArrayList<>();
        colors.add("Color");
        colors.add("Red");
        colors.add("Yellow");
        colors.add("Blue");
        colors.add("Green");
        colors.add("Other");
        return colors;
    }
    public void getLocations(Listener<List<String>> listener) {
        List<String> data=new ArrayList<>();
        data.add("Location");
        RetrofitClient.getClient().create(CitiesApi.class).getCities("IL","P",1000,"adir")
                .enqueue(new Callback<GeoNamesResponse>() {
                    @Override
                    public void onResponse(Call<GeoNamesResponse> call, Response<GeoNamesResponse> response) {
                        List<City> cities = response.body().getGeonames();
                        for (City city : cities) {
                            data.add(city.getName());
                        }
                        mainHandler.post(()-> listener.onComplete(data));

                    }

                    @Override
                    public void onFailure(Call<GeoNamesResponse> call, Throwable throwable) {
                        Log.e("API Call Failure", throwable.getMessage());

                    }
                });
    }

    public void updatePost(Post post,Listener<Void> listener) {
            firebaseModel.updatePost(post,listener);
            refreshAllPosts();

    }
    public void deletePost(Post post, Listener<Void> listener) {
        firebaseModel.deletePost(post);
        executor.execute(()->{
            localDb.postDao().delete(post);
            mainHandler.post(()->{listener.onComplete(null);});
        });
        refreshAllPosts();
    }
    public void uploadImage(String id, Bitmap bitmap, Listener<String> listener) {
        firebaseModel.uploadPhoto(id,bitmap,listener);
    }

    public void updateUserAndHisPosts(User user, Listener<Void> listener) {
        firebaseModel.updateUserAndHisPosts(user,listener);
        refreshAllUsers();
    }
    public void register(User newUser,String password,Listener<Void> listener){
        listener.onComplete(null);
        firebaseModel.register(newUser,password);

        refreshAllUsers();


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
    LiveData<User> otherUser;
    public LiveData<User>  getOtherUser(String username) {
        otherUser=localDb.userDao().getUserByUsername(username);
        return otherUser;

    }

    public boolean isUserLoggedIn() {
        return firebaseModel.isLoggedIn();
    }

    public void logout() {
        firebaseModel.logOut();
        user=null;
        postList=null;
    }


}
