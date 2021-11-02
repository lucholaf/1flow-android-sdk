package com.circo.oneflow.controller;

import android.content.Context;
import android.os.CountDownTimer;

import com.circo.oneflow.repositories.EventDBRepo;
import com.circo.oneflow.sdkdb.SDKDB;
import com.circo.oneflow.utils.Constants;
import com.circo.oneflow.utils.Helper;
import com.circo.oneflow.utils.MyResponseHandler;

import java.util.HashMap;

public class EventController implements MyResponseHandler {

    Context mContext;
    SDKDB sdkdb;
    String tag = this.getClass().getName();

    static EventController ec;
    CountDownTimer cdt = new CountDownTimer(30000,1000) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {

        }
    };
    public static EventController getInstance(Context mContext){
        if(ec==null){
            ec = new EventController(mContext);
        }
        return ec;
    }
    private EventController(Context mContext){
        this.mContext = mContext;
        sdkdb = SDKDB.getInstance(mContext);
        cdt.start();
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
