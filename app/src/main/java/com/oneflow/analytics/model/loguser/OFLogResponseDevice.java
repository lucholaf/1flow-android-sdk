package com.oneflow.analytics.model.loguser;

import com.google.gson.annotations.SerializedName;

public class OFLogResponseDevice {

    @SerializedName("__v")
    private Integer __v;
    @SerializedName("_id")
    private String _id;
    @SerializedName("carrier")
    private String carrier;
    @SerializedName("common_id")
    private String common_id;
    @SerializedName("deleted")
    private Integer deleted;
    @SerializedName("deleted_on")
    private String deleted_on;
    @SerializedName("device_id")
    private String device_id;
    @SerializedName("manufacturer")
    private String manufacturer;
    @SerializedName("model")
    private String model;
    @SerializedName("os")
    private String os;
    @SerializedName("os_ver")
    private String os_ver;
    @SerializedName("schema_version")
    private Integer schema_version;
    @SerializedName("screen_height")
    private String screen_height;
    @SerializedName("screen_width")
    private String screen_width;
    @SerializedName("type")
    private String type;
    @SerializedName("unique_id")
    private String unique_id;

    public Integer get__v() {
        return __v;
    }

    public void set__v(Integer __v) {
        this.__v = __v;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getCommon_id() {
        return common_id;
    }

    public void setCommon_id(String common_id) {
        this.common_id = common_id;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getDeleted_on() {
        return deleted_on;
    }

    public void setDeleted_on(String deleted_on) {
        this.deleted_on = deleted_on;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOs_ver() {
        return os_ver;
    }

    public void setOs_ver(String os_ver) {
        this.os_ver = os_ver;
    }

    public Integer getSchema_version() {
        return schema_version;
    }

    public void setSchema_version(Integer schema_version) {
        this.schema_version = schema_version;
    }

    public String getScreen_height() {
        return screen_height;
    }

    public void setScreen_height(String screen_height) {
        this.screen_height = screen_height;
    }

    public String getScreen_width() {
        return screen_width;
    }

    public void setScreen_width(String screen_width) {
        this.screen_width = screen_width;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }
}
