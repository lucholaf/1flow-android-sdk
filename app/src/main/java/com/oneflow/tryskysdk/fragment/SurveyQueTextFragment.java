package com.oneflow.tryskysdk.fragment;

import android.app.Dialog;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oneflow.tryskysdk.R;
import com.oneflow.tryskysdk.SurveyActivity;
import com.oneflow.tryskysdk.adapter.SurveyOptionsAdapter;
import com.oneflow.tryskysdk.customwidgets.CustomEditText;
import com.oneflow.tryskysdk.customwidgets.CustomTextView;
import com.oneflow.tryskysdk.customwidgets.CustomTextViewBold;
import com.oneflow.tryskysdk.model.survey.SurveyScreens;
import com.oneflow.tryskysdk.utils.Helper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SurveyQueTextFragment extends Fragment implements View.OnClickListener {


    @BindView(R.id.survey_title)
    CustomTextViewBold surveyTitle;
    @BindView(R.id.child_user_input)
    CustomEditText userInput;
    @BindView(R.id.text_limit)
    CustomTextView surveyInputLimit;
    @BindView(R.id.survey_description)
    CustomTextView surveyDescription;
    @BindView(R.id.submit_btn)
    CustomTextViewBold submitButton;
    @BindView(R.id.cancel_btn)
    CustomTextViewBold cancelButton;


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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.survey_que_text_fragment, container, false);
        ButterKnife.bind(this, view);

        Helper.v(tag, "OneAxis list data[" + surveyScreens + "]");

        surveyTitle.setText(surveyScreens.getTitle());
        if (surveyScreens.getMessage() != null) {
            surveyDescription.setText(surveyScreens.getMessage());
        } else {
            surveyDescription.setVisibility(View.GONE);
        }

        surveyInputLimit.setText("0/" + surveyScreens.getInput().getMax_chars());
        Helper.v(tag," OneFlow onTextChanged min["+surveyScreens.getInput().getMin_chars()+"]max["+surveyScreens.getInput().getMax_chars()+"]");
        //setMaxLength(surveyScreens.getInput().getMax_chars());
        userInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                //Helper.v(tag," beforeTextChanged s["+s+"]start["+start+"]after["+after+"]count["+count+"]");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (userInput.getText().toString().length() >= surveyScreens.getInput().getMin_chars()) {
                    if (surveyScreens.getButtons().size() == 1) {
                        submitButton.setText(surveyScreens.getButtons().get(0).getTitle());
                        submitButton.setVisibility(View.VISIBLE);

                    } else if (surveyScreens.getButtons().size() == 2) {
                        submitButton.setText(surveyScreens.getButtons().get(0).getTitle());
                        submitButton.setVisibility(View.VISIBLE);
                        cancelButton.setText(surveyScreens.getButtons().get(1).getTitle());
                        cancelButton.setVisibility(View.VISIBLE);

                    }
                }else{
                    if (surveyScreens.getButtons().size() == 1) {
                        submitButton.setVisibility(View.GONE);

                    } else if (surveyScreens.getButtons().size() == 2) {
                        submitButton.setVisibility(View.GONE);
                        cancelButton.setVisibility(View.GONE);
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
        cancelButton.setOnClickListener(this);


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

        switch (v.getId()) {
            case R.id.submit_btn:
                //Helper.makeText(getActivity(),"Clicked on submit button",1);
                //sa.surveyQueViewPager.setCurrentItem(sa.surveyQueViewPager.getCurrentItem()+1);
                sa.addUserResponseToList(surveyScreens.get_id(), null, userInput.getText().toString());
                break;
            case R.id.cancel_btn:
                Helper.makeText(getActivity(), "Clicked on cancel button", 1);
                break;
        }

    }
}
