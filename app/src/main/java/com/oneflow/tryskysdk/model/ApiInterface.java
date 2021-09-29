package com.oneflow.tryskysdk.model;

import com.oneflow.tryskysdk.model.adduser.AddUserRequest;
import com.oneflow.tryskysdk.model.adduser.AddUserResponse;
import com.oneflow.tryskysdk.model.adduser.AddUserResultResponse;
import com.oneflow.tryskysdk.model.createsession.CreateSessionRequest;
import com.oneflow.tryskysdk.model.createsession.CreateSessionResponse;
import com.oneflow.tryskysdk.model.location.LocationResponse;
import com.oneflow.tryskysdk.model.survey.SurveyUserResponse;
import com.oneflow.tryskysdk.model.survey.GetSurveyListResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

    //1XdRfcEB8jVN05hkDk/+ltke3BHrQ3R9W35JBylCWzg=

    String headerKey = "7oKyqBl/myk8h1Zkq1uSkxffXe9U+p6trHLqA2q1JOU=";

    @Headers("one_flow_key:" + headerKey)
    @POST("v1/2021-06-15/project_users")
    Call<GenericResponse<AddUserResultResponse>> addUserComman(@Body AddUserRequest aur);

    @Headers("one_flow_key:" + headerKey)
    @POST("v1/2021-06-15/sessions")
    Call<GenericResponse<CreateSessionResponse>> createSession(@Body CreateSessionRequest aur);

    @Headers("one_flow_key:" + headerKey)
    @GET("v1/2021-06-15/survey")
    Call<GenericResponse<ArrayList<GetSurveyListResponse>>> getSurvey();

    @Headers("one_flow_key:" + headerKey)
    @GET("v1/2021-06-15/location")
    Call<GenericResponse<LocationResponse>> getLocation();

    @Headers("one_flow_key:" + headerKey)
    @POST("v1/2021-06-15/survey-response")
    Call<GenericResponse<String>> submitSurveyUserResponse(@Body SurveyUserResponse aur);

    @GET("v1/2021-06-15/keys/{project_id}")
    Call<String> fetchProjectDetails(@Path("project_id") String projectKey);


    /*@POST("v1/2021-06-15/events/bulk")
    Call<GenericResponse<AddUserResponse>> addEvents(@Body AddUserRequest aur);

    @POST("v1/2021-06-15/json")
    Call<GenericResponse<AddUserResponse>> uploadFile(@Body AddUserRequest aur);*/


}
