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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.oneflow.analytics.OneFlow;
import com.oneflow.analytics.R;
import com.oneflow.analytics.customwidgets.OFCustomTextView;
import com.oneflow.analytics.customwidgets.OFCustomTextViewBold;
import com.oneflow.analytics.model.survey.OFDataLogic;
import com.oneflow.analytics.model.survey.OFSDKSettingsTheme;
import com.oneflow.analytics.model.survey.OFSurveyScreens;
import com.oneflow.analytics.utils.OFHelper;

public class OFSurveyQueThankyouFragment extends BaseFragment {


    ImageView thankyouImage, waterMarkImage;

    OFCustomTextViewBold surveyTitle;

    OFCustomTextView surveyDescription;

    String tag = this.getClass().getName();


    public static OFSurveyQueThankyouFragment newInstance(OFSurveyScreens ahdList, OFSDKSettingsTheme sdkTheme, String themeColor) {
        OFSurveyQueThankyouFragment myFragment = new OFSurveyQueThankyouFragment();

        Bundle args = new Bundle();
        args.putSerializable("data", ahdList);
        args.putSerializable("theme", sdkTheme);
        args.putString("themeColor", themeColor);
        myFragment.setArguments(args);

        return myFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.survey_que_thankyou_fragment, container, false);
        OFHelper.v(tag, "1Flow list data[" + surveyScreens + "]");


        thankyouImage = (ImageView) view.findViewById(R.id.thankyou_img);
        waterMarkImage = (ImageView) view.findViewById(R.id.watermark_img);
        waterMarkLayout = (LinearLayout) view.findViewById(R.id.bottom_water_mark);

        surveyTitle = (OFCustomTextViewBold) view.findViewById(R.id.survey_title);
        surveyDescription = (OFCustomTextView) view.findViewById(R.id.survey_sub_title);
        surveyTitle.setTextColor(Color.parseColor(OFHelper.handlerColor(sdkTheme.getText_color())));

        int colorAlpha = OFHelper.manipulateColor(Color.parseColor(OFHelper.handlerColor(sdkTheme.getText_color())), 0.8f);//ColorUtils.setAlphaComponent(Color.parseColor(OFHelper.handlerColor(sdkTheme.getText_color())), 80);//ColorUtils.setAlphaComponent(Color.parseColor(OFHelper.handlerColor(sdkTheme.getText_color())), 80);
        int colorlike = OFHelper.manipulateColor(Color.parseColor(OFHelper.handlerColor(sdkTheme.getText_color())), 0.6f);
        ((TextView) waterMarkLayout.getChildAt(1)).setTextColor(colorlike);
        surveyDescription.setTextColor(colorAlpha);


        if (OneFlow.titleFace != null) {
            if (OneFlow.titleFace.getTypeface() != null) {
                surveyTitle.setTypeface(OneFlow.titleFace.getTypeface());
            }
            if (OneFlow.titleFace.getFontSize() != null) {
                surveyTitle.setTextSize(OneFlow.titleFace.getFontSize());
            }
        }
        surveyTitle.setText(surveyScreens.getTitle());
        if (surveyScreens.getMessage() != null) {

            if (OneFlow.subTitleFace != null) {
                if (OneFlow.subTitleFace.getTypeface() != null) {
                    surveyDescription.setTypeface(OneFlow.subTitleFace.getTypeface());
                }

                if (OneFlow.subTitleFace.getFontSize() != null) {
                    surveyDescription.setTextSize(OneFlow.subTitleFace.getFontSize());
                }
            }
            surveyDescription.setText(surveyScreens.getMessage());
        } else {
            surveyDescription.setVisibility(View.GONE);
        }


        sa.position = sa.screens.size();
        handleWaterMarkStyle(sdkTheme);
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

                            if (surveyScreens.getRules() != null) {
                                if (surveyScreens.getRules().getDismissBehavior() != null) {
                                    if (surveyScreens.getRules().getDismissBehavior().getFadesAway()) {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                ruleAction();
                                                sa.finish();
                                            }
                                        }, surveyScreens.getRules().getDismissBehavior().getFadesAway() ? (surveyScreens.getRules().getDismissBehavior().getDelayInSeconds() * 1000) : 20);
                                        // above logic is added for fade away if true then should fade away in mentioned duration
                                    }
                                } else {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            ruleAction();
                                            sa.finish();
                                        }
                                    }, 20);
                                }
                            } else {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        sa.finish();
                                    }
                                }, 20);
                            }


                            /*if (surveyScreens.getRules() != null) {
                                if (surveyScreens.getRules().getDismissBehavior() != null) {
                                    if (surveyScreens.getRules().getDismissBehavior().getFadesAway()) {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                sa.finish();
                                            }
                                        }, surveyScreens.getRules().getDismissBehavior().getFadesAway() ? (surveyScreens.getRules().getDismissBehavior().getDelayInSeconds() * 1000) : 20);
                                        // above logic is added for fade away if true then should fade away in mentioned duration
                                    }
                                } else {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            sa.finish();
                                        }
                                    }, 20);
                                }
                            }*/
                        }
                    });
                }

            }
        });
        //ruleAction();
        sa.initFragment();
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            //Logic for showing close button if fade away is false then have to show close button at thankyou page
            if (!surveyScreens.getRules().getDismissBehavior().getFadesAway()) {
                sa.closeBtn.setVisibility(View.VISIBLE);
            }
        } catch (Exception ex) {
            OFHelper.e(tag, "1Flow Error[" + ex.getMessage() + "]");
        }
    }

    private void ruleAction() {
        //OFHelper.v(tag,"1Flow thankyou page rule ["+new Gson().toJson(surveyScreens.getRules())+"]");
        if (surveyScreens.getRules() != null) {
            if (surveyScreens.getRules().getDataLogic() != null && surveyScreens.getRules().getDataLogic().size() > 0) {
                OFDataLogic dl = surveyScreens.getRules().getDataLogic().get(0);
                if (dl != null) {
                    if (dl.getType().equalsIgnoreCase("open-url")) {
                        //todo need to close properly
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(dl.getAction()));
                        startActivity(browserIntent);
                    } else if (dl.getType().equalsIgnoreCase("rating")) {
                        // OFHelper.makeText(OFSurveyActivity.this,"RATING METHOD CALLED",1);
                        sa.reviewThisApp(getActivity());
                    }
                }
            }
        }
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



   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sa = (OFSDKBaseActivity) context;

    }*/


  /*  public void handleClick(View v) {
        if (v.getId() == R.id.watermark_img) {
            String waterMark = "https://1flow.app/?utm_source=1flow-android-sdk&utm_medium=watermark&utm_campaign=real-time+feedback+powered+by+1flow";//https://www.notion.so/Powered-by-1Flow-logo-should-link-to-website-c186fca5220e41d19f420dd871f9696d";
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(waterMark));
            startActivity(browserIntent);

        }
    }*/



}
