package com.oneflow.analytics.repositories;

import android.content.Context;

import com.oneflow.analytics.model.OFApiInterface;
import com.oneflow.analytics.model.OFGenericResponse;
import com.oneflow.analytics.model.OFRetroBaseService;
import com.oneflow.analytics.model.events.OFEventAPIRequest;
import com.oneflow.analytics.model.events.OFEventSubmitResponse;
import com.oneflow.analytics.sdkdb.OFOneFlowSHP;
import com.oneflow.analytics.utils.OFConstants;
import com.oneflow.analytics.utils.OFHelper;
import com.oneflow.analytics.utils.OFMyResponseHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OFEventAPIRepo {
    static String tag = "EventAPIRepo";
    public static void sendLogsToApi(Context context, OFEventAPIRequest ear, OFMyResponseHandler mrh, OFConstants.ApiHitType type, Integer []ids){

        OFHelper.v(tag,"OneFlow sendLogsToApi reached");
        OFApiInterface connectAPI = OFRetroBaseService.getClient().create(OFApiInterface.class);
        try {
            Call<OFGenericResponse<OFEventSubmitResponse>> responseCall = null;


            String url = "https://us-west-2.aws.webhooks.mongodb-realm.com/api/client/v2.0/app/1flow-wslxs/service/events-bulk/incoming_webhook/insert-events";
            responseCall = connectAPI.uploadAllUnSyncedEvents(new OFOneFlowSHP(context).getStringValue(OFConstants.APPIDSHP),ear,url);

            responseCall.enqueue(new Callback<OFGenericResponse<OFEventSubmitResponse>>() {
                @Override
                public void onResponse(Call<OFGenericResponse<OFEventSubmitResponse>> call, Response<OFGenericResponse<OFEventSubmitResponse>> response) {
                    OFHelper.v(tag, "OneFlow sendLogsToApi reached success["+response.isSuccessful()+"]");
                    OFHelper.v(tag, "OneFlow sendLogsToApi reached success raw["+response.raw()+"]");
                    OFHelper.v(tag, "OneFlow sendLogsToApi reached success errorBody["+response.errorBody()+"]");
                    OFHelper.v(tag, "OneFlow sendLogsToApi reached success message["+response.message()+"]");


                    if (response.isSuccessful()) {
                        OFHelper.v(tag,"OneFlow response["+response.body().toString()+"]");
                        OFHelper.v(tag,"OneFlow response["+response.body().getSuccess()+"]");
                        mrh.onResponseReceived(type,ids,0l);
                    } else {
                        OFHelper.v(tag,"OneFlow response 0["+response.body()+"]");
                    }
                }

                @Override
                public void onFailure(Call<OFGenericResponse<OFEventSubmitResponse>> call, Throwable t) {


                    OFHelper.e(tag,"OneFlow error["+t.toString()+"]");
                    OFHelper.e(tag,"OneFlow errorMsg["+t.getMessage()+"]");

                }
            });
        } catch (Exception ex) {

        }

    }
}
