package com.oneflow.tryskysdk.model.survey;

import com.google.gson.annotations.SerializedName;
import com.oneflow.tryskysdk.model.BaseModel;

public class RetakeSurvey extends BaseModel {
    @SerializedName("retake_input_value")
    private Long retake_input_value;
    @SerializedName("retake_select_value")
    private String retake_select_value;
    @SerializedName("_id")
    private String _id;

    public Long getRetake_input_value() {
        return retake_input_value;
    }

    public void setRetake_input_value(Long retake_input_value) {
        this.retake_input_value = retake_input_value;
    }

    public String getRetake_select_value() {
        return retake_select_value;
    }

    public void setRetake_select_value(String retake_select_value) {
        this.retake_select_value = retake_select_value;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
