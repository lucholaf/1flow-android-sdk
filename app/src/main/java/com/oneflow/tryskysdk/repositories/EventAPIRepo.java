package com.oneflow.tryskysdk.repositories;

import android.content.Context;

import com.oneflow.tryskysdk.model.ApiInterface;
import com.oneflow.tryskysdk.model.GenericResponse;
import com.oneflow.tryskysdk.model.RetroBaseService;
import com.oneflow.tryskysdk.model.adduser.AddUserRequest;
import com.oneflow.tryskysdk.model.adduser.AddUserResultResponse;
import com.oneflow.tryskysdk.model.events.EventAPIRequest;
import com.oneflow.tryskysdk.model.events.EventSubmitResponse;
import com.oneflow.tryskysdk.model.events.RecordEventsTab;
import com.oneflow.tryskysdk.model.events.RecordEventsTabToAPI;
import com.oneflow.tryskysdk.utils.Constants;
import com.oneflow.tryskysdk.utils.Helper;
import com.oneflow.tryskysdk.utils.MyResponseHandler;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventAPIRepo {
    static String tag = "EventAPIRepo";
    public static void sendLogsToApi(Context context, EventAPIRequest ear, MyResponseHandler mrh, Constants.ApiHitType type,Integer []ids){

        Helper.v(tag,"OneFlow sendLogsToApi reached");
        ApiInterface connectAPI = RetroBaseService.getClient().create(ApiInterface.class);
        try {
            Call<GenericResponse<EventSubmitResponse>> responseCall = null;

            responseCall = connectAPI.uploadAllUnSyncedEvents(ear);

            responseCall.enqueue(new Callback<GenericResponse<EventSubmitResponse>>() {
                @Override
                public void onResponse(Call<GenericResponse<EventSubmitResponse>> call, Response<GenericResponse<EventSubmitResponse>> response) {
                    Helper.v(tag, "OneFlow sendLogsToApi reached success["+response.isSuccessful()+"]");
                    Helper.v(tag, "OneFlow sendLogsToApi reached success raw["+response.raw()+"]");
                    Helper.v(tag, "OneFlow sendLogsToApi reached success errorBody["+response.errorBody()+"]");
                    Helper.v(tag, "OneFlow sendLogsToApi reached success message["+response.message()+"]");


                    if (response.isSuccessful()) {
                        Helper.v(tag,"OneFlow response["+response.body().toString()+"]");
                        Helper.v(tag,"OneFlow response["+response.body().getSuccess()+"]");
                        mrh.onResponseReceived(type,ids,0);
                    } else {
                        Helper.v(tag,"OneFlow response 0["+response.body()+"]");
                    }
                }

                @Override
                public void onFailure(Call<GenericResponse<EventSubmitResponse>> call, Throwable t) {


                    Helper.e(tag,"OneFlow error["+t.toString()+"]");
                    Helper.e(tag,"OneFlow errorMsg["+t.getMessage()+"]");

                }
            });
        } catch (Exception ex) {

        }

    }
}
