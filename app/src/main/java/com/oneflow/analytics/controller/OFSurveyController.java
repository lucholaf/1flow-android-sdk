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

import com.oneflow.analytics.OFSurveyActivityBannerBottom;
import com.oneflow.analytics.OFSurveyActivityBannerTop;
import com.oneflow.analytics.OFSurveyActivityBottom;
import com.oneflow.analytics.OFSurveyActivityCenter;
import com.oneflow.analytics.OFSurveyActivityFullScreen;
import com.oneflow.analytics.OFSurveyActivityTop;
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
import java.util.HashMap;
import java.util.List;

public class OFSurveyController implements OFMyResponseHandler {

    Context mContext;
    static OFSurveyController sc;

    private OFSurveyController(Context context) {
        this.mContext = context;
       // getSurveyFromAPI();
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
        OFOneFlowSHP shp = new OFOneFlowSHP(mContext);
        OFSurvey.getSurvey(shp.getStringValue(OFConstants.APPIDSHP), this, OFConstants.ApiHitType.fetchSurveysFromAPI,shp.getUserDetails().getAnalytic_user_id(),shp.getStringValue(OFConstants.SESSIONDETAIL_IDSHP),OFHelper.getAppVersionName(mContext));
    }

    public void fetchSurveyFromList() {

    }

    public void submitFinishedSurveyToAPI() {

    }
    HashMap<String, Class> activityName;
    public void setUpHashForActivity() {
        activityName = new HashMap<>();

        activityName.put("top-banner", OFSurveyActivityBannerTop.class);
        activityName.put("bottom-banner", OFSurveyActivityBannerBottom.class);

        activityName.put("fullscreen", OFSurveyActivityFullScreen.class);

        activityName.put("top-left", OFSurveyActivityTop.class);
        activityName.put("top-center", OFSurveyActivityTop.class);
        activityName.put("top-right", OFSurveyActivityTop.class);

        activityName.put("middle-left", OFSurveyActivityCenter.class); //name changed
        activityName.put("middle-center", OFSurveyActivityCenter.class); //name changed
        activityName.put("middle-right", OFSurveyActivityCenter.class); //name changed

        activityName.put("bottom-left", OFSurveyActivityBottom.class);
        activityName.put("bottom-center", OFSurveyActivityBottom.class); //default one
        activityName.put("bottom-right", OFSurveyActivityBottom.class);


    }
    @Override
    public void onResponseReceived(OFConstants.ApiHitType hitType, Object obj, Long reserve, String reserved) {

        OFHelper.v("SurveyController","OneFlow onReceived called type["+hitType+"]");

            switch (hitType) {
                case fetchSurveysFromAPI:
                    OFHelper.v("SurveyController", "OneFlow survey received");
                    if(obj!=null) {
                        ArrayList<OFGetSurveyListResponse> surveyListResponse = (ArrayList<OFGetSurveyListResponse>) obj;
                        if (surveyListResponse != null) {
                            new OFOneFlowSHP(mContext).setSurveyList(surveyListResponse);

                            Intent intent = new Intent("survey_list_fetched");
                            mContext.sendBroadcast(intent);

                        } else {
                            if (OFConstants.MODE.equalsIgnoreCase("dev")) {
                                OFHelper.makeText(mContext, reserved, 1);
                            }
                        }
                        //Enabled again on 13/June/22
                        OFEventDBRepo.fetchEventsBeforeSurvey(mContext, this, OFConstants.ApiHitType.fetchEventsBeforSurveyFetched);
                    }
                    break;
                case fetchEventsBeforSurveyFetched:
                    if(obj!=null) {
                        String[] name = (String[]) obj;
                        OFHelper.v("SurveyController", "OneFlow events before survey found[" + Arrays.asList(name) + "]length[" + name.length + "]");
                        if (name.length > 0) {
                            Object[] ret = checkSurveyTitleAndScreens(Arrays.asList(name));
                            OFGetSurveyListResponse surveyItem = (OFGetSurveyListResponse) ret[1];
                            OFHelper.v("SurveyController", "OneFlow survey found[" + surveyItem + "]");
                            if (surveyItem != null) {
                                if (surveyItem.getScreens() != null) {

                                    OFHelper.v("OneFlow", "OneFlow screens not null");

                                    if (surveyItem.getScreens().size() > 0) {
                                        setUpHashForActivity();
                                        new OFOneFlowSHP(mContext).storeValue(OFConstants.SHP_SURVEYSTART, Calendar.getInstance().getTimeInMillis());
                                        Intent surveyIntent = null;
                                        if (surveyItem.getSurveySettings().getSdkTheme().getWidgetPosition() == null) {
                                            surveyIntent = new Intent(mContext.getApplicationContext(), activityName.get("bottom-center"));
                                        } else {
                                            surveyIntent = new Intent(mContext.getApplicationContext(), activityName.get(surveyItem.getSurveySettings().getSdkTheme().getWidgetPosition()));
                                        }
                                        surveyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        surveyIntent.putExtra("SurveyType", surveyItem);//"move_file_in_folder");//""empty0");//
                                        surveyIntent.putExtra("eventName", (String) ret[0]);
                                        mContext.getApplicationContext().startActivity(surveyIntent);
                                    } else {
                                        OFHelper.v("SurveyController", "OneFlow no older survey found");
                                    }

                                }

                            }
                        }
                    }
                    break;
            }


    }

    /**
     * This method will return survey and its event name
     * @param type
     * @return [0] = event name [1] = Survey data
     */
    private Object[] checkSurveyTitleAndScreens(List<String> type) {
        Object []ret = new Object[2];
        OFOneFlowSHP ofs = new OFOneFlowSHP(mContext);
        if (ofs.getBooleanValue(OFConstants.SHP_SHOULD_SHOW_SURVEY,true)) {
            ArrayList<OFGetSurveyListResponse> slr = ofs.getSurveyList();
            //OFGetSurveyListResponse gslr = null;
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
                          //  gslr = item;
                            recordFound = true;
                            OFHelper.v("SurveyController", "OneFlow survey from against[" + name + "]");
                            ret[0]=name;
                            ret[1]=item;
                            break;
                        }
                    }
                    if (recordFound) break;
                }
            } /*else {
            Helper.makeText(mContext, "Configure project first", 1);
        }*/


            //retake survey check not required

            return ret;
        }else{
            return null;
        }
    }
}
