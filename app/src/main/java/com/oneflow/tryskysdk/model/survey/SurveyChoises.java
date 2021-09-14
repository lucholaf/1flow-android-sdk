package com.oneflow.tryskysdk.model.survey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SurveyChoises implements Serializable {
    @SerializedName("_id")
    private String id;
    @SerializedName("title")
    private String title;

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
