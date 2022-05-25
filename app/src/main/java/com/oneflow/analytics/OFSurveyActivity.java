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

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.google.gson.Gson;
import com.oneflow.analytics.controller.OFEventController;
import com.oneflow.analytics.fragment.OFSurveyQueFragment;
import com.oneflow.analytics.fragment.OFSurveyQueTextFragment;
import com.oneflow.analytics.fragment.OFSurveyQueThankyouFragment;
import com.oneflow.analytics.model.survey.OFDataLogic;
import com.oneflow.analytics.model.survey.OFFinishCallBack;
import com.oneflow.analytics.model.survey.OFGetSurveyListResponse;
import com.oneflow.analytics.model.survey.OFSDKSettingsTheme;
import com.oneflow.analytics.model.survey.OFSurveyChoises;
import com.oneflow.analytics.model.survey.OFSurveyFinishChild;
import com.oneflow.analytics.model.survey.OFSurveyFinishModel;
import com.oneflow.analytics.model.survey.OFSurveyScreens;
import com.oneflow.analytics.model.survey.OFSurveyUserInput;
import com.oneflow.analytics.model.survey.OFSurveyUserResponseChild;
import com.oneflow.analytics.repositories.OFLogUserDBRepo;
import com.oneflow.analytics.repositories.OFSurvey;
import com.oneflow.analytics.sdkdb.OFOneFlowSHP;
import com.oneflow.analytics.utils.OFConstants;
import com.oneflow.analytics.utils.OFHelper;
import com.oneflow.analytics.utils.OFMyResponseHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;


public class OFSurveyActivity extends AppCompatActivity implements OFMyResponseHandler {

    ProgressBar pagePositionPBar;
    ImageView closeBtn;
    View slider;
    RelativeLayout sliderLayout,basePopupLayout,mainChildForBackground;
    FrameLayout fragmentView;

    String tag = this.getClass().getName();
    String triggerEventName = "";
    public ArrayList<OFSurveyUserResponseChild> surveyResponseChildren;
    public ArrayList<OFSurveyScreens> screens;
    private Long inTime = 0l;
    String surveyClosingStatus = "finished";
    ArrayList<OFSurveyFinishModel> surveyFinishList;
    String surveyName = "";
    public OFSDKSettingsTheme sdkTheme;

    //public HashMap<Integer,String> colorCode;

