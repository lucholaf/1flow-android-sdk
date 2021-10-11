package com.oneflow.tryskysdk.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.oneflow.tryskysdk.repositories.EventDBRepo;
import com.oneflow.tryskysdk.sdkdb.SDKDB;

import java.util.HashMap;

public class EventController implements MyResponseHandler{

    Context mContext;
    SDKDB sdkdb;
    String tag = this.getClass().getName();
    public EventController(Context mContext){
        this.mContext = mContext;
        sdkdb = SDKDB.getInstance(mContext);
    }
    public void storeEventsInDB(String eventName, HashMap<String, String> eventValue,int value){

        //String data = new Gson().toJson(eventValue);
        EventDBRepo.insertEvents(mContext,eventName,eventValue,value,this, Constants.ApiHitType.insertEventsInDB);

    }

    @Override
    public void onResponseReceived(Constants.ApiHitType hitType, Object obj, int reserve) {

        switch(hitType){
            case insertEventsInDB:
                Helper.v(tag,"Oneflow records inserted ["+((Integer)obj)+"]");
                break;
        }
    }
}
