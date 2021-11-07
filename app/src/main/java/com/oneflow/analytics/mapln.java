package com.oneflow.analytics;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.CountDownTimer;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.oneflow.analytics.utils.Helper;
import com.oneflow.analytics.utils.NetworkChangeReceiver;

public class mapln extends Application implements LifecycleObserver {

    String tag = this.getClass().getName();
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onMoveToForeground() {
        cdt.start();
        Helper.v(tag, "OneFlow application onMoveToForeground");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onMoveToBackground() {
        try {
                cdt.cancel();
                Helper.v(tag, "OneFlow application onMoveToBackground");

        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
    }


    Long duration = 1000*60*60*24L;
    Long interval = 1000*100L; //100L L FOR LONG
    CountDownTimer cdt = new CountDownTimer(duration,interval) {
        @Override
        public void onTick(long millisUntilFinished) {
            //Helper.makeText(getApplicationContext(),"interval called",1);
            OneFlow.sendEventsToApi(getApplicationContext());

        }

        @Override
        public void onFinish() {
            //Helper.makeText(getApplicationContext(),"finish called",1);
        }

    };

    @Override
    public void onCreate() {
        super.onCreate();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        NetworkChangeReceiver ncr = new NetworkChangeReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(ncr,intentFilter);
    }
}
