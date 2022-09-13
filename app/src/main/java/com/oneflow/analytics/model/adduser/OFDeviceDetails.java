/*
 *  Copyright 2021 1Flow, Inc.
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.oneflow.analytics.model.adduser;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.oneflow.analytics.model.OFBaseModel;

@Entity
public class OFDeviceDetails extends OFBaseModel {

    @ColumnInfo(name = "type")
    @SerializedName("type")
    private String type;

    @ColumnInfo(name = "carrier")
    @SerializedName("carrier")
    private String carrier;

    @ColumnInfo(name = "manufacturer")
    @SerializedName("manufacturer")
    private String manufacturer;

    @ColumnInfo(name = "model")
    @SerializedName("model")
    private String model;

    @ColumnInfo(name = "os_ver")
    @SerializedName("os_ver")
    private String os_ver;

    @ColumnInfo(name = "screen_width")
    @SerializedName("screen_width")
    private Integer screen_width;

    @ColumnInfo(name = "screen_height")
    @SerializedName("screen_height")
    private Integer screen_height;

    @ColumnInfo(name = "schema")
    @SerializedName("schema")
    private Integer schema;

    @PrimaryKey
    @ColumnInfo(name = "_id")
    @SerializedName("_id")
    private String _id;

    @ColumnInfo(name = "os")
    @SerializedName("os")
    private String os;

    @ColumnInfo(name = "unique_id")
    @SerializedName("unique_id")
    private String unique_id;

    @ColumnInfo(name = "device_id")
    @SerializedName("device_id")
    private String device_id;

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

    public Integer getScreen_width() {
        return screen_width;
    }

    public void setScreen_width(Integer screen_width) {
        this.screen_width = screen_width;
    }

    public Integer getScreen_height() {
        return screen_height;
    }

    public void setScreen_height(Integer screen_height) {
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
