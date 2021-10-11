package com.oneflow.tryskysdk.repositories;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.oneflow.tryskysdk.SurveyActivity;
import com.oneflow.tryskysdk.SurveyList;
import com.oneflow.tryskysdk.model.ApiInterface;
import com.oneflow.tryskysdk.model.GenericResponse;
import com.oneflow.tryskysdk.model.RetroBaseService;
import com.oneflow.tryskysdk.model.createsession.CreateSessionResponse;
import com.oneflow.tryskysdk.model.location.LocationResponse;
import com.oneflow.tryskysdk.model.survey.GetSurveyListResponse;
import com.oneflow.tryskysdk.model.survey.SurveyUserResponse;
import com.oneflow.tryskysdk.sdkdb.OneFlowSHP;
import com.oneflow.tryskysdk.sdkdb.SDKDB;
import com.oneflow.tryskysdk.sdkdb.survey.SubmittedSurveysTab;
import com.oneflow.tryskysdk.utils.Constants;
import com.oneflow.tryskysdk.utils.Helper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Survey {

    static String tag = "Survey";

    public static void getSurvey(Context context) {
        ApiInterface connectAPI = RetroBaseService.getClient().create(ApiInterface.class);
        try {
            Call<GenericResponse<ArrayList<GetSurveyListResponse>>> responseCall = null;

            responseCall = connectAPI.getSurvey();

            responseCall.enqueue(new Callback<GenericResponse<ArrayList<GetSurveyListResponse>>>() {
                @Override
                public void onResponse(Call<GenericResponse<ArrayList<GetSurveyListResponse>>> call, Response<GenericResponse<ArrayList<GetSurveyListResponse>>> response) {


                    Helper.v(tag, "OneFlow reached success[" + response.isSuccessful() + "]");
                    Helper.v(tag, "OneFlow reached success raw[" + response.raw() + "]");
                    Helper.v(tag, "OneFlow reached success errorBody[" + response.errorBody() + "]");
                    Helper.v(tag, "OneFlow reached success message[" + response.message() + "]");


                    if (response.isSuccessful()) {
                        Helper.v(tag, "OneFlow response[" + response.body().toString() + "]");
                        Helper.v(tag, "OneFlow response[" + response.body().getSuccess() + "]");
                        Helper.v(tag, "OneFlow response message[" + response.body().getMessage() + "]");
                        Helper.v(tag, "OneFlow response size[" + response.body().getResult().size() + "]");
                        Helper.v(tag, "OneFlow response trigger name[" + response.body().getResult().get(0).getTrigger_event_name() + "]");
                        Helper.v(tag, "OneFlow response event name[" + response.body().getResult().get(0).getName() + "]");

                        int counter = 0;
                        for (GetSurveyListResponse gsl : response.body().getResult()) {
                            if (gsl.getTrigger_event_name() == null || gsl.getTrigger_event_name().equalsIgnoreCase("")) {
                                gsl.setTrigger_event_name("empty" + counter++);
                            }
                        }

                        Helper.v(tag,"OneFlow counter reached at["+counter+"]");
                        new OneFlowSHP(context).setSurveyList(response.body().getResult());
                        Intent intent = new Intent(context, SurveyList.class);
                        //intent.putExtra("SurveyType", "tap_skip_subs");//"move_file_in_folder");//""empty0");//
                        context.startActivity(intent);

                    } else {
                        //mrh.onResponseReceived(response.body(), type);
                        Helper.v(tag, "OneFlow response 0[" + response.body() + "]");
                        Helper.v(tag, "OneFlow response 1[" + response.body().getMessage() + "]");
                        Helper.v(tag, "OneFlow response 2[" + response.body().getSuccess() + "]");
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
    public static void submitUserResponse(Context context, SurveyUserResponse sur) {
        ApiInterface connectAPI = RetroBaseService.getClient().create(ApiInterface.class);
        try {
            Call<GenericResponse<String>> responseCall = null;

            responseCall = connectAPI.submitSurveyUserResponse(sur);

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
                        new OneFlowSHP(context).storeValue(sur.getSurvey_id(),true);
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
