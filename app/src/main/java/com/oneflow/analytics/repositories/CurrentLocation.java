package com.oneflow.analytics.repositories;

import android.content.Context;

import com.oneflow.analytics.model.ApiInterface;
import com.oneflow.analytics.model.RetroBaseService;
import com.oneflow.analytics.model.location.LocationResponse;
import com.oneflow.analytics.sdkdb.OneFlowSHP;
import com.oneflow.analytics.utils.Constants;
import com.oneflow.analytics.utils.Helper;
import com.oneflow.analytics.utils.MyResponseHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrentLocation {

    static String tag = "CurrentLocation";
    public static void getCurrentLocation(Context context, MyResponseHandler mrh, Constants.ApiHitType type){

        Helper.v(tag,"OneFlow current location fetching ");
        ApiInterface connectAPI = RetroBaseService.getClient().create(ApiInterface.class);
        try {
            Call<LocationResponse> responseCall = null;

            responseCall = connectAPI.getLocation(new OneFlowSHP(context).getStringValue(Constants.APPIDSHP));

            responseCall.enqueue(new Callback<LocationResponse>() {
                @Override
                public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {

                    if (response.isSuccessful()) {
                        Helper.v(tag,"OneFlow location response["+response.body()+"]");
                        Helper.v(tag,"OneFlow location response["+response.body().toString()+"]");
                        /*Helper.v(tag,"OneFlow response["+response.body().getSuccess()+"]");*/

                        new OneFlowSHP(context).setUserLocationDetails(response.body());
                        mrh.onResponseReceived(type,null,0l);

                    } else {
                        //mrh.onResponseReceived(response.body(), type);
                        Helper.v(tag,"OneFlow response 0["+response.body()+"]");


                    }
                }

                @Override
                public void onFailure(Call<LocationResponse> call, Throwable t) {

                    Helper.e(tag,"OneFlow error["+t.toString()+"]");
                    Helper.e(tag,"OneFlow errorMsg["+t.getMessage()+"]");

                }
            });
        } catch (Exception ex) {
            Helper.e(tag,"OneFlow error["+ex.getMessage()+"]");
        }

    }
}
