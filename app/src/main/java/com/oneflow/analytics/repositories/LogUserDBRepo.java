package com.oneflow.analytics.repositories;

import android.content.Context;

import com.oneflow.analytics.model.survey.SurveyUserInput;
import com.oneflow.analytics.sdkdb.SDKDB;
import com.oneflow.analytics.utils.Constants;
import com.oneflow.analytics.utils.Helper;
import com.oneflow.analytics.utils.MyResponseHandler;

public class LogUserDBRepo {


    public static void insertUserInputs(Context context, SurveyUserInput sui, MyResponseHandler mrh, Constants.ApiHitType type){
        Helper.v("LogDBRepo.insertUserInputs","OneFlow reached at insertUserInput method");

        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                SDKDB sdkdb = SDKDB.getInstance(context);

                sdkdb.logDAO().insertUserInput(sui);

                //return not required
                //mrh.onResponseReceived(type,1,0);
            }};
        thread.start();

    }
    public static void deleteSentSurveyFromDB(Context context,Integer []ids,MyResponseHandler mrh, Constants.ApiHitType type){
        Helper.v("LogDBRepo.DeleteUserInput","OneFlow reached at delete method");
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                SDKDB sdkdb = SDKDB.getInstance(context);
                Integer deleted = sdkdb.logDAO().deleteSurvey(ids);
                mrh.onResponseReceived(type,deleted,0);
            }};
        thread.start();

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
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                SDKDB sdkdb = SDKDB.getInstance(context);
                Helper.v("fetchSurveyInputList","OneFlow fetching events list from db 0");
                SurveyUserInput sui = sdkdb.logDAO().getOfflineUserInput();
                Helper.v("fetchSurveyInputList","OneFlow fetching events list from db 1");
                mrh.onResponseReceived(type,sui,0);
            }
        };
        thread.start();

    }


}
