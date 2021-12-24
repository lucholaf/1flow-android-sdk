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

package com.oneflow.analytics.controller;

import android.content.Context;
import android.content.Intent;

import com.oneflow.analytics.OFSurveyActivity;
import com.oneflow.analytics.model.survey.OFGetSurveyListResponse;
import com.oneflow.analytics.repositories.OFEventDBRepo;
import com.oneflow.analytics.repositories.OFSurvey;
import com.oneflow.analytics.sdkdb.OFOneFlowSHP;
import com.oneflow.analytics.utils.OFConstants;
import com.oneflow.analytics.utils.OFHelper;
import com.oneflow.analytics.utils.OFMyResponseHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class OFSurveyController implements OFMyResponseHandler {

    Context mContext;
    static OFSurveyController sc;

    private OFSurveyController(Context context) {
        this.mContext = context;
        getSurveyFromAPI();
    }

    public static OFSurveyController getInstance(Context context) {
        OFHelper.v("SurveyController", "OneFlow reached SurveyController ["+sc+"]");
        if (sc == null) {
            sc = new OFSurveyController(context);
        }
        return sc;
    }

    public void getSurveyFromAPI() {
        OFHelper.v("SurveyController", "OneFlow reached SurveyController 0");
        OFSurvey.getSurvey(mContext, this, OFConstants.ApiHitType.fetchSurveysFromAPI);
    }

    public void fetchSurveyFromList() {

    }

    public void submitFinishedSurveyToAPI() {

    }

    @Override
    public void onResponseReceived(OFConstants.ApiHitType hitType, Object obj, Long reserve) {

        OFHelper.v("SurveyController","OneFlow onReceived called type["+hitType+"]");
        switch (hitType) {
            case fetchSurveysFromAPI:
                OFEventDBRepo.fetchEventsBeforeSurvey(mContext, this, OFConstants.ApiHitType.fetchEventsBeforSurveyFetched);
                break;
            case fetchEventsBeforSurveyFetched:
                String[] name = (String[]) obj;
                OFHelper.v("SurveyController", "OneFlow events before survey found[" + Arrays.asList(name) + "]");
                OFGetSurveyListResponse surveyItem = checkSurveyTitleAndScreens(Arrays.asList(name));
                OFHelper.v("SurveyController", "OneFlow survey found[" + surveyItem + "]");
                if (surveyItem != null) {
                      if (surveyItem.getScreens() != null) {

                        OFHelper.v("OneFlow", "OneFlow screens not null");

                        if (surveyItem.getScreens().size() > 0) {
                            new OFOneFlowSHP(mContext).storeValue(OFConstants.SHP_SURVEYSTART, Calendar.getInstance().getTimeInMillis());
                            Intent intent = new Intent(mContext.getApplicationContext(), OFSurveyActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("SurveyType", surveyItem);//"move_file_in_folder");//""empty0");//
                            mContext.getApplicationContext().startActivity(intent);
                        }else{
                            OFHelper.v("SurveyController","OneFlow no older survey found");
                        }

                    }

                }


                break;
        }

    }

    private OFGetSurveyListResponse checkSurveyTitleAndScreens(List<String> type) {
        OFOneFlowSHP ofs = new OFOneFlowSHP(mContext);
        if (ofs.getBooleanValue(OFConstants.SHP_SHOULD_SHOW_SURVEY,true)) {
            ArrayList<OFGetSurveyListResponse> slr = ofs.getSurveyList();
            OFGetSurveyListResponse gslr = null;
            //ArrayList<SurveyScreens> surveyScreens = null;

            int counter = 0;
            String tag = this.getClass().getName();

            if (slr != null) {
                OFHelper.v(tag, "OneFlow list size[" + slr.size() + "]type[" + type + "]");
                for (OFGetSurveyListResponse item : slr) {
                    OFHelper.v(tag, "OneFlow list size 0 [" + item.getTrigger_event_name() + "]type[" + type + "]");

                    boolean recordFound = false;
                    for (String name : type) {

                        if (item.getTrigger_event_name().contains(name)) {
                            gslr = item;
                            recordFound = true;
                            OFHelper.v("SurveyController", "OneFlow survey from against[" + name + "]");

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
