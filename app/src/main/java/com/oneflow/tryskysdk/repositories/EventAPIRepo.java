package com.oneflow.tryskysdk.repositories;

import android.content.Context;

import com.oneflow.tryskysdk.model.ApiInterface;
import com.oneflow.tryskysdk.model.GenericResponse;
import com.oneflow.tryskysdk.model.RetroBaseService;
import com.oneflow.tryskysdk.model.adduser.AddUserRequest;
import com.oneflow.tryskysdk.model.adduser.AddUserResultResponse;
import com.oneflow.tryskysdk.model.events.EventAPIRequest;
import com.oneflow.tryskysdk.model.events.RecordEventsTab;
import com.oneflow.tryskysdk.utils.Constants;
import com.oneflow.tryskysdk.utils.Helper;
import com.oneflow.tryskysdk.utils.MyResponseHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventAPIRepo {
    static String tag = "CreateSession";
    public static void sendLogsToApi(Context context, EventAPIRequest ear, MyResponseHandler mrh, Constants.ApiHitType type,Integer []ids){

        ApiInterface connectAPI = RetroBaseService.getClient().create(ApiInterface.class);
        try {
            Call<GenericResponse<String>> responseCall = null;

            responseCall = connectAPI.uploadAllUnSyncedEvents(ear);

            responseCall.enqueue(new Callback<GenericResponse<String>>() {
                @Override
                public void onResponse(Call<GenericResponse<String>> call, Response<GenericResponse<String>> response) {
                    Helper.v(tag, "OneFlow reached success["+response.isSuccessful()+"]");
                    Helper.v(tag, "OneFlow reached success raw["+response.raw()+"]");
                    Helper.v(tag, "OneFlow reached success errorBody["+response.errorBody()+"]");
                    Helper.v(tag, "OneFlow reached success message["+response.message()+"]");


                    if (response.isSuccessful()) {
                        Helper.v(tag,"OneFlow response["+response.body().toString()+"]");
                        Helper.v(tag,"OneFlow response["+response.body().getSuccess()+"]");
                        mrh.onResponseReceived(type,ids,0);
                    } else {

                        Helper.v(tag,"OneFlow response 0["+response.body()+"]");
                    }
                }

                @Override
                public void onFailure(Call<GenericResponse<String>> call, Throwable t) {

                    // mrh.onErrorReceived("QuestionFetch:Something went wrong", type);
                    /*BaseResponse br = new BaseResponse();
                    br.setMessage(t.getMessage());
                    br.setSuccess(0);*/

                    Helper.e(tag,"OneFlow error["+t.toString()+"]");
                    Helper.e(tag,"OneFlow errorMsg["+t.getMessage()+"]");

                }
            });
        } catch (Exception ex) {

        }

    }
}
