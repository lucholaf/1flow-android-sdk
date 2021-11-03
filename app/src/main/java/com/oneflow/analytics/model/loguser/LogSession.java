package com.oneflow.analytics.model.loguser;

import com.google.gson.annotations.SerializedName;
import com.oneflow.analytics.model.BaseModel;
import com.oneflow.analytics.model.Connectivity;
import com.oneflow.analytics.model.adduser.LocationDetails;

public class LogSession extends BaseModel {

    @SerializedName("_id")
    private String _id;
    @SerializedName("api_endpoint")
    private String api_endpoint;
    @SerializedName("api_version")
    private String api_version;
    @SerializedName("app_build_number")
    private String app_build_number;
    @SerializedName("app_version")
    private String app_version;
    @SerializedName("connectivity")
    private Connectivity connectivity;
    @SerializedName("device")
    private LogDevice device;
    @SerializedName("end_date")
    private String end_date;
    @SerializedName("library_name")
    private String library_name;
    @SerializedName("library_version")
    private String library_version;
    @SerializedName("location")
    private LocationDetails location;
    @SerializedName("location_check")
    private Integer location_check;
    @SerializedName("start_date")
    private String start_date;
    @SerializedName("system_id")
    private String system_id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public String getApp_build_number() {
        return app_build_number;
    }

    public void setApp_build_number(String app_build_number) {
        this.app_build_number = app_build_number;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public Connectivity getConnectivity() {
        return connectivity;
    }

    public void setConnectivity(Connectivity connectivity) {
        this.connectivity = connectivity;
    }

    public LogDevice getDevice() {
        return device;
    }

    public void setDevice(LogDevice device) {
        this.device = device;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
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

    public LocationDetails getLocation() {
        return location;
    }

    public void setLocation(LocationDetails location) {
        this.location = location;
    }

    public Integer getLocation_check() {
        return location_check;
    }

    public void setLocation_check(Integer location_check) {
        this.location_check = location_check;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getSystem_id() {
        return system_id;
    }

    public void setSystem_id(String system_id) {
        this.system_id = system_id;
    }
}
