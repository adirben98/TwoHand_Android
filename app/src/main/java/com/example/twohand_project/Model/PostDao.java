package com.example.twohand_project.Model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.List;

@Dao
public interface PostDao {
    @Query("select * from Post where owner != :username")
    LiveData<List<Post>> getAllWithoutUser(String username);

    @Query("select * from Post where owner=:username")
    LiveData<List<Post>> getUserPosts(String username);
    @Query("select * from Post where id=:id")
    LiveData<Post> getPostById(String id);
    @Query("select * from Post where id in (:postIds)")
    List<Post> getFavorites(List<String> postIds);
    @RawQuery
    List<Post> getPostsByQuery(SimpleSQLiteQuery query);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Post post);

    @Delete
    void delete(Post post);
}
