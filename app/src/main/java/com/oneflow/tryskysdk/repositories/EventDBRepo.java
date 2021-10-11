package com.oneflow.tryskysdk.repositories;

import android.content.Context;

import androidx.room.RoomDatabase;

import com.oneflow.tryskysdk.model.events.RecordEventsTab;
import com.oneflow.tryskysdk.model.events.RecordEventsTabToAPI;
import com.oneflow.tryskysdk.sdkdb.SDKDB;
import com.oneflow.tryskysdk.utils.Constants;
import com.oneflow.tryskysdk.utils.Helper;
import com.oneflow.tryskysdk.utils.MyResponseHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class EventDBRepo {


    public static void insertEvents(Context context, String eventName, HashMap<String,String> data, int value, MyResponseHandler mrh, Constants.ApiHitType type){
        Helper.v("EventDBRepo.DeleteEvents","OneFlow reached at insertEvent method");

        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                SDKDB sdkdb = SDKDB.getInstance(context);
                RecordEventsTab ret = new RecordEventsTab();
                ret.setEventName(eventName);
                ret.setDataMap(data);
                ret.setValue(String.valueOf(value));
                ret.setTime(Calendar.getInstance().getTimeInMillis()/1000);
                ret.setSynced(0);
                sdkdb.eventDAO().insertAll(ret);
                mrh.onResponseReceived(type,1,0);
            }};
        thread.start();



    }
    public static void deleteEvents(Context context,Integer []ids,MyResponseHandler mrh, Constants.ApiHitType type){
        Helper.v("EventDBRepo.DeleteEvents","OneFlow reached at delete method");
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                SDKDB sdkdb = SDKDB.getInstance(context);
                Integer deleted = sdkdb.eventDAO().deleteSyncedEvents(ids);
                mrh.onResponseReceived(type,deleted,0);
            }};
        thread.start();

    }
    public static void fetchEvents(Context context, MyResponseHandler mrh, Constants.ApiHitType type){
        Helper.v("EventDBRepo.DeleteEvents","OneFlow reached at fetchEvents method");
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                SDKDB sdkdb = SDKDB.getInstance(context);
                List<RecordEventsTab> retList = sdkdb.eventDAO().getAllUnsyncedEvents();

                mrh.onResponseReceived(type,retList,0);
            }
        };
        thread.start();

    }


}
