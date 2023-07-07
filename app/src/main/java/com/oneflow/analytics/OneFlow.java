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

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
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
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ProcessLifecycleOwner;

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
import com.oneflow.analytics.model.adduser.OFAddUserContext;
import com.oneflow.analytics.model.adduser.OFAddUserReq;
import com.oneflow.analytics.model.adduser.OFAddUserResponse;
import com.oneflow.analytics.model.adduser.OFDeviceDetails;
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
import com.oneflow.analytics.repositories.OFEventAPIRepo;
import com.oneflow.analytics.repositories.OFEventDBRepoKT;
import com.oneflow.analytics.repositories.OFLogUserDBRepoKT;
import com.oneflow.analytics.repositories.OFLogUserRepo;
import com.oneflow.analytics.repositories.OFSurvey;
import com.oneflow.analytics.sdkdb.OFOneFlowSHP;
import com.oneflow.analytics.utils.OFConstants;
import com.oneflow.analytics.utils.OFDelayedSurveyCountdownTimer;
import com.oneflow.analytics.utils.OFHelper;
import com.oneflow.analytics.utils.OFLogCountdownTimer;
import com.oneflow.analytics.utils.OFMyCountDownTimer;
import com.oneflow.analytics.utils.OFMyResponseHandlerOneFlow;
import com.oneflow.analytics.utils.OFNetworkChangeReceiver;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.oneflow.analytics.utils.OFLogCountdownTimer;

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
            OFHelper.v("1Flow", "1Flow shouldShow1[" + shouldShow + "]");
            OFOneFlowSHP.getInstance(mContext).storeValue(OFConstants.SHP_SHOULD_SHOW_SURVEY, shouldShow);
        } catch (Exception ex) {
            OFHelper.e("1Flow", "1Flow error showSurvey1[" + ex.getMessage() + "]");
        }
    }

    public static void shouldPrintLog(Boolean shouldShow) {
        try {
            OFHelper.v("1Flow", "1Flow shouldShowLog[" + shouldShow + "]");
            OFOneFlowSHP.getInstance(mContext).storeValue(OFConstants.SHP_SHOULD_PRINT_LOG, shouldShow);
            OFHelper.commanLogEnable = shouldShow;
        } catch (Exception ex) {
            OFHelper.e("1Flow", "1Flow error showSurvey[" + ex.getMessage() + "]");
        }
    }


   /* public static void registerActivityCallback() {
        ((Application) mContext.getApplicationContext()).registerActivityLifecycleCallbacks(new OFActivityCallbacks());
    }*/

   /* public static void configure(Context mContext, String projectKey, OFFontSetup titleFont) {
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
    }*/
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

   /* public static void configure(Context mContext, String projectKey, OFFontSetup titleFont, OFFontSetup descriptionFont) {
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

    }*/


    public static void configure(Context mContext, String projectKey) {
        OFHelper.v("1Flow", "1Flow configure called project Key[" + projectKey + "]");
        if (!OFHelper.validateString(projectKey).equalsIgnoreCase("NA")) {
            OFOneFlowSHP fc = OFOneFlowSHP.getInstance(mContext);
            if (OFHelper.validateString(OFHelper.headerKey).equalsIgnoreCase("NA")) {// && !fc.getBooleanValue(OFConstants.AUTOEVENT_FIRSTOPEN,false)) {
                //if(!fc.getBooleanValue(OFConstants.AUTOEVENT_FIRSTOPEN,false)) {
                //ProcessLifecycleOwner.get().getLifecycle().addObserver(new AppLifecycleListener(mContext));
                configureLocal(mContext, projectKey);
            } else {
                OFHelper.e("1Flow", "Re-register called, Nothing happen");
            }
        } else {
            OFHelper.e("1Flow", "Empty project given");
        }
    }

    public static String fontNameStr = "";

    public static void useFont(String fontFileName) {
        fontNameStr = fontFileName;
    }


    /*public static void configure(Context mContext, String projectKey, String fontName) {
        OFHelper.v("1Flow", "1Flow configure called project Key [" + projectKey + "]strName["+fontName+"]");
        if (!OFHelper.validateString(projectKey).equalsIgnoreCase("NA")) {
            OFOneFlowSHP fc = OFOneFlowSHP.getInstance(mContext);
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
    }*/


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
                        OFHelper.v("InAppPurchase", "1Flow InAppPurchase Called");
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


        final OFOneFlowSHP ofs = OFOneFlowSHP.getInstance(mContext);

        // network listener and timer listener to make sure registered only once.
        if (!ofs.getBooleanValue(OFConstants.SHP_TIMER_LISTENER, false)) {
            OFMyCountDownTimer cmdt = OFMyCountDownTimer.getInstance(mContext, duration, interval);
            cmdt.start();
            ofs.storeValue(OFConstants.SHP_TIMER_LISTENER, true);
        }

        if (!ofs.getBooleanValue(OFConstants.SHP_NETWORK_LISTENER, false)) {
            OFHelper.v("1Flow", "1Flow network listener registered ");
            OFNetworkChangeReceiver ncr = new OFNetworkChangeReceiver();
            IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            mContext.registerReceiver(ncr, intentFilter);
            ofs.storeValue(OFConstants.SHP_NETWORK_LISTENER, true);
        }
        OFHelper.v("1Flow", "1Flow Throttling receiver[" + ofs.getBooleanValue(OFConstants.SHP_THROTTLING_RECEIVER, false) + "]");


        Thread confThread = new Thread() {
            @Override
            public void run() {
                super.run();
                Looper.prepare();


                OFHelper.v("1Flow", "1Flow configure called isConnected[" + OFHelper.isConnected(mContext) + "]");
                ofs.storeValue(OFConstants.APPIDSHP, projectKey);

                if (OFHelper.isConnected(mContext)) {
                    /*fc.getLocation();*/
                    OFHelper.headerKey = projectKey;
                    fc.registerUser();
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

                OFHelper.v("FeedbackController", "1Flow current version [" + OFConstants.currentVersion + "]old version [" + oldVersion + "]");


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
        OFHelper.v("1Flow", "1Flow confThread isAlive[" + confThread.isAlive() + "]");


        // this logic is required because config was also being called from network change initially
        if (!confThread.isAlive()) {
            Long lastHit = ofs.getLongValue(OFConstants.SHP_ONEFLOW_CONFTIMING);

            Long diff = 10l; // set default value 100 for first time
            Long currentTime = Calendar.getInstance().getTimeInMillis();
            diff = (currentTime - lastHit) / 1000;

            OFHelper.v("1Flow", "1Flow conf recordEvents diff [" + diff + "]currentTime[" + currentTime + "]lastHit[" + lastHit + "]readable[" + OFHelper.formatedDate(lastHit, "yyyy-MM-dd hh:mm:ss") + "]");
            if (!ofs.getBooleanValue(OFConstants.AUTOEVENT_FIRSTOPEN, false)) {
                ofs.storeValue(OFConstants.SHP_ONEFLOW_CONFTIMING, currentTime);
                confThread.start();
            } else if (lastHit == 0 || diff > 10) {//reduced to 10 sec as not hitting everytime
                OFHelper.v("1Flow", "1Flow conf inside if");
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

                OFHelper.v("FakeBillingClass", "1Flow payment billing disconnected");
            }
        });

    }

    /*BroadcastReceiver listFetched = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Helper.v("1Flow","1Flow reached receiver at OneFlow");
            if(intent.getAction().equalsIgnoreCase("survey_list_fetched")) {
                //ArrayList<GetSurveyListResponse> slr = new OneFlowSHP(mContext).getSurveyList();
                EventDBRepo.fetchEventsBeforSurvey(mContext,this, Constants.ApiHitType.fet);

            }else if(intent.getAction().equalsIgnoreCase("events_submitted")){
                //EventDBRepo.fetchEvents(FirstActivity.this, FirstActivity.this, Constants.ApiHitType.fetchEventsFromDB);

            }
        }
    };*/


    //private OFAddUserRequestNew createRequest() {
    private OFAddUserReq createRequest() {
        OFDeviceDetails dd = new OFDeviceDetails();
        dd.setUnique_id(OFHelper.getDeviceId(mContext));
        dd.setDevice_id(OFHelper.getDeviceId(mContext));
        dd.setOs("android");

        final OFOneFlowSHP ofs = OFOneFlowSHP.getInstance(mContext);


        HashMap<String, String> device = new HashMap<>();
        device.put("manufacturer", Build.MANUFACTURER);
        device.put("model", Build.DEVICE);

        String app_ver = "";
        try {
            app_ver = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;

        } catch (PackageManager.NameNotFoundException e) {

            app_ver = "";
        }
        String app_ver_code = "";
        try {
            app_ver_code = String.valueOf(mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode);

        } catch (PackageManager.NameNotFoundException e) {

            app_ver_code = "";
        }

        HashMap<String, String> app = new HashMap<>();
        app.put("version", app_ver);
        app.put("build", app_ver_code);

        HashMap<String, String> library = new HashMap<>();
        library.put("version", OFConstants.currentVersion);
        library.put("name", "1flow-android-sdk");


        OFConnectivity connectivity = getConnectivityData();

        HashMap<String, Object> network = new HashMap<>();
        network.put("carrier", connectivity.getCarrier());
        //network.put("wifi",connectivity.getRadio()!=null?connectivity.getRadio():false);
        network.put("wifi", connectivity.getRadio() != null ? true : false);

        HashMap<String, String> os = new HashMap<>();
        os.put("name", "android");
        os.put("version", String.valueOf(Build.VERSION.SDK_INT));


        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        HashMap<String, String> screen = new HashMap<>();
        screen.put("width", String.valueOf(metrics.widthPixels));
        screen.put("height", String.valueOf(metrics.heightPixels));
        screen.put("type", isTablet(mContext));


        /*AddUserContext auc = new AddUserContext();

        auc.setDevice(device);
        auc.setApp(app);
        auc.setLibrary(library);
        auc.setNetwork(network);
        auc.setOs(os);
        auc.setScreen(screen);*/

        OFAddUserContext ofau = new OFAddUserContext(app, device, library, network, screen, os);

        /*OFAddUserRequestNew aur = new OFAddUserRequestNew();
        aur.setUser_id(OFHelper.getDeviceId(mContext));
        aur.setAddUserContext(ofau);*/

        OFAddUserReq aur = new OFAddUserReq(OFHelper.getDeviceId(mContext), ofau);

        return aur;
    }

    public String isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large) ? "Tablet" : "Mobile";
    }


    private void registerUser() {

        //registerUser(createRequest());
        // checking old event if delete is pending
        //Boolean isDeletePending = OFOneFlowSHP.getInstance(mContext).getBooleanValue(OFConstants.SHP_EVENTS_DELETE_PENDING,false);
        OFHelper.v("1Flow", "1Flow called register user");
       /* if(!isDeletePending) {
            new OFEventDBRepoKT().fetchEvents(mContext, this, OFConstants.ApiHitType.fetchEventsFromDBBeforeConfig);
        }else{*/
        OFAddUserRepo.addUser(OFOneFlowSHP.getInstance(mContext).getStringValue(OFConstants.APPIDSHP), createRequest(), this, OFConstants.ApiHitType.CreateUser);
        //}


    }


    public static void startFlow(String surveyId) {
        OFOneFlowSHP shp = OFOneFlowSHP.getInstance(mContext);
        try {
            String userId = shp.getUserDetails().getAnalytic_user_id();
            if (!OFHelper.validateString(userId).equalsIgnoreCase("na")) {
                OneFlow of = new OneFlow(mContext);
                of.initDirectSurvey(userId, surveyId);
            } else {
                OFHelper.v("1Flow", "1Flow no survey available");
            }
        } catch (Exception ex) {
           // ex.printStackTrace();
        }

    }

    public void initDirectSurvey(String userId, String surveyID) {
        OFSurvey.getSurveyWithoutCondition(mContext, this, surveyID, OFConstants.ApiHitType.directSurvey, userId, OFConstants.currentVersion);
    }


    static Map<String, Object> eventMap = new HashMap<>();
   /* private void createSession(OFCreateSessionRequest csr) {
        //sdkdb = Room.databaseBuilder(context, SDKDB.class,"one-flow-db").build();
        OFCreateSession.createSession(OFOneFlowSHP.getInstance(mContext).getStringValue(OFConstants.APPIDSHP), csr, this, OFConstants.ApiHitType.CreateSession);
    }*/
   public static void recordEvents(String eventName) {
       recordEvents(eventName,null);
   }
    /**
     * Record events on any user action. This method will recognize if any survey is available against this event name
     *
     * @param eventName   : to recognize event and start survey if have any
     * @param eventValues : will accept HashMap<String,Object>
     */
    public static void recordEvents(String eventName, HashMap eventValues) {


        OFHelper.v("1Flow", "1Flow recordEvents record called with[" + eventName + "]at[" + OFHelper.formatedDate(System.currentTimeMillis(), "dd-MM-yyyy hh:mm:ss.SSS") + "]");

        try {
            if (!OFHelper.validateString(eventName.trim()).equalsIgnoreCase("NA")) {
                OneFlow of = new OneFlow(mContext);
                of.checkThrottlingLife();
                // this 'if' is for converting date object to second format(timestamp)
                if (eventValues != null) {
                    eventValues = OFHelper.checkDateInHashMap(eventValues);
                }
                OFHelper.v("1Flow", "1Flow recordEvents record called with[" + eventValues + "]");
                if (mContext != null) {
                    // storage, api call and check survey if available.
                    //EventController.getInstance(mContext).storeEventsInDB(eventName, eventValues, 0);
                    OFEventController ec = OFEventController.getInstance(mContext);
                    ec.storeEventsInDB(eventName, eventValues, 0);

                    eventMap = new HashMap<>();
                    eventMap.put("name", eventName);
                    eventMap.put("parameters", eventValues);
                    eventMap.put("timestamp", System.currentTimeMillis() / 1000);

                    //Checking if any survey available under coming event.
                    //of.checkSurveyTitleAndScreensInBackground(OFConstants.ApiHitType.checkResurveyNSubmission, eventName);
                    of.triggerSurveyNew(eventName);

                } else {
                    OFHelper.v("1Flow", "1Flow null context for event");
                }
            } else {
                OFHelper.v("1Flow", "1Flow empty event unable to trigger survey");
            }
        } catch (Exception ex) {

        }
    }

    public static void recordEventsWithoutSurvey(String eventName, HashMap eventValues) {

        OFHelper.v("1Flow", "1Flow recordEvents record called without firing survey with[" + eventName + "]at[" + OFHelper.formatedDate(System.currentTimeMillis(), "dd-MM-yyyy hh:mm:ss.SSS") + "]");

        try {
            OneFlow of = new OneFlow(mContext);
            of.checkThrottlingLife(); //checking throttling life span if activated.
            // this 'if' is for converting date object to second format(timestamp)
            if (eventValues != null) {
                eventValues = OFHelper.checkDateInHashMap(eventValues);
            }

            OFHelper.v("1Flow", "1Flow recordEvents record called without firing survey with[" + eventValues + "]");

            if (mContext != null) {
                // storage, api call and check survey if available.
                //EventController.getInstance(mContext).storeEventsInDB(eventName, eventValues, 0);
                OFEventController ec = OFEventController.getInstance(mContext);
                ec.storeEventsInDB(eventName, eventValues, 0);


                //Checking if any survey available under coming event.
                //of.checkSurveyTitleAndScreensInBackground(OFConstants.ApiHitType.checkResurveyNSubmission, eventName);

            } else {
                OFHelper.v("1Flow", "1Flow null context for event");
            }
        } catch (Exception ex) {

        }
    }

    /*public static void logUser(String uniqueId, HashMap<String, String> mapValue) {
        if (OFHelper.isConnected(mContext)) {

            OFAddUserResultResponse aurr = OFOneFlowSHP.getInstance(mContext).getUserDetails();
            if (aurr != null) {
                OFHelper.v("1Flow", "1Flow logUser data stored");
                OFLogUserRequest lur = new OFLogUserRequest();
                lur.setSystem_id(uniqueId);
                lur.setAnonymous_user_id(OFOneFlowSHP.getInstance(mContext).getUserDetails().getAnalytic_user_id());
                lur.setParameters(mapValue);
                lur.setSession_id(OFOneFlowSHP.getInstance(mContext).getStringValue(OFConstants.SESSIONDETAIL_IDSHP));
                OFOneFlowSHP.getInstance(mContext).setLogUserRequest(lur);
                // this api calling shifted to send Event api response
            }
            sendEventsToApi(mContext);
        }
    }*/
    static boolean logUserPending = false;

    /**
     * This method will help to recognize user. Below mentioned 2 values will be required
     *
     * @param uniqueId   : to identify user uniquely, it could be e-mail id or any thing.
     * @param userDetail : data related to user.
     */
    public static void logUser(String uniqueId, HashMap<String, Object> userDetail) {
        OFHelper.v("1Flow", "1Flow logUser data stored 0");
        // User id must not be empty
        if (OFHelper.validateString(uniqueId).equalsIgnoreCase("NA")) {

            OFHelper.e("1Flow LogUser Error", "1Flow User id must not be empty to log user");
           /* String str = null;
            Log.v("1Flow","Application str.split["+str.substring(5)+"]");*/
        } else {
            if (OFHelper.isConnected(mContext)) {
                if (userDetail != null) {
                    userDetail = OFHelper.checkDateInHashMap(userDetail);
                }
                OFHelper.v("1Flow", "1Flow logUser data stored 1");
                OFAddUserResponse aurr = OFOneFlowSHP.getInstance(mContext).getUserDetails();
                OFLogUserRequest lur = new OFLogUserRequest();
                lur.setUser_id(uniqueId);//"rqqVmpdHc9QsKbZuz9P5YqPaEb23");//aurr.getAnalytic_user_id());
                lur.setParameters(userDetail);
                if (aurr != null) {
                    OFHelper.v("1Flow", "1Flow logUser data stored 2");
                    lur.setAnonymous_user_id(aurr.getAnalytic_user_id());
                    // this api calling shifted to send Event api response
                }


                OFOneFlowSHP.getInstance(mContext).setLogUserRequest(lur);
                OFHelper.v("1Flow", "1Flow createUserRunning Status[" + aurr + "]");
                if (aurr != null) {
                    sendEventsToApi(mContext);
                } else {
                    //this logic added to makesure that logUser is called after create user.Please check create user response.
                    logUserPending = true;
                    OFHelper.v("1Flow", "1Flow logUser not calling as config pending");
                }
            }
        }
    }

    /**
     * This method will check all aspects of re-survey
     *
     * @return SurveyList to check
     */
    private OFGetSurveyListResponse shouldReturnSurvey(OFGetSurveyListResponse gslr) {

        Long submitTime = OFOneFlowSHP.getInstance(mContext).getLongValue(gslr.get_id());
        OFHelper.v("1Flow", "1Flow resurvey check[" + submitTime + "]");
        if (submitTime > 0) {
            //Checking offline storage of survey

            try {
                OFHelper.v("1Flow", "1Flow resurvey check option[" + gslr.getSurveySettings().getResurvey_option() + "]current[" + Calendar.getInstance().getTimeInMillis() + "]");
                if (gslr.getSurveySettings().getResurvey_option()) {
                    Long totalInterval = 0l;
                    Long diff = Calendar.getInstance().getTimeInMillis() - submitTime;
                    int diffDuration = 0;
                    OFHelper.v("1Flow", "1Flow resurvey check diff[" + diff + "]retakeInputValue[" + gslr.getSurveySettings().getRetake_survey().getRetake_input_value() + "]");
                    OFHelper.v("1Flow", "1Flow resurvey check retakeSelectValue[" + gslr.getSurveySettings().getRetake_survey().getRetake_select_value() + "]");
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
                            OFHelper.v("1Flow", "1Flow retake_select_value is neither of minutes, hours or days");
                    }
                    OFHelper.v("1Flow", "1Flow resurvey check diffDuration[" + diffDuration + "]totalInterval[" + totalInterval + "]");
                    if (diffDuration > totalInterval) {
                        return gslr;
                    } else {
                        return null;
                    }

                } else {
                    OFHelper.v("1Flow", "1Flow ResurveyOption[false]");
                    return null;
                }
            } catch (Exception ex) {
               // ex.printStackTrace();
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
        OFOneFlowSHP ofs = OFOneFlowSHP.getInstance(mContext);
        OFHelper.v("1Flow", "1Flow checkSurveyTitleAndScreens[" + ofs.getBooleanValue(OFConstants.SHP_SHOULD_SHOW_SURVEY, true) + "]");
        if (ofs.getBooleanValue(OFConstants.SHP_SHOULD_SHOW_SURVEY, true)) {
            ArrayList<OFGetSurveyListResponse> slr = ofs.getSurveyList();
            OFGetSurveyListResponse gslr = null;
            //ArrayList<SurveyScreens> surveyScreens = null;

            int counter = 0;
            String tag = this.getClass().getName();

            if (slr != null) {
                OFHelper.v(tag, "1Flow list size[" + slr.size() + "]type[" + type + "]");
                for (OFGetSurveyListResponse item : slr) {
                    OFHelper.v(tag, "1Flow list size 0 [" + item.getTrigger_event_name() + "]type[" + type + "]");
                    String[] eventName = item.getTrigger_event_name().split(",");
                    boolean recordFound = false;
                    for (String name : eventName) {
                        if (name.contains(type)) {
                            gslr = item;
                            OFHelper.v(tag, "1Flow survey found on event name[" + type + "]");
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
     * @param hitType
     * @return
     */


    private void checkSurveyTitleAndScreensInBackground(OFConstants.ApiHitType hitType, String firedEventName) {
        OFOneFlowSHP ofs = OFOneFlowSHP.getInstance(mContext);
        OFHelper.v("1Flow", "1Flow checkSurveyTitleAndScreens[" + ofs.getBooleanValue(OFConstants.SHP_SHOULD_SHOW_SURVEY, true) + "]");
        if (ofs.getBooleanValue(OFConstants.SHP_SHOULD_SHOW_SURVEY, true)) {
            ArrayList<OFGetSurveyListResponse> slr = ofs.getSurveyList();
            OFGetSurveyListResponse gslr = null;
            //ArrayList<SurveyScreens> surveyScreens = null;

            int counter = ofs.getIntegerValue(OFConstants.SHP_SURVEY_SEARCH_POSITION);
            OFHelper.v("1Flow", "1Flow counter value before[" + counter + "]");
            String tag = this.getClass().getName();

            if (slr != null) {
                OFHelper.v(tag, "1Flow list size[" + slr.size() + "]firedEventName[" + firedEventName + "]");
                OFGetSurveyListResponse item = null;
                for (int i = 0; i < slr.size(); i++) {

                    OFHelper.v(tag, "1Flow list searching at[" + i + "]");
                    if (i < counter) continue;

                    item = slr.get(i);

                    counter++;
                    OFHelper.v(tag, "1Flow list size 0 [" + item.getTrigger_event_name() + "]firedEventName[" + firedEventName + "]");
                    String[] eventName;
                    boolean recordFound = false;
                    //=====TempCode====
                    if (!OFHelper.validateString(item.getTrigger_event_name()).equalsIgnoreCase("na")) {
                        eventName = item.getTrigger_event_name().split(",");
                    } else {
                        eventName = new String[1];
                        eventName[0] = item.getSurveySettings().getTriggerFilters().get(0).getField();
                    }
                    OFHelper.v(tag, "1Flow list size eventName [" + Arrays.asList(eventName) + "]");
                    //======TempCode=======
                    for (String name : eventName) {
                        if (name.contains(firedEventName)) {
                            gslr = item;
                            OFHelper.v(tag, "1Flow survey found on event name[" + firedEventName + "]");
                            recordFound = true;
                            break;
                        }
                    }

                    if (recordFound) {
                        break;
                    } else {
                        OFHelper.v(tag, "1Flow survey not found for [" + firedEventName + "] ");
                    }
                }
            } /*else {
            Helper.makeText(mContext, "Configure project first", 1);
        }*/

            OFHelper.v("1Flow", "1Flow counter value after[" + counter + "]size[" + slr.size() + "]");
            if (counter < slr.size()) {
                ofs.storeValue(OFConstants.SHP_SURVEY_SEARCH_POSITION, counter);
            } else {
                ofs.storeValue(OFConstants.SHP_SURVEY_SEARCH_POSITION, 0);
            }
            //Resurvey login
            if (gslr == null) {
                onResponseReceived(hitType, null, 0l, "", null, null);
            } else {
                new OFLogUserDBRepoKT().findSurveyForID(mContext, this, OFConstants.ApiHitType.fetchSubmittedSurvey, gslr, gslr.get_id(), OFOneFlowSHP.getInstance(mContext).getStringValue(OFConstants.USERUNIQUEIDSHP), firedEventName);
                //new OFMyDBAsyncTask(mContext,this,OFConstants.ApiHitType.fetchSubmittedSurvey,false).execute(gslr, OFOneFlowSHP.getInstance(mContext).getStringValue(OFConstants.USERUNIQUEIDSHP), firedEventName);
            }
        }
    }


   /* public void getProjectDetails() {
        OFProjectDetails.getProject(mContext);
    }*/

    public static void sendEventsToApi(Context contex) {
        OneFlow fc = new OneFlow(contex);
        //OFEventDBRepo.fetchEvents(mContext, fc, OFConstants.ApiHitType.fetchEventsFromDB);
        new OFEventDBRepoKT().fetchEvents(mContext, fc, OFConstants.ApiHitType.fetchEventsFromDB);
    }


    OFGetSurveyListResponse gslrGlobal;
    static int createUserCounter = 0;

    @Override
    public void onResponseReceived(OFConstants.ApiHitType hitType, Object obj, Long reserve, String reserved, Object obj2, Object obj3) {
        OFHelper.v("1Flow", "1Flow onReceived type[" + hitType + "]reserve[" + reserve + "]");
        switch (hitType) {


            case fetchEventsFromDBBeforeConfig:
                if (obj != null) {
                    ArrayList<OFRecordEventsTab> list = (ArrayList<OFRecordEventsTab>) obj;
                    OFHelper.v("1Flow", "1Flow checking older events[" + list.size() + "]");
                    //Preparing list to send api
                    if (list.size() > 0) {
                        Integer[] ids = new Integer[list.size()];
                        int i = 0;
                        ArrayList<OFRecordEventsTabToAPI> retListToAPI = new ArrayList<>();
                        OFRecordEventsTabToAPI retMain;
                        for (OFRecordEventsTab ret : list) {
                            ids[i++] = ret.getId();
                        }

                        new OFEventDBRepoKT().deleteEvents(mContext, ids, this, OFConstants.ApiHitType.deleteEventsFromDBLastSession);

                    } else {
                        OFHelper.v("1Flow", "1Flow checking older events not found hitting adduser");
                        OFAddUserRepo.addUser(OFOneFlowSHP.getInstance(mContext).getStringValue(OFConstants.APPIDSHP), createRequest(), this, OFConstants.ApiHitType.CreateUser);
                    }
                } else {
                    OFHelper.v("1Flow", "1Flow checking older events not found hitting adduser.");
                    OFAddUserRepo.addUser(OFOneFlowSHP.getInstance(mContext).getStringValue(OFConstants.APPIDSHP), createRequest(), this, OFConstants.ApiHitType.CreateUser);
                }
                break;

            case deleteEventsFromDBLastSession:
                OFHelper.v("1Flow", "1Flow checking older events deleted hitting adduser");
                OFOneFlowSHP.getInstance(mContext).storeValue(OFConstants.SHP_EVENTS_DELETE_PENDING, false);
                OFAddUserRepo.addUser(OFOneFlowSHP.getInstance(mContext).getStringValue(OFConstants.APPIDSHP), createRequest(), this, OFConstants.ApiHitType.CreateUser);

                break;

            case CreateUser:

                if (obj != null) {
                    createUserCounter=0;
                    OFAddUserResponse userResponse = (OFAddUserResponse) obj;
                    OFOneFlowSHP oneFlowSHP = OFOneFlowSHP.getInstance(mContext);
                    oneFlowSHP.setUserDetails(userResponse);


                    String app_ver = "";
                    try {
                        app_ver = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;

                    } catch (PackageManager.NameNotFoundException e) {

                        app_ver = "";
                    }

                    if (!oneFlowSHP.getBooleanValue(OFConstants.AUTOEVENT_FIRSTOPEN, false)) {

                        HashMap<String, Object> mapValue = new HashMap<>();
                        mapValue.put("app_version", app_ver);//OFConstants.currentVersion);confirmed from rohan on 7th july 2023
                        recordEvents(OFConstants.AUTOEVENT_FIRSTOPEN, mapValue);
                        oneFlowSHP.storeValue(OFConstants.AUTOEVENT_FIRSTOPEN, true);
                    }



                    HashMap<String, Object> mapValueSession = new HashMap<>();
                    mapValueSession.put("library_version", OFConstants.currentVersion);
                    mapValueSession.put("app_version", app_ver);

                    //if (!oneFlowSHP.getBooleanValue(OFConstants.AUTOEVENT_SESSIONSTART, false)) {
                    OFEventController ec = OFEventController.getInstance(mContext);
                    ec.storeEventsInDB(OFConstants.AUTOEVENT_SESSIONSTART, mapValueSession, 0);

                    oneFlowSHP.storeValue(OFConstants.AUTOEVENT_SESSIONSTART, true);
                    ProcessLifecycleOwner.get().getLifecycle().addObserver(new AppLifecycleListener(mContext));
                    /*} else {
                        OFHelper.v("1Flow", "1Flow app is in start_session already recorded.");
                    }*/

                    OFLogUserRequest lur = oneFlowSHP.getLogUserRequest();

                    long diff = System.currentTimeMillis()-oneFlowSHP.getLongValue(OFConstants.SHP_CACHE_FILE_UPDATE_TIME);
                    OFHelper.v("1Flow", "1Flow create user finish cache file life span diff[" + diff + "]["+(diff>OFConstants.cacheFileLifeSpan)+"]");
                    if(diff>OFConstants.cacheFileLifeSpan) {
                        new OFCacheHandler(mContext).start();
                    }

                    OFHelper.v("1Flow", "1Flow create user finish hitting pending logUser[" + lur + "]");
                    if (lur != null && logUserPending) {
                        logUserPending = false;
                        lur.setAnonymous_user_id(userResponse.getAnalytic_user_id());
                        //lur.setAnonymous_user_id(userResponse);
                        OFLogUserRepo.logUser(OFOneFlowSHP.getInstance(mContext).getStringValue(OFConstants.APPIDSHP), lur, this, OFConstants.ApiHitType.logUser);


                    } else {
                        //calling fetch survey api on ADD USER success changed on 17-01-23
                        OFSurveyController.getInstance(mContext).getSurveyFromAPI();
                    }
                } else {
                    OFHelper.headerKey = "";

                    OFHelper.v("1Flow", "1Flow create user finish failed calling again[" + createUserCounter + "]");
                    if(createUserCounter<1){
                        createUserCounter++;
                            registerUser();
                    }else {
                        createUserCounter = 0;
                        Intent intent = new Intent("survey_list_fetched");
                        intent.putExtra("msg", "User Not Created");//"Invalid project key");
                        mContext.sendBroadcast(intent);
                    }
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
                        OFOneFlowSHP oneFlowSHP = OFOneFlowSHP.getInstance(mContext);

                        OFHelper.v("1Flow", "1Flow checking firstOpen [" + oneFlowSHP.getBooleanValue(OFConstants.AUTOEVENT_FIRSTOPEN, false) + "]");
                        if (!oneFlowSHP.getBooleanValue(OFConstants.AUTOEVENT_FIRSTOPEN, false)) {

                            HashMap<String, Object> mapValue = new HashMap<>();
                            mapValue.put("app_version", OFConstants.currentVersion);
                            recordEvents(OFConstants.AUTOEVENT_FIRSTOPEN, mapValue);
                            oneFlowSHP.storeValue(OFConstants.AUTOEVENT_FIRSTOPEN, true);
                        }


                       /* oneFlowSHP.storeValue(OFConstants.SESSIONDETAIL_IDSHP, createSession.get_id());
                        oneFlowSHP.storeValue(OFConstants.SESSIONDETAIL_SYSTEM_IDSHP, createSession.getSystem_id());*/

                        //calling fetch survey api on create session success
                        OFSurveyController.getInstance(mContext).getSurveyFromAPI();
                    } else {
                        OFHelper.headerKey = "";
                        if (OFConstants.MODE.equalsIgnoreCase("dev")) {
                            OFHelper.makeText(mContext, reserved, 1);
                        }
                    }
                } else {
                    OFHelper.e("1Flow", "1Flow subimission failed CreateSession");
                }

                break;

            case fetchEventsFromDB:

                OFHelper.v("FeedbackController", "1Flow checking before log fetchEventsFromDB came back");

                //OneFlow fc = new OneFlow(mContext);

                OFOneFlowSHP ofshp = OFOneFlowSHP.getInstance(mContext);
                if (obj != null) {
                    ArrayList<OFRecordEventsTab> list = (ArrayList<OFRecordEventsTab>) obj;
                    OFHelper.v("FeedbackController", "1Flow checking before log fetchEventsFromDB list received size[" + list.size() + "]");
                    //Preparing list to send api
                    if (list.size() > 0) {
                        Integer[] ids = new Integer[list.size()];
                        int i = 0;
                        ArrayList<OFRecordEventsTabToAPI> retListToAPI = new ArrayList<>();
                        OFRecordEventsTabToAPI retMain;
                        for (OFRecordEventsTab ret : list) {
                            retMain = new OFRecordEventsTabToAPI();
                            retMain.setEventName(ret.getEventName());
                            retMain.set_id(ret.getUuid());
                            retMain.setTime(ret.getTime());
                            retMain.setPlatform("a");
                            retMain.setDataMap(ret.getDataMap());
                            retListToAPI.add(retMain);
                            ids[i++] = ret.getId();
                        }

                        if (!OFHelper.validateString(ofshp.getUserDetails().getAnalytic_user_id()).equalsIgnoreCase("NA")) {
                            OFEventAPIRequest ear = new OFEventAPIRequest();
                            ear.setUserId(ofshp.getUserDetails().getAnalytic_user_id());
                            ear.setEvents(retListToAPI);

                            OFHelper.v("1Flow", "1Flow checking before log fetchEventsFromDB request prepared");


                            ofshp.storeValue(OFConstants.SHP_EVENTS_DELETE_PENDING, true);

                            OFEventAPIRepo.sendLogsToApi(OFOneFlowSHP.getInstance(mContext).getStringValue(OFConstants.APPIDSHP), ear, OneFlow.this, OFConstants.ApiHitType.sendEventsToAPI, ids);

                        }
                    } else {

                        OFLogUserRequest lur = ofshp.getLogUserRequest();
                        OFHelper.e("1Flow", "1Flow checking No event available hitting log[" + lur + "]");
                        if (lur != null) {
                            OFLogUserRepo.logUser(OFOneFlowSHP.getInstance(mContext).getStringValue(OFConstants.APPIDSHP), lur, this, OFConstants.ApiHitType.logUser);
                        }
                    }
                } else {
                    OFHelper.e("1Flow", "1Flow subimission failed fetchedEvents");
                }
                break;
            case sendEventsToAPI:
                //Events has been sent to api not deleting local records
                Integer[] ids1 = (Integer[]) obj;
                //OFEventDBRepo.deleteEvents(mContext, ids1, this, OFConstants.ApiHitType.deleteEventsFromDB);
                new OFEventDBRepoKT().deleteEvents(mContext, ids1, this, OFConstants.ApiHitType.deleteEventsFromDB);

                break;
            case deleteEventsFromDB:
                OFHelper.v("1flow", "1Flow checking events submitted hitting logs delete count[" + ((Integer) obj) + "]");
                Intent intent = new Intent("events_submitted");
                intent.putExtra("size", String.valueOf((Integer) obj));
                mContext.sendBroadcast(intent);


                OFOneFlowSHP.getInstance(mContext).storeValue(OFConstants.SHP_EVENTS_DELETE_PENDING, false);

                OFLogUserRequest lur = OFOneFlowSHP.getInstance(mContext).getLogUserRequest();
                OFHelper.v("1flow", "1Flow checking events submitted hitting logUser[" + lur + "]");
                if (lur != null) {
                    OFLogUserRepo.logUser(OFOneFlowSHP.getInstance(mContext).getStringValue(OFConstants.APPIDSHP), lur, this, OFConstants.ApiHitType.logUser);
                }
                break;
            case fetchSubmittedSurvey:
                if (obj != null) {
                    OFGetSurveyListResponse gslr = (OFGetSurveyListResponse) obj;
                    Long createdOn = (Long) obj2;
                    if (createdOn > 0) {
                        //Checking offline storage of survey

                        try {
                            OFHelper.v("1Flow", "1Flow resurvey check option[" + gslr.getSurveySettings().getResurvey_option() + "]current[" + Calendar.getInstance().getTimeInMillis() + "]");
                            if (gslr.getSurveySettings().getResurvey_option()) {
                                Long totalInterval = 0l;
                                Long diff = Calendar.getInstance().getTimeInMillis() - createdOn;
                                int diffDuration = 0;
                                OFHelper.v("1Flow", "1Flow resurvey check diff[" + diff + "]retakeInputValue[" + gslr.getSurveySettings().getRetake_survey().getRetake_input_value() + "]");
                                OFHelper.v("1Flow", "1Flow resurvey check retakeSelectValue[" + gslr.getSurveySettings().getRetake_survey().getRetake_select_value() + "]");
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
                                        OFHelper.v("1Flow", "1Flow retake_select_value is neither of minutes, hours or days");
                                }
                                OFHelper.v("1Flow", "1Flow resurvey check diffDuration[" + diffDuration + "]totalInterval[" + totalInterval + "]");
                                if (diffDuration > totalInterval) {
                                    String eventName = (String) obj3;
                                    onResponseReceived(OFConstants.ApiHitType.checkResurveyNSubmission, gslr, 0l, eventName, null, null);
                                }
                            } else {

                                int searchPostion = OFOneFlowSHP.getInstance(mContext).getIntegerValue(OFConstants.SHP_SURVEY_SEARCH_POSITION);

                                String eventName = (String) obj3;
                                // as this survey not fired, going to check again if any other survey available with same event name
                                OFHelper.v("1Flow", "1Flow resurvey failed checking list again [" + eventName + "]");
                                if (searchPostion > 0) { // this login add to stop re-iteration
                                    checkSurveyTitleAndScreensInBackground(OFConstants.ApiHitType.checkResurveyNSubmission, eventName);
                                }
                            }
                        } catch (Exception ex) {
                          //  ex.printStackTrace();

                        }
                    } else {

                        String eventName = (String) obj3;
                        OFHelper.v("1Flow", "1Flow no survey found show directly eventName[" + eventName + "]");
                        onResponseReceived(OFConstants.ApiHitType.checkResurveyNSubmission, gslr, 0l, eventName, null, null);
                    }
                } else {
                    OFHelper.e("1Flow", "1Flow subimission failed fetchSubmittedSurvey");
                }
                break;
            case checkResurveyNSubmission:
                if (obj != null) {
                    OFGetSurveyListResponse gslr = (OFGetSurveyListResponse) obj;
                    if (gslr != null) {
                        OFHelper.v("1flow", "1Flow resurvey checked survey found surveyItem[" + new Gson().toJson(gslr) + "]event name[" + reserved + "]");

                        OFOneFlowSHP ofs = OFOneFlowSHP.getInstance(mContext);

                        // resetting counter for similar type of event name
                        ofs.storeValue(OFConstants.SHP_SURVEY_SEARCH_POSITION, 0);
                        if (gslr.getScreens() != null) {

                            if (gslr.getScreens().size() > 0) {
                                OFHelper.v("1Flow", "1Flow resurvey checked running survey[" + (!ofs.getBooleanValue(OFConstants.SHP_SURVEY_RUNNING, false)) + "]");
                                if (!ofs.getBooleanValue(OFConstants.SHP_SURVEY_RUNNING, false)) {

                                    ArrayList<String> closedSurveyList = ofs.getClosedSurveyList();

                                    boolean hasClosed = false;
                                    if (closedSurveyList != null) {
                                        hasClosed = closedSurveyList.contains(gslr.get_id());
                                    }
                                    OFHelper.v("1Flow", "1Flow closed survey[" + hasClosed + "][" + gslr.getSurveySettings().getClosedAsFinished() + "]position[" + gslr.getSurveySettings().getSdkTheme().getWidgetPosition() + "]");
                                    if (!(gslr.getSurveySettings().getClosedAsFinished() && hasClosed)) { // this is for empty closed survey

                                        setUpHashForActivity();

                                        triggerSurvey(gslr, reserved);

                                        /*
                                        //throttling condition below
                                        OFThrottlingConfig throttlingConfig = ofs.getThrottlingConfig();
                                        if (throttlingConfig == null) {
                                            triggerSurvey(gslr, reserved);
                                        } else {

                                            OFHelper.v("1Flow", "1Flow globalThrottling[" + gslr.getSurveySettings().getOverrideGlobalThrottling() + "]throttlingConfig isActivated[" + throttlingConfig.isActivated() + "]");


                                            if (gslr.getSurveySettings().getOverrideGlobalThrottling()) {
                                                triggerSurvey(gslr, reserved);
                                            } else {
                                                if (throttlingConfig.isActivated()) {
                                                    if (throttlingConfig.getActivatedById().equalsIgnoreCase(gslr.get_id())) {

                                                        OFHelper.v("1Flow", "1Flow globalThrottling id matched ");
                                                        gslrGlobal = gslr;
                                                        // check in submitted survey list locally if this survey has been submitted then false
                                                        new OFLogUserDBRepoKT().findLastSubmittedSurveyID(mContext, this, OFConstants.ApiHitType.lastSubmittedSurvey, reserved);

//                                                        new OFMyDBAsyncTask(mContext,this, OFConstants.ApiHitType.lastSubmittedSurvey,false).execute();

                                                    }
                                                    //else nothing to do
                                                } else {
                                                    triggerSurvey(gslr, reserved);
                                                }
                                            }

                                        }*/
                                    } else {
                                        OFHelper.v("1Flow", "1Flow resurvey this survey already submitted searching again");
                                        // as this survey not fired, going to check again if any other survey available with same event name
                                        //  checkSurveyTitleAndScreensInBackground(OFConstants.ApiHitType.checkResurveyNSubmission,reserved);//reserved is event name
                                    }
                                }
                            }
                        }
                    }
                } else {
                    OFHelper.e("1Flow", "1Flow subimission failed checkResurveyNSubmission");
                }
                break;

            case lastSubmittedSurvey:
                OFHelper.v("1Flow", "1Flow globalThrottling received[" + obj + "]");

                OFGetSurveyListResponse gslr = gslrGlobal;
                if (obj == null) {
                    triggerSurvey(gslr, reserved);
                } else {
                    OFSurveyUserInput ofSurveyUserInput = (OFSurveyUserInput) obj;
                    OFOneFlowSHP ofs1 = OFOneFlowSHP.getInstance(mContext);
                    OFThrottlingConfig config = ofs1.getThrottlingConfig();
                    OFHelper.v("1Flow", "1Flow globalThrottling inside else [" + (ofSurveyUserInput.getCreatedOn() < config.getActivatedAt()) + "]");
                    if (ofSurveyUserInput.getCreatedOn() < config.getActivatedAt()) {
                        triggerSurvey(gslr, reserved);
                    }
                }

                break;
            case logUser:
                logUserPending = false;
                if (obj != null) {
                    OFLogUserResponse logUserResponse = (OFLogUserResponse) obj;
                    if (logUserResponse != null) {
                        // replacing current session id and user analytical id
                        OFOneFlowSHP ofs = OFOneFlowSHP.getInstance(mContext);
                        //ofs.storeValue(OFConstants.SHP_LOG_USER_KEY, reserved);//ofs.getLogUserRequest().getSystem_id()); // system id stored for sending next app launch
                        ofs.clearLogUserRequest();
                        OFAddUserResponse aurr = ofs.getUserDetails();
                        //setting up new user analytical id

                        //testing for multiple app launches
                        ofs.storeValue(OFConstants.SHP_DEVICE_UNIQUE_ID, reserved);//logUserResponse.getAnalytic_user_id());

                        aurr.setAnalytic_user_id(logUserResponse.getAnalytic_user_id());
                        ofs.setUserDetails(aurr);
                        //ofs.storeValue(OFConstants.SESSIONDETAIL_IDSHP, logUserResponse.getSessionId());

                        //storing this to support multi user survey
                        ofs.storeValue(OFConstants.USERUNIQUEIDSHP, reserved);

                        // mrh.onResponseReceived(hitType,null,0);
                        OFHelper.v("1Flow", "1Flow Log record inserted...");

                        //Updating old submitted surveys with logged user id.
                        new OFLogUserDBRepoKT().updateSurveyUserId(mContext, this, reserved, OFConstants.ApiHitType.updateSurveyIds);
                        //new OFMyDBAsyncTask(mContext,this,OFConstants.ApiHitType.updateSurveyIds,false).execute(reserved);
                    } else {
                        // OFHelper.e("1Flow", "1Flow LogApi subimission failed logUser");
                        OFLogCountdownTimer.getInstance(mContext, 15000l, 5000l).start();
                        if (OFConstants.MODE.equalsIgnoreCase("dev")) {
                            OFHelper.makeText(mContext, reserved, 1);
                        }
                    }
                } else {
                    //  OFHelper.e("1Flow", "1Flow LogApi subimission failed logUser");
                    OFLogCountdownTimer.getInstance(mContext, 15000l, 5000l).start();
                }

                break;
            case updateSurveyIds:
                if (OFOneFlowSHP.getInstance(mContext).getUserDetails() != null) {
                    long surveyFetchTimeDiff = (System.currentTimeMillis() - OFOneFlowSHP.getInstance(mContext).getLongValue(OFConstants.SHP_SURVEY_FETCH_TIME)) / 1000;
                    OFHelper.v("1Flow", "1Flow survey fetch time diff[" + surveyFetchTimeDiff + "]");
                    if (surveyFetchTimeDiff > 60) {
                        OFSurveyController.getInstance(mContext).getSurveyFromAPI();
                    }
                } else {
                    OFHelper.v("1Flow", "1Flow survey fetch not called as user id not present");
                }
                break;
            case directSurvey:
                OFHelper.v("1Flow", "1Flow survey callback");
                OFGetSurveyListResponse surveyResponse = (OFGetSurveyListResponse) obj;
                setUpHashForActivity();
                if (surveyResponse != null) {
                    OFHelper.v("1Flow", "1Flow survey callback not null");

                    directSurveyShowUp(surveyResponse,"triggered_manually");
                } else {
                    OFHelper.v("1Flow", "1Flow survey callback null");
                }
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

    private void triggerSurveyNew(String eventName){

        JSONArray eventMapArray = new JSONArray();
        eventMapArray.put(new JSONObject(eventMap));

        final Intent surveyIntent = new Intent(mContext.getApplicationContext(), OFFirstLanderActivity.class);

        surveyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        surveyIntent.putExtra("eventName", eventName);
        surveyIntent.putExtra("eventData", eventMapArray.toString());


        OFHelper.v("1Flow", "1Flow activity running 0[" + OFSDKBaseActivity.isActive + "]");

        if (!OFSDKBaseActivity.isActive) {
            mContext.getApplicationContext().startActivity(surveyIntent);
        }
    }

    Long delayDuration = 0l;


    private void triggerSurvey(OFGetSurveyListResponse gslr, String eventName) {


        OFHelper.v("1Flow", "1Flow eventName[" + eventName + "]surveyId[" + gslr.get_id() + "]position[" + gslr.getSurveySettings().getSdkTheme().getWidgetPosition() + "]");
        //Intent surveyIntent = null;
       /* if (gslr.getSurveySettings().getSdkTheme().getWidgetPosition() == null) {
            surveyIntent = new Intent(mContext.getApplicationContext(), activityName.get("bottom-center"));
        } else {
            surveyIntent = new Intent(mContext.getApplicationContext(), activityName.get(gslr.getSurveySettings().getSdkTheme().getWidgetPosition()));
        }*/
        final Intent surveyIntent = new Intent(mContext.getApplicationContext(), OFFirstLanderActivity.class);

        //OFOneFlowSHP ofs1 = OFOneFlowSHP.getInstance(mContext);


        // #001 below code commented because survey will finally be started in next activity after event validation
       /* HashMap<String, Object> mapValue = new HashMap<>();
        mapValue.put("flow_id", gslr.get_id());
        OFEventController ec = OFEventController.getInstance(mContext);
        ec.storeEventsInDB(OFConstants.AUTOEVENT_SURVEYIMPRESSION, mapValue, 0);

        ofs1.storeValue(OFConstants.SHP_SURVEY_RUNNING, true);
        ofs1.storeValue(OFConstants.SHP_SURVEYSTART, Calendar.getInstance().getTimeInMillis());*/


        /*OFThrottlingConfig config = ofs1.getThrottlingConfig();

        if (config != null) {

            OFHelper.v("1Flow", "1Flow throttling config not null");
            config.setActivated(true);
            config.setActivatedById(gslr.get_id());
            config.setActivatedAt(System.currentTimeMillis());

            ofs1.setThrottlingConfig(config);

            setupGlobalTimerToDeactivateThrottlingLocally();
        } else {
            OFHelper.v("1Flow", "1Flow throttling config null");
        }*/

        surveyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        surveyIntent.putExtra("SurveyType", gslr);
        surveyIntent.putExtra("eventName", eventName);
        surveyIntent.putExtra("eventData", new JSONObject(eventMap).toString());


        OFHelper.v("1Flow", "1Flow activity running 1[" + OFSDKBaseActivity.isActive + "]");

        if (!OFSDKBaseActivity.isActive) {
            if (gslr.getSurveyTimeInterval() != null) {

                    if (gslr.getSurveyTimeInterval().getType().equalsIgnoreCase("show_after")) {

                        try {
                            delayDuration = gslr.getSurveyTimeInterval().getValue() * 1000;
                        } catch (Exception ex) {
                           // ex.printStackTrace();
                        }
                        OFHelper.v("1Flow", "1Flow activity waiting duration[" + delayDuration + "]");


                        ContextCompat.getMainExecutor(mContext).execute(new Runnable() {
                            @Override
                            public void run() {
                                OFDelayedSurveyCountdownTimer delaySurvey = OFDelayedSurveyCountdownTimer.getInstance(mContext, delayDuration, 1000l, surveyIntent);
                                delaySurvey.start();
                            }
                        });


                    } else {
                        mContext.getApplicationContext().startActivity(surveyIntent);
                    }

            } else {
                mContext.getApplicationContext().startActivity(surveyIntent);
            }
        }
    }
    Intent surveyIntentD = null;
    private void directSurveyShowUp(OFGetSurveyListResponse gslr, String eventName) {

        OFHelper.v("1Flow", "1Flow eventName[" + eventName + "]surveyId[" + gslr.get_id() + "]position[" + gslr.getSurveySettings().getSdkTheme().getWidgetPosition() + "]");

        if (gslr.getSurveySettings().getSdkTheme().getWidgetPosition() == null) {
            surveyIntentD = new Intent(mContext.getApplicationContext(), activityName.get("bottom-center"));
        } else {
            surveyIntentD = new Intent(mContext.getApplicationContext(), activityName.get(gslr.getSurveySettings().getSdkTheme().getWidgetPosition()));
        }
        //final Intent surveyIntentD = new Intent(mContext.getApplicationContext(), OFFirstLanderActivity.class);

        OFOneFlowSHP ofs1 = OFOneFlowSHP.getInstance(mContext);


        // #001 below code commented because survey will finally be started in next activity after event validation
        HashMap<String, Object> mapValue = new HashMap<>();
        mapValue.put("flow_id", gslr.get_id());
        OFEventController ec = OFEventController.getInstance(mContext);
        ec.storeEventsInDB(OFConstants.AUTOEVENT_SURVEYIMPRESSION, mapValue, 0);
        ec.storeEventsInDB(OFConstants.AUTOEVENT_FLOWSTARTED, mapValue, 0);

        ofs1.storeValue(OFConstants.SHP_SURVEY_RUNNING, true);
        ofs1.storeValue(OFConstants.SHP_SURVEYSTART, Calendar.getInstance().getTimeInMillis());


        OFThrottlingConfig config = ofs1.getThrottlingConfig();

        if (config != null) {

            OFHelper.v("1Flow", "1Flow throttling config not null");
            config.setActivated(true);
            config.setActivatedById(gslr.get_id());
            config.setActivatedAt(System.currentTimeMillis());

            ofs1.setThrottlingConfig(config);

            setupGlobalTimerToDeactivateThrottlingLocally();
        } else {
            OFHelper.v("1Flow", "1Flow throttling config null");
        }

        surveyIntentD.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        surveyIntentD.putExtra("SurveyType", gslr);
        surveyIntentD.putExtra("eventName", eventName);
        surveyIntentD.putExtra("eventData", new JSONObject(eventMap).toString());


        OFHelper.v("1Flow", "1Flow activity running 2[" + OFSDKBaseActivity.isActive + "]");

        if (!OFSDKBaseActivity.isActive) {
            if (gslr.getSurveyTimeInterval() != null) {

                if (gslr.getSurveyTimeInterval().getType().equalsIgnoreCase("show_after")) {

                    try {
                        delayDuration = gslr.getSurveyTimeInterval().getValue() * 1000;
                    } catch (Exception ex) {
                       // ex.printStackTrace();
                    }
                    OFHelper.v("1Flow", "1Flow activity waiting duration[" + delayDuration + "]");


                    ContextCompat.getMainExecutor(mContext).execute(new Runnable() {
                        @Override
                        public void run() {
                            OFDelayedSurveyCountdownTimer delaySurvey = OFDelayedSurveyCountdownTimer.getInstance(mContext, delayDuration, 1000l, surveyIntentD);
                            delaySurvey.start();
                        }
                    });


                } else {
                    mContext.getApplicationContext().startActivity(surveyIntentD);
                }

            } else {
                mContext.getApplicationContext().startActivity(surveyIntentD);
            }
        }
    }


    private void setupGlobalTimerToDeactivateThrottlingLocally() {


        OFHelper.v("1Flow", "1Flow deactivate called ");
        OFThrottlingConfig config = OFOneFlowSHP.getInstance(mContext).getThrottlingConfig();
        OFHelper.v("1Flow", "1Flow deactivate called config activated[" + config.isActivated() + "]globalTime[" + config.getGlobalTime() + "]activatedBy[" + config.getActivatedById() + "]");
        //OFMyCountDownTimerThrottling.getInstance(mContext,0l,0l).cancel();
        if (config.getGlobalTime() != null && config.getGlobalTime() > 0) {
            //OFMyCountDownTimerThrottling.getInstance(mContext, config.getGlobalTime() * 1000, ((Long) (config.getGlobalTime() * 1000) / 2)).start();
            setThrottlingAlarm(config);
        } else {
            OFHelper.v("1Flow", "1Flow deactivate called at else");
            config.setActivated(false);
            config.setActivatedById(null);
            OFOneFlowSHP.getInstance(mContext).setThrottlingConfig(config);
        }


        /*if (config != null) {

            if (config.getGlobalTime() != null) {
                if (config.getGlobalTime() > 0) {

                    OFHelper.v("1Flow", "1Flow deactivate called config global time not null");
                    if (config.isActivated()) {

                        long throttlingLifeTime = config.getActivatedAt() + (config.getGlobalTime() * 1000);
                        if (System.currentTimeMillis() > throttlingLifeTime) {
                            OFHelper.v("1Flow", "1Flow deactivate called time finished");
                            config.setActivated(false);
                            config.setActivatedById(null);
                            OFOneFlowSHP.getInstance(mContext).setThrottlingConfig(config);
                        }

                    }
                } else {
                    config.setActivated(false);
                    config.setActivatedById(null);
                    OFOneFlowSHP.getInstance(mContext).setThrottlingConfig(config);
                }
            }
        }*/
    }

    public void checkThrottlingLife() {
        OFOneFlowSHP shp = OFOneFlowSHP.getInstance(mContext);
        OFThrottlingConfig config = shp.getThrottlingConfig();
        if (config != null) {

            OFHelper.v("MyCountDownTimerThrottling", "1Flow Throttling found [" + config.isActivated() + "]");

            if (config.isActivated()) {
                if (System.currentTimeMillis() > shp.getLongValue(OFConstants.SHP_THROTTLING_TIME)) {
                    OFHelper.v("MyCountDownTimerThrottling", "1Flow Throttling deactivate called time finished");
                    config.setActivated(false);
                    config.setActivatedById(null);
                    shp.setThrottlingConfig(config);
                    shp.storeValue(OFConstants.SHP_THROTTLING_TIME, 0L);
                } else {
                    OFHelper.v("1Flow", "1Flow Throttling pending[" + (shp.getLongValue(OFConstants.SHP_THROTTLING_TIME) - System.currentTimeMillis()) + "]");
                }
            } else {
                OFHelper.v("1Flow", "1Flow Throttling not enabled");
            }

        } else {
            OFHelper.v("1Flow", "1Flow Throttling not enabled");
        }
    }

    public void setThrottlingAlarm(OFThrottlingConfig config) {
        OFHelper.v("1Flow", "1Flow Setting ThrottlingAlarm [" + config.getGlobalTime() + "]");

        /*AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, OFThrottlingAlarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, (config.getGlobalTime() * 1000)+System.currentTimeMillis(), pi);*/

        OFOneFlowSHP shp = OFOneFlowSHP.getInstance(mContext);
        shp.storeValue(OFConstants.SHP_THROTTLING_TIME, config.getGlobalTime() * 1000 + System.currentTimeMillis());


    }


}