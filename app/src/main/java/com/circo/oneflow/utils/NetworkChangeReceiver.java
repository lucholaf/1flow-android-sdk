package com.circo.oneflow.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.circo.oneflow.model.survey.SurveyUserInput;
import com.circo.oneflow.repositories.LogUserDBRepo;
import com.circo.oneflow.repositories.Survey;

public class NetworkChangeReceiver extends BroadcastReceiver implements MyResponseHandler{

    public Context context;
    @Override
    public void onReceive(final Context context, final Intent intent) {

        this.context = context;
       // int status = NetworkUtil.getConnectivityStatusString(context);
        Helper.makeText(context,"OneFlow Receiver called ["+intent.getAction()+"]",1);
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            if (Helper.isConnected(context)) {
                Helper.makeText(context,"Network available",1);
                checkOffLineSurvey();
            } else {
                Helper.makeText(context,"Network gone",1);
            }
        }
    }

    public void checkOffLineSurvey(){
        LogUserDBRepo.fetchSurveyInput(context,this, Constants.ApiHitType.fetchSurveysFromDB);
    }

    @Override
    public void onResponseReceived(Constants.ApiHitType hitType, Object obj, int reserve) {

        switch(hitType){
            case fetchSurveysFromDB:
                SurveyUserInput survey = (SurveyUserInput)obj;
                Survey.submitUserResponseOffline(context, survey,this, Constants.ApiHitType.logUser);
                break;
            case logUser:
                SurveyUserInput survey1 = (SurveyUserInput)obj;
                LogUserDBRepo.deleteSentSurveyFromDB(context,new Integer[]{survey1.get_id()},this, Constants.ApiHitType.deleteEventsFromDB);
                break;
            case deleteSurveyFromDB:
                checkOffLineSurvey();
                break;
        }
    }
}