package com.circo.oneflow.model.createsession;

import com.google.gson.annotations.SerializedName;
import com.circo.oneflow.model.Connectivity;
import com.circo.oneflow.model.adduser.DeviceDetails;
import com.circo.oneflow.model.adduser.LocationDetails;

import java.io.Serializable;

public class CreateSessionRequest implements Serializable {

    @SerializedName("analytic_user_id")
    private String analytic_user_id;
    @SerializedName("system_id")
    private String system_id;
    @SerializedName("device")
    private DeviceDetails device;
    @SerializedName("location_check")
    private boolean location_check;
    @SerializedName("location")
    private LocationDetails location;
    @SerializedName("connectivity")
    private Connectivity connectivity;
    @SerializedName("app_version")
    private String app_version;
    @SerializedName("app_build_number")
    private String app_build_number;
    @SerializedName("library_name")
    private String library_name;
    @SerializedName("library_version")
    private String library_version;
    @SerializedName("api_endpoint")
    private String api_endpoint;
    @SerializedName("api_version")
    private String api_version;

    public String getAnalytic_user_id() {
        return analytic_user_id;
    }

    public void setAnalytic_user_id(String analytic_user_id) {
        this.analytic_user_id = analytic_user_id;
    }

    public String getSystem_id() {
        return system_id;
    }

    public void setSystem_id(String system_id) {
        this.system_id = system_id;
    }

    public DeviceDetails getDevice() {
        return device;
    }

    public void setDevice(DeviceDetails device) {
        this.device = device;
    }

    public boolean isLocation_check() {
        return location_check;
    }

    public void setLocation_check(boolean location_check) {
        this.location_check = location_check;
    }

    public LocationDetails getLocation() {
        return location;
    }

    public void setLocation(LocationDetails location) {
        this.location = location;
    }

    public Connectivity getConnectivity() {
        return connectivity;
    }

    public void setConnectivity(Connectivity connectivity) {
        this.connectivity = connectivity;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getApp_build_number() {
        return app_build_number;
    }

    public void setApp_build_number(String app_build_number) {
        this.app_build_number = app_build_number;
    }

    public String getLibrary_name() {
        return library_name;
    }

    public void setLibrary_name(String library_name) {
        this.library_name = library_name;
    }

    public String getLibrary_version() {
        return library_version;
    }

    public void setLibrary_version(String library_version) {
        this.library_version = library_version;
    }

    public String getApi_endpoint() {
        return api_endpoint;
    }

    public void setApi_endpoint(String api_endpoint) {
        this.api_endpoint = api_endpoint;
    }

    public String getApi_version() {
        return api_version;
    }

    public void setApi_version(String api_version) {
        this.api_version = api_version;
    }
}
