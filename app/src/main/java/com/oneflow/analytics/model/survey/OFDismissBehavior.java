package com.oneflow.analytics.model.survey;

import com.google.gson.annotations.SerializedName;
import com.oneflow.analytics.model.OFBaseModel;

public class OFDismissBehavior extends OFBaseModel {
    @SerializedName("fades_away")
    private Boolean fadesAway;
    @SerializedName("delay_in_seconds")
    private Integer delayInSeconds;

    public Boolean getFadesAway() {
        return fadesAway;
    }

    public void setFadesAway(Boolean fadesAway) {
        this.fadesAway = fadesAway;
    }

    public Integer getDelayInSeconds() {
        return delayInSeconds;
    }

    public void setDelayInSeconds(Integer delayInSeconds) {
        this.delayInSeconds = delayInSeconds;
    }
}
