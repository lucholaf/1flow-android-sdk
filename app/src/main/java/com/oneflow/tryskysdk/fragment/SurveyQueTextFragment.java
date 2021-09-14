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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oneflow.tryskysdk.R;
import com.oneflow.tryskysdk.adapter.SurveyOptionsAdapter;
import com.oneflow.tryskysdk.customwidgets.CustomEditText;
import com.oneflow.tryskysdk.customwidgets.CustomTextView;
import com.oneflow.tryskysdk.model.survey.SurveyScreens;
import com.oneflow.tryskysdk.utils.Helper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SurveyQueTextFragment extends Fragment {


    @BindView(R.id.survey_title)
    CustomTextView surveyTitle;
    @BindView(R.id.child_user_input)
    CustomEditText userInput;
    @BindView(R.id.text_limit)
    CustomTextView surveyInputLimit;
    @BindView(R.id.survey_description)
    CustomTextView surveyDescription;


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
        //setMaxLength(surveyScreens.getInput().getMax_chars());
        userInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                //Helper.v(tag,"OneAxis beforeTextChanged s["+s+"]start["+start+"]after["+after+"]count["+count+"]");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (userInput.getText().toString().length() > surveyScreens.getInput().getMax_chars()) {
                    Helper.makeText(getActivity(), "You have exceeded max length", 1);
                    userInput.setText(userInput.getText().toString().substring(0,userInput.getText().length()-count));
                    userInput.setSelection(userInput.getText().toString().length());
                }

                surveyInputLimit.setText(userInput.getText().toString().length() + "/" + surveyScreens.getInput().getMax_chars());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;

    }

    private void setMaxLength(int maxLength) {

        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(maxLength);
        surveyInputLimit.setFilters(fArray);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //mna = (mna_new)context;

    }


    Dialog dialog;


}
