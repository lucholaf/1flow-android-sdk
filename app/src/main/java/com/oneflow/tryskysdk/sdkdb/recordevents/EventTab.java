package com.oneflow.tryskysdk.sdkdb.recordevents;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class EventTab {
    @PrimaryKey
    public int _recId;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "time")
    public Long time;

    @ColumnInfo(name = "value")
    public Integer value;

    @ColumnInfo(name = "session_id")
    public String sessionId;

    @ColumnInfo(name = "synced")
    public Boolean synced;


}
