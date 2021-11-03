package com.oneflow.analytics.repositories;

import android.content.Context;

import com.oneflow.analytics.model.ApiInterface;
import com.oneflow.analytics.model.GenericResponse;
import com.oneflow.analytics.model.RetroBaseService;
import com.oneflow.analytics.model.events.EventAPIRequest;
import com.oneflow.analytics.model.events.EventSubmitResponse;
import com.oneflow.analytics.sdkdb.OneFlowSHP;
import com.oneflow.analytics.utils.Constants;
import com.oneflow.analytics.utils.Helper;
import com.oneflow.analytics.utils.MyResponseHandler;

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


            String url = "https://us-west-2.aws.webhooks.mongodb-realm.com/api/client/v2.0/app/1flow-wslxs/service/events-bulk/incoming_webhook/insert-events";
            responseCall = connectAPI.uploadAllUnSyncedEvents(new OneFlowSHP(context).getStringValue(Constants.APPIDSHP),ear,url);

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
