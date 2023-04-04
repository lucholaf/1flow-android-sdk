package com.oneflow.analytics.fragment;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.oneflow.analytics.OFSDKBaseActivity;
import com.oneflow.analytics.OFSurveyActivityFullScreen;
import com.oneflow.analytics.controller.OFEventController;
import com.oneflow.analytics.customwidgets.OFCustomeWebView;
import com.oneflow.analytics.model.survey.OFSDKSettingsTheme;
import com.oneflow.analytics.model.survey.OFSurveyScreens;
import com.oneflow.analytics.utils.OFConstants;
import com.oneflow.analytics.utils.OFHelper;

import java.util.HashMap;

public class BaseFragment extends Fragment {
    public boolean isActive = false;
    public GradientDrawable gdSubmit;
    public OFSDKBaseActivity sa;
    public View webLayout;
    public ProgressBar pBar;
    public OFCustomeWebView webContent;
    LinearLayout waterMarkLayout,infoWebLayout;
    public OFSurveyScreens surveyScreens;
    public OFSDKSettingsTheme sdkTheme;
    public String themeColor;
    public String tag = this.getClass().getName();
    //CustomFrag customFrag;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //sa = (OFSurveyActivityBottom) context;
        try {
            sa = (OFSDKBaseActivity) context;
            OFHelper.v(tag,"1Flow custom survery reading");
        }catch(Exception ex){
            OFHelper.v(tag,"1Flow custom survery exception");

            sa = null;
            //customFrag = CustomFrag.newInstance();
           // OFHelper.v(tag,"OneFlow custom survery exception ["+customFrag+"]");
            ex.printStackTrace();
        }


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        OFHelper.v(tag,"1Flow frag onSaveInstanceState called 0");
        outState.putSerializable("data",surveyScreens);
        outState.putSerializable("theme",sdkTheme);
        outState.putSerializable("themeColor",themeColor);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(savedInstanceState!=null){
            OFHelper.v(tag,"1Flow frag onCreate called 0");
            surveyScreens = (OFSurveyScreens)savedInstanceState.getSerializable("data");
            sdkTheme = (OFSDKSettingsTheme) savedInstanceState.getSerializable("theme");
            themeColor = (String) savedInstanceState.getString("themeColor");
        }else {
            OFHelper.v(tag,"1Flow frag onCreate called 1");
            surveyScreens = (OFSurveyScreens) getArguments().getSerializable("data");
            sdkTheme = (OFSDKSettingsTheme) getArguments().getSerializable("theme");
            themeColor = (String) getArguments().getString("themeColor");
        }
//        OFHelper.v(tag,"1Flow frag data contains["+surveyScreens.getMediaEmbedHTML().contains("/undefined")+"]");
        OFHelper.v(tag,"1Flow frag data["+surveyScreens.getMediaEmbedHTML() +"]");
        //https://www.loom.com/embed/31fdc69a9331436eb56ae41807f7f3ab
       /* if(surveyScreens.getMediaEmbedHTML()!=null) {
            if (surveyScreens.getMediaEmbedHTML().contains("/undefined")) {
                String str = surveyScreens.getMediaEmbedHTML();
                str = surveyScreens.getMediaEmbedHTML().replace("\n", "");
                str = str.replace("undefined", "31fdc69a9331436eb56ae41807f7f3ab");
                surveyScreens.setMediaEmbedHTML(str);
            }
        }*/




    }
    public int thisViewHeight = 0;
    public void setThisViewHeight(int newHeight){
        if(newHeight>thisViewHeight){
            thisViewHeight = newHeight;
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(!OFHelper.validateString(surveyScreens.getMediaEmbedHTML()).equalsIgnoreCase("NA")) {
            view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    //sa.resetHeight(((View)view.getParent()).getHeight());
                    //sa.resetHeight(((View)((View)(((View)view.getParent())).getParent()).getParent()).getHeight());
                    try {
                        setThisViewHeight(((View) ((View) ((View) ((View) (((View) view.getParent())).getParent()).getParent()).getParent()).getParent()).getHeight());
                    }catch (Exception ex){
                        setThisViewHeight(((View) ((View) ((View) (((View) view.getParent())).getParent()).getParent()).getParent()).getHeight());
                    }
                    //sa.resetHeight(thisViewHeight);
                    //OFHelper.v(tag, "1Flow display inside view created now [" + ((View) ((View) ((View) ((View) (((View) view.getParent())).getParent()).getParent()).getParent()).getParent()).getHeight() + "][]");

                }
            });
            OFHelper.e(tag, "1Flow view created now [" + view.getHeight() + "]parent[" + ((View) view.getParent()).getHeight() + "]");
        }
    }
    public void setupWeb(){

        double[] data = OFHelper.getScreenSize(getActivity());
        OFHelper.v(tag, "1Flow Window size width["+data[0]+"]");

        if(OFHelper.validateString(surveyScreens.getMediaEmbedHTML()).equalsIgnoreCase("NA")){
            webLayout.setVisibility(View.GONE);
        }else{
            if(surveyScreens.getMediaEmbedHTML().contains("<video")|| surveyScreens.getMediaEmbedHTML().contains("<iframe")){
                webContent.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            }else{
                webContent.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            webContent.getSettings().setJavaScriptEnabled(true);
            webContent.getSettings().setMediaPlaybackRequiresUserGesture(false);
            webContent.getSettings().setBlockNetworkImage(false);
            webContent.setBackgroundColor(Color.TRANSPARENT);
            webLayout.setVisibility(View.VISIBLE);
            webContent.setVisibility(View.VISIBLE);
            //webLayout.setVisibility(View.VISIBLE);

            webContent.setWebChromeClient(new WebChromeClient() {
                                              @Override
                                              public void onProgressChanged(WebView view, int newProgress) {
                                                  super.onProgressChanged(view, newProgress);
                                                  //OFHelper.v(tag,"1Flow display web progress["+newProgress+"]["+view.getHeight()+"]["+webContent.getHeight()+"]["+((View)((View)((View)((View)(((View)view.getParent())).getParent()).getParent()).getParent()).getParent()).getHeight()+"][]");
                                                    try {
                                                        if (((View) ((View) ((View) ((View) (((View) view.getParent())).getParent()).getParent()).getParent()).getParent()).getParent() != null) {
                                                            setThisViewHeight(((View) ((View) ((View) ((View) (((View) view.getParent())).getParent()).getParent()).getParent()).getParent()).getHeight());
                                                        }
                                                    }catch (Exception ex){
                                                        setThisViewHeight(((View) ((View) ((View) (((View) view.getParent())).getParent()).getParent()).getParent()).getHeight());
                                                    }
                                                  if (newProgress >= 100) {

                                                        //sa.resetHeight(thisViewHeight);
                                                  }
                                              }
                                          }
                );



                String webData = "<html><head></head><body style='margin:0;padding:0;'>"+surveyScreens.getMediaEmbedHTML()+"</body></html>";
                OFHelper.v(tag,"1Flow htmlData after ["+webData+"]");

                webContent.loadDataWithBaseURL(null,webData,"text/html", "UTF-8",null);


        }

    }
    public void transitActive() {
        try {
            int colorFrom = OFHelper.manipulateColorNew(Color.parseColor(sa.themeColor), OFConstants.buttonActiveValue);//getResources().getColor(R.color.ratings_focused);
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
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void transitInActive() {
        try {
            int colorFrom = Color.parseColor(sa.themeColor);
            int colorTo = OFHelper.manipulateColorNew(Color.parseColor(sa.themeColor), OFConstants.buttonActiveValue);//getResources().getColor(R.color.ratings_focused);
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
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void handleWaterMarkStyle(OFSDKSettingsTheme theme) {

        try {
            if (sa instanceof OFSurveyActivityFullScreen) {
                waterMarkLayout.setVisibility(View.GONE);
            } else {
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
        } catch (Exception ex) {
            OFHelper.e("BaseFragment", "1Flow watermark error ");
        }
    }
}
