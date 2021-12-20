package com.oneflow.analytics.repositories;

import android.content.Context;

import com.oneflow.analytics.model.OFApiInterface;
import com.oneflow.analytics.model.OFRetroBaseService;
import com.oneflow.analytics.model.location.OFLocationResponse;
import com.oneflow.analytics.sdkdb.OFOneFlowSHP;
import com.oneflow.analytics.utils.OFConstants;
import com.oneflow.analytics.utils.OFHelper;
import com.oneflow.analytics.utils.OFMyResponseHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OFCurrentLocation {

    static String tag = "CurrentLocation";
    public static void getCurrentLocation(Context context, OFMyResponseHandler mrh, OFConstants.ApiHitType type){

        OFHelper.v(tag,"OneFlow current location fetching ");
        OFApiInterface connectAPI = OFRetroBaseService.getClient().create(OFApiInterface.class);
        try {
            Call<OFLocationResponse> responseCall = null;

            responseCall = connectAPI.getLocation(new OFOneFlowSHP(context).getStringValue(OFConstants.APPIDSHP));

            responseCall.enqueue(new Callback<OFLocationResponse>() {
                @Override
                public void onResponse(Call<OFLocationResponse> call, Response<OFLocationResponse> response) {

                    if (response.isSuccessful()) {
                        OFHelper.v(tag,"OneFlow location response["+response.body()+"]");
                        OFHelper.v(tag,"OneFlow location response["+response.body().toString()+"]");
                        /*Helper.v(tag,"OneFlow response["+response.body().getSuccess()+"]");*/

                        new OFOneFlowSHP(context).setUserLocationDetails(response.body());
                        mrh.onResponseReceived(type,null,0l);

                    } else {
                        //mrh.onResponseReceived(response.body(), type);
                        OFHelper.v(tag,"OneFlow response 0["+response.body()+"]");


                    }
                }

                @Override
                public void onFailure(Call<OFLocationResponse> call, Throwable t) {

                    OFHelper.e(tag,"OneFlow error["+t.toString()+"]");
                    OFHelper.e(tag,"OneFlow errorMsg["+t.getMessage()+"]");

                }
            });
        } catch (Exception ex) {
            OFHelper.e(tag,"OneFlow error["+ex.getMessage()+"]");
        }

    }
}
