package com.oneflow.analytics.model.events;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface OFRecordEventDAO {

    @Insert
    void insertAll(OFRecordEventsTab ret);

    @Query("Select * from RecordEvents")
    OFRecordEventsTab getAllRecordedEvents();

    @Query("Select name from RecordEvents where created_on<:surveyTime")
    String[] getEventBeforeSurvey(Long surveyTime);

    @Query("Select * from RecordEvents where synced = 0")
    List<OFRecordEventsTab> getAllUnsyncedEvents();

    @Query("Delete from RecordEvents where _id in (:idList)")
    Integer deleteSyncedEvents(Integer[] idList);

}
