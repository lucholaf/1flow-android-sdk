/*
 *  Copyright 2021 1Flow, Inc.
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.oneflow.analytics;

import android.app.Application;
import android.os.CountDownTimer;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.oneflow.analytics.utils.OFHelper;

public class OFmapln extends Application implements LifecycleObserver {

    String tag = this.getClass().getName();
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onMoveToForeground() {
        cdt.start();
        OFHelper.v(tag, "OneFlow application onMoveToForeground");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onMoveToBackground() {
        try {
                cdt.cancel();
                OFHelper.v(tag, "OneFlow application onMoveToBackground");

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
            if(OFHelper.isConnected(OFmapln.this)) {
                OneFlow.sendEventsToApi(getApplicationContext());
            }

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
        OFHelper.makeText(this,"Application called",0);
        OFHelper.v(tag,"OneFlow application called");
    }
}
