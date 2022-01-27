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
import com.oneflow.analytics.model.OFGenericResponse;
import com.oneflow.analytics.model.OFRetroBaseService;
import com.oneflow.analytics.model.adduser.OFAddUserRequest;
import com.oneflow.analytics.model.adduser.OFAddUserResultResponse;
import com.oneflow.analytics.sdkdb.OFOneFlowSHP;
import com.oneflow.analytics.utils.OFConstants;
import com.oneflow.analytics.utils.OFHelper;
import com.oneflow.analytics.utils.OFMyResponseHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OFAddUserRepo {

    static String tag = "OFAddUserRepo";
    OFMyResponseHandler myResponseHandler;
    public static void addUser(OFAddUserRequest aur, Context context, OFMyResponseHandler mrh, OFConstants.ApiHitType hitType){

        OFApiInterface connectAPI = OFRetroBaseService.getClient().create(OFApiInterface.class);
        try {
            Call<OFGenericResponse<OFAddUserResultResponse>> responseCall = null;


            String url = "https://us-west-2.aws.webhooks.mongodb-realm.com/api/client/v2.0/app/1flow-wslxs/service/project-analytics-user/incoming_webhook/add-user";
            responseCall = connectAPI.addUserComman(new OFOneFlowSHP(context).getStringValue(OFConstants.APPIDSHP),aur,url);

            responseCall.enqueue(new Callback<OFGenericResponse<OFAddUserResultResponse>>() {
                @Override
                public void onResponse(Call<OFGenericResponse<OFAddUserResultResponse>> call, Response<OFGenericResponse<OFAddUserResultResponse>> response) {
                    OFHelper.v(tag, "OneFlow add user reached success["+response.isSuccessful()+"]");

                    if (response.isSuccessful()) {
                        OFHelper.v(tag,"OneFlow add user response["+response.body().toString()+"]");

                        new OFOneFlowSHP(context).setUserDetails(response.body().getResult());
                        mrh.onResponseReceived(hitType,null,0l);

                       /* AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                Helper.v(tag,"OneFlow inserting data");
                                db.userDAO().insertUser(response.body().getResult());
                                Helper.v(tag,"OneFlow inserted data");
                            }
                        }); */

                        OFHelper.v(tag,"OneFlow record inserted...");
                    } else {
                        OFHelper.v(tag,"OneFlow response 0["+response.body()+"]");
                       /* Helper.v(tag,"OneFlow response 1["+response.body().getMessage()+"]");
                        Helper.v(tag,"OneFlow response 2["+response.body().getSuccess()+"]");*/

                    }
                }

                @Override
                public void onFailure(Call<OFGenericResponse<OFAddUserResultResponse>> call, Throwable t) {

                    OFHelper.e(tag,"OneFlow error["+t.toString()+"]");
                    OFHelper.e(tag,"OneFlow errorMsg["+t.getMessage()+"]");

                }
            });
        } catch (Exception ex) {

        }

    }
}
