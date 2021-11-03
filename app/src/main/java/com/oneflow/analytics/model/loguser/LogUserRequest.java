package com.oneflow.analytics.model.loguser;

import com.google.gson.annotations.SerializedName;
import com.oneflow.analytics.model.BaseModel;

import java.util.HashMap;

public class LogUserRequest extends BaseModel {

    @SerializedName("parameters")
    private HashMap<String,String> parameters;
    @SerializedName("anonymous_user_id")
    private String anonymous_user_id;
    @SerializedName("system_id")
    private String system_id;
    @SerializedName("session_id")
    private String session_id;

    public HashMap<String,String> getParameters() {
        return parameters;
    }

    public void setParameters(HashMap<String,String> parameters) {
        this.parameters = parameters;
    }

    public String getAnonymous_user_id() {
        return anonymous_user_id;
    }

    public void setAnonymous_user_id(String anonymous_user_id) {
        this.anonymous_user_id = anonymous_user_id;
    }

    public String getSystem_id() {
        return system_id;
    }

    public void setSystem_id(String system_id) {
        this.system_id = system_id;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }
}
