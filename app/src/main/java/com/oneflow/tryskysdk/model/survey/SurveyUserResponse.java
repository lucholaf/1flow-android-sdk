package com.oneflow.tryskysdk.model.survey;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SurveyUserResponse {

    @SerializedName("analytic_user_id")
    private String analytic_user_id;
    @SerializedName("session_id")
    private String session_id;
    @SerializedName("survey_id")
    private String survey_id;
    @SerializedName("os")
    private String os;
    @SerializedName("answers")
    private ArrayList<SurveyUserResponseChild> answers;


    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getAnalytic_user_id() {
        return analytic_user_id;
    }

    public void setAnalytic_user_id(String analytic_user_id) {
        this.analytic_user_id = analytic_user_id;
    }

    public String getSurvey_id() {
        return survey_id;
    }

    public void setSurvey_id(String survey_id) {
        this.survey_id = survey_id;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public ArrayList<SurveyUserResponseChild> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<SurveyUserResponseChild> answers) {
        this.answers = answers;
    }
}
