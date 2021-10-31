package com.circo.oneflow.model.adduser;

import com.google.gson.annotations.SerializedName;
import com.circo.oneflow.model.BaseModel;

public class AddUserRequest extends BaseModel {
    @SerializedName("system_id")
    private String system_id;
    @SerializedName("device")
    private DeviceDetails deviceDetails;
    @SerializedName("location_check")
    private Boolean locationCheck;
    @SerializedName("location")
    private LocationDetails locationDetails;

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

    public DeviceDetails getDeviceDetails() {
        return deviceDetails;
    }

    public void setDeviceDetails(DeviceDetails deviceDetails) {
        this.deviceDetails = deviceDetails;
    }

    public LocationDetails getLocationDetails() {
        return locationDetails;
    }

    public void setLocationDetails(LocationDetails locationDetails) {
        this.locationDetails = locationDetails;
    }
}
