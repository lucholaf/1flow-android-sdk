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

package com.oneflow.analytics.utils;

public interface OFConstants {


    String DBNAME = "one_flow_db";
    String APPKEYSHP = "one_flow_config_key";
    String APPIDSHP = "one_flow_app_id_key";
    String USERDETAILSHP = "one_flow_user_detail_key";
    String USERUNIQUEIDSHP = "one_flow_user_unique_id_key";
    String LOGUSERREQUESTSHP = "one_flow_log_user_detail_key";
    String USERLOCATIONDETAILSHP = "one_flow_user_location_detail_key";
    String SESSIONDETAIL_IDSHP = "one_flow_session_detail_id_key";
    String SESSIONDETAIL_SYSTEM_IDSHP = "one_flow_session_detail_system_key";
    String SURVEYLISTSHP = "one_flow_survey_list_key";
    String SDKVERSIONSHP = "sdk_version_key";
    String BRACTION_EVENTS = "one_flow_submit_events";
    String BRACTION_SURVEYS = "one_flow_submit_surveys";
    String AUTOEVENT_FIRSTOPEN = "first_open";
    String AUTOEVENT_APPUPDATE = "one_flow_submit_surveys";
    String AUTOEVENT_SESSIONSTART = "session_starts";
    String SHP_SURVEYSTART = "survey_starts";
    String SHP_ONEFLOW_CONFTIMING = "conf_timing";
    String SHP_SURVEY_RUNNING = "survey_running";
    String SHP_SHOULD_SHOW_SURVEY = "should_show_survey";

    String os = "android";



    enum ApiHitType{
        Config,CreateUser, CreateSession, RecordLogs, fetchEventsFromDB, sendEventsToAPI, insertEventsInDB,
        deleteEventsFromDB, logUser, insertSurveyInDB, fetchSurveysFromDB, deleteSurveyFromDB, fetchLocation,
        fetchSurveysFromAPI,fetchEventsBeforSurveyFetched,fetchSubmittedSurvey,checkResurveyNSubmission
    }
    enum BRActionType{
        submitEvents,submitSurveys
    }

}
