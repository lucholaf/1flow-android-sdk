package com.circo.oneflow.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.circo.oneflow.R;
import com.circo.oneflow.SurveyActivity;
import com.circo.oneflow.adapter.SurveyOptionsAdapter;
import com.circo.oneflow.customwidgets.CustomTextView;
import com.circo.oneflow.customwidgets.CustomTextViewBold;
import com.circo.oneflow.model.survey.RatingsModel;
import com.circo.oneflow.model.survey.SurveyScreens;
import com.circo.oneflow.utils.Helper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SurveyQueFragment extends Fragment implements View.OnClickListener {


    CustomTextViewBold surveyTitle, submitButton;
    RecyclerView surveyOptionRecyclerView;
    CustomTextView ratingsNotLike, ratingsFullLike, surveyDescription;


    /*@BindView(R.id.cancel_btn)
    CustomTextViewBold cancelButton;*/
    RelativeLayout optionLayout;
    //this is for testing

    String tag = this.getClass().getName();
    SurveyScreens surveyScreens;
    SurveyOptionsAdapter dashboardAdapter;
    Animation animation1, animation2, animation3, animation4, animationIn;//animationOut;

    public static SurveyQueFragment newInstance(SurveyScreens ahdList) {
        SurveyQueFragment myFragment = new SurveyQueFragment();

        Bundle args = new Bundle();
        args.putSerializable("data", ahdList);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        surveyScreens = (SurveyScreens) getArguments().getSerializable("data");

    }

    int i = 0;

    @Override
    public void onResume() {
        super.onResume();
        Helper.v(tag, "OneFlow OnResume");


        View[] animateViews = new View[]{surveyTitle, surveyDescription, optionLayout};


        Animation[] annim = new Animation[]{animation1, animation2, animation3};

        if (i == 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    annim[i].setFillAfter(true);
                    animateViews[i].startAnimation(annim[i]);

                }
            }, 800);

            animation1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    Helper.v(tag, "OneFlow animation START [" + i + "]");
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Helper.v(tag, "OneFlow animation END[" + i + "]");
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
                    Helper.v(tag, "OneFlow animation REPEAT[" + i + "]");
                }
            });
            animation2.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    Helper.v(tag, "OneFlow animation START [" + i + "]");
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Helper.v(tag, "OneFlow animation END[" + i + "]");
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
                    Helper.v(tag, "OneFlow animation REPEAT[" + i + "]");
                }
            });
            animation3.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    Helper.v(tag, "OneFlow animation START [" + i + "]");
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Helper.v(tag, "OneFlow animation END[" + i + "]");

                    i++;
                    if (i < animateViews.length) {
                        animateViews[i].setVisibility(View.VISIBLE);
                        //animateViews[i].clearAnimation();
                        animateViews[i].startAnimation(annim[i]);
                    }

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    Helper.v(tag, "OneFlow animation REPEAT[" + i + "]");
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


        surveyTitle = (CustomTextViewBold) view.findViewById(R.id.survey_title_que);
        submitButton = (CustomTextViewBold) view.findViewById(R.id.submit_btn);
        surveyDescription = (CustomTextView) view.findViewById(R.id.survey_description_que);
        ratingsNotLike = (CustomTextView) view.findViewById(R.id.ratings_not_like);
        ratingsFullLike = (CustomTextView) view.findViewById(R.id.ratings_full_like);
        surveyOptionRecyclerView = (RecyclerView) view.findViewById(R.id.survey_options_list);
        optionLayout = (RelativeLayout) view.findViewById(R.id.option_layout);

        submitButton.setOnClickListener(this);

        Helper.v(tag, "OneAxis list data[" + surveyScreens + "]");
        Helper.v(tag, "OneAxis list title[" + surveyScreens.getTitle() + "]");
        Helper.v(tag, "OneAxis list desc[" + surveyScreens.getMessage() + "]");


        animation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        animation2 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        animation3 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        animation4 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        animationIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        //animationOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);

        surveyTitle.setText(surveyScreens.getTitle());

        if (surveyScreens.getMessage() != null) {
            surveyDescription.setText(surveyScreens.getMessage());
        } else {
            surveyDescription.setVisibility(View.GONE);
        }

        Helper.v(tag, "OneFlow input type [" + surveyScreens.getInput().getInput_type() + "][" + surveyScreens.getInput().getStars() + "]min[" + surveyScreens.getInput().getMin_val() + "][" + surveyScreens.getInput().getMax_val() + "][][][]");
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

                surveyScreens.getInput().setRatingsList(prepareRatingsList(surveyScreens.getInput().getMin_val(), surveyScreens.getInput().getMax_val()));
                ratingsNotLike.setVisibility(View.GONE);
                ratingsFullLike.setVisibility(View.GONE);
            }
        }/*else if (surveyScreens.getInput().getInput_type().equalsIgnoreCase("mcq")) {
            if (surveyScreens.getInput() != null) {

                surveyScreens.getInput().setRatingsList(prepareRatingsList(surveyScreens.getInput().getMin_val(), surveyScreens.getInput().getMax_val()));
                ratingsNotLike.setVisibility(View.GONE);
                ratingsFullLike.setVisibility(View.GONE);
            }
        }else if (surveyScreens.getInput().getInput_type().equalsIgnoreCase("checkbox")) {
            if (surveyScreens.getInput() != null) {

                surveyScreens.getInput().setRatingsList(prepareRatingsList(surveyScreens.getInput().getMin_val(), surveyScreens.getInput().getMax_val()));
                ratingsNotLike.setVisibility(View.GONE);
                ratingsFullLike.setVisibility(View.GONE);
            }
        }*/ else if (surveyScreens.getInput().getInput_type().contains("rating-emojis")) {
            if (surveyScreens.getInput() != null) {

                surveyScreens.getInput().setRatingsList(prepareRatingsList(surveyScreens.getInput().getMin_val(), surveyScreens.getInput().getMax_val()));
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
        Helper.v(tag, "OneFlow input type min after[" + surveyScreens.getInput().getMin_val() + "][" + surveyScreens.getInput().getMax_val() + "]");

        RecyclerView.LayoutManager mLayoutManager = null;
        if (surveyScreens.getInput().getInput_type().contains("rating") || surveyScreens.getInput().getInput_type().contains("nps")) {
            Helper.v(tag, "OneFlow gridLayout set");
            mLayoutManager = new GridLayoutManager(getActivity(), (surveyScreens.getInput().getMax_val() + 1) - surveyScreens.getInput().getMin_val());
        } else {
            if (surveyScreens.getInput().getChoices() != null) {
                if (surveyScreens.getInput().getChoices().size() > 0) {
                    Helper.v(tag, "OneFlow inputtype choices init");
                    checkBoxSelection = new ArrayList<String>();
                }
            }

            Helper.v(tag, "OneFlow linearlayout set");
            mLayoutManager = new LinearLayoutManager(getActivity());
        }

        dashboardAdapter = new SurveyOptionsAdapter(getActivity(), surveyScreens.getInput(), this, sa.themeColor);

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

    private ArrayList<RatingsModel> prepareRatingsList(int min, int max) {
        ArrayList<RatingsModel> ratingsList = new ArrayList<>();
        RatingsModel rm = null;
        while (min <= max) {
            rm = new RatingsModel();
            rm.setId(min++);
            rm.setSelected(false);
            ratingsList.add(rm);
        }
        return ratingsList;
    }

    SurveyActivity sa;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Helper.v(tag, "OneFlow onAttach called");
        sa = (SurveyActivity) context;
        sa.position++;

    }


    @Override
    public void onClick(View v) {
        // Helper.makeText(getActivity(), "Clicked on [" + v.getTag() + "]", 1);

        if (v.getId() == R.id.submit_btn) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (checkBoxSelection.size() > 0) {
                        String allSelections = checkBoxSelection.toString().replace("[", "");
                        allSelections = allSelections.replace("]", "");
                        allSelections = allSelections.replace(" ", "");
                        Helper.v(tag, "OneFlow allselection[" + allSelections + "]");
                        sa.addUserResponseToList(surveyScreens.get_id(), null, allSelections);
                    } else {
                        sa.initFragment();
                    }
                }
            }, 1000);
        } else {

            Helper.v(tag, "OneFlow inputtype[" + surveyScreens.getInput().getInput_type() + "]isCheckbox[" + surveyScreens.getInput().getInput_type().equalsIgnoreCase("checkbox") + "]ratings[" + surveyScreens.getInput().getInput_type().contains("rating") + "]isStar[" + surveyScreens.getInput().getStars() + "]");
            if (surveyScreens.getInput().getInput_type().contains("rating")) {
                int position = (int) v.getTag();

                Helper.v(tag, "OneFlow inputType[" + surveyScreens.getInput().getStars() + "]position[" + position + "]");
                if (surveyScreens.getInput().getStars()) {
                    setSelected(position, false);
                } else {
                    setSelected(position, true);
                }
            } else if (surveyScreens.getInput().getInput_type().contains("nps")) {
                int position = (int) v.getTag();
                setSelected(position, true);
            } else if (surveyScreens.getInput().getInput_type().equalsIgnoreCase("mcq")) {

                RadioButton rb = (RadioButton) v;
                String position = (String) v.getTag();
                Helper.v(tag, "OneFlow mcq clicked Position[" + position + "]");
                    /*if (rb.isChecked()) {
                        ((View) rb.getParent()).setBackgroundResource(R.drawable.rounded_rectangle_with_border_blue);
                    } else {
                        ((View) rb.getParent()).setBackgroundResource(R.drawable.rounded_rectangle_with_border_gray);
                    }*/
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sa.addUserResponseToList(surveyScreens.get_id(), position, null);
                    }
                }, 1000);

            } else if (surveyScreens.getInput().getInput_type().equalsIgnoreCase("checkbox")) {
                Helper.v(tag, "OneFlow inside checkbox");
                CheckBox cb = (CheckBox) v;
                Helper.v(tag, "OneFlow inside checkbox 1");
                String viewTag = (String) cb.getTag();
                Helper.v(tag, "OneFlow inside checkbox tag[" + viewTag + "]isChecked[" + cb.isChecked() + "]");
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
        Helper.v(tag, "OneFlow position [" + position + "]isSingle[" + isSingle + "]");
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
                for (RatingsModel rm : surveyScreens.getInput().getRatingsList()) {
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


        Helper.v(tag, "OneFlow button size found[" + checkBoxSelection.size() + "]");
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
