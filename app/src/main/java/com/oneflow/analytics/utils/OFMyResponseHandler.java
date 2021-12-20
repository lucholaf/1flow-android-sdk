package com.oneflow.analytics.utils;

public interface OFMyResponseHandler {
    public void onResponseReceived(OFConstants.ApiHitType hitType, Object obj, Long reserve);
}
