package com.example.twohand_project.Model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.twohand_project.MyApplication;

@Database(entities = {Post.class}, version = 2)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract PostDao postDao();
}
public class AppLocalDb {
    static public AppLocalDbRepository db =
            Room.databaseBuilder(MyApplication.getMyContext(),
                            AppLocalDbRepository.class,
                            "dbFileName.db")
                    .fallbackToDestructiveMigration()
                    .build();
}
