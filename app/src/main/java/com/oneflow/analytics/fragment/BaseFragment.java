package com.oneflow.analytics.fragment;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import androidx.fragment.app.Fragment;

import com.oneflow.analytics.OFSurveyActivity;
import com.oneflow.analytics.R;

public class BaseFragment extends Fragment {
    public boolean isActive = false;
    public GradientDrawable gdSubmit;
    public OFSurveyActivity sa;
    public void transitActive(){
        int colorFrom = getResources().getColor(R.color.ratings_focused);
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
    public void transitInActive(){
        int colorFrom = Color.parseColor(sa.themeColor);
        int colorTo = getResources().getColor(R.color.ratings_focused);
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
}
