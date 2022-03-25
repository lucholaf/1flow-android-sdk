package com.oneflow.analytics.model.survey;

import com.google.gson.annotations.SerializedName;
import com.oneflow.analytics.model.OFBaseModel;

import java.util.ArrayList;

public class OFRules extends OFBaseModel {
    @SerializedName("userProperty")
    private String userProperty;
    @SerializedName("dataLogic")
    private ArrayList<OFDataLogic> dataLogic;
    @SerializedName("_id")
    private String _id;

    public String getUserProperty() {
        return userProperty;
    }

    public void setUserProperty(String userProperty) {
        this.userProperty = userProperty;
    }

    public ArrayList<OFDataLogic> getDataLogic() {
        return dataLogic;
    }

    public void setDataLogic(ArrayList<OFDataLogic> dataLogic) {
        this.dataLogic = dataLogic;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
