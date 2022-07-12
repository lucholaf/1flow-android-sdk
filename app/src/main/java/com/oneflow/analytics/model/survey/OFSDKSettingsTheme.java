package com.oneflow.analytics.model.survey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OFSDKSettingsTheme implements Serializable {

    @SerializedName("widget_position")
    private String widgetPosition;
    @SerializedName("background_color")
    private String background_color = "#ffffff";
    @SerializedName("background_color_opacity")
    private Integer background_color_opacity;
    @SerializedName("text_color")
    private String text_color = "#000000";
    @SerializedName("text_color_opacity")
    private Integer text_color_opacity;
    @SerializedName("remove_watermark")
    private Boolean remove_watermark = false;
    @SerializedName("dark_overlay")
    private Boolean dark_overlay;
    @SerializedName("close_button")
    private Boolean close_button = false;
    @SerializedName("progress_bar")
    private Boolean progress_bar = true;

    public String getWidgetPosition() {
        return widgetPosition;
    }

    public void setWidgetPosition(String widgetPosition) {
        this.widgetPosition = widgetPosition;
    }

    public String getBackground_color() {
        return background_color;
    }

    public void setBackground_color(String background_color) {
        this.background_color = background_color;
    }

    public Integer getBackground_color_opacity() {
        return background_color_opacity;
    }

    public void setBackground_color_opacity(Integer background_color_opacity) {
        this.background_color_opacity = background_color_opacity;
    }

    public String getText_color() {
        return text_color;
    }

    public void setText_color(String text_color) {
        this.text_color = text_color;
    }

    public Integer getText_color_opacity() {
        return text_color_opacity;
    }

    public void setText_color_opacity(Integer text_color_opacity) {
        this.text_color_opacity = text_color_opacity;
    }

    public Boolean getRemove_watermark() {
        return remove_watermark;
    }

    public void setRemove_watermark(Boolean remove_watermark) {
        this.remove_watermark = remove_watermark;
    }

    public Boolean getDark_overlay() {
        return dark_overlay;
    }

    public void setDark_overlay(Boolean dark_overlay) {
        this.dark_overlay = dark_overlay;
    }

    public Boolean getClose_button() {
        return close_button;
    }

    public void setClose_button(Boolean close_button) {
        this.close_button = close_button;
    }

    public Boolean getProgress_bar() {
        return progress_bar;
    }

    public void setProgress_bar(Boolean progress_bar) {
        this.progress_bar = progress_bar;
    }
}
