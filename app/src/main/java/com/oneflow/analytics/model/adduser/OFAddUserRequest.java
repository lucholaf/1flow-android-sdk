package com.oneflow.analytics.model.adduser;

import com.google.gson.annotations.SerializedName;
import com.oneflow.analytics.model.OFBaseModel;

public class OFAddUserRequest extends OFBaseModel {
    @SerializedName("system_id")
    private String system_id;
    @SerializedName("device")
    private OFDeviceDetails OFDeviceDetails;
    @SerializedName("location_check")
    private Boolean locationCheck;
    @SerializedName("location")
    private OFLocationDetails OFLocationDetails;

    public Boolean getLocationCheck() {
        return locationCheck;
    }

    public void setLocationCheck(Boolean locationCheck) {
        this.locationCheck = locationCheck;
    }

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

    public OFLocationDetails getOFLocationDetails() {
        return OFLocationDetails;
    }

    public void setOFLocationDetails(OFLocationDetails OFLocationDetails) {
        this.OFLocationDetails = OFLocationDetails;
    }
}
