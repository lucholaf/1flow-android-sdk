package com.oneflow.tryskysdk.utils;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.oneflow.tryskysdk.MainActivity;
import com.oneflow.tryskysdk.model.Connectivity;
import com.oneflow.tryskysdk.model.adduser.AddUserRequest;
import com.oneflow.tryskysdk.model.adduser.AddUserResultResponse;
import com.oneflow.tryskysdk.model.adduser.DeviceDetails;
import com.oneflow.tryskysdk.model.adduser.LocationDetails;
import com.oneflow.tryskysdk.model.createsession.CreateSessionRequest;
import com.oneflow.tryskysdk.repositories.AddUserRepo;
import com.oneflow.tryskysdk.repositories.CreateSession;
import com.oneflow.tryskysdk.repositories.CurrentLocation;
import com.oneflow.tryskysdk.repositories.ProjectDetails;
import com.oneflow.tryskysdk.repositories.Survey;
import com.oneflow.tryskysdk.sdkdb.OneFlowSHP;
import com.oneflow.tryskysdk.sdkdb.SDKDB;

import java.util.HashMap;

import javax.xml.XMLConstants;

public class FeedbackController implements MyResponseHandler{

    //TODO Convert this class to singleton

   //static SDKDB sdkdb;
  /* Context mContext;
    public void Build(Context context){
        mContext = context;
    }
    public static void configure(String appKey){

        new OneFlowSHP(mContext).storeValue(Constants.APPIDSHP,appKey);

    }*/


    static Context mContext;
    private FeedbackController(Context context){
        this.mContext = context;
    }


    public static void configure(Context mContext,String projectKey){
        new OneFlowSHP(mContext).storeValue(Constants.APPIDSHP,projectKey);
        FeedbackController fc = new FeedbackController(mContext);
        fc.registerUser(fc.createRequest());
    }

    private AddUserRequest createRequest(){
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

    private void registerUser(AddUserRequest aur){
        //sdkdb = Room.databaseBuilder(context, SDKDB.class,"one-flow-db").build();
        AddUserRepo.addUser(aur,mContext,this, Constants.ApiHitType.CreateUser);
    }
    private void createSession(CreateSessionRequest csr){
        //sdkdb = Room.databaseBuilder(context, SDKDB.class,"one-flow-db").build();
        CreateSession.createSession(csr,mContext,this, Constants.ApiHitType.CreateSession);
    }
   public static void recordEvents(Context mContext, String eventName, HashMap eventValues){
        // storage, api call and check survey if available.

   }




    public void getSurvey(){
        Survey.getSurvey(mContext);
    }
    public void getProjectDetails(){
        ProjectDetails.getProject(mContext);
    }
    public void getLocation(){
        CurrentLocation.getCurrentLocation(mContext);
    }


    @Override
    public void onResponseReceived(Constants.ApiHitType hitType, Object obj, int reserve) {
        switch (hitType){
            case CreateSession:
                getSurvey();
                break;
            case CreateUser:

                TelephonyManager telephonyManager =((TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE));
                String operatorName = telephonyManager.getNetworkOperatorName().isEmpty()?"Jio":telephonyManager.getNetworkOperatorName();



                WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                DisplayMetrics metrics = new DisplayMetrics();
                display.getMetrics(metrics);

                CreateSessionRequest csr = new CreateSessionRequest();
                Connectivity con = new Connectivity();
                con.setCarrier(true);
                con.setRadio(false);
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

                createSession(csr);
                break;
        }
    }
}
