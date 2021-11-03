package com.oneflow.analytics.utils;

public interface MyResponseHandler {
    public void onResponseReceived(Constants.ApiHitType hitType, Object obj, int reserve);
}
