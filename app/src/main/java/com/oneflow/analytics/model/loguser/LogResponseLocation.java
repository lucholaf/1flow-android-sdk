package com.oneflow.analytics.model.loguser;

import com.google.gson.annotations.SerializedName;
import com.oneflow.analytics.model.BaseModel;

public class LogResponseLocation extends BaseModel {

    @SerializedName("__v")
    private Integer __v;
    @SerializedName("_id")
    private String _id;
    @SerializedName("city")
    private String city;
    @SerializedName("common_id")
    private String common_id;
    @SerializedName("country")
    private String country;
    @SerializedName("deleted")
    private Boolean deleted;
    @SerializedName("deleted_on")
    private String deleted_on;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;
    @SerializedName("region")
    private String region;
    @SerializedName("schema_version")
    private Integer schema_version;
    @SerializedName("type")
    private String type;

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCommon_id() {
        return common_id;
    }

    public void setCommon_id(String common_id) {
        this.common_id = common_id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getDeleted_on() {
        return deleted_on;
    }

    public void setDeleted_on(String deleted_on) {
        this.deleted_on = deleted_on;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Integer getSchema_version() {
        return schema_version;
    }

    public void setSchema_version(Integer schema_version) {
        this.schema_version = schema_version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
