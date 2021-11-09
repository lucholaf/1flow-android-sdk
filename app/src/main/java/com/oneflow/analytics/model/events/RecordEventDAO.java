package com.oneflow.analytics.model.events;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecordEventDAO {

    @Insert
    void insertAll(RecordEventsTab ret);

    @Query("Select * from recordEvents")
    RecordEventsTab getAllRecordedEvents();

    @Query("Select name from recordEvents where created_on<:surveyTime")
    String[] getEventBeforeSurvey(Long surveyTime);

    @Query("Select * from recordEvents where synced = 0")
    List<RecordEventsTab> getAllUnsyncedEvents();

    @Query("Delete from recordEvents where _id in (:idList)")
    Integer deleteSyncedEvents(Integer[] idList);

}
