package com.oneflow.tryskysdk.utils;

public interface Constants {

    String APPKEYSHP = "one_flow_config_key";
    String APPIDSHP = "one_flow_app_id_key";
    String USERDETAILSHP = "one_flow_user_detail_key";
    String SESSIONDETAIL_IDSHP = "one_flow_session_detail_id_key";
    String SESSIONDETAIL_SYSTEM_IDSHP = "one_flow_session_detail_system_key";
    String SURVEYLISTSHP = "one_flow_survey_list_key";
    String os = "android";



    enum ApiHitType{
        Config,CreateUser,CreateSession,RecordLogs
    }

}
