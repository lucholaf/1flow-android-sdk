package com.oneflow.tryskysdk.model.survey;

import com.google.gson.annotations.SerializedName;
import com.oneflow.tryskysdk.model.BaseModel;

public class SurveySettings extends BaseModel {

    @SerializedName("show_watermark")
    private Boolean show_watermark;
    @SerializedName("resurvey_option")
    private Boolean resurvey_option;
    @SerializedName("_id")
    private String _id;
    @SerializedName("frequency_capping")
    private FrequencyCapping frequency_capping;
    @SerializedName("retake_survey")
    private RetakeSurvey retake_survey;

    public Boolean getShow_watermark() {
        return show_watermark;
    }

    public void setShow_watermark(Boolean show_watermark) {
        this.show_watermark = show_watermark;
    }

    public Boolean getResurvey_option() {
        return resurvey_option;
    }

    public void setResurvey_option(Boolean resurvey_option) {
        this.resurvey_option = resurvey_option;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public FrequencyCapping getFrequency_capping() {
        return frequency_capping;
    }

    public void setFrequency_capping(FrequencyCapping frequency_capping) {
        this.frequency_capping = frequency_capping;
    }

    public RetakeSurvey getRetake_survey() {
        return retake_survey;
    }

    public void setRetake_survey(RetakeSurvey retake_survey) {
        this.retake_survey = retake_survey;
    }
}
