package com.oneflow.analytics.repositories;

import android.content.Context;

import com.oneflow.analytics.model.events.RecordEventsTab;
import com.oneflow.analytics.sdkdb.SDKDB;
import com.oneflow.analytics.utils.Constants;
import com.oneflow.analytics.utils.Helper;
import com.oneflow.analytics.utils.MyResponseHandler;

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
                Helper.v("EventDBRepo","OneFlow fetching events from db 0");
                List<RecordEventsTab> retList = sdkdb.eventDAO().getAllUnsyncedEvents();
                Helper.v("EventDBRepo","OneFlow fetching events from db 1");
                mrh.onResponseReceived(type,retList,0);
            }
        };
        thread.start();

    }


}
