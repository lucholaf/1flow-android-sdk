package com.oneflow.analytics.utils;

import android.content.Context;

import com.oneflow.analytics.model.survey.OFGetSurveyListResponse;
import com.oneflow.analytics.sdkdb.OFOneFlowSHP;

import java.util.ArrayList;
import java.util.Calendar;

public class OFFilterSurveys extends Thread{
    Context context;
    OFMyResponseHandlerOneFlow responseHandler;
    OFConstants.ApiHitType type;
    //OFGetSurveyListResponse gslr;
    String eventName;
    public OFFilterSurveys(Context context, OFMyResponseHandlerOneFlow responseHandler, OFConstants.ApiHitType type,  String eventName) {
        this.context = context;
        this.type = type;
        //this.gslr = gslr;
        this.eventName = eventName;
        this.responseHandler = responseHandler;
    }

    @Override
    public void run() {
        super.run();
        getFilterList();
    }

    String tag = this.getClass().getName();
    //public ArrayList<OFGetSurveyListResponse> getFilterList() {
    public void getFilterList() {
        ArrayList<OFGetSurveyListResponse> currentList = OFOneFlowSHP.getInstance(context).getSurveyList();
        ArrayList<OFGetSurveyListResponse> returningList = new ArrayList<>();

        if(currentList!=null) {
            OFHelper.v(tag, "1Flow actual size[" + currentList.size() + "]");
            for (OFGetSurveyListResponse gslrLocal : currentList) {
                if (checkSurveyAvailability(gslrLocal)) {
                    OFHelper.v(tag, "1Flow actual found true");
                    returningList.add(gslrLocal);
                } else {
                    OFHelper.v(tag, "1Flow actual found false");
                }
            }
            OFHelper.v(tag, "1Flow actual size 1[" + returningList.size() + "]");
        }
        responseHandler.onResponseReceived(type,returningList,0l,eventName,null,null);

    }

    private Boolean checkSurveyAvailability(OFGetSurveyListResponse gslr) {


        Long submittedSurvey = OFOneFlowSHP.getInstance(context).getLongValue(gslr.get_id());

        if (gslr.getSurveySettings().getResurvey_option()) {
            OFHelper.v(tag,"1Flow actual size inside if");
            Long totalInterval = 0l;
            Long diff = Calendar.getInstance().getTimeInMillis() - submittedSurvey;
            int diffDuration = 0;
            OFHelper.v("1Flow", "1Flow actual resurvey check diff[" + diff + "]retakeInputValue[" + gslr.getSurveySettings().getRetake_survey().getRetake_input_value() + "]");
            OFHelper.v("1Flow", "1Flow actual resurvey check retakeSelectValue[" + gslr.getSurveySettings().getRetake_survey().getRetake_select_value() + "]");
            diffDuration = (int) (diff / 1000);
            switch (gslr.getSurveySettings().getRetake_survey().getRetake_select_value()) {
                case "minutes":
                    totalInterval = gslr.getSurveySettings().getRetake_survey().getRetake_input_value() * 60;
                    break;
                case "hours":
                    totalInterval = gslr.getSurveySettings().getRetake_survey().getRetake_input_value() * 60 * 60;
                    break;
                case "days":
                    totalInterval = gslr.getSurveySettings().getRetake_survey().getRetake_input_value() * 24 * 60 * 60;
                    break;
                default:
                    OFHelper.v("1Flow", "1Flow actual retake_select_value is neither of minutes, hours or days");
            }
            OFHelper.v("1Flow", "1Flow actual resurvey check diffDuration[" + diffDuration + "]totalInterval[" + totalInterval + "]");
            if (diffDuration > totalInterval) {
                return true;
            }
        } else {
            OFHelper.v(tag,"1Flow actual size inside else");
            if(!(submittedSurvey>0)){
                return true;
            }
        }
        return false;
    }
}
