package com.circo.oneflow.sdkdb.adduser;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.circo.oneflow.model.adduser.AddUserResultResponse;

@Dao
public interface UserDAO {

    @Insert
    void insertUser(AddUserResultResponse aurr);

    @Query("Select * from User")
    AddUserResultResponse getUser();
}
