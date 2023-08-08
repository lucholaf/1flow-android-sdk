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
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.oneflow.analytics.adapter.OFSurveyListAdapter;
import com.oneflow.analytics.customwidgets.OFCustomEditText;
import com.oneflow.analytics.customwidgets.OFCustomTextView;
import com.oneflow.analytics.model.events.OFRecordEventsTabKT;
import com.oneflow.analytics.model.survey.OFGetSurveyListResponse;
import com.oneflow.analytics.repositories.OFEventDBRepoKT;
import com.oneflow.analytics.sdkdb.OFOneFlowSHP;
import com.oneflow.analytics.utils.OFConstants;
import com.oneflow.analytics.utils.OFHelper;
import com.oneflow.analytics.utils.OFMyResponseHandlerOneFlow;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class OFFirstActivity extends AppCompatActivity implements OFMyResponseHandlerOneFlow {

    String tag = this.getClass().getName();

    OFCustomTextView sendLogsToAPI, noSurvey;

    // OFCustomEditText fakeEditText;
    RecyclerView listOfSurvey;
    ProgressBar progressBar;
    ArrayList<OFGetSurveyListResponse> slr;
    OFSurveyListAdapter addb;
    Boolean configureCalled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noSurvey = (OFCustomTextView) findViewById(R.id.no_survey);
        ((OFCustomTextView) findViewById(R.id.build_mode)).setText(OFConstants.MODE);
        sendLogsToAPI = (OFCustomTextView) findViewById(R.id.send_log_to_api);
        listOfSurvey = (RecyclerView) findViewById(R.id.list_of_survey);

        progressBar = (ProgressBar) findViewById(R.id.progress_circular);

        /*Long lastHit = new SHPRepo().getLongShp(this,OFConstants.SHP_ONEFLOW_CONFTIMING);
        OFHelper.v(tag,"StrictMode ["+lastHit+"]");

        new SHPRepo().storeValue(this,OFConstants.SHP_THROTTLING_TIME,"Circo.OneFlow");*/

        OFOneFlowSHP ofs = OFOneFlowSHP.getInstance(this);

        Long lastHit = ofs.getLongValue(OFConstants.SHP_ONEFLOW_CONFTIMING);


        OFHelper.v(tag, "1Flow lastHit[" + lastHit + "]");

        OFHelper.v(tag, "1Flow LanguageCodeTAG[" + Locale.getDefault().toLanguageTag() + "]");
        OFHelper.v(tag, "1Flow LanguageCodeLanguage[" + Locale.getDefault().getLanguage() + "]");
        OFHelper.v(tag, "1Flow LanguageCodeISO3[" + Locale.getDefault().getISO3Language() + "]");
        OFHelper.v(tag, "1Flow LanguageCodetoString[" + Locale.getDefault().toString() + "]");
        OFHelper.v(tag, "1Flow LanguageCodeDisplayLanguage[" + Locale.getDefault().getDisplayLanguage() + "]");
        OFHelper.v(tag, "1Flow LanguageCountry[" + Locale.getDefault().getCountry() + "]");


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

        String projectKey = ofs.getStringValue(OFConstants.APPIDSHP);


        OFHelper.v(tag, "1Flow projectKey[" + projectKey + "]");
        if (!OFHelper.validateString(projectKey).equalsIgnoreCase("na")) {
            configureCalled = true;
            configureOneFlow(projectKey);
        } else {

            //OFHelper.makeText(this,"Project key not available, Won't call OneFlow config",1);
            noSurvey.setText("Configure not called");
            progressBar.setVisibility(View.GONE);
            noSurvey.setVisibility(View.VISIBLE);
        }

        /*fakeEditText.setHintTextColor(Color.parseColor("#00ff00"));
        fakeEditText.setTextColor(Color.parseColor("#0000ff"));*/
       // FirebaseApp.initializeApp(this);
       // retriveCurrentFCMToken();

    //    OFHelper.v(tag,"1Flow mongodb id method["+objectIdMy()+"]");

    }



    /*private void retriveCurrentFCMToken() {
        try {
//            sendNotification();
            final String[] token = {""};
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(Task<String> task) {
                    if(task.isComplete()){
                        token[0] = task.getResult();
                        OFHelper.e("AppConstants", "1Flow onComplete: new Token got: "+token[0] );

                    }
                }

            });
            //return token[0];

        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                e.printStackTrace();
        }
    }*/

   /* @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);


        Bundle bundle = intent.getExtras();
        for (String key: bundle.keySet())
        {
            OFHelper.v(tag,"1Flow data from notifcation key["+key+"]value["+bundle.get(key)+"]");
        }
        OFHelper.v(tag,"1Flow data from notifcation finish");

    }*/

    private void configureOneFlow(String projectKey) {

        OneFlow.configure(getApplicationContext(), projectKey);//"fonts/pacifico1.ttf");//,titleSetup,descriptionFont,optionsFont);
        //OneFlow.useFont("fonts/pacifico.ttf");
        OneFlow.shouldShowSurvey(true);
        OneFlow.shouldPrintLog(true);


       /* HashMap<String, Object> mapValue = new HashMap<>();
        mapValue.put("location", "MP");
        mapValue.put("env", "Prod");
        mapValue.put("test_date", OFHelper.formatDateIntoCustomFormat(new Date(),"yyyy-mm-dd"));
        mapValue.put("tested_by", "Amit kumar sharma");

        OneFlow.logUser("test_Android_2023_03_01", mapValue);*/
    }

    private void showCofigureDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_project_key);
        OFCustomEditText projectKeyET = dialog.findViewById(R.id.project_key);
        //String projectKey = "oneflow_prod_UjlFunf96DxcEXXXgJKqm32q1RDIYXbmDkepkDmomBoDdlzXQM/U9qzEAKh6yj34xfQQT1Ejp0ltJnF9wGJU5Q==";// [TEST] All Screens
        //String projectKey = "oneflow_prod_yxwI14oGAEhYgOEJjo43IsoKuWbSPoXBcKD+Bj5UkiZtPXb1vuuBkRUm5YxfBCs6thcsxPWbxDeJHJZlSGzxkw==";// SANVEDI'S PROJECT
        //String projectKey = "oneflow_sandbox_oV+xY+hArzT2i4lMP69YZnRBLK1a/qmYW16MboVc208IVjiNKPfHRIylm0rVFgEubtaRuhKMTdlTt5TEuP+8Pw==";// AmitRepeatTest
        //String projectKey = "oneflow_prod_mnIy0sLLQ1k7ljLTTsuZnBUlWJbzmgp9Gmh5VmdX/7oGk4M9Ki3rU1AKN9xO8/3bCqL7iWEPNJf1e/MCbwaTlg==";// Sparsh Project
        //String projectKey = "oneflow_prod_CE5Cvb8EahGdiyeu7TkY4DDR8inq1u8qLHvV2XnJM5UuZNW1V0I+XMQi2Qo5raeyrk7b3GEl+a9iz6F2EWGtNg==";// QA-2
        //String projectKey = "oneflow_prod_Wl3l4jvO6pS+sljMrS57wNbqZBwHMjP+bvpADic7ezDdewUFd2KWIJSDsx/oizwLfQI+FvtrsmPz3HBFqZVQtw==";//  Android + iOS build sanity
        //String projectKey = "oneflow_prod_z1IhZWJa3hgnvqPoaK7GiTRD0gB329MKOuXHgyGj4zE5dWXGhtlPAd4q9nVCKqS8qOc0YtSEAlDR1fysr8SqiQ==";// NewScreenTestProj
        //String projectKey = "oneflow_prod_cfgHr2eqbDESMQHMpnjxO8rGR+QXltsFaJ5nIcm9lt/BjeXwaTMK/J+d9JanbLtsDEkBnuEf2xkl26D+hJz2+g=="; //Conversation AI
        //String projectKey = "oneflow_prod_SR8Fn2G0BMPY4RW7ZE/bG37M2VbOQrG8KKfOCkW6K8MdYNMKj2Ug9VPkwtbgTLXZE6YZ2fvm6M9UxuEBcVB9Xw==";// Ahsan project key
        //String projectKey = "oneflow_prod_hlXx+7J/PLaZmjrScYvDqVr75+oIAS+Fyc2Hs7hO4o1GcsbyeMTJ74XKceugfPhDZ3MPdbB65rltbhP9cWmaYA==";// Embed I&V Android
        String projectKey = "oneflow_prod_YMslXVT1uFOldcBl5kuupFSuLY1yaWkg1lC9lnsZ9jYDvB1KQdRyp4w34VOvMZwlUZ5efuXUWAV5JEizYPzfwA==";//AndroidTestinProject
        //String projectKey = "oneflow_prod_S2Fhp9kIgnuUifdybu416l5zcTJ1H2olfCwMbq2stQKvH/tvurEgpBSWgUIRFRxMkmc2cs7KOwALqzr235/wpw==";//AndroidTestinProject
       // String projectKey = "oneflow_prod_XYDk9mmRqZHYwKnG5s+3bkB3kqTnmew74mxqUQS8S4fcsfxx2E6ItOnNA3DKmMiWoKrQz0IqAvra9UbXDhGP6A==";//Ahsan's Project 10_july
        //String projectKey = "oneflow_prod_43WVuVjDSygeAejX5uzRQUaYHpzs9eV6zrG+wPRRI2aCHjRwKRftCzVsEJ+Fp2+cBnB8rYogwdXjFnVKZEjxpw==";//AndroidTestinProject
        //String projectKey = "oneflow_prod_/xygSKirAO9POupz31Ef7RIO8gvkkRiqfeO4Q98hQHS6QugUesc/Fme4AXuIFvlwSe8KsMt9ochqOO71ojMXpg==";//AmitV2Testing
        //String projectKey = "oneflow_prod_FMmrAqrKisPtrNzr1nn2Fapz0hutCSAhwo7Ln7G2521f0JZV/G1iYhSRnnUgsgnUWVrtLdzq1Y00B8+lshncfg==";//AndroidTestinProject
        //String projectKey = "oneflow_prod_Aj1cg38j4iAzpwhl/jEACm1HTDFgYJhj5yxEDDHlMo9u53RNdpqcDjZvGeBW17CldKelGu2/TXfUfTE4bDgR3Q==";//Rohan's Arabic
        //String projectKey = "oneflow_prod_SR8Fn2G0BMPY4RW7ZE/bGxkFmyfC4mcHfsqjNpy5zHnO+GcpXBdg+Dw6pJmVBC2lcGFgTpHQziPqFUsmcaWAIA==";//Rohan's Arabic
        //String projectKey = "oneflow_prod_yxwI14oGAEhYgOEJjo43IsoKuWbSPoXBcKD+Bj5UkiZtPXb1vuuBkRUm5YxfBCs6thcsxPWbxDeJHJZlSGzxkw==";//[TEST]Flutter/React Native SDKs
        projectKeyET.setText(projectKey);
        OFCustomTextView registerButton = dialog.findViewById(R.id.register_project);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (projectKeyET.getText().toString().equalsIgnoreCase("")) {
                    OFHelper.makeText(OFFirstActivity.this, "Please enter project key", 1);
                } else {
                    dialog.cancel();
                    noSurvey.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    configureOneFlow(projectKeyET.getText().toString());
                }
            }
        });
        dialog.show();
    }

  
    BroadcastReceiver listFetched = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            OFHelper.v(tag, "OneFlow reached receiver");


            if (intent.getAction().equalsIgnoreCase("survey_list_fetched")) {
                slr = OFOneFlowSHP.getInstance(OFFirstActivity.this).getSurveyList();
                progressBar.setVisibility(View.GONE);
                OFHelper.v(tag, "OneFlow reached receiver 0[" + slr + "]msg[" + intent.getStringExtra("msg") + "]");
                if (slr != null) {

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

            } else if (intent.getAction().equalsIgnoreCase("survey_finished")) {
                String triggerName = intent.getStringExtra("surveyDetail");
                OFHelper.v(tag, "OneFlow Submitted survey data[" + triggerName + "]");
            }
        }
    };
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String tag = (String) v.getTag();

            OFHelper.v(tag,"1Flow clicked on tag["+tag+"]");
            String tagArray[] = tag.split(",");
            //GetSurveyListResponse surveyItem = checkSurveyTitleAndScreens(tag);

            /*if (tagArray[0].trim().equalsIgnoreCase("")) {
                OFHelper.showAlert(OFFirstActivity.this, "", "Event name not defined, Unable to trigger");
            } else {*/
                /*HashMap<String, Object> mapvalues = new HashMap<String, Object>();
                mapvalues.put("testKey1", "testValue1");
                mapvalues.put("testKey2", 25);
                mapvalues.put("testKey3", "testValue3");
                OneFlow.recordEvents(tagArray[0], mapvalues);*/
            OneFlow.startFlow(tagArray[0]);
            //}
            /*Intent intent = new Intent(SurveyList.this, SurveyActivity.class);
            intent.putExtra("SurveyType", surveyItem);
            startActivity(intent);*/
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        //Helper.makeText(this,"isConnected["+Helper.isInternetAvailable()+"]",1);

        OFHelper.v(tag,"1Flow onResume ["+configureCalled+"]");
        if (!configureCalled) {
            slr = OFOneFlowSHP.getInstance(OFFirstActivity.this).getSurveyList();
            if (slr != null) {
                addb.notifyMyList(slr);
                noSurvey.setVisibility(View.GONE);
            }

            new OFEventDBRepoKT().fetchEvents(this, this, OFConstants.ApiHitType.fetchEventsFromDB);
        }
    }

    public void clickHandler(View v) {


        if (v.getId() == R.id.trigger_survey) {
            startActivity(new Intent(OFFirstActivity.this,OFSecondActivity.class));
        }else if (v.getId() == R.id.send_log_to_api) {
            // Helper.makeText(FirstActivity.this, "Clicked on button 0", 1);
            OneFlow.sendEventsToApi(this);

        } else if (v.getId() == R.id.configure_oneflow) {
            showCofigureDialog();
        }else if (v.getId() == R.id.direct_survey) {
            OFHelper.v(tag,"1Flow clicked on direct survey");
            OneFlow.startFlow("3432498320480909w3");
        }

        /* else if (v.getId() == R.id.start_session) {
           // OneFlow.recordEvents("start_session",null);

            OFEventController ec = OFEventController.getInstance(OFFirstActivity.this);
            ec.storeEventsInDB("start_session", null, 0);


        } */else if (v.getId() == R.id.log_user) {


            String emailId = "";
            final EditText edittext = new EditText(this);
            final AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("Enter Your Title");

            alert.setView(edittext);

            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    /*int a[] = new int[]{1, 2, 3, 4};
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
                    OneFlow.logUser(edittext.getText().toString(), mapValue);*/
                    /*} else {
                        OFHelper.makeText(OFFirstActivity.this, "Enter email id", 1);
                    }*/
                    if (edittext.getText().toString().trim().equalsIgnoreCase("")) {
                        OFHelper.makeText(OFFirstActivity.this, "Empty user name", 1);

                    } else {
                        if (OFHelper.isConnected(OFFirstActivity.this)) {

                            OFHelper.v(tag, "1Flow date[" + OFHelper.formatDateIntoCustomFormat(new Date(), "YYYY-MM-dd") + "]");

                            HashMap<String, Object> mapValue = new HashMap<>();
                            //mapValue.put("test_date", OFHelper.formatDateIntoCustomFormat(new Date(), "YYYY-MM-dd"));
                            //mapValue.put("tested_by", "DummyBuild_2023.02.09");


                            OneFlow.logUser(edittext.getText().toString(),null);//edittext.getText().toString());//, mapValue);

                        }
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

    public void showDirectSurveyDialog(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_project_key);
        OFCustomEditText projectKeyET = dialog.findViewById(R.id.project_key);
        String projectKey = "8a88f2103aaf49641d5efd37";
        projectKeyET.setHint("Enter Survey Id");
        projectKeyET.setText(projectKey);
        OFCustomTextView registerButton = dialog.findViewById(R.id.register_project);
        registerButton.setText("Init Survey");
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (projectKeyET.getText().toString().equalsIgnoreCase("")) {
                    OFHelper.makeText(getApplicationContext(), "Please enter survey ID", 1);
                } else {
                    dialog.cancel();

                    OneFlow.startFlow(projectKeyET.getText().toString());
                }
            }
        });
        dialog.show();

    }
    @Override
    public void onResponseReceived(OFConstants.ApiHitType hitType, Object obj, Long reserve, String reserved, Object Obj2, Object obj3) {
        OFHelper.v(tag, "1Flow reached onResponseEvent["+hitType+"]obj["+obj+"]");
        switch (hitType) {
            case fetchEventsFromDB:
                if (obj != null) {
                    List<OFRecordEventsTabKT> list = (List<OFRecordEventsTabKT>) obj;
                    OFHelper.v(tag, "1Flow Events size[" + new Gson().toJson(list) + "]");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sendLogsToAPI.setText("Send Events to API (" + list.size() + ")");
                        }
                    });

                }else{
                    OFHelper.v(tag, "1Flow Events size no event to submit");
                }
                break;

        }
    }

}