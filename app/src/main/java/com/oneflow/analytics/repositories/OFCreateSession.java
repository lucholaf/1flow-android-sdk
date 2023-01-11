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

import com.oneflow.analytics.model.OFApiInterface;
import com.oneflow.analytics.model.OFGenericResponse;
import com.oneflow.analytics.model.OFRetroBaseService;
import com.oneflow.analytics.model.createsession.OFCreateSessionRequest;
import com.oneflow.analytics.model.createsession.OFCreateSessionResponse;
import com.oneflow.analytics.utils.OFConstants;
import com.oneflow.analytics.utils.OFHelper;
import com.oneflow.analytics.utils.OFMyResponseHandlerOneFlow;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OFCreateSession {
    static String tag = "CreateSession";
    public static void createSession(String headerKey, OFCreateSessionRequest csr, OFMyResponseHandlerOneFlow mrh, OFConstants.ApiHitType hitType){

        OFApiInterface connectAPI = OFRetroBaseService.getClient().create(OFApiInterface.class);
        try {
            Call<OFGenericResponse<OFCreateSessionResponse>> responseCall = null;
           // String url = "https://us-west-2.aws.webhooks.mongodb-realm.com/api/client/v2.0/app/1flow-wslxs/service/sessions/incoming_webhook/add_sessions";
            responseCall = connectAPI.createSession(headerKey,csr);//,url);

            responseCall.enqueue(new Callback<OFGenericResponse<OFCreateSessionResponse>>() {
                @Override
                public void onResponse(Call<OFGenericResponse<OFCreateSessionResponse>> call, Response<OFGenericResponse<OFCreateSessionResponse>> response) {
                    OFHelper.v(tag, "OneFlow reached session success["+response.isSuccessful()+"]");

                    if (response.isSuccessful()) {

                        if(response.body()!=null) {
                            OFHelper.v(tag, "OneFlow session created [" + response.body().getResult().getSystem_id() + "]");
                            mrh.onResponseReceived(hitType, response.body().getResult(), 0l, "",null,null);
                        }

                    } else {
                        //mrh.onResponseReceived(response.body(), type);
                        OFHelper.v(tag,"OneFlow session failed ["+response.body()+"]");
                        mrh.onResponseReceived(hitType,null,0l,response.message(),null,null);


                    }
                }

                @Override
                public void onFailure(Call<OFGenericResponse<OFCreateSessionResponse>> call, Throwable t) {

                    OFHelper.e(tag,"OneFlow create session error["+t.toString()+"]");
                    OFHelper.e(tag,"OneFlow creatd session errorMsg["+t.getMessage()+"]");
                    mrh.onResponseReceived(hitType,null,0l,"Something went wrong",null,null);

                }
            });
        } catch (Exception ex) {

        }

    }
}
