/*
 *  Copyright 2021 1Flow, Inc.
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

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
    @SerializedName("rules")
    private OFRules rules;

    @SerializedName("media_embed_html")
    private String mediaEmbedHTML;

    public String getMediaEmbedHTML() {
        return mediaEmbedHTML;
    }

    public void setMediaEmbedHTML(String mediaEmbedHTML) {
        this.mediaEmbedHTML = mediaEmbedHTML;
    }

    public OFRules getRules() {
        return rules;
    }

    public void setRules(OFRules rules) {
        this.rules = rules;
    }

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
