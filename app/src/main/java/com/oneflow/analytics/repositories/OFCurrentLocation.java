/*
 *  Copyright 2021 1Flow, Inc.
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

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
