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

public class OFSurveyStyle extends OFBaseModel {
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
