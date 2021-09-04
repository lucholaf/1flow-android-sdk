package com.oneflow.tryskysdk.sdkdb;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.oneflow.tryskysdk.sdkdb.recordevents.EventDAO;
import com.oneflow.tryskysdk.sdkdb.recordevents.EventTab;

@Database(entities = {EventTab.class},version = 1)
public abstract class SDKDB extends RoomDatabase {

    public abstract EventDAO eventDAO();
}
