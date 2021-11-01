package com.circo.oneflow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.circo.oneflow.adapter.SurveyListAdapter;
import com.circo.oneflow.customwidgets.CustomTextView;
import com.circo.oneflow.model.events.RecordEventsTab;
import com.circo.oneflow.model.survey.GetSurveyListResponse;
import com.circo.oneflow.repositories.EventDBRepo;
import com.circo.oneflow.sdkdb.OneFlowSHP;
import com.circo.oneflow.utils.Constants;
import com.circo.oneflow.controller.OneFlow;
import com.circo.oneflow.utils.Helper;
import com.circo.oneflow.utils.MyResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends SDKBaseActivity implements MyResponseHandler {

    String tag = this.getClass().getName();
    CustomTextView result, sendLogsToAPI;

    RecyclerView listOfSurvey;

    ArrayList<GetSurveyListResponse> slr;
    SurveyListAdapter addb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = (CustomTextView) findViewById(R.id.result);
        sendLogsToAPI = (CustomTextView) findViewById(R.id.send_log_to_api);
        listOfSurvey = (RecyclerView) findViewById(R.id.list_of_survey);

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

        OneFlow.configure(this, "7oKyqBl/myk8h1Zkq1uSkxffXe9U+p6trHLqA2q1JOU=");
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
            OneFlow.recordEvents(MainActivity.this, tag, mapvalues, 150);

            /*Intent intent = new Intent(SurveyList.this, SurveyActivity.class);
            intent.putExtra("SurveyType", surveyItem);
            startActivity(intent);*/
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        EventDBRepo.fetchEvents(this, this, Constants.ApiHitType.fetchEventsFromDB);
    }

    public void clickHandler(View v) {

        if(v.getId()==R.id.get_location) {
            Helper.makeText(MainActivity.this, "Clicked on button 3", 1);
            //new FeedbackController(this).getLocation();
        }
        else if(v.getId()==R.id.fetch_survey_list){
                Helper.makeText(MainActivity.this, "Clicked on button 4", 1);
                //FeedbackController.getSurvey(this);
        }
        else if(v.getId()==R.id.project_details){
                Helper.makeText(MainActivity.this, "Clicked on button 0", 1);
                //new FeedbackController(this).getProjectDetails();
        }
        else if(v.getId()==R.id.send_log_to_api) {
            Helper.makeText(MainActivity.this, "Clicked on button 0", 1);
            OneFlow.sendEventsToApi(this);
        }
        else if(v.getId()==R.id.record_log){
                Helper.makeText(MainActivity.this, "Clicked on button 5", 1);
                HashMap<String, String> mapvalues = new HashMap<String, String>();
                mapvalues.put("testKey1", "testValue1");
                mapvalues.put("testKey2", "testValue2");
                mapvalues.put("testKey3", "testValue3");
                OneFlow.recordEvents(this, "empty18", mapvalues, 50);
        }
        else if(v.getId()==R.id.configure_project){
                Helper.makeText(MainActivity.this, "Clicked on button conf", 1);
                OneFlow.configure(this, "7oKyqBl/myk8h1Zkq1uSkxffXe9U+p6trHLqA2q1JOU=");
        }
        else if(v.getId()==R.id.log_user){
                HashMap<String,String> mapValue = new HashMap<>();
                mapValue.put("firstName","RamLal");
                mapValue.put("lastName","Goswami");
                mapValue.put("number","1234");
                OneFlow.logUser("ThisIsUniqueIdForUser",mapValue);

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