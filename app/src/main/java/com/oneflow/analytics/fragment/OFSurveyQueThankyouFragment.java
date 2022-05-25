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

package com.oneflow.analytics.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.oneflow.analytics.OFSurveyActivity;
import com.oneflow.analytics.R;
import com.oneflow.analytics.customwidgets.OFCustomTextView;
import com.oneflow.analytics.customwidgets.OFCustomTextViewBold;
import com.oneflow.analytics.model.survey.OFSurveyScreens;
import com.oneflow.analytics.utils.OFHelper;

public class OFSurveyQueThankyouFragment extends BaseFragment {



    ImageView thankyouImage,waterMarkImage;

   // LinearLayout waterMarkLayout;

    OFCustomTextViewBold surveyTitle;

    OFCustomTextView surveyDescription;

    String tag = this.getClass().getName();

    OFSurveyScreens surveyScreens;

    public static OFSurveyQueThankyouFragment newInstance(OFSurveyScreens ahdList) {
        OFSurveyQueThankyouFragment myFragment = new OFSurveyQueThankyouFragment();

        Bundle args = new Bundle();
        args.putSerializable("data", ahdList);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        surveyScreens = (OFSurveyScreens) getArguments().getSerializable("data");

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.survey_que_thankyou_fragment, container, false);
        OFHelper.v(tag, "OneFlow list data[" + surveyScreens + "]");


        thankyouImage = (ImageView)view.findViewById(R.id.thankyou_img);
        waterMarkImage = (ImageView)view.findViewById(R.id.watermark_img);
        waterMarkLayout = (LinearLayout) view.findViewById(R.id.bottom_water_mark);

        /*if(sa.sdkTheme.getRemove_watermark()){
            waterMarkLayout.setVisibility(View.GONE);
        }else{
            waterMarkLayout.setVisibility(View.VISIBLE);
        }*/


        surveyTitle = (OFCustomTextViewBold) view.findViewById(R.id.survey_title);
        surveyDescription = (OFCustomTextView) view.findViewById(R.id.survey_sub_title);

        surveyTitle.setTextColor(Color.parseColor(OFHelper.handlerColor(sa.sdkTheme.getText_color())));

        int colorAlpha = OFHelper.manipulateColor(Color.parseColor(OFHelper.handlerColor(sa.sdkTheme.getText_color())), 0.8f);//ColorUtils.setAlphaComponent(Color.parseColor(OFHelper.handlerColor(sa.sdkTheme.getText_color())), 80);//ColorUtils.setAlphaComponent(Color.parseColor(OFHelper.handlerColor(sa.sdkTheme.getText_color())), 80);
        int colorlike = OFHelper.manipulateColor(Color.parseColor(OFHelper.handlerColor(sa.sdkTheme.getText_color())), 0.6f);
        ((OFCustomTextView) waterMarkLayout.getChildAt(1)).setTextColor(colorlike);
        surveyDescription.setTextColor(colorAlpha);

        sa.position = sa.screens.size();
        handleWaterMarkStyle(sa.sdkTheme);
        //Glide.with(this).load(R.drawable.thank_you).into(thankyouImage);
        Glide.with(this).load(R.drawable.thanku_bg).into(new DrawableImageViewTarget(thankyouImage) {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                super.onResourceReady(resource, transition);
                if (resource instanceof GifDrawable) {
                    ((GifDrawable) resource).setLoopCount(1);
                    ((GifDrawable) resource).registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                        @Override
                        public void onAnimationStart(Drawable drawable) {
                            super.onAnimationStart(drawable);
                        }

                        @Override
                        public void onAnimationEnd(Drawable drawable) {
                            super.onAnimationEnd(drawable);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    sa.initFragment();
                                }
                            }, 200);

                        }
                    });
                }

            }
        });

        return view;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_sdk);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            surveyTitle.startAnimation(animation);
        } else {
            //Helper.makeText(getActivity(), "Visibility Gone", 1);
        }
    }

    OFSurveyActivity sa;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sa = (OFSurveyActivity) context;

    }


    public void handleClick(View v) {
        if(v.getId()== R.id.watermark_img){
                String waterMark = "https://1flow.app/?utm_source=1flow-android-sdk&utm_medium=watermark&utm_campaign=real-time+feedback+powered+by+1flow";//https://www.notion.so/Powered-by-1Flow-logo-should-link-to-website-c186fca5220e41d19f420dd871f9696d";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(waterMark));
                startActivity(browserIntent);

        }
    }

    Dialog dialog;


}
