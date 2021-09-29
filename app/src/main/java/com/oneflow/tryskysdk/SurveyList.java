package com.oneflow.tryskysdk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oneflow.tryskysdk.adapter.SurveyListAdapter;
import com.oneflow.tryskysdk.model.survey.GetSurveyListResponse;
import com.oneflow.tryskysdk.sdkdb.OneFlowSHP;
import com.oneflow.tryskysdk.utils.Constants;

import java.util.ArrayList;

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
            String tag = (String)v.getTag();
            Intent intent = new Intent(SurveyList.this, SurveyActivity.class);
            intent.putExtra("SurveyType", tag);//"move_file_in_folder");//""empty0");//
            startActivity(intent);
        }
    };
}
