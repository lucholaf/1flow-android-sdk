package com.oneflow.analytics.utils;

import android.content.Context;

import androidx.fragment.app.FragmentManager;

import com.oneflow.analytics.model.survey.OFGetSurveyListResponse;

public interface CustomViewInterface {
    public void surveyFound( OFGetSurveyListResponse gslr, String eventName);
}
