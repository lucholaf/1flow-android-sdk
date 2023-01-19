package com.oneflow.analytics.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OFActivityCallbacks implements Application.ActivityLifecycleCallbacks {
    String tag = this.getClass().getName();
    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        OFHelper.d(tag,"ActivityCallbacks onActivityCreated["+activity.getClass().getName()+"]",true);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        OFHelper.d(tag,"ActivityCallbacks onActivityStarted["+activity.getClass().getName()+"]",true);
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        OFHelper.d(tag,"ActivityCallbacks onActivityResumed["+activity.getClass().getName()+"]",true);
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        OFHelper.d(tag,"ActivityCallbacks onActivityPaused["+activity.getClass().getName()+"]",true);
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        OFHelper.d(tag,"ActivityCallbacks onActivityStopped["+activity.getClass().getName()+"]",true);
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        OFHelper.d(tag,"ActivityCallbacks onActivitySaveInstanceState["+activity.getClass().getName()+"]",true);
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        OFHelper.d(tag,"ActivityCallbacks onActivityDestroyed["+activity.getClass().getName()+"]",true);
    }
}
