package com.oneflow.tryskysdk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;

import com.oneflow.tryskysdk.customwidgets.CustomTextView;
import com.oneflow.tryskysdk.model.Connectivity;
import com.oneflow.tryskysdk.model.events.RecordEventsTab;
import com.oneflow.tryskysdk.repositories.EventDBRepo;
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
import com.oneflow.tryskysdk.utils.MyResponseHandler;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends SDKBaseActivity implements MyResponseHandler {

    String tag = this.getClass().getName();
    CustomTextView result,sendLogsToAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = (CustomTextView) findViewById(R.id.result);
        sendLogsToAPI = (CustomTextView) findViewById(R.id.send_log_to_api);


    }

    @Override
    protected void onResume() {
        super.onResume();
        EventDBRepo.fetchEvents(this,this, Constants.ApiHitType.fetchEventsFromDB);
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
            case R.id.send_log_to_api:
                Helper.makeText(MainActivity.this, "Clicked on button 0", 1);
                FeedbackController.sendEventsToApi(this);
                break;
            case R.id.record_log:
                Helper.makeText(MainActivity.this, "Clicked on button 5", 1);
                HashMap<String,String> mapvalues = new HashMap<String, String>();
                mapvalues.put("testKey1","testValue1");
                mapvalues.put("testKey2","testValue2");
                mapvalues.put("testKey3","testValue3");
                FeedbackController.recordEvents(this,"empty18",mapvalues,50);
                break;

            case R.id.configure_project:
                Helper.makeText(MainActivity.this, "Clicked on button conf", 1);
                FeedbackController.configure(this,"7oKyqBl/myk8h1Zkq1uSkxffXe9U+p6trHLqA2q1JOU=");
                break;
        }
    }

    @Override
    public void onResponseReceived(Constants.ApiHitType hitType, Object obj, int reserve) {
        switch(hitType){
            case fetchEventsFromDB:
                List<RecordEventsTab> list = (List<RecordEventsTab>)obj;
                sendLogsToAPI.setText("Send Events to API ("+list.size()+")");
                break;
        }
    }
}