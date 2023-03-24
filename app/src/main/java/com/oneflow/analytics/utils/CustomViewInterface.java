package com.oneflow.analytics.utils;

import com.oneflow.analytics.model.survey.OFGetSurveyListResponse;

public interface CustomViewInterface {
    public void surveyFound( OFGetSurveyListResponse gslr, String eventName);
}
