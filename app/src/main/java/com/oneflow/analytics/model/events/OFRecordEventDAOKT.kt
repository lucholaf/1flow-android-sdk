package com.oneflow.analytics.model.events

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OFRecordEventDAOKT {

    @Insert
    fun insertAll(ret: OFRecordEventsTabKT?): Long

    @Query("Select * from RecordEvents")
    fun getAllRecordedEvents(): OFRecordEventsTabKT?

    @Query("Select name from RecordEvents where created_on<:surveyTime")
    fun getEventBeforeSurvey(surveyTime: Long?): Array<String?>?

    /* @Query("Select * from RecordEvents")// where name like :eventName
    List<OFRecordEventsTabKT> findIfEventAlreadyLogged();//String eventName);*/

    /* @Query("Select * from RecordEvents")// where name like :eventName
    List<OFRecordEventsTabKT> findIfEventAlreadyLogged();//String eventName);*/
    @Query("Select name from RecordEvents where created_on>:surveyTime order by created_on")
    fun getEventBeforeSurvey3Sec(surveyTime: Long?): Array<String?>?

    @Query("Select * from RecordEvents where synced = 0")
    fun getAllUnsyncedEvents(): List<OFRecordEventsTabKT?>?

    @Query("Delete from RecordEvents where _id in (:idList)")
    fun deleteSyncedEvents(idList: Array<Int?>?): Int?

}