package com.oneflow.analytics.sdkdb.convertes;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oneflow.analytics.model.adduser.DeviceDetails;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

//@ProvidedTypeConverter
public class DataConverterDevice implements Serializable {

    @TypeConverter
    public String toStringFromList(ArrayList<DeviceDetails> myList){

        if (myList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<DeviceDetails>>() {
        }.getType();
        String json = gson.toJson(myList, type);
        return json;
    }

    @TypeConverter
    public ArrayList<DeviceDetails> toListFromString(String listStr){
        if (listStr == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<DeviceDetails>>() {}.getType();
        ArrayList<DeviceDetails> productCategoriesList = gson.fromJson(listStr, type);
        return productCategoriesList;
    }
}
