package com.oneflow.analytics.utils;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;

import com.oneflow.analytics.model.events.OFEventAPIRequest;
import com.oneflow.analytics.model.events.OFRecordEventsTab;
import com.oneflow.analytics.model.events.OFRecordEventsTabToAPI;
import com.oneflow.analytics.repositories.OFEventAPIRepo;
import com.oneflow.analytics.repositories.OFEventDBRepo;
import com.oneflow.analytics.sdkdb.OFOneFlowSHP;

import java.util.ArrayList;

public class OFMyCountDownTimer extends CountDownTimer implements OFMyResponseHandler {
    Context mContext;
    public OFMyCountDownTimer(Context context, Long duration, Long interval){
        super(duration,interval);
        this.mContext = context;
        OFHelper.v("MyCountDownTimer","OneFlow Constructor called");
    }
    @Override
    public void onTick(long millisUntilFinished) {
        OFHelper.v("MyCountDownTimer","OneFlow tick called");
        if (OFHelper.isConnected(mContext)) {
            OFEventDBRepo.fetchEvents(mContext, this, OFConstants.ApiHitType.fetchEventsFromDB);
        }
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onResponseReceived(OFConstants.ApiHitType hitType, Object obj, Long reserve) {
        OFHelper.v("OneFlow", "OneFlow onReceived type[" + hitType + "]");
        switch (hitType) {

            case fetchEventsFromDB:

                OFHelper.v("FeedbackController", "OneFlow fetchEventsFromDB came back");


                ArrayList<OFRecordEventsTab> list = (ArrayList<OFRecordEventsTab>) obj;
                OFHelper.v("FeedbackController", "OneFlow fetchEventsFromDB list received size[" + list.size() + "]");
                //Preparing list to send api
                if(list.size()>0) {
                    Integer[] ids = new Integer[list.size()];
                    int i = 0;
                    ArrayList<OFRecordEventsTabToAPI> retListToAPI = new ArrayList<>();
                    OFRecordEventsTabToAPI retMain;
                    for (OFRecordEventsTab ret : list) {
                        retMain = new OFRecordEventsTabToAPI();
                        retMain.setEventName(ret.getEventName());
                        retMain.setTime(ret.getTime());

                        //retMain.setDataMap(ret.getDataMap());
                        retListToAPI.add(retMain);

                        ids[i++] = ret.getId();
                    }



                        if (!new OFOneFlowSHP(mContext).getStringValue(OFConstants.SESSIONDETAIL_IDSHP).equalsIgnoreCase("NA")) {
                            OFEventAPIRequest ear = new OFEventAPIRequest();
                            ear.setSessionId(new OFOneFlowSHP(mContext).getStringValue(OFConstants.SESSIONDETAIL_IDSHP));
                            ear.setEvents(retListToAPI);
                            OFHelper.v("FeedbackController", "OneFlow fetchEventsFromDB request prepared");
                            OFEventAPIRepo.sendLogsToApi(mContext, ear, this, OFConstants.ApiHitType.sendEventsToAPI, ids);
                        }

                }
                break;
            case sendEventsToAPI:
                //Events has been sent to api not deleting local records
                Integer[] ids1 = (Integer[]) obj;
                OFEventDBRepo.deleteEvents(mContext, ids1, this, OFConstants.ApiHitType.deleteEventsFromDB);

                break;
            case deleteEventsFromDB:
                OFHelper.v("FeedbackControler", "OneFlow events delete count[" + ((Integer) obj) + "]");
                Intent intent = new Intent("events_submitted");
                intent.putExtra("size", String.valueOf((Integer) obj));
                mContext.sendBroadcast(intent);

           break;
        }
    }
}
