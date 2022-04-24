package com.nhomduan.quanlyungdungdathang.LocalDatabase;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nhomduan.quanlyungdungdathang.Model.User;

import java.util.List;

@Dao
public interface LocalUserDao {


    @Query("SELECT * FROM User")
    List<User> getAll();

    @Query("SELECT * FROM User WHERE username LIKE :id")
    User getUser(String id);

    @Insert(onConflict = REPLACE)
    void insert(User users);

    @Update
    int update(User user);

    @Delete
    int delete(User user);

}
