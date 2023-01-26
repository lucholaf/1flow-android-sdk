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
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oneflow.analytics.adapter.OFSurveyListAdapter;
import com.oneflow.analytics.customwidgets.OFCustomEditText;
import com.oneflow.analytics.customwidgets.OFCustomTextView;
import com.oneflow.analytics.model.events.OFRecordEventsTabKT;
import com.oneflow.analytics.model.survey.OFDataLogic;
import com.oneflow.analytics.model.survey.OFGetSurveyListResponse;
import com.oneflow.analytics.repositories.OFEventDBRepoKT;
import com.oneflow.analytics.sdkdb.OFOneFlowSHP;
import com.oneflow.analytics.utils.OFConstants;
import com.oneflow.analytics.utils.OFHelper;
import com.oneflow.analytics.utils.OFMyResponseHandlerOneFlow;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class OFFirstActivity extends AppCompatActivity implements OFMyResponseHandlerOneFlow {

    String tag = this.getClass().getName();
    OFCustomTextView result, sendLogsToAPI,noSurvey;
    OFCustomEditText fakeEditText;
    RecyclerView listOfSurvey;
    ProgressBar progressBar;
    ArrayList<OFGetSurveyListResponse> slr;
    OFSurveyListAdapter addb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = (OFCustomTextView) findViewById(R.id.result);
        noSurvey = (OFCustomTextView) findViewById(R.id.no_survey);
        sendLogsToAPI = (OFCustomTextView) findViewById(R.id.send_log_to_api);
        listOfSurvey = (RecyclerView) findViewById(R.id.list_of_survey);
        fakeEditText = (OFCustomEditText) findViewById(R.id.fake_edit_text);
        progressBar = (ProgressBar) findViewById(R.id.progress_circular);

        /*Long lastHit = new SHPRepo().getLongShp(this,OFConstants.SHP_ONEFLOW_CONFTIMING);
        OFHelper.v(tag,"StrictMode ["+lastHit+"]");

        new SHPRepo().storeValue(this,OFConstants.SHP_THROTTLING_TIME,"Circo.OneFlow");*/

        OFOneFlowSHP ofs = OFOneFlowSHP.getInstance(this);

        Long lastHit = ofs.getLongValue(OFConstants.SHP_ONEFLOW_CONFTIMING);

        OFHelper.v(tag,"1Flow lastHit["+lastHit+"]");

        OFHelper.v(tag,"1Flow LanguageCodeTAG["+ Locale.getDefault().toLanguageTag()+"]");
        OFHelper.v(tag,"1Flow LanguageCodeLanguage["+ Locale.getDefault().getLanguage()+"]");
        OFHelper.v(tag,"1Flow LanguageCodeISO3["+ Locale.getDefault().getISO3Language()+"]");
        OFHelper.v(tag,"1Flow LanguageCodetoString["+ Locale.getDefault().toString() +"]");
        OFHelper.v(tag,"1Flow LanguageCodeDisplayLanguage["+ Locale.getDefault().getDisplayLanguage()+"]");
        OFHelper.v(tag,"1Flow LanguageCountry["+Locale.getDefault().getCountry()+"]");

        slr = new ArrayList<>();
        addb = new OFSurveyListAdapter(this, slr, clickListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listOfSurvey.setLayoutManager(linearLayoutManager);
        listOfSurvey.setAdapter(addb);


        IntentFilter inf = new IntentFilter();
        inf.addAction("survey_list_fetched");
        inf.addAction("events_submitted");
        inf.addAction("survey_finished");
        registerReceiver(listFetched, inf);

        Typeface faceBold = Typeface.createFromAsset(getAssets(), "fonts/Lato-Bold.ttf");
        Typeface faceReg = Typeface.createFromAsset(getAssets(), "fonts/Lato-Regular.ttf");
        //OneFlow.configure(this, "BaTElA/QFYa8B+LWBYDdSRDBvRdu0ZBCvXHG4JBAYHZuDIdtT2X8hAKJEHGjBybKQOaua/xllAOXAJC2dJfHcw==");//"1XdRfcEB8jVN05hkDk/+ltke3BHrQ3R9W35JBylCWzg=");//"7oKyqBl/myk8h1Zkq1uSkxffXe9U+p6trHLqA2q1JOU=");//"u6NKK1Vx5xbx3TeOt3ASTGRABmN1gIhhnef53wwwGKo=");//"BaTElA/QFYa8B+LWBYDdSRDBvRdu0ZBCvXHG4JBAYHZuDIdtT2X8hAKJEHGjBybKQOaua/xllAOXAJC2dJfHcw==");//"7oKyqBl/myk8h1Zkq1uSkxffXe9U+p6trHLqA2q1JOU=");//
        //OneFlow.configure(getApplicationContext(), "uiO1MtmMY3Qa31oB3G8ubgnf7Eirmy6UJTe/8lsHB44xRiJgcNXbgwpmrDm0MmAzNVjMi/nAgBlJVgoy7QUs+A==");//""BaTElA/QFYa8B+LWBYDdSRDBvRdu0ZBCvXHG4JBAYHZuDIdtT2X8hAKJEHGjBybKQOaua/xllAOXAJC2dJfHcw==");
        //OneFlow.configure(getApplicationContext(), "uiO1MtmMY3Qa31oB3G8ubgnf7Eirmy6UJTe/8lsHB44xRiJgcNXbgwpmrDm0MmAzNVjMi/nAgBlJVgoy7QUs+A==");//""BaTElA/QFYa8B+LWBYDdSRDBvRdu0ZBCvXHG4JBAYHZuDIdtT2X8hAKJEHGjBybKQOaua/xllAOXAJC2dJfHcw==");
       /* OFFontSetup titleSetup = new OFFontSetup(faceReg, 20F);

        OFFontSetup descriptionFont = new OFFontSetup(null, 12f);

        OFFontSetup optionsFont = new OFFontSetup(faceBold, 12f);*/

       // String projectKey = "oneflow_sandbox_2Z9e492aa1qH22E2SnoSAT5broVR80RF9EXhQ0UcOTyZNgDRCsS4Y88hG4mL+IjPURFgrvCIsuNtUinVIr/ClQ==";
        //String projectKey = "oneflow_sandbox_9NtGc0TDDoOiq+c4z1OTaYpAsu6wUfZ+qECnLtbRYDKiSvMn+sbP+Y1UuSt3bu2RfOr+N4ZNk+84ZEyCeFgJGg==";
        //String projectKey = "oneflow_sandbox_hPz4Tfti7FgaKJ+yTdDGgf+OTNdW2czSmdAFMJL40tGbCqDWfswx+2Zy47zGdcax6zwdQRaYJugbfKglb2SLFA=="; //FakeProject
        //String projectKey = "oneflow_sandbox_oV+xY+hArzT2i4lMP69YZnRBLK1a/qmYW16MboVc208IVjiNKPfHRIylm0rVFgEubtaRuhKMTdlTt5TEuP+8Pw==";
        //String projectKey = "oneflow_prod_YMslXVT1uFOldcBl5kuupFSuLY1yaWkg1lC9lnsZ9jYDvB1KQdRyp4w34VOvMZwlUZ5efuXUWAV5JEizYPzfwA==";
        //String projectKey = "oneflow_prod_OUl/Rzs1AwluTu8j+N2QkdR9ubxrJV7V9ukU9rPp433upW9FghUGVZ947Ntfnvfw/xh00BpYqN8qtTqPvr4KVg=="; //languageTesting
       String projectKey = this.getIntent().getStringExtra("pk");//"oneflow_sandbox_oV+xY+hArzT2i4lMP69YZnRBLK1a/qmYW16MboVc208IVjiNKPfHRIylm0rVFgEubtaRuhKMTdlTt5TEuP+8Pw=="; //AmitRepeatTest
       // String projectKey = "oneflow_prod_YMslXVT1uFOldcBl5kuupFSuLY1yaWkg1lC9lnsZ9jYDvB1KQdRyp4w34VOvMZwlUZ5efuXUWAV5JEizYPzfwA==";//AndroidTestinProject
        //String projectKey = "oneflow_prod_rjz2cV390BlTDSHQi1zHeL8w09+/ZQOJe7mpXJ1SY05sA2UapiKIZl+BwOq0JFoXJIxaXm87TQVo9MQnokf4fQ==";//WelcomeEndScreen
        //String projectKey = "oneflow_prod_YMslXVT1uFOldcBl5kuupFSuLY1yaWkg1lC9lnsZ9jYDvB1KQdRyp4w34VOvMZwlUZ5efuXUWAV5JEizYPzfwA==";//"oneflow_prod_RyR/jsDNOiHS+GMW1ov0bykRA0NHE5mmIqM6eZJtN2ziWaecbiMQu+EvVDmmM3pUzupp7JJyZZcqZDlGASckiA==";

        //OneFlow.configure(getApplicationContext(), projectKey);//,titleSetup,descriptionFont,optionsFont);
        OneFlow.configure(getApplicationContext(), projectKey);//"fonts/pacifico1.ttf");//,titleSetup,descriptionFont,optionsFont);
        //OneFlow.useFont("fonts/pacifico.ttf");
        OneFlow.shouldShowSurvey(true);
        OneFlow.shouldPrintLog(true);

        fakeEditText.setHintTextColor(Color.parseColor("#00ff00"));
        fakeEditText.setTextColor(Color.parseColor("#0000ff"));

    }

    BroadcastReceiver listFetched = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            OFHelper.v(tag, "OneFlow reached receiver");


            if (intent.getAction().equalsIgnoreCase("survey_list_fetched")) {
                slr = OFOneFlowSHP.getInstance(OFFirstActivity.this).getSurveyList();
                progressBar.setVisibility(View.GONE);

                if(slr!=null) {
                    if (slr.size() > 0) {
                        listOfSurvey.setVisibility(View.VISIBLE);
                        addb.notifyMyList(slr);
                    } else {
                        //OFHelper.makeText(OFFirstActivity.this, "No survey received", 1);
                        noSurvey.setText(intent.getStringExtra("msg"));
                        noSurvey.setVisibility(View.VISIBLE);
                    }
                } else {
                    //OFHelper.makeText(OFFirstActivity.this, "No survey received", 1);
                    noSurvey.setText(intent.getStringExtra("msg"));
                    noSurvey.setVisibility(View.VISIBLE);
                }
            } else if (intent.getAction().equalsIgnoreCase("events_submitted")) {
                new OFEventDBRepoKT().fetchEvents(OFFirstActivity.this, OFFirstActivity.this, OFConstants.ApiHitType.fetchEventsFromDB);

            }else if(intent.getAction().equalsIgnoreCase("survey_finished")){
                String triggerName = intent.getStringExtra("surveyDetail");
                OFHelper.v(tag,"OneFlow Submitted survey data["+triggerName+"]");
            }
        }
    };
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String tag = (String) v.getTag();
            String tagArray[] = tag.split(",");
            //GetSurveyListResponse surveyItem = checkSurveyTitleAndScreens(tag);


            HashMap<String, Object> mapvalues = new HashMap<String, Object>();
            mapvalues.put("testKey1", "testValue1");
            mapvalues.put("testKey2", 25);
            mapvalues.put("testKey3", "testValue3");
            OneFlow.recordEvents(tagArray[0], mapvalues);

            /*Intent intent = new Intent(SurveyList.this, SurveyActivity.class);
            intent.putExtra("SurveyType", surveyItem);
            startActivity(intent);*/
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        //Helper.makeText(this,"isConnected["+Helper.isInternetAvailable()+"]",1);

        slr = OFOneFlowSHP.getInstance(OFFirstActivity.this).getSurveyList();
        if (slr != null) {
            addb.notifyMyList(slr);
        }

        new OFEventDBRepoKT().fetchEvents(this, this, OFConstants.ApiHitType.fetchEventsFromDB);
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
            mapvalues.put("platform", "android");
            mapvalues.put("location", "bihar");
            mapvalues.put("webhook_test_andrid", "test_event_name");
            //OneFlow.recordEvents("connect_vpn", mapvalues);
            OneFlow.recordEvents("PopularOS", mapvalues);


        } else if (v.getId() == R.id.disconnect_vpn) {
            // Helper.makeText(FirstActivity.this, "Clicked on button 0", 1);

            HashMap<String, String> mapvalues = new HashMap<String, String>();
            mapvalues.put("disconnect1", "testValue1");
            mapvalues.put("Location", "Nepal");
            mapvalues.put("disconnect3", "testValue3");
            OneFlow.recordEvents("Others", mapvalues);

        } else if (v.getId() == R.id.record_log) {
            OFHelper.v(tag, "OneFlow Clicked on button record log");
            String localTag = (String) v.getTag();
            HashMap<String, Object> mapvalues = new HashMap<String, Object>();
            mapvalues.put("testKey1_" + localTag, "testValue1");
            mapvalues.put("namewa", "Bigu");
            mapvalues.put("testKey3_" + localTag, "testValue3");
            mapvalues.put("testKey3_" + localTag, 23);
            OneFlow.recordEvents(localTag, mapvalues);
        } else if (v.getId() == R.id.configure_project) {

            OneFlow.configure(this, "7oKyqBl/myk8h1Zkq1uSkxffXe9U+p6trHLqA2q1JOU=");
        } else if (v.getId() == R.id.log_user) {

            String emailId = "";
            final EditText edittext = new EditText(this);
            final AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("Enter Your Title");

            alert.setView(edittext);

            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    int a[] = new int[]{1, 2, 3, 4};
                    String b[] = new String[]{"One", "Two", "Three", "Four"};
                    OFDataLogic dl = new OFDataLogic();
                    dl.setAction("Action");
                    dl.setCondition("Condition");
                    dl.setType("Type");
                    dl.setValues("Values");

                    OFDataLogic obj[] = new OFDataLogic[]{dl};
                    //if (!edittext.getText().toString().isEmpty()) {
                        dialog.cancel();
                        HashMap<String, Object> mapValue = new HashMap<>();
                        mapValue.put("location", "Bihar");
                        mapValue.put("env", null);
                        mapValue.put("name", "Amit kumar");
                        mapValue.put("age", 86);
                        mapValue.put("isActive", true);
                        mapValue.put("desitance", 25.16);
                        mapValue.put("timestamp", System.currentTimeMillis());
                        mapValue.put("DateObj", new Date());
                        mapValue.put("StringArray", b);
                        mapValue.put("IntArray", a);
                        mapValue.put("pojo", dl);
                        mapValue.put("pojoArray", obj);
                        OneFlow.logUser(edittext.getText().toString(), mapValue);
                    /*} else {
                        OFHelper.makeText(OFFirstActivity.this, "Enter email id", 1);
                    }*/
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
    public void onResponseReceived(OFConstants.ApiHitType hitType, Object obj, Long reserve, String reserved, Object Obj2, Object obj3) {
        switch (hitType) {
            case fetchEventsFromDB:
                if(obj!=null) {
                    List<OFRecordEventsTabKT> list = (List<OFRecordEventsTabKT>) obj;
                    OFHelper.v(tag,"OneFlow Events size["+list.size()+"]");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sendLogsToAPI.setText("Send Events to API (" + list.size() + ")");
                        }
                    });

                }
                break;

        }
    }

}