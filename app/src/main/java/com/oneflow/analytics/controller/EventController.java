package com.oneflow.analytics.controller;

import android.content.Context;
import android.os.CountDownTimer;

import com.oneflow.analytics.repositories.EventDBRepo;
import com.oneflow.analytics.sdkdb.SDKDB;
import com.oneflow.analytics.utils.Constants;
import com.oneflow.analytics.utils.Helper;
import com.oneflow.analytics.utils.MyResponseHandler;

import java.util.HashMap;

public class EventController implements MyResponseHandler {

    Context mContext;
    SDKDB sdkdb;
    String tag = this.getClass().getName();

    private static EventController ec;

    public static EventController getInstance(Context mContext){
        if(ec==null){
            ec = new EventController(mContext);
        }
        return ec;
    }
    private EventController(Context mContext){
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
