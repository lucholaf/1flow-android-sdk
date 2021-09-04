package com.oneflow.tryskysdk.model.adduser;

import com.google.gson.annotations.SerializedName;
import com.oneflow.tryskysdk.model.BaseModel;

public class DeviceDetails extends BaseModel {

    @SerializedName("type")
    private String type;
    @SerializedName("carrier")
    private String carrier;
    @SerializedName("manufacturer")
    private String manufacturer;
    @SerializedName("model")
    private String model;
    @SerializedName("os_ver")
    private String os_ver;
    @SerializedName("screen_width")
    private String screen_width;
    @SerializedName("screen_height")
    private String screen_height;
    @SerializedName("schema")
    private Integer schema;
    @SerializedName("_id")
    private String _id;


    @SerializedName("os")
    private String os;
    @SerializedName("unique_id")
    private String unique_id;
    @SerializedName("device_id")
    private String device_id;
    @SerializedName("common_id")
    private String common_id;
    @SerializedName("__v")
    private Integer __v;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
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

    public String getOs_ver() {
        return os_ver;
    }

    public void setOs_ver(String os_ver) {
        this.os_ver = os_ver;
    }

    public String getScreen_width() {
        return screen_width;
    }

    public void setScreen_width(String screen_width) {
        this.screen_width = screen_width;
    }

    public String getScreen_height() {
        return screen_height;
    }

    public void setScreen_height(String screen_height) {
        this.screen_height = screen_height;
    }

    public Integer getSchema() {
        return schema;
    }

    public void setSchema(Integer schema) {
        this.schema = schema;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCommon_id() {
        return common_id;
    }

    public void setCommon_id(String common_id) {
        this.common_id = common_id;
    }

    public Integer get__v() {
        return __v;
    }

    public void set__v(Integer __v) {
        this.__v = __v;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }
}
