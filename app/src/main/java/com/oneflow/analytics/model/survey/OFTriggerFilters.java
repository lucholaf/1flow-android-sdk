package com.oneflow.analytics.model.survey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OFTriggerFilters implements Serializable {

    @SerializedName("type")
    String type;

    @SerializedName("timingOption")
    com.oneflow.analytics.model.survey.OFTimingOption OFTimingOption;

    @SerializedName("_id")
    String _id;

    @SerializedName("field")
    String field;

    @SerializedName("property_filters")
    OFPropertyFilters propertyFilters;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public com.oneflow.analytics.model.survey.OFTimingOption getOFTimingOption() {
        return OFTimingOption;
    }

    public void setOFTimingOption(com.oneflow.analytics.model.survey.OFTimingOption OFTimingOption) {
        this.OFTimingOption = OFTimingOption;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public OFPropertyFilters getPropertyFilters() {
        return propertyFilters;
    }

    public void setPropertyFilters(OFPropertyFilters propertyFilters) {
        this.propertyFilters = propertyFilters;
    }
}
