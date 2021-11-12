package com.oneflow.analytics.repositories;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.oneflow.analytics.model.events.RecordEventsTab;
import com.oneflow.analytics.sdkdb.SDKDB;
import com.oneflow.analytics.utils.Constants;
import com.oneflow.analytics.utils.Helper;
import com.oneflow.analytics.utils.MyResponseHandler;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import java.util.logging.LogRecord;

public class EventDBRepo {


    public static void insertEvents(Context context, String eventName, HashMap<String,String> data, int value, MyResponseHandler mrh, Constants.ApiHitType type){
        Helper.v("EventDBRepo.DeleteEvents","OneFlow reached at insertEvent method");

        new AsyncTask<String,Integer,Integer>(){

            @Override
            protected Integer doInBackground(String... strings) {
                SDKDB sdkdb = SDKDB.getInstance(context);
                RecordEventsTab ret = new RecordEventsTab();
                ret.setEventName(eventName);
                ret.setDataMap(data);
                ret.setValue(String.valueOf(value));
                ret.setTime(Calendar.getInstance().getTimeInMillis()/1000);
                ret.setSynced(0);
                ret.setCreatedOn(Calendar.getInstance().getTimeInMillis());
                sdkdb.eventDAO().insertAll(ret);
                return null;
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                mrh.onResponseReceived(type,1,0);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }
    public static void deleteEvents(Context context,Integer []ids,MyResponseHandler mrh, Constants.ApiHitType type){
        Helper.v("EventDBRepo.DeleteEvents","OneFlow reached at delete method");
        new AsyncTask<String,Integer,Integer>(){

            @Override
            protected Integer doInBackground(String... strings) {
                SDKDB sdkdb = SDKDB.getInstance(context);
                Integer deleted = sdkdb.eventDAO().deleteSyncedEvents(ids);
                return deleted;
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                mrh.onResponseReceived(type,integer,0);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }
    public static void fetchEvents(Context context, MyResponseHandler mrh, Constants.ApiHitType type){
        Helper.v("EventDBRepo.DeleteEvents","OneFlow reached at fetchEvents method");


        new AsyncTask<String,Integer,List<RecordEventsTab>>(){
            @Override
            protected List<RecordEventsTab> doInBackground(String... strings) {
                SDKDB sdkdb = SDKDB.getInstance(context);
                Helper.v("EventDBRepo","OneFlow fetching events from db 0");
                List<RecordEventsTab> retList = sdkdb.eventDAO().getAllUnsyncedEvents();
                return retList;
            }

            @Override
            protected void onPostExecute(List<RecordEventsTab> recordEventsTabs) {
                super.onPostExecute(recordEventsTabs);
                mrh.onResponseReceived(type,recordEventsTabs,0);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);



    }

    public static void fetchEventsBeforeSurvey(Context context, MyResponseHandler mrh, Constants.ApiHitType type){
        Helper.v("EventDBRepo.fetchEventsBeforeSurvey","OneFlow reached at fetchEventsBeforeSurvey method");
        //String[] beforeSurveyEvent = new String[1];
        new AsyncTask<String,Integer,String[]>(){

            @Override
            protected String[] doInBackground(String... strings) {
                SDKDB sdkdb = SDKDB.getInstance(context);
                Helper.v("EventDBRepo","OneFlow fetching events from db 0");
                String []beforeSurveyEvent = sdkdb.eventDAO().getEventBeforeSurvey(Calendar.getInstance().getTimeInMillis());
                Helper.v("EventDBRepo","OneFlow fetching events from db ["+ Arrays.asList(beforeSurveyEvent)+"]length["+beforeSurveyEvent.length+"]");
                return beforeSurveyEvent;
            }

            @Override
            protected void onPostExecute(String[] strings) {
                super.onPostExecute(strings);
                mrh.onResponseReceived(type,strings,0);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);




    }


}
