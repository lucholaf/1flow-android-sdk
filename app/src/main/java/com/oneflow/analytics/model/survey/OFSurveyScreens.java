package com.oneflow.analytics.model.survey;

import com.google.gson.annotations.SerializedName;
import com.oneflow.analytics.model.OFBaseModel;

import java.util.ArrayList;

public class OFSurveyScreens extends OFBaseModel {
    @SerializedName("title")
    private String title;
    @SerializedName("message")
    private String message;
    @SerializedName("_id")
    private String _id;
    @SerializedName("input")
    private OFSurveyInputs input;
    @SerializedName("buttons")
    private ArrayList<OFSurveyButtons> buttons;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public OFSurveyInputs getInput() {
        return input;
    }

    public void setInput(OFSurveyInputs input) {
        this.input = input;
    }

    public ArrayList<OFSurveyButtons> getButtons() {
        return buttons;
    }

    public void setButtons(ArrayList<OFSurveyButtons> buttons) {
        this.buttons = buttons;
    }
}
