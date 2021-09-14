package com.oneflow.tryskysdk.sdkdb;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.oneflow.tryskysdk.sdkdb.convertes.DataConverterDevice;
import com.oneflow.tryskysdk.sdkdb.convertes.DataConverterLocation;
import com.oneflow.tryskysdk.model.adduser.AddUserResultResponse;
import com.oneflow.tryskysdk.sdkdb.adduser.UserDAO;
import com.oneflow.tryskysdk.sdkdb.recordevents.EventDAO;
import com.oneflow.tryskysdk.sdkdb.recordevents.EventTab;

@Database(entities = {EventTab.class, AddUserResultResponse.class},version = 1)
@TypeConverters({DataConverterLocation.class, DataConverterDevice.class})
public abstract class SDKDB extends RoomDatabase {

    public abstract EventDAO eventDAO();
    public abstract UserDAO userDAO();
}
