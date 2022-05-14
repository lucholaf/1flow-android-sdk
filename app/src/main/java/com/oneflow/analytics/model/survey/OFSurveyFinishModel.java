package com.oneflow.analytics.model.survey;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OFSurveyFinishModel {
    @SerializedName("screen_id")
    private String screenId;
    @SerializedName("question_title")
    private String questionTitle;
    @SerializedName("question_ans")
    private ArrayList<OFSurveyFinishChild> questionAns;
    @SerializedName("question_type")
    private String questionType;

    public String getScreenId() {
        return screenId;
    }

    public void setScreenId(String screenId) {
        this.screenId = screenId;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public ArrayList<OFSurveyFinishChild> getQuestionAns() {
        return questionAns;
    }

    public void setQuestionAns(ArrayList<OFSurveyFinishChild> questionAns) {
        this.questionAns = questionAns;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }
}
