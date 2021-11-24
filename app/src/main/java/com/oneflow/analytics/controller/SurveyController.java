package com.oneflow.analytics.controller;

import android.content.Context;
import android.content.Intent;

import com.oneflow.analytics.OneFlow;
import com.oneflow.analytics.SurveyActivity;
import com.oneflow.analytics.model.survey.GetSurveyListResponse;
import com.oneflow.analytics.repositories.EventDBRepo;
import com.oneflow.analytics.repositories.Survey;
import com.oneflow.analytics.sdkdb.OneFlowSHP;
import com.oneflow.analytics.utils.Constants;
import com.oneflow.analytics.utils.Helper;
import com.oneflow.analytics.utils.MyResponseHandler;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class SurveyController implements MyResponseHandler {

    Context mContext;
    static SurveyController sc;

    private SurveyController(Context context) {
        this.mContext = context;
        getSurveyFromAPI();
    }

    public static SurveyController getInstance(Context context) {
        Helper.v("SurveyController", "OneFlow reached SurveyController ["+sc+"]");
        if (sc == null) {
            sc = new SurveyController(context);
        }
        return sc;
    }

    public void getSurveyFromAPI() {
        Helper.v("SurveyController", "OneFlow reached SurveyController 0");
        Survey.getSurvey(mContext, this, Constants.ApiHitType.fetchSurveysFromAPI);
    }

    public void fetchSurveyFromList() {

    }

    public void submitFinishedSurveyToAPI() {

    }

    @Override
    public void onResponseReceived(Constants.ApiHitType hitType, Object obj, int reserve) {

        Helper.v("SurveyController","OneFlow onReceived called type["+hitType+"]");
        switch (hitType) {
            case fetchSurveysFromAPI:
                EventDBRepo.fetchEventsBeforeSurvey(mContext, this, Constants.ApiHitType.fetchEventsBeforSurveyFetched);
                break;
            case fetchEventsBeforSurveyFetched:
                String[] name = (String[]) obj;
                Helper.v("SurveyController", "OneFlow events before survey found[" + Arrays.asList(name) + "]");
                GetSurveyListResponse surveyItem = checkSurveyTitleAndScreens(Arrays.asList(name));
                Helper.v("SurveyController", "OneFlow survey found[" + surveyItem + "]");
                if (surveyItem != null) {
                      if (surveyItem.getScreens() != null) {

                        Helper.v("OneFlow", "OneFlow screens not null");

                        if (surveyItem.getScreens().size() > 0) {
                            new OneFlowSHP(mContext).storeValue(Constants.SHP_SURVEYSTART, Calendar.getInstance().getTimeInMillis());
                            Intent intent = new Intent(mContext.getApplicationContext(), SurveyActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("SurveyType", surveyItem);//"move_file_in_folder");//""empty0");//
                            mContext.getApplicationContext().startActivity(intent);
                        }else{
                            Helper.v("SurveyController","OneFlow no older survey found");
                        }

                    }

                }


                break;
        }

    }

    private GetSurveyListResponse checkSurveyTitleAndScreens(List<String> type) {
        OneFlowSHP ofs = new OneFlowSHP(mContext);
        if (ofs.getBooleanValue(Constants.SHP_SHOULD_SHOW_SURVEY,true)) {
            ArrayList<GetSurveyListResponse> slr = ofs.getSurveyList();
            GetSurveyListResponse gslr = null;
            //ArrayList<SurveyScreens> surveyScreens = null;

            int counter = 0;
            String tag = this.getClass().getName();

            if (slr != null) {
                Helper.v(tag, "OneFlow list size[" + slr.size() + "]type[" + type + "]");
                for (GetSurveyListResponse item : slr) {
                    Helper.v(tag, "OneFlow list size 0 [" + item.getTrigger_event_name() + "]type[" + type + "]");

                    boolean recordFound = false;
                    for (String name : type) {

                        if (item.getTrigger_event_name().contains(name)) {
                            gslr = item;
                            recordFound = true;
                            Helper.v("SurveyController", "OneFlow survey from against[" + name + "]");

                            break;
                        }
                    }
                    if (recordFound) break;
                }
            } /*else {
            Helper.makeText(mContext, "Configure project first", 1);
        }*/


            //retake survey check not required

            return gslr;
        }else{
            return null;
        }
    }
}
