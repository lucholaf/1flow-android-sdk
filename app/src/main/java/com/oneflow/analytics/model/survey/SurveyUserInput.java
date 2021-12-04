package com.oneflow.analytics.model.survey;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.oneflow.analytics.sdkdb.convertes.SurveyUserResponseChildConverter;

import java.util.ArrayList;

@Entity(tableName = "SurveyUserInput")
public class SurveyUserInput {


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    @SerializedName("_id")
    private Integer _id;

    @ColumnInfo(name = "analytic_user_id")
    @SerializedName("analytic_user_id")
    private String analytic_user_id;

    @ColumnInfo(name = "session_id")
    @SerializedName("session_id")
    private String session_id;

    @ColumnInfo(name = "survey_id")
    @SerializedName("survey_id")
    private String survey_id;

    @ColumnInfo(name = "os")
    @SerializedName("os")
    private String os;

    @ColumnInfo(name = "mode")
    @SerializedName("mode")
    private String mode;

    @ColumnInfo(name = "trigger_event")
    @SerializedName("trigger_event")
    private String trigger_event;

    @TypeConverters(SurveyUserResponseChildConverter.class)
    @ColumnInfo(name = "answers")
    @SerializedName("answers")
    private ArrayList<SurveyUserResponseChild> answers;


    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getTrigger_event() {
        return trigger_event;
    }

    public void setTrigger_event(String trigger_event) {
        this.trigger_event = trigger_event;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

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
