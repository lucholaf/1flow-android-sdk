package com.oneflow.analytics.repositories;

import android.content.Context;
import android.os.AsyncTask;

import com.oneflow.analytics.model.events.RecordEventsTab;
import com.oneflow.analytics.model.survey.GetSurveyListResponse;
import com.oneflow.analytics.model.survey.SurveyUserInput;
import com.oneflow.analytics.sdkdb.SDKDB;
import com.oneflow.analytics.utils.Constants;
import com.oneflow.analytics.utils.Helper;
import com.oneflow.analytics.utils.MyResponseHandler;

import java.util.Calendar;

public class LogUserDBRepo {


    public static void insertUserInputs(Context context, SurveyUserInput sui, MyResponseHandler mrh, Constants.ApiHitType type) {
        Helper.v("LogDBRepo.insertUserInputs", "OneFlow reached at insertUserInput method ["+sui.getUser_id()+"]");


        new AsyncTask<String, Integer, Integer>() {

            @Override
            protected Integer doInBackground(String... strings) {
                SDKDB sdkdb = SDKDB.getInstance(context);
                sdkdb.logDAO().insertUserInput(sui);
                return null;
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                mrh.onResponseReceived(type, sui, 0l);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    public static void deleteSentSurveyFromDB(Context context, Integer[] ids, MyResponseHandler mrh, Constants.ApiHitType type) {
        Helper.v("LogDBRepo.DeleteUserInput", "OneFlow reached at delete method");

        new AsyncTask<String, Integer, Integer>() {

            @Override
            protected Integer doInBackground(String... strings) {
                SDKDB sdkdb = SDKDB.getInstance(context);
                Integer deleted = sdkdb.logDAO().deleteSurvey(ids);
                return deleted;
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                mrh.onResponseReceived(type, integer, 0l);
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
                mrh.onResponseReceived(type,sui,0l);
            }
        };
        thread.start();

    }*/
    public static void fetchSurveyInput(Context context, MyResponseHandler mrh, Constants.ApiHitType type) {
        Helper.v("LogDBRepo.fetchSurveyInputList", "OneFlow reached at fetchSurveyEvents method");

        new AsyncTask<String, Integer, SurveyUserInput>() {

            @Override
            protected SurveyUserInput doInBackground(String... strings) {
                SDKDB sdkdb = SDKDB.getInstance(context);
                Helper.v("fetchSurveyInputList", "OneFlow fetching events list from db 0");
                SurveyUserInput sui = sdkdb.logDAO().getOfflineUserInput();
                return sui;
            }

            @Override
            protected void onPostExecute(SurveyUserInput sui) {
                super.onPostExecute(sui);
                mrh.onResponseReceived(type, sui, 0l);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    // update record once data is sent to server
    public static void updateSurveyInput(Context context, MyResponseHandler mrh, Constants.ApiHitType type, Boolean syncNew, Integer id) {
        Helper.v("LogDBRepo.updateSurveyInput", "OneFlow reached at updateSurveyInput method");

        new AsyncTask<String, Integer, Integer>() {

            @Override
            protected Integer doInBackground(String... strings) {
                SDKDB sdkdb = SDKDB.getInstance(context);
                Helper.v("LogDBRepo.updateSurveyInput", "OneFlow fetching events list from db 0");
                Integer inserted = sdkdb.logDAO().updateUserInput(syncNew, id);
                return inserted;
            }

            @Override
            protected void onPostExecute(Integer sui) {
                super.onPostExecute(sui);
                //mrh.onResponseReceived(type, sui, 0l);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    // update user id for surveys before log
    public static void updateSurveyUserId(Context context,String userId) {
        Helper.v("LogDBRepo.updateSurveyUserId", "OneFlow updating empty user id");

        new AsyncTask<String, Integer, Integer>() {

            @Override
            protected Integer doInBackground(String... strings) {
                SDKDB sdkdb = SDKDB.getInstance(context);

                Integer inserted = sdkdb.logDAO().updateUserID(userId);
                Helper.v("LogDBRepo.updateSurveyInput", "OneFlow updating empty user id 0["+inserted+"]");
                return inserted;
            }

            @Override
            protected void onPostExecute(Integer sui) {
                super.onPostExecute(sui);
               // mrh.onResponseReceived(type, sui, 0l);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    public static void findSurveyForID(Context context, MyResponseHandler mrh, Constants.ApiHitType type,final GetSurveyListResponse gslr, String id, String userId) {
        Helper.v("LogDBRepo.findSurveyForID", "OneFlow reached at updateSurveyInput method");

        new AsyncTask<String, Integer, Long>() {

            @Override
            protected Long doInBackground(String... strings) {
                SDKDB sdkdb = SDKDB.getInstance(context);
                Helper.v("LogDBRepo.findSurveyForID", "OneFlow finding survey for id from db 0");

                SurveyUserInput surveyUserInput = sdkdb.logDAO().getSurveyForID(id,userId);
                if(surveyUserInput!=null){
                    return surveyUserInput.getCreatedOn();
                }else{
                    return 0l;
                }

            }

            @Override
            protected void onPostExecute(Long sui) {
                super.onPostExecute(sui);
                Helper.v("LogDBRepo.findSurveyForID", "OneFlow returning created_On[" + sui + "]");
                if(sui>0) {
                    Helper.v("LogDBRepo.findSurveyForID", "OneFlow returning created_On readable[" + Helper.formatedDate(sui, "dd-MM-yy hh:mm:ss") + "]");
                }
                mrh.onResponseReceived(type, gslr, sui);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }


}
