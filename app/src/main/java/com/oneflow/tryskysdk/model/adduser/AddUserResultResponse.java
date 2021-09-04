package com.oneflow.tryskysdk.model.adduser;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AddUserResultResponse {

    @SerializedName("analytic_user_id")
    private String analytic_user_id;
    @SerializedName("first_name")
    private String first_name;
    @SerializedName("last_name")
    private String last_name;
    @SerializedName("system_id")
    private String system_id;
    @SerializedName("devices")
    private ArrayList<DeviceDetails> devices;
    @SerializedName("locations")
    private ArrayList<LocationDetails> locations;
    @SerializedName("created_on")
    private Long created_on;
    @SerializedName("updated_on")
    private Long updated_on;

    public String getAnalytic_user_id() {
        return analytic_user_id;
    }

    public void setAnalytic_user_id(String analytic_user_id) {
        this.analytic_user_id = analytic_user_id;
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

    public String getSystem_id() {
        return system_id;
    }

    public void setSystem_id(String system_id) {
        this.system_id = system_id;
    }

    public ArrayList<DeviceDetails> getDevices() {
        return devices;
    }

    public void setDevices(ArrayList<DeviceDetails> devices) {
        this.devices = devices;
    }

    public ArrayList<LocationDetails> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<LocationDetails> locations) {
        this.locations = locations;
    }

    public Long getCreated_on() {
        return created_on;
    }

    public void setCreated_on(Long created_on) {
        this.created_on = created_on;
    }

    public Long getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(Long updated_on) {
        this.updated_on = updated_on;
    }
}
