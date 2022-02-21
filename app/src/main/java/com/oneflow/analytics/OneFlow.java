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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
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
import com.oneflow.analytics.controller.OFEventController;
import com.oneflow.analytics.controller.OFSurveyController;
import com.oneflow.analytics.model.OFConnectivity;
import com.oneflow.analytics.model.OFFontSetup;
import com.oneflow.analytics.model.adduser.OFAddUserRequest;
import com.oneflow.analytics.model.adduser.OFAddUserResultResponse;
import com.oneflow.analytics.model.adduser.OFDeviceDetails;
import com.oneflow.analytics.model.adduser.OFLocationDetails;
import com.oneflow.analytics.model.createsession.OFCreateSessionRequest;
import com.oneflow.analytics.model.events.OFEventAPIRequest;
import com.oneflow.analytics.model.events.OFRecordEventsTab;
import com.oneflow.analytics.model.events.OFRecordEventsTabToAPI;
import com.oneflow.analytics.model.location.OFLocationResponse;
import com.oneflow.analytics.model.loguser.OFLogUserRequest;
import com.oneflow.analytics.model.survey.OFGetSurveyListResponse;
import com.oneflow.analytics.repositories.OFAddUserRepo;
import com.oneflow.analytics.repositories.OFCreateSession;
import com.oneflow.analytics.repositories.OFCurrentLocation;
import com.oneflow.analytics.repositories.OFEventAPIRepo;
import com.oneflow.analytics.repositories.OFEventDBRepo;
import com.oneflow.analytics.repositories.OFLogUserDBRepo;
import com.oneflow.analytics.repositories.OFLogUserRepo;
import com.oneflow.analytics.repositories.OFProjectDetails;
import com.oneflow.analytics.sdkdb.OFOneFlowSHP;
import com.oneflow.analytics.utils.OFConstants;
import com.oneflow.analytics.utils.OFHelper;
import com.oneflow.analytics.utils.OFMyCountDownTimer;
import com.oneflow.analytics.utils.OFMyResponseHandler;
import com.oneflow.analytics.utils.OFNetworkChangeReceiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class OneFlow implements OFMyResponseHandler{

    //TODO Convert this class to singleton
    static Context mContext;

    private static Long duration = 1000 * 60 * 60 * 12L;
    private static Long interval = 1000 * 100L; //100L L FOR LONG



    static BillingClient bcFake;

    private OneFlow(Context context) {
        this.mContext = context;
    }
    public static OFFontSetup titleFace,subTitleFace,optionsFace;

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

    public static void configure(Context mContext, String projectKey,OFFontSetup titleFont){
        configureLocal(mContext,projectKey);
        titleFace = titleFont;
    }
    public static void configure(Context mContext, String projectKey,OFFontSetup titleFont,OFFontSetup descriptionFont){
        configureLocal(mContext,projectKey);
        titleFace = titleFont;
        subTitleFace = descriptionFont;
    }
    public static void configure(Context mContext, String projectKey,OFFontSetup titleFont,OFFontSetup descriptionFont,OFFontSetup optionsFont){
        configureLocal(mContext,projectKey);
        titleFace = titleFont;
        subTitleFace = descriptionFont;
        optionsFace = optionsFont;
    }

    public static void configure(Context mContext, String projectKey){
        configureLocal(mContext,projectKey);
    }


    public static void configureLocal(Context mContext, String projectKey) {
        final OneFlow fc = new OneFlow(mContext);

        bcFake = BillingClient.newBuilder(mContext)
                .setListener(new PurchasesUpdatedListener() {
                    @Override
                    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> purchases) {
                        OFHelper.v("InAppPurchase","OneFlow InAppPurchase Called");
                        //OFHelper.makeText(mContext,"in app purchase called",1);

                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                                && purchases != null) {
                            HashMap<String,String> eventValues = new HashMap<>();

                            eventValues.put("productID",purchases.get(0).getOrderId());
                            eventValues.put("quantity",String.valueOf(purchases.get(0).getQuantity()));
                            eventValues.put("price","NA");
                            eventValues.put("subscriptionPeriod","NA");
                            eventValues.put("subscriptionUnit","NA");
                            eventValues.put("localCurrencyPrice","NA");
                            eventValues.put("transactionIdentifier",purchases.get(0).getSignature());
                            eventValues.put("transactionDate",OFHelper.formatedDate(purchases.get(0).getPurchaseTime(),"MM/dd/YYYY"));
                            recordEvents(OFConstants.AUTOEVENT_INAPP_PURCHASE, eventValues);
                        }

                    }
                })
                .enablePendingPurchases()
                .build();


        fc.connectBillingClient();


        final OFOneFlowSHP ofs = new OFOneFlowSHP(mContext);
        OFMyCountDownTimer cmdt = OFMyCountDownTimer.getInstance(mContext, duration, interval);
        cmdt.start();


        Thread confThread = new Thread() {
            @Override
            public void run() {
                super.run();
                Looper.prepare();

                OFNetworkChangeReceiver ncr = new OFNetworkChangeReceiver();
                IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
                mContext.registerReceiver(ncr, intentFilter);

                OFHelper.v("OneFlow", "OneFlow configure called isConnected["+OFHelper.isConnected(mContext)+"]");
                ofs.storeValue(OFConstants.APPIDSHP, projectKey);

                if (OFHelper.isConnected(mContext)) {
                    /*fc.getLocation();*/

                        fc.registerUser(fc.createRequest());

                    OFSurveyController.getInstance(mContext);

                    /*IntentFilter inf = new IntentFilter();
                    inf.addAction("survey_list_fetched");
                    inf.addAction("events_submitted");

                    mContext.registerReceiver(fc.listFetched, inf);*/
                }
                OFHelper.headerKey = projectKey;

                //Fetching current app version
                String currentVersion = OFHelper.getAppVersion(mContext);
                HashMap<String, String> mapValue = new HashMap<>();
                mapValue.put("app_version", currentVersion);
                recordEvents(OFConstants.AUTOEVENT_FIRSTOPEN, mapValue);


                // cheching for update, if version number has changed
                String oldVersion = ofs.getStringValue(OFConstants.SDKVERSIONSHP);

                OFHelper.v("FeedbackController", "OneFlow current version [" + currentVersion + "]old version [" + oldVersion + "]");


                if (oldVersion.equalsIgnoreCase("NA")) {
                    ofs.storeValue(OFConstants.SDKVERSIONSHP, currentVersion);
                } else {
                    if (!oldVersion.equalsIgnoreCase(currentVersion)) {
                        HashMap<String, String> mapUpdateValue = new HashMap<>();
                        mapUpdateValue.put("app_version_current", currentVersion);
                        mapUpdateValue.put("app_version_previous", oldVersion);
                        recordEvents(OFConstants.AUTOEVENT_APPUPDATE, mapUpdateValue);
                    }
                }
                Looper.loop();
            }
        };
        OFHelper.v("OneFlow", "OneFlow confThread isAlive[" + confThread.isAlive() + "]");


        // this logic is required because config was also being called from network change initially
        if (!confThread.isAlive()) {
            Long lastHit = ofs.getLongValue(OFConstants.SHP_ONEFLOW_CONFTIMING);
            Long diff = 10l; // set default value 100 for first time
            Long currentTime = Calendar.getInstance().getTimeInMillis();
            diff = (currentTime - lastHit) / 1000;

            OFHelper.v("OneFlow", "OneFlow conf recordEvents diff [" + diff + "]currentTime[" + currentTime + "]lastHit[" + lastHit + "]");
            if (lastHit == 0 || diff > 60) {
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

        OFAddUserRequest aur = new OFAddUserRequest();
        aur.setSystem_id(OFHelper.getDeviceId(mContext));
        aur.setLanguage(new Locale(Locale.getDefault().getLanguage()).getDisplayName(Locale.US));
        aur.setOFDeviceDetails(dd);
        aur.setOFLocationDetails(null);
        aur.setLocationCheck(true);

        return aur;
    }

    private void registerUser(OFAddUserRequest aur) {
        //sdkdb = Room.databaseBuilder(context, SDKDB.class,"one-flow-db").build();
        OFAddUserRepo.addUser(aur, mContext, this, OFConstants.ApiHitType.CreateUser);
    }

    private void createSession(OFCreateSessionRequest csr) {
        //sdkdb = Room.databaseBuilder(context, SDKDB.class,"one-flow-db").build();
        OFCreateSession.createSession(csr, mContext, this, OFConstants.ApiHitType.CreateSession);
    }

    public static void recordEvents(String eventName, HashMap eventValues) {

        OFHelper.v("FeedbackController", "OneFlow recordEvents record called with[" + eventName + "]");
        try {
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

    public static void logUser(String uniqueId, HashMap<String, String> mapValue) {
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
                //
            }
            sendEventsToApi(mContext);
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
    //private ArrayList<SurveyScreens> checkSurveyTitleAndScreens(String type){
    private void checkSurveyTitleAndScreensInBackground(OFConstants.ApiHitType hitType, String type) {
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
            if (gslr == null) {
                onResponseReceived(hitType, null, 0l,"");
            } else {
                OFLogUserDBRepo.findSurveyForID(mContext, this, OFConstants.ApiHitType.fetchSubmittedSurvey,gslr, gslr.get_id(),new OFOneFlowSHP(mContext).getStringValue(OFConstants.USERUNIQUEIDSHP),type);
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


    @Override
    public void onResponseReceived(OFConstants.ApiHitType hitType, Object obj, Long reserve, String reserved) {
        OFHelper.v("OneFlow", "OneFlow onReceived type[" + hitType + "]");
        switch (hitType) {
            case fetchLocation:

                if (OFHelper.isConnected(mContext)) {
                    registerUser(createRequest());
                }
                break;
            case fetchEventsFromDB:

                OFHelper.v("FeedbackController", "OneFlow checking before log fetchEventsFromDB came back");
                OneFlow fc = new OneFlow(mContext);
                OFOneFlowSHP ofshp = new OFOneFlowSHP(mContext);
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

                        retMain.setDataMap(ret.getDataMap());
                        retListToAPI.add(retMain);

                        ids[i++] = ret.getId();
                    }

                    if (!ofshp.getStringValue(OFConstants.SESSIONDETAIL_IDSHP).equalsIgnoreCase("NA")) {
                        OFEventAPIRequest ear = new OFEventAPIRequest();
                        ear.setSessionId(ofshp.getStringValue(OFConstants.SESSIONDETAIL_IDSHP));
                        ear.setEvents(retListToAPI);

                        OFHelper.v("FeedbackController", "OneFlow checking before log fetchEventsFromDB request prepared");
                        OFEventAPIRepo.sendLogsToApi(mContext, ear, fc, OFConstants.ApiHitType.sendEventsToAPI, ids);
                    }
                } else {
                    OFHelper.e("OneFlow", "OneFlow checking No event available hitting log");
                    OFLogUserRequest lur = ofshp.getLogUserRequest();
                    if (lur != null) {
                        OFLogUserRepo.logUser(lur, mContext, null, OFConstants.ApiHitType.logUser);
                    }
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
                    OFLogUserRepo.logUser(lur, mContext, null, OFConstants.ApiHitType.logUser);
                }
            case CreateSession:
                //TODO Call paralle with create user
                // getSurvey();
                break;
            case CreateUser:

                TelephonyManager telephonyManager = ((TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE));
                String operatorName = telephonyManager.getNetworkOperatorName().isEmpty() ? null : telephonyManager.getNetworkOperatorName();

                WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                DisplayMetrics metrics = new DisplayMetrics();
                display.getMetrics(metrics);

                OFCreateSessionRequest csr = new OFCreateSessionRequest();

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
                OFAddUserResultResponse ofarr = new OFOneFlowSHP(mContext).getUserDetails();
                if(ofarr!=null){
                    userId = ofarr.getAnalytic_user_id();
                }
                csr.setAnalytic_user_id(userId);
                csr.setSystem_id(OFHelper.getDeviceId(mContext));
                csr.setDevice(ddc);
                csr.setLocation_check(true);
                csr.setLocation(null);
                csr.setConnectivity(getConnectivityData());
                String version = "0.1";
                try {
                    PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
                    version = pInfo.versionName;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                csr.setApi_version(version);
                csr.setApp_build_number("23451");
                csr.setLibrary_name("1flow-android-sdk");
                csr.setLibrary_version(String.valueOf(1));
                csr.setApi_endpoint("session");
                csr.setApi_version("0.6.22");
                csr.setApp_version(OFHelper.getAppVersion(mContext));

                recordEvents(OFConstants.AUTOEVENT_SESSIONSTART, null);

                createSession(csr);
                break;
            case fetchSubmittedSurvey:
                if(obj!=null) {
                    OFGetSurveyListResponse gslr = (OFGetSurveyListResponse) obj;

                    if (reserve > 0) {
                        //Checking offline storage of survey

                        try {
                            OFHelper.v("OneFlow", "OneFlow resurvey check option[" + gslr.getSurveySettings().getResurvey_option() + "]current[" + Calendar.getInstance().getTimeInMillis() + "]");
                            if (gslr.getSurveySettings().getResurvey_option()) {
                                Long totalInterval = 0l;
                                Long diff = Calendar.getInstance().getTimeInMillis() - reserve;
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
                                    onResponseReceived(OFConstants.ApiHitType.checkResurveyNSubmission,gslr,0l,reserved);
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();

                        }
                    } else{
                        OFHelper.v("OneFlow","OneFlow no survey found show directly");
                        onResponseReceived(OFConstants.ApiHitType.checkResurveyNSubmission,gslr,0l,reserved);
                    }
                }
                break;
            case checkResurveyNSubmission:
                OFGetSurveyListResponse gslr = (OFGetSurveyListResponse) obj;
                if (gslr != null) {
                    OFHelper.v("FeedbackController", "OneFlow resurvey checked survey found surveyItem[" + gslr + "]");

                    OFOneFlowSHP ofs = new OFOneFlowSHP(mContext);

                    if (gslr.getScreens() != null) {

                        if (gslr.getScreens().size() > 0) {
                            OFHelper.v("OneFlow", "OneFlow resurvey checked running survey[" + (!ofs.getBooleanValue(OFConstants.SHP_SURVEY_RUNNING, false)) + "]");
                            if (!ofs.getBooleanValue(OFConstants.SHP_SURVEY_RUNNING, false)) {
                                ofs.storeValue(OFConstants.SHP_SURVEY_RUNNING, true);
                                ofs.storeValue(OFConstants.SHP_SURVEYSTART, Calendar.getInstance().getTimeInMillis());
                                Intent surveyIntent = new Intent(mContext.getApplicationContext(), OFSurveyActivity.class);
                                //surveyIntent.setType("plain/text");
                                surveyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                surveyIntent.putExtra("SurveyType", gslr);//"move_file_in_folder");//""empty0");//
                                surveyIntent.putExtra("eventName",reserved);
                                mContext.getApplicationContext().startActivity(surveyIntent);
                            }
                        }
                    }
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


}
