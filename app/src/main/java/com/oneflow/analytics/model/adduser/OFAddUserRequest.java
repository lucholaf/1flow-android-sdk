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

package com.oneflow.analytics.model.adduser;

import com.google.gson.annotations.SerializedName;
import com.oneflow.analytics.model.OFBaseModel;

public class OFAddUserRequest extends OFBaseModel {
    @SerializedName("system_id")
    private String system_id;
    @SerializedName("language")
    private String language;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("device")
    private OFDeviceDetails OFDeviceDetails;
   /* @SerializedName("location_check")
    private Boolean locationCheck;
    @SerializedName("location")
    private OFLocationDetails OFLocationDetails;*/


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    /*public Boolean getLocationCheck() {
        return locationCheck;
    }

    public void setLocationCheck(Boolean locationCheck) {
        this.locationCheck = locationCheck;
    }*/

    public String getSystem_id() {
        return system_id;
    }

    public void setSystem_id(String system_id) {
        this.system_id = system_id;
    }

    public OFDeviceDetails getOFDeviceDetails() {
        return OFDeviceDetails;
    }

    public void setOFDeviceDetails(OFDeviceDetails OFDeviceDetails) {
        this.OFDeviceDetails = OFDeviceDetails;
    }

    /*public OFLocationDetails getOFLocationDetails() {
        return OFLocationDetails;
    }

    public void setOFLocationDetails(OFLocationDetails OFLocationDetails) {
        this.OFLocationDetails = OFLocationDetails;
    }*/
}
