package com.oneflow.analytics.repositories;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.oneflow.analytics.model.ApiInterface;
import com.oneflow.analytics.model.GenericResponse;
import com.oneflow.analytics.model.RetroBaseService;

import com.oneflow.analytics.model.survey.GetSurveyListResponse;
import com.oneflow.analytics.model.survey.SurveyUserInput;
import com.oneflow.analytics.sdkdb.OneFlowSHP;
import com.oneflow.analytics.utils.Constants;
import com.oneflow.analytics.utils.Helper;
import com.oneflow.analytics.utils.MyResponseHandler;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Survey {

    static String tag = "Survey";

    public static void getSurvey(Context context, MyResponseHandler mrh, Constants.ApiHitType type) {
        Helper.v(tag, "OneFlow survey reached getSurvey called");
        ApiInterface connectAPI = RetroBaseService.getClient().create(ApiInterface.class);
        try {
            Call<GenericResponse<ArrayList<GetSurveyListResponse>>> responseCall = null;

            responseCall = connectAPI.getSurvey(new OneFlowSHP(context).getStringValue(Constants.APPIDSHP),"android");

            responseCall.enqueue(new Callback<GenericResponse<ArrayList<GetSurveyListResponse>>>() {
                @Override
                public void onResponse(Call<GenericResponse<ArrayList<GetSurveyListResponse>>> call, Response<GenericResponse<ArrayList<GetSurveyListResponse>>> response) {


                    /*Helper.v(tag,"OneFlow survey list response["+response.isSuccessful()+"]");
                    Helper.v(tag,"OneFlow survey list response["+response.body().getSuccess()+"]");*/
                    if (response.isSuccessful()) {

                        //Helper.v(tag,"OneFlow counter reached at["+counter+"]");
                        new OneFlowSHP(context).setSurveyList(response.body().getResult());

                        Intent intent = new Intent("survey_list_fetched");
                        context.sendBroadcast(intent);

                        mrh.onResponseReceived(type,null,0);

                    }else{
                        Helper.v(tag,"OneFlow survey list not fetched isSuccessfull false");
                        //TempResponseModel trm = new Gson().fromJson(response.body())
                    }

                }

                @Override
                public void onFailure(Call<GenericResponse<ArrayList<GetSurveyListResponse>>> call, Throwable t) {

                    Helper.e(tag, "OneFlow error[" + t.toString() + "]");
                    Helper.e(tag, "OneFlow errorMsg[" + t.getMessage() + "]");

                }
            });
        } catch (Exception ex) {

        }
    }
    public static void submitUserResponse(Context context, SurveyUserInput sur) {
        ApiInterface connectAPI = RetroBaseService.getClient().create(ApiInterface.class);
        try {
            Call<GenericResponse<String>> responseCall = null;

            responseCall = connectAPI.submitSurveyUserResponse(new OneFlowSHP(context).getStringValue(Constants.APPIDSHP),sur);

            responseCall.enqueue(new Callback<GenericResponse<String>>() {
                @Override
                public void onResponse(Call<GenericResponse<String>> call, Response<GenericResponse<String>> response) {


                    Helper.v(tag, "OneFlow reached success[" + response.isSuccessful() + "]");
                    Helper.v(tag, "OneFlow reached success raw[" + response.raw() + "]");
                    Helper.v(tag, "OneFlow reached success errorBody[" + response.errorBody() + "]");
                    Helper.v(tag, "OneFlow reached success message[" + response.message() + "]");


                    if (response.isSuccessful()) {
                        Helper.v(tag, "OneFlow response[" + response.body().getSuccess() + "]");
                        Helper.v(tag, "OneFlow response message[" + response.body().getMessage() + "]");
                        new OneFlowSHP(context).storeValue(sur.getSurvey_id(), Calendar.getInstance().getTimeInMillis());
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
                        Helper.v(tag, "OneFlow response 0[" + response.body() + "]");
                        /*Helper.v(tag, "OneFlow response 1[" + response.body().getMessage() + "]");
                        Helper.v(tag, "OneFlow response 2[" + response.body().getSuccess() + "]");*/
                    }

                }

                @Override
                public void onFailure(Call<GenericResponse<String>> call, Throwable t) {

                    Helper.e(tag, "OneFlow error[" + t.toString() + "]");
                    Helper.e(tag, "OneFlow errorMsg[" + t.getMessage() + "]");

                }
            });
        } catch (Exception ex) {

        }
    }
    public static void submitUserResponseOffline(Context context, SurveyUserInput sur, MyResponseHandler mrh, Constants.ApiHitType type) {
        ApiInterface connectAPI = RetroBaseService.getClient().create(ApiInterface.class);
        try {
            Call<GenericResponse<String>> responseCall = null;

            responseCall = connectAPI.submitSurveyUserResponse(new OneFlowSHP(context).getStringValue(Constants.APPIDSHP),sur);

            responseCall.enqueue(new Callback<GenericResponse<String>>() {
                @Override
                public void onResponse(Call<GenericResponse<String>> call, Response<GenericResponse<String>> response) {


                    Helper.v(tag, "OneFlow reached success[" + response.isSuccessful() + "]");
                    Helper.v(tag, "OneFlow reached success raw[" + response.raw() + "]");
                    Helper.v(tag, "OneFlow reached success errorBody[" + response.errorBody() + "]");
                    Helper.v(tag, "OneFlow reached success message[" + response.message() + "]");


                    if (response.isSuccessful()) {
                        Helper.v(tag, "OneFlow response[" + response.body().getSuccess() + "]");
                        Helper.v(tag, "OneFlow response message[" + response.body().getMessage() + "]");
                        mrh.onResponseReceived(type,sur,0);
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
                        Helper.v(tag, "OneFlow response 0[" + response.body() + "]");
                        /*Helper.v(tag, "OneFlow response 1[" + response.body().getMessage() + "]");
                        Helper.v(tag, "OneFlow response 2[" + response.body().getSuccess() + "]");*/
                    }

                }

                @Override
                public void onFailure(Call<GenericResponse<String>> call, Throwable t) {

                    Helper.e(tag, "OneFlow error[" + t.toString() + "]");
                    Helper.e(tag, "OneFlow errorMsg[" + t.getMessage() + "]");

                }
            });
        } catch (Exception ex) {

        }
    }

}
