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
import com.oneflow.analytics.model.adduser.OFAddUserResultResponse;
import com.oneflow.analytics.model.loguser.OFLogUserRequest;
import com.oneflow.analytics.model.loguser.OFLogUserResponse;
import com.oneflow.analytics.sdkdb.OFOneFlowSHP;
import com.oneflow.analytics.utils.OFConstants;
import com.oneflow.analytics.utils.OFHelper;
import com.oneflow.analytics.utils.OFMyResponseHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OFLogUserRepo {

    static String tag = "LogUserRepo";
    OFMyResponseHandler myResponseHandler;
    public static void logUser(OFLogUserRequest lur, Context context, OFMyResponseHandler mrh, OFConstants.ApiHitType hitType){

        OFApiInterface connectAPI = OFRetroBaseService.getClient().create(OFApiInterface.class);
        try {
            Call<OFGenericResponse<OFLogUserResponse>> responseCall = null;
            String url = "https://us-west-2.aws.webhooks.mongodb-realm.com/api/client/v2.0/app/1flow-wslxs/service/Log-user/incoming_webhook/anonymous-user-api";
            responseCall = connectAPI.logUser(new OFOneFlowSHP(context).getStringValue(OFConstants.APPIDSHP),lur,url);

            responseCall.enqueue(new Callback<OFGenericResponse<OFLogUserResponse>>() {
                @Override
                public void onResponse(Call<OFGenericResponse<OFLogUserResponse>> call, Response<OFGenericResponse<OFLogUserResponse>> response) {


                    if (response.isSuccessful()) {

                        // replacing current session id and user analytical id
                        OFOneFlowSHP ofs = new OFOneFlowSHP(context);
                        ofs.storeValue(OFConstants.SHP_LOG_USER_KEY,ofs.getLogUserRequest().getSystem_id()); // system id stored for sending next app launch
                        ofs.clearLogUserRequest();
                        OFAddUserResultResponse aurr = ofs.getUserDetails();
                        //setting up new user analytical id
                        OFHelper.v(tag,"OneFlow new Analytic id["+response.body().getResult().getAnalytic_user_id()+"] old Analytic id["+aurr.getAnalytic_user_id()+"]");
                        aurr.setAnalytic_user_id(response.body().getResult().getAnalytic_user_id());
                        ofs.setUserDetails(aurr);
                        OFHelper.v(tag,"OneFlow new Session id["+response.body().getResult().getSessionId()+"] old Session id["+ofs.getStringValue(OFConstants.SESSIONDETAIL_IDSHP)+"]");
                        ofs.storeValue(OFConstants.SESSIONDETAIL_IDSHP,response.body().getResult().getSessionId());

                        //storing this to support multi user survey
                        ofs.storeValue(OFConstants.USERUNIQUEIDSHP,lur.getSystem_id());

                       // mrh.onResponseReceived(hitType,null,0);
                        OFHelper.v(tag,"OneFlow record inserted...");

                        //Update old survey's user id
                        OFLogUserDBRepo.updateSurveyUserId(context,mrh,lur.getSystem_id(),hitType);


                    } else {
                        OFHelper.v(tag,"OneFlow response 0["+response.body()+"]");
                       /* Helper.v(tag,"OneFlow response 1["+response.body().getMessage()+"]");
                        Helper.v(tag,"OneFlow response 2["+response.body().getSuccess()+"]");*/

                    }
                }

                @Override
                public void onFailure(Call<OFGenericResponse<OFLogUserResponse>> call, Throwable t) {

                    OFHelper.e(tag,"OneFlow error["+t.toString()+"]");
                    OFHelper.e(tag,"OneFlow errorMsg["+t.getMessage()+"]");

                }
            });
        } catch (Exception ex) {

        }

    }
}
