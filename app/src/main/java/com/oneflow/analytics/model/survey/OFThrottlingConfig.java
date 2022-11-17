package com.oneflow.analytics.model.survey;

import com.google.gson.annotations.SerializedName;
import com.oneflow.analytics.model.OFBaseModel;

public class OFThrottlingConfig extends OFBaseModel {

    @SerializedName("isThrottlingActivated")
    private boolean isActivated;
    @SerializedName("globalTime")
    private Long globalTime ;
    @SerializedName("activatedBySurveyID")
    private String activatedById;
    @SerializedName("throttlingActivatedTime")
    private Long activatedAt;

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    public Long getGlobalTime() {
        return globalTime;
    }

    public void setGlobalTime(Long globalTime) {
        this.globalTime = globalTime;
    }

    public String getActivatedById() {
        return activatedById;
    }

    public void setActivatedById(String activatedById) {
        this.activatedById = activatedById;
    }

    public Long getActivatedAt() {
        return activatedAt;
    }

    public void setActivatedAt(Long activatedAt) {
        this.activatedAt = activatedAt;
    }
}
