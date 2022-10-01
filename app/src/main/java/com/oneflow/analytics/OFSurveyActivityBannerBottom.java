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

public class OFSurveyActivityBannerBottom extends OFSDKBaseActivity {
    String tag = this.getClass().getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.survey_view_banner);

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

        wlp.gravity = Gravity.BOTTOM;
       /* if (data[0] > 3) {
            wlp.width = 1000;
        } else {*/
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //}
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.flags &= WindowManager.LayoutParams.FLAG_DIM_BEHIND;

        window.setAttributes(wlp);

   }

 @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.nothing,R.anim.slide_down_new_theme);
    }
}
