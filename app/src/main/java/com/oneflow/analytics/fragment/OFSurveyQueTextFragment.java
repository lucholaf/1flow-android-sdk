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

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.oneflow.analytics.OFSDKBaseActivity;
import com.oneflow.analytics.OFSurveyActivityBottom;
import com.oneflow.analytics.OneFlow;
import com.oneflow.analytics.R;
import com.oneflow.analytics.customwidgets.OFCustomEditText;
import com.oneflow.analytics.customwidgets.OFCustomTextView;
import com.oneflow.analytics.customwidgets.OFCustomTextViewBold;
import com.oneflow.analytics.model.survey.OFSDKSettingsTheme;
import com.oneflow.analytics.model.survey.OFSurveyScreens;
import com.oneflow.analytics.sdkdb.OFOneFlowSHP;
import com.oneflow.analytics.utils.OFConstants;
import com.oneflow.analytics.utils.OFHelper;


public class OFSurveyQueTextFragment extends BaseFragment implements View.OnClickListener {


    OFCustomTextViewBold surveyTitle, submitButton;
    RelativeLayout optionLayout;
    OFCustomEditText userInput, userInputShort;

    OFCustomTextView surveyInputLimit, skipBtn;

    OFCustomTextView surveyDescription;

    String userText="";

    String tag = this.getClass().getName();


    public static OFSurveyQueTextFragment newInstance(OFSurveyScreens ahdList, OFSDKSettingsTheme sdkTheme, String themeColor) {
        OFSurveyQueTextFragment myFragment = new OFSurveyQueTextFragment();

        Bundle args = new Bundle();
        args.putSerializable("data", ahdList);
        args.putSerializable("theme", sdkTheme);
        args.putString("themeColor",themeColor);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        OFHelper.v(tag,"OneFlow reached onSaveInstanceState");
        if(surveyScreens.getInput().getInput_type().equalsIgnoreCase("short-text")){
            new OFOneFlowSHP(getActivity()).storeValue("userInput",userInputShort.getText().toString());
        }else{
            new OFOneFlowSHP(getActivity()).storeValue("userInput",userInput.getText().toString());
        }

        outState.putString("userText",userText);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        try {
            OFHelper.v(tag,"OneFlow reached onSaveInstanceState");
            userText = savedInstanceState.getString("userText");
            OFHelper.v(tag,"OneFlow reached onSaveInstanceState0["+userText+"]");
            if (surveyScreens.getInput().getInput_type().equalsIgnoreCase("short-text")) {
                userInputShort.setText(userText);
            } else {
                userInput.setText(userText);
            }
        }catch(Exception ex){

        }
    }

