package com.oneflow.tryskysdk.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oneflow.tryskysdk.R;
import com.oneflow.tryskysdk.adapter.SurveyOptionsAdapter;
import com.oneflow.tryskysdk.customwidgets.CustomTextView;
import com.oneflow.tryskysdk.model.survey.SurveyScreens;
import com.oneflow.tryskysdk.utils.Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SurveyQueFragment extends Fragment {


    @BindView(R.id.survey_title)
    CustomTextView surveyTitle;
    @BindView(R.id.survey_description)
    CustomTextView surveyDescription;
   @BindView(R.id.survey_options_list)
    RecyclerView surveyOptionRecyclerView;

    String tag = this.getClass().getName();
    SurveyScreens surveyScreens;

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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.survey_que_fragment, container, false);
        ButterKnife.bind(this, view);

        Helper.v(tag, "OneAxis list data[" + surveyScreens + "]");
        Helper.v(tag, "OneAxis list title[" + surveyScreens.getTitle() + "]");
        Helper.v(tag, "OneAxis list desc[" + surveyScreens.getMessage() + "]");

        surveyTitle.setText(surveyScreens.getTitle());
        if(surveyScreens.getMessage()!=null) {
            surveyDescription.setText(surveyScreens.getMessage());
        }else{
            surveyDescription.setVisibility(View.GONE);
        }


        SurveyOptionsAdapter dashboardAdapter = new SurveyOptionsAdapter(getActivity(),surveyScreens.getInput());
        RecyclerView.LayoutManager mLayoutManager = null;
        if(surveyScreens.getInput().getInput_type().equalsIgnoreCase("rating")) {
            mLayoutManager = new GridLayoutManager(getActivity(), surveyScreens.getInput().getMax_val()+1);
        }else{
            mLayoutManager =new LinearLayoutManager(getActivity());
        }
        surveyOptionRecyclerView.setLayoutManager(mLayoutManager);
        surveyOptionRecyclerView.setItemAnimator(new DefaultItemAnimator());
        surveyOptionRecyclerView.setAdapter(dashboardAdapter);

        if(surveyScreens.getInput().getInput_type().equalsIgnoreCase("thank_you"))
        {
            surveyOptionRecyclerView.setVisibility(View.GONE);
        }else{
            surveyOptionRecyclerView.setVisibility(View.VISIBLE);
        }

        return view;

    }

    //private mna_new mna;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //mna = (mna_new)context;

    }


    Dialog dialog;


}
