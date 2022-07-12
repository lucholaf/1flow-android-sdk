package com.oneflow.analytics;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.oneflow.analytics.utils.OFConstants;
import com.oneflow.analytics.utils.OFMyResponseHandler;

public class OFSecondActivity extends OFSDKBaseActivity implements OFMyResponseHandler {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResponseReceived(OFConstants.ApiHitType hitType, Object obj, Long reserve, String reserved) {

    }
}
