package com.oneflow.analytics.controller;

import android.content.Context;

import com.oneflow.analytics.repositories.Survey;
import com.oneflow.analytics.utils.Helper;

public class SurveyController {

    Context mContext;
    static SurveyController sc;
    private SurveyController(Context context){
        this.mContext = context;
        getSurveyFromAPI();
    }
    public static SurveyController getInstance(Context context)
    {
        Helper.v("SurveyController","OneFlow reached SurveyController");
        if(sc == null){
            sc = new SurveyController(context);
        }
        return sc;
    }
    public void getSurveyFromAPI(){
        Helper.v("SurveyController","OneFlow reached SurveyController 0");
        Survey.getSurvey(mContext);
    }
    public void fetchSurveyFromList(){

    }
    public void submitFinishedSurveyToAPI(){

    }
}