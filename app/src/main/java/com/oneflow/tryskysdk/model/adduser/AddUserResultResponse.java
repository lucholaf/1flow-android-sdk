package com.oneflow.tryskysdk.model.adduser;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.oneflow.tryskysdk.sdkdb.convertes.DataConverterDevice;
import com.oneflow.tryskysdk.sdkdb.convertes.DataConverterLocation;

import java.util.ArrayList;

@Entity(tableName = "User")
public class AddUserResultResponse {

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


    @TypeConverters(DataConverterDevice.class)
    @ColumnInfo(name = "devices")
    @SerializedName("devices")
    private ArrayList<DeviceDetails> devices;

    @TypeConverters(DataConverterLocation.class)
    @ColumnInfo(name = "locations")
    @SerializedName("locations")
    private ArrayList<LocationDetails> locations;

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

    public ArrayList<DeviceDetails> getDevices() {
        return devices;
    }

    public void setDevices(ArrayList<DeviceDetails> devices) {
        this.devices = devices;
    }

    public ArrayList<LocationDetails> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<LocationDetails> locations) {
        this.locations = locations;
    }

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
