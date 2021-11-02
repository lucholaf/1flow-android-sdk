package com.circo.oneflow.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.circo.oneflow.SurveyActivity;
import com.circo.oneflow.model.Connectivity;
import com.circo.oneflow.model.adduser.AddUserRequest;
import com.circo.oneflow.model.adduser.DeviceDetails;
import com.circo.oneflow.model.adduser.LocationDetails;
import com.circo.oneflow.model.createsession.CreateSessionRequest;
import com.circo.oneflow.model.events.EventAPIRequest;
import com.circo.oneflow.model.events.RecordEventsTab;
import com.circo.oneflow.model.events.RecordEventsTabToAPI;
import com.circo.oneflow.model.loguser.LogUserRequest;
import com.circo.oneflow.model.survey.GetSurveyListResponse;
import com.circo.oneflow.repositories.AddUserRepo;
import com.circo.oneflow.repositories.CreateSession;
import com.circo.oneflow.repositories.CurrentLocation;
import com.circo.oneflow.repositories.EventAPIRepo;
import com.circo.oneflow.repositories.EventDBRepo;
import com.circo.oneflow.repositories.LogUserRepo;
import com.circo.oneflow.repositories.ProjectDetails;
import com.circo.oneflow.sdkdb.OneFlowSHP;
import com.circo.oneflow.utils.Constants;
import com.circo.oneflow.utils.Helper;
import com.circo.oneflow.utils.MyResponseHandler;

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
        new OneFlowSHP(mContext).storeValue(Constants.APPIDSHP, projectKey);
        Helper.headerKey = projectKey;

        //Fetching current app version
        String currentVersion = Helper.getAppVersion(mContext);
        HashMap<String,String> mapValue = new HashMap<>();
        mapValue.put("app_version",currentVersion);
        recordEvents(mContext,Constants.AUTOEVENT_FIRSTOPEN,mapValue,0);


        // cheching for update, if version number has changed
        String oldVersion = new OneFlowSHP(mContext).getStringValue(Constants.SDKVERSIONSHP);

        Helper.v("FeedbackController","OneFlow current version ["+currentVersion+"]old version ["+oldVersion+"]");


        if(oldVersion.equalsIgnoreCase("NA")) {
            new OneFlowSHP(mContext).storeValue(Constants.SDKVERSIONSHP, currentVersion);
        }else{
            if(!oldVersion.equalsIgnoreCase(currentVersion)){
                HashMap<String,String> mapUpdateValue = new HashMap<>();
                mapUpdateValue.put("app_version_current",currentVersion);
                mapUpdateValue.put("app_version_previous",oldVersion);
                recordEvents(mContext,Constants.AUTOEVENT_APPUPDATE,mapUpdateValue,0);
            }
        }

        OneFlow fc = new OneFlow(mContext);
        SurveyController sc = SurveyController.getInstance(mContext);

        fc.registerUser(fc.createRequest());
    }

    private AddUserRequest createRequest() {
        DeviceDetails dd = new DeviceDetails();
        dd.setUnique_id(Helper.getDeviceId(mContext));
        dd.setDevice_id(Helper.getDeviceId(mContext));
        dd.setOs("android");

        LocationDetails ld = new LocationDetails();
        ld.setCity("Patna");
        ld.setRegion("Eastern");
        ld.setCountry("India");
        ld.setLatitude(25.5893);
        ld.setLongitude(87.3334);


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

    public static void recordEvents(Context mContext, String eventName, HashMap eventValues,int value) {


        Helper.v("FeedbackController","OneFlow Event record called with["+eventName+"]");

        // storage, api call and check survey if available.
        EventController.getInstance(mContext).storeEventsInDB(eventName, eventValues,value);

        //Checking if any survey available under coming event.
        GetSurveyListResponse surveyItem = new OneFlow(mContext).checkSurveyTitleAndScreens(eventName);
        Helper.v("FeedbackController","OneFlow surveyItem["+surveyItem+"]");
        if (surveyItem != null) {

           /* Long submitTime = new OneFlowSHP(mContext).getLongValue(surveyItem.get_id());
            Helper.v("FeedbackController","OneFlow surveyItem submitted["+submitTime+"]");
            if (submitTime>0) {*/
                if (surveyItem.getScreens() != null) {
                    if (surveyItem.getScreens().size() > 0) {
                        Intent intent = new Intent(mContext, SurveyActivity.class);
                        intent.putExtra("SurveyType", surveyItem);//"move_file_in_folder");//""empty0");//
                        mContext.startActivity(intent);
                    }
                } else {
                    Helper.makeText(mContext, "Click on Configure Project", 1);
                }
            /*}else{
                Helper.makeText(mContext, "Already submitted", 1);
            }*/
        }

    }

    public static void logUser(String uniqueId, HashMap<String,String> mapValue){

        Helper.v("OneFlow","OneFlow logUser called");
        LogUserRequest lur = new LogUserRequest();
        lur.setSystem_id(uniqueId);
        lur.setAnonymous_user_id(new OneFlowSHP(mContext).getUserDetails().getAnalytic_user_id());
        lur.setParameters(mapValue);
        lur.setSession_id(new OneFlowSHP(mContext).getStringValue(Constants.SDKVERSIONSHP));
        LogUserRepo.logUser(lur,mContext,null, Constants.ApiHitType.logUser);

    }
    /**
     * This method will check all aspects of re-survey
     * @return
     */
    private GetSurveyListResponse shouldReturnSurvey(GetSurveyListResponse gslr){

        Long submitTime = new OneFlowSHP(mContext).getLongValue(gslr.get_id());
        if(submitTime>0){
            if(gslr.getSurveySettings().getResurvey_option()){
                    Long totalInterval = 0l;
                Long diff = Calendar.getInstance().getTimeInMillis()-submitTime;
                int diffDuration = 0;
                switch (gslr.getSurveySettings().getRetake_survey().getRetake_select_value()) {
                    case "minutes":
                        totalInterval = gslr.getSurveySettings().getRetake_survey().getRetake_input_value() * 60;
                        diffDuration = (int)(diff/1000)/60;
                        break;
                    case "hours":
                        totalInterval = gslr.getSurveySettings().getRetake_survey().getRetake_input_value() * 60 * 60;
                        diffDuration = (int)((diff/1000)/60)/60;
                        break;
                    case "days":
                        totalInterval = gslr.getSurveySettings().getRetake_survey().getRetake_input_value() * 60 * 60 * 24;
                        diffDuration = (int)(((diff/1000)/60)/60)/60;
                        break;
                    default:
                        Helper.v("FeedbackController","OneFlow retake_select_value is neither of minutes, hours or days");
                }

                if(diffDuration>totalInterval){
                    return gslr;
                }else{
                    return null;
                }

            }
            else{
                Helper.v("FeedbackController","OneFlow ResurveyOption[false]");
                return null;
            }
        }else{
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
                    if (item.getTrigger_event_name().equalsIgnoreCase(type)) {
                        gslr = item;
                /*surveyScreens = item.getScreens();
                selectedSurveyId = item.get_id();
                themeColor = item.getThemeColor();*/
                        // Helper.v(tag,"OneFlow survey found at ["+(counter++)+"]triggerName["+item.getTrigger_event_name()+"]queSize["+item.getScreens().size()+"]");
                        // Helper.v(tag,"OneFlow survey queSize["+new Gson().toJson(item.getScreens())+"]");
                /*int i=0;
                while(i<item.getScreens().size()) {
                    try {
                        if(item.getScreens().get(i).getInput()!=null) {
                            Helper.v(tag, "OneFlow input type["+i+"][" + item.getScreens().get(i).getInput().getInput_type() + "]");
                        }else{
                            Helper.v(tag,"OneFlow found null");
                        }
                    }catch(Exception ex){

                    }
                i++;
                }*/
                        break;
                    }
                }
            } else {
                Helper.makeText(mContext, "Configure project first", 1);
            }


            //Resurvey login
        if(gslr !=null) {
            gslr = shouldReturnSurvey(gslr);
        }

        return gslr;
    }




    public void getProjectDetails() {
        ProjectDetails.getProject(mContext);
    }
    public static void sendEventsToApi(Context contex) {
        OneFlow fc = new OneFlow(contex);
        EventDBRepo.fetchEvents(mContext,fc, Constants.ApiHitType.fetchEventsFromDB);
    }

    public void getLocation() {
        CurrentLocation.getCurrentLocation(mContext);
    }


    @Override
    public void onResponseReceived(Constants.ApiHitType hitType, Object obj, int reserve) {
        switch (hitType) {
            case fetchEventsFromDB:

                Helper.v("FeedbackController","OneFlow fetchEventsFromDB came back");
                OneFlow fc = new OneFlow(mContext);

                ArrayList<RecordEventsTab> list = (ArrayList<RecordEventsTab>)obj;
                Helper.v("FeedbackController","OneFlow fetchEventsFromDB list received size["+list.size()+"]");
                //Preparing list to send api

                Integer []ids = new Integer[list.size()];
                int i=0;
                ArrayList<RecordEventsTabToAPI> retListToAPI = new ArrayList<>();
                RecordEventsTabToAPI retMain;
                for(RecordEventsTab ret:list){
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
                Helper.v("FeedbackController","OneFlow fetchEventsFromDB request prepared");
                EventAPIRepo.sendLogsToApi(mContext,ear,fc, Constants.ApiHitType.sendEventsToAPI,ids);

                break;
            case sendEventsToAPI:
                //Events has been sent to api not deleting local records
                Integer []ids1 = (Integer[])obj;
                EventDBRepo.deleteEvents(mContext,ids1,this, Constants.ApiHitType.deleteEventsFromDB);

                break;
            case deleteEventsFromDB:
                Helper.v("FeedbackControler","OneFlow events delete count["+((Integer)obj)+"]");
                Intent intent = new Intent("events_submitted");
                intent.putExtra("size",String.valueOf((Integer)obj));
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

                LocationDetails ldc = new LocationDetails();
                ldc.setCity("Patna");
                ldc.setRegion("Eastern");
                ldc.setCountry("India");
                ldc.setLatitude(25.5893);
                ldc.setLongitude(87.3334);

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
                csr.setLocation(ldc);
                csr.setConnectivity(con);
                csr.setApi_version("2.2");
                csr.setApp_build_number("23451");
                csr.setLibrary_name("oneflow-android-sdk");
                csr.setLibrary_version(String.valueOf(1));
                csr.setApi_endpoint("session");
                csr.setApi_version("2021-06-15");

                
                recordEvents(mContext,Constants.AUTOEVENT_SESSIONSTART,null,0);

                createSession(csr);
                break;
        }
    }
}
