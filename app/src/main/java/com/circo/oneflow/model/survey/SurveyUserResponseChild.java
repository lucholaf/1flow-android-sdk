package com.circo.oneflow.model.survey;

import com.google.gson.annotations.SerializedName;

public class SurveyUserResponseChild {
    @SerializedName("screen_id")
    private String screen_id;
    @SerializedName("answer_index")
    private String answer_index;
    @SerializedName("answer_value")
    private String answer_value;

    public String getScreen_id() {
        return screen_id;
    }

    public void setScreen_id(String screen_id) {
        this.screen_id = screen_id;
    }

    public String getAnswer_index() {
        return answer_index;
    }

    public void setAnswer_index(String answer_index) {
        this.answer_index = answer_index;
    }

    public String getAnswer_value() {
        return answer_value;
    }

    public void setAnswer_value(String answer_value) {
        this.answer_value = answer_value;
    }
}
