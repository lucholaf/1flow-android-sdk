package com.circo.oneflow.repositories;

import android.content.Context;
import android.content.Intent;

import com.circo.oneflow.model.ApiInterface;
import com.circo.oneflow.model.GenericResponse;
import com.circo.oneflow.model.RetroBaseService;
import com.circo.oneflow.model.survey.GetSurveyListResponse;
import com.circo.oneflow.model.survey.SurveyUserInput;
import com.circo.oneflow.sdkdb.OneFlowSHP;
import com.circo.oneflow.utils.Constants;
import com.circo.oneflow.utils.Helper;
import com.circo.oneflow.utils.MyResponseHandler;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Survey {

    static String tag = "Survey";

    public static void getSurvey(Context context) {
        Helper.v(tag, "OneFlow survey reached getSurvey");
        ApiInterface connectAPI = RetroBaseService.getClient().create(ApiInterface.class);
        try {
            Call<GenericResponse<ArrayList<GetSurveyListResponse>>> responseCall = null;

            responseCall = connectAPI.getSurvey(new OneFlowSHP(context).getStringValue(Constants.APPIDSHP),"android");

            responseCall.enqueue(new Callback<GenericResponse<ArrayList<GetSurveyListResponse>>>() {
                @Override
                public void onResponse(Call<GenericResponse<ArrayList<GetSurveyListResponse>>> call, Response<GenericResponse<ArrayList<GetSurveyListResponse>>> response) {


                    Helper.v(tag, "OneFlow survey reached success[" + response.isSuccessful() + "]");
                    Helper.v(tag, "OneFlow survey reached success raw[" + response.raw() + "]");
                    Helper.v(tag, "OneFlow survey reached success errorBody[" + response.errorBody() + "]");
                    Helper.v(tag, "OneFlow survey reached success message[" + response.message() + "]");


                    if (response.isSuccessful()) {
                        Helper.v(tag, "OneFlow survey response[" + response.body().toString() + "]");
                        Helper.v(tag, "OneFlow survey response[" + response.body().getSuccess() + "]");
                        Helper.v(tag, "OneFlow survey response message[" + response.body().getMessage() + "]");
                        Helper.v(tag, "OneFlow survey response size[" + response.body().getResult().size() + "]");
                        Helper.v(tag, "OneFlow survey response trigger name[" + response.body().getResult().get(0).getTrigger_event_name() + "]");
                        Helper.v(tag, "OneFlow survey response event name[" + response.body().getResult().get(0).getName() + "]");

                        int counter = 0;
                        for (GetSurveyListResponse gsl : response.body().getResult()) {
                            if (gsl.getTrigger_event_name() == null || gsl.getTrigger_event_name().equalsIgnoreCase("")) {
                                gsl.setTrigger_event_name("empty" + counter++);
                            }
                        }

                        Helper.v(tag,"OneFlow counter reached at["+counter+"]");
                        new OneFlowSHP(context).setSurveyList(response.body().getResult());
                        //Intent intent = new Intent(context, SurveyList.class);
                        //intent.putExtra("SurveyType", "tap_skip_subs");//"move_file_in_folder");//""empty0");//
                        //context.startActivity(intent);
                        Intent intent = new Intent("survey_list_fetched");
                        context.sendBroadcast(intent);

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
