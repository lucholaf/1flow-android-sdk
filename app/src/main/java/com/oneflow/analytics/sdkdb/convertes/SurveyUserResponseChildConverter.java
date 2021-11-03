package com.oneflow.analytics.sdkdb.convertes;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oneflow.analytics.model.survey.SurveyUserResponseChild;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;


//@ProvidedTypeConverter
public class SurveyUserResponseChildConverter implements Serializable {

    @TypeConverter
    public String toStringFromList(ArrayList<SurveyUserResponseChild> myList){

        if (myList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<SurveyUserResponseChild>>() {
        }.getType();
        String json = gson.toJson(myList, type);
        return json;
    }

    @TypeConverter
    public ArrayList<SurveyUserResponseChild> toListFromString(String listStr){
        if (listStr == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<SurveyUserResponseChild>>() {}.getType();
        ArrayList<SurveyUserResponseChild> productCategoriesList = gson.fromJson(listStr, type);
        return productCategoriesList;
    }
}
