package com.oneflow.analytics.repositories;

import android.content.Context;

import com.oneflow.analytics.model.ApiInterface;
import com.oneflow.analytics.model.GenericResponse;
import com.oneflow.analytics.model.RetroBaseService;
import com.oneflow.analytics.model.adduser.AddUserResultResponse;
import com.oneflow.analytics.model.loguser.LogUserRequest;
import com.oneflow.analytics.model.loguser.LogUserResponse;
import com.oneflow.analytics.sdkdb.OneFlowSHP;
import com.oneflow.analytics.utils.Constants;
import com.oneflow.analytics.utils.Helper;
import com.oneflow.analytics.utils.MyResponseHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogUserRepo {

    static String tag = "LogUserRepo";
    MyResponseHandler myResponseHandler;
    public static void logUser(LogUserRequest lur, Context context, MyResponseHandler mrh, Constants.ApiHitType hitType){

        ApiInterface connectAPI = RetroBaseService.getClient().create(ApiInterface.class);
        try {
            Call<GenericResponse<LogUserResponse>> responseCall = null;
            String url = "https://us-west-2.aws.webhooks.mongodb-realm.com/api/client/v2.0/app/1flow-wslxs/service/Log-user/incoming_webhook/anonymous-user-api";
            responseCall = connectAPI.logUser(new OneFlowSHP(context).getStringValue(Constants.APPIDSHP),lur,url);

            responseCall.enqueue(new Callback<GenericResponse<LogUserResponse>>() {
                @Override
                public void onResponse(Call<GenericResponse<LogUserResponse>> call, Response<GenericResponse<LogUserResponse>> response) {


                    if (response.isSuccessful()) {

                        // replacing current session id and user analytical id
                        OneFlowSHP ofs = new OneFlowSHP(context);
                        ofs.clearLogUserRequest();
                        AddUserResultResponse aurr = ofs.getUserDetails();
                        //setting up new user analytical id
                        Helper.v(tag,"OneFlow new Analytic id["+response.body().getResult().getAnalytic_user_id()+"] old Analytic id["+aurr.getAnalytic_user_id()+"]");
                        aurr.setAnalytic_user_id(response.body().getResult().getAnalytic_user_id());
                        ofs.setUserDetails(aurr);
                        Helper.v(tag,"OneFlow new Session id["+response.body().getResult().getSessionId()+"] old Session id["+ofs.getStringValue(Constants.SESSIONDETAIL_IDSHP)+"]");
                        ofs.storeValue(Constants.SESSIONDETAIL_IDSHP,response.body().getResult().getSessionId());

                        //storing this to support multi user survey
                        ofs.storeValue(Constants.USERUNIQUEIDSHP,lur.getSystem_id());

                       // mrh.onResponseReceived(hitType,null,0);
                        Helper.v(tag,"OneFlow record inserted...");

                        //Update old survey's user id
                        LogUserDBRepo.updateSurveyUserId(context,lur.getSystem_id());

                    } else {
                        Helper.v(tag,"OneFlow response 0["+response.body()+"]");
                       /* Helper.v(tag,"OneFlow response 1["+response.body().getMessage()+"]");
                        Helper.v(tag,"OneFlow response 2["+response.body().getSuccess()+"]");*/

                    }
                }

                @Override
                public void onFailure(Call<GenericResponse<LogUserResponse>> call, Throwable t) {

                    Helper.e(tag,"OneFlow error["+t.toString()+"]");
                    Helper.e(tag,"OneFlow errorMsg["+t.getMessage()+"]");

                }
            });
        } catch (Exception ex) {

        }

    }
}
