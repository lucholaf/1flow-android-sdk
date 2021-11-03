package com.oneflow.analytics.model.survey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SurveyChoises implements Serializable {
    @SerializedName("_id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("is_selected")
    private Boolean isSelected =false;

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
