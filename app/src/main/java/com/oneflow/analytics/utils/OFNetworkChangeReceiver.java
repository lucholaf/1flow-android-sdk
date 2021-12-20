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
        } else {

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
    public void onResponseReceived(OFConstants.ApiHitType hitType, Object obj, Long reserve) {

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