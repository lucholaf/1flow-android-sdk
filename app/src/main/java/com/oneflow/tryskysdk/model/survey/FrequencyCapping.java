package com.oneflow.tryskysdk.model.survey;

import com.google.gson.annotations.SerializedName;
import com.oneflow.tryskysdk.model.BaseModel;

public class FrequencyCapping extends BaseModel {
    @SerializedName("enabled")
    private Boolean enabled;
    @SerializedName("frequency_capping_input_value")
    private String frequency_capping_input_value;
    @SerializedName("frequency_capping_select_value")
    private String frequency_capping_select_value;
    @SerializedName("time_per_user")
    private String time_per_user;
    @SerializedName("_id")
    private String _id;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getFrequency_capping_input_value() {
        return frequency_capping_input_value;
    }

    public void setFrequency_capping_input_value(String frequency_capping_input_value) {
        this.frequency_capping_input_value = frequency_capping_input_value;
    }

    public String getFrequency_capping_select_value() {
        return frequency_capping_select_value;
    }

    public void setFrequency_capping_select_value(String frequency_capping_select_value) {
        this.frequency_capping_select_value = frequency_capping_select_value;
    }

    public String getTime_per_user() {
        return time_per_user;
    }

    public void setTime_per_user(String time_per_user) {
        this.time_per_user = time_per_user;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
