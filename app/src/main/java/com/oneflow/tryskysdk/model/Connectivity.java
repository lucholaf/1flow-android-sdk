package com.oneflow.tryskysdk.model;

import com.google.gson.annotations.SerializedName;

public class Connectivity {
    @SerializedName("carrier")
    private Boolean carrier;
    @SerializedName("wifi")
    private Boolean wifi;
    @SerializedName("radio")
    private Boolean radio;

    public Boolean getCarrier() {
        return carrier;
    }

    public void setCarrier(Boolean carrier) {
        this.carrier = carrier;
    }

    public Boolean getWifi() {
        return wifi;
    }

    public void setWifi(Boolean wifi) {
        this.wifi = wifi;
    }

    public Boolean getRadio() {
        return radio;
    }

    public void setRadio(Boolean radio) {
        this.radio = radio;
    }
}
