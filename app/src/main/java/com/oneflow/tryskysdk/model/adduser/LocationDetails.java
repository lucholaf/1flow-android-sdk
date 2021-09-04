package com.oneflow.tryskysdk.model.adduser;

import com.google.gson.annotations.SerializedName;
import com.oneflow.tryskysdk.model.BaseModel;

public class LocationDetails extends BaseModel {

    @SerializedName("type")
    private String type;
    @SerializedName("schema")
    private Integer schema;
    @SerializedName("_id")
    private String _id;
    @SerializedName("city")
    private String city;
    @SerializedName("region")
    private String region;
    @SerializedName("country")
    private String country;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
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

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
