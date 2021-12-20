package com.oneflow.analytics.repositories;

import android.content.Context;
import android.os.AsyncTask;

import com.oneflow.analytics.model.survey.OFGetSurveyListResponse;
import com.oneflow.analytics.model.survey.OFSurveyUserInput;
import com.oneflow.analytics.sdkdb.OFSDKDB;
import com.oneflow.analytics.utils.OFConstants;
import com.oneflow.analytics.utils.OFHelper;
import com.oneflow.analytics.utils.OFMyResponseHandler;

public class OFLogUserDBRepo {


    public static void insertUserInputs(Context context, OFSurveyUserInput sui, OFMyResponseHandler mrh, OFConstants.ApiHitType type) {
        OFHelper.v("LogDBRepo.insertUserInputs", "OneFlow reached at insertUserInput method ["+sui.getUser_id()+"]");


        new AsyncTask<String, Integer, Integer>() {

            @Override
            protected Integer doInBackground(String... strings) {
                OFSDKDB sdkdb = OFSDKDB.getInstance(context);
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

    public static void deleteSentSurveyFromDB(Context context, Integer[] ids, OFMyResponseHandler mrh, OFConstants.ApiHitType type) {
        OFHelper.v("LogDBRepo.DeleteUserInput", "OneFlow reached at delete method");

        new AsyncTask<String, Integer, Integer>() {

            @Override
            protected Integer doInBackground(String... strings) {
                OFSDKDB sdkdb = OFSDKDB.getInstance(context);
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
    public static void fetchSurveyInput(Context context, OFMyResponseHandler mrh, OFConstants.ApiHitType type) {
        OFHelper.v("LogDBRepo.fetchSurveyInputList", "OneFlow reached at fetchSurveyEvents method");

        new AsyncTask<String, Integer, OFSurveyUserInput>() {

            @Override
            protected OFSurveyUserInput doInBackground(String... strings) {
                OFSDKDB sdkdb = OFSDKDB.getInstance(context);
                OFHelper.v("fetchSurveyInputList", "OneFlow fetching events list from db 0");
                OFSurveyUserInput sui = sdkdb.logDAO().getOfflineUserInput();
                return sui;
            }

            @Override
            protected void onPostExecute(OFSurveyUserInput sui) {
                super.onPostExecute(sui);
                mrh.onResponseReceived(type, sui, 0l);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    // update record once data is sent to server
    public static void updateSurveyInput(Context context, OFMyResponseHandler mrh, OFConstants.ApiHitType type, Boolean syncNew, Integer id) {
        OFHelper.v("LogDBRepo.updateSurveyInput", "OneFlow reached at updateSurveyInput method");

        new AsyncTask<String, Integer, Integer>() {

            @Override
            protected Integer doInBackground(String... strings) {
                OFSDKDB sdkdb = OFSDKDB.getInstance(context);
                OFHelper.v("LogDBRepo.updateSurveyInput", "OneFlow fetching events list from db 0");
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
        OFHelper.v("LogDBRepo.updateSurveyUserId", "OneFlow updating empty user id");

        new AsyncTask<String, Integer, Integer>() {

            @Override
            protected Integer doInBackground(String... strings) {
                OFSDKDB sdkdb = OFSDKDB.getInstance(context);

                Integer inserted = sdkdb.logDAO().updateUserID(userId);
                OFHelper.v("LogDBRepo.updateSurveyInput", "OneFlow updating empty user id 0["+inserted+"]");
                return inserted;
            }

            @Override
            protected void onPostExecute(Integer sui) {
                super.onPostExecute(sui);
               // mrh.onResponseReceived(type, sui, 0l);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    public static void findSurveyForID(Context context, OFMyResponseHandler mrh, OFConstants.ApiHitType type, final OFGetSurveyListResponse gslr, String id, String userId) {
        OFHelper.v("LogDBRepo.findSurveyForID", "OneFlow reached at updateSurveyInput method");

        new AsyncTask<String, Integer, Long>() {

            @Override
            protected Long doInBackground(String... strings) {
                OFSDKDB sdkdb = OFSDKDB.getInstance(context);
                OFHelper.v("LogDBRepo.findSurveyForID", "OneFlow finding survey for id from db 0");

                OFSurveyUserInput surveyUserInput = sdkdb.logDAO().getSurveyForID(id,userId);
                if(surveyUserInput!=null){
                    return surveyUserInput.getCreatedOn();
                }else{
                    return 0l;
                }

            }

            @Override
            protected void onPostExecute(Long sui) {
                super.onPostExecute(sui);
                OFHelper.v("LogDBRepo.findSurveyForID", "OneFlow returning created_On[" + sui + "]");
                if(sui>0) {
                    OFHelper.v("LogDBRepo.findSurveyForID", "OneFlow returning created_On readable[" + OFHelper.formatedDate(sui, "dd-MM-yy hh:mm:ss") + "]");
                }
                mrh.onResponseReceived(type, gslr, sui);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }


}
