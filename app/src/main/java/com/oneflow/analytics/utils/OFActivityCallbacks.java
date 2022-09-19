package com.oneflow.analytics.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OFActivityCallbacks implements Application.ActivityLifecycleCallbacks {
    String tag = this.getClass().getName();
    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        OFHelper.d(tag,"Amit ActivityCallbacks onActivityCreated["+activity.getClass().getName()+"]",true);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        OFHelper.d(tag,"Amit ActivityCallbacks onActivityStarted",true);
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        OFHelper.d(tag,"Amit ActivityCallbacks onActivityResumed",true);
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        OFHelper.d(tag,"Amit ActivityCallbacks onActivityPaused",true);
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        OFHelper.d(tag,"Amit ActivityCallbacks onActivityStopped",true);
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        OFHelper.d(tag,"Amit ActivityCallbacks onActivitySaveInstanceState",true);
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        OFHelper.d(tag,"Amit ActivityCallbacks onActivityDestroyed",true);
    }
}
