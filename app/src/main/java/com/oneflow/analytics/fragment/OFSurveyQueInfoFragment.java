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
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.oneflow.analytics.OneFlow;
import com.oneflow.analytics.R;
import com.oneflow.analytics.customwidgets.OFCustomTextView;
import com.oneflow.analytics.customwidgets.OFCustomTextViewBold;
import com.oneflow.analytics.model.survey.OFSDKSettingsTheme;
import com.oneflow.analytics.model.survey.OFSurveyScreens;
import com.oneflow.analytics.sdkdb.OFOneFlowSHP;
import com.oneflow.analytics.utils.OFHelper;

public class OFSurveyQueInfoFragment extends BaseFragment implements View.OnClickListener{


    ImageView waterMarkImage;

    // LinearLayout waterMarkLayout;

    OFCustomTextViewBold surveyTitle, submitButton;

    OFCustomTextView surveyDescription;

    String tag = this.getClass().getName();


    public static OFSurveyQueInfoFragment newInstance(OFSurveyScreens ahdList, OFSDKSettingsTheme sdkTheme, String themeColor) {
        OFSurveyQueInfoFragment myFragment = new OFSurveyQueInfoFragment();

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
        View view = inflater.inflate(R.layout.survey_que_info_fragment, container, false);
        OFHelper.v(tag, "OneFlow list data[" + new Gson().toJson(surveyScreens) + "]");

        animation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_sdk);
        animation2 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_sdk);
        animation3 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_sdk);
        animation4 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_sdk);
        animation5 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_sdk);

        waterMarkImage = (ImageView) view.findViewById(R.id.watermark_img);
        waterMarkLayout = (LinearLayout) view.findViewById(R.id.bottom_water_mark);
        submitButton = (OFCustomTextViewBold) view.findViewById(R.id.submit_btn);


        surveyTitle = (OFCustomTextViewBold) view.findViewById(R.id.survey_title);
        surveyDescription = (OFCustomTextView) view.findViewById(R.id.survey_sub_title);

        surveyTitle.setTextColor(Color.parseColor(OFHelper.handlerColor(sdkTheme.getText_color())));

        int colorAlpha = OFHelper.manipulateColor(Color.parseColor(OFHelper.handlerColor(sdkTheme.getText_color())), 0.8f);//ColorUtils.setAlphaComponent(Color.parseColor(OFHelper.handlerColor(sdkTheme.getText_color())), 80);//ColorUtils.setAlphaComponent(Color.parseColor(OFHelper.handlerColor(sdkTheme.getText_color())), 80);
        int colorlike = OFHelper.manipulateColor(Color.parseColor(OFHelper.handlerColor(sdkTheme.getText_color())), 0.6f);
        ((OFCustomTextView) waterMarkLayout.getChildAt(1)).setTextColor(colorlike);
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
        submitButton.setOnClickListener(this);
        handleWaterMarkStyle(sdkTheme);
        //Glide.with(this).load(R.drawable.thank_you).into(thankyouImage);
        submitButtonBeautification();
        submitButton.requestFocus();
        transitActive();

        return view;

    }

    private void submitButtonBeautification() {
        try {
            gdSubmit = (GradientDrawable) (submitButton).getBackground();

            int colorAlpha = OFHelper.manipulateColor(Color.parseColor(themeColor), 0.5f);
            gdSubmit.setColor(colorAlpha);//Color.parseColor(themeColor));


            submitButton.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //if (userInput.getText().toString().trim().length() >= surveyScreens.getInput().getMin_chars()) {
                            gdSubmit.setColor(colorAlpha);
                            //}
                            break;
                        case MotionEvent.ACTION_MOVE:

                            break;

                        case MotionEvent.ACTION_UP:
                            //if (userInput.getText().toString().trim().length() >= surveyScreens.getInput().getMin_chars()) {
                            gdSubmit.setColor(Color.parseColor(themeColor));
                            //}
                            break;
                    }
                    return false;
                }
            });
        } catch (Exception ex) {

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


    public void handleClick(View v) {
        if (v.getId() == R.id.watermark_img) {
            String waterMark = "https://1flow.app/?utm_source=1flow-android-sdk&utm_medium=watermark&utm_campaign=real-time+feedback+powered+by+1flow";//https://www.notion.so/Powered-by-1Flow-logo-should-link-to-website-c186fca5220e41d19f420dd871f9696d";
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(waterMark));
            startActivity(browserIntent);

        }
    }

    int i = 0;
    Dialog dialog;
    Animation animation1, animation2, animation3, animation4, animation5;

    @Override
    public void onResume() {
        super.onResume();
        View[] animateViews;

        animateViews = new View[]{surveyTitle, surveyDescription, submitButton};


        Animation[] annim = new Animation[]{animation1, animation2, animation3, animation4, animation5};

        if (i == 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    annim[i].setFillAfter(true);
                    animateViews[i].startAnimation(annim[i]);

                }
            }, 500);

            animation1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    OFHelper.v(tag, "OneFlow animation START [" + i + "]");
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    OFHelper.v(tag, "OneFlow animation END[" + i + "]");
                    //
                    i++;
                    if (i < animateViews.length) {
                        animateViews[i].setVisibility(View.VISIBLE);
                        //animateViews[i].clearAnimation();
                        animateViews[i].startAnimation(annim[i]);
                    }

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    OFHelper.v(tag, "OneFlow animation REPEAT[" + i + "]");
                }
            });
            animation2.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    OFHelper.v(tag, "OneFlow animation START [" + i + "]");
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    OFHelper.v(tag, "OneFlow animation END[" + i + "]");
                    //
                    i++;
                    if (i < animateViews.length) {
                        animateViews[i].setVisibility(View.VISIBLE);
                        //animateViews[i].clearAnimation();
                        animateViews[i].startAnimation(annim[i]);
                    }

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    OFHelper.v(tag, "OneFlow animation REPEAT[" + i + "]");
                }
            });
            animation3.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    OFHelper.v(tag, "OneFlow animation START [" + i + "]");
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    OFHelper.v(tag, "OneFlow animation END[" + i + "]");

                    i++;

                    if (i < animateViews.length) {
                        /* if (surveyScreens.getInput().getMin_chars() <= 0) {*/
                        try {
                            OFHelper.v(tag, "OneFlow min char reached [" + surveyScreens.getButtons().get(0).getTitle() + "]");
                            if (!OFHelper.validateString(surveyScreens.getButtons().get(0).getTitle()).equalsIgnoreCase("NA")) {
                                ((OFCustomTextViewBold) animateViews[i]).setText(surveyScreens.getButtons().get(0).getTitle());
                            }
                            animateViews[i].setVisibility(View.VISIBLE);
                            animateViews[i].startAnimation(annim[i]);

                        } catch (Exception ex) {
                            OFHelper.e(tag, "Button list not found");
                        }
                    }

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    OFHelper.v(tag, "OneFlow animation REPEAT[" + i + "]");
                }
            });
            animation4.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    OFHelper.v(tag, "OneFlow animation4 START [" + i + "]");
                }

                @Override
                public void onAnimationEnd(Animation animation) {


                    OFHelper.v(tag, "OneFlow animation4 END[" + i + "]len[" + animateViews.length + "][" + surveyScreens.getInput().getMin_chars() + "]");


                    i++;
                    if (i < animateViews.length) {
                        animateViews[i].setVisibility(View.VISIBLE);
                        //animateViews[i].clearAnimation();
                        animateViews[i].startAnimation(annim[i]);
                    }

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    OFHelper.v(tag, "OneFlow animation REPEAT[" + i + "]");
                }
            });
        }

    }
    @Override
    public void onClick(View v) {

        //OFHelper.makeText(getActivity(), "Clicked on button", 1);
       /* if (v.getId() == R.id.skip_btn) {
            sa.addUserResponseToList(surveyScreens.get_id(), null, null);
        } else if (v.getId() == R.id.submit_btn) {*/

                    sa.addUserResponseToList(surveyScreens.get_id(), null,  null);


        /*} else if (v.getId() == R.id.cancel_btn) {
            //
        }*/
    }
}
