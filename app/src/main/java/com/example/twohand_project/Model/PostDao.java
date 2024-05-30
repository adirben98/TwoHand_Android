package com.example.twohand_project.Model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PostDao {
    @Query("select * from Post")
    LiveData<List<Post>> getAll();
//    @Query("select * from Post")
//    List<Post> get();
    @Query("select * from Post where owner=:username")
    List<Post> getUserPosts(String username);
    @Query("select * from Post where id=:id")
    Post getPostById(String id);
    @Query("select * from Post where id in (:postIds)")
    List<Post> getFavorites(List<String> postIds);
    @Query("SELECT * FROM Post WHERE kind = :kind and color=:color")
    List<Post> getPostsByCategories(String kind,String color);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Post post);

    @Delete
    void delete(Post post);
}
