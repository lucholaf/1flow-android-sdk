package com.oneflow.analytics.utils;

public interface Constants {

    String DBNAME = "one_flow_db";
    String APPKEYSHP = "one_flow_config_key";
    String APPIDSHP = "one_flow_app_id_key";
    String USERDETAILSHP = "one_flow_user_detail_key";
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
        fetchSurveysFromAPI,fetchEventsBeforSurveyFetched
    }
    enum BRActionType{
        submitEvents,submitSurveys
    }

}
