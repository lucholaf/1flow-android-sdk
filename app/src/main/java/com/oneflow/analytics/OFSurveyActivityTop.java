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

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.oneflow.analytics.controller.OFEventController;
import com.oneflow.analytics.model.survey.OFGetSurveyListResponse;
import com.oneflow.analytics.sdkdb.OFOneFlowSHP;
import com.oneflow.analytics.utils.OFConstants;
import com.oneflow.analytics.utils.OFHelper;

import java.util.ArrayList;
import java.util.HashMap;


public class OFSurveyActivityTop extends OFSDKBaseActivity {



    String tag = this.getClass().getName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.survey_view);

        inTime = System.currentTimeMillis();
        OFHelper.v(tag, "OneFlow reached at surveyActivity");
        pagePositionPBar = (ProgressBar) findViewById(R.id.pbar);
        closeBtn = (ImageView) findViewById(R.id.close_btn_image_view);
        slider = (View) findViewById(R.id.slider);
        sliderLayout = (RelativeLayout) findViewById(R.id.slider_layout);
        basePopupLayout = (RelativeLayout) findViewById(R.id.base_popup_layout);
        mainChildForBackground = (RelativeLayout) findViewById(R.id.view_layout);
        fragmentView = (FrameLayout) findViewById(R.id.fragment_view);

        window = this.getWindow();

        WindowManager.LayoutParams wlp = window.getAttributes();
        OFHelper.v(tag, "OneFlow Window size width[" + window.getAttributes().width + "]height[" + window.getAttributes().height + "]");

        double[] data = OFHelper.getScreenSize(this);

        wlp.gravity = Gravity.TOP;
        if (data[0] > 3) {
            wlp.width = 1000;
        } else {
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        }
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.flags &= WindowManager.LayoutParams.FLAG_DIM_BEHIND;

        //window.setWindowAnimations(R.style.WindowTransitionFromTop);
        window.setAttributes(wlp);

    }

   /* @Override
    protected void onStart() {
        super.onStart();
    
        OFGetSurveyListResponse surveyItem = (OFGetSurveyListResponse) this.getIntent().getSerializableExtra("SurveyType");

        surveyName = surveyItem.getName();
        screens = surveyItem.getScreens();
        triggerEventName = this.getIntent().getStringExtra("eventName");//surveyItem.getTrigger_event_name();
        // Helper.makeText(getApplicationContext(),"Size ["+screens.size()+"]",1);
        setProgressMax(surveyItem.getScreens().size() - 1); // -1 for excluding thankyou page from progress bar
        selectedSurveyId = surveyItem.get_id();
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //closed survey logic for storage.
                OFOneFlowSHP ofs = new OFOneFlowSHP(OFSurveyActivityTop.this);
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
                        OFEventController ec = OFEventController.getInstance(OFSurveyActivityTop.this);
                        HashMap<String, Object> mapValue = new HashMap<>();
                        mapValue.put("survey_id", selectedSurveyId);
                        ec.storeEventsInDB(OFConstants.AUTOEVENT_CLOSED_SURVEY, mapValue, 0);
                    }

                } else {
                    surveyClosingStatus = "closed";
                }

                OFSurveyActivityTop.this.finish();
                // overridePendingTransition(0,R.anim.slide_down_dialog);
            }
        });


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

                OFSurveyActivityTop.this.finish();
                // overridePendingTransition(0,R.anim.slide_down_dialog);
                return false;
            }
        });
        initFragment();

    }*/


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.nothing,R.anim.slide_exit_upward);
    }
}
