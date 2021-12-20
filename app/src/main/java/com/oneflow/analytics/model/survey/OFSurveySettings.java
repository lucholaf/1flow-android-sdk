package com.oneflow.analytics.model.survey;

import com.google.gson.annotations.SerializedName;
import com.oneflow.analytics.model.OFBaseModel;

public class OFSurveySettings extends OFBaseModel {

    @SerializedName("show_watermark")
    private Boolean show_watermark;
    @SerializedName("resurvey_option")
    private Boolean resurvey_option;
    @SerializedName("_id")
    private String _id;
    @SerializedName("frequency_capping")
    private OFFrequencyCapping frequency_capping;
    @SerializedName("retake_survey")
    private OFRetakeSurvey retake_survey;

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

    public OFFrequencyCapping getFrequency_capping() {
        return frequency_capping;
    }

    public void setFrequency_capping(OFFrequencyCapping frequency_capping) {
        this.frequency_capping = frequency_capping;
    }

    public OFRetakeSurvey getRetake_survey() {
        return retake_survey;
    }

    public void setRetake_survey(OFRetakeSurvey retake_survey) {
        this.retake_survey = retake_survey;
    }
}
