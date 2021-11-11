package com.oneflow.analytics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.oneflow.analytics.controller.EventController;
import com.oneflow.analytics.controller.SurveyController;
import com.oneflow.analytics.model.Connectivity;
import com.oneflow.analytics.model.adduser.AddUserRequest;
import com.oneflow.analytics.model.adduser.DeviceDetails;
import com.oneflow.analytics.model.adduser.LocationDetails;
import com.oneflow.analytics.model.createsession.CreateSessionRequest;
import com.oneflow.analytics.model.events.EventAPIRequest;
import com.oneflow.analytics.model.events.RecordEventsTab;
import com.oneflow.analytics.model.events.RecordEventsTabToAPI;
import com.oneflow.analytics.model.location.LocationResponse;
import com.oneflow.analytics.model.loguser.LogUserRequest;
import com.oneflow.analytics.model.survey.GetSurveyListResponse;
import com.oneflow.analytics.repositories.AddUserRepo;
import com.oneflow.analytics.repositories.CreateSession;
import com.oneflow.analytics.repositories.CurrentLocation;
import com.oneflow.analytics.repositories.EventAPIRepo;
import com.oneflow.analytics.repositories.EventDBRepo;
import com.oneflow.analytics.repositories.LogUserRepo;
import com.oneflow.analytics.repositories.ProjectDetails;
import com.oneflow.analytics.sdkdb.OneFlowSHP;
import com.oneflow.analytics.utils.Constants;
import com.oneflow.analytics.utils.Helper;
import com.oneflow.analytics.utils.MyResponseHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class OneFlow implements MyResponseHandler {

    //TODO Convert this class to singleton


    static Context mContext;

    private OneFlow(Context context) {
        this.mContext = context;
    }


    public static void configure(Context mContext, String projectKey) {
        final OneFlowSHP ofs = new OneFlowSHP(mContext);
        Thread confThread = new Thread() {
            @Override
            public void run() {
                super.run();


                Helper.v("OneFlow","OneFlow configure called");
                ofs.storeValue(Constants.APPIDSHP, projectKey);


                OneFlow fc = new OneFlow(mContext);
                if (Helper.isConnected(mContext)) {
                    fc.getLocation();
                    SurveyController.getInstance(mContext);

                    /*IntentFilter inf = new IntentFilter();
                    inf.addAction("survey_list_fetched");
                    inf.addAction("events_submitted");

                    mContext.registerReceiver(fc.listFetched, inf);*/
                }
                Helper.headerKey = projectKey;

                //Fetching current app version
                String currentVersion = Helper.getAppVersion(mContext);
                HashMap<String, String> mapValue = new HashMap<>();
                mapValue.put("app_version", currentVersion);
                recordEvents(Constants.AUTOEVENT_FIRSTOPEN, mapValue);


                // cheching for update, if version number has changed
                String oldVersion = ofs.getStringValue(Constants.SDKVERSIONSHP);

                Helper.v("FeedbackController", "OneFlow current version [" + currentVersion + "]old version [" + oldVersion + "]");


                if (oldVersion.equalsIgnoreCase("NA")) {
                    ofs.storeValue(Constants.SDKVERSIONSHP, currentVersion);
                } else {
                    if (!oldVersion.equalsIgnoreCase(currentVersion)) {
                        HashMap<String, String> mapUpdateValue = new HashMap<>();
                        mapUpdateValue.put("app_version_current", currentVersion);
                        mapUpdateValue.put("app_version_previous", oldVersion);
                        recordEvents(Constants.AUTOEVENT_APPUPDATE, mapUpdateValue);
                    }
                }
            }
        };
        Helper.v("OneFlow","OneFlow confThread isAlive["+confThread.isAlive()+"]");


        // this logic is required because config was also being called from network change initially
        if(!confThread.isAlive()) {
            Long lastHit = ofs.getLongValue(Constants.SHP_ONEFLOW_CONFTIMING);
            Long diff = 10l; // set default value 100 for first time
            Long currentTime = Calendar.getInstance().getTimeInMillis();
            diff = (currentTime - lastHit) / 1000;

            Helper.v("OneFlow", "OneFlow conf recordEvents diff [" + diff + "]currentTime[" + currentTime + "]lastHit[" + lastHit + "]");
            if (lastHit == 0 || diff > 60) {
                ofs.storeValue(Constants.SHP_ONEFLOW_CONFTIMING, currentTime);
                confThread.start();
            }
        }
        //fc.registerUser(fc.createRequest());
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
    private AddUserRequest createRequest() {
        DeviceDetails dd = new DeviceDetails();
        dd.setUnique_id(Helper.getDeviceId(mContext));
        dd.setDevice_id(Helper.getDeviceId(mContext));
        dd.setOs("android");


        LocationResponse lr = new OneFlowSHP(mContext).getUserLocationDetails();
        LocationDetails ld = new LocationDetails();
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
        }

        AddUserRequest aur = new AddUserRequest();
        aur.setSystem_id(Helper.getDeviceId(mContext));
        aur.setDeviceDetails(dd);
        aur.setLocationDetails(ld);
        aur.setLocationCheck(false);

        return aur;
    }

    private void registerUser(AddUserRequest aur) {
        //sdkdb = Room.databaseBuilder(context, SDKDB.class,"one-flow-db").build();
        AddUserRepo.addUser(aur, mContext, this, Constants.ApiHitType.CreateUser);
    }

    private void createSession(CreateSessionRequest csr) {
        //sdkdb = Room.databaseBuilder(context, SDKDB.class,"one-flow-db").build();
        CreateSession.createSession(csr, mContext, this, Constants.ApiHitType.CreateSession);
    }

    public static void recordEvents(String eventName, HashMap eventValues) {


        Helper.v("FeedbackController", "OneFlow recordEvents record called with[" + eventName + "]");

        // storage, api call and check survey if available.
        EventController.getInstance(mContext).storeEventsInDB(eventName, eventValues, 0);

        //Checking if any survey available under coming event.
        GetSurveyListResponse surveyItem = new OneFlow(mContext).checkSurveyTitleAndScreens(eventName);
        Helper.v("FeedbackController", "OneFlow recordEvents surveyItem[" + surveyItem + "]");
        if (surveyItem != null) {

           /* Long submitTime = new OneFlowSHP(mContext).getLongValue(surveyItem.get_id());
            Helper.v("FeedbackController","OneFlow surveyItem submitted["+submitTime+"]");
            if (submitTime>0) {*/

            OneFlowSHP ofs = new OneFlowSHP(mContext);

            if (surveyItem.getScreens() != null) {
                Long lastHit = ofs.getLongValue(Constants.SHP_SURVEYSTART);
                Long diff = 100l; // set default value 100 for first time
                Long currentTime = Calendar.getInstance().getTimeInMillis();
                if (lastHit > 0) {
                    diff = (currentTime - lastHit) / 1000;
                }
                Helper.v("OneFlow", "OneFlow recordEvents diff [" + diff + "]currentTime[" + currentTime + "]lastHit[" + lastHit + "]");
                if (diff > 3) {
                    if (surveyItem.getScreens().size() > 0) {
                        new OneFlowSHP(mContext).storeValue(Constants.SHP_SURVEYSTART, Calendar.getInstance().getTimeInMillis());
                        Intent intent = new Intent(mContext, SurveyActivity.class);
                        //intent.setType("plain/text");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("SurveyType", surveyItem);//"move_file_in_folder");//""empty0");//
                        mContext.startActivity(intent);
                    }
                } else {
                    //Helper.makeText(mContext,"Already running",1);
                }
            } else {
                //Helper.makeText(mContext, "Click on Configure Project", 1);
            }
            /*}else{
                Helper.makeText(mContext, "Already submitted", 1);
            }*/
        }

    }

    public static void logUser(String uniqueId, HashMap<String, String> mapValue) {
        if (Helper.isConnected(mContext)) {
            Helper.v("OneFlow", "OneFlow logUser called");
            LogUserRequest lur = new LogUserRequest();
            lur.setSystem_id(uniqueId);
            lur.setAnonymous_user_id(new OneFlowSHP(mContext).getUserDetails().getAnalytic_user_id());
            lur.setParameters(mapValue);
            lur.setSession_id(new OneFlowSHP(mContext).getStringValue(Constants.SDKVERSIONSHP));
            LogUserRepo.logUser(lur, mContext, null, Constants.ApiHitType.logUser);
        }
    }

    /**
     * This method will check all aspects of re-survey
     *
     * @return
     */
    private GetSurveyListResponse shouldReturnSurvey(GetSurveyListResponse gslr) {

        Long submitTime = new OneFlowSHP(mContext).getLongValue(gslr.get_id());
        if (submitTime > 0) {
            try {
                if (gslr.getSurveySettings().getResurvey_option()) {
                    Long totalInterval = 0l;
                    Long diff = Calendar.getInstance().getTimeInMillis() - submitTime;
                    int diffDuration = 0;
                    switch (gslr.getSurveySettings().getRetake_survey().getRetake_select_value()) {
                        case "minutes":
                            totalInterval = gslr.getSurveySettings().getRetake_survey().getRetake_input_value() * 60;
                            diffDuration = (int) (diff / 1000) / 60;
                            break;
                        case "hours":
                            totalInterval = gslr.getSurveySettings().getRetake_survey().getRetake_input_value() * 60 * 60;
                            diffDuration = (int) ((diff / 1000) / 60) / 60;
                            break;
                        case "days":
                            totalInterval = gslr.getSurveySettings().getRetake_survey().getRetake_input_value() * 60 * 60 * 24;
                            diffDuration = (int) (((diff / 1000) / 60) / 60) / 60;
                            break;
                        default:
                            Helper.v("FeedbackController", "OneFlow retake_select_value is neither of minutes, hours or days");
                    }

                    if (diffDuration > totalInterval) {
                        return gslr;
                    } else {
                        return null;
                    }

                } else {
                    Helper.v("FeedbackController", "OneFlow ResurveyOption[false]");
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

    /**
     * This method will check if trigger name is available in the list or not
     *
     * @param type
     * @return
     */
    //private ArrayList<SurveyScreens> checkSurveyTitleAndScreens(String type){
    private GetSurveyListResponse checkSurveyTitleAndScreens(String type) {
        ArrayList<GetSurveyListResponse> slr = new OneFlowSHP(mContext).getSurveyList();
        GetSurveyListResponse gslr = null;
        //ArrayList<SurveyScreens> surveyScreens = null;

        int counter = 0;
        String tag = this.getClass().getName();

        if (slr != null) {
            Helper.v(tag, "OneFlow list size[" + slr.size() + "]type[" + type + "]");
            for (GetSurveyListResponse item : slr) {
                Helper.v(tag, "OneFlow list size 0 [" + item.getTrigger_event_name() + "]type[" + type + "]");
                String []eventName = item.getTrigger_event_name().split(",");
                boolean recordFound = false;
                for(String name : eventName){
                    if(name.contains(type)){
                        gslr = item;
                        Helper.v(tag, "OneFlow survey found on event name["+type+"]");
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
    }


    public void getProjectDetails() {
        ProjectDetails.getProject(mContext);
    }

    public static void sendEventsToApi(Context contex) {
        OneFlow fc = new OneFlow(contex);
        EventDBRepo.fetchEvents(mContext, fc, Constants.ApiHitType.fetchEventsFromDB);
    }

    public void getLocation() {
        CurrentLocation.getCurrentLocation(mContext, this, Constants.ApiHitType.fetchLocation);
    }


    @Override
    public void onResponseReceived(Constants.ApiHitType hitType, Object obj, int reserve) {
        Helper.v("OneFlow","OneFlow onReceived type["+hitType+"]");
        switch (hitType) {
            case fetchLocation:

                if (Helper.isConnected(mContext)) {
                    registerUser(createRequest());
                }
                break;
            case fetchEventsFromDB:

                Helper.v("FeedbackController", "OneFlow fetchEventsFromDB came back");
                OneFlow fc = new OneFlow(mContext);

                ArrayList<RecordEventsTab> list = (ArrayList<RecordEventsTab>) obj;
                Helper.v("FeedbackController", "OneFlow fetchEventsFromDB list received size[" + list.size() + "]");
                //Preparing list to send api

                Integer[] ids = new Integer[list.size()];
                int i = 0;
                ArrayList<RecordEventsTabToAPI> retListToAPI = new ArrayList<>();
                RecordEventsTabToAPI retMain;
                for (RecordEventsTab ret : list) {
                    retMain = new RecordEventsTabToAPI();
                    retMain.setEventName(ret.getEventName());
                    retMain.setTime(ret.getTime());

                    //retMain.setDataMap(ret.getDataMap());
                    retListToAPI.add(retMain);

                    ids[i++] = ret.getId();
                }


                EventAPIRequest ear = new EventAPIRequest();
                ear.setSessionId(new OneFlowSHP(mContext).getStringValue(Constants.SESSIONDETAIL_IDSHP));
                ear.setEvents(retListToAPI);
                Helper.v("FeedbackController", "OneFlow fetchEventsFromDB request prepared");
                EventAPIRepo.sendLogsToApi(mContext, ear, fc, Constants.ApiHitType.sendEventsToAPI, ids);

                break;
            case sendEventsToAPI:
                //Events has been sent to api not deleting local records
                Integer[] ids1 = (Integer[]) obj;
                EventDBRepo.deleteEvents(mContext, ids1, this, Constants.ApiHitType.deleteEventsFromDB);

                break;
            case deleteEventsFromDB:
                Helper.v("FeedbackControler", "OneFlow events delete count[" + ((Integer) obj) + "]");
                Intent intent = new Intent("events_submitted");
                intent.putExtra("size", String.valueOf((Integer) obj));
                mContext.sendBroadcast(intent);

            case CreateSession:
                //TODO Call paralle with create user
                // getSurvey();
                break;
            case CreateUser:

                TelephonyManager telephonyManager = ((TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE));
                String operatorName = telephonyManager.getNetworkOperatorName().isEmpty() ? "Jio" : telephonyManager.getNetworkOperatorName();


                WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                DisplayMetrics metrics = new DisplayMetrics();
                display.getMetrics(metrics);

                CreateSessionRequest csr = new CreateSessionRequest();
                Connectivity con = new Connectivity();
                con.setCarrier("true");
                con.setRadio("false");
                con.setWifi(false);




                LocationResponse lr = new OneFlowSHP(mContext).getUserLocationDetails();
                LocationDetails ld = new LocationDetails();
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
                }

                DeviceDetails ddc = new DeviceDetails();
                ddc.setUnique_id(Helper.getDeviceId(mContext));
                ddc.setDevice_id(Helper.getDeviceId(mContext));
                ddc.setOs("android");
                ddc.setCarrier(operatorName);
                ddc.setManufacturer(Build.MANUFACTURER);
                ddc.setModel(Build.MODEL);
                ddc.setOs_ver(Build.VERSION.SDK);
                ddc.setScreen_width(metrics.widthPixels);
                ddc.setScreen_height(metrics.heightPixels);

                csr.setAnalytic_user_id(new OneFlowSHP(mContext).getUserDetails().getAnalytic_user_id());
                csr.setSystem_id(Helper.getDeviceId(mContext));
                csr.setDevice(ddc);
                csr.setLocation_check(false);
                csr.setLocation(ld);
                csr.setConnectivity(con);
                String version = "0.1";
                try {
                    PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
                     version = pInfo.versionName;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                csr.setApi_version(version);
                csr.setApp_build_number("23451");
                csr.setLibrary_name("oneflow-android-sdk");
                csr.setLibrary_version(String.valueOf(1));
                csr.setApi_endpoint("session");
                csr.setApi_version("2021-06-15");


                recordEvents(Constants.AUTOEVENT_SESSIONSTART, null);

                createSession(csr);
                break;
        }
    }
}
