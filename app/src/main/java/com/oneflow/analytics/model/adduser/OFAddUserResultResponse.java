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

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "User")
public class OFAddUserResultResponse {

    @PrimaryKey@NonNull
    @ColumnInfo(name = "analytic_user_id")
    @SerializedName("analytic_user_id")
    private String analytic_user_id;

    @ColumnInfo(name = "first_name")
    @SerializedName("first_name")
    private String first_name;

    @ColumnInfo(name = "last_name")
    @SerializedName("last_name")
    private String last_name;

    @ColumnInfo(name = "system_id")
    @SerializedName("system_id")
    private String system_id;


    //@TypeConverters(OFDataConverterDevice.class)
    /*@ColumnInfo(name = "devices")
    @SerializedName("devices")
    private OFDeviceDetails devices;*/
    //private ArrayList<OFDeviceDetails> devices;

    //@TypeConverters(OFDataConverterLocation.class)
   /* @ColumnInfo(name = "locations")
    @SerializedName("locations")
    private OFLocationDetails locations;*/
    //private ArrayList<OFLocationDetails> locations;

    @ColumnInfo(name = "created_on")
    @SerializedName("created_on")
    private Long created_on;

    @ColumnInfo(name = "updated_on")
    @SerializedName("updated_on")
    private Long updated_on;

    public String getAnalytic_user_id() {
        return analytic_user_id;
    }

    public void setAnalytic_user_id(String analytic_user_id) {
        this.analytic_user_id = analytic_user_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getSystem_id() {
        return system_id;
    }

    public void setSystem_id(String system_id) {
        this.system_id = system_id;
    }

    /*public ArrayList<DeviceDetails> getDevicesList() {
        ArrayList<DeviceDetails> devicesList = null;
        try {
            devicesList = Helper.fromJsonToArrayList(devices, DeviceDetails.class);
        }catch(Exception ex){

        }
        return devicesList;
    }*/

    /*public String getDevices() {
        return devices;
    }*/

    /*public String getLocations() {
        return locations;
    }

    public void setDevices(String devices) {
        this.devices = devices;
    }*/

    /*public ArrayList<LocationDetails> getLocationsList() {

        ArrayList<LocationDetails> locationsList = null;
        try{
            locationsList = Helper.fromJsonToArrayList(locations,LocationDetails.class);
        }catch(Exception ex){

        }
        return locationsList;
    }

    public void setLocations(String locations) {
        this.locations = locations;
    }*/

    /*public ArrayList<OFDeviceDetails> getDevices() {
        return devices;
    }

    public void setDevices(ArrayList<OFDeviceDetails> devices) {
        this.devices = devices;
    }

    public ArrayList<OFLocationDetails> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<OFLocationDetails> locations) {
        this.locations = locations;
    }*/
   /* public OFDeviceDetails getDevices() {
        return devices;
    }

    public void setDevices(OFDeviceDetails devices) {
        this.devices = devices;
    }

    public OFLocationDetails getLocations() {
        return locations;
    }

    public void setLocations(OFLocationDetails locations) {
        this.locations = locations;
    }*/

    public Long getCreated_on() {
        return created_on;
    }

    public void setCreated_on(Long created_on) {
        this.created_on = created_on;
    }

    public Long getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(Long updated_on) {
        this.updated_on = updated_on;
    }
}
