package com.oneflow.analytics.fragment;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.gson.Gson;
import com.oneflow.analytics.R;
import com.oneflow.analytics.controller.OFEventController;
import com.oneflow.analytics.model.survey.OFDataLogic;
import com.oneflow.analytics.model.survey.OFGetSurveyListResponse;
import com.oneflow.analytics.model.survey.OFSDKSettingsTheme;
import com.oneflow.analytics.model.survey.OFSurveyScreens;
import com.oneflow.analytics.model.survey.OFSurveyUserResponseChild;
import com.oneflow.analytics.sdkdb.OFOneFlowSHP;
import com.oneflow.analytics.utils.OFConstants;
import com.oneflow.analytics.utils.OFHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class CustomFrag extends Fragment {

    String tag = this.getClass().getName();
    OFSurveyScreens surveyScreens;
    ArrayList<OFSurveyUserResponseChild> surveyResponseChildren;
    OFGetSurveyListResponse surveyItem;
    static CustomFrag myFragment;
    public static CustomFrag newInstance(){//OFGetSurveyListResponse ahdList,String eventName) {


        if(myFragment ==null){
             myFragment = new CustomFrag();
        }
        /*Bundle args = new Bundle();
        args.putSerializable("SurveyType", ahdList);
        args.putString("eventName", eventName);
        myFragment.setArguments(args);*/

        return myFragment;
    }

    public ArrayList<OFSurveyScreens> screens;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        /*surveyItem = (OFGetSurveyListResponse) getArguments().getSerializable("SurveyType");
        triggerEventName = (String)getArguments().getString("eventName");//surveyItem.getTrigger_event_name();*/

    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setVisibility(View.GONE);
        IntentFilter inf = new IntentFilter();
        inf.addAction("CustomView");
        getActivity().registerReceiver(br, inf);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(br);
    }

    BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            OFHelper.v("SecondActivity broadcast recevice","OneFlow receiver called["+intent.getStringExtra("eventName")+"]");
            surveyItem = (OFGetSurveyListResponse) intent.getSerializableExtra("SurveyType");
            triggerEventName = intent.getStringExtra("eventName");
            if(surveyItem!=null) {
                getView().setVisibility(View.VISIBLE);
                initSurvey();
            }
        }
    };
    ProgressBar pagePositionPBar;
    ImageView closeBtn;
    View slider;
    RelativeLayout sliderLayout, basePopupLayout, mainChildForBackground;
    FrameLayout fragmentView;
    Long inTime = 0l;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.survey_view_custom_frag, container, false);

        inTime = System.currentTimeMillis();
        OFHelper.v(tag, "OneFlow reached at surveyActivity");
        pagePositionPBar = (ProgressBar) view.findViewById(R.id.pbar);
        closeBtn = (ImageView) view.findViewById(R.id.close_btn_image_view);

        basePopupLayout = (RelativeLayout) view.findViewById(R.id.base_popup_layout);
        mainChildForBackground = (RelativeLayout) view.findViewById(R.id.view_layout);
        fragmentView = (FrameLayout) view.findViewById(R.id.fragment_view);


        return view;

    }

    public int position = 0;

    public void initFragment() {
        OFHelper.v(tag, "OneFlow answer position [" + position + "]screensize[" + screens.size() + "]selected answers[" + new Gson().toJson(surveyResponseChildren) + "]");
//        OFHelper.v(tag, "OneFlow answer position [" +new Gson().toJson(screens.get(position-1) )+ "]");
        if (position >= screens.size()) {
            //TODO FINISH OR HIDE THIS FRAGMENT
            ((ViewGroup)getView().getParent()).setVisibility(View.GONE);
            //this.finish();
            //overridePendingTransition(0,R.anim.slide_down_dialog);
            //slideDown();
        } else {
            loadFragments();
        }
        OFHelper.v(tag, "OneFlow answer position after[" + position + "]screensize[" + screens.size() + "]selected answers[" + new Gson().toJson(surveyResponseChildren) + "]");
    }


    void setProgressMax(int max) {
        OFHelper.v(tag, "OneFlow max [" + max + "]postion[" + position + "]");
        pagePositionPBar.setMax(max * 100);
    }

    private void setProgressAnimate() {
        // OFHelper.v(tag, "OneFlow animation started [" + position + "] max [" + pagePositionPBar.getProgress() + "]postion[" + (position * 100) + "]");
        if (position == 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ObjectAnimator animation = ObjectAnimator.ofInt(pagePositionPBar, "progress", 0, 100);
                    animation.setDuration(500);
                    animation.setAutoCancel(true);
                    animation.setInterpolator(new DecelerateInterpolator());
                    animation.start();
                }
            }, 500);
        } else {
            ObjectAnimator animation = ObjectAnimator.ofInt(pagePositionPBar, "progress", pagePositionPBar.getProgress(), (position + 1) * 100);
            animation.setDuration(500);
            animation.setAutoCancel(true);
            animation.setInterpolator(new DecelerateInterpolator());
            animation.start();
        }
    }


    private void loadFragments() {

        setProgressAnimate();

        //if (screen != null) {
        Fragment frag = getFragment();

        if (frag != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            //FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (position == 0) {
                ft.add(R.id.fragment_view, frag).commit();
            } else {
                ft.replace(R.id.fragment_view, frag).commit();
            }
        } else {
            //Helper.makeText(getApplicationContext(), "frag null", 1);
        }

    }

    public Fragment getFragment() {
        Fragment frag = null;
        try {
            OFSurveyScreens screen = validateScreens();
            if (screen != null) {
                if (screen.getInput().getInput_type().equalsIgnoreCase("thank_you")) {
                    pagePositionPBar.setVisibility(View.GONE);
                    frag = OFSurveyQueThankyouFragment.newInstance(screen,sdkTheme,themeColor);
                } else if (screen.getInput().getInput_type().equalsIgnoreCase("text")) {
                    frag = OFSurveyQueTextFragment.newInstance(screen,sdkTheme,themeColor);
                } else {
                    frag = OFSurveyQueFragment.newInstance(screen,sdkTheme,themeColor);
                }
            }
        } catch (Exception ex) {
            OFHelper.e(tag, "OneFlow ERROR [" + ex.getMessage() + "]");
            //frag = SurveyQueThankyouFragment.newInstance(screen);
        }
        return frag;
    }

    /**
     * Record User inputs
     *
     * @param screenID screenId is required to map with question
     * @param answerIndex to check answer position
     * @param answerValue
     */
    public void addUserResponseToList(String screenID, String answerIndex, String answerValue) {

        OFHelper.v(tag, "OneFlow answerindex position 0 [" + position + "][" + answerIndex + "]answervalue[" + answerValue + "]");
        //this condition for skipping question
        if (answerIndex != null || answerValue != null) {
            OFSurveyUserResponseChild asrc = new OFSurveyUserResponseChild();
            asrc.setScreen_id(screenID);

            if (answerValue != null) {
                asrc.setAnswer_value(answerValue);
            }
            if (answerIndex != null) {
                asrc.setAnswer_index(answerIndex);
            }
            boolean found = false;
            // checking if list already have same value
            for (OFSurveyUserResponseChild src : surveyResponseChildren) {
                if (src.getScreen_id() == screenID) {
                    OFHelper.v(tag, "OneFlow Replacing Value");
                    found = true;
                    Collections.replaceAll(surveyResponseChildren, src, asrc);
                }
            }
            if (!found) {
                surveyResponseChildren.add(asrc);
            }
        }
        OFHelper.v(tag, "OneFlow position [" + position + "]");
        try {
           // OFHelper.v(tag, "OneFlow rules [" + new Gson().toJson(screens.get(position - 1).getRules()) + "]");
            if (screens.get(position - 1).getRules() != null) {
                preparePositionOnRule(screenID, answerIndex, answerValue);
            } else {
                initFragment();
            }
        } catch (Exception ex) {
            OFHelper.e(tag, "Survey Result error[" + ex.getMessage() + "]");
        }
    }
    String surveyName = "";
    public OFSDKSettingsTheme sdkTheme;
    String triggerEventName = "";
    String surveyClosingStatus = "finished";
    @Override
    public void onStart() {
        super.onStart();



    }
    public void initSurvey(){
        surveyName = surveyItem.getName();
        screens = surveyItem.getScreens();

        // Helper.makeText(getApplicationContext(),"Size ["+screens.size()+"]",1);
        setProgressMax(surveyItem.getScreens().size() - 1); // -1 for excluding thankyou page from progress bar
        selectedSurveyId = surveyItem.get_id();
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //closed survey logic for storage.
                OFOneFlowSHP ofs = OFOneFlowSHP.getInstance(getActivity());
                ArrayList<String> closedSurveyList = ofs.getClosedSurveyList();
                if (closedSurveyList == null) {
                    closedSurveyList = new ArrayList<>();
                }
                OFHelper.v(tag, "OneFlow close button clicked [" + surveyResponseChildren + "]");
                if (surveyResponseChildren == null || surveyResponseChildren.size() == 0) {

                    surveyClosingStatus = "skipped";
                    if (!closedSurveyList.contains(selectedSurveyId)) {
                        closedSurveyList.add(selectedSurveyId);
                        ofs.setClosedSurveyList(closedSurveyList);
                        OFEventController ec = OFEventController.getInstance(getActivity());
                        HashMap<String, Object> mapValue = new HashMap<>();
                        mapValue.put("survey_id", selectedSurveyId);
                        ec.storeEventsInDB(OFConstants.AUTOEVENT_CLOSED_SURVEY, mapValue, 0);
                    }

                } else {
                    surveyClosingStatus = "closed";
                }

                getActivity().finish();
                // overridePendingTransition(0,R.anim.slide_down_dialog);
            }
        });


        themeColor = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        OFHelper.v(tag, "OneFlow color 1[" + themeColor + "]primaryColor[" + surveyItem.getStyle().getPrimary_color() + "]");
        try {

            String tranparancy = "";
            if (surveyItem.getStyle().getPrimary_color().length() > 6 && !surveyItem.getStyle().getPrimary_color().startsWith("#")) {
                tranparancy = surveyItem.getStyle().getPrimary_color().substring(6, 8);
            }
            String tempColor;
            if (!surveyItem.getStyle().getPrimary_color().startsWith("#")) {
                tempColor = surveyItem.getStyle().getPrimary_color().substring(0, 6);
            } else {
                tempColor = surveyItem.getStyle().getPrimary_color().substring(1, 7);
            }


            if (!surveyItem.getStyle().getPrimary_color().startsWith("#")) {
                themeColor = "#" + tranparancy + tempColor;//surveyItem.getStyle().getPrimary_color();
            } else {
                themeColor = tranparancy + tempColor;//surveyItem.getStyle().getPrimary_color();
            }
            OFHelper.v(tag, "OneFlow colors transparancy [" + tranparancy + "]tempColor[" + tempColor + "]themeColor[" + themeColor + "]");
        } catch (Exception ex) {
            //styleColor=""+getResources().getColor(R.color.colorPrimaryDark);
        }
        //styleColor=String.valueOf(getResources().getColor(R.color.colorPrimaryDark));
        OFHelper.v(tag, "OneFlow color after[" + themeColor + "]");
        try {
            pagePositionPBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(themeColor)));
            //pagePositionPBar.getProgressDrawable().setColorFilter(Color.parseColor(styleColor.toString()), PorterDuff.Mode.DARKEN);
        } catch (NumberFormatException nfe) {
            OFHelper.e(tag, "OneFlow color number format exception after[" + nfe.getMessage() + "]");
            themeColor = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
            pagePositionPBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(themeColor)));
        }


        //This is temp remove in prod
        //surveyItem.getSurveySettings().getSdkTheme().setText_color(themeColor);

        sdkTheme = surveyItem.getSurveySettings().getSdkTheme();

        OFHelper.v(tag, "OneFlow sdkTheme [" + new Gson().toJson(sdkTheme) + "]");
        OFHelper.v(tag, "OneFlow sdkTheme Close[" + sdkTheme.getClose_button() + "]");
        OFHelper.v(tag, "OneFlow sdkTheme progress[" + sdkTheme.getProgress_bar() + "]");

        mainChildForBackground.setBackgroundColor(Color.parseColor(OFHelper.handlerColor(sdkTheme.getBackground_color())));

        Drawable closeIcon = closeBtn.getDrawable();
        closeIcon.setColorFilter(OFHelper.manipulateColor(Color.parseColor(OFHelper.handlerColor(sdkTheme.getText_color())), 0.6f), PorterDuff.Mode.SRC_ATOP);

        surveyResponseChildren = new ArrayList<>();


        OFHelper.v(tag, "OneFlow sdkTheme 0[" + sdkTheme + "]widget[" + sdkTheme.getWidgetPosition() + "]");
        OFHelper.v(tag, "OneFlow sdkTheme 0 Close[" + sdkTheme.getClose_button() + "]");
        OFHelper.v(tag, "OneFlow sdkTheme 0 progress[" + sdkTheme.getProgress_bar() + "]");
        //New theme custome UI
        if (sdkTheme.getProgress_bar()) {
            pagePositionPBar.setVisibility(View.VISIBLE);
        } else {
            pagePositionPBar.setVisibility(View.GONE);
        }
        if (sdkTheme.getClose_button()) {
            closeBtn.setVisibility(View.VISIBLE);
        } else {
            closeBtn.setVisibility(View.GONE);
        }

        /*if (sdkTheme.getDark_overlay()) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); // This flag is required to set otherwise the setDimAmount method will not show any effect
            window.setDimAmount(0.25f); //0 for no dim to 1 for full dim
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); // This flag is required to set otherwise the setDimAmount method will not show any effect
            window.setDimAmount(0f); //0 for no dim to 1 for full dim
        }*/
        initFragment();
    }

    //Checking rules
    private void preparePositionOnRule(String screenID, String answerIndex, String answerValue) {

        boolean found = false;
        String action = "", type = "";

        if (screens.get(position - 1).getRules() != null) {
            for (OFDataLogic dataLogic : screens.get(position - 1).getRules().getDataLogic()) {

                OFHelper.v(tag, "OneFlow condition rule[" + new Gson().toJson(screens.get(position - 1).getRules()) + "]");
                OFHelper.v(tag, "OneFlow condition 0[" + dataLogic.getCondition() + "]");
                action = dataLogic.getAction();
                type = dataLogic.getType();
                if (dataLogic.getCondition().equalsIgnoreCase("is")) {

                    OFHelper.v(tag, "OneFlow condition at is ");
                    if (answerIndex != null) {
                        OFHelper.v(tag, "OneFlow condition value[" + dataLogic.getValues() + "][" + answerIndex + "]");
                        if (dataLogic.getValues().equalsIgnoreCase(answerIndex)) {
                            found = true;
                            break;

                        }
                    } else {
                        if (answerValue != null) {
                            String[] valueArray = answerValue.split(",");
                            String[] logicValue = dataLogic.getValues().split(",");
                            int i = 0;
                            OFHelper.v(tag, "OneFlow condition[" + Arrays.asList(valueArray) + "][" + dataLogic.getValues() + "]");

                            if (Arrays.equals(valueArray, logicValue)) {
                                found = true;
                                break;
                            }

                        }
                    }
                } else if (dataLogic.getCondition().equalsIgnoreCase("is-not")) {

                    OFHelper.v(tag, "OneFlow condition at is NOT [" + dataLogic.getValues() + "]index[" + answerIndex + "]");
                    if (!dataLogic.getValues().equalsIgnoreCase(answerIndex)) {
                        found = true;
                        break;
                        //findNextQuestionPosition(dataLogic.getValues());
                    } /*else {
                    initFragment();
                }*/
                } else if (dataLogic.getCondition().equalsIgnoreCase("is-one-of")) {

                    OFHelper.v(tag, "OneFlow condition at is one of [" + dataLogic.getValues() + "]index[" + answerIndex + "]answerValue[" + answerValue + "]");
                    String[] rulesArray = dataLogic.getValues().split(",");

                    if (answerIndex != null) {
                        if (Arrays.asList(rulesArray).contains(answerIndex)) {
                            found = true;
                            break;
                        }
                    } else {
                        String[] values = answerValue.split(",");
                        for (String value : values) {
                            OFHelper.v(tag, "OneFlow condition[" + value + "][" + Arrays.asList(values) + "][" + Arrays.asList(rulesArray).contains(value) + "]");
                            if (Arrays.asList(rulesArray).contains(value)) {
                                found = true;
                                break;
                            }
                        }
                        // breaking outer loop
                        if (found) break;
                    }
                } else if (dataLogic.getCondition().equalsIgnoreCase("is-none-of")) {
                    String[] rulesArray1 = dataLogic.getValues().split(",");
                    found = true;
                    if (answerIndex != null) {
                        if (Arrays.asList(rulesArray1).contains(answerIndex)) {
                            found = false;
                            break;

                        }
                    } else {
                        String[] values = answerValue.split(",");
                        for (String value : values) {
                            if (Arrays.asList(rulesArray1).contains(value)) {
                                found = true;
                                break;
                            }
                        }
                        // breaking outer loop
                        if (found) break;
                    }
                } else if (dataLogic.getCondition().equalsIgnoreCase("is-any")) {

                    //findNextQuestionPosition(dataLogic.getAction());
                    found = true;
                    break;
                }
            }
        }

        OFHelper.v(tag, "OneFlow found [" + found + "]action[" + action + "]type[" + type + "]");
        // rating and open url is pending
        if (found) {
            if (OFHelper.validateString(action).equalsIgnoreCase("the-end")) {
                position = screens.size() - 1;
                initFragment();
            } else {
                if (type.equalsIgnoreCase("open-url")) {
                    //todo need to close properly

                    position = screens.size();
                    initFragment();

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(action));
                    startActivity(browserIntent);
                } else if (type.equalsIgnoreCase("rating")) {
                    // OFHelper.makeText(OFSurveyActivity.this,"RATING METHOD CALLED",1);
                    position = screens.size();
                    initFragment();
                    reviewThisApp(getActivity());
                } else {
                    findNextQuestionPosition(action);
                }
            }
        } else {
            initFragment();
        }
    }

    // initFragment();
    private void findNextQuestionPosition(String nextQuestionId) {
        int index = position - 1;
        OFHelper.v(tag, "OneFlow condition[" + nextQuestionId + "]");
        while (index < screens.size()) {
            if (nextQuestionId.equalsIgnoreCase(screens.get(index).get_id())) {
                //question found break the loop
                OFHelper.v(tag, "OneFlow condition question found at [" + index + "]");
                break;
            }
            index++;
        }

        position = index;
        initFragment();
    }

    public String themeColor = "";
    String selectedSurveyId;

    public void reviewThisApp(Context context) {

        ReviewManager manager = ReviewManagerFactory.create(context);
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                OFHelper.v(tag, "OneFlow review success called");
                ReviewInfo reviewInfo = task.getResult();
                Task<Void> flow = manager.launchReviewFlow((Activity) context, reviewInfo);
                flow.addOnCompleteListener(task2 -> {

                });
                flow.addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void result) {

                    }
                });
            }
        });


    }

    /**
     * This method will check if any unknown survey has came
     *
     * @return
     */
    public OFSurveyScreens validateScreens() {
        String[] possibleType = new String[]{"text", "thank_you", "rating-numerical", "rating-5-star", "rating", "rating-emojis", "nps"};
        OFSurveyScreens screen = null;

        while (position < screens.size()) {
            screen = screens.get(position);
            if (Arrays.asList(possibleType).contains(screen.getInput().getInput_type())) {
                break; // if found then stop;
            } else {
                position++;// if not found then check next survey
            }
        }


        return screen;
    }
}
