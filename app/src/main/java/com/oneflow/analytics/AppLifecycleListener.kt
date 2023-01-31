package com.oneflow.analytics

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.oneflow.analytics.utils.OFConstants
import com.oneflow.analytics.utils.OFHelper
import java.util.*

class AppLifecycleListener : LifecycleObserver {
    var tag = "AppLifecycleListener"
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() { // app moved to foreground
        OFHelper.v(tag, "1Flow app is in Foreground");
        //GetSurveyListResponse surveyItem = checkSurveyTitleAndScreens(tag);
        val mapvalues = HashMap<String, Long>()
        mapvalues.put("whenStarted",System.currentTimeMillis()/1000)
        OneFlow.recordEventsWithoutSurvey(OFConstants.AUTOEVENT_SESSIONSTART, mapvalues);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onMoveToBackground() { // app moved to background
        OFHelper.v(tag, "1Flow app is in Background");
    }
}