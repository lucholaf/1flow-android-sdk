package com.oneflow.tryskysdk.model.adduser;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.oneflow.tryskysdk.model.BaseModel;

public class LocationDetails extends BaseModel {

    @ColumnInfo(name = "type")
    @SerializedName("type")
    private String type;

    @ColumnInfo(name = "schema")
    @SerializedName("schema")
    private Integer schema;

    @PrimaryKey
    @ColumnInfo(name = "_id")
    @SerializedName("_id")
    private String _id;

    @ColumnInfo(name = "city")
    @SerializedName("city")
    private String city;

    @ColumnInfo(name = "region")
    @SerializedName("region")
    private String region;

    @ColumnInfo(name = "country")
    @SerializedName("country")
    private String country;

    @ColumnInfo(name = "latitude")
    @SerializedName("latitude")
    private double latitude;

    @ColumnInfo(name = "longitude")
    @SerializedName("longitude")
    private double longitude;

    @ColumnInfo(name = "common_id")
    @SerializedName("common_id")
    private String common_id;

    @ColumnInfo(name = "__v")
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
