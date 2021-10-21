package com.oneflow.tryskysdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oneflow.tryskysdk.adapter.SurveyListAdapter;
import com.oneflow.tryskysdk.customwidgets.CustomTextView;
import com.oneflow.tryskysdk.model.events.RecordEventsTab;
import com.oneflow.tryskysdk.model.survey.GetSurveyListResponse;
import com.oneflow.tryskysdk.repositories.EventDBRepo;
import com.oneflow.tryskysdk.sdkdb.OneFlowSHP;
import com.oneflow.tryskysdk.utils.Constants;
import com.oneflow.tryskysdk.controller.FeedbackController;
import com.oneflow.tryskysdk.utils.Helper;
import com.oneflow.tryskysdk.utils.MyResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends SDKBaseActivity implements MyResponseHandler {

    String tag = this.getClass().getName();
    CustomTextView result, sendLogsToAPI;

    @BindView(R.id.list_of_survey)
    RecyclerView listOfSurvey;

    ArrayList<GetSurveyListResponse> slr;
    SurveyListAdapter addb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = (CustomTextView) findViewById(R.id.result);
        sendLogsToAPI = (CustomTextView) findViewById(R.id.send_log_to_api);
        ButterKnife.bind(this);


        slr = new ArrayList<>();
        addb = new SurveyListAdapter(this, slr, clickListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listOfSurvey.setLayoutManager(linearLayoutManager);
        listOfSurvey.setAdapter(addb);

        IntentFilter inf = new IntentFilter();
        inf.addAction("survey_list_fetched");
        inf.addAction("events_submitted");

        registerReceiver(listFetched, inf);
    }

    BroadcastReceiver listFetched = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Helper.v(tag,"OneFlow reached receiver");
            if(intent.getAction().equalsIgnoreCase("survey_list_fetched")) {
                slr = new OneFlowSHP(MainActivity.this).getSurveyList();
                addb.notifyMyList(slr);
            }else if(intent.getAction().equalsIgnoreCase("events_submitted")){
                EventDBRepo.fetchEvents(MainActivity.this, MainActivity.this, Constants.ApiHitType.fetchEventsFromDB);

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
            FeedbackController.recordEvents(MainActivity.this, tag, mapvalues, 150);

            /*Intent intent = new Intent(SurveyList.this, SurveyActivity.class);
            intent.putExtra("SurveyType", surveyItem);
            startActivity(intent);*/
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        FeedbackController.configure(this, "7oKyqBl/myk8h1Zkq1uSkxffXe9U+p6trHLqA2q1JOU=");
        EventDBRepo.fetchEvents(this, this, Constants.ApiHitType.fetchEventsFromDB);
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
                HashMap<String, String> mapvalues = new HashMap<String, String>();
                mapvalues.put("testKey1", "testValue1");
                mapvalues.put("testKey2", "testValue2");
                mapvalues.put("testKey3", "testValue3");
                FeedbackController.recordEvents(this, "empty18", mapvalues, 50);
                break;

            case R.id.configure_project:
                Helper.makeText(MainActivity.this, "Clicked on button conf", 1);
                FeedbackController.configure(this, "7oKyqBl/myk8h1Zkq1uSkxffXe9U+p6trHLqA2q1JOU=");
                break;
        }
    }

    @Override
    public void onResponseReceived(Constants.ApiHitType hitType, Object obj, int reserve) {
        switch (hitType) {
            case fetchEventsFromDB:
                List<RecordEventsTab> list = (List<RecordEventsTab>) obj;
                sendLogsToAPI.setText("Send Events to API (" + list.size() + ")");
                break;

        }
    }
}