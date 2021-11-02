package com.circo.oneflow.repositories;

import android.content.Context;

import com.circo.oneflow.model.ApiInterface;
import com.circo.oneflow.model.GenericResponse;
import com.circo.oneflow.model.RetroBaseService;
import com.circo.oneflow.model.adduser.AddUserResultResponse;
import com.circo.oneflow.model.loguser.LogUserRequest;
import com.circo.oneflow.model.loguser.LogUserResponse;
import com.circo.oneflow.sdkdb.OneFlowSHP;
import com.circo.oneflow.utils.Constants;
import com.circo.oneflow.utils.Helper;
import com.circo.oneflow.utils.MyResponseHandler;

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

            responseCall = connectAPI.logUser(new OneFlowSHP(context).getStringValue(Constants.APPIDSHP),lur);

            responseCall.enqueue(new Callback<GenericResponse<LogUserResponse>>() {
                @Override
                public void onResponse(Call<GenericResponse<LogUserResponse>> call, Response<GenericResponse<LogUserResponse>> response) {
                    Helper.v(tag, "OneFlow reached success["+response.isSuccessful()+"]");
                    Helper.v(tag, "OneFlow reached success raw["+response.raw()+"]");
                    Helper.v(tag, "OneFlow reached success errorBody["+response.errorBody()+"]");
                    Helper.v(tag, "OneFlow reached success message["+response.message()+"]");

                    if (response.isSuccessful()) {
                        Helper.v(tag,"OneFlow response["+response.body().toString()+"]");
                        Helper.v(tag,"OneFlow response["+response.body().getSuccess()+"]");
                        Helper.v(tag,"OneFlow response["+response.body().getMessage()+"]");
                        Helper.v(tag,"OneFlow response["+response.body().getResult().getAnalytic_user_id()+"]");

                        // replacing current session id and user analytical id
                        //TODO ask rohan about this
                        OneFlowSHP ofs = new OneFlowSHP(context);
                        AddUserResultResponse aurr = ofs.getUserDetails();
                        //setting up new user analytical id
                        Helper.v(tag,"OneFlow new Analytic id["+response.body().getResult().getAnalytic_user_id()+"] old Analytic id["+aurr.getAnalytic_user_id()+"]");
                        aurr.setAnalytic_user_id(response.body().getResult().getAnalytic_user_id());
                        ofs.setUserDetails(aurr);

                        ofs.storeValue(Constants.SESSIONDETAIL_IDSHP,response.body().getResult().getSession().get_id());

                       // mrh.onResponseReceived(hitType,null,0);
                        Helper.v(tag,"OneFlow record inserted...");
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
