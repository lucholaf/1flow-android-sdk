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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oneflow.analytics.OFSDKBaseActivity;
import com.oneflow.analytics.OFSurveyActivityBottom;
import com.oneflow.analytics.OFSurveyActivityFullScreen;
import com.oneflow.analytics.OneFlow;
import com.oneflow.analytics.R;
import com.oneflow.analytics.adapter.OFSurveyOptionsAdapter;
import com.oneflow.analytics.customwidgets.OFCustomTextView;
import com.oneflow.analytics.customwidgets.OFCustomTextViewBold;
import com.oneflow.analytics.model.survey.OFRatingsModel;
import com.oneflow.analytics.model.survey.OFSDKSettingsTheme;
import com.oneflow.analytics.model.survey.OFSurveyChoises;
import com.oneflow.analytics.model.survey.OFSurveyScreens;
import com.oneflow.analytics.repositories.OFSurvey;
import com.oneflow.analytics.utils.OFGenericClickHandler;
import com.oneflow.analytics.utils.OFHelper;

import java.util.ArrayList;


public class OFSurveyQueFragment extends BaseFragment implements OFGenericClickHandler {//View.OnClickListener {


    OFCustomTextViewBold surveyTitle, submitButton;
    RecyclerView surveyOptionRecyclerView;
    OFCustomTextView ratingsNotLike, ratingsFullLike, surveyDescription, starRatingLabel;


    /*@BindView(R.id.cancel_btn)
    CustomTextViewBold cancelButton;*/
    RelativeLayout optionLayout;

    //this is for testing

    String tag = this.getClass().getName();

    OFSurveyOptionsAdapter dashboardAdapter;
    Animation animation1, animation2, animation3, animation4, animationIn;//animationOut;

    //String ratingsLabel[] = new String[]{"Very dissatisfied", "Somewhat dissatisfied", "Not dissatisfied nor satisfied", "Somewhat satisfied", "Very satisfied"};


    public static OFSurveyQueFragment newInstance(OFSurveyScreens ahdList, OFSDKSettingsTheme sdkTheme, String themeColor) {
        OFSurveyQueFragment myFragment = new OFSurveyQueFragment();

        Bundle args = new Bundle();
        args.putSerializable("data", ahdList);
        args.putSerializable("theme", sdkTheme);
        args.putString("themeColor", themeColor);
        myFragment.setArguments(args);
        return myFragment;
    }

    int i = 0;

