package com.oneflow.analytics.model.survey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OFSDKSettingsTheme implements Serializable {
    @SerializedName("background_color")
    private String background_color = "#ffffff";
    @SerializedName("text_color")
    private String text_color = "#000000";
    @SerializedName("remove_watermark")
    private Boolean remove_watermark = false;

    public String getBackground_color() {
        return background_color;
    }

    public void setBackground_color(String background_color) {
        this.background_color = background_color;
    }

    public String getText_color() {
        return text_color;
    }

    public void setText_color(String text_color) {
        this.text_color = text_color;
    }

    public Boolean getRemove_watermark() {
        return remove_watermark;
    }

    public void setRemove_watermark(Boolean remove_watermark) {
        this.remove_watermark = remove_watermark;
    }
}
