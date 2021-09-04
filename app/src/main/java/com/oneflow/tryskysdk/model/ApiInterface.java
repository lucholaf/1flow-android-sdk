package com.oneflow.tryskysdk.model;

import com.oneflow.tryskysdk.model.adduser.AddUserRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {

@POST("/v1/2021-06-15/project_users")
    Call<> addUser(@Body AddUserRequest aur);


}
