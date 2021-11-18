package com.oneflow.analytics.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Looper;

import com.oneflow.analytics.OneFlow;
import com.oneflow.analytics.mapln;
import com.oneflow.analytics.model.location.LocationResponse;
import com.oneflow.analytics.model.survey.SurveyUserInput;
import com.oneflow.analytics.repositories.LogUserDBRepo;
import com.oneflow.analytics.repositories.Survey;
import com.oneflow.analytics.sdkdb.OneFlowSHP;

public class NetworkChangeReceiver extends BroadcastReceiver implements MyResponseHandler {

    public Context context;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        this.context = context;
        // int status = NetworkUtil.getConnectivityStatusString(context);
        //Helper.makeText(context,"OneFlow Receiver called ["+intent.getAction()+"]",1);
        Helper.v("NetworkChangeReceiver", "OneFlow network state changes[" + Helper.isConnected(context) + "]");


        if (Helper.isConnected(context)) {
            // Helper.makeText(context,"Network available",1);

            LocationResponse lr = new OneFlowSHP(context).getUserLocationDetails();

            if (lr != null) {
                checkOffLineSurvey();
            } else {
                String projectKey = new OneFlowSHP(context).getStringValue(Constants.APPIDSHP);
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
        LogUserDBRepo.fetchSurveyInput(context, this, Constants.ApiHitType.fetchSurveysFromDB);
    }

    @Override
    public void onResponseReceived(Constants.ApiHitType hitType, Object obj, int reserve) {

        switch (hitType) {

            case fetchSurveysFromDB:
                SurveyUserInput survey = (SurveyUserInput) obj;
                if (survey != null) {
                    Survey.submitUserResponseOffline(context, survey, this, Constants.ApiHitType.logUser);
                }
                break;
            case logUser:
                SurveyUserInput survey1 = (SurveyUserInput) obj;
                LogUserDBRepo.deleteSentSurveyFromDB(context, new Integer[]{survey1.get_id()}, this, Constants.ApiHitType.deleteEventsFromDB);
                break;
            case deleteSurveyFromDB:
                checkOffLineSurvey();
                break;
        }
    }
}