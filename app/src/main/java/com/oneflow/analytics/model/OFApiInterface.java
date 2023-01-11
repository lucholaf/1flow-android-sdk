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

package com.oneflow.analytics.model;

import com.oneflow.analytics.model.adduser.OFAddUserRequest;
import com.oneflow.analytics.model.adduser.OFAddUserResultResponse;
import com.oneflow.analytics.model.createsession.OFCreateSessionRequest;
import com.oneflow.analytics.model.createsession.OFCreateSessionResponse;
import com.oneflow.analytics.model.events.OFEventAPIRequest;
import com.oneflow.analytics.model.location.OFLocationResponse;
import com.oneflow.analytics.model.loguser.OFLogUserRequest;
import com.oneflow.analytics.model.loguser.OFLogUserResponse;
import com.oneflow.analytics.model.survey.OFGetSurveyListResponse;
import com.oneflow.analytics.model.survey.OFSurveyUserInputKT;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface OFApiInterface {

    @POST("add-user")
    Call<OFGenericResponse<OFAddUserResultResponse>> addUserComman(@Header("one_flow_key") String headerKey, @Body OFAddUserRequest aur);//, @Url String url);

    @POST("add-session")
    Call<OFGenericResponse<OFCreateSessionResponse>> createSession(@Header("one_flow_key") String headerKey, @Body OFCreateSessionRequest aur);//, @Url String url);

/*
@Url String url,
 @Query("mode") String mode
*
* */
    @GET("surveys")
    Call<OFGenericResponse<ArrayList<OFGetSurveyListResponse>>> getSurvey(@Header("one_flow_key") String headerKey,

                                                                          @Query("platform") String platform,
                                                                          @Query("user_id") String userId,
                                                                          @Query("session_id") String sessionId,
                                                                          @Query("language_code") String languageCode,
                                                                          @Query("min_version") String minVersion




    );

    @GET("v1/2021-06-15/location")
    Call<OFLocationResponse> getLocation(@Header("one_flow_key") String headerKey);

    @POST("add-responses")
    Call<OFGenericResponse> submitSurveyUserResponse(@Header("one_flow_key") String headerKey, @Body OFSurveyUserInputKT aur);//, @Url String url);

    ///@POST("v1/2021-06-15/events/bulk")
    @POST("events")
    Call<OFGenericResponse> uploadAllUnSyncedEvents(@Header("one_flow_key") String headerKey, @Body OFEventAPIRequest ear);//, @Url String url);

    @GET("v1/2021-06-15/keys/{project_id}")
    Call<String> fetchProjectDetails(@Header("one_flow_key") String headerKey,@Path("project_id") String projectKey);

    @POST("log-user")
    Call<OFGenericResponse<OFLogUserResponse>> logUser(@Header("one_flow_key") String headerKey, @Body OFLogUserRequest request);//, @Url String url);


   /* @POST("v1/2021-06-15/json")
    Call<GenericResponse<AddUserResponse>> uploadFile(@Body AddUserRequest aur);*/


}
