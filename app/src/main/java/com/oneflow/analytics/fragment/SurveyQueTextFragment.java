package com.oneflow.analytics.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.oneflow.analytics.R;
import com.oneflow.analytics.SurveyActivity;
import com.oneflow.analytics.customwidgets.CustomEditText;
import com.oneflow.analytics.customwidgets.CustomTextView;
import com.oneflow.analytics.customwidgets.CustomTextViewBold;
import com.oneflow.analytics.model.survey.SurveyScreens;
import com.oneflow.analytics.utils.Helper;

import butterknife.ButterKnife;

public class SurveyQueTextFragment extends Fragment implements View.OnClickListener {


    CustomTextViewBold surveyTitle, submitButton;

    CustomEditText userInput;


    CustomTextView surveyInputLimit;

    CustomTextView surveyDescription;
   /* @BindView(R.id.cancel_btn)
    CustomTextViewBold cancelButton;*/


    String tag = this.getClass().getName();
    SurveyScreens surveyScreens;

    public static SurveyQueTextFragment newInstance(SurveyScreens ahdList) {
        SurveyQueTextFragment myFragment = new SurveyQueTextFragment();

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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Helper.v(tag, "OneFlow visible to user");
        if (isVisibleToUser) {

            View[] animateViews = new View[]{surveyTitle, surveyDescription};


            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    Helper.v(tag, "OneFlow animation START [" + i + "]");
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Helper.v(tag, "OneFlow animation END[" + i + "]");
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
            Helper.makeText(getActivity(), "Visibility Gone", 1);
        }
    }

    Animation animationIn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.survey_que_text_fragment, container, false);
        ButterKnife.bind(this, view);


        surveyTitle = (CustomTextViewBold) view.findViewById(R.id.survey_title);
        submitButton = (CustomTextViewBold) view.findViewById(R.id.submit_btn);
        surveyDescription = (CustomTextView) view.findViewById(R.id.survey_description);
        userInput = (CustomEditText) view.findViewById(R.id.child_user_input);
        surveyInputLimit = (CustomTextView) view.findViewById(R.id.text_limit);


        Helper.v(tag, "OneAxis list data[" + surveyScreens + "]");
        animationIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        surveyTitle.setText(surveyScreens.getTitle());
        if (surveyScreens.getMessage() != null) {
            surveyDescription.setText(surveyScreens.getMessage());
        } else {
            surveyDescription.setVisibility(View.GONE);
        }

        surveyInputLimit.setText("0/" + surveyScreens.getInput().getMax_chars());
        Helper.v(tag, " OneFlow onTextChanged min[" + surveyScreens.getInput().getMin_chars() + "]max[" + surveyScreens.getInput().getMax_chars() + "]");
        //setMaxLength(surveyScreens.getInput().getMax_chars());
        userInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                //Helper.v(tag," beforeTextChanged s["+s+"]start["+start+"]after["+after+"]count["+count+"]");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (userInput.getText().toString().length() >= surveyScreens.getInput().getMin_chars()) {
                    // if (surveyScreens.getButtons().size() == 1) {
                    if (submitButton.getVisibility() != View.VISIBLE) {
                        submitButton.setText(surveyScreens.getButtons().get(0).getTitle());
                        submitButton.setVisibility(View.VISIBLE);
                        submitButton.startAnimation(animationIn);
                    }
                   /* } else if (surveyScreens.getButtons().size() == 2) {
                        submitButton.setText(surveyScreens.getButtons().get(0).getTitle());
                        submitButton.setVisibility(View.VISIBLE);
                        submitButton.startAnimation(animationIn);
                        *//*cancelButton.setText(surveyScreens.getButtons().get(1).getTitle());
                        cancelButton.setVisibility(View.VISIBLE);*//*

                    }*/
                } else {
                    if (surveyScreens.getButtons().size() == 1) {
                        submitButton.setVisibility(View.INVISIBLE);

                    } else if (surveyScreens.getButtons().size() == 2) {
                        submitButton.setVisibility(View.INVISIBLE);
                        //cancelButton.setVisibility(View.GONE);
                    }
                }
                if (userInput.getText().toString().length() > surveyScreens.getInput().getMax_chars()) {
                    Helper.makeText(getActivity(), "You have exceeded max length", 1);
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

    private void setMaxLength(int maxLength) {

        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(maxLength);
        surveyInputLimit.setFilters(fArray);
    }


    SurveyActivity sa;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sa = (SurveyActivity) context;
        sa.position++;

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.submit_btn) {
            sa.addUserResponseToList(surveyScreens.get_id(), null, userInput.getText().toString());
        } else if (v.getId() == R.id.cancel_btn) {
            Helper.makeText(getActivity(), "Clicked on cancel button", 1);
        }

    }
}