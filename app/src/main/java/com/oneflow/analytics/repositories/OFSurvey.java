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
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oneflow.analytics.model.OFApiInterface;
import com.oneflow.analytics.model.OFGenericResponse;
import com.oneflow.analytics.model.OFRetroBaseService;
import com.oneflow.analytics.model.survey.OFGetSurveyListResponse;
import com.oneflow.analytics.model.survey.OFSurveyUserInput;
import com.oneflow.analytics.sdkdb.OFOneFlowSHP;
import com.oneflow.analytics.utils.OFConstants;
import com.oneflow.analytics.utils.OFHelper;
import com.oneflow.analytics.utils.OFMyResponseHandler;
import com.oneflow.analytics.utils.OFMyResponseHandlerOneFlow;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OFSurvey {

    static String tag = "Survey";

    public static void getSurvey(String headerKey, OFMyResponseHandlerOneFlow mrh, OFConstants.ApiHitType type, String userId, String sessionId, String versionName) {

        String language = Locale.getDefault().toString();
        if (OFHelper.validateString(language).equalsIgnoreCase("NA")) {
            language = "en_US";
        }
        OFHelper.v(tag, "OneFlow Language survey reached getSurvey called language[" + language + "]");
        OFApiInterface connectAPI = OFRetroBaseService.getClient().create(OFApiInterface.class);

        try {
            Call<OFGenericResponse<ArrayList<OFGetSurveyListResponse>>> responseCall = null;
            //String url = "https://us-west-2.aws.webhooks.mongodb-realm.com/api/client/v2.0/app/1flow-wslxs/service/survey/incoming_webhook/get-surveys";

            responseCall = connectAPI.getSurvey(headerKey, "android", userId, sessionId, language, versionName);//,OFConstants.MODE);

            responseCall.enqueue(new Callback<OFGenericResponse<ArrayList<OFGetSurveyListResponse>>>() {
                @Override
                public void onResponse(Call<OFGenericResponse<ArrayList<OFGetSurveyListResponse>>> call, Response<OFGenericResponse<ArrayList<OFGetSurveyListResponse>>> response) {


                    OFHelper.v(tag, "OneFlow recordEvents survey list response[" + response.isSuccessful() + "]at [" + OFHelper.formatedDate(System.currentTimeMillis(), "dd-MM-yyyy hh:mm:ss.SSS") + "]");

                    if (response.isSuccessful()) {
                        try {

                            GsonBuilder builder = new GsonBuilder();
                            builder.serializeNulls();
                            Gson gson = builder.setPrettyPrinting().create();

                            String throttling = gson.toJson(response.body().getThrottlingConfig());
                            mrh.onResponseReceived(type, response.body().getResult(), 0l, throttling,null,null);
                        } catch (Exception ex) {
                            mrh.onResponseReceived(type, "", 0l, "",null,null);
                        }

                    } else {
                        OFHelper.v(tag, "OneFlow survey list not fetched isSuccessfull false");
                        mrh.onResponseReceived(type, null, 0l, response.message(),null,null);
                        //TempResponseModel trm = new Gson().fromJson(response.body())
                    }

                }

                @Override
                public void onFailure(Call<OFGenericResponse<ArrayList<OFGetSurveyListResponse>>> call, Throwable t) {

                    OFHelper.e(tag, "OneFlow error[" + t.toString() + "]");
                    OFHelper.e(tag, "OneFlow errorMsg[" + t.getMessage() + "]");

                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void submitUserResponse(String headerKey, OFSurveyUserInput sur, OFConstants.ApiHitType type, OFMyResponseHandlerOneFlow handler) {
        OFApiInterface connectAPI = OFRetroBaseService.getClient().create(OFApiInterface.class);
        try {
            Call<OFGenericResponse> responseCall = null;
            String url = "https://us-west-2.aws.webhooks.mongodb-realm.com/api/client/v2.0/app/1flow-wslxs/service/survey/incoming_webhook/add_survey_response";
            responseCall = connectAPI.submitSurveyUserResponse(headerKey, sur);//, url);

            responseCall.enqueue(new Callback<OFGenericResponse>() {
                @Override
                public void onResponse(Call<OFGenericResponse> call, Response<OFGenericResponse> response) {


                    //OFHelper.v(tag, "OneFlow reached success[" + response.isSuccessful() + "]");
                    //OFHelper.v(tag, "OneFlow reached success raw[" + response.raw() + "]");

                    //OFHelper.makeText(context.getApplicationContext(),"Survey submit status["+response.isSuccessful()+"]",1);

                    if (response.isSuccessful()) {
                        //OFHelper.v(tag, "OneFlow response[" + response.body().getSuccess() + "]");
                        //OFHelper.v(tag, "OneFlow response message[" + response.body().getMessage() + "]");

                        handler.onResponseReceived(type, sur, 0l, "",null,null);
                        /*AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                Helper.v(tag,"OneFlow inserting data ["+sur.getSurvey_id()+"]");
                                SubmittedSurveysTab sst = new SubmittedSurveysTab();
                                sst.setSurveyId(sur.getSurvey_id());
                                SDKDB.getInstance(context).submittedSurveyDAO().insertSubmittedSurvey(sst);
                                Helper.v(tag,"OneFlow inserted data");
                            }
                        });*/

                    } else {
                        //mrh.onResponseReceived(response.body(), type);
                        // OFHelper.v(tag, "OneFlow response 0[" + response.body() + "]");
                        /*Helper.v(tag, "OneFlow response 1[" + response.body().getMessage() + "]");
                        Helper.v(tag, "OneFlow response 2[" + response.body().getSuccess() + "]");*/
                    }

                }

                @Override
                public void onFailure(Call<OFGenericResponse> call, Throwable t) {

                    OFHelper.e(tag, "OneFlow error[" + t.toString() + "]");
                    OFHelper.e(tag, "OneFlow errorMsg[" + t.getMessage() + "]");

                }
            });
        } catch (Exception ex) {

        }
    }

    public static void submitUserResponseOffline(Context context, OFSurveyUserInput sur, OFMyResponseHandlerOneFlow mrh, OFConstants.ApiHitType type) {
        OFApiInterface connectAPI = OFRetroBaseService.getClient().create(OFApiInterface.class);
        try {
            Call<OFGenericResponse> responseCall = null;
            String url = "https://us-west-2.aws.webhooks.mongodb-realm.com/api/client/v2.0/app/1flow-wslxs/service/survey/incoming_webhook/add_survey_response";
            responseCall = connectAPI.submitSurveyUserResponse(new OFOneFlowSHP(context).getStringValue(OFConstants.APPIDSHP), sur);//, url);

            responseCall.enqueue(new Callback<OFGenericResponse>() {
                @Override
                public void onResponse(Call<OFGenericResponse> call, Response<OFGenericResponse> response) {


                    OFHelper.v(tag, "OneFlow reached success[" + response.isSuccessful() + "]");
                    OFHelper.v(tag, "OneFlow reached success raw[" + response.raw() + "]");
                    OFHelper.v(tag, "OneFlow reached success errorBody[" + response.errorBody() + "]");
                    OFHelper.v(tag, "OneFlow reached success message[" + response.message() + "]");


                    if (response.isSuccessful()) {
                        //  OFHelper.v(tag, "OneFlow response[" + response.body().getSuccess() + "]");
                        // OFHelper.v(tag, "OneFlow response message[" + response.body().getMessage() + "]");
                        mrh.onResponseReceived(type, sur, 0l, "",null,null);
                        /*AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                Helper.v(tag,"OneFlow inserting data ["+sur.getSurvey_id()+"]");
                                SubmittedSurveysTab sst = new SubmittedSurveysTab();
                                sst.setSurveyId(sur.getSurvey_id());
                                SDKDB.getInstance(context).submittedSurveyDAO().insertSubmittedSurvey(sst);
                                Helper.v(tag,"OneFlow inserted data");
                            }
                        });*/

                    } else {
                        //mrh.onResponseReceived(response.body(), type);
                        OFHelper.v(tag, "OneFlow response 0[" + response.body() + "]");
                        /*Helper.v(tag, "OneFlow response 1[" + response.body().getMessage() + "]");
                        Helper.v(tag, "OneFlow response 2[" + response.body().getSuccess() + "]");*/
                    }

                }

                @Override
                public void onFailure(Call<OFGenericResponse> call, Throwable t) {

                    OFHelper.e(tag, "OneFlow error[" + t.toString() + "]");
                    OFHelper.e(tag, "OneFlow errorMsg[" + t.getMessage() + "]");

                }
            });
        } catch (Exception ex) {

        }
    }

}
