package com.oneflow.analytics.controller;

import android.content.Context;

import com.oneflow.analytics.repositories.OFEventDBRepo;
import com.oneflow.analytics.sdkdb.OFSDKDB;
import com.oneflow.analytics.utils.OFConstants;
import com.oneflow.analytics.utils.OFHelper;
import com.oneflow.analytics.utils.OFMyResponseHandler;

import java.util.HashMap;

public class OFEventController implements OFMyResponseHandler {

    Context mContext;
    OFSDKDB sdkdb;
    String tag = this.getClass().getName();

    private static OFEventController ec;

    public static OFEventController getInstance(Context mContext){
        if(ec==null){
            ec = new OFEventController(mContext);
        }
        return ec;
    }
    private OFEventController(Context mContext){
        this.mContext = mContext;
        sdkdb = OFSDKDB.getInstance(mContext);

    }

    public void storeEventsInDB(String eventName, HashMap<String, String> eventValue,int value){

        //String data = new Gson().toJson(eventValue);
        OFEventDBRepo.insertEvents(mContext,eventName,eventValue,value,this, OFConstants.ApiHitType.insertEventsInDB);

    }

    @Override
    public void onResponseReceived(OFConstants.ApiHitType hitType, Object obj, Long reserve) {

        switch(hitType){
            case insertEventsInDB:
                OFHelper.v(tag,"Oneflow records inserted ["+((Integer)obj)+"]");
                break;
        }
    }
}
