package com.oneflow.analytics.repositories;

import android.content.Context;

import com.oneflow.analytics.model.OFApiInterface;
import com.oneflow.analytics.model.OFRetroBaseService;
import com.oneflow.analytics.sdkdb.OFOneFlowSHP;
import com.oneflow.analytics.utils.OFConstants;
import com.oneflow.analytics.utils.OFHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OFProjectDetails {
    static String tag = "ProjectDetails";

    public static void getProject(Context context) {
        OFApiInterface connectAPI = OFRetroBaseService.getClient().create(OFApiInterface.class);
        try {
            Call<String> responseCall = null;
            String projectKey = new OFOneFlowSHP(context).getStringValue(OFConstants.APPIDSHP);
            responseCall = connectAPI.fetchProjectDetails(new OFOneFlowSHP(context).getStringValue(OFConstants.APPIDSHP),projectKey);

            responseCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {


                    OFHelper.v(tag, "OneFlow reached success[" + response.isSuccessful() + "]");
                    OFHelper.v(tag, "OneFlow reached success raw[" + response.raw() + "]");
                    OFHelper.v(tag, "OneFlow reached success errorBody[" + response.errorBody() + "]");
                    OFHelper.v(tag, "OneFlow reached success message[" + response.message() + "]");


                    if (response.isSuccessful()) {
                        OFHelper.v(tag, "OneFlow response[" + response.body().toString() + "]");


                        //new OneFlowSHP(context).setSurveyList(response.body().getResult(), Constants.SURVEYLISTSHP);

                    } else {
                        //mrh.onResponseReceived(response.body(), type);
                        OFHelper.v(tag, "OneFlow response 0[" + response.body() + "]");

                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                    OFHelper.e(tag, "OneFlow error[" + t.toString() + "]");
                    OFHelper.e(tag, "OneFlow errorMsg[" + t.getMessage() + "]");

                }
            });
        } catch (Exception ex) {

        }
    }
}
