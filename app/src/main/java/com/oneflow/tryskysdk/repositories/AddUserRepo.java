package com.oneflow.tryskysdk.repositories;

import android.content.Context;
import android.os.AsyncTask;

import com.oneflow.tryskysdk.model.ApiInterface;
import com.oneflow.tryskysdk.model.GenericResponse;
import com.oneflow.tryskysdk.model.RetroBaseService;
import com.oneflow.tryskysdk.model.adduser.AddUserRequest;
import com.oneflow.tryskysdk.model.adduser.AddUserResponse;
import com.oneflow.tryskysdk.model.adduser.AddUserResultResponse;
import com.oneflow.tryskysdk.sdkdb.OneFlowSHP;
import com.oneflow.tryskysdk.sdkdb.SDKDB;
import com.oneflow.tryskysdk.utils.Constants;
import com.oneflow.tryskysdk.utils.Helper;
import com.oneflow.tryskysdk.utils.MyResponseHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUserRepo {

    static String tag = "AddUserRepo";
    public static void addUser(AddUserRequest aur, Context context){

        ApiInterface connectAPI = RetroBaseService.getClient().create(ApiInterface.class);
        try {
            Call<GenericResponse<AddUserResultResponse>> responseCall = null;

            responseCall = connectAPI.addUserComman(aur);

            responseCall.enqueue(new Callback<GenericResponse<AddUserResultResponse>>() {
                @Override
                public void onResponse(Call<GenericResponse<AddUserResultResponse>> call, Response<GenericResponse<AddUserResultResponse>> response) {
                    Helper.v(tag, "OneFlow reached success["+response.isSuccessful()+"]");
                    Helper.v(tag, "OneFlow reached success raw["+response.raw()+"]");
                    Helper.v(tag, "OneFlow reached success errorBody["+response.errorBody()+"]");
                    Helper.v(tag, "OneFlow reached success message["+response.message()+"]");



                    if (response.isSuccessful()) {
                        Helper.v(tag,"OneFlow response["+response.body().toString()+"]");
                        Helper.v(tag,"OneFlow response["+response.body().getSuccess()+"]");
                        Helper.v(tag,"OneFlow response["+response.body().getMessage()+"]");
                        Helper.v(tag,"OneFlow response["+response.body().getResult().getAnalytic_user_id()+"]");

                        new OneFlowSHP(context).setUserDetails(Constants.USERDETAILSHP,response.body().getResult());

                       /* AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                Helper.v(tag,"OneFlow inserting data");
                                db.userDAO().insertUser(response.body().getResult());
                                Helper.v(tag,"OneFlow inserted data");
                            }
                        });*/

                       // db.userDAO().insertUser(response.body().getResult());
                        Helper.v(tag,"OneFlow record inserted...");
                    } else {
                        //mrh.onResponseReceived(response.body(), type);
                        Helper.v(tag,"OneFlow response 0["+response.body()+"]");
                        Helper.v(tag,"OneFlow response 1["+response.body().getMessage()+"]");
                        Helper.v(tag,"OneFlow response 2["+response.body().getSuccess()+"]");

                    }
                }

                @Override
                public void onFailure(Call<GenericResponse<AddUserResultResponse>> call, Throwable t) {

                    // mrh.onErrorReceived("QuestionFetch:Something went wrong", type);
                    /*BaseResponse br = new BaseResponse();
                    br.setMessage(t.getMessage());
                    br.setSuccess(0);*/

                    Helper.e(tag,"OneFlow error["+t.toString()+"]");
                    Helper.e(tag,"OneFlow errorMsg["+t.getMessage()+"]");

                }
            });
        } catch (Exception ex) {

        }

    }
}
