package com.oneflow.analytics;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.oneflow.analytics.controller.OFEventController;
import com.oneflow.analytics.customwidgets.OFCustomeWebView;
import com.oneflow.analytics.model.survey.OFGetSurveyListResponse;
import com.oneflow.analytics.model.survey.OFSurveyUserInput;
import com.oneflow.analytics.model.survey.OFThrottlingConfig;
import com.oneflow.analytics.repositories.OFLogUserDBRepoKT;
import com.oneflow.analytics.sdkdb.OFOneFlowSHP;
import com.oneflow.analytics.utils.OFConstants;
import com.oneflow.analytics.utils.OFDelayedSurveyCountdownTimer;
import com.oneflow.analytics.utils.OFFilterSurveys;
import com.oneflow.analytics.utils.OFHelper;
import com.oneflow.analytics.utils.OFMyResponseHandlerOneFlow;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class OFFirstLanderActivity extends AppCompatActivity implements OFMyResponseHandlerOneFlow {
    OFCustomeWebView wv;
    //OFGetSurveyListResponse surveyItem;
    String triggerEventName;
    String eventData;
    String tag = this.getClass().getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getSupportActionBar().hide();

        setContentView(R.layout.lander_page);
        wv = findViewById(R.id.webview_contents_lander);
        //surveyItem = (OFGetSurveyListResponse) this.getIntent().getSerializableExtra("SurveyType");
        triggerEventName = this.getIntent().getStringExtra("eventName");//surveyItem.getTrigger_event_name();
        eventData = this.getIntent().getStringExtra("eventData");
        OFHelper.v(tag, "1Flow webmethod called [" + eventData + "]");


        new OFFilterSurveys(OFFirstLanderActivity.this, OFFirstLanderActivity.this, OFConstants.ApiHitType.Config.filterSurveys, triggerEventName).start();

        setUpHashForActivity();

    }


    String jsFunction = "";

    private void checkWebviewFunction(ArrayList<OFGetSurveyListResponse> returningList) {
        OFHelper.v(tag, "1Flow webmethod called 0");
        //String jsCode = getFileContents("logic-engine1.js");
        String jsCode = getFileContents(getCacheDir().getPath() + File.separator + OFConstants.cacheFileName);
        //String jsCode = "function oneFlowFilterSurvey(survey,event){alert(\"I am js alert\");}";

        if (jsCode == null) {
            jsCode = getFileContentsFromLocal(OFConstants.cacheFileName);
        }
        if (jsCode != null) {
            OFOneFlowSHP shp = OFOneFlowSHP.getInstance(OFFirstLanderActivity.this);
            try {

                jsFunction = "oneFlowFilterSurvey(" + new Gson().toJson(returningList) + "," + eventData + ")";

            } catch (Exception ex) {
                OFHelper.e(tag, "1Flow error[" + ex.getMessage() + "]");
            }

            //OFHelper.v(tag, "1Flow webmethod called2[" + OFHelper.formatedDate(System.currentTimeMillis(), "dd-MM-yyyy hh:mm:ss") + "]");

            String jsCallerMethod = "function oneFlowCallBack(survey){ console.log(\"reached at callback method\"); android.onResultReceived(JSON.stringify(survey));}";
            String finalCode = jsCode + "\n\n" + jsFunction + "\n\n" + jsCallerMethod;
            //String finalCode = jsFunction+"\n\n" + jsCallerMethod;
            //  OFHelper.v(tag, "1Flow webmethod called2.1 [" +finalCode + "]");

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    wv.getSettings().setJavaScriptEnabled(true);
                    wv.addJavascriptInterface(new MyJavaScriptInterface(), "android");
                    wv.setWebChromeClient(new WebChromeClient() {
                        @Override
                        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                            if (consoleMessage.messageLevel() == ConsoleMessage.MessageLevel.ERROR) {
                                OFHelper.e(tag, "1Flow webpage JS Error[" + consoleMessage.message() + "]");
                                OFFirstLanderActivity.this.finish();
                            } else {
                                OFHelper.v(tag, "1Flow webpage JS log[" + consoleMessage.message() + "]");
                            }

                            return true;

                        }
                    });

                    // wv.evaluateJavascript(jsCode + jsFunction + "\n\n" + jsCallerMethod, new ValueCallback<String>() {
                    wv.evaluateJavascript(finalCode, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String result) {

                        }
                    });
                }
            });
        } else {
            OFFirstLanderActivity.this.finish();
        }
    }


    @Override
    public void onResponseReceived(OFConstants.ApiHitType hitType, Object obj1, Long reserve, String reserved, Object obj2, Object obj3) {
        switch (hitType) {
            case lastSubmittedSurvey:
                OFHelper.v(tag, "1Flow globalThrottling received[" + obj1 + "]");

                OFGetSurveyListResponse gslrTH = (OFGetSurveyListResponse) obj2;
                if (obj1 == null) {
                    launchSurvey(gslrTH);
                } else {
                    OFSurveyUserInput ofSurveyUserInput = (OFSurveyUserInput) obj1;
                    OFOneFlowSHP ofs1 = OFOneFlowSHP.getInstance(OFFirstLanderActivity.this);
                    OFThrottlingConfig config = ofs1.getThrottlingConfig();
                    OFHelper.v(tag, "1Flow globalThrottling inside else [" + (ofSurveyUserInput.getCreatedOn() < config.getActivatedAt()) + "]");
                    if (ofSurveyUserInput.getCreatedOn() < config.getActivatedAt()) {
                        launchSurvey(gslrTH);
                    }
                }

                break;
            case filterSurveys:

                OFHelper.v("1Flow", "1Flow filterSurvey came back [" + obj1 + "]");
                if (obj1 != null) {
                    ArrayList<OFGetSurveyListResponse> returningList = (ArrayList<OFGetSurveyListResponse>) obj1;

                    OFHelper.v("1Flow", "1Flow filterSurvey came back size[" + returningList.size() + "]");

                    if (returningList.size() > 0) {
                        checkWebviewFunction(returningList);
                    } else {
                        OFFirstLanderActivity.this.finish();
                    }
                }

                break;
        }
    }


    public class MyJavaScriptInterface {


        public MyJavaScriptInterface() {

        }

        @JavascriptInterface
        public void onResultReceived(String resultJson) {

            OFHelper.v(tag, "1Flow JavaScript returns1: " + resultJson);
            if (resultJson.equalsIgnoreCase("null")) {
                OFFirstLanderActivity.this.finish();
            } else {
                if (!OFHelper.validateString(resultJson).equalsIgnoreCase("NA")) {
                    if (!resultJson.equalsIgnoreCase("undefined")) {
                        Gson gson = new Gson();
                        OFGetSurveyListResponse result = gson.fromJson(resultJson, OFGetSurveyListResponse.class);
                        handleResult(result);
                    } else {
                        OFFirstLanderActivity.this.finish();
                    }
                } else {
                    OFFirstLanderActivity.this.finish();
                }
            }

        }

        private void handleResult(OFGetSurveyListResponse result) {
            // Do something with the result
            OFHelper.v(tag, "1Flow JavaScript returns2: " + result.toString());
            if (result != null) {
                //OFFirstLanderActivity.this.finish();
                //launchSurvey();
                throtlingCheck(result);
            } else {
                OFFirstLanderActivity.this.finish();
                OFHelper.v(tag, "1Flow event flow check failed no survey launch");
            }
        }


    }


    Long delayDuration;
    public void throtlingCheck (OFGetSurveyListResponse surveyToInit){
        OFOneFlowSHP ofs = OFOneFlowSHP.getInstance(OFFirstLanderActivity.this);
        OFThrottlingConfig throttlingConfig = ofs.getThrottlingConfig();
        if (throttlingConfig == null) {
            //triggerSurvey(gslr, reserved)
            launchSurvey(surveyToInit);
        } else {

            OFHelper.v(tag, "1Flow globalThrottling[" + surveyToInit.getSurveySettings().getOverrideGlobalThrottling() + "]throttlingConfig isActivated[" + throttlingConfig.isActivated() + "]");


            if (surveyToInit.getSurveySettings().getOverrideGlobalThrottling()) {
                //triggerSurvey(gslr, reserved);
                launchSurvey(surveyToInit);
            } else {
                if (throttlingConfig.isActivated()) {
                    if (throttlingConfig.getActivatedById().equalsIgnoreCase(surveyToInit.get_id())) {

                        OFHelper.v(tag, "1Flow globalThrottling id matched ");
                        //gslrGlobal = gslr;
                        // check in submitted survey list locally if this survey has been submitted then false
                        new OFLogUserDBRepoKT().findLastSubmittedSurveyID(OFFirstLanderActivity.this, this, OFConstants.ApiHitType.lastSubmittedSurvey, triggerEventName, surveyToInit);

//                                                        new OFMyDBAsyncTask(mContext,this, OFConstants.ApiHitType.lastSubmittedSurvey,false).execute();

                    }
                    //else nothing to do
                } else {
                    //triggerSurvey(gslr, reserved);
                    launchSurvey(surveyToInit);
                }
            }

        }
    }
    Intent surveyIntent = null;
    public void launchSurvey (OFGetSurveyListResponse surveyToInit){
        OFHelper.v(tag, "1Flow eventName[" + triggerEventName + "]surveyId[" + surveyToInit.get_id() + "]");

        if (surveyToInit.getSurveySettings().getSdkTheme().getWidgetPosition() == null) {
            surveyIntent = new Intent(getApplicationContext(), activityName.get("bottom-center"));
        } else {
            surveyIntent = new Intent(getApplicationContext(), activityName.get(surveyToInit.getSurveySettings().getSdkTheme().getWidgetPosition()));
        }
        //surveyIntent = new Intent(getApplicationContext(), OFFirstLanderActivity.class);

        OFOneFlowSHP ofs1 = OFOneFlowSHP.getInstance(this);

        HashMap<String, Object> mapValue = new HashMap<>();
        mapValue.put("flow_id", surveyToInit.get_id());
        OFEventController ec = OFEventController.getInstance(this);
        ec.storeEventsInDB(OFConstants.AUTOEVENT_SURVEYIMPRESSION, mapValue, 0);

        ofs1.storeValue(OFConstants.SHP_SURVEY_RUNNING, true);
        ofs1.storeValue(OFConstants.SHP_SURVEYSTART, Calendar.getInstance().getTimeInMillis());


        OFThrottlingConfig config = ofs1.getThrottlingConfig();
        //flow correction and time should be in second
        if (config != null) {

            OFHelper.v(tag, "1Flow throttling config not null");
            config.setActivated(true);
            config.setActivatedById(surveyToInit.get_id());
            config.setActivatedAt(System.currentTimeMillis());

            ofs1.setThrottlingConfig(config);

            setupGlobalTimerToDeactivateThrottlingLocally();
        } else {
            OFHelper.v(tag, "1Flow throttling config null");
        }

        // resetting counter for similar type of event name
        //ofs1.storeValue(OFConstants.SHP_SURVEY_SEARCH_POSITION, 0);

        surveyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        surveyIntent.putExtra("SurveyType", surveyToInit);
        surveyIntent.putExtra("eventName", triggerEventName);

        OFHelper.v(tag, "1Flow activity running[" + OFSDKBaseActivity.isActive + "]");

        if (!OFSDKBaseActivity.isActive) {

            if (surveyToInit.getSurveyTimeInterval() != null) {

                if (surveyToInit.getSurveyTimeInterval().getType().equalsIgnoreCase("show_after")) {

                    try {
                        delayDuration = surveyToInit.getSurveyTimeInterval().getValue() * 1000;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    OFHelper.v("1Flow", "1Flow activity waiting duration[" + delayDuration + "]");


                    ContextCompat.getMainExecutor(this).execute(new Runnable() {
                        @Override
                        public void run() {
                            OFDelayedSurveyCountdownTimer delaySurvey = OFDelayedSurveyCountdownTimer.getInstance(OFFirstLanderActivity.this, delayDuration, 1000l, surveyIntent);
                            delaySurvey.start();
                        }
                    });


                } else {
                    startActivity(surveyIntent);
                }

            } else {
                startActivity(surveyIntent);
            }

            OFFirstLanderActivity.this.finish();
        }
    }


    private void setupGlobalTimerToDeactivateThrottlingLocally () {


        OFHelper.v(tag, "1Flow deactivate called ");
        OFThrottlingConfig config = OFOneFlowSHP.getInstance(this).getThrottlingConfig();
        OFHelper.v(tag, "1Flow deactivate called config activated[" + config.isActivated() + "]globalTime[" + config.getGlobalTime() + "]activatedBy[" + config.getActivatedById() + "]");
        //OFMyCountDownTimerThrottling.getInstance(mContext,0l,0l).cancel();
        if (config.getGlobalTime() != null && config.getGlobalTime() > 0) {
            //OFMyCountDownTimerThrottling.getInstance(mContext, config.getGlobalTime() * 1000, ((Long) (config.getGlobalTime() * 1000) / 2)).start();
            setThrottlingAlarm(config);
        } else {
            OFHelper.v(tag, "1Flow deactivate called at else");
            config.setActivated(false);
            config.setActivatedById(null);
            OFOneFlowSHP.getInstance(this).setThrottlingConfig(config);
        }


    }

    public void setThrottlingAlarm (OFThrottlingConfig config){
        OFHelper.v(tag, "1Flow Setting ThrottlingAlarm [" + config.getGlobalTime() + "]");

        OFOneFlowSHP shp = OFOneFlowSHP.getInstance(this);
        shp.storeValue(OFConstants.SHP_THROTTLING_TIME, config.getGlobalTime() * 1000 + System.currentTimeMillis());


    }

    private String getCacheFileContents (String cacheFileName){
        String fileContents = "";
        try {
            String cacheFilePath = getCacheDir().getPath() + File.separator + cacheFileName;
            FileInputStream fileInputStream = new FileInputStream(cacheFilePath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {

                stringBuilder.append(line);
            }

            fileContents = stringBuilder.toString();
            OFHelper.v(tag, fileContents);
            // Do something with the file contents
            // For example, parse JSON or display the text

            bufferedReader.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContents;
    }

    private String getFileContents(String fileName) {
        try {

            FileInputStream is = new FileInputStream(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getFileContentsFromLocal(String fileName) {
        try {
            InputStream is = getAssets().open(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    HashMap<String, Class> activityName;

    public void setUpHashForActivity () {
        activityName = new HashMap<>();

        activityName.put("top-banner", OFSurveyActivityBannerTop.class);
        activityName.put("bottom-banner", OFSurveyActivityBannerBottom.class);

        activityName.put("fullscreen", OFSurveyActivityFullScreen.class);

        activityName.put("top-left", OFSurveyActivityTop.class);
        activityName.put("top-center", OFSurveyActivityTop.class);
        activityName.put("top-right", OFSurveyActivityTop.class);

        activityName.put("middle-left", OFSurveyActivityCenter.class); //name changed
        activityName.put("middle-center", OFSurveyActivityCenter.class); //name changed
        activityName.put("middle-right", OFSurveyActivityCenter.class); //name changed

        activityName.put("bottom-left", OFSurveyActivityBottom.class);
        activityName.put("bottom-center", OFSurveyActivityBottom.class); //default one
        activityName.put("bottom-right", OFSurveyActivityBottom.class);


    }

    @Override
    public void onBackPressed () {
        super.onBackPressed();


        OFOneFlowSHP.getInstance(this).storeValue(OFConstants.SHP_SURVEY_RUNNING, false);
        OFHelper.v(this.getClass().getName(), "1Flow onStop called");


    }


}
