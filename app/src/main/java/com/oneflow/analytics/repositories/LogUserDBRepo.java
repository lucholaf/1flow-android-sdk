package com.oneflow.analytics.repositories;

import android.content.Context;
import android.os.AsyncTask;

import com.oneflow.analytics.model.events.RecordEventsTab;
import com.oneflow.analytics.model.survey.SurveyUserInput;
import com.oneflow.analytics.sdkdb.SDKDB;
import com.oneflow.analytics.utils.Constants;
import com.oneflow.analytics.utils.Helper;
import com.oneflow.analytics.utils.MyResponseHandler;

import java.util.Calendar;

public class LogUserDBRepo {


    public static void insertUserInputs(Context context, SurveyUserInput sui, MyResponseHandler mrh, Constants.ApiHitType type){
        Helper.v("LogDBRepo.insertUserInputs","OneFlow reached at insertUserInput method");


        new AsyncTask<String,Integer,Integer>(){

            @Override
            protected Integer doInBackground(String... strings) {
                SDKDB sdkdb = SDKDB.getInstance(context);
                sdkdb.logDAO().insertUserInput(sui);
                return null;
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }
    public static void deleteSentSurveyFromDB(Context context,Integer []ids,MyResponseHandler mrh, Constants.ApiHitType type){
        Helper.v("LogDBRepo.DeleteUserInput","OneFlow reached at delete method");

        new AsyncTask<String,Integer,Integer>(){

            @Override
            protected Integer doInBackground(String... strings) {
                SDKDB sdkdb = SDKDB.getInstance(context);
                Integer deleted = sdkdb.logDAO().deleteSurvey(ids);
                return deleted;
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                mrh.onResponseReceived(type,integer,0);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }
    /*public static void fetchSurveyInputList(Context context, MyResponseHandler mrh, Constants.ApiHitType type){
        Helper.v("LogDBRepo.fetchSurveyInputList","OneFlow reached at fetchSurveyEvents method");
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                SDKDB sdkdb = SDKDB.getInstance(context);
                Helper.v("fetchSurveyInputList","OneFlow fetching events list from db 0");
                ArrayList<SurveyUserInput> sui = sdkdb.logDAO().getOfflineUserInputList();
                Helper.v("fetchSurveyInputList","OneFlow fetching events list from db 1");
                mrh.onResponseReceived(type,sui,0);
            }
        };
        thread.start();

    }*/
    public static void fetchSurveyInput(Context context, MyResponseHandler mrh, Constants.ApiHitType type){
        Helper.v("LogDBRepo.fetchSurveyInputList","OneFlow reached at fetchSurveyEvents method");

        new AsyncTask<String,Integer,SurveyUserInput>(){

            @Override
            protected SurveyUserInput doInBackground(String... strings) {
                SDKDB sdkdb = SDKDB.getInstance(context);
                Helper.v("fetchSurveyInputList","OneFlow fetching events list from db 0");
                SurveyUserInput sui = sdkdb.logDAO().getOfflineUserInput();
                return sui;
            }

            @Override
            protected void onPostExecute(SurveyUserInput sui) {
                super.onPostExecute(sui);
                mrh.onResponseReceived(type,sui,0);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }


}
