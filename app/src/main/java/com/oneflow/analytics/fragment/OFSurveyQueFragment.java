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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oneflow.analytics.OneFlow;
import com.oneflow.analytics.R;
import com.oneflow.analytics.OFSurveyActivity;
import com.oneflow.analytics.adapter.OFSurveyOptionsAdapter;
import com.oneflow.analytics.customwidgets.OFCustomTextView;
import com.oneflow.analytics.customwidgets.OFCustomTextViewBold;
import com.oneflow.analytics.model.survey.OFRatingsModel;
import com.oneflow.analytics.model.survey.OFSurveyScreens;
import com.oneflow.analytics.utils.OFHelper;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class OFSurveyQueFragment extends Fragment implements View.OnClickListener {


    OFCustomTextViewBold surveyTitle, submitButton;
    RecyclerView surveyOptionRecyclerView;
    OFCustomTextView ratingsNotLike, ratingsFullLike, surveyDescription;


    /*@BindView(R.id.cancel_btn)
    CustomTextViewBold cancelButton;*/
    RelativeLayout optionLayout;
    //this is for testing

    String tag = this.getClass().getName();
    OFSurveyScreens surveyScreens;
    OFSurveyOptionsAdapter dashboardAdapter;
    Animation animation1, animation2, animation3, animation4, animationIn;//animationOut;

    public static OFSurveyQueFragment newInstance(OFSurveyScreens ahdList) {
        OFSurveyQueFragment myFragment = new OFSurveyQueFragment();

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

    int i = 0;

    @Override
    public void onResume() {
        super.onResume();
        OFHelper.v(tag, "OneFlow OnResume");


        View[] animateViews = new View[]{surveyTitle, surveyDescription, optionLayout};


        Animation[] annim = new Animation[]{animation1, animation2, animation3};

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

           /* }
        },1100);*/
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.survey_que_fragment, container, false);
        ButterKnife.bind(this, view);


        surveyTitle = (OFCustomTextViewBold) view.findViewById(R.id.survey_title_que);
        submitButton = (OFCustomTextViewBold) view.findViewById(R.id.submit_btn);
        surveyDescription = (OFCustomTextView) view.findViewById(R.id.survey_description_que);
        ratingsNotLike = (OFCustomTextView) view.findViewById(R.id.ratings_not_like);
        ratingsFullLike = (OFCustomTextView) view.findViewById(R.id.ratings_full_like);
        surveyOptionRecyclerView = (RecyclerView) view.findViewById(R.id.survey_options_list);
        optionLayout = (RelativeLayout) view.findViewById(R.id.option_layout);

        submitButton.setOnClickListener(this);
        submitButtonBeautification();
        OFHelper.v(tag, "OneAxis list data[" + surveyScreens + "]");
        OFHelper.v(tag, "OneAxis list title[" + surveyScreens.getTitle() + "]");
        OFHelper.v(tag, "OneAxis list desc[" + surveyScreens.getMessage() + "]");


        animation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_sdk);
        animation2 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_sdk);
        animation3 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_sdk);
        animation4 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_sdk);
        animationIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_sdk);
        //animationOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);

        if(OneFlow.titleFace!=null) {
            if(OneFlow.titleFace.getTypeface()!=null) {
                surveyTitle.setTypeface(OneFlow.titleFace.getTypeface());
            }
            if(OneFlow.titleFace.getFontSize()!=null) {
                surveyTitle.setTextSize(OneFlow.titleFace.getFontSize());
            }
        }
        surveyTitle.setText(surveyScreens.getTitle());

        if (surveyScreens.getMessage() != null) {
            if(OneFlow.subTitleFace!=null) {
                if (OneFlow.subTitleFace != null) {
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


        if (surveyScreens.getInput().getRating_min_text() != null) {
            ratingsNotLike.setText(surveyScreens.getInput().getRating_min_text());
        }
        if (surveyScreens.getInput().getRating_max_text() != null) {
            ratingsFullLike.setText(surveyScreens.getInput().getRating_max_text());
        }

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
        }else if (surveyScreens.getInput().getInput_type().contains("rating-emojis")) {
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
        } else if (surveyScreens.getInput().getInput_type().equalsIgnoreCase("rating") || surveyScreens.getInput().getInput_type().equalsIgnoreCase("rating-5-star")) {
            mLayoutManager = new GridLayoutManager(getActivity(), 5);
        } else {
            if (surveyScreens.getInput().getChoices() != null) {
                if (surveyScreens.getInput().getChoices().size() > 0) {
                    OFHelper.v(tag, "OneFlow inputtype choices init");
                    checkBoxSelection = new ArrayList<String>();
                }
            }

            OFHelper.v(tag, "OneFlow linearlayout set");
            mLayoutManager = new LinearLayoutManager(getActivity());
        }

        OFHelper.v(tag, "OneFlow theme color [" + sa.themeColor + "]");
        dashboardAdapter = new OFSurveyOptionsAdapter(getActivity(), surveyScreens.getInput(), this, sa.themeColor);

        surveyOptionRecyclerView.setLayoutManager(mLayoutManager);
        surveyOptionRecyclerView.setItemAnimator(new DefaultItemAnimator());
        surveyOptionRecyclerView.setAdapter(dashboardAdapter);

        if (surveyScreens.getInput().getInput_type().equalsIgnoreCase("checkbox")) {
            if (surveyScreens.getButtons() != null) {
                if (surveyScreens.getButtons().size() > 0) {

                }

            }
        }
        return view;

    }

    private void submitButtonBeautification() {
        GradientDrawable gdSubmit = (GradientDrawable) (submitButton).getBackground();
        gdSubmit.setColor(Color.parseColor(sa.themeColor));
        int colorAlpha = ColorUtils.setAlphaComponent(Color.parseColor(sa.themeColor), 125);
        submitButton.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        gdSubmit.setColor(colorAlpha);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // touch move code
                        //Helper.makeText(mContext,"Moved",1);
                        break;

                    case MotionEvent.ACTION_UP:
                        // touch up code
                        //Helper.makeText(mContext, "Released", 1);
                        gdSubmit.setColor(Color.parseColor(sa.themeColor));
                        break;
                }
                return false;
            }
        });
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

    OFSurveyActivity sa;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        OFHelper.v(tag, "OneFlow onAttach called");
        sa = (OFSurveyActivity) context;
        sa.position++;

    }


    @Override
    public void onClick(View v) {
        // Helper.makeText(getActivity(), "Clicked on [" + v.getTag() + "]", 1);

        if (v.getId() == R.id.submit_btn) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (checkBoxSelection != null) {
                        if (checkBoxSelection.size() > 0) {
                            String allSelections = checkBoxSelection.toString().replace("[", "");
                            allSelections = allSelections.replace("]", "");
                            allSelections = allSelections.replace(" ", "");
                            OFHelper.v(tag, "OneFlow allselection[" + allSelections + "]");
                            sa.addUserResponseToList(surveyScreens.get_id(), null, allSelections);
                        } else {
                            sa.initFragment();
                        }
                    } else {
                        sa.initFragment();
                    }
                }
            }, 1000);
        } else {

            OFHelper.v(tag, "OneFlow inputtype[" + surveyScreens.getInput().getInput_type() + "]isCheckbox[" + surveyScreens.getInput().getInput_type().equalsIgnoreCase("checkbox") + "]ratings[" + surveyScreens.getInput().getInput_type().contains("rating") + "]isStar[" + surveyScreens.getInput().getStars() + "]");
            if (surveyScreens.getInput().getInput_type().contains("rating-emojis")) {
                int position = (int) v.getTag();
                OFHelper.v(tag, "OneFlow inputType[" + surveyScreens.getInput().getStars() + "]position[" + position + "]");
                setSelected(position, true);

            } else if (surveyScreens.getInput().getInput_type().equalsIgnoreCase("rating") || surveyScreens.getInput().getInput_type().equalsIgnoreCase("rating-5-star")) {
                int position = (int) v.getTag();
                OFHelper.v(tag, "OneFlow inputType[" + surveyScreens.getInput().getStars() + "]position[" + position + "]");
                setSelected(position, false);

            } else if (surveyScreens.getInput().getInput_type().equalsIgnoreCase("nps") || surveyScreens.getInput().getInput_type().equalsIgnoreCase("rating-numerical")) {
                int position = (int) v.getTag();
                setSelected(position, true);
            } else if (surveyScreens.getInput().getInput_type().equalsIgnoreCase("mcq")) {

                RadioButton rb = (RadioButton) v;
                String position = (String) v.getTag();
                OFHelper.v(tag, "OneFlow mcq clicked Position[" + position + "]");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sa.addUserResponseToList(surveyScreens.get_id(), position, null);
                    }
                }, 1000);

            } else if (surveyScreens.getInput().getInput_type().equalsIgnoreCase("checkbox")) {
                OFHelper.v(tag, "OneFlow inside checkbox");
                CheckBox cb = (CheckBox) v;
                OFHelper.v(tag, "OneFlow inside checkbox 1");
                String viewTag = (String) cb.getTag();
                OFHelper.v(tag, "OneFlow inside checkbox tag[" + viewTag + "]isChecked[" + cb.isChecked() + "]");
                    /*if (cb.isChecked()) {
                        ((View) cb.getParent()).setBackgroundResource(R.drawable.rounded_rectangle_with_border_blue);
                    } else {
                        ((View) cb.getParent()).setBackgroundResource(R.drawable.rounded_rectangle_with_border_gray);
                    }*/
                checkBoxSelectionStatus(viewTag, cb.isChecked());
            }
        }
    }


    private void setSelected(int position, Boolean isSingle) {
        int i = 0;
        OFHelper.v(tag, "OneFlow position [" + position + "]isSingle[" + isSingle + "]");
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
                    sa.addUserResponseToList(surveyScreens.get_id(), String.valueOf(position), null);
                }
            }, 1000);
        }
    }

    ArrayList<String> checkBoxSelection;

    private void checkBoxSelectionStatus(String tag, Boolean isCheck) {


        if (isCheck) { // adding value in the list
            checkBoxSelection.add(tag);
        } else { // removing value from the list
            checkBoxSelection.remove(tag);
        }


        OFHelper.v(tag, "OneFlow button size found[" + checkBoxSelection.size() + "]");
        if (checkBoxSelection.size() > 0) {
            if (surveyScreens.getButtons() != null) {
                if (surveyScreens.getButtons().size() == 1) {
                    if (submitButton.getVisibility() != View.VISIBLE) {
                        submitButton.setText(surveyScreens.getButtons().get(0).getTitle());
                        submitButton.setVisibility(View.VISIBLE);
                        submitButton.startAnimation(animationIn);
                        submitButton.setOnClickListener(this);
                    }
                } else if (surveyScreens.getButtons().size() == 2) {
                    if (submitButton.getVisibility() != View.VISIBLE) {
                        submitButton.setText(surveyScreens.getButtons().get(0).getTitle());
                        submitButton.setVisibility(View.VISIBLE);
                        submitButton.startAnimation(animationIn);
                        submitButton.setOnClickListener(this);
                    }
                    /*cancelButton.setText(surveyScreens.getButtons().get(1).getTitle());
                    cancelButton.setVisibility(View.VISIBLE);
                    cancelButton.setOnClickListener(this);*/
                }
            }
        } else {
            //In case of selection reverted

            submitButton.setVisibility(View.INVISIBLE);
            //submitButton.startAnimation(animationOut);

           /* cancelButton.setVisibility(View.INVISIBLE);
            cancelButton.startAnimation(animationOut);*/
        }


    }

}
