package com.oneflow.analytics.model.survey;

import com.google.gson.annotations.SerializedName;
import com.oneflow.analytics.model.BaseModel;

public class SurveyStyle extends BaseModel {
    @SerializedName("display_mode")
    private String display_mode;
    @SerializedName("font")
    private String font;
    @SerializedName("_id")
    private String _id;
    @SerializedName("primary_color")
    private String primary_color;
    @SerializedName("corner_radius")
    private Integer corner_radius;

    public String getDisplay_mode() {
        return display_mode;
    }

    public void setDisplay_mode(String display_mode) {
        this.display_mode = display_mode;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getPrimary_color() {
        return primary_color;
    }

    public void setPrimary_color(String primary_color) {
        this.primary_color = primary_color;
    }

    public Integer getCorner_radius() {
        return corner_radius;
    }

    public void setCorner_radius(Integer corner_radius) {
        this.corner_radius = corner_radius;
    }
}
