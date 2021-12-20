package com.oneflow.analytics.sdkdb.convertes;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oneflow.analytics.model.adduser.OFDeviceDetails;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

//@ProvidedTypeConverter
public class OFEventRecordConverter implements Serializable {

    @TypeConverter
    public String toStringFromList(ArrayList<OFDeviceDetails> myList){

        if (myList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<OFDeviceDetails>>() {
        }.getType();
        String json = gson.toJson(myList, type);
        return json;
    }

    @TypeConverter
    public ArrayList<OFDeviceDetails> toListFromString(String listStr){
        if (listStr == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<OFDeviceDetails>>() {}.getType();
        ArrayList<OFDeviceDetails> productCategoriesList = gson.fromJson(listStr, type);
        return productCategoriesList;
    }
}
