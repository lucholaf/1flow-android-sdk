package com.oneflow.tryskysdk.model.events;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.oneflow.tryskysdk.sdkdb.convertes.MapConverter;

import java.util.HashMap;


public class RecordEventsTabToAPI {

    @SerializedName("name")
    private String eventName;

    @SerializedName("parameters")
    private HashMap<String,String> dataMap;

    @SerializedName("time")
    private Long time;

    @SerializedName("value")
    private String value;



    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public HashMap<String,String> getDataMap() {
        return dataMap;
    }

    public void setDataMap(HashMap<String,String> dataMap) {
        this.dataMap = dataMap;
    }


}
