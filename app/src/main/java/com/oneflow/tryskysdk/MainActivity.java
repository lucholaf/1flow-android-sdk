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

            case R.id.get_location:
                Helper.makeText(MainActivity.this, "Clicked on button 3", 1);
                //new FeedbackController(this).getLocation();
                break;
            case R.id.fetch_survey_list:
                Helper.makeText(MainActivity.this, "Clicked on button 4", 1);
                //FeedbackController.getSurvey(this);
                break;
            case R.id.project_details:
                Helper.makeText(MainActivity.this, "Clicked on button 0", 1);
                //new FeedbackController(this).getProjectDetails();
                break;
            case R.id.record_log:
                Helper.makeText(MainActivity.this, "Clicked on button 5", 1);
                //FeedbackController.getSurvey(this);
                break;

            case R.id.configure_project:
                Helper.makeText(MainActivity.this, "Clicked on button conf", 1);
                FeedbackController.configure(this,"7oKyqBl/myk8h1Zkq1uSkxffXe9U+p6trHLqA2q1JOU=");
                break;
        }
    }
}