   /* public void initHashMap(){
        colorCode = new HashMap<>();
        colorCode.put(100,"FF");
        colorCode.put(99,"FC");
        colorCode.put(98,"FA");
        colorCode.put(97,"F7");
        colorCode.put(96,"F5");
        colorCode.put(95,"F2");
        colorCode.put(94,"F0");
        colorCode.put(93,"ED");
        colorCode.put(92,"EB");
        colorCode.put(91,"E8");
        colorCode.put(90,"E6");
        colorCode.put(89,"E3");
        colorCode.put(88,"E0");
        colorCode.put(87,"DE");
        colorCode.put(86,"DB");
        colorCode.put(85,"D9");
        colorCode.put(84,"D6");
        colorCode.put(83,"D4");
        colorCode.put(82,"D1");
        colorCode.put(81,"CF");
        colorCode.put(80,"CC");
        colorCode.put(79,"C9");
        colorCode.put(78,"C7");
        colorCode.put(77,"C4");
        colorCode.put(76,"C2");
        colorCode.put(75,"BF");
        colorCode.put(74,"BD");
        colorCode.put(73,"BA");
        colorCode.put(72,"B8");
        colorCode.put(71,"B5");
        colorCode.put(70,"B3");
        colorCode.put(69,"B0");
        colorCode.put(68,"AD");
        colorCode.put(67,"AB");
        colorCode.put(66,"A8");
        colorCode.put(65,"A6");
        colorCode.put(64,"A3");
        colorCode.put(63,"A1");
        colorCode.put(62,"9E");
        colorCode.put(61,"9C");
        colorCode.put(60,"99");
        colorCode.put(59,"96");
        colorCode.put(58,"94");
        colorCode.put(57,"91");
        colorCode.put(56,"8F");
        colorCode.put(55,"8C");
        colorCode.put(54,"8A");
        colorCode.put(53,"87");
        colorCode.put(52,"85");
        colorCode.put(51,"82");
        colorCode.put(50,"80");
        colorCode.put(49,"7D");
        colorCode.put(48,"7A");
        colorCode.put(47,"78");
        colorCode.put(46,"75");
        colorCode.put(45,"73");
        colorCode.put(44,"70");
        colorCode.put(43,"6E");
        colorCode.put(42,"6B");
        colorCode.put(41,"69");
        colorCode.put(40,"66");
        colorCode.put(39,"63");
        colorCode.put(38,"61");
        colorCode.put(37,"5E");
        colorCode.put(36,"5C");
        colorCode.put(35,"59");
        colorCode.put(34,"57");
        colorCode.put(33,"54");
        colorCode.put(32,"52");
        colorCode.put(31,"4F");
        colorCode.put(30,"4D");
        colorCode.put(29,"4A");
        colorCode.put(28,"47");
        colorCode.put(27,"45");
        colorCode.put(26,"42");
        colorCode.put(25,"40");
        colorCode.put(24,"3D");
        colorCode.put(23,"3B");
        colorCode.put(22,"38");
        colorCode.put(21,"36");
        colorCode.put(20,"33");
        colorCode.put(19,"30");
        colorCode.put(18,"2E");
        colorCode.put(17,"2B");
        colorCode.put(16,"29");
        colorCode.put(15,"26");
        colorCode.put(14,"24");
        colorCode.put(13,"21");
        colorCode.put(12,"1F");
        colorCode.put(11,"1C");
        colorCode.put(10,"1A");
        colorCode.put(9,"17");
        colorCode.put(8,"14");
        colorCode.put(7,"12");
        colorCode.put(6,"0F");
        colorCode.put(5,"0D");
        colorCode.put(4,"0A");
        colorCode.put(3,"08");
        colorCode.put(2,"05");
        colorCode.put(1,"03");
        colorCode.put(0,"00");

    }*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.survey_view);

       // initHashMap();

        inTime = System.currentTimeMillis();
        OFHelper.v(tag, "OneFlow reached at surveyActivity");
        pagePositionPBar = (ProgressBar) findViewById(R.id.pbar);
        closeBtn = (ImageView) findViewById(R.id.close_btn_image_view);
        slider = (View) findViewById(R.id.slider);
        sliderLayout = (RelativeLayout) findViewById(R.id.slider_layout);
        basePopupLayout = (RelativeLayout) findViewById(R.id.base_popup_layout);
        mainChildForBackground = (RelativeLayout) findViewById(R.id.view_layout);
        fragmentView = (FrameLayout) findViewById(R.id.fragment_view);

        Window window = this.getWindow();

        WindowManager.LayoutParams wlp = window.getAttributes();
        OFHelper.v(tag, "OneFlow Window size width[" + window.getAttributes().width + "]height[" + window.getAttributes().height + "]");

        double[] data = OFHelper.getScreenSize(this);

        wlp.gravity = Gravity.BOTTOM;
        if (data[0] > 3) {
            wlp.width = 1000;
        } else {
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        }
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.flags &= WindowManager.LayoutParams.FLAG_DIM_BEHIND;

        window.setAttributes(wlp);



        //String surveyType = this.getIntent().getStringExtra("SurveyType");
        OFGetSurveyListResponse surveyItem = (OFGetSurveyListResponse) this.getIntent().getSerializableExtra("SurveyType");



        surveyName = surveyItem.getName();
        screens = surveyItem.getScreens();//checkSurveyTitleAndScreens(surveyType);
        triggerEventName = this.getIntent().getStringExtra("eventName");//surveyItem.getTrigger_event_name();
        // Helper.makeText(getApplicationContext(),"Size ["+screens.size()+"]",1);
        setProgressMax(surveyItem.getScreens().size() - 1); // -1 for excluding thankyou page from progress bar
        selectedSurveyId = surveyItem.get_id();
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //closed survey logic for storage.
                OFOneFlowSHP ofs = new OFOneFlowSHP(OFSurveyActivity.this);
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
                        OFEventController ec = OFEventController.getInstance(OFSurveyActivity.this);
                        HashMap<String, Object> mapValue = new HashMap<>();
                        mapValue.put("survey_id", selectedSurveyId);
                        ec.storeEventsInDB(OFConstants.AUTOEVENT_CLOSED_SURVEY, mapValue, 0);
                    }

                } else {
                    surveyClosingStatus = "closed";
                }

                OFSurveyActivity.this.finish();
                // overridePendingTransition(0,R.anim.slide_down_dialog);
            }
        });


        /*pagePositionPBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(surveyItem.get)));
        pagePositionPBar.setIndeterminateTintList(ColorStateList.valueOf(getResources().getColor(R.color.whitetxt)));*/
//      Helper.v(tag,"OneFlow color["+surveyItem.getStyle().getPrimary_color()+"]");

