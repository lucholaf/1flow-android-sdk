package com.oneflow.analytics.model.survey;

import com.google.gson.annotations.SerializedName;
import com.oneflow.analytics.model.BaseModel;

import java.util.ArrayList;

public class SurveyInputs extends BaseModel {

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
    private ArrayList<SurveyChoises> choices;
    @SerializedName("input_type")
    private String input_type;
    @SerializedName("ratings_list")
    private ArrayList<RatingsModel> ratingsList;
    @SerializedName("rating_min_text")
    private String rating_min_text;
    @SerializedName("rating_max_text")
    private String rating_max_text;

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

    public ArrayList<RatingsModel> getRatingsList() {
        return ratingsList;
    }

    public void setRatingsList(ArrayList<RatingsModel> ratingsList) {
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

    public ArrayList<SurveyChoises> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<SurveyChoises> choices) {
        this.choices = choices;
    }

    public String getInput_type() {
        return input_type;
    }

    public void setInput_type(String input_type) {
        this.input_type = input_type;
    }
}
