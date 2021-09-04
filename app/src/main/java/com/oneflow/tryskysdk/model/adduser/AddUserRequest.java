package com.oneflow.tryskysdk.model.adduser;

import com.google.gson.annotations.SerializedName;
import com.oneflow.tryskysdk.model.BaseModel;

public class AddUserRequest extends BaseModel {
    @SerializedName("system_id")
    private String system_id;
    @SerializedName("device")
    private DeviceDetails deviceDetails;
    @SerializedName("location")
    private LocationDetails locationDetails;

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
