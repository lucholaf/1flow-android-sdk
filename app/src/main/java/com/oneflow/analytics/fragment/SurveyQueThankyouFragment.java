package com.oneflow.analytics.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.oneflow.analytics.R;
import com.oneflow.analytics.SurveyActivity;
import com.oneflow.analytics.customwidgets.CustomTextView;
import com.oneflow.analytics.customwidgets.CustomTextViewBold;
import com.oneflow.analytics.model.survey.SurveyScreens;
import com.oneflow.analytics.utils.Helper;

import butterknife.ButterKnife;

public class SurveyQueThankyouFragment extends Fragment {



    ImageView thankyouImage,waterMarkImage;

    CustomTextViewBold surveyTitle;

    CustomTextView surveyDescription;

    String tag = this.getClass().getName();

    SurveyScreens surveyScreens;

    public static SurveyQueThankyouFragment newInstance(SurveyScreens ahdList) {
        SurveyQueThankyouFragment myFragment = new SurveyQueThankyouFragment();

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
        View view = inflater.inflate(R.layout.survey_que_thankyou_fragment, container, false);
        ButterKnife.bind(this, view);
        Helper.v(tag, "OneAxis list data[" + surveyScreens + "]");


        thankyouImage = (ImageView)view.findViewById(R.id.thankyou_img);
        waterMarkImage = (ImageView)view.findViewById(R.id.watermark_img);
        surveyTitle = (CustomTextViewBold) view.findViewById(R.id.survey_title);
        surveyDescription = (CustomTextView) view.findViewById(R.id.survey_description);
        //surveyTitle.setText(surveyScreens.getTitle());
        /*if(surveyScreens.getMessage()!=null) {
            surveyDescription.setText(surveyScreens.getMessage());
        }else{
            surveyDescription.setVisibility(View.GONE);
        }*/

        sa.position = sa.screens.size();

        //Glide.with(this).load(R.drawable.thank_you).into(thankyouImage);
        Glide.with(this).load(R.drawable.thank_you).into(new DrawableImageViewTarget(thankyouImage) {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                super.onResourceReady(resource, transition);
                if (resource instanceof GifDrawable) {
                    ((GifDrawable) resource).setLoopCount(1);
                    ((GifDrawable) resource).registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                        @Override
                        public void onAnimationStart(Drawable drawable) {
                            super.onAnimationStart(drawable);
                        }

                        @Override
                        public void onAnimationEnd(Drawable drawable) {
                            super.onAnimationEnd(drawable);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    sa.initFragment();
                                }
                            }, 3000);

                        }
                    });
                }

            }
        });

        return view;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_sdk);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            surveyTitle.startAnimation(animation);
        } else {
            //Helper.makeText(getActivity(), "Visibility Gone", 1);
        }
    }

    SurveyActivity sa;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sa = (SurveyActivity) context;

    }


    public void handleClick(View v) {
        if(v.getId()== R.id.watermark_img){
                String waterMark = "https://www.notion.so/Powered-by-1Flow-logo-should-link-to-website-c186fca5220e41d19f420dd871f9696d";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(waterMark));
                startActivity(browserIntent);

        }
    }

    Dialog dialog;


}
