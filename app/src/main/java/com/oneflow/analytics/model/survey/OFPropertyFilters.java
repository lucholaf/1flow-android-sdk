package com.oneflow.analytics.model.survey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class OFPropertyFilters implements Serializable {

    @SerializedName("operator")
    String operator;

    @SerializedName("filters")
    ArrayList<OFSurveyFilters> timingOption;

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public ArrayList<OFSurveyFilters> getTimingOption() {
        return timingOption;
    }

    public void setTimingOption(ArrayList<OFSurveyFilters> timingOption) {
        this.timingOption = timingOption;
    }
}
