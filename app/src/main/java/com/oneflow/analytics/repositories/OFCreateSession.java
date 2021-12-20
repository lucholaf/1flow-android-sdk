package com.oneflow.analytics.repositories;

import android.content.Context;

import com.oneflow.analytics.model.OFApiInterface;
import com.oneflow.analytics.model.OFGenericResponse;
import com.oneflow.analytics.model.OFRetroBaseService;
import com.oneflow.analytics.model.createsession.OFCreateSessionRequest;
import com.oneflow.analytics.model.createsession.OFCreateSessionResponse;
import com.oneflow.analytics.sdkdb.OFOneFlowSHP;
import com.oneflow.analytics.utils.OFConstants;
import com.oneflow.analytics.utils.OFHelper;
import com.oneflow.analytics.utils.OFMyResponseHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OFCreateSession {
    static String tag = "CreateSession";
    public static void createSession(OFCreateSessionRequest csr, Context context, OFMyResponseHandler mrh, OFConstants.ApiHitType hitType){

        OFApiInterface connectAPI = OFRetroBaseService.getClient().create(OFApiInterface.class);
        try {
            Call<OFGenericResponse<OFCreateSessionResponse>> responseCall = null;
            String url = "https://us-west-2.aws.webhooks.mongodb-realm.com/api/client/v2.0/app/1flow-wslxs/service/sessions/incoming_webhook/add_sessions";
            responseCall = connectAPI.createSession(new OFOneFlowSHP(context).getStringValue(OFConstants.APPIDSHP),csr,url);

            responseCall.enqueue(new Callback<OFGenericResponse<OFCreateSessionResponse>>() {
                @Override
                public void onResponse(Call<OFGenericResponse<OFCreateSessionResponse>> call, Response<OFGenericResponse<OFCreateSessionResponse>> response) {
                    OFHelper.v(tag, "OneFlow reached success["+response.isSuccessful()+"]");
                    OFHelper.v(tag, "OneFlow reached success raw["+response.raw()+"]");
                    OFHelper.v(tag, "OneFlow reached success errorBody["+response.errorBody()+"]");
                    OFHelper.v(tag, "OneFlow reached success message["+response.message()+"]");


                    if (response.isSuccessful()) {
                        OFHelper.v(tag,"OneFlow response["+response.body().toString()+"]");
                        OFHelper.v(tag,"OneFlow response["+response.body().getSuccess()+"]");
                        OFHelper.v(tag,"OneFlow response["+response.body().getMessage()+"]");
                        OFHelper.v(tag,"OneFlow response["+response.body().getResult().get_id()+"]");
                        OFHelper.v(tag,"OneFlow response["+response.body().getResult().getSystem_id()+"]");
                        new OFOneFlowSHP(context).storeValue(OFConstants.SESSIONDETAIL_IDSHP,response.body().getResult().get_id());
                        new OFOneFlowSHP(context).storeValue(OFConstants.SESSIONDETAIL_SYSTEM_IDSHP,response.body().getResult().getSystem_id());
                        mrh.onResponseReceived(hitType,null,0l);

                    } else {
                        //mrh.onResponseReceived(response.body(), type);
                        OFHelper.v(tag,"OneFlow response 0["+response.body()+"]");
                       // Helper.v(tag,"OneFlow response 1["+response.body().getMessage()+"]");
                       // Helper.v(tag,"OneFlow response 2["+response.body().getSuccess()+"]");

                    }
                }

                @Override
                public void onFailure(Call<OFGenericResponse<OFCreateSessionResponse>> call, Throwable t) {

                    OFHelper.e(tag,"OneFlow error["+t.toString()+"]");
                    OFHelper.e(tag,"OneFlow errorMsg["+t.getMessage()+"]");

                }
            });
        } catch (Exception ex) {

        }

    }
}
