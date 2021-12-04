package com.oneflow.analytics.repositories;

import android.content.Context;

import com.oneflow.analytics.model.ApiInterface;
import com.oneflow.analytics.model.GenericResponse;
import com.oneflow.analytics.model.RetroBaseService;
import com.oneflow.analytics.model.createsession.CreateSessionRequest;
import com.oneflow.analytics.model.createsession.CreateSessionResponse;
import com.oneflow.analytics.sdkdb.OneFlowSHP;
import com.oneflow.analytics.utils.Constants;
import com.oneflow.analytics.utils.Helper;
import com.oneflow.analytics.utils.MyResponseHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateSession {
    static String tag = "CreateSession";
    public static void createSession(CreateSessionRequest csr, Context context, MyResponseHandler mrh, Constants.ApiHitType hitType){

        ApiInterface connectAPI = RetroBaseService.getClient().create(ApiInterface.class);
        try {
            Call<GenericResponse<CreateSessionResponse>> responseCall = null;
            String url = "https://us-west-2.aws.webhooks.mongodb-realm.com/api/client/v2.0/app/1flow-wslxs/service/sessions/incoming_webhook/add_sessions";
            responseCall = connectAPI.createSession(new OneFlowSHP(context).getStringValue(Constants.APPIDSHP),csr,url);

            responseCall.enqueue(new Callback<GenericResponse<CreateSessionResponse>>() {
                @Override
                public void onResponse(Call<GenericResponse<CreateSessionResponse>> call, Response<GenericResponse<CreateSessionResponse>> response) {
                    Helper.v(tag, "OneFlow reached success["+response.isSuccessful()+"]");
                    Helper.v(tag, "OneFlow reached success raw["+response.raw()+"]");
                    Helper.v(tag, "OneFlow reached success errorBody["+response.errorBody()+"]");
                    Helper.v(tag, "OneFlow reached success message["+response.message()+"]");


                    if (response.isSuccessful()) {
                        Helper.v(tag,"OneFlow response["+response.body().toString()+"]");
                        Helper.v(tag,"OneFlow response["+response.body().getSuccess()+"]");
                        Helper.v(tag,"OneFlow response["+response.body().getMessage()+"]");
                        Helper.v(tag,"OneFlow response["+response.body().getResult().get_id()+"]");
                        Helper.v(tag,"OneFlow response["+response.body().getResult().getSystem_id()+"]");
                        new OneFlowSHP(context).storeValue(Constants.SESSIONDETAIL_IDSHP,response.body().getResult().get_id());
                        new OneFlowSHP(context).storeValue(Constants.SESSIONDETAIL_SYSTEM_IDSHP,response.body().getResult().getSystem_id());
                        mrh.onResponseReceived(hitType,null,0);

                    } else {
                        //mrh.onResponseReceived(response.body(), type);
                        Helper.v(tag,"OneFlow response 0["+response.body()+"]");
                       // Helper.v(tag,"OneFlow response 1["+response.body().getMessage()+"]");
                       // Helper.v(tag,"OneFlow response 2["+response.body().getSuccess()+"]");

                    }
                }

                @Override
                public void onFailure(Call<GenericResponse<CreateSessionResponse>> call, Throwable t) {

                    Helper.e(tag,"OneFlow error["+t.toString()+"]");
                    Helper.e(tag,"OneFlow errorMsg["+t.getMessage()+"]");

                }
            });
        } catch (Exception ex) {

        }

    }
}
