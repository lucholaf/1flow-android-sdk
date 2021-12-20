package com.oneflow.analytics.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class OFMyReceiver extends BroadcastReceiver {

    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        Bundle data = intent.getBundleExtra("data");
        String type = data.getString("type");
        if(type.equalsIgnoreCase(OFConstants.BRACTION_EVENTS)){
            sendEventsToAPI();
        }else if(type.equalsIgnoreCase(OFConstants.BRACTION_SURVEYS)){
            sendServeyToAPI();
        }

    }

    private void sendEventsToAPI(){
        //Helper.makeText(context,"sending event called from BR",0);
    }
    private void sendServeyToAPI(){
        //Helper.makeText(context,"sending survey called from BR",0);
    }
}
