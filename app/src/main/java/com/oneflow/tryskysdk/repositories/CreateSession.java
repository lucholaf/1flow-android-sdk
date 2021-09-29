package com.oneflow.tryskysdk.repositories;

import android.content.Context;

import com.oneflow.tryskysdk.model.ApiInterface;
import com.oneflow.tryskysdk.model.GenericResponse;
import com.oneflow.tryskysdk.model.RetroBaseService;
import com.oneflow.tryskysdk.model.adduser.AddUserRequest;
import com.oneflow.tryskysdk.model.adduser.AddUserResultResponse;
import com.oneflow.tryskysdk.model.createsession.CreateSessionRequest;
import com.oneflow.tryskysdk.model.createsession.CreateSessionResponse;
import com.oneflow.tryskysdk.sdkdb.OneFlowSHP;
import com.oneflow.tryskysdk.sdkdb.SDKDB;
import com.oneflow.tryskysdk.utils.Constants;
import com.oneflow.tryskysdk.utils.Helper;
import com.oneflow.tryskysdk.utils.MyResponseHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateSession {
    static String tag = "CreateSession";
    public static void createSession(CreateSessionRequest csr, Context context, MyResponseHandler mrh, Constants.ApiHitType hitType){

        ApiInterface connectAPI = RetroBaseService.getClient().create(ApiInterface.class);
        try {
            Call<GenericResponse<CreateSessionResponse>> responseCall = null;

            responseCall = connectAPI.createSession(csr);

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
