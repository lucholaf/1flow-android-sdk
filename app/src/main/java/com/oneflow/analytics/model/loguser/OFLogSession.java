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

package com.oneflow.analytics.model.loguser;

import com.google.gson.annotations.SerializedName;
import com.oneflow.analytics.model.OFBaseModel;
import com.oneflow.analytics.model.OFConnectivity;
import com.oneflow.analytics.model.adduser.OFLocationDetails;

public class OFLogSession extends OFBaseModel {

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
    private OFConnectivity connectivity;
    @SerializedName("device")
    private OFLogDevice device;
    @SerializedName("end_date")
    private String end_date;
    @SerializedName("library_name")
    private String library_name;
    @SerializedName("library_version")
    private String library_version;
    @SerializedName("location")
    private OFLocationDetails location;
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

    public OFConnectivity getConnectivity() {
        return connectivity;
    }

    public void setConnectivity(OFConnectivity connectivity) {
        this.connectivity = connectivity;
    }

    public OFLogDevice getDevice() {
        return device;
    }

    public void setDevice(OFLogDevice device) {
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

    public OFLocationDetails getLocation() {
        return location;
    }

    public void setLocation(OFLocationDetails location) {
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
