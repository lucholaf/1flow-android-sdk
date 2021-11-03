package com.oneflow.analytics.model.survey;

import com.google.gson.annotations.SerializedName;
import com.oneflow.analytics.model.BaseModel;

import java.util.ArrayList;

public class GetSurveyListResponse extends BaseModel {
    @SerializedName("_id")
    private String _id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("num_responses")
    private String num_responses;
    @SerializedName("end_date")
    private String end_date;
    @SerializedName("live")
    private Boolean live;
    @SerializedName("platforms")
    private ArrayList<String> platforms;
    @SerializedName("deleted")
    private Boolean deleted;
    @SerializedName("deleted_on")
    private String deleted_on;
    @SerializedName("schema_version")
    private Integer schema_version;
    @SerializedName("project_id")
    private String project_id;
    @SerializedName("style")
    private SurveyStyle style;
    @SerializedName("survey_settings")
    private SurveySettings surveySettings;
    @SerializedName("screens")
    private ArrayList<SurveyScreens> screens;
    @SerializedName("trigger_event_name")
    private String trigger_event_name ="empty";
    @SerializedName("color")
    private String themeColor = "#5D5FEF";
    @SerializedName("start_date")
    private Long start_date;
    @SerializedName("created_on")
    private Long created_on;
    @SerializedName("updated_on")
    private Long updated_on;
    @SerializedName("__v")
    private Integer __v;


    public SurveySettings getSurveySettings() {
        return surveySettings;
    }

    public void setSurveySettings(SurveySettings surveySettings) {
        this.surveySettings = surveySettings;
    }

    public String getThemeColor() {
        if(themeColor.startsWith("#")){
            return themeColor;
        }else{
            return "#"+themeColor;
        }

    }

    public void setThemeColor(String themeColor) {
        this.themeColor = themeColor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNum_responses() {
        return num_responses;
    }

    public void setNum_responses(String num_responses) {
        this.num_responses = num_responses;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public Boolean getLive() {
        return live;
    }

    public void setLive(Boolean live) {
        this.live = live;
    }

    public ArrayList<String> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(ArrayList<String> platforms) {
        this.platforms = platforms;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getDeleted_on() {
        return deleted_on;
    }

    public void setDeleted_on(String deleted_on) {
        this.deleted_on = deleted_on;
    }

    public Integer getSchema_version() {
        return schema_version;
    }

    public void setSchema_version(Integer schema_version) {
        this.schema_version = schema_version;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public SurveyStyle getStyle() {
        return style;
    }

    public void setStyle(SurveyStyle style) {
        this.style = style;
    }

    public ArrayList<SurveyScreens> getScreens() {
        return screens;
    }

    public void setScreens(ArrayList<SurveyScreens> screens) {
        this.screens = screens;
    }

    public String getTrigger_event_name() {
        return trigger_event_name;
    }

    public void setTrigger_event_name(String trigger_event_name) {
        this.trigger_event_name = trigger_event_name;
    }

    public Long getStart_date() {
        return start_date;
    }

    public void setStart_date(Long start_date) {
        this.start_date = start_date;
    }

    public Long getCreated_on() {
        return created_on;
    }

    public void setCreated_on(Long created_on) {
        this.created_on = created_on;
    }

    public Long getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(Long updated_on) {
        this.updated_on = updated_on;
    }

    public Integer get__v() {
        return __v;
    }

    public void set__v(Integer __v) {
        this.__v = __v;
    }
}
