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
        OFFontSetup titleSetup = new OFFontSetup(faceReg,20F);

        OFFontSetup descriptionFont = new OFFontSetup(null,12f);

        OFFontSetup optionsFont = new OFFontSetup(faceBold,12f);

        String projectKey = "oneflow_sandbox_2Z9e492aa1qH22E2SnoSAT5broVR80RF9EXhQ0UcOTyZNgDRCsS4Y88hG4mL+IjPURFgrvCIsuNtUinVIr/ClQ==";//"2Z9e492aa1qH22E2SnoSATz/9CfN4l/Gkz5Anc99bUQ=";//"7oKyqBl/myk8h1Zkq1uSkxffXe9U+p6trHLqA2q1JOU=";//"uiO1MtmMY3Qa31oB3G8ubgnf7Eirmy6UJTe/8lsHB44xRiJgcNXbgwpmrDm0MmAzNVjMi/nAgBlJVgoy7QUs+A==";//""BaTElA/QFYa8B+LWBYDdSRDBvRdu0ZBCvXHG4JBAYHZuDIdtT2X8hAKJEHGjBybKQOaua/xllAOXAJC2dJfHcw==");//"gMZTvChPL5nOf8sHweZpYQOXOuURnUYbfjvFqSbKo7u85xv2u7B6L94fdOGni4BnshOxwleSsgfNQGZz1hs6Rg==";//"";//


        OneFlow.configure(getApplicationContext(), projectKey);//,titleSetup,descriptionFont,optionsFont);
        OneFlow.shouldShowSurvey(true);
        OneFlow.shouldPrintLog(true);

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

        slr = new OFOneFlowSHP(OFFirstActivity.this).getSurveyList();
        if(slr!=null) {
            addb.notifyMyList(slr);
        }

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
            //OneFlow.recordEvents("connect_vpn", mapvalues);
            OneFlow.recordEvents("start_all_survey_data_logic", mapvalues);

        } else if (v.getId() == R.id.disconnect_vpn) {
            // Helper.makeText(FirstActivity.this, "Clicked on button 0", 1);

            HashMap<String, String> mapvalues = new HashMap<String, String>();
            mapvalues.put("disconnect1", "testValue1");
            mapvalues.put("disconnect2", "testValue2");
            mapvalues.put("disconnect3", "testValue3");
            OneFlow.recordEvents("disconnect_vpn", mapvalues);

        } else if (v.getId() == R.id.record_log) {
            OFHelper.v(tag, "OneFlow Clicked on button record log");
            String localTag = (String) v.getTag();
            HashMap<String, String> mapvalues = new HashMap<String, String>();
            mapvalues.put("testKey1_"+localTag, "testValue1");
            mapvalues.put("testKey2_"+localTag, "testValue2");
            mapvalues.put("testKey3_"+localTag, "testValue3");
            OneFlow.recordEvents(localTag, mapvalues);
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
    public void onResponseReceived(OFConstants.ApiHitType hitType, Object obj, Long reserve, String reserved) {
        switch (hitType) {
            case fetchEventsFromDB:
                List<OFRecordEventsTab> list = (List<OFRecordEventsTab>) obj;
                sendLogsToAPI.setText("Send Events to API (" + list.size() + ")");
                break;

        }
    }
}