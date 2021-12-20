package com.oneflow.analytics.model.survey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OFRatingsModel implements Serializable {
    @SerializedName("id")
    private Integer id;
    @SerializedName("isSelected")
    private Boolean isSelected;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
