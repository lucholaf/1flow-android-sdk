package com.oneflow.analytics.sdkdb.adduser;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.oneflow.analytics.model.adduser.OFAddUserResultResponse;

@Dao
public interface OFUserDAO {

    @Insert
    void insertUser(OFAddUserResultResponse aurr);

    @Query("Select * from User")
    OFAddUserResultResponse getUser();
}
