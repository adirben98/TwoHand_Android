package com.example.twohand_project.Model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PostDao {
    @Query("select * from Post")
    List<Post> getAll();
    @Query("select * from Post where id=:id")
    Post getPostById(String id);
    @Query("select * from Post where color=:color and kind=:kind")
    List<Post> getPostsByCategories(String color, String kind);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Post post);

    @Delete
    void delete(Post post);
}
