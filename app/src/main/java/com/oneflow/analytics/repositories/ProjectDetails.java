package com.oneflow.analytics.repositories;

import android.content.Context;

import com.oneflow.analytics.model.ApiInterface;
import com.oneflow.analytics.model.RetroBaseService;
import com.oneflow.analytics.sdkdb.OneFlowSHP;
import com.oneflow.analytics.utils.Constants;
import com.oneflow.analytics.utils.Helper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectDetails {
    static String tag = "ProjectDetails";

    public static void getProject(Context context) {
        ApiInterface connectAPI = RetroBaseService.getClient().create(ApiInterface.class);
        try {
            Call<String> responseCall = null;
            String projectKey = new OneFlowSHP(context).getStringValue(Constants.APPIDSHP);
            responseCall = connectAPI.fetchProjectDetails(new OneFlowSHP(context).getStringValue(Constants.APPIDSHP),projectKey);

            responseCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {


                    Helper.v(tag, "OneFlow reached success[" + response.isSuccessful() + "]");
                    Helper.v(tag, "OneFlow reached success raw[" + response.raw() + "]");
                    Helper.v(tag, "OneFlow reached success errorBody[" + response.errorBody() + "]");
                    Helper.v(tag, "OneFlow reached success message[" + response.message() + "]");


                    if (response.isSuccessful()) {
                        Helper.v(tag, "OneFlow response[" + response.body().toString() + "]");


                        //new OneFlowSHP(context).setSurveyList(response.body().getResult(), Constants.SURVEYLISTSHP);

                    } else {
                        //mrh.onResponseReceived(response.body(), type);
                        Helper.v(tag, "OneFlow response 0[" + response.body() + "]");

                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                    Helper.e(tag, "OneFlow error[" + t.toString() + "]");
                    Helper.e(tag, "OneFlow errorMsg[" + t.getMessage() + "]");

                }
            });
        } catch (Exception ex) {

        }
    }
}
