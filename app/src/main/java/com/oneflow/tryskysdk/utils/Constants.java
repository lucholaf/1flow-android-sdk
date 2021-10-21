package com.oneflow.tryskysdk.utils;

public interface Constants {

    String DBNAME = "one_flow_db";
    String APPKEYSHP = "one_flow_config_key";
    String APPIDSHP = "one_flow_app_id_key";
    String USERDETAILSHP = "one_flow_user_detail_key";
    String SESSIONDETAIL_IDSHP = "one_flow_session_detail_id_key";
    String SESSIONDETAIL_SYSTEM_IDSHP = "one_flow_session_detail_system_key";
    String SURVEYLISTSHP = "one_flow_survey_list_key";
    String BRACTION_EVENTS = "one_flow_submit_events";
    String BRACTION_SURVEYS = "one_flow_submit_surveys";


    String os = "android";



    enum ApiHitType{
        Config,CreateUser,CreateSession,RecordLogs,fetchEventsFromDB,sendEventsToAPI,insertEventsInDB,deleteEventsFromDB,
    }
    enum BRActionType{
        submitEvents,submitSurveys
    }

}
