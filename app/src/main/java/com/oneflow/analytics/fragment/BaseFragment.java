package com.oneflow.analytics.fragment;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.oneflow.analytics.OFSurveyActivity;
import com.oneflow.analytics.R;
import com.oneflow.analytics.adapter.OFSurveyOptionsAdapter;
import com.oneflow.analytics.model.survey.OFSDKSettingsTheme;
import com.oneflow.analytics.utils.OFHelper;

public class BaseFragment extends Fragment {
    public boolean isActive = false;
    public GradientDrawable gdSubmit;
    public OFSurveyActivity sa;
    LinearLayout waterMarkLayout;

    public void transitActive() {
        int colorFrom = OFHelper.manipulateColor(Color.parseColor(sa.themeColor),0.5f);//getResources().getColor(R.color.ratings_focused);
        int colorTo = Color.parseColor(sa.themeColor);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(250); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                //submitButton.setBackgroundColor((int) animator.getAnimatedValue());
                gdSubmit.setColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
    }

    public void transitInActive() {
        int colorFrom = Color.parseColor(sa.themeColor);
        int colorTo = OFHelper.manipulateColor(Color.parseColor(sa.themeColor),0.5f);//getResources().getColor(R.color.ratings_focused);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(250); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                //submitButton.setBackgroundColor((int) animator.getAnimatedValue());
                gdSubmit.setColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
    }

    public void handleWaterMarkStyle(OFSDKSettingsTheme theme) {


        if (theme.getRemove_watermark()) {
             waterMarkLayout.setVisibility(View.GONE);
        } else {
            waterMarkLayout.setVisibility(View.VISIBLE);
        }
        int colorAlpha = OFHelper.manipulateColor(Color.parseColor(OFHelper.handlerColor(theme.getText_color())), 0.1f);
        GradientDrawable gd = (GradientDrawable) waterMarkLayout.getBackground();
        waterMarkLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String waterMark = "https://1flow.app/?utm_source=1flow-android-sdk&utm_medium=watermark&utm_campaign=real-time+feedback+powered+by+1flow";//https://www.notion.so/Powered-by-1Flow-logo-should-link-to-website-c186fca5220e41d19f420dd871f9696d";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(waterMark));
                startActivity(browserIntent);
            }
        });
        waterMarkLayout.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        gd.setColor(colorAlpha);
                        break;

                    case MotionEvent.ACTION_MOVE:
                        // touch move code
                        //Helper.makeText(mContext,"Moved",1);
                        break;

                    case MotionEvent.ACTION_UP:

                        gd.setColor(null);


                        break;
                }
                return false;
            }
        });
    }
}
