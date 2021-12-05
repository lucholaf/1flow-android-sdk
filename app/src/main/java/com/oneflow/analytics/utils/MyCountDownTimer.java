package com.oneflow.analytics.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.oneflow.analytics.OneFlow;
import com.oneflow.analytics.model.Connectivity;
import com.oneflow.analytics.model.adduser.DeviceDetails;
import com.oneflow.analytics.model.adduser.LocationDetails;
import com.oneflow.analytics.model.createsession.CreateSessionRequest;
import com.oneflow.analytics.model.events.EventAPIRequest;
import com.oneflow.analytics.model.events.RecordEventsTab;
import com.oneflow.analytics.model.events.RecordEventsTabToAPI;
import com.oneflow.analytics.model.location.LocationResponse;
import com.oneflow.analytics.repositories.EventAPIRepo;
import com.oneflow.analytics.repositories.EventDBRepo;
import com.oneflow.analytics.sdkdb.OneFlowSHP;

import java.util.ArrayList;

public class MyCountDownTimer extends CountDownTimer implements MyResponseHandler {
    Context mContext;
    public MyCountDownTimer(Context context,Long duration, Long interval){
        super(duration,interval);
        this.mContext = context;
        Helper.v("MyCountDownTimer","OneFlow Constructor called");
    }
    @Override
    public void onTick(long millisUntilFinished) {
        Helper.v("MyCountDownTimer","OneFlow tick called");
        if (Helper.isConnected(mContext)) {
            EventDBRepo.fetchEvents(mContext, this, Constants.ApiHitType.fetchEventsFromDB);
        }
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onResponseReceived(Constants.ApiHitType hitType, Object obj, int reserve) {
        Helper.v("OneFlow", "OneFlow onReceived type[" + hitType + "]");
        switch (hitType) {

            case fetchEventsFromDB:

                Helper.v("FeedbackController", "OneFlow fetchEventsFromDB came back");


                ArrayList<RecordEventsTab> list = (ArrayList<RecordEventsTab>) obj;
                Helper.v("FeedbackController", "OneFlow fetchEventsFromDB list received size[" + list.size() + "]");
                //Preparing list to send api
                if(list.size()>0) {
                    Integer[] ids = new Integer[list.size()];
                    int i = 0;
                    ArrayList<RecordEventsTabToAPI> retListToAPI = new ArrayList<>();
                    RecordEventsTabToAPI retMain;
                    for (RecordEventsTab ret : list) {
                        retMain = new RecordEventsTabToAPI();
                        retMain.setEventName(ret.getEventName());
                        retMain.setTime(ret.getTime());

                        //retMain.setDataMap(ret.getDataMap());
                        retListToAPI.add(retMain);

                        ids[i++] = ret.getId();
                    }


                    if(retListToAPI.size()>0) {
                        if (!new OneFlowSHP(mContext).getStringValue(Constants.SESSIONDETAIL_IDSHP).equalsIgnoreCase("NA")) {
                            EventAPIRequest ear = new EventAPIRequest();
                            ear.setSessionId(new OneFlowSHP(mContext).getStringValue(Constants.SESSIONDETAIL_IDSHP));
                            ear.setEvents(retListToAPI);
                            ear.setMode("prod");
                            Helper.v("FeedbackController", "OneFlow fetchEventsFromDB request prepared");
                            EventAPIRepo.sendLogsToApi(mContext, ear, this, Constants.ApiHitType.sendEventsToAPI, ids);
                        }
                    }else{
                        Helper.e("OneFlow","OneFlow no event available");
                    }
                }
                break;
            case sendEventsToAPI:
                //Events has been sent to api not deleting local records
                Integer[] ids1 = (Integer[]) obj;
                EventDBRepo.deleteEvents(mContext, ids1, this, Constants.ApiHitType.deleteEventsFromDB);

                break;
            case deleteEventsFromDB:
                Helper.v("FeedbackControler", "OneFlow events delete count[" + ((Integer) obj) + "]");
                Intent intent = new Intent("events_submitted");
                intent.putExtra("size", String.valueOf((Integer) obj));
                mContext.sendBroadcast(intent);

           break;
        }
    }
}
