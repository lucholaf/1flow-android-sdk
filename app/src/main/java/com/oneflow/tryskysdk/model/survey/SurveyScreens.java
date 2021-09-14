package com.oneflow.tryskysdk.model.survey;

import com.google.gson.annotations.SerializedName;
import com.oneflow.tryskysdk.model.BaseModel;

import java.util.ArrayList;

public class SurveyScreens extends BaseModel {
    @SerializedName("title")
    private String title;
    @SerializedName("message")
    private String message;
    @SerializedName("_id")
    private String _id;
    @SerializedName("input")
    private SurveyInputs input;
    @SerializedName("buttons")
    private ArrayList<SurveyButtons> buttons;

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

    public SurveyInputs getInput() {
        return input;
    }

    public void setInput(SurveyInputs input) {
        this.input = input;
    }

    public ArrayList<SurveyButtons> getButtons() {
        return buttons;
    }

    public void setButtons(ArrayList<SurveyButtons> buttons) {
        this.buttons = buttons;
    }
}
