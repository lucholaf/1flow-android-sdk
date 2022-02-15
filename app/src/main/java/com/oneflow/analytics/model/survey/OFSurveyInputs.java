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

public class OFSurveyInputs extends OFBaseModel {

    @SerializedName("min_val")
    private Integer min_val;
    @SerializedName("max_val")
    private String max_val;
    @SerializedName("emoji")
    private Boolean emoji = false;
    @SerializedName("stars")
    private Boolean stars = false;
    @SerializedName("emojis")
    private String[] emojis;
    @SerializedName("star_fill_color")
    private String star_fill_color;
    @SerializedName("min_chars")
    private Integer min_chars = 0;
    @SerializedName("max_chars")
    private Integer max_chars;
    @SerializedName("_id")
    private String _id;
    @SerializedName("choices")
    private ArrayList<OFSurveyChoises> choices;
    @SerializedName("input_type")
    private String input_type;
    @SerializedName("ratings_list")
    private ArrayList<OFRatingsModel> ratingsList;
    @SerializedName("rating_min_text")
    private String rating_min_text;
    @SerializedName("rating_max_text")
    private String rating_max_text;
    @SerializedName("other_option_id")
    private String otherOption = "";//""dfe27f6a4b9f0e10acbedf7a";//"63ded0458b225257f3b19362";

    public String getOtherOption() {
        return otherOption;
    }

    public void setOtherOption(String otherOption) {
        this.otherOption = otherOption;
    }

    public String getRating_min_text() {
        return rating_min_text;
    }

    public void setRating_min_text(String rating_min_text) {
        this.rating_min_text = rating_min_text;
    }

    public String getRating_max_text() {
        return rating_max_text;
    }

    public void setRating_max_text(String rating_max_text) {
        this.rating_max_text = rating_max_text;
    }

    public ArrayList<OFRatingsModel> getRatingsList() {
        return ratingsList;
    }

    public void setRatingsList(ArrayList<OFRatingsModel> ratingsList) {
        this.ratingsList = ratingsList;
    }

    public Integer getMin_val() {
        return min_val;
    }

    public void setMin_val(Integer min_val) {
        this.min_val = min_val;
    }

    public Integer getMax_val() {
        if (max_val != null) {
            return Integer.parseInt(max_val);
        } else {
            return 0;
        }

    }

    public void setMax_val(String max_val) {
        this.max_val = max_val;
    }

    public Boolean getEmoji() {
        return emoji;
    }

    public void setEmoji(Boolean emoji) {
        this.emoji = emoji;
    }

    public Boolean getStars() {
        return stars;
    }

    public void setStars(Boolean stars) {
        this.stars = stars;
    }

    public String[] getEmojis() {
        return emojis;
    }

    public void setEmojis(String[] emojis) {
        this.emojis = emojis;
    }

    public String getStar_fill_color() {
        return star_fill_color;
    }

    public void setStar_fill_color(String star_fill_color) {
        this.star_fill_color = star_fill_color;
    }

    public Integer getMin_chars() {
        return min_chars;
    }

    public void setMin_chars(Integer min_chars) {
        this.min_chars = min_chars;
    }

    public Integer getMax_chars() {
        return max_chars;
    }

    public void setMax_chars(Integer max_chars) {
        this.max_chars = max_chars;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public ArrayList<OFSurveyChoises> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<OFSurveyChoises> choices) {
        this.choices = choices;
    }

    public String getInput_type() {
        return input_type;
    }

    public void setInput_type(String input_type) {
        this.input_type = input_type;
    }
}
