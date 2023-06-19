package com.oneflow.analytics.model.survey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OFTimingOption implements Serializable {
    @SerializedName("type")
    String type;

    @SerializedName("value")
    Long value = 0l;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}
