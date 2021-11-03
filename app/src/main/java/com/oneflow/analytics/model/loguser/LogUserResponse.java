package com.oneflow.analytics.model.loguser;

import com.google.gson.annotations.SerializedName;
import com.oneflow.analytics.model.BaseModel;

import java.util.ArrayList;

public class LogUserResponse extends BaseModel {

    @SerializedName("analytic_user_id")
    private String analytic_user_id;
    @SerializedName("created_on")
    private String created_on;
    @SerializedName("devices")
    private ArrayList<LogDevice> devices;
    @SerializedName("first_name")
    private String first_name;
    @SerializedName("last_name")
    private String last_name;
    @SerializedName("locations")
    private ArrayList<LogResponseLocation> locations;
    @SerializedName("parameters")
    private LogParameter parameters;
    @SerializedName("session")
    private LogSession session;
    @SerializedName("system_id")
    private String system_id;
    @SerializedName("updated_on")
    private String updated_on;

    public String getAnalytic_user_id() {
        return analytic_user_id;
    }

    public void setAnalytic_user_id(String analytic_user_id) {
        this.analytic_user_id = analytic_user_id;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public ArrayList<LogDevice> getDevices() {
        return devices;
    }

    public void setDevices(ArrayList<LogDevice> devices) {
        this.devices = devices;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public ArrayList<LogResponseLocation> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<LogResponseLocation> locations) {
        this.locations = locations;
    }

    public LogParameter getParameters() {
        return parameters;
    }

    public void setParameters(LogParameter parameters) {
        this.parameters = parameters;
    }

    public LogSession getSession() {
        return session;
    }

    public void setSession(LogSession session) {
        this.session = session;
    }

    public String getSystem_id() {
        return system_id;
    }

    public void setSystem_id(String system_id) {
        this.system_id = system_id;
    }

    public String getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(String updated_on) {
        this.updated_on = updated_on;
    }
}
