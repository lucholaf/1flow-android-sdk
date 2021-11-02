package com.circo.oneflow.utils;

public interface MyResponseHandler {
    public void onResponseReceived(Constants.ApiHitType hitType, Object obj, int reserve);
}
