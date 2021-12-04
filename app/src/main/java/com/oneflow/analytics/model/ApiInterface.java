package com.oneflow.analytics.model;

import com.oneflow.analytics.model.adduser.AddUserRequest;
import com.oneflow.analytics.model.adduser.AddUserResultResponse;
import com.oneflow.analytics.model.createsession.CreateSessionRequest;
import com.oneflow.analytics.model.createsession.CreateSessionResponse;
import com.oneflow.analytics.model.events.EventAPIRequest;
import com.oneflow.analytics.model.events.EventSubmitResponse;
import com.oneflow.analytics.model.location.LocationResponse;
import com.oneflow.analytics.model.loguser.LogUserRequest;
import com.oneflow.analytics.model.loguser.LogUserResponse;
import com.oneflow.analytics.model.survey.SurveyUserInput;
import com.oneflow.analytics.model.survey.GetSurveyListResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {

    @POST//("v1/2021-06-15/project_users")
    Call<GenericResponse<AddUserResultResponse>> addUserComman(@Header("one_flow_key") String headerKey,@Body AddUserRequest aur, @Url String url);

    @POST//("v1/2021-06-15/sessions")
    Call<GenericResponse<CreateSessionResponse>> createSession(@Header("one_flow_key") String headerKey,@Body CreateSessionRequest aur, @Url String url);

    @GET//("v1/2021-06-15/survey")
    Call<GenericResponse<ArrayList<GetSurveyListResponse>>> getSurvey(@Header("one_flow_key") String headerKey,@Url String url, @Query("platform") String platform, @Query("mode") String mode);

    @GET("v1/2021-06-15/location")
    Call<LocationResponse> getLocation(@Header("one_flow_key") String headerKey);

    @POST//("v1/2021-06-15/survey-response")
    Call<GenericResponse<String>> submitSurveyUserResponse(@Header("one_flow_key") String headerKey,@Body SurveyUserInput aur, @Url String url);

    ///@POST("v1/2021-06-15/events/bulk")
    @POST  //("https://us-west-2.aws.webhooks.mongodb-realm.com/api/client/v2.0/app/1flow-wslxs/service/events-bulk/incoming_webhook/insert-events")
    Call<GenericResponse<EventSubmitResponse>> uploadAllUnSyncedEvents(@Header("one_flow_key") String headerKey, @Body EventAPIRequest ear, @Url String url);

    @GET("v1/2021-06-15/keys/{project_id}")
    Call<String> fetchProjectDetails(@Header("one_flow_key") String headerKey,@Path("project_id") String projectKey);

    @POST//("v1/2021-06-15/project_users/log_user")
    Call<GenericResponse<LogUserResponse>> logUser(@Header("one_flow_key") String headerKey, @Body LogUserRequest request, @Url String url);



   /* @POST("v1/2021-06-15/json")
    Call<GenericResponse<AddUserResponse>> uploadFile(@Body AddUserRequest aur);*/


}
