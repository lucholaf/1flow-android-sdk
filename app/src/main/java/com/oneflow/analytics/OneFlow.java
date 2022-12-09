

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
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.google.gson.Gson;
import com.oneflow.analytics.controller.OFEventController;
import com.oneflow.analytics.controller.OFSurveyController;
import com.oneflow.analytics.model.OFConnectivity;
import com.oneflow.analytics.model.OFFontSetup;
import com.oneflow.analytics.model.adduser.OFAddUserRequest;
import com.oneflow.analytics.model.adduser.OFAddUserResultResponse;
import com.oneflow.analytics.model.adduser.OFDeviceDetails;
import com.oneflow.analytics.model.createsession.OFCreateSessionRequest;
import com.oneflow.analytics.model.createsession.OFCreateSessionResponse;
import com.oneflow.analytics.model.events.OFEventAPIRequest;
import com.oneflow.analytics.model.events.OFRecordEventsTab;
import com.oneflow.analytics.model.events.OFRecordEventsTabToAPI;
import com.oneflow.analytics.model.loguser.OFLogUserRequest;
import com.oneflow.analytics.model.loguser.OFLogUserResponse;
import com.oneflow.analytics.model.survey.OFGetSurveyListResponse;
import com.oneflow.analytics.model.survey.OFSurveyUserInput;
import com.oneflow.analytics.model.survey.OFThrottlingConfig;
import com.oneflow.analytics.repositories.OFAddUserRepo;
import com.oneflow.analytics.repositories.OFCreateSession;
import com.oneflow.analytics.repositories.OFCurrentLocation;
import com.oneflow.analytics.repositories.OFEventAPIRepo;
import com.oneflow.analytics.repositories.OFEventDBRepo;
import com.oneflow.analytics.repositories.OFLogUserRepo;
import com.oneflow.analytics.repositories.OFProjectDetails;
import com.oneflow.analytics.sdkdb.OFOneFlowSHP;
import com.oneflow.analytics.utils.OFMyDBAsyncTask;
import com.oneflow.analytics.utils.OFActivityCallbacks;
import com.oneflow.analytics.utils.OFConstants;
import com.oneflow.analytics.utils.OFHelper;
//import com.oneflow.analytics.utils.OFLogCountdownTimer;
import com.oneflow.analytics.utils.OFLogCountdownTimer;
import com.oneflow.analytics.utils.OFMyCountDownTimer;
import com.oneflow.analytics.utils.OFMyCountDownTimerThrottling;
import com.oneflow.analytics.utils.OFMyResponseHandlerOneFlow;
import com.oneflow.analytics.utils.OFNetworkChangeReceiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class OneFlow implements OFMyResponseHandlerOneFlow {

    //TODO Convert this class to singleton
    static Context mContext;

    private static Long duration = 1000 * 60 * 60 * 12L;
    private static Long interval = 1000 * 100L; //100L L FOR LONG

    static BillingClient bcFake;

    HashMap<String, Class> activityName;

    private OneFlow(Context context) {
        this.mContext = context;
    }

    public static OFFontSetup titleFace, subTitleFace, optionsFace;

    public static void shouldShowSurvey(Boolean shouldShow) {
        try {
            OFHelper.v("OneFlow", "OneFlow shouldShow1[" + shouldShow + "]");
            new OFOneFlowSHP(mContext).storeValue(OFConstants.SHP_SHOULD_SHOW_SURVEY, shouldShow);
        } catch (Exception ex) {
            OFHelper.e("OneFlow", "OneFlow error showSurvey1[" + ex.getMessage() + "]");
        }
    }

    public static void shouldPrintLog(Boolean shouldShow) {
        try {
            OFHelper.v("OneFlow", "OneFlow shouldShowLog[" + shouldShow + "]");
            new OFOneFlowSHP(mContext).storeValue(OFConstants.SHP_SHOULD_PRINT_LOG, shouldShow);
            OFHelper.commanLogEnable = shouldShow;
        } catch (Exception ex) {
            OFHelper.e("OneFlow", "OneFlow error showSurvey[" + ex.getMessage() + "]");
        }
    }


    public static void registerActivityCallback() {
        ((Application) mContext.getApplicationContext()).registerActivityLifecycleCallbacks(new OFActivityCallbacks());
    }

    public static void configure(Context mContext, String projectKey, OFFontSetup titleFont) {
        if (!OFHelper.validateString(projectKey).equalsIgnoreCase("NA")) {
            if (OFHelper.validateString(OFHelper.headerKey).equalsIgnoreCase("NA")) {
                configureLocal(mContext, projectKey);
                titleFace = titleFont;

            } else {
                OFHelper.e("1Flow", "Re-register called, Nothing happen");
            }
        } else {
            OFHelper.e("1Flow", "Empty project given");
        }
    }
   /* public static void configure(Context mContext, String projectKey, OFFontSetup titleFont,Boolean trackScreens) {
        if (!OFHelper.validateString(projectKey).equalsIgnoreCase("NA")) {
            if (OFHelper.validateString(OFHelper.headerKey).equalsIgnoreCase("NA")) {
                configureLocal(mContext, projectKey);
                titleFace = titleFont;
                if(trackScreens){
                    registerActivityCallback();
                }

            } else {
                OFHelper.e("1Flow", "Re-register called, Nothing happen");
            }
        } else {
            OFHelper.e("1Flow", "Empty project given");
        }
    }*/

    public static void configure(Context mContext, String projectKey, OFFontSetup titleFont, OFFontSetup descriptionFont) {
        if (!OFHelper.validateString(projectKey).equalsIgnoreCase("NA")) {
            if (OFHelper.validateString(OFHelper.headerKey).equalsIgnoreCase("NA")) {
                configureLocal(mContext, projectKey);
                titleFace = titleFont;
                subTitleFace = descriptionFont;
            } else {
                OFHelper.e("1Flow", "Re-register called, Nothing happen");
            }
        } else {
            OFHelper.e("1Flow", "Empty project given");
        }
    }

    public static void configure(Context mContext, String projectKey, OFFontSetup titleFont, OFFontSetup descriptionFont, OFFontSetup optionsFont) {
        if (!OFHelper.validateString(projectKey).equalsIgnoreCase("NA")) {
            if (OFHelper.validateString(OFHelper.headerKey).equalsIgnoreCase("NA")) {
                configureLocal(mContext, projectKey);
                titleFace = titleFont;
                subTitleFace = descriptionFont;
                optionsFace = optionsFont;
            } else {
                OFHelper.e("1Flow", "Re-register called, Nothing happen");
            }
        } else {
            OFHelper.e("1Flow", "Empty project given");
        }

    }

    public static void configure(Context mContext, String projectKey) {
        OFHelper.v("1Flow", "OneFlow configure called project Key[" + projectKey + "]");
        if (!OFHelper.validateString(projectKey).equalsIgnoreCase("NA")) {
            OFOneFlowSHP fc = new OFOneFlowSHP(mContext);
            if (OFHelper.validateString(OFHelper.headerKey).equalsIgnoreCase("NA")) {// && !fc.getBooleanValue(OFConstants.AUTOEVENT_FIRSTOPEN,false)) {
                //if(!fc.getBooleanValue(OFConstants.AUTOEVENT_FIRSTOPEN,false)) {
                configureLocal(mContext, projectKey);
            } else {
                OFHelper.e("1Flow", "Re-register called, Nothing happen");
            }
        } else {
            OFHelper.e("1Flow", "Empty project given");
        }
    }
    public static String fontNameStr = "";
    public static void configure(Context mContext, String projectKey, String fontName) {
        OFHelper.v("1Flow", "OneFlow configure called project Key [" + projectKey + "]strName["+fontName+"]");
        if (!OFHelper.validateString(projectKey).equalsIgnoreCase("NA")) {
            OFOneFlowSHP fc = new OFOneFlowSHP(mContext);
            if (OFHelper.validateString(OFHelper.headerKey).equalsIgnoreCase("NA")) {// && !fc.getBooleanValue(OFConstants.AUTOEVENT_FIRSTOPEN,false)) {
                //if(!fc.getBooleanValue(OFConstants.AUTOEVENT_FIRSTOPEN,false)) {
                fontNameStr = fontName;
                configureLocal(mContext, projectKey);
            } else {
                OFHelper.e("1Flow", "Re-register called, Nothing happen");
            }
        } else {
            OFHelper.e("1Flow", "Empty project given");
        }
    }


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

    public static void configureLocal(Context mContext, String projectKey) {
        final OneFlow fc = new OneFlow(mContext);
        //fc.setUpHashForActivity();
        bcFake = BillingClient.newBuilder(mContext)
                .setListener(new PurchasesUpdatedListener() {
                    @Override
                    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> purchases) {
                        OFHelper.v("InAppPurchase", "OneFlow InAppPurchase Called");
                        //OFHelper.makeText(mContext,"in app purchase called",1);

                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                                && purchases != null) {
                            HashMap<String, String> eventValues = new HashMap<>();

                            eventValues.put("productID", purchases.get(0).getOrderId());
                            eventValues.put("quantity", String.valueOf(purchases.get(0).getQuantity()));
                            eventValues.put("price", "NA");
                            eventValues.put("subscriptionPeriod", "NA");
                            eventValues.put("subscriptionUnit", "NA");
                            eventValues.put("localCurrencyPrice", "NA");
                            eventValues.put("transactionIdentifier", purchases.get(0).getSignature());
                            eventValues.put("transactionDate", OFHelper.formatedDate(purchases.get(0).getPurchaseTime(), "MM/dd/YYYY"));
                            recordEvents(OFConstants.AUTOEVENT_INAPP_PURCHASE, eventValues);
                        }

                    }
                })
                .enablePendingPurchases()
                .build();


        fc.connectBillingClient();


        final OFOneFlowSHP ofs = new OFOneFlowSHP(mContext);

        // network listener and timer listener to make sure registered only once.
        if (!ofs.getBooleanValue(OFConstants.SHP_TIMER_LISTENER, false)) {
            OFMyCountDownTimer cmdt = OFMyCountDownTimer.getInstance(mContext, duration, interval);
            cmdt.start();
            ofs.storeValue(OFConstants.SHP_TIMER_LISTENER, true);
        }

        if (!ofs.getBooleanValue(OFConstants.SHP_NETWORK_LISTENER, false)) {
            OFHelper.v("1Flow", "OneFlow network listener registered ");
            OFNetworkChangeReceiver ncr = new OFNetworkChangeReceiver();
            IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            mContext.registerReceiver(ncr, intentFilter);
            ofs.storeValue(OFConstants.SHP_NETWORK_LISTENER, true);
        }


        Thread confThread = new Thread() {
            @Override
            public void run() {
                super.run();
                Looper.prepare();


                OFHelper.v("OneFlow", "OneFlow configure called isConnected[" + OFHelper.isConnected(mContext) + "]");
                ofs.storeValue(OFConstants.APPIDSHP, projectKey);

                if (OFHelper.isConnected(mContext)) {
                    /*fc.getLocation();*/
                    OFHelper.headerKey = projectKey;
                    fc.registerUser(fc.createRequest());
                    // flow has been change now calling survey after add session
                    // OFSurveyController.getInstance(mContext);

                    /*IntentFilter inf = new IntentFilter();
                    inf.addAction("survey_list_fetched");
                    inf.addAction("events_submitted");

                    mContext.registerReceiver(fc.listFetched, inf);*/
                }


                //Fetching current app version

               /* HashMap<String, String> mapValue = new HashMap<>();
                mapValue.put("app_version", currentVersion);
                recordEvents(OFConstants.AUTOEVENT_FIRSTOPEN, mapValue);*/


                // checking for update, if version number has changed
                String oldVersion = ofs.getStringValue(OFConstants.SDKVERSIONSHP);

                OFHelper.v("FeedbackController", "OneFlow current version [" + OFConstants.currentVersion + "]old version [" + oldVersion + "]");


                if (oldVersion.equalsIgnoreCase("NA")) {
                    ofs.storeValue(OFConstants.SDKVERSIONSHP, OFConstants.currentVersion);
                } else {
                    if (!oldVersion.equalsIgnoreCase(OFConstants.currentVersion)) {
                        HashMap<String, String> mapUpdateValue = new HashMap<>();
                        mapUpdateValue.put("app_version_current", OFConstants.currentVersion);
                        mapUpdateValue.put("app_version_previous", oldVersion);
                        recordEvents(OFConstants.AUTOEVENT_APPUPDATE, mapUpdateValue);
                    }
                }
                // Looper.loop();
            }
        };
        OFHelper.v("OneFlow", "OneFlow confThread isAlive[" + confThread.isAlive() + "]");


        // this logic is required because config was also being called from network change initially
        if (!confThread.isAlive()) {
            Long lastHit = ofs.getLongValue(OFConstants.SHP_ONEFLOW_CONFTIMING);

            Long diff = 10l; // set default value 100 for first time
            Long currentTime = Calendar.getInstance().getTimeInMillis();
            diff = (currentTime - lastHit) / 1000;

            OFHelper.v("OneFlow", "OneFlow conf recordEvents diff [" + diff + "]currentTime[" + currentTime + "]lastHit[" + lastHit + "]readable[" + OFHelper.formatedDate(lastHit, "yyyy-MM-dd hh:mm:ss") + "]");
            if (!ofs.getBooleanValue(OFConstants.AUTOEVENT_FIRSTOPEN, false)) {
                ofs.storeValue(OFConstants.SHP_ONEFLOW_CONFTIMING, currentTime);
                confThread.start();
            } else if (lastHit == 0 || diff > 60) {
                OFHelper.v("OneFlow", "OneFlow conf inside if");
                ofs.storeValue(OFConstants.SHP_ONEFLOW_CONFTIMING, currentTime);
                confThread.start();
            }
        }
        //fc.registerUser(fc.createRequest());
    }

    public void connectBillingClient() {
        bcFake.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {

                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.

                }

            }

            @Override
            public void onBillingServiceDisconnected() {

                Log.v("FakeBillingClass", "Amit payment billing disconnected");
            }
        });

    }

    /*BroadcastReceiver listFetched = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Helper.v("OneFlow","OneFlow reached receiver at OneFlow");
            if(intent.getAction().equalsIgnoreCase("survey_list_fetched")) {
                //ArrayList<GetSurveyListResponse> slr = new OneFlowSHP(mContext).getSurveyList();
                EventDBRepo.fetchEventsBeforSurvey(mContext,this, Constants.ApiHitType.fet);

            }else if(intent.getAction().equalsIgnoreCase("events_submitted")){
                //EventDBRepo.fetchEvents(FirstActivity.this, FirstActivity.this, Constants.ApiHitType.fetchEventsFromDB);

            }
        }
    };*/
    private OFAddUserRequest createRequest() {
        OFDeviceDetails dd = new OFDeviceDetails();
        dd.setUnique_id(OFHelper.getDeviceId(mContext));
        dd.setDevice_id(OFHelper.getDeviceId(mContext));
        dd.setOs("android");


        //OFLocationResponse lr = new OFOneFlowSHP(mContext).getUserLocationDetails();
       /* OFLocationDetails ld = new OFLocationDetails();
        ld.setCity(lr.getCity());
        ld.setRegion(lr.getRegion());
        ld.setCountry(lr.getCountry());

        try {
            ld.setLatitude(Double.parseDouble(lr.getLat()));
        } catch (Exception ex) {
            ld.setLatitude(0d);
        }

        try {
            ld.setLongitude(Double.parseDouble(lr.getLon()));
        } catch (Exception ex) {
            ld.setLongitude(0d);
        }*/
        final OFOneFlowSHP ofs = new OFOneFlowSHP(mContext);
        OFAddUserRequest aur = new OFAddUserRequest();

        // this flow added for recognizing user on server side to provide proper list of surveys
        String systemId = ofs.getStringValue(OFConstants.SHP_LOG_USER_KEY);
        if (systemId.equalsIgnoreCase("NA")) {
            aur.setSystem_id(OFHelper.getDeviceId(mContext));
        } else {
            aur.setSystem_id(systemId);
        }
        aur.setLanguage(new Locale(Locale.getDefault().getLanguage()).getDisplayName(Locale.US));
        aur.setOFDeviceDetails(dd);
        aur.setOFLocationDetails(null);
        aur.setLocationCheck(true);
        /*aur.setName("Amit");
        aur.setEmail("amit@tryskybox.com");*/
        return aur;
    }

    private void registerUser(OFAddUserRequest aur) {

        OFAddUserRepo.addUser(new OFOneFlowSHP(mContext).getStringValue(OFConstants.APPIDSHP), aur, this, OFConstants.ApiHitType.CreateUser);
    }

    private void createSession(OFCreateSessionRequest csr) {
        //sdkdb = Room.databaseBuilder(context, SDKDB.class,"one-flow-db").build();
        OFCreateSession.createSession(new OFOneFlowSHP(mContext).getStringValue(OFConstants.APPIDSHP), csr, this, OFConstants.ApiHitType.CreateSession);
    }

    /**
     * Record events on any user action. This method will recognize if any survey is available against this event name
     *
     * @param eventName   : to recognize event and start survey if have any
     * @param eventValues : will accept HashMap<String,Object>
     */
    public static void recordEvents(String eventName, HashMap eventValues) {

        OFHelper.v("FeedbackController", "OneFlow recordEvents record called with[" + eventName + "]at[" + OFHelper.formatedDate(System.currentTimeMillis(), "dd-MM-yyyy hh:mm:ss.SSS") + "]");
        try {
            // this 'if' is for converting date object to second format(timestamp)
            if (eventValues != null) {
                eventValues = OFHelper.checkDateInHashMap(eventValues);
            }
            OFHelper.v("FeedbackController", "OneFlow recordEvents record called with[" + eventValues + "]");
            if (mContext != null) {
                // storage, api call and check survey if available.
                //EventController.getInstance(mContext).storeEventsInDB(eventName, eventValues, 0);
                OFEventController ec = OFEventController.getInstance(mContext);
                ec.storeEventsInDB(eventName, eventValues, 0);


                //Checking if any survey available under coming event.
                new OneFlow(mContext).checkSurveyTitleAndScreensInBackground(OFConstants.ApiHitType.checkResurveyNSubmission, eventName);

            } else {
                OFHelper.v("OneFlow", "OneFlow null context for event");
            }
        } catch (Exception ex) {

        }
    }



    /*public static void logUser(String uniqueId, HashMap<String, String> mapValue) {
        if (OFHelper.isConnected(mContext)) {

            OFAddUserResultResponse aurr = new OFOneFlowSHP(mContext).getUserDetails();
            if (aurr != null) {
                OFHelper.v("OneFlow", "OneFlow logUser data stored");
                OFLogUserRequest lur = new OFLogUserRequest();
                lur.setSystem_id(uniqueId);
                lur.setAnonymous_user_id(new OFOneFlowSHP(mContext).getUserDetails().getAnalytic_user_id());
                lur.setParameters(mapValue);
                lur.setSession_id(new OFOneFlowSHP(mContext).getStringValue(OFConstants.SESSIONDETAIL_IDSHP));
                new OFOneFlowSHP(mContext).setLogUserRequest(lur);
                // this api calling shifted to send Event api response
            }
            sendEventsToApi(mContext);
        }
    }*/

    /**
     * This method will help to recognize user. Below mentioned 2 values will be required
     *
     * @param uniqueId   : to identify user uniquely, it could be e-mail id or any thing.
     * @param userDetail : data related to user.
     */
    public static void logUser(String uniqueId, HashMap<String, Object> userDetail) {
        OFHelper.v("OneFlow", "OneFlow logUser data stored 0");
        // User id must not be empty
        if (OFHelper.validateString(uniqueId).equalsIgnoreCase("NA")) {

            OFHelper.e("OneFlow LogUser Error", "User id must not be empty to log user");
           /* String str = null;
            Log.v("OneFlow","Application str.split["+str.substring(5)+"]");*/
        } else {
            if (OFHelper.isConnected(mContext)) {
                if (userDetail != null) {
                    userDetail = OFHelper.checkDateInHashMap(userDetail);
                }
                OFHelper.v("OneFlow", "OneFlow logUser data stored 1");
                OFAddUserResultResponse aurr = new OFOneFlowSHP(mContext).getUserDetails();
                if (aurr != null) {
                    OFHelper.v("OneFlow", "OneFlow logUser data stored 2");
                    OFLogUserRequest lur = new OFLogUserRequest();
                    lur.setSystem_id(uniqueId);
                    lur.setAnonymous_user_id(new OFOneFlowSHP(mContext).getUserDetails().getAnalytic_user_id());
                    lur.setParameters(userDetail);
                    lur.setSession_id(new OFOneFlowSHP(mContext).getStringValue(OFConstants.SESSIONDETAIL_IDSHP));
                    new OFOneFlowSHP(mContext).setLogUserRequest(lur);
                    // this api calling shifted to send Event api response
                }
                sendEventsToApi(mContext);
            }
        }
    }

    /**
     * This method will check all aspects of re-survey
     *
     * @return
     */
    private OFGetSurveyListResponse shouldReturnSurvey(OFGetSurveyListResponse gslr) {

        Long submitTime = new OFOneFlowSHP(mContext).getLongValue(gslr.get_id());
        OFHelper.v("OneFlow", "OneFlow resurvey check[" + submitTime + "]");
        if (submitTime > 0) {
            //Checking offline storage of survey

            try {
                OFHelper.v("OneFlow", "OneFlow resurvey check option[" + gslr.getSurveySettings().getResurvey_option() + "]current[" + Calendar.getInstance().getTimeInMillis() + "]");
                if (gslr.getSurveySettings().getResurvey_option()) {
                    Long totalInterval = 0l;
                    Long diff = Calendar.getInstance().getTimeInMillis() - submitTime;
                    int diffDuration = 0;
                    OFHelper.v("OneFlow", "OneFlow resurvey check diff[" + diff + "]retakeInputValue[" + gslr.getSurveySettings().getRetake_survey().getRetake_input_value() + "]");
                    OFHelper.v("OneFlow", "OneFlow resurvey check retakeSelectValue[" + gslr.getSurveySettings().getRetake_survey().getRetake_select_value() + "]");
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
                            OFHelper.v("FeedbackController", "OneFlow retake_select_value is neither of minutes, hours or days");
                    }
                    OFHelper.v("OneFlow", "OneFlow resurvey check diffDuration[" + diffDuration + "]totalInterval[" + totalInterval + "]");
                    if (diffDuration > totalInterval) {
                        return gslr;
                    } else {
                        return null;
                    }

                } else {
                    OFHelper.v("FeedbackController", "OneFlow ResurveyOption[false]");
                    return null;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        } else {
            return gslr;
        }

    }

    private void shouldReturnSurveyCheckingFromDB(OFConstants.ApiHitType apiHitType, OFGetSurveyListResponse gslr) {


    }

    /**
     * This method will check if trigger name is available in the list or not
     *
     * @param type
     * @return
     */
    //private ArrayList<SurveyScreens> checkSurveyTitleAndScreens(String type){
    private OFGetSurveyListResponse checkSurveyTitleAndScreens(String type) {
        OFOneFlowSHP ofs = new OFOneFlowSHP(mContext);
        OFHelper.v("OneFlow", "OneFlow checkSurveyTitleAndScreens[" + ofs.getBooleanValue(OFConstants.SHP_SHOULD_SHOW_SURVEY, true) + "]");
        if (ofs.getBooleanValue(OFConstants.SHP_SHOULD_SHOW_SURVEY, true)) {
            ArrayList<OFGetSurveyListResponse> slr = ofs.getSurveyList();
            OFGetSurveyListResponse gslr = null;
            //ArrayList<SurveyScreens> surveyScreens = null;

            int counter = 0;
            String tag = this.getClass().getName();

            if (slr != null) {
                OFHelper.v(tag, "OneFlow list size[" + slr.size() + "]type[" + type + "]");
                for (OFGetSurveyListResponse item : slr) {
                    OFHelper.v(tag, "OneFlow list size 0 [" + item.getTrigger_event_name() + "]type[" + type + "]");
                    String[] eventName = item.getTrigger_event_name().split(",");
                    boolean recordFound = false;
                    for (String name : eventName) {
                        if (name.contains(type)) {
                            gslr = item;
                            OFHelper.v(tag, "OneFlow survey found on event name[" + type + "]");
                            recordFound = true;
                            break;
                        }
                    }

                    if (recordFound) {
                        break;
                    }
                }
            } /*else {
            Helper.makeText(mContext, "Configure project first", 1);
        }*/


            //Resurvey login
            if (gslr != null) {
                gslr = shouldReturnSurvey(gslr);
            }

            return gslr;
        } else {
            return null;
        }
    }

    /**
     * This method will check if trigger name is available in the list or not
     *
     * @param type
     * @return
     */


    private void checkSurveyTitleAndScreensInBackground(OFConstants.ApiHitType hitType, String firedEventName) {
        OFOneFlowSHP ofs = new OFOneFlowSHP(mContext);
        OFHelper.v("OneFlow", "OneFlow checkSurveyTitleAndScreens[" + ofs.getBooleanValue(OFConstants.SHP_SHOULD_SHOW_SURVEY, true) + "]");
        if (ofs.getBooleanValue(OFConstants.SHP_SHOULD_SHOW_SURVEY, true)) {
            ArrayList<OFGetSurveyListResponse> slr = ofs.getSurveyList();
            OFGetSurveyListResponse gslr = null;
            //ArrayList<SurveyScreens> surveyScreens = null;

            int counter = ofs.getIntegerValue(OFConstants.SHP_SURVEY_SEARCH_POSITION);
            OFHelper.v("OneFlow", "OneFlow counter value before["+counter+"]");
            String tag = this.getClass().getName();

            if (slr != null) {
                OFHelper.v(tag, "OneFlow list size[" + slr.size() + "]firedEventName[" + firedEventName + "]");
                OFGetSurveyListResponse item = null;
                for(int i = 0;i<slr.size();i++){

                    OFHelper.v(tag, "OneFlow list searching at["+i+"]");
                    if(i<counter)continue;

                    item = slr.get(i);

                    counter++;
                    OFHelper.v(tag, "OneFlow list size 0 [" + item.getTrigger_event_name() + "]firedEventName[" + firedEventName + "]");
                    String[] eventName = item.getTrigger_event_name().split(",");
                    boolean recordFound = false;
                    for (String name : eventName) {
                        if (name.contains(firedEventName)) {
                            gslr = item;
                            OFHelper.v(tag, "OneFlow survey found on event name[" + firedEventName + "]");
                            recordFound = true;
                            break;
                        }
                    }

                    if (recordFound) {
                        break;
                    } else {
                        OFHelper.v(tag, "OneFlow survey not found for [" + firedEventName + "] ");
                    }
                }
            } /*else {
            Helper.makeText(mContext, "Configure project first", 1);
        }*/

            OFHelper.v("OneFlow", "OneFlow counter value after["+counter+"]size["+slr.size()+"]");
            if(counter<slr.size()) {
                ofs.storeValue(OFConstants.SHP_SURVEY_SEARCH_POSITION, counter);
            }else{
                ofs.storeValue(OFConstants.SHP_SURVEY_SEARCH_POSITION, 0);
            }
            //Resurvey login
            if (gslr == null) {
                onResponseReceived(hitType, null, 0l, "", null, null);
            } else {
               // OFLogUserDBRepo.findSurveyForID(mContext, this, OFConstants.ApiHitType.fetchSubmittedSurvey, gslr, gslr.get_id(), new OFOneFlowSHP(mContext).getStringValue(OFConstants.USERUNIQUEIDSHP), firedEventName);
                new OFMyDBAsyncTask(mContext,this,OFConstants.ApiHitType.fetchSubmittedSurvey,false).execute(gslr, new OFOneFlowSHP(mContext).getStringValue(OFConstants.USERUNIQUEIDSHP), firedEventName);
            }


        }
    }


    public void getProjectDetails() {
        OFProjectDetails.getProject(mContext);
    }

    public static void sendEventsToApi(Context contex) {
        OneFlow fc = new OneFlow(contex);
        OFEventDBRepo.fetchEvents(mContext, fc, OFConstants.ApiHitType.fetchEventsFromDB);
    }

    public void getLocation() {
        OFCurrentLocation.getCurrentLocation(mContext, this, OFConstants.ApiHitType.fetchLocation);
    }


    OFGetSurveyListResponse gslrGlobal;

    @Override
    public void onResponseReceived(OFConstants.ApiHitType hitType, Object obj, Long reserve, String reserved, Object obj2, Object obj3) {
        OFHelper.v("OneFlow", "OneFlow onReceived type[" + hitType + "]reserve[" + reserve + "]");
        switch (hitType) {


            case CreateUser:

                if (obj != null) {
                    OFAddUserResultResponse userResponse = (OFAddUserResultResponse) obj;
                    new OFOneFlowSHP(mContext).setUserDetails(userResponse);
                    TelephonyManager telephonyManager = ((TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE));
                    String operatorName = telephonyManager.getNetworkOperatorName().isEmpty() ? null : telephonyManager.getNetworkOperatorName();

                    WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                    Display display = wm.getDefaultDisplay();
                    DisplayMetrics metrics = new DisplayMetrics();
                    display.getMetrics(metrics);

                    OFCreateSessionRequest csr = new OFCreateSessionRequest();


                    OFDeviceDetails ddc = new OFDeviceDetails();
                    ddc.setUnique_id(OFHelper.getDeviceId(mContext));
                    ddc.setDevice_id(OFHelper.getDeviceId(mContext));
                    ddc.setOs("android");
                    ddc.setCarrier(operatorName);
                    ddc.setManufacturer(Build.MANUFACTURER);
                    ddc.setModel(Build.MODEL);
                    ddc.setOs_ver(Build.VERSION.SDK);
                    ddc.setScreen_width(metrics.widthPixels);
                    ddc.setScreen_height(metrics.heightPixels);
                    String userId = "NA";

                    if (userResponse != null) {
                        userId = userResponse.getAnalytic_user_id();
                    }
                    csr.setAnalytic_user_id(userId);
                    csr.setSystem_id(OFHelper.getDeviceId(mContext));
                    csr.setDevice(ddc);
                    csr.setLocation_check(true);
                    csr.setLocation(null);
                    csr.setConnectivity(getConnectivityData());

                    csr.setApi_version(OFConstants.currentVersion);
                    csr.setApp_build_number(OFConstants.currentVersion);
                    csr.setLibrary_name("1flow-android-sdk");
                    csr.setLibrary_version(String.valueOf(1));
                    csr.setApi_endpoint("session");


                    csr.setApp_version(OFConstants.currentVersion);

                    recordEvents(OFConstants.AUTOEVENT_SESSIONSTART, null);

                    createSession(csr);
                } else {
                    OFHelper.headerKey = "";
                    Intent intent = new Intent("survey_list_fetched");
                    intent.putExtra("msg", "Invalid project key");
                    mContext.sendBroadcast(intent);
                    if (OFConstants.MODE.equalsIgnoreCase("dev")) {
                        OFHelper.makeText(mContext, reserved, 1);
                    }
                }
                break;
            case CreateSession:
                if (obj != null) {
                    //Earlier calling parallel with create user. Flow changed now calling once session created
                    OFCreateSessionResponse createSession = (OFCreateSessionResponse) obj;
                    if (createSession != null) {
                        OFOneFlowSHP oneFlowSHP = new OFOneFlowSHP(mContext);

                        OFHelper.v("FeedbackController", "OneFlow checking firstOpen [" + oneFlowSHP.getBooleanValue(OFConstants.AUTOEVENT_FIRSTOPEN, false) + "]");
                        if (!oneFlowSHP.getBooleanValue(OFConstants.AUTOEVENT_FIRSTOPEN, false)) {

                            HashMap<String, Object> mapValue = new HashMap<>();
                            mapValue.put("app_version", OFConstants.currentVersion);
                            recordEvents(OFConstants.AUTOEVENT_FIRSTOPEN, mapValue);
                            oneFlowSHP.storeValue(OFConstants.AUTOEVENT_FIRSTOPEN, true);
                        }


                        oneFlowSHP.storeValue(OFConstants.SESSIONDETAIL_IDSHP, createSession.get_id());
                        oneFlowSHP.storeValue(OFConstants.SESSIONDETAIL_SYSTEM_IDSHP, createSession.getSystem_id());

                        //calling fetch survey api on create session success
                        OFSurveyController.getInstance(mContext).getSurveyFromAPI();
                    } else {
                        OFHelper.headerKey = "";
                        if (OFConstants.MODE.equalsIgnoreCase("dev")) {
                            OFHelper.makeText(mContext, reserved, 1);
                        }
                    }
                } else {
                    OFHelper.e("OneFlow", "OneFlow subimission failed CreateSession");
                }

                break;
            case fetchLocation:

                if (OFHelper.isConnected(mContext)) {
                    registerUser(createRequest());
                }
                break;
            case fetchEventsFromDB:

                OFHelper.v("FeedbackController", "OneFlow checking before log fetchEventsFromDB came back");
                OneFlow fc = new OneFlow(mContext);
                OFOneFlowSHP ofshp = new OFOneFlowSHP(mContext);
                if (obj != null) {
                    ArrayList<OFRecordEventsTab> list = (ArrayList<OFRecordEventsTab>) obj;
                    OFHelper.v("FeedbackController", "OneFlow checking before log fetchEventsFromDB list received size[" + list.size() + "]");
                    //Preparing list to send api
                    if (list.size() > 0) {
                        Integer[] ids = new Integer[list.size()];
                        int i = 0;
                        ArrayList<OFRecordEventsTabToAPI> retListToAPI = new ArrayList<>();
                        OFRecordEventsTabToAPI retMain;
                        for (OFRecordEventsTab ret : list) {
                            retMain = new OFRecordEventsTabToAPI();
                            retMain.setEventName(ret.getEventName());
                            retMain.setTime(ret.getTime());
                            retMain.setPlatform("a");
                            retMain.setDataMap(ret.getDataMap());
                            retListToAPI.add(retMain);
                            ids[i++] = ret.getId();
                        }

                        if (!ofshp.getStringValue(OFConstants.SESSIONDETAIL_IDSHP).equalsIgnoreCase("NA")) {
                            OFEventAPIRequest ear = new OFEventAPIRequest();
                            ear.setSessionId(ofshp.getStringValue(OFConstants.SESSIONDETAIL_IDSHP));
                            ear.setEvents(retListToAPI);

                            OFHelper.v("OneFlow", "OneFlow checking before log fetchEventsFromDB request prepared");
                            OFEventAPIRepo.sendLogsToApi(new OFOneFlowSHP(mContext).getStringValue(OFConstants.APPIDSHP), ear, fc, OFConstants.ApiHitType.sendEventsToAPI, ids);
                        }
                    } else {

                        OFLogUserRequest lur = ofshp.getLogUserRequest();
                        OFHelper.e("OneFlow", "OneFlow checking No event available hitting log[" + lur + "]");
                        if (lur != null) {
                            OFLogUserRepo.logUser(new OFOneFlowSHP(mContext).getStringValue(OFConstants.APPIDSHP), lur, this, OFConstants.ApiHitType.logUser);
                        }
                    }
                } else {
                    OFHelper.e("OneFlow", "OneFlow subimission failed fetchedEvents");
                }
                break;
            case sendEventsToAPI:
                //Events has been sent to api not deleting local records
                Integer[] ids1 = (Integer[]) obj;
                OFEventDBRepo.deleteEvents(mContext, ids1, this, OFConstants.ApiHitType.deleteEventsFromDB);

                break;
            case deleteEventsFromDB:
                OFHelper.v("FeedbackControler", "OneFlow checking events submitted hitting logs delete count[" + ((Integer) obj) + "]");
                Intent intent = new Intent("events_submitted");
                intent.putExtra("size", String.valueOf((Integer) obj));
                mContext.sendBroadcast(intent);
                OFLogUserRequest lur = new OFOneFlowSHP(mContext).getLogUserRequest();
                if (lur != null) {
                    OFLogUserRepo.logUser(new OFOneFlowSHP(mContext).getStringValue(OFConstants.APPIDSHP), lur, this, OFConstants.ApiHitType.logUser);
                }
                break;
            case fetchSubmittedSurvey:
                if (obj != null) {
                    OFGetSurveyListResponse gslr = (OFGetSurveyListResponse) obj;
                    Long createdOn = (Long)obj2;
                    if (createdOn > 0) {
                        //Checking offline storage of survey

                        try {
                            OFHelper.v("OneFlow", "OneFlow resurvey check option[" + gslr.getSurveySettings().getResurvey_option() + "]current[" + Calendar.getInstance().getTimeInMillis() + "]");
                            if (gslr.getSurveySettings().getResurvey_option()) {
                                Long totalInterval = 0l;
                                Long diff = Calendar.getInstance().getTimeInMillis() - createdOn;
                                int diffDuration = 0;
                                OFHelper.v("OneFlow", "OneFlow resurvey check diff[" + diff + "]retakeInputValue[" + gslr.getSurveySettings().getRetake_survey().getRetake_input_value() + "]");
                                OFHelper.v("OneFlow", "OneFlow resurvey check retakeSelectValue[" + gslr.getSurveySettings().getRetake_survey().getRetake_select_value() + "]");
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
                                        OFHelper.v("OneFlow", "OneFlow retake_select_value is neither of minutes, hours or days");
                                }
                                OFHelper.v("OneFlow", "OneFlow resurvey check diffDuration[" + diffDuration + "]totalInterval[" + totalInterval + "]");
                                if (diffDuration > totalInterval) {
                                    String eventName = (String)obj3;
                                    onResponseReceived(OFConstants.ApiHitType.checkResurveyNSubmission, gslr, 0l, eventName, null, null);
                                }
                            }else{
                                // as this survey not fired, going to check again if any other survey available with same event name
                                OFHelper.v("OneFlow", "OneFlow resurvey failed checking list again");
                                String eventName = (String)obj3;
                                checkSurveyTitleAndScreensInBackground(OFConstants.ApiHitType.checkResurveyNSubmission,eventName);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();

                        }
                    } else {
                        OFHelper.v("OneFlow", "OneFlow no survey found show directly");
                        String eventName = (String)obj3;
                        onResponseReceived(OFConstants.ApiHitType.checkResurveyNSubmission, gslr, 0l, eventName, null, null);
                    }
                } else {
                    OFHelper.e("OneFlow", "OneFlow subimission failed fetchSubmittedSurvey");
                }
                break;
            case checkResurveyNSubmission:
                if (obj != null) {
                    OFGetSurveyListResponse gslr = (OFGetSurveyListResponse) obj;
                    if (gslr != null) {
                        OFHelper.v("FeedbackController", "OneFlow resurvey checked survey found surveyItem[" + new Gson().toJson(gslr) + "]event name[" + reserved + "]");

                        OFOneFlowSHP ofs = new OFOneFlowSHP(mContext);

                        // resetting counter for similar type of event name
                        ofs.storeValue(OFConstants.SHP_SURVEY_SEARCH_POSITION, 0);
                        if (gslr.getScreens() != null) {

                            if (gslr.getScreens().size() > 0) {
                                OFHelper.v("OneFlow", "OneFlow resurvey checked running survey[" + (!ofs.getBooleanValue(OFConstants.SHP_SURVEY_RUNNING, false)) + "]");
                                if (!ofs.getBooleanValue(OFConstants.SHP_SURVEY_RUNNING, false)) {

                                    ArrayList<String> closedSurveyList = ofs.getClosedSurveyList();

                                    boolean hasClosed = false;
                                    if (closedSurveyList != null) {
                                        hasClosed = closedSurveyList.contains(gslr.get_id());
                                    }
                                    OFHelper.v("OneFlow", "OneFlow closed survey[" + hasClosed + "][" + gslr.getSurveySettings().getClosedAsFinished() + "]position[" + gslr.getSurveySettings().getSdkTheme().getWidgetPosition() + "]");
                                    if (!(gslr.getSurveySettings().getClosedAsFinished() && hasClosed)) { // this is if for empty closed survey

                                        setUpHashForActivity();


                                        //throttling condition below
                                        OFThrottlingConfig throttlingConfig = ofs.getThrottlingConfig();
                                        if (throttlingConfig == null) {
                                            triggerSurvey(gslr, reserved);
                                        } else {

                                            OFHelper.v("OneFlow", "OneFlow globalThrottling[" + gslr.getSurveySettings().getOverrideGlobalThrottling() + "]throttlingConfig isActivated[" + throttlingConfig.isActivated() + "]");


                                            if (gslr.getSurveySettings().getOverrideGlobalThrottling()) {
                                                triggerSurvey(gslr, reserved);
                                            } else {
                                                if (throttlingConfig.isActivated()) {
                                                    if (throttlingConfig.getActivatedById().equalsIgnoreCase(gslr.get_id())) {

                                                        OFHelper.v("OneFlow", "OneFlow globalThrottling id matched ");
                                                        gslrGlobal = gslr;
                                                        //OFLogUserDBRepo.findLastSubmittedSurveyID(mContext, this, OFConstants.ApiHitType.lastSubmittedSurvey, null, reserved);
                                                        // check in submitted survey list locally if this survey has been submitted then false
                                                        new OFMyDBAsyncTask(mContext,this, OFConstants.ApiHitType.lastSubmittedSurvey,false).execute();

                                                    }
                                                    //else nothing to do
                                                } else {
                                                    triggerSurvey(gslr, reserved);
                                                }
                                            }

                                        }
                                    }else{
                                        OFHelper.v("OneFlow", "OneFlow resurvey this survey already submitted searching again");
                                        // as this survey not fired, going to check again if any other survey available with same event name
                                      //  checkSurveyTitleAndScreensInBackground(OFConstants.ApiHitType.checkResurveyNSubmission,reserved);//reserved is event name
                                    }
                                }
                            }
                        }
                    }
                } else {
                    OFHelper.e("OneFlow", "OneFlow subimission failed checkResurveyNSubmission");
                }
                break;
            case lastSubmittedSurvey:
                OFHelper.v("OneFlow", "OneFlow globalThrottling received["+obj+"]");

                OFGetSurveyListResponse gslr = gslrGlobal;
                if (obj == null) {
                    triggerSurvey(gslr, reserved);
                } else {
                    OFSurveyUserInput ofSurveyUserInput = (OFSurveyUserInput) obj;
                    OFOneFlowSHP ofs1 = new OFOneFlowSHP(mContext);
                    OFThrottlingConfig config = ofs1.getThrottlingConfig();
                    OFHelper.v("OneFlow", "OneFlow globalThrottling inside else ["+(ofSurveyUserInput.getCreatedOn() < config.getActivatedAt())+"]");
                    if (ofSurveyUserInput.getCreatedOn() < config.getActivatedAt()) {
                        triggerSurvey(gslr, reserved);
                    }
                }

                break;
            case logUser:

                if (obj != null) {
                    OFLogUserResponse logUserResponse = (OFLogUserResponse) obj;
                    if (logUserResponse != null) {
                        // replacing current session id and user analytical id
                        OFOneFlowSHP ofs = new OFOneFlowSHP(mContext);
                        ofs.storeValue(OFConstants.SHP_LOG_USER_KEY, reserved);//ofs.getLogUserRequest().getSystem_id()); // system id stored for sending next app launch
                        ofs.clearLogUserRequest();
                        OFAddUserResultResponse aurr = ofs.getUserDetails();
                        //setting up new user analytical id

                        aurr.setAnalytic_user_id(logUserResponse.getAnalytic_user_id());
                        ofs.setUserDetails(aurr);
                        ofs.storeValue(OFConstants.SESSIONDETAIL_IDSHP, logUserResponse.getSessionId());

                        //storing this to support multi user survey
                        ofs.storeValue(OFConstants.USERUNIQUEIDSHP, reserved);

                        // mrh.onResponseReceived(hitType,null,0);
                        OFHelper.v("OneFlow", "OneFlow Log record inserted...");

                        //Updating old submitted surveys with logged user id.
                        //OFLogUserDBRepo.updateSurveyUserId(mContext, this, reserved, OFConstants.ApiHitType.updateSurveyIds);
                        new OFMyDBAsyncTask(mContext,this,OFConstants.ApiHitType.updateSurveyIds,false).execute(reserved);
                    } else {
                        // OFHelper.e("OneFlow", "OneFlow LogApi subimission failed logUser");
                        OFLogCountdownTimer.getInstance(mContext, 15000l, 5000l).start();
                        if (OFConstants.MODE.equalsIgnoreCase("dev")) {
                            OFHelper.makeText(mContext, reserved, 1);
                        }
                    }
                } else {
                    //  OFHelper.e("OneFlow", "OneFlow LogApi subimission failed logUser");
                    OFLogCountdownTimer.getInstance(mContext, 15000l, 5000l).start();
                }

                break;
            case updateSurveyIds:
                OFSurveyController.getInstance(mContext).getSurveyFromAPI();
                break;

        }
    }


    private OFConnectivity getConnectivityData() {
        OFConnectivity connectivity = new OFConnectivity();
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(mContext.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        TelephonyManager telephonyManager = ((TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE));
        String operatorName = telephonyManager.getNetworkOperatorName().isEmpty() ? null : telephonyManager.getNetworkOperatorName();

        if (activeNetwork != null) { // connected to the internet


            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                //connectivity.setWifi(true);
                connectivity.setRadio("wireless");
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                //connectivity.setWifi(false);
                //connectivity.setRadio("true");
                connectivity.setCarrier(operatorName);
            }
        } else {
            // not connected to the internet
            connectivity.setRadio("false");

        }
        return connectivity;
    }


    private void triggerSurvey(OFGetSurveyListResponse gslr, String eventName) {


        OFHelper.v("OneFlow","OneFlow eventName["+eventName+"]");
        Intent surveyIntent = null;
        if (gslr.getSurveySettings().getSdkTheme().getWidgetPosition() == null) {
            surveyIntent = new Intent(mContext.getApplicationContext(), activityName.get("bottom-center"));
        } else {
            surveyIntent = new Intent(mContext.getApplicationContext(), activityName.get(gslr.getSurveySettings().getSdkTheme().getWidgetPosition()));
        }

        OFOneFlowSHP ofs1 = new OFOneFlowSHP(mContext);

        HashMap<String, Object> mapValue = new HashMap<>();
        mapValue.put("survey_id", gslr.get_id());
        OFEventController ec = OFEventController.getInstance(mContext);
        ec.storeEventsInDB(OFConstants.AUTOEVENT_SURVEYIMPRESSION, mapValue, 0);

        ofs1.storeValue(OFConstants.SHP_SURVEY_RUNNING, true);
        ofs1.storeValue(OFConstants.SHP_SURVEYSTART, Calendar.getInstance().getTimeInMillis());


        OFThrottlingConfig config = ofs1.getThrottlingConfig();
        //flow correction and time should be in second
        if (config != null) {

            OFHelper.v("OneFlow","OneFlow throttling config not null");
            config.setActivated(true);
            config.setActivatedById(gslr.get_id());
            config.setActivatedAt(System.currentTimeMillis());

            new OFOneFlowSHP(mContext).setThrottlingConfig(config);

            setupGlobalTimerToDeactivateThrottlingLocally();
        } else{
            OFHelper.v("OneFlow","OneFlow throttling config null");
        }

        // resetting counter for similar type of event name
        //ofs1.storeValue(OFConstants.SHP_SURVEY_SEARCH_POSITION, 0);

        surveyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        surveyIntent.putExtra("SurveyType", gslr);
        surveyIntent.putExtra("eventName", eventName);

        mContext.getApplicationContext().startActivity(surveyIntent);
    }

    private void setupGlobalTimerToDeactivateThrottlingLocally() {


        OFHelper.v("OneFlow", "OneFlow deactivate called ");
        OFThrottlingConfig config = new OFOneFlowSHP(mContext).getThrottlingConfig();
        OFHelper.v("OneFlow", "OneFlow deactivate called config activated[" + config.isActivated() + "]globalTime["+config.getGlobalTime()+"]activatedBy["+config.getActivatedById()+"]");
        //OFMyCountDownTimerThrottling.getInstance(mContext,0l,0l).cancel();
        if(config.getGlobalTime()!=null && config.getGlobalTime()>0) {
            OFMyCountDownTimerThrottling.getInstance(mContext, config.getGlobalTime() * 1000, ((Long) (config.getGlobalTime() * 1000) / 2)).start();
        } else {
            OFHelper.v("OneFlow", "OneFlow deactivate called at else");
            config.setActivated(false);
            config.setActivatedById(null);
            new OFOneFlowSHP(mContext).setThrottlingConfig(config);
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
}