        themeColor = "#" + Integer.toHexString(ContextCompat.getColor(this, R.color.colorPrimaryDark));
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
            themeColor = "#" + Integer.toHexString(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            pagePositionPBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(themeColor)));
        }


        //This is temp remove in prod
        //surveyItem.getSurveySettings().getSdkTheme().setText_color(themeColor);

        sdkTheme = surveyItem.getSurveySettings().getSdkTheme();

        mainChildForBackground.setBackgroundColor(Color.parseColor(OFHelper.handlerColor(sdkTheme.getBackground_color())));

        Drawable closeIcon= closeBtn.getDrawable();
        closeIcon.setColorFilter(OFHelper.manipulateColor(Color.parseColor(OFHelper.handlerColor(sdkTheme.getText_color())),0.6f), PorterDuff.Mode.SRC_ATOP);

        surveyResponseChildren = new ArrayList<>();
        slider.setOnTouchListener(sliderTouchListener);
        sliderLayout.setOnTouchListener(sliderTouchListener);
        slider.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                OFHelper.v(tag, "OneFlow getAction[" + event.getAction() + "]");
                OFHelper.v(tag, "OneFlow getX[" + event.getX() + "]");

                OFSurveyActivity.this.finish();
                // overridePendingTransition(0,R.anim.slide_down_dialog);
                return false;
            }
        });
        initFragment();

    }


    /*private void setReenterTransition() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition transition = new Transition(R.anim.slide_up_new_theme);
            transition.setDuration(600);
            transition.setInterpolator(new FastOutLinearInInterpolator());
            getWindow().setReenterTransition(transition);
        }
    }*/
    private int previousFingerPosition = 0;
    private int baseLayoutPosition = 0;
    private int defaultViewHeight;
    private boolean isClosing = false;
    private boolean isScrollingUp = false;
    private boolean isScrollingDown = false;

    View.OnTouchListener sliderTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // Get finger position on screen
            final int Y = (int) event.getRawY();

            // Switch on motion event type
            switch (event.getAction() & MotionEvent.ACTION_MASK) {

                case MotionEvent.ACTION_DOWN:
                    // save default base layout height
                    defaultViewHeight = basePopupLayout.getHeight();

                    // Init finger and view position
                    previousFingerPosition = Y;
                    baseLayoutPosition = (int) basePopupLayout.getY();
                    break;

                case MotionEvent.ACTION_UP:
                    // If user was doing a scroll up
                    if (isScrollingUp) {
                        // Reset baselayout position
                        basePopupLayout.setY(0);
                        // We are not in scrolling up mode anymore
                        isScrollingUp = false;
                    }

                    // If user was doing a scroll down
                    if (isScrollingDown) {
                        // Reset baselayout position
                        basePopupLayout.setY(0);
                        // Reset base layout size
                        basePopupLayout.getLayoutParams().height = defaultViewHeight;
                        basePopupLayout.requestLayout();
                        // We are not in scrolling down mode anymore
                        isScrollingDown = false;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!isClosing) {
                        int currentYPosition = (int) basePopupLayout.getY();

                        // If we scroll up
                        if (previousFingerPosition > Y) {
                            // First time android rise an event for "up" move
                            if (!isScrollingUp) {
                                isScrollingUp = true;
                            }

                            // Has user scroll down before -> view is smaller than it's default size -> resize it instead of change it position
                            if (basePopupLayout.getHeight() < defaultViewHeight) {
                                /*basePopupLayout.getLayoutParams().height = basePopupLayout.getHeight() - (Y - previousFingerPosition);
                                basePopupLayout.requestLayout();*/
                            } else {
                                // Has user scroll enough to "auto close" popup ?
                                if ((baseLayoutPosition - currentYPosition) > defaultViewHeight / 4) {
                                    //closeUpAndDismissDialog(currentYPosition);
                                    return true;
                                }

                                //
                            }
                            // basePopupLayout.setY(basePopupLayout.getY() + (Y - previousFingerPosition));

                        }
                        // If we scroll down
                        else {

                            // First time android rise an event for "down" move
                            if (!isScrollingDown) {
                                isScrollingDown = true;
                            }

                            // Has user scroll enough to "auto close" popup ?
                            if (Math.abs(baseLayoutPosition - currentYPosition) > defaultViewHeight / 2) {
                                closeDownAndDismissDialog(currentYPosition);
                                return true;
                            }

                            // Change base layout size and position (must change position because view anchor is top left corner)
                            basePopupLayout.setY(basePopupLayout.getY() + (Y - previousFingerPosition));
                            basePopupLayout.getLayoutParams().height = basePopupLayout.getHeight() - (Y - previousFingerPosition);
                            basePopupLayout.requestLayout();
                        }

                        // Update position
                        previousFingerPosition = Y;
                    }
                    break;
            }
            return true;
        }
    };

    public void closeUpAndDismissDialog(int currentPosition) {
        isClosing = true;
        ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(basePopupLayout, "y", currentPosition, -basePopupLayout.getHeight());
        positionAnimator.setDuration(300);
        positionAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                finish();
            }

        });
        positionAnimator.start();
    }

    public void closeDownAndDismissDialog(int currentPosition) {
        isClosing = true;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenHeight = size.y;
        ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(basePopupLayout, "y", currentPosition, screenHeight + basePopupLayout.getHeight());
        positionAnimator.setDuration(300);
        positionAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                finish();
            }

        });
        positionAnimator.start();
    }

    public void checkIfAnswerAlreadyGiven() {

    }

    @Override
    protected void onPause() {
        new OFOneFlowSHP(this).storeValue(OFConstants.SHP_SURVEY_RUNNING, false);
        //on close of this page considering survey is over, so submit the respones to api
        if (surveyResponseChildren.size() > 0) {
            OFHelper.v(tag, "OneFlow input found submitting");
            prepareAndSubmitUserResposneNew();
        } else {
            OFHelper.v(tag, "OneFlow no input no submit");
            surveyFinishList = new ArrayList<>();
            Intent intent = new Intent("survey_finished");

            OFFinishCallBack finishData = new OFFinishCallBack();
            finishData.setStatus(surveyClosingStatus);
            finishData.setSurveyId(selectedSurveyId);
            finishData.setSurveyName(surveyName);
            finishData.setTriggerName(triggerEventName);
            finishData.setScreens(prepareFinishCallback());

            intent.putExtra(OFConstants.surveyDetail,new Gson().toJson(finishData));
            //OFHelper.v(tag,"OneFlow sending data ["+new Gson().toJson(finishData)+"]");
            sendBroadcast(intent);
        }
        OFHelper.v(tag, "OneFlow onPause called");
        //overridePendingTransition(0, R.anim.slide_down_dialog_sdk);
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        OFHelper.v(tag, "OneFlow onStop called");
        //overridePendingTransition(0,R.anim.slide_down_dialog);
    }

    /**
     * Record User inputs
     *
     * @param screenID
     * @param answerIndex
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
            OFHelper.v(tag, "OneFlow rules [" + new Gson().toJson(screens.get(position - 1).getRules()) + "]");
            if (screens.get(position - 1).getRules() != null) {
                preparePositionOnRule(screenID, answerIndex, answerValue);
            } else {
                initFragment();
            }
        } catch (Exception ex) {
            OFHelper.e(tag, "Survey Result error[" + ex.getMessage() + "]");
        }
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
                            //action = dataLogic.getAction();
                            /*if (dataLogic.getAction().equalsIgnoreCase("the-end")) {
                                position = screens.size();
                                initFragment();
                            } else {
                                findNextQuestionPosition(dataLogic.getAction());
                            }*/
                        } /*else {
                        initFragment();
                    }*/
                    } else {
                        if (answerValue != null) {
                            String[] valueArray = answerValue.split(",");
                            String[] logicValue = dataLogic.getValues().split(",");
                            int i = 0;
                            OFHelper.v(tag, "OneFlow condition[" + Arrays.asList(valueArray) + "][" + dataLogic.getValues() + "]");
                            // if(logicValue.length == valueArray.length) {
                            /*while (i < valueArray.length) {
                                if (dataLogic.getValues().equalsIgnoreCase(valueArray[i])) {
                                    OFHelper.v(tag, "OneFlow condition[found in array]");
                                    found = true;
                                    //      action = dataLogic.getAction();
                                    break;
                                }
                                i++;
                            }*/
                            if (Arrays.equals(valueArray, logicValue)) {
                                found = true;
                                break;
                            }
                            //}
                        /*// breaking outer loop
                        if(found) break;*/
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
                    reviewThisApp(OFSurveyActivity.this);
                } else {
                    findNextQuestionPosition(action);
                }
            }
        } else {
            initFragment();
        }
    }
    // initFragment();

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

        /*Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent redirectMarket = new Intent(Intent.ACTION_VIEW, uri);
        redirectMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(redirectMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }*/
    }

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


    public void prepareAndSubmitUserResposne() {

        OFOneFlowSHP ofs = new OFOneFlowSHP(this);
        ofs.storeValue(OFConstants.SHP_SURVEY_RUNNING, false);
        OFSurveyUserInput sur = new OFSurveyUserInput();
        sur.setMode(OFConstants.MODE);
        sur.setTrigger_event(triggerEventName);
        sur.setAnswers(surveyResponseChildren);
        sur.setOs(OFConstants.os);
        sur.setAnalytic_user_id(ofs.getUserDetails().getAnalytic_user_id());
        sur.setSurvey_id(selectedSurveyId);
        sur.setSession_id(ofs.getStringValue(OFConstants.SESSIONDETAIL_IDSHP));
        //if internet available then send to api else store locally
        if (OFHelper.isConnected(this)) {
            OFHelper.v(tag, "OneFlow calling submit user Resposne");
            OFSurvey.submitUserResponse(new OFOneFlowSHP(OFSurveyActivity.this).getStringValue(OFConstants.APPIDSHP), sur, OFConstants.ApiHitType.surveySubmited, OFSurveyActivity.this);
        } else {

            sur.setUser_id(ofs.getStringValue(OFConstants.USERUNIQUEIDSHP));

            //TODO Store data in db
            OFLogUserDBRepo.insertUserInputs(this, sur, null, OFConstants.ApiHitType.insertSurveyInDB);
            //storing id for avoiding repeatation of offline surveys
            new OFOneFlowSHP(this).storeValue(sur.getSurvey_id(), Calendar.getInstance().getTimeInMillis());
            //Helper.makeText(this,getString(R.string.no_network),1);
        }
    }

    public void prepareAndSubmitUserResposneNew() {

        OFOneFlowSHP ofs = new OFOneFlowSHP(this);
        ofs.storeValue(OFConstants.SHP_SURVEY_RUNNING, false);
        OFSurveyUserInput sur = new OFSurveyUserInput();
        sur.setTotDuration(totalTimeSpentInSec());
        sur.setMode(OFConstants.MODE);
        sur.setTrigger_event(triggerEventName);
        sur.setAnswers(surveyResponseChildren);
        sur.setOs(OFConstants.os);
        sur.setAnalytic_user_id(OFHelper.validateString(ofs.getUserDetails().getAnalytic_user_id()));
        sur.setSurvey_id(selectedSurveyId);
        sur.setSession_id(ofs.getStringValue(OFConstants.SESSIONDETAIL_IDSHP));
        sur.setUser_id(ofs.getStringValue(OFConstants.USERUNIQUEIDSHP));
        sur.setCreatedOn(System.currentTimeMillis());
        OFLogUserDBRepo.insertUserInputs(this, sur, this, OFConstants.ApiHitType.insertSurveyInDB);

    }

    private Integer totalTimeSpentInSec() {

        Long l = (System.currentTimeMillis() - inTime) / 1000;
        OFHelper.v(tag, "OneFlow inTime [" + inTime + "][" + System.currentTimeMillis() + "][" + l + "]");
        return l.intValue();
    }

    public int position = 0;

    public void initFragment() {
        OFHelper.v(tag, "OneFlow answer position [" + position + "]screensize[" + screens.size() + "]selected answers[" + new Gson().toJson(surveyResponseChildren) + "]");
//        OFHelper.v(tag, "OneFlow answer position [" +new Gson().toJson(screens.get(position-1) )+ "]");
        if (position >= screens.size()) {
            OFSurveyActivity.this.finish();
            //overridePendingTransition(0,R.anim.slide_down_dialog);
            //slideDown();
        } else {
            loadFragments(screens.get(position));
        }
        OFHelper.v(tag, "OneFlow answer position after[" + position + "]screensize[" + screens.size() + "]selected answers[" + new Gson().toJson(surveyResponseChildren) + "]");
    }

    /*private void setProgressBarPosition() {

        int v = (int) (Math.ceil(100 / screens.size())) * (position + 1);

        Integer temp = (int) (Math.ceil(100f / screens.size())) * (position + 1);//((Integer)(Math.ceil(100/screens.size()))*(position+1);
        final Integer progressValueTo = temp > 110 ? 110 : temp;//((Integer)(Math.ceil(100/screens.size()))*(position+1);
        int progressValueFrom = (int) (Math.ceil(100f / screens.size())) * (position);
        //OFHelper.v(tag, "OneFlow progressValue before [" + Math.ceil(100f / screens.size()) + "] ceil[" + (100f / screens.size()) + "]from[" + progressValueFrom + "]to[" + progressValueTo + "]screenSize[" + screens.size() + "]position[" + position + "]");

        new Thread(new Runnable() {
            @Override
            public void run() {

                int sleepDuration = (int) 300 / (progressValueTo - progressValueFrom);
                OFHelper.v(tag, "OneFlow sleepDuration[" + sleepDuration + "]");

                for (int i = progressValueFrom; i < progressValueTo; i++) {
                    try {
                        Thread.sleep(sleepDuration);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    pagePositionPBar.setProgress(i);
                    //OFHelper.v(tag, "OneFlow progress loop[" + i + "]");
                }
            }
        }).start();


        // pagePositionPBar.setProgress(progressValue);

    }*/

    private void setProgressBarPositionAsync() {

        //int v = (int) (Math.ceil(100 / screens.size())) * (position + 1);

        //Integer temp = (int) (Math.ceil(100f / screens.size())) * (position + 1);//((Integer)(Math.ceil(100/screens.size()))*(position+1);
        // final Integer progressValueTo = temp > 110 ? 110 : temp;//((Integer)(Math.ceil(100/screens.size()))*(position+1);
        // int progressValueFrom = (int) (Math.ceil(100f / screens.size())) * (position);
        //OFHelper.v(tag, "OneFlow progressValue before [" + Math.ceil(100f / screens.size()) + "] ceil[" + (100f / screens.size()) + "]from[" + progressValueFrom + "]to[" + progressValueTo + "]screenSize[" + screens.size() + "]position[" + position + "]");


        setProgressAnimate();




        /*AsyncTask progressAsync = new AsyncTask<Object,Integer,Object>() {
            @Override
            protected Object doInBackground(Object[] objects) {

               setProgressAnimate(position);

                return null;
            }

            @Override
            protected void onProgressUpdate(Integer[] values) {
                super.onProgressUpdate(values);
                ObjectAnimator animation = ObjectAnimator.ofInt(pagePositionPBar, "progress", pagePositionPBar.getProgress(), values[0] * 100);
                animation.setDuration(500);
                animation.setAutoCancel(true);
                animation.setInterpolator(new DecelerateInterpolator());
                animation.start();
            }
        };

        progressAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);*/


    }


    private void setProgressMax(int max) {
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


    private void loadFragments(OFSurveyScreens screen) {
        //setProgressBarPosition();
        //setProgressBarPositionAsync();
        setProgressAnimate();
        //Helper.makeText(getApplicationContext(),"Screen input ["+screen.getInput().getInput_type()+"]",1);
        //Helper.showAlert(getApplicationContext(),"","Screen input type["+screen.getInput().getInput_type()+"]");
        if (screen != null) {
            Fragment frag = null;
            try {
                if (screen.getInput().getInput_type().equalsIgnoreCase("thank_you")) {
                    pagePositionPBar.setVisibility(View.GONE);
                    frag = OFSurveyQueThankyouFragment.newInstance(screen);
                } else if (screen.getInput().getInput_type().equalsIgnoreCase("text")) {
                    frag = OFSurveyQueTextFragment.newInstance(screen);
                } else {
                    frag = OFSurveyQueFragment.newInstance(screen);
                }
            } catch (Exception ex) {
                OFHelper.e(tag, "OneFlow ERROR [" + ex.getMessage() + "]");
                //frag = SurveyQueThankyouFragment.newInstance(screen);
            }


            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (frag != null) {
                if (position == 0) {
                    ft.add(R.id.fragment_view, frag).commit();
                } else {
                    ft.replace(R.id.fragment_view, frag).commit();
                }
            } else {
                //Helper.makeText(getApplicationContext(), "frag null", 1);
            }
        } else {

        }
    }


    @Override
    public void onResponseReceived(OFConstants.ApiHitType hitType, Object obj, Long reserve, String reserved) {
        OFHelper.v(tag, "OneFlow submitting survey[" + hitType + "]");
        switch (hitType) {
            case insertSurveyInDB:
                //if internet available then send to api else store locally
                if (OFHelper.isConnected(this)) {
                    OFSurveyUserInput sur = (OFSurveyUserInput) obj;
                    OFHelper.v(tag, "OneFlow calling submit user Resposne [" + sur.getAnswers() + "]");
                    if (sur.getAnswers() != null) {
                        if (sur.getAnswers().size() > 0) {
                            OFSurvey.submitUserResponse(new OFOneFlowSHP(OFSurveyActivity.this).getStringValue(OFConstants.APPIDSHP), sur, OFConstants.ApiHitType.surveySubmited, OFSurveyActivity.this);
                        }
                    }
                } else {
                    OFHelper.v(tag, "OneFlow no data connectivity available submit survey later");
                }
                break;
            case surveySubmited:

                OFHelper.v(tag, "OneFlow survey submitted successfully");
                OFSurveyUserInput sur = (OFSurveyUserInput) obj;
                //Updating survey once data is sent to server, Sending type null as return is not required
                OFLogUserDBRepo.updateSurveyInput(OFSurveyActivity.this, null, null, true, sur.get_id());

                new OFOneFlowSHP(OFSurveyActivity.this).storeValue(sur.getSurvey_id(), Calendar.getInstance().getTimeInMillis());

                Intent intent = new Intent("survey_finished");

                OFFinishCallBack finishData = new OFFinishCallBack();
                finishData.setStatus(surveyClosingStatus);
                finishData.setSurveyId(sur.getSurvey_id());
                finishData.setSurveyName(surveyName);
                finishData.setTriggerName(sur.getTrigger_event());
                finishData.setScreens(prepareFinishCallback());

                intent.putExtra(OFConstants.surveyDetail,new Gson().toJson(finishData));
                //OFHelper.v(tag,"OneFlow sending data ["+new Gson().toJson(finishData)+"]");
                sendBroadcast(intent);
                break;
        }
    }

    private ArrayList<OFSurveyFinishModel> prepareFinishCallback() {
        ArrayList<OFSurveyFinishModel> list = new ArrayList<>();
        OFSurveyFinishModel finishModel = null;
        OFSurveyFinishChild finishChild = null;
        ArrayList<OFSurveyFinishChild> listInner;
       // if(surveyResponseChildren.size()>0) {
            for (int i = 0; i < screens.size() - 1; i++) {

                OFSurveyScreens ss = screens.get(i);

                finishModel = new OFSurveyFinishModel();
                finishModel.setScreenId(ss.get_id());
                finishModel.setQuestionTitle(ss.getTitle());
                finishModel.setQuestionType(ss.getInput().getInput_type());
                listInner = new ArrayList<>();
                for (OFSurveyUserResponseChild sr : surveyResponseChildren) {
                    if (sr.getScreen_id().equalsIgnoreCase(ss.get_id())) {
                        finishChild = new OFSurveyFinishChild();
                        if (ss.getInput().getInput_type().equalsIgnoreCase("mcq") ||
                                ss.getInput().getInput_type().equalsIgnoreCase("checkbox")) {
                            //This if is for handling multiple option in checkbox.
                            if (sr.getAnswer_index().contains(",")) {
                                String[] options = sr.getAnswer_index().split(",");
                               // OFHelper.v(tag,"OneFlow sending data ["+options.length+"]");
                                for (String option : options) {
                                  //  OFHelper.v(tag,"OneFlow sending data inside loop["+option+"]");
                                    finishChild = new OFSurveyFinishChild();
                                    finishChild.setAnswerValue(getFieldValue(ss, option));
                                    if (finishChild.getAnswerValue().equalsIgnoreCase("other") || finishChild.getAnswerValue().equalsIgnoreCase("others")) {
                                        finishChild.setOtherValue(sr.getAnswer_value());
                                    }
                                    listInner.add(finishChild);
                                }
                            } else {
                                finishChild.setAnswerValue(getFieldValue(ss, sr.getAnswer_index()));
                                if (finishChild.getAnswerValue().equalsIgnoreCase("other") || finishChild.getAnswerValue().equalsIgnoreCase("others")) {
                                    finishChild.setOtherValue(sr.getAnswer_value());
                                }
                                listInner.add(finishChild);
                            }


                        } else {
                            finishChild.setAnswerValue(sr.getAnswer_value());
                            listInner.add(finishChild);
                        }

                    }

                    finishModel.setQuestionAns(listInner);

                }
                if(finishModel.getQuestionAns() !=null && finishModel.getQuestionAns().size()>0) {
                    list.add(finishModel);
                }
            }
        //}
       // OFHelper.v(tag,"OneFlow list ["+new Gson().toJson(list)+"]");
        return list;
    }


    public String getFieldValue(OFSurveyScreens ss, String findThisId) {
        String label = "";
        OFHelper.v(tag,"OneFlow field value in ["+ss+"]["+findThisId+"]");
        for(OFSurveyChoises choice : ss.getInput().getChoices()){

            if(choice.getId().equalsIgnoreCase(findThisId)){
                label = choice.getTitle();
                break;
            }

        }
        OFHelper.v(tag,"OneFlow field value out ["+label+"]");
        return label;

    }
}
