package com.oneflow.analytics.model.survey;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OFFinishCallBack {
    @SerializedName("status") private String status;
    @SerializedName("survey_id") private String surveyId;
    @SerializedName("survey_name") private String surveyName;
    @SerializedName("trigger_name") private String triggerName;
    @SerializedName("screens") private ArrayList<OFSurveyFinishModel> screens;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public ArrayList<OFSurveyFinishModel> getScreens() {
        return screens;
    }

    public void setScreens(ArrayList<OFSurveyFinishModel> screens) {
        this.screens = screens;
    }
}
