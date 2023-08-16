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

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.oneflow.analytics.utils.OFHelper;


public class OFSurveyActivityFullScreen extends OFSDKBaseActivity {



    String tag = this.getClass().getName();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.survey_view_full_screen);

        inTime = System.currentTimeMillis();
        OFHelper.v(tag, "OneFlow reached at surveyActivity");
        pagePositionPBar = (ProgressBar) findViewById(R.id.pbar);
        closeBtn = (ImageView) findViewById(R.id.close_btn_image_view);
        slider = (View) findViewById(R.id.slider);
        sliderLayout = (RelativeLayout) findViewById(R.id.slider_layout);
        basePopupLayout = (RelativeLayout) findViewById(R.id.base_popup_layout);
        mainChildForBackground = (RelativeLayout) findViewById(R.id.view_layout);
        fragmentView = (FrameLayout) findViewById(R.id.fragment_view);
        waterMarkLayout = (LinearLayout) findViewById(R.id.bottom_water_mark);


        window = this.getWindow();

        WindowManager.LayoutParams wlp = window.getAttributes();
        OFHelper.v(tag, "OneFlow Window size width[" + window.getAttributes().width + "]height[" + window.getAttributes().height + "]");

        double[] data = OFHelper.getScreenSize(this);

       // wlp.gravity = Gravity.BOTTOM;
       /* if (data[0] > 3) {
            wlp.width = 1000;
        } else {*/
           // wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //}
        //wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.flags &= WindowManager.LayoutParams.FLAG_DIM_BEHIND;

       // window.setAttributes(wlp);
        handleWaterMarkStyle();
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.nothing,R.anim.fade_out_sdk);
    }
    public void handleWaterMarkStyle() {

        try {

                if (!sdkTheme.getRemove_watermark()) {
                    waterMarkLayout.setVisibility(View.GONE);
                } else {
                    waterMarkLayout.setVisibility(View.VISIBLE);
                }
                int colorAlpha = OFHelper.manipulateColor(Color.parseColor(OFHelper.handlerColor(sdkTheme.getText_color())), 0.1f);
                GradientDrawable gd = (GradientDrawable) waterMarkLayout.getBackground();
                waterMarkLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String waterMark = "https://1flow.app/?utm_source=1flow-android-sdk&utm_medium=watermark&utm_campaign=real-time+feedback+powered+by+1flow";//https://www.notion.so/Powered-by-1Flow-logo-should-link-to-website-c186fca5220e41d19f420dd871f9696d";
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(waterMark));
                        startActivity(browserIntent);
                    }
                });
                waterMarkLayout.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        switch (event.getAction()) {

                            case MotionEvent.ACTION_DOWN:
                                gd.setColor(colorAlpha);
                                break;

                            case MotionEvent.ACTION_MOVE:
                                // touch move code
                                //Helper.makeText(mContext,"Moved",1);
                                break;

                            case MotionEvent.ACTION_UP:

                                gd.setColor(null);


                                break;
                        }
                        return false;
                    }
                });

        } catch (Exception ex) {
            OFHelper.e("BaseFragment", "OneFlow watermark error ");
        }
    }
}