    int i = 0;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        OFHelper.v(tag, "OneFlow visible to user");
        if (isVisibleToUser) {

            View[] animateViews = new View[]{surveyTitle, surveyDescription};


            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_sdk);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    //OFHelper.v(tag, "OneFlow animation START [" + i + "]");
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                   // OFHelper.v(tag, "OneFlow animation END[" + i + "]");
                    if (i < animateViews.length) {
                        animateViews[i++].startAnimation(animation);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animateViews[i++].startAnimation(animation);
        } else {
            // Helper.makeText(getActivity(), "Visibility Gone", 1);
        }
    }


    Animation animation1, animation2, animation3, animation4, animation5, animationIn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.survey_que_text_fragment, container, false);

        animation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_sdk);
        animation2 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_sdk);
        animation3 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_sdk);
        animation4 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_sdk);
        animation5 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_sdk);


        surveyTitle = (OFCustomTextViewBold) view.findViewById(R.id.survey_title);
        submitButton = (OFCustomTextViewBold) view.findViewById(R.id.submit_btn);
        surveyDescription = (OFCustomTextView) view.findViewById(R.id.survey_description);
        skipBtn = (OFCustomTextView) view.findViewById(R.id.skip_btn);
        userInput = (OFCustomEditText) view.findViewById(R.id.child_user_input);
        userInputShort = (OFCustomEditText) view.findViewById(R.id.child_user_input_short);
        surveyInputLimit = (OFCustomTextView) view.findViewById(R.id.text_limit);
        optionLayout = (RelativeLayout) view.findViewById(R.id.option_layout);
        waterMarkLayout = (LinearLayout) view.findViewById(R.id.bottom_water_mark);



        handleWaterMarkStyle(sdkTheme);
        surveyTitle.setTextColor(Color.parseColor(OFHelper.handlerColor(sdkTheme.getText_color())));

        int colorAlpha = OFHelper.manipulateColor(Color.parseColor(OFHelper.handlerColor(sdkTheme.getText_color())), 0.8f);//ColorUtils.setAlphaComponent(Color.parseColor(OFHelper.handlerColor(sdkTheme.getText_color())), 80);
        int colorlike = OFHelper.manipulateColor(Color.parseColor(OFHelper.handlerColor(sdkTheme.getText_color())), 0.6f);
        surveyDescription.setTextColor(colorAlpha);
        skipBtn.setTextColor(colorlike);
        ((OFCustomTextView) waterMarkLayout.getChildAt(1)).setTextColor(colorlike);


        skipBtn.setOnClickListener(this);

        surveyInputLimit.setTextColor(OFHelper.manipulateColor(Color.parseColor(OFHelper.handlerColor(sdkTheme.getText_color())), 0.5f));


        OFHelper.v(tag, "OneFlow list data[" + surveyScreens + "]");
        animationIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_sdk);

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

        surveyInputLimit.setText("0/" + surveyScreens.getInput().getMax_chars());
        OFHelper.v(tag, " OneFlow onTextChanged min[" + surveyScreens.getInput().getMin_chars() + "]max[" + surveyScreens.getInput().getMax_chars() + "]");
        //setMaxLength(surveyScreens.getInput().getMax_chars());
        userInput.setHintTextColor(OFHelper.manipulateColor(Color.parseColor(OFHelper.handlerColor(sdkTheme.getText_color())), 0.5f));
        userInput.setTextColor(Color.parseColor(OFHelper.handlerColor(sdkTheme.getText_color())));
        userInputShort.setHintTextColor(OFHelper.manipulateColor(Color.parseColor(OFHelper.handlerColor(sdkTheme.getText_color())), 0.5f));
        userInputShort.setTextColor(Color.parseColor(OFHelper.handlerColor(sdkTheme.getText_color())));

        if (surveyScreens.getInput().getInput_type().equalsIgnoreCase("text")) {
            userInput.setHint(surveyScreens.getInput().getPlaceholderText());
            userInputShort.setVisibility(View.GONE);
            optionLayout.setVisibility(View.INVISIBLE);
            submitButtonBeautification();
        } else {
            userInputShort.setHint(surveyScreens.getInput().getPlaceholderText());
            optionLayout.setVisibility(View.GONE);
            userInputShort.setVisibility(View.INVISIBLE);
        }

        submitButton.requestFocus();
        userInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                //Helper.v(tag," beforeTextChanged s["+s+"]start["+start+"]after["+after+"]count["+count+"]");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (userInput.getText().toString().trim().length() >= surveyScreens.getInput().getMin_chars()) {

                    try {
                        if (!OFHelper.validateString(surveyScreens.getButtons().get(0).getTitle()).equalsIgnoreCase("NA")) {
                            submitButton.setText(surveyScreens.getButtons().get(0).getTitle());
                        }
                    } catch (Exception ex) {
                        OFHelper.e(tag, "Button list not found");
                    }

                    if (!isActive) {
                        transitActive();
                        isActive = true;
                    }

                } else {
                    if (surveyScreens.getButtons().size() == 1) {

                        if (isActive) {
                            transitInActive();
                            isActive = false;
                        }
                    } else if (surveyScreens.getButtons().size() == 2) {

                        if (isActive) {
                            transitInActive();
                            isActive = false;
                        }
                        //cancelButton.setVisibility(View.GONE);
                    }
                }
                if (userInput.getText().toString().length() > surveyScreens.getInput().getMax_chars()) {
                    OFHelper.makeText(getActivity(), "You have exceeded max length", 1);
                    userInput.setText(userInput.getText().toString().substring(0, userInput.getText().length() - count));
                    userInput.setSelection(userInput.getText().toString().length());
                }

                surveyInputLimit.setText(userInput.getText().toString().length() + "/" + surveyScreens.getInput().getMax_chars());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        submitButton.setOnClickListener(this);
        //cancelButton.setOnClickListener(this);

        return view;

    }


    private void submitButtonBeautification() {
        try {
            gdSubmit = (GradientDrawable) (submitButton).getBackground();
            // GradientDrawable gdOption = (GradientDrawable) optionLayout.getBackground();
            //submitButton.setVisibility(View.INVISIBLE);
            //gdOption.setColor(getResources().getColor(R.color.white));
            int colorAlpha = OFHelper.manipulateColor(Color.parseColor(themeColor), 0.5f);
            gdSubmit.setColor(colorAlpha);//Color.parseColor(themeColor));


            submitButton.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            if (userInput.getText().toString().trim().length() >= surveyScreens.getInput().getMin_chars()) {
                                gdSubmit.setColor(colorAlpha);
                            }
                            break;
                        case MotionEvent.ACTION_MOVE:

                            break;

                        case MotionEvent.ACTION_UP:
                            if (userInput.getText().toString().trim().length() >= surveyScreens.getInput().getMin_chars()) {
                                gdSubmit.setColor(Color.parseColor(themeColor));
                            }
                            break;
                    }
                    return false;
                }
            });
        } catch (Exception ex) {

        }
    }

    private void setMaxLength(int maxLength) {

        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(maxLength);
        surveyInputLimit.setFilters(fArray);
    }

    @Override
    public void onResume() {
        super.onResume();
        View[] animateViews;
        String savedValue = new OFOneFlowSHP(getActivity()).getStringValue("userInput");
        if (surveyScreens.getInput().getInput_type().equalsIgnoreCase("text")) {
            animateViews = new View[]{surveyTitle, surveyDescription, optionLayout, submitButton};//, skipBtn};
            if(!savedValue.equalsIgnoreCase("NA")) {
                userInput.setText(savedValue);
            }
        } else {
            animateViews = new View[]{surveyTitle, surveyDescription, userInputShort, submitButton};
            if(!savedValue.equalsIgnoreCase("NA")) {
                userInputShort.setText(savedValue);
            }
        }

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

        OFHelper.v(tag,"OneFlow reached onResume 0["+userText+"]");
        if(!userText.isEmpty()) {
            if (surveyScreens.getInput().getInput_type().equalsIgnoreCase("short-text")) {
                userInputShort.setText(userText);
            } else {
                userInput.setText(userText);
            }
        }
    }





    @Override
    public void onClick(View v) {

        new OFOneFlowSHP(getActivity()).storeValue("userInput","");
        if (v.getId() == R.id.skip_btn) {
            sa.addUserResponseToList(surveyScreens.get_id(), null, null);
        } else if (v.getId() == R.id.submit_btn) {
            if (surveyScreens.getInput().getInput_type().equalsIgnoreCase("text")) {
                if (userInput.getText().toString().trim().length() >= surveyScreens.getInput().getMin_chars()) {
                    sa.addUserResponseToList(surveyScreens.get_id(), null, userInput.getText().toString().trim().length() > 0 ? userInput.getText().toString().trim() : null);
                }
            } else {
                sa.addUserResponseToList(surveyScreens.get_id(), null, userInputShort.getText().toString().trim().length() > 0 ? userInputShort.getText().toString().trim() : null);
            }

        } else if (v.getId() == R.id.cancel_btn) {
            //  Helper.makeText(getActivity(), "Clicked on cancel button", 1);
        }
    }

}
