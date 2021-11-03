package com.oneflow.analytics.sdkdb.convertes;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oneflow.analytics.model.adduser.LocationDetails;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;


//@ProvidedTypeConverter
public class DataConverterLocation implements Serializable {

    @TypeConverter
    public String toStringFromList(ArrayList<LocationDetails> myList){

        if (myList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<LocationDetails>>() {
        }.getType();
        String json = gson.toJson(myList, type);
        return json;
    }

    @TypeConverter
    public ArrayList<LocationDetails> toListFromString(String listStr){
        if (listStr == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<LocationDetails>>() {}.getType();
        ArrayList<LocationDetails> productCategoriesList = gson.fromJson(listStr, type);
        return productCategoriesList;
    }
}
