package com.oneflow.tryskysdk.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.oneflow.tryskysdk.R;
import com.oneflow.tryskysdk.SurveyActivity;
import com.oneflow.tryskysdk.adapter.SurveyOptionsAdapter;
import com.oneflow.tryskysdk.customwidgets.CustomTextView;
import com.oneflow.tryskysdk.customwidgets.CustomTextViewBold;
import com.oneflow.tryskysdk.model.survey.SurveyScreens;
import com.oneflow.tryskysdk.utils.Helper;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SurveyQueThankyouFragment extends Fragment {


    @BindView(R.id.thankyou_img)
    ImageView thankyouImage;
    @BindView(R.id.survey_title)
    CustomTextViewBold surveyTitle;
    @BindView(R.id.survey_description)
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

        //surveyTitle.setText(surveyScreens.getTitle());
        /*if(surveyScreens.getMessage()!=null) {
            surveyDescription.setText(surveyScreens.getMessage());
        }else{
            surveyDescription.setVisibility(View.GONE);
        }*/

        sa.position = sa.screens.size();

        //Glide.with(this).load(R.drawable.thank_you).into(thankyouImage);
        Glide.with(this).load(R.drawable.thank_you).into(new DrawableImageViewTarget(thankyouImage){
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                super.onResourceReady(resource, transition);
                if(resource instanceof GifDrawable){
                    ((GifDrawable)resource).setLoopCount(1);
                    ((GifDrawable)resource).registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
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
                            },500);

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
        if(isVisibleToUser){
            Animation animation = AnimationUtils.loadAnimation(getActivity(),R.anim.fade_in);
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
        }else{
            Helper.makeText(getActivity(),"Visibility Gone",1);
        }
    }
    SurveyActivity sa;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sa = (SurveyActivity) context;

    }


    Dialog dialog;


}
