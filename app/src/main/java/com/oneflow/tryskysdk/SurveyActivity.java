package com.oneflow.tryskysdk;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.oneflow.tryskysdk.fragment.SurveyQueFragment;
import com.oneflow.tryskysdk.fragment.SurveyQueTextFragment;
import com.oneflow.tryskysdk.fragment.SurveyQueThankyouFragment;
import com.oneflow.tryskysdk.model.survey.SurveyUserResponse;
import com.oneflow.tryskysdk.model.survey.SurveyUserResponseChild;
import com.oneflow.tryskysdk.model.survey.GetSurveyListResponse;
import com.oneflow.tryskysdk.model.survey.SurveyScreens;
import com.oneflow.tryskysdk.repositories.Survey;
import com.oneflow.tryskysdk.sdkdb.OneFlowSHP;
import com.oneflow.tryskysdk.utils.Constants;
import com.oneflow.tryskysdk.utils.Helper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SurveyActivity extends AppCompatActivity {

   /* @BindView(R.id.survey_viewpager)
   public CustomViewPager surveyQueViewPager;*/

    @BindView(R.id.close_btn_image_view)
    ImageView closeBtn;

    @BindView(R.id.fragment_view)
    FrameLayout fragmentView;

    String tag = this.getClass().getName();

    public ArrayList<SurveyUserResponseChild> surveyResponseChildren;
    private ArrayList<SurveyScreens> screens;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.survey_view);

        Window window = this.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.flags &= WindowManager.LayoutParams.FLAG_DIM_BEHIND;

        window.setAttributes(wlp);

        ButterKnife.bind(this);

       String surveyType = this.getIntent().getStringExtra("SurveyType");

       screens = checkSurveyTitleAndScreens(surveyType);




        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SurveyActivity.this.finish();
            }
        });
        surveyResponseChildren = new ArrayList<>();

        initFragment();

    }
    public void checkIfAnswerAlreadyGiven(){

    }

    /**
     * Record User inputs
     * @param screenID
     * @param answerIndex
     * @param answerValue
     */
    public void addUserResponseToList(String screenID,String answerIndex, String answerValue){
        SurveyUserResponseChild asrc = new SurveyUserResponseChild();
        asrc.setScreen_id(screenID);
        if(answerValue!=null){
            asrc.setAnswer_value(answerValue);
        }else{
            asrc.setAnswer_index(answerIndex);
        }

        surveyResponseChildren.add(asrc);
        initFragment();
    }
    public String themeColor = "";
    String selectedSurveyId;
    /**
     * This method will check if trigger name is available in the list or not
     * @param type
     * @return
     */
    private ArrayList<SurveyScreens> checkSurveyTitleAndScreens(String type){
        ArrayList<GetSurveyListResponse> slr = new OneFlowSHP(this).getSurveyList();
        ArrayList<SurveyScreens> surveyScreens = null;
        int counter = 0;
        Helper.v(tag,"OneFlow list size["+slr.size()+"]type["+type+"]");
        for(GetSurveyListResponse item:slr){
            if(item.getTrigger_event_name().equalsIgnoreCase(type)){
                surveyScreens = item.getScreens();
                selectedSurveyId = item.get_id();
                themeColor = item.getThemeColor();
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

    public void prepareAndSubmitUserResposne(){
        SurveyUserResponse sur = new SurveyUserResponse();
        sur.setAnswers(surveyResponseChildren);
        sur.setOs(Constants.os);
        sur.setAnalytic_user_id(new OneFlowSHP(this).getUserDetails().getAnalytic_user_id());
        sur.setSurvey_id(selectedSurveyId);
        Survey.submitUserResponse(this,sur);
    }

    public int position = 0;
    public void initFragment(){
        Helper.v(tag,"OneFlow selected answers["+new Gson().toJson(surveyResponseChildren)+"]");
        if(position>=screens.size()){
            prepareAndSubmitUserResposne();
            SurveyActivity.this.finish();
        }else {
            loadFragments(screens.get(position));
        }

    }
    private void loadFragments(SurveyScreens screen){
        Fragment frag;
        try {
            if (screen.getInput().getInput_type().equalsIgnoreCase("thank_you")) {
                frag = SurveyQueThankyouFragment.newInstance(screen);
            } else if (screen.getInput().getInput_type().equalsIgnoreCase("text")) {
                frag = SurveyQueTextFragment.newInstance(screen);
            } else {
                frag = SurveyQueFragment.newInstance(screen);
            }
        }catch(Exception ex){
            Helper.e(tag,"OneFlow ERROR ["+ex.getMessage()+"]");
            frag = SurveyQueThankyouFragment.newInstance(screen);
        }


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(position==0) {
            ft.add(R.id.fragment_view, frag).commit();
        }else{
            ft.replace(R.id.fragment_view,frag).commit();
        }
    }


}
