package com.oneflow.analytics.model;

import com.google.gson.annotations.SerializedName;

public class Connectivity extends BaseModel {
    @SerializedName("carrier")
    private String carrier;
    @SerializedName("wifi")
    private Boolean wifi;
    @SerializedName("radio")
    private String radio;

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public Boolean getWifi() {
        return wifi;
    }

    public void setWifi(Boolean wifi) {
        this.wifi = wifi;
    }

    public String getRadio() {
        return radio;
    }

    public void setRadio(String radio) {
        this.radio = radio;
    }
}
