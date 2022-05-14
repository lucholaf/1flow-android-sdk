package com.oneflow.analytics.model.survey;

import com.google.gson.annotations.SerializedName;

public class OFSurveyFinishChild {
    @SerializedName("answer_value")
    private String answerValue;
    @SerializedName("other_value")
    private String otherValue;

    public String getAnswerValue() {
        return answerValue;
    }

    public void setAnswerValue(String answerValue) {
        this.answerValue = answerValue;
    }

    public String getOtherValue() {
        return otherValue;
    }

    public void setOtherValue(String otherValue) {
        this.otherValue = otherValue;
    }
}
