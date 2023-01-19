package com.oneflow.analytics.model.adduser;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;

public class AddUserContext  implements Serializable {
    @SerializedName("app")
    private HashMap<String,String> app;
    @SerializedName("device")
    private HashMap<String,String> device;
    @SerializedName("library")
    private HashMap<String,String> library;
    @SerializedName("network")
    private HashMap<String,Object> network;
    @SerializedName("screen")
    private HashMap<String,String> screen;
    @SerializedName("os")
    private HashMap<String,String> os;

    public HashMap<String, String> getApp() {
        return app;
    }

    public void setApp(HashMap<String, String> app) {
        this.app = app;
    }

    public HashMap<String, String> getDevice() {
        return device;
    }

    public void setDevice(HashMap<String, String> device) {
        this.device = device;
    }

    public HashMap<String, String> getLibrary() {
        return library;
    }

    public void setLibrary(HashMap<String, String> library) {
        this.library = library;
    }

    public HashMap<String, Object> getNetwork() {
        return network;
    }

    public void setNetwork(HashMap<String, Object> network) {
        this.network = network;
    }

    public HashMap<String, String> getScreen() {
        return screen;
    }

    public void setScreen(HashMap<String, String> screen) {
        this.screen = screen;
    }

    public HashMap<String, String> getOs() {
        return os;
    }

    public void setOs(HashMap<String, String> os) {
        this.os = os;
    }
}
