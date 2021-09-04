package com.oneflow.tryskysdk.sdkdb.recordevents;

import androidx.room.Insert;

public interface EventDAO {
    @Insert
    void insertAll(EventTab et);
}
