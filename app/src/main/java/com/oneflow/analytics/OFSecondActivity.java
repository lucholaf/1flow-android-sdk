package com.oneflow.analytics;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.oneflow.analytics.utils.OFConstants;
import com.oneflow.analytics.utils.OFHelper;
import com.oneflow.analytics.utils.OFMyResponseHandler;

import java.util.HashMap;

public class OFSecondActivity extends AppCompatActivity implements OFMyResponseHandler {
    FrameLayout frameLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        frameLayout = findViewById(R.id.survey_view_custom_child);
        CustomClassView ccv = new CustomClassView(this, frameLayout, getSupportFragmentManager());

    }

    String tag = this.getClass().getName();

    public void clickHandler(View v) {
        OFHelper.v(tag, "OneFlow Clicked on button record log");
        String localTag = "TopLeft";//(String) v.getTag();
        HashMap<String, Object> mapvalues = new HashMap<String, Object>();
        mapvalues.put("testKey1_" + localTag, "testValue1");
        mapvalues.put("namewa", "Bigu");
        mapvalues.put("testKey3_" + localTag, "testValue3");
        mapvalues.put("testKey3_" + localTag, 23);
        OneFlow.recordEvents(localTag, mapvalues, "custom");
    }

    @Override
    public void onResponseReceived(OFConstants.ApiHitType hitType, Object obj, Long reserve, String reserved) {

    }
}
