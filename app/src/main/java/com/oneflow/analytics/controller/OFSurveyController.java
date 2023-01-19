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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oneflow.analytics.OFSurveyActivityBannerBottom;
import com.oneflow.analytics.OFSurveyActivityBannerTop;
import com.oneflow.analytics.OFSurveyActivityBottom;
import com.oneflow.analytics.OFSurveyActivityCenter;
import com.oneflow.analytics.OFSurveyActivityFullScreen;
import com.oneflow.analytics.OFSurveyActivityTop;
import com.oneflow.analytics.model.survey.OFGetSurveyListResponse;
import com.oneflow.analytics.model.survey.OFThrottlingConfig;
import com.oneflow.analytics.repositories.OFEventDBRepoKT;
import com.oneflow.analytics.repositories.OFSurvey;
import com.oneflow.analytics.sdkdb.OFOneFlowSHP;
import com.oneflow.analytics.utils.OFConstants;
import com.oneflow.analytics.utils.OFHelper;
import com.oneflow.analytics.utils.OFMyResponseHandlerOneFlow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class OFSurveyController implements OFMyResponseHandlerOneFlow {

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
        OFSurvey.getSurvey(shp.getStringValue(OFConstants.APPIDSHP), this, OFConstants.ApiHitType.fetchSurveysFromAPI,shp.getUserDetails().getAnalytic_user_id(),OFConstants.currentVersion);
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
    public void onResponseReceived(OFConstants.ApiHitType hitType, Object obj, Long reserve, String reserved, Object obj2, Object obj3) {

        OFHelper.v("SurveyController","OneFlow onReceived called type["+hitType+"]");

        switch (hitType) {
            case fetchSurveysFromAPI:
                OFHelper.v("SurveyController", "OneFlow survey received throttling[" + reserved + "]");
                if (obj != null) {

                    ArrayList<OFGetSurveyListResponse> surveyListResponse = (ArrayList<OFGetSurveyListResponse>) obj;
                    OFOneFlowSHP shp = new OFOneFlowSHP(mContext);
                    if (!OFHelper.validateString(reserved).equalsIgnoreCase("NA")) {
                        GsonBuilder builder = new GsonBuilder();
                        builder.serializeNulls();
                        Gson gson = builder.setPrettyPrinting().create();


                        OFThrottlingConfig ofThrottlingConfig = (OFThrottlingConfig) gson.fromJson(reserved, OFThrottlingConfig.class);
                        shp.setThrottlingConfig(ofThrottlingConfig);
                    }
                    setupGlobalTimerToDeactivateThrottlingLocally();

                    if (surveyListResponse != null) {

                        shp.setSurveyList(surveyListResponse);

                        Intent intent = new Intent("survey_list_fetched");
                        mContext.sendBroadcast(intent);

                    } else {
                        Intent intent = new Intent("survey_list_fetched");
                        intent.putExtra("msg", "No survey received");
                        mContext.sendBroadcast(intent);
                        if (OFConstants.MODE.equalsIgnoreCase("dev")) {
                            OFHelper.makeText(mContext, reserved, 1);
                        }
                    }
                    //Enabled again on 13/June/22
                    new OFEventDBRepoKT().fetchEventsBeforeSurvey(mContext, this, OFConstants.ApiHitType.fetchEventsBeforSurveyFetched);
                    //new OFMyDBAsyncTask(mContext, this, OFConstants.ApiHitType.fetchEventsBeforSurveyFetched).execute();
                }
                break;
            case fetchEventsBeforSurveyFetched:
                if (obj != null) {
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

    private void setupGlobalTimerToDeactivateThrottlingLocally() {


        OFHelper.v("OneFlow", "OneFlow checking throttling after survey received");
        OFThrottlingConfig config = new OFOneFlowSHP(mContext).getThrottlingConfig();
        //OFMyCountDownTimerThrottling.getInstance(mContext,0l,0l).cancel();
        if (config != null) {
            OFHelper.v("OneFlow", "OneFlow checking called config[" + config.getActivatedById() + "][" + config.getActivatedAt() + "]");
            if (config.getGlobalTime() != null && config.getGlobalTime() > 0) {


                if (config.isActivated()) {

                    long throttlingLifeTime = (config.getActivatedAt() + config.getGlobalTime()) * 1000;
                    OFHelper.v("OneFlow", "OneFlow checking called activated at [" + OFHelper.formatedDate(config.getActivatedAt() * 1000, "dd-MM-yyyy hh:mm:ss") + "]readable[" + OFHelper.formatedDate(throttlingLifeTime, "dd-MM-yyyy hh:mm:ss") + "]");
                    if (System.currentTimeMillis() < throttlingLifeTime) {
                        long throttlingFinishTime = throttlingLifeTime - System.currentTimeMillis();
                        OFHelper.v("OneFlow", "OneFlow checking called remaining time ["+throttlingFinishTime+"]");
                        //OFMyCountDownTimerThrottling.getInstance(mContext, throttlingFinishTime, (throttlingFinishTime / 2)).start();
                        setThrottlingAlarm(throttlingFinishTime);
                    } else {
                        OFHelper.v("OneFlow", "OneFlow checking throttling time over no need to start timer");
                    }
                } else {
                    config.setActivated(false);
                    config.setActivatedById(null);
                    new OFOneFlowSHP(mContext).setThrottlingConfig(config);
                }
            } else {
                config.setActivated(false);
                config.setActivatedById(null);
                new OFOneFlowSHP(mContext).setThrottlingConfig(config);
                OFHelper.v("OneFlow", "OneFlow checking called no throttling found after survey received");
            }
        }
        /*if (config != null) {

            if (config.getGlobalTime() != null) {
                if (config.getGlobalTime() > 0) {

                    OFHelper.v("OneFlow", "OneFlow deactivate called config global time not null");
                    if (config.isActivated()) {

                        long throttlingLifeTime = config.getActivatedAt() + (config.getGlobalTime() * 1000);
                        if (System.currentTimeMillis() > throttlingLifeTime) {
                            OFHelper.v("OneFlow", "OneFlow deactivate called time finished");
                            config.setActivated(false);
                            config.setActivatedById(null);
                            new OFOneFlowSHP(mContext).setThrottlingConfig(config);
                        }

                    }
                } else {
                    config.setActivated(false);
                    config.setActivatedById(null);
                    new OFOneFlowSHP(mContext).setThrottlingConfig(config);
                }
            }
        }*/
    }
    public void setThrottlingAlarm(long throttlingLifeTime) {


        /*AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, OFThrottlingAlarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, (config.getGlobalTime() * 1000)+System.currentTimeMillis(), pi);*/

        OFOneFlowSHP shp = new OFOneFlowSHP(mContext);
        shp.storeValue(OFConstants.SHP_THROTTLING_TIME, throttlingLifeTime);


    }
    /**
     * This method will return survey and its event name
     *
     * @param type
     * @return [0] = event name [1] = Survey data
     */
    private Object[] checkSurveyTitleAndScreens(List<String> type) {
        Object[] ret = new Object[2];
        OFOneFlowSHP ofs = new OFOneFlowSHP(mContext);
        if (ofs.getBooleanValue(OFConstants.SHP_SHOULD_SHOW_SURVEY, true)) {
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
                            ret[0] = name;
                            ret[1] = item;
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
        } else {
            return null;
        }
    }
}
