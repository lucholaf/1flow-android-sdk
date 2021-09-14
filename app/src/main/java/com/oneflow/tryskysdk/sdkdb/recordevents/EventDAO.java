package com.oneflow.tryskysdk.sdkdb.recordevents;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface EventDAO {
    @Insert
    void insertAll(EventTab et);
}
