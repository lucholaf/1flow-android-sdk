package com.oneflow.tryskysdk;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.oneflow.tryskysdk.adapter.SurveyAdapter;
import com.oneflow.tryskysdk.customwidgets.CustomViewPager;
import com.oneflow.tryskysdk.model.survey.GetSurveyListResponse;
import com.oneflow.tryskysdk.model.survey.SurveyScreens;
import com.oneflow.tryskysdk.sdkdb.OneFlowSHP;
import com.oneflow.tryskysdk.utils.Constants;
import com.oneflow.tryskysdk.utils.Helper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SurveyActivity extends SDKBaseActivity{

    @BindView(R.id.survey_viewpager)
    CustomViewPager surveyQueViewPager;

    String tag = this.getClass().getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey_view);

        ButterKnife.bind(this);

       String surveyType = this.getIntent().getStringExtra("SurveyType");

       ArrayList<SurveyScreens> screens = checkSurveyTitleAndScreens(surveyType);
       if(screens!=null) {
           SurveyAdapter sa = new SurveyAdapter(getSupportFragmentManager(), screens);
           surveyQueViewPager.setAdapter(sa);
       }else{
           Helper.makeText(this,"Incorrect survey",1);
       }
        /*calViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/

    }
    private ArrayList<SurveyScreens> checkSurveyTitleAndScreens(String type){
        ArrayList<GetSurveyListResponse> slr = new OneFlowSHP(this).getSurveyList(Constants.SURVEYLISTSHP);
        ArrayList<SurveyScreens> surveyScreens = null;
        int counter = 0;
        Helper.v(tag,"OneFlow list size["+slr.size()+"]type["+type+"]");
        for(GetSurveyListResponse item:slr){
            if(item.getTrigger_event_name().equalsIgnoreCase(type)){
                surveyScreens = item.getScreens();
                Helper.v(tag,"OneFlow survey found at ["+(counter++)+"]triggerName["+item.getTrigger_event_name()+"]queSize["+item.getScreens().size()+"]");
                Helper.v(tag,"OneFlow survey queSize["+new Gson().toJson(item.getScreens())+"]");
                /*int i=0;
                while(i<item.getScreens().size()) {
                    try {
                        if(item.getScreens().get(i).getInput()!=null) {
                            Helper.v(tag, "OneFlow input type["+i+"][" + item.getScreens().get(i).getInput().getInput_type() + "]");
                        }else{
                            Helper.v(tag,"OneFlow found null");
                        }
                    }catch(Exception ex){

                    }
                i++;
                }*/
                break;
            }
        }
        return surveyScreens;
    }
}
