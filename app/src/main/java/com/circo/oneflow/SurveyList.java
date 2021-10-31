package com.circo.oneflow;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.circo.oneflow.adapter.SurveyListAdapter;
import com.circo.oneflow.model.survey.GetSurveyListResponse;
import com.circo.oneflow.sdkdb.OneFlowSHP;
import com.circo.oneflow.controller.OneFlow;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SurveyList extends SDKBaseActivity {
    @BindView(R.id.list_of_survey)
    RecyclerView listOfSurvey;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_survey_list);
        ButterKnife.bind(this);

        ArrayList<GetSurveyListResponse> slr = new OneFlowSHP(this).getSurveyList();
        SurveyListAdapter addb = new SurveyListAdapter(this, slr, clickListener);
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
            OneFlow.recordEvents(SurveyList.this,tag,mapvalues,150);

            /*Intent intent = new Intent(SurveyList.this, SurveyActivity.class);
            intent.putExtra("SurveyType", surveyItem);
            startActivity(intent);*/
        }
    };
}