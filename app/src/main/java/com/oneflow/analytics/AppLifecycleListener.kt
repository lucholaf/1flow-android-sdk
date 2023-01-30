package com.oneflow.analytics

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.oneflow.analytics.utils.OFHelper

class AppLifecycleListener : LifecycleObserver {
    var tag = "AppLifecycleListener"
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() { // app moved to foreground
        OFHelper.v(tag,"1Flow app is in Foreground");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onMoveToBackground() { // app moved to background
        OFHelper.v(tag,"1Flow app is in Background");
    }
}