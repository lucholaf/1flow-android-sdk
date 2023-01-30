package com.oneflow.analytics;

import android.app.Application;
import android.os.StrictMode;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.oneflow.analytics.utils.OFHelper;

public class MyApp extends Application implements LifecycleObserver {

    String tag = this.getClass().getName();

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onMoveToForeground() { // app moved to foreground
        OFHelper.v(tag,"1Flow app is in Foreground");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onMoveToBackground() { // app moved to background
        OFHelper.v(tag,"1Flow app is in Background");
    }

    @Override
    public void onCreate() {


        //OFHelper.v(this.getClass().getName(),"StrictMode Enabling");
        /*StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());*/
        //OFHelper.v(this.getClass().getName(),"StrictMode Enablinged");
        super.onCreate();

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

    }
}