    @Override
    public void onResume() {
        super.onResume();
        OFHelper.v(tag, "OneFlow OnResume");


        View[] animateViews = new View[]{surveyTitle, surveyDescription, optionLayout, submitButton};

        Animation[] annim = new Animation[]{animation1, animation2, animation3, animation4};

        if (i == 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    annim[i].setFillAfter(true);
                    animateViews[i].startAnimation(annim[i]);

                }
            }, 1000);

            animation1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    //OFHelper.v(tag, "OneFlow animation START [" + i + "]");
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    //OFHelper.v(tag, "OneFlow animation END[" + i + "]");
                    //
                    i++;

                    if (!(surveyScreens.getMessage() != null && surveyScreens.getMessage().length() > 0)) {
                        i++;
                    }

                    if (i < animateViews.length) {
                        animateViews[i].setVisibility(View.VISIBLE);
                        //animateViews[i].clearAnimation();
                        animateViews[i].startAnimation(annim[i]);
                    }

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    //OFHelper.v(tag, "OneFlow animation REPEAT[" + i + "]");
                }
            });
            animation2.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    //OFHelper.v(tag, "OneFlow animation START [" + i + "]");
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    //OFHelper.v(tag, "OneFlow animation END[" + i + "]");
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
                    //OFHelper.v(tag, "OneFlow animation START [" + i + "]");
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // OFHelper.v(tag, "OneFlow animation END[" + i + "]");

                    i++;
                    if (surveyScreens.getInput().getInput_type().equalsIgnoreCase("checkbox")) {
                        if (i < animateViews.length) {
                            animateViews[i].setVisibility(View.VISIBLE);
                            //animateViews[i].clearAnimation();
                            animateViews[i].startAnimation(annim[i]);
                        }
                    }

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    OFHelper.v(tag, "OneFlow animation REPEAT[" + i + "]");
                }
            });
        }

           /* }
        },1100);*/
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.survey_que_fragment, container, false);

        surveyTitle = (OFCustomTextViewBold) view.findViewById(R.id.survey_title_que);
        submitButton = (OFCustomTextViewBold) view.findViewById(R.id.submit_btn);
        surveyDescription = (OFCustomTextView) view.findViewById(R.id.survey_description_que);
        ratingsNotLike = (OFCustomTextView) view.findViewById(R.id.ratings_not_like);
        ratingsFullLike = (OFCustomTextView) view.findViewById(R.id.ratings_full_like);
        starRatingLabel = (OFCustomTextView) view.findViewById(R.id.star_ratings_label);
        surveyOptionRecyclerView = (RecyclerView) view.findViewById(R.id.survey_options_list);
        optionLayout = (RelativeLayout) view.findViewById(R.id.option_layout);
        waterMarkLayout = (LinearLayout) view.findViewById(R.id.bottom_water_mark);


        int colorTitle = OFHelper.manipulateColor(Color.parseColor(OFHelper.handlerColor(sdkTheme.getText_color())), 1.0f);

        surveyTitle.setTextColor(colorTitle);

        int colorDesc = OFHelper.manipulateColor(Color.parseColor(OFHelper.handlerColor(sdkTheme.getText_color())), 0.8f);//ColorUtils.setAlphaComponent(Color.parseColor(OFHelper.handlerColor(sdkTheme.getText_color())), OFHelper.getAlphaNumber(80));
        int colorlike = OFHelper.manipulateColor(Color.parseColor(OFHelper.handlerColor(sdkTheme.getText_color())), 0.6f);//ColorUtils.setAlphaComponent(Color.parseColor(OFHelper.handlerColor(sdkTheme.getText_color())), OFHelper.getAlphaNumber(60));


        surveyDescription.setTextColor(colorDesc);
        ratingsNotLike.setTextColor(colorlike);
        ratingsFullLike.setTextColor(colorlike);
        starRatingLabel.setTextColor(colorlike);
        //((OFCustomTextView) waterMarkLayout.getChildAt(1)).setTextColor(colorlike);


        handleWaterMarkStyle(sdkTheme);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OFHelper.v(tag, "OneFlow button size found 0 ");
                itemClicked(v, null, "");
            }
        });
        submitButtonBeautification();

        OFHelper.v(tag, "OneFlow list title[" + surveyScreens.getTitle() + "]");
        OFHelper.v(tag, "OneFlow list desc[" + surveyScreens.getMessage() + "]length[" + surveyScreens.getMessage().length() + "]");


        animation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_sdk);
        animation2 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_sdk);
        animation3 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_sdk);
        animation4 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_sdk);
        animationIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_sdk);
        //animationOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);

        if (OneFlow.titleFace != null) {
            if (OneFlow.titleFace.getTypeface() != null) {
                surveyTitle.setTypeface(OneFlow.titleFace.getTypeface());
            }
            if (OneFlow.titleFace.getFontSize() != null) {
                surveyTitle.setTextSize(OneFlow.titleFace.getFontSize());
            }
        }
        surveyTitle.setText(surveyScreens.getTitle());

        if (surveyScreens.getMessage() != null && surveyScreens.getMessage().length() > 0) {
            OFHelper.v(tag, "OneFlow progress bar inside if");
            if (OneFlow.subTitleFace != null) {
                if (OneFlow.subTitleFace != null) {
                    surveyDescription.setTypeface(OneFlow.subTitleFace.getTypeface());
                }
                if (OneFlow.subTitleFace.getFontSize() != null) {
                    surveyDescription.setTextSize(OneFlow.subTitleFace.getFontSize());
                }
            }
            surveyDescription.setText(surveyScreens.getMessage());
        } else {
            OFHelper.v(tag, "OneFlow progress bar inside else");
            surveyDescription.setVisibility(View.GONE);
        }


        if (surveyScreens.getInput().getRating_min_text() != null) {
            ratingsNotLike.setText(surveyScreens.getInput().getRating_min_text());
        }/*else{
            ratingsNotLike.setText("Not likely at all");
        }*/

        if (surveyScreens.getInput().getRating_max_text() != null) {
            ratingsFullLike.setText(surveyScreens.getInput().getRating_max_text());
        }/*else{
            ratingsFullLike.setText("Extermely likely");
        }*/

        OFHelper.v(tag, "OneFlow input type [" + surveyScreens.getInput().getInput_type() + "][" + surveyScreens.getInput().getStars() + "]min[" + surveyScreens.getInput().getMin_val() + "][" + surveyScreens.getInput().getMax_val() + "][][][]");
        if (surveyScreens.getInput().getInput_type().equalsIgnoreCase("rating-numerical")) {
            if (surveyScreens.getInput() != null) {
                //Setting default value if not received from api
                if (surveyScreens.getInput().getMin_val() == null) {
                    surveyScreens.getInput().setMin_val(1);
                }
                if (surveyScreens.getInput().getMax_val() == null || surveyScreens.getInput().getMax_val() == 0) {
                    surveyScreens.getInput().setMax_val("5");
                }

                surveyScreens.getInput().setRatingsList(prepareRatingsList(surveyScreens.getInput().getMin_val(), surveyScreens.getInput().getMax_val()));
            }

        } else if (surveyScreens.getInput().getInput_type().equalsIgnoreCase("rating-5-star") || surveyScreens.getInput().getInput_type().equalsIgnoreCase("rating")) {
            if (surveyScreens.getInput() != null) {

                surveyScreens.getInput().setRatingsList(prepareRatingsList(1, 5));//surveyScreens.getInput().getMin_val(), surveyScreens.getInput().getMax_val()));
                ratingsNotLike.setVisibility(View.GONE);
                ratingsFullLike.setVisibility(View.GONE);
            }
        } else if (surveyScreens.getInput().getInput_type().contains("rating-emojis")) {
            if (surveyScreens.getInput() != null) {

                surveyScreens.getInput().setRatingsList(prepareRatingsList(1, 5));//surveyScreens.getInput().getMin_val(), surveyScreens.getInput().getMax_val()));
                ratingsNotLike.setVisibility(View.GONE);
                ratingsFullLike.setVisibility(View.GONE);
            }
        } else if (surveyScreens.getInput().getInput_type().contains("nps")) {
            //Setting default value if not received from api
            if (surveyScreens.getInput().getMin_val() == null) {
                surveyScreens.getInput().setMin_val(0);
            }
            if (surveyScreens.getInput().getMax_val() == null || surveyScreens.getInput().getMax_val() == 0) {
                surveyScreens.getInput().setMax_val("10");
            }

            surveyScreens.getInput().setRatingsList(prepareRatingsList(surveyScreens.getInput().getMin_val(), surveyScreens.getInput().getMax_val()));
        } else {
            ratingsNotLike.setVisibility(View.GONE);
            ratingsFullLike.setVisibility(View.GONE);
        }
        OFHelper.v(tag, "OneFlow input type min after[" + surveyScreens.getInput().getMin_val() + "][" + surveyScreens.getInput().getMax_val() + "]");

        RecyclerView.LayoutManager mLayoutManager = null;
        if (surveyScreens.getInput().getInput_type().equalsIgnoreCase("rating-numerical") || surveyScreens.getInput().getInput_type().equalsIgnoreCase("nps")) {
            OFHelper.v(tag, "OneFlow gridLayout set");
            mLayoutManager = new GridLayoutManager(getActivity(), (surveyScreens.getInput().getMax_val() + 1) - surveyScreens.getInput().getMin_val());
        } else if (surveyScreens.getInput().getInput_type().equalsIgnoreCase("rating-emojis")) {
            mLayoutManager = new GridLayoutManager(getActivity(), 5);
        } else if (surveyScreens.getInput().getInput_type().equalsIgnoreCase("rating") || surveyScreens.getInput().getInput_type().equalsIgnoreCase("rating-5-star")) {
            mLayoutManager = new GridLayoutManager(getActivity(), 5);
            starRatingLabel.setVisibility(View.VISIBLE);
            starRatingLabel.setText(surveyScreens.getInput().getRating_text().get("0"));
        } else {
            if (surveyScreens.getInput().getChoices() != null) {
                if (surveyScreens.getInput().getChoices().size() > 0) {
                    OFHelper.v(tag, "OneFlow inputtype choices init");
                    ratingsNotLike.setVisibility(View.GONE);
                    starRatingLabel.setVisibility(View.GONE);
                    ratingsFullLike.setVisibility(View.GONE);
                    checkBoxSelection = new ArrayList<String>();
                    //OFHelper.v(tag, "OneFlow inputtype choices init ["+surveyScreens.getInput().getChoices().size()+"]");
                    // surveyScreens.getInput().getChoices().addAll(fakeList());
                    // OFHelper.v(tag, "OneFlow inputtype choices init after["+surveyScreens.getInput().getChoices().size()+"]");
                }
            }

            OFHelper.v(tag, "OneFlow linearlayout set");
            mLayoutManager = new LinearLayoutManager(getActivity());
        }

        OFHelper.v(tag, "OneFlow theme color [" + themeColor + "]");
        dashboardAdapter = new OFSurveyOptionsAdapter(getActivity(), surveyScreens.getInput(), this, themeColor, OFHelper.handlerColor(sdkTheme.getText_color()));

        surveyOptionRecyclerView.setLayoutManager(mLayoutManager);
        surveyOptionRecyclerView.setItemAnimator(new DefaultItemAnimator());
        surveyOptionRecyclerView.setAdapter(dashboardAdapter);


        return view;

    }

    public ArrayList<OFSurveyChoises> fakeList() {
        String label[] = new String[]{"first", "Second", "Third", "Fourth", "Fifth", "Sixth", "Seventh", "Eighth", "Ninth", "Tenth", "Eleventh", "Twelfth", "Thirteen", "Fourteen", "Fifteen", "Sixteen"};
        ArrayList<OFSurveyChoises> flist = new ArrayList<>();
        OFSurveyChoises sc = null;
        for (int i = 0; i < label.length; i++) {
            sc = new OFSurveyChoises();
            sc.setId(String.valueOf(i));
            sc.setTitle(label[i]);
            flist.add(sc);
        }
        OFHelper.v(tag, "OneFlow inputtype choices init after 0[" + flist.size() + "]");
        return flist;
    }


    private void submitButtonBeautification() {
        try {
            gdSubmit = (GradientDrawable) (submitButton).getBackground();
            gdSubmit.setColor(Color.parseColor(themeColor));
            int colorAlpha = OFHelper.manipulateColor(Color.parseColor(themeColor), 0.5f);
            ;
            submitButton.setText(surveyScreens.getButtons().get(0).getTitle());
            submitButton.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            if (checkBoxSelection != null && checkBoxSelection.size() > 0) {
                                gdSubmit.setColor(colorAlpha);
                            }
                            break;
                        case MotionEvent.ACTION_MOVE:
                            //touch move code
                            //Helper.makeText(mContext,"Moved",1);
                            break;

                        case MotionEvent.ACTION_UP:
                            // touch up code
                            //Helper.makeText(mContext, "Released", 1);
                            if (checkBoxSelection != null && checkBoxSelection.size() > 0) {
                                gdSubmit.setColor(Color.parseColor(themeColor));
                            }

                            break;
                    }
                    return false;
                }
            });

            if (surveyScreens.getInput().getInput_type().equalsIgnoreCase("checkbox")) {
                //submitButton.setVisibility(View.INVISIBLE);
                gdSubmit.setColor(colorAlpha);//Color.parseColor(themeColor));
            } else {
                submitButton.setVisibility(View.GONE);
            }
        } catch (Exception ex) {

        }
    }

    private ArrayList<OFRatingsModel> prepareRatingsList(int min, int max) {
        ArrayList<OFRatingsModel> ratingsList = new ArrayList<>();
        OFRatingsModel rm = null;
        while (min <= max) {
            rm = new OFRatingsModel();
            rm.setId(min++);
            rm.setSelected(false);
            ratingsList.add(rm);
        }
        return ratingsList;
    }




    @Override
    public void itemClicked(View v, Object obj, String reserve) {
        OFHelper.v(tag, "OneFlow othervalue [" + obj + "]reserve[" + reserve + "]");
        if (v.getId() == R.id.submit_btn) {
            OFHelper.v(tag, "OneFlow othervalue submit btn");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (checkBoxSelection != null) {
                        if (checkBoxSelection.size() > 0) {
                            String allSelections = checkBoxSelection.toString().replace("[", "");
                            allSelections = allSelections.replace("]", "");
                            allSelections = allSelections.replace(" ", "");
                            OFHelper.v(tag, "OneFlow allselection[" + allSelections + "] str[" + reserve + "]");

                            sa.addUserResponseToList(surveyScreens.get_id(), allSelections, reserve);
                        }
                    }
                }
            }, 1000);
        } else {

            OFHelper.v(tag, "OneFlow inputtype[" + surveyScreens.getInput().getInput_type() + "]isCheckbox[" + surveyScreens.getInput().getInput_type().equalsIgnoreCase("checkbox") + "]ratings[" + surveyScreens.getInput().getInput_type().contains("rating") + "]isStar[" + surveyScreens.getInput().getStars() + "]");
            if (surveyScreens.getInput().getInput_type().contains("rating-emojis")) {
                int position = (int) v.getTag();
                OFHelper.v(tag, "OneFlow inputType[" + surveyScreens.getInput().getStars() + "]position[" + position + "]");
                setSelected(position, true);

            } else if (surveyScreens.getInput().getInput_type().equalsIgnoreCase("rating") ||
                    surveyScreens.getInput().getInput_type().equalsIgnoreCase("rating-5-star")) {
                int position = (int) v.getTag();
                OFHelper.v(tag, "OneFlow inputType[" + surveyScreens.getInput().getStars() + "]position[" + position + "]rating text["+surveyScreens.getInput().getRating_text()+"]");
                setSelected(position, false);
                starRatingLabel.setText(surveyScreens.getInput().getRating_text().get(String.valueOf(position+1)));//ratingsLabel[position]);
            } else if (surveyScreens.getInput().getInput_type().equalsIgnoreCase("nps") ||
                    surveyScreens.getInput().getInput_type().equalsIgnoreCase("rating-numerical")) {
                int position = (int) v.getTag();
                setSelected(position, true);
            } else if (surveyScreens.getInput().getInput_type().equalsIgnoreCase("mcq")) {
                String position = (String) v.getTag();
                if (v instanceof RadioButton) { // added for handling other click

                    OFHelper.v(tag, "OneFlow mcq clicked Position[" + position + "]");
                    OFHelper.v(tag, "OneFlow mcq clicked choices radio id[]other id[" + surveyScreens.getInput().getOtherOption() + "]");
                    if (!surveyScreens.getInput().getOtherOption().equalsIgnoreCase(position)) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sa.addUserResponseToList(surveyScreens.get_id(), position, null);
                            }
                        }, 5);
                    }
                } else {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sa.addUserResponseToList(surveyScreens.get_id(), position, (String) obj);
                        }
                    }, 5);
                }

            } else if (surveyScreens.getInput().getInput_type().equalsIgnoreCase("checkbox")) {
                OFHelper.v(tag, "OneFlow inside checkbox reserve[" + reserve + "]");
                if (v instanceof CheckBox) {
                    CheckBox cb = (CheckBox) v;
                    OFHelper.v(tag, "OneFlow inside checkbox 1");
                    String viewTag = (String) cb.getTag();
                    OFHelper.v(tag, "OneFlow inside checkbox tag[" + viewTag + "]isChecked[" + cb.isChecked() + "]");
                    checkBoxSelectionStatus(viewTag, cb.isChecked(), (String) obj);
                } else {
                    String viewTag = (String) v.getTag();
                    checkBoxSelectionStatus(viewTag, (boolean) obj, reserve);
                }
            }
        }
    }


    private void setSelected(int position, Boolean isSingle) {
        int i = 0;
        OFHelper.v(tag, "OneFlow position [" + position + "]isSingle[" + isSingle + "]");
        try {


            while (i < surveyScreens.getInput().getRatingsList().size()) {
                if (isSingle) {
                    surveyScreens.getInput().getRatingsList().get(i).setSelected(false);
                } else {
                    if (i <= position) {
                        surveyScreens.getInput().getRatingsList().get(i).setSelected(true);
                    } else {
                        surveyScreens.getInput().getRatingsList().get(i).setSelected(false);
                    }
                }
                i++;
            }
            if (isSingle) {
                if (surveyScreens.getInput().getInput_type().equalsIgnoreCase("nps") ||
                        surveyScreens.getInput().getInput_type().equalsIgnoreCase("rating-numerical") ||
                        surveyScreens.getInput().getInput_type().equalsIgnoreCase("rating-5-star")
                ) {
                    for (OFRatingsModel rm : surveyScreens.getInput().getRatingsList()) {
                        OFHelper.v(tag, "OneFlow " + surveyScreens.getInput().getInput_type() + " rm.getId()[" + rm.getId() + "]position[" + position + "]");
                        if (rm.getId() == position) {
                            rm.setSelected(true);
                        }
                    }
                } else {
                    surveyScreens.getInput().getRatingsList().get(position).setSelected(true);
                }
            }
            dashboardAdapter.notifyMyList(surveyScreens.getInput());
            if (submitButton.getVisibility() != View.VISIBLE) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (surveyScreens.getInput().getInput_type().equalsIgnoreCase("rating-5-star") ||
                                surveyScreens.getInput().getInput_type().equalsIgnoreCase("rating-emojis")) {
                            sa.addUserResponseToList(surveyScreens.get_id(), null, String.valueOf(position + 1));
                        } else {
                            sa.addUserResponseToList(surveyScreens.get_id(), null, String.valueOf(position));
                        }
                    }
                }, 1000);
            }
        } catch (Exception ex) {
            OFHelper.e(tag, "setSelect[" + ex.getMessage() + "]");
        }
    }

    ArrayList<String> checkBoxSelection;

    private void checkBoxSelectionStatus(String tag, Boolean isCheck, String str) {
        OFHelper.v(tag, "OneFlow button size tag[" + tag + "]isChecked[" + isCheck + "]othervalue[" + str + "]");
        if (isCheck) { // adding value in the list
            if (!checkBoxSelection.contains(tag)) {
                checkBoxSelection.add(tag);
            }
        } else { // removing value from the list
            checkBoxSelection.remove(tag);
        }


        // OFHelper.v(tag, "OneFlow button size found[" + checkBoxSelection.size() + "]othervalue[" + str + "]btn[" + surveyScreens.getButtons() + "][" + surveyScreens.getButtons().size() + "]");
        if (checkBoxSelection.size() > 0) {
            if (surveyScreens.getButtons() != null) {
                if (surveyScreens.getButtons().size() == 1) {
                    //if (submitButton.getVisibility() != View.VISIBLE) {
                    submitButton.setText(surveyScreens.getButtons().get(0).getTitle());
                    //submitButton.setVisibility(View.VISIBLE);
                    OFHelper.v(tag, "OneFlow color theme[" + themeColor + "]parsed color[" + Color.parseColor(themeColor) + "]");

                    if (!isActive) {
                        transitActive();
                        isActive = true;
                    }
                    //submitButton.startAnimation(animationIn);
                    submitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            OFHelper.v(tag, "OneFlow button size found 1 ");
                            String strLoc = dashboardAdapter.handleCheckboxFromOutside();
                            itemClicked(v, str, strLoc);
                        }
                    });
                    // }
                } else if (surveyScreens.getButtons().size() == 2) {
                    //if (submitButton.getVisibility() != View.VISIBLE) {
                    submitButton.setText(surveyScreens.getButtons().get(0).getTitle());
                    //submitButton.setVisibility(View.VISIBLE);
                    submitButton.setBackgroundColor(Color.parseColor(themeColor));
                    gdSubmit.setColor(Color.parseColor(themeColor));
                    //submitButton.startAnimation(animationIn);
                    submitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            OFHelper.v(tag, "OneFlow button size found 2 ");
                            itemClicked(v, str, "");
                        }
                    });
                }
                    /*cancelButton.setText(surveyScreens.getButtons().get(1).getTitle());
                    cancelButton.setVisibility(View.VISIBLE);
                    cancelButton.setOnClickListener(this);*/
                // }
            } else {
                OFHelper.e(tag, "Button list not found");
            }
        } else {
            //In case of selection reverted


            if (isActive) {
                transitInActive();
                isActive = false;
            }

        }


    }


}
