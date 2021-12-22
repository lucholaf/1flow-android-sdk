package com.oneflow.analytics;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oneflow.analytics.adapter.OFSurveyListAdapter;
import com.oneflow.analytics.customwidgets.OFCustomTextView;
import com.oneflow.analytics.model.OFFontSetup;
import com.oneflow.analytics.model.events.OFRecordEventsTab;
import com.oneflow.analytics.model.survey.OFGetSurveyListResponse;
import com.oneflow.analytics.repositories.OFEventDBRepo;
import com.oneflow.analytics.sdkdb.OFOneFlowSHP;
import com.oneflow.analytics.utils.OFConstants;
import com.oneflow.analytics.utils.OFHelper;
import com.oneflow.analytics.utils.OFMyResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;

public class OFFirstActivity extends OFSDKBaseActivity implements OFMyResponseHandler {

    String tag = this.getClass().getName();
    OFCustomTextView result, sendLogsToAPI;

    RecyclerView listOfSurvey;

    ArrayList<OFGetSurveyListResponse> slr;
    OFSurveyListAdapter addb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = (OFCustomTextView) findViewById(R.id.result);
        sendLogsToAPI = (OFCustomTextView) findViewById(R.id.send_log_to_api);
        listOfSurvey = (RecyclerView) findViewById(R.id.list_of_survey);

        ButterKnife.bind(this);


        slr = new ArrayList<>();
        addb = new OFSurveyListAdapter(this, slr, clickListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listOfSurvey.setLayoutManager(linearLayoutManager);
        listOfSurvey.setAdapter(addb);


        IntentFilter inf = new IntentFilter();
        inf.addAction("survey_list_fetched");
        inf.addAction("events_submitted");
        registerReceiver(listFetched, inf);

        Typeface faceBold = Typeface.createFromAsset(getAssets(),"fonts/Lato-Bold.ttf");
        Typeface faceReg = Typeface.createFromAsset(getAssets(),"fonts/Lato-Regular.ttf");
        //OneFlow.configure(this, "BaTElA/QFYa8B+LWBYDdSRDBvRdu0ZBCvXHG4JBAYHZuDIdtT2X8hAKJEHGjBybKQOaua/xllAOXAJC2dJfHcw==");//"1XdRfcEB8jVN05hkDk/+ltke3BHrQ3R9W35JBylCWzg=");//"7oKyqBl/myk8h1Zkq1uSkxffXe9U+p6trHLqA2q1JOU=");//"u6NKK1Vx5xbx3TeOt3ASTGRABmN1gIhhnef53wwwGKo=");//"BaTElA/QFYa8B+LWBYDdSRDBvRdu0ZBCvXHG4JBAYHZuDIdtT2X8hAKJEHGjBybKQOaua/xllAOXAJC2dJfHcw==");//"7oKyqBl/myk8h1Zkq1uSkxffXe9U+p6trHLqA2q1JOU=");//
        //OneFlow.configure(getApplicationContext(), "uiO1MtmMY3Qa31oB3G8ubgnf7Eirmy6UJTe/8lsHB44xRiJgcNXbgwpmrDm0MmAzNVjMi/nAgBlJVgoy7QUs+A==");//""BaTElA/QFYa8B+LWBYDdSRDBvRdu0ZBCvXHG4JBAYHZuDIdtT2X8hAKJEHGjBybKQOaua/xllAOXAJC2dJfHcw==");
        //OneFlow.configure(getApplicationContext(), "uiO1MtmMY3Qa31oB3G8ubgnf7Eirmy6UJTe/8lsHB44xRiJgcNXbgwpmrDm0MmAzNVjMi/nAgBlJVgoy7QUs+A==");//""BaTElA/QFYa8B+LWBYDdSRDBvRdu0ZBCvXHG4JBAYHZuDIdtT2X8hAKJEHGjBybKQOaua/xllAOXAJC2dJfHcw==");
        String projectKey = "uiO1MtmMY3Qa31oB3G8ubgnf7Eirmy6UJTe/8lsHB44xRiJgcNXbgwpmrDm0MmAzNVjMi/nAgBlJVgoy7QUs+A==";//""BaTElA/QFYa8B+LWBYDdSRDBvRdu0ZBCvXHG4JBAYHZuDIdtT2X8hAKJEHGjBybKQOaua/xllAOXAJC2dJfHcw==");

        OFFontSetup titleSetup = new OFFontSetup();
        titleSetup.setTypeface(faceReg);
        titleSetup.setFontSize(10f);

        OFFontSetup descriptionFont = new OFFontSetup();
        descriptionFont.setTypeface(null);
        descriptionFont.setFontSize(15f);

        OFFontSetup optionsFont = new OFFontSetup();
        optionsFont.setTypeface(faceBold);
        optionsFont.setFontSize(null);

        OneFlow.configure(getApplicationContext(), projectKey,titleSetup,descriptionFont,optionsFont);
        OneFlow.shouldShowSurvey(true);

    }

