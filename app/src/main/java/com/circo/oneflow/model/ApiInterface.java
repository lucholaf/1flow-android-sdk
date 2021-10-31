package com.circo.oneflow.model;

import com.circo.oneflow.model.adduser.AddUserRequest;
import com.circo.oneflow.model.adduser.AddUserResultResponse;
import com.circo.oneflow.model.createsession.CreateSessionRequest;
import com.circo.oneflow.model.createsession.CreateSessionResponse;
import com.circo.oneflow.model.events.EventAPIRequest;
import com.circo.oneflow.model.events.EventSubmitResponse;
import com.circo.oneflow.model.location.LocationResponse;
import com.circo.oneflow.model.loguser.LogUserRequest;
import com.circo.oneflow.model.loguser.LogUserResponse;
import com.circo.oneflow.model.survey.SurveyUserInput;
import com.circo.oneflow.model.survey.GetSurveyListResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @POST("v1/2021-06-15/project_users")
    Call<GenericResponse<AddUserResultResponse>> addUserComman(@Header("one_flow_key") String headerKey,@Body AddUserRequest aur);

    @POST("v1/2021-06-15/sessions")
    Call<GenericResponse<CreateSessionResponse>> createSession(@Header("one_flow_key") String headerKey,@Body CreateSessionRequest aur);

    @GET("v1/2021-06-15/survey")
    Call<GenericResponse<ArrayList<GetSurveyListResponse>>> getSurvey(@Header("one_flow_key") String headerKey,@Query("platform") String platform);

    @GET("v1/2021-06-15/location")
    Call<GenericResponse<LocationResponse>> getLocation(@Header("one_flow_key") String headerKey);

    @POST("v1/2021-06-15/survey-response")
    Call<GenericResponse<String>> submitSurveyUserResponse(@Header("one_flow_key") String headerKey,@Body SurveyUserInput aur);

    @POST("v1/2021-06-15/events/bulk")
    Call<GenericResponse<EventSubmitResponse>> uploadAllUnSyncedEvents(@Header("one_flow_key") String headerKey,@Body EventAPIRequest ear);

    @GET("v1/2021-06-15/keys/{project_id}")
    Call<String> fetchProjectDetails(@Header("one_flow_key") String headerKey,@Path("project_id") String projectKey);

    @POST("v1/2021-06-15/project_users/log_user")
    Call<GenericResponse<LogUserResponse>> logUser(@Header("one_flow_key") String headerKey, @Body LogUserRequest request);

   /* @POST("v1/2021-06-15/json")
    Call<GenericResponse<AddUserResponse>> uploadFile(@Body AddUserRequest aur);*/


}
