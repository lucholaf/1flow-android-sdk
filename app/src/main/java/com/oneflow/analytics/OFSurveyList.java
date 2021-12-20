package com.oneflow.analytics;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oneflow.analytics.adapter.OFSurveyListAdapter;
import com.oneflow.analytics.model.survey.OFGetSurveyListResponse;
import com.oneflow.analytics.sdkdb.OFOneFlowSHP;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;

public class OFSurveyList extends OFSDKBaseActivity {

    RecyclerView listOfSurvey;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_survey_list);
        ButterKnife.bind(this);


        listOfSurvey = (RecyclerView) findViewById(R.id.list_of_survey);

        ArrayList<OFGetSurveyListResponse> slr = new OFOneFlowSHP(this).getSurveyList();
        OFSurveyListAdapter addb = new OFSurveyListAdapter(this, slr, clickListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listOfSurvey.setLayoutManager(linearLayoutManager);
        listOfSurvey.setAdapter(addb);



    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String tag = (String) v.getTag();

            //GetSurveyListResponse surveyItem = checkSurveyTitleAndScreens(tag);

            HashMap<String,String> mapvalues = new HashMap<String, String>();
            mapvalues.put("testKey1","testValue1");
            mapvalues.put("testKey2","testValue2");
            mapvalues.put("testKey3","testValue3");
            OneFlow.recordEvents(tag,mapvalues);

            /*Intent intent = new Intent(SurveyList.this, SurveyActivity.class);
            intent.putExtra("SurveyType", surveyItem);
            startActivity(intent);*/
        }
    };
}