    BroadcastReceiver listFetched = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            OFHelper.v(tag, "OneFlow reached receiver");
            if (intent.getAction().equalsIgnoreCase("survey_list_fetched")) {
                slr = new OFOneFlowSHP(OFFirstActivity.this).getSurveyList();
                addb.notifyMyList(slr);
            } else if (intent.getAction().equalsIgnoreCase("events_submitted")) {
                OFEventDBRepo.fetchEvents(OFFirstActivity.this, OFFirstActivity.this, OFConstants.ApiHitType.fetchEventsFromDB);

            }
        }
    };
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String tag = (String) v.getTag();

            //GetSurveyListResponse surveyItem = checkSurveyTitleAndScreens(tag);

            HashMap<String, String> mapvalues = new HashMap<String, String>();
            mapvalues.put("testKey1", "testValue1");
            mapvalues.put("testKey2", "testValue2");
            mapvalues.put("testKey3", "testValue3");
            OneFlow.recordEvents(tag, mapvalues);

            /*Intent intent = new Intent(SurveyList.this, SurveyActivity.class);
            intent.putExtra("SurveyType", surveyItem);
            startActivity(intent);*/
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        //Helper.makeText(this,"isConnected["+Helper.isInternetAvailable()+"]",1);
        OFEventDBRepo.fetchEvents(this, this, OFConstants.ApiHitType.fetchEventsFromDB);
    }

    public void clickHandler(View v) {

        if (v.getId() == R.id.get_location) {
            // Helper.makeText(FirstActivity.this, "Clicked on button 3", 1);
            //new FeedbackController(this).getLocation();
        } else if (v.getId() == R.id.fetch_survey_list) {
            //  Helper.makeText(FirstActivity.this, "Clicked on button 4", 1);
            //FeedbackController.getSurvey(this);
        } else if (v.getId() == R.id.project_details) {
            // Helper.makeText(FirstActivity.this, "Clicked on button 0", 1);
            //new FeedbackController(this).getProjectDetails();
        } else if (v.getId() == R.id.send_log_to_api) {
            // Helper.makeText(FirstActivity.this, "Clicked on button 0", 1);
            OneFlow.sendEventsToApi(this);
        } else if (v.getId() == R.id.connect_vpn) {
            // Helper.makeText(FirstActivity.this, "Clicked on button 0", 1);

            HashMap<String, String> mapvalues = new HashMap<String, String>();
            mapvalues.put("connect1", "testValue1");
            mapvalues.put("connect2", "testValue2");
            mapvalues.put("connect3", "testValue3");
            OneFlow.recordEvents("connect_vpn", mapvalues);

        } else if (v.getId() == R.id.disconnect_vpn) {
            // Helper.makeText(FirstActivity.this, "Clicked on button 0", 1);

            HashMap<String, String> mapvalues = new HashMap<String, String>();
            mapvalues.put("disconnect1", "testValue1");
            mapvalues.put("disconnect2", "testValue2");
            mapvalues.put("disconnect3", "testValue3");
            OneFlow.recordEvents("disconnect_vpn", mapvalues);

        } else if (v.getId() == R.id.record_log) {
            //   Helper.makeText(FirstActivity.this, "Clicked on button 5", 1);
            HashMap<String, String> mapvalues = new HashMap<String, String>();
            mapvalues.put("testKey1", "testValue1");
            mapvalues.put("testKey2", "testValue2");
            mapvalues.put("testKey3", "testValue3");
            OneFlow.recordEvents("empty18", mapvalues);
        } else if (v.getId() == R.id.configure_project) {
            //  Helper.makeText(FirstActivity.this, "Clicked on button conf", 1);
            OneFlow.configure(this, "7oKyqBl/myk8h1Zkq1uSkxffXe9U+p6trHLqA2q1JOU=",null);
        } else if (v.getId() == R.id.log_user) {

            String emailId = "";
            final EditText edittext = new EditText(this);
            final AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("Enter Your Title");

            alert.setView(edittext);

            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    if (!edittext.getText().toString().isEmpty()) {
                        dialog.cancel();
                        HashMap<String, String> mapValue = new HashMap<>();
                        mapValue.put("firstName", "RamLal");
                        mapValue.put("lastName", "Goswami");
                        mapValue.put("number", "1234");
                        OneFlow.logUser(edittext.getText().toString(), mapValue);
                    } else {
                        OFHelper.makeText(OFFirstActivity.this, "Enter email id", 1);
                    }
                }
            });

            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // what ever you want to do with No option.
                }
            });

            alert.show();


            //OneFlow.recordEvents("event_ev",mapValue);

        }
    }

    @Override
    public void onResponseReceived(OFConstants.ApiHitType hitType, Object obj, Long reserve) {
        switch (hitType) {
            case fetchEventsFromDB:
                List<OFRecordEventsTab> list = (List<OFRecordEventsTab>) obj;
                sendLogsToAPI.setText("Send Events to API (" + list.size() + ")");
                break;

        }
    }
}