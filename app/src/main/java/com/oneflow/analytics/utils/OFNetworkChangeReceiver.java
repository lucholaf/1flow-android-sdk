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

package com.oneflow.analytics.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.oneflow.analytics.OneFlow;
import com.oneflow.analytics.model.location.OFLocationResponse;
import com.oneflow.analytics.model.survey.OFSurveyUserInput;
import com.oneflow.analytics.repositories.OFLogUserDBRepo;
import com.oneflow.analytics.repositories.OFSurvey;
import com.oneflow.analytics.sdkdb.OFOneFlowSHP;

public class OFNetworkChangeReceiver extends BroadcastReceiver implements OFMyResponseHandler {

    public Context context;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        this.context = context;
        // int status = NetworkUtil.getConnectivityStatusString(context);
        //Helper.makeText(context,"OneFlow Receiver called ["+intent.getAction()+"]",1);
        OFHelper.v("NetworkChangeReceiver", "OneFlow network state changes[" + OFHelper.isConnected(context) + "]");


        if (OFHelper.isConnected(context)) {
            // Helper.makeText(context,"Network available",1);

            OFLocationResponse lr = new OFOneFlowSHP(context).getUserLocationDetails();

            if (lr != null) {
                checkOffLineSurvey();
            } else {
                String projectKey = new OFOneFlowSHP(context).getStringValue(OFConstants.APPIDSHP);
                OneFlow.configure(context, projectKey);
                // CurrentLocation.getCurrentLocation(context,this,Constants.ApiHitType.fetchLocation);
            }
        }
    }


    /*Long duration = 1000 * 60 * 60 * 24L;
    Long interval = 1000 * 100L; //100L L FOR LONG
    CountDownTimer cdt = new CountDownTimer(duration, interval) {
        @Override
        public void onTick(long millisUntilFinished) {
            Helper.v("NetworkChangeReceiver","OneFlow tick called interval called");
            if (Helper.isConnected(context)) {
                OneFlow.sendEventsToApi(context.getApplicationContext());
            }

        }

        @Override
        public void onFinish() {
            //Helper.makeText(getApplicationContext(),"finish called",1);
        }

    };*/

    public void checkOffLineSurvey() {
        OFLogUserDBRepo.fetchSurveyInput(context, this, OFConstants.ApiHitType.fetchSurveysFromDB);
    }

    @Override
    public void onResponseReceived(OFConstants.ApiHitType hitType, Object obj, Long reserve, String reserved) {

        switch (hitType) {

            case fetchSurveysFromDB:
                OFSurveyUserInput survey = (OFSurveyUserInput) obj;
                if (survey != null) {
                    OFSurvey.submitUserResponseOffline(context, survey, this, OFConstants.ApiHitType.logUser);
                }
                break;
            case logUser:
                OFSurveyUserInput survey1 = (OFSurveyUserInput) obj;
                OFLogUserDBRepo.deleteSentSurveyFromDB(context, new Integer[]{survey1.get_id()}, this, OFConstants.ApiHitType.deleteEventsFromDB);
                break;
            case deleteSurveyFromDB:
                checkOffLineSurvey();
                break;
        }
    }
}