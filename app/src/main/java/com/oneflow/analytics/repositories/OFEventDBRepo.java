/*
 *  Copyright 2021 1Flow, Inc.
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.oneflow.analytics.repositories;

import android.content.Context;
import android.os.AsyncTask;

import com.oneflow.analytics.model.events.OFRecordEventsTab;
import com.oneflow.analytics.sdkdb.OFSDKDB;
import com.oneflow.analytics.utils.OFConstants;
import com.oneflow.analytics.utils.OFHelper;
import com.oneflow.analytics.utils.OFMyResponseHandler;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class OFEventDBRepo {


    public static void insertEvents(Context context, String eventName, HashMap<String,String> data, int value, OFMyResponseHandler mrh, OFConstants.ApiHitType type){
        OFHelper.v("EventDBRepo.DeleteEvents","OneFlow reached at insertEvent method");

        new AsyncTask<String,Integer,Integer>(){

            @Override
            protected Integer doInBackground(String... strings) {
                OFSDKDB sdkdb = OFSDKDB.getInstance(context);
                OFRecordEventsTab ret = new OFRecordEventsTab();
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
                mrh.onResponseReceived(type,1,0l,"");
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }
    public static void deleteEvents(Context context, Integer []ids, OFMyResponseHandler mrh, OFConstants.ApiHitType type){
        OFHelper.v("EventDBRepo.DeleteEvents","OneFlow reached at delete method");
        new AsyncTask<String,Integer,Integer>(){

            @Override
            protected Integer doInBackground(String... strings) {
                OFSDKDB sdkdb = OFSDKDB.getInstance(context);
                Integer deleted = sdkdb.eventDAO().deleteSyncedEvents(ids);
                return deleted;
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                mrh.onResponseReceived(type,integer,0l,"");
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }
    public static void fetchEvents(Context context, OFMyResponseHandler mrh, OFConstants.ApiHitType type){
        OFHelper.v("EventDBRepo.fetchEvents","OneFlow reached at fetchEvents method");


        new AsyncTask<String,Integer,List<OFRecordEventsTab>>(){
            @Override
            protected List<OFRecordEventsTab> doInBackground(String... strings) {
                OFSDKDB sdkdb = OFSDKDB.getInstance(context);
                OFHelper.v("EventDBRepo","OneFlow fetching events from db 0");
                List<OFRecordEventsTab> retList = sdkdb.eventDAO().getAllUnsyncedEvents();
                return retList;
            }

            @Override
            protected void onPostExecute(List<OFRecordEventsTab> OFRecordEventsTabs) {
                super.onPostExecute(OFRecordEventsTabs);
                mrh.onResponseReceived(type, OFRecordEventsTabs,0l,"");
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);



    }

    public static void fetchEventsBeforeSurvey(Context context, OFMyResponseHandler mrh, OFConstants.ApiHitType type){
        OFHelper.v("EventDBRepo.fetchEventsBeforeSurvey","OneFlow reached at fetchEventsBeforeSurvey method");
        //String[] beforeSurveyEvent = new String[1];
        new AsyncTask<String,Integer,String[]>(){

            @Override
            protected String[] doInBackground(String... strings) {
                OFSDKDB sdkdb = OFSDKDB.getInstance(context);
                OFHelper.v("EventDBRepo","OneFlow fetching events from db 0");
                String []beforeSurveyEvent = sdkdb.eventDAO().getEventBeforeSurvey(Calendar.getInstance().getTimeInMillis());
                OFHelper.v("EventDBRepo","OneFlow fetching events from db ["+ Arrays.asList(beforeSurveyEvent)+"]length["+beforeSurveyEvent.length+"]");
                return beforeSurveyEvent;
            }

            @Override
            protected void onPostExecute(String[] strings) {
                super.onPostExecute(strings);
                mrh.onResponseReceived(type,strings,0l,"");
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);




    }


}
