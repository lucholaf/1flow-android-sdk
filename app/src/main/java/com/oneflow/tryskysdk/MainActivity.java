package com.oneflow.tryskysdk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;

import com.oneflow.tryskysdk.customwidgets.CustomTextView;
import com.oneflow.tryskysdk.model.Connectivity;
import com.oneflow.tryskysdk.sdkdb.OneFlowSHP;
import com.oneflow.tryskysdk.sdkdb.convertes.DataConverterLocation;
import com.oneflow.tryskysdk.model.adduser.AddUserRequest;
import com.oneflow.tryskysdk.model.adduser.DeviceDetails;
import com.oneflow.tryskysdk.model.adduser.LocationDetails;
import com.oneflow.tryskysdk.model.createsession.CreateSessionRequest;
import com.oneflow.tryskysdk.sdkdb.SDKDB;
import com.oneflow.tryskysdk.utils.Constants;
import com.oneflow.tryskysdk.utils.FeedbackController;
import com.oneflow.tryskysdk.utils.Helper;

public class MainActivity extends SDKBaseActivity {

    String tag = this.getClass().getName();
    CustomTextView result;
    SDKDB sdkdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = (CustomTextView) findViewById(R.id.result);

        sdkdb = Room.databaseBuilder(getApplicationContext(), SDKDB.class, "one-flow-db")
                .build();

    }


    public void clickHandler(View v) {

        switch (v.getId()) {
            case R.id.create_user:
                Helper.makeText(MainActivity.this, "Clicked on button 0", 1);
                DeviceDetails dd = new DeviceDetails();
                dd.setUnique_id(Helper.getDeviceId(MainActivity.this));
                dd.setDevice_id(Helper.getDeviceId(MainActivity.this));
                dd.setOs("android");

                LocationDetails ld = new LocationDetails();
                ld.setCity("Patna");
                ld.setRegion("Eastern");
                ld.setCountry("India");
                ld.setLatitude(25.5893);
                ld.setLongitude(87.3334);


                AddUserRequest aur = new AddUserRequest();
                aur.setSystem_id(Helper.getDeviceId(MainActivity.this));
                aur.setDeviceDetails(dd);
                aur.setLocationDetails(ld);
                aur.setLocationCheck(false);
                FeedbackController.registerUser(this, aur);

                break;
            case R.id.create_session:
                Helper.makeText(MainActivity.this, "Clicked on button 1", 1);

                String userId = new OneFlowSHP(MainActivity.this).getUserDetails(Constants.USERDETAILSHP).getAnalytic_user_id();
                Helper.v(tag, "OneFlow fetching data from db[" + userId + "]");

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
                ddc.setUnique_id(Helper.getDeviceId(MainActivity.this));
                ddc.setDevice_id(Helper.getDeviceId(MainActivity.this));
                ddc.setOs("android");
                ddc.setCarrier("jio");
                ddc.setManufacturer("motorola");
                ddc.setModel("G-40");
                ddc.setOs_ver("11");
                ddc.setScreen_width(0);
                ddc.setScreen_height(0);

                csr.setAnalytic_user_id(userId);
                csr.setSystem_id(Helper.getDeviceId(MainActivity.this));
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

                FeedbackController.createSession(this, csr);
                break;
            case R.id.get_location:
                Helper.makeText(MainActivity.this, "Clicked on button 2", 1);
                FeedbackController.getLocation(this);
                break;
            case R.id.record_log:
                Helper.makeText(MainActivity.this, "Clicked on button 3", 1);
                FeedbackController.getSurvey(this);
                break;
        }
    }
}