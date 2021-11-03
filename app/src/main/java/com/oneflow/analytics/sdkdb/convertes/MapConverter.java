package com.oneflow.analytics.sdkdb.convertes;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.HashMap;

//@ProvidedTypeConverter
public class MapConverter implements Serializable {
    @TypeConverter
    public HashMap<String, String> stringToMap(String value)  {
        return new Gson().fromJson(value,  new TypeToken<HashMap<String, String>>(){}.getType());
    }

    @TypeConverter
    public String mapToString(HashMap<String, String> map) {
        if(map == null)
            return "";
        else
            return new Gson().toJson(map);
    }
}
