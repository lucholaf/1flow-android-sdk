package com.oneflow.tryskysdk;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Display;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.CompoundButtonCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.oneflow.tryskysdk.adapter.SurveyOptionsAdapter;
import com.oneflow.tryskysdk.fragment.SurveyQueFragment;
import com.oneflow.tryskysdk.fragment.SurveyQueTextFragment;
import com.oneflow.tryskysdk.fragment.SurveyQueThankyouFragment;
import com.oneflow.tryskysdk.model.survey.SurveyUserResponse;
import com.oneflow.tryskysdk.model.survey.SurveyUserResponseChild;
import com.oneflow.tryskysdk.model.survey.GetSurveyListResponse;
import com.oneflow.tryskysdk.model.survey.SurveyScreens;
import com.oneflow.tryskysdk.repositories.Survey;
import com.oneflow.tryskysdk.sdkdb.OneFlowSHP;
import com.oneflow.tryskysdk.utils.Constants;
import com.oneflow.tryskysdk.utils.Helper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

//https://stackoverflow.com/questions/27246981/android-floating-activity-with-dismiss-on-swipe
public class SurveyActivity extends AppCompatActivity {

   /* @BindView(R.id.survey_viewpager)
   public CustomViewPager surveyQueViewPager;*/

    @BindView(R.id.pbar)
    ProgressBar pagePositionPBar;

    @BindView(R.id.close_btn_image_view)
    ImageView closeBtn;

    @BindView(R.id.slider)
    View slider;

    @BindView(R.id.slider_layout)
    RelativeLayout sliderLayout;

    @BindView(R.id.base_popup_layout)
    RelativeLayout basePopupLayout;

    @BindView(R.id.fragment_view)
    FrameLayout fragmentView;

    String tag = this.getClass().getName();

    public ArrayList<SurveyUserResponseChild> surveyResponseChildren;
    public ArrayList<SurveyScreens> screens;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.survey_view);

        Window window = this.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.flags &= WindowManager.LayoutParams.FLAG_DIM_BEHIND;

        window.setAttributes(wlp);

        ButterKnife.bind(this);

        //String surveyType = this.getIntent().getStringExtra("SurveyType");
        GetSurveyListResponse surveyItem = (GetSurveyListResponse) this.getIntent().getSerializableExtra("SurveyType");

        screens = surveyItem.getScreens();//checkSurveyTitleAndScreens(surveyType);
        selectedSurveyId = surveyItem.get_id();
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SurveyActivity.this.finish();
               // overridePendingTransition(0,R.anim.slide_down_dialog);
            }
        });


        /*pagePositionPBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(surveyItem.get)));
        pagePositionPBar.setIndeterminateTintList(ColorStateList.valueOf(getResources().getColor(R.color.whitetxt)));*/
        Helper.v(tag,"OneFlow color["+surveyItem.getStyle().getPrimary_color()+"]");

        String styleColor = "";
        try {

            if(!surveyItem.getStyle().getPrimary_color().startsWith("#")){
                styleColor="#"+surveyItem.getStyle().getPrimary_color();
            }else{
                styleColor= surveyItem.getStyle().getPrimary_color();
            }
        }catch(Exception ex){
            styleColor=""+getResources().getColor(R.color.colorPrimaryDark);
        }
        //styleColor=String.valueOf(getResources().getColor(R.color.colorPrimaryDark));
        Helper.v(tag,"OneFlow color after["+styleColor+"]");
        pagePositionPBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(styleColor)));
        //pagePositionPBar.getProgressDrawable().setColorFilter(Color.parseColor(styleColor.toString()), PorterDuff.Mode.DARKEN);

        surveyResponseChildren = new ArrayList<>();
        slider.setOnTouchListener(sliderTouchListener);
        sliderLayout.setOnTouchListener(sliderTouchListener);
        slider.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                Helper.v(tag,"OneFlow getAction["+event.getAction()+"]");
                Helper.v(tag,"OneFlow getX["+event.getX()+"]");

                SurveyActivity.this.finish();
               // overridePendingTransition(0,R.anim.slide_down_dialog);
                return false;
            }
        });
        initFragment();

    }
    private int previousFingerPosition = 0;
    private int baseLayoutPosition = 0;
    private int defaultViewHeight;
    private boolean isClosing = false;
    private boolean isScrollingUp = false;
    private boolean isScrollingDown = false;

    View.OnTouchListener sliderTouchListener =  new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // Get finger position on screen
            final int Y = (int) event.getRawY();

            // Switch on motion event type
            switch (event.getAction() & MotionEvent.ACTION_MASK) {

                case MotionEvent.ACTION_DOWN:
                    // save default base layout height
                    defaultViewHeight = basePopupLayout.getHeight();

                    // Init finger and view position
                    previousFingerPosition = Y;
                    baseLayoutPosition = (int) basePopupLayout.getY();
                    break;

                case MotionEvent.ACTION_UP:
                    // If user was doing a scroll up
                    if(isScrollingUp){
                        // Reset baselayout position
                        basePopupLayout.setY(0);
                        // We are not in scrolling up mode anymore
                        isScrollingUp = false;
                    }

                    // If user was doing a scroll down
                    if(isScrollingDown){
                        // Reset baselayout position
                        basePopupLayout.setY(0);
                        // Reset base layout size
                        basePopupLayout.getLayoutParams().height = defaultViewHeight;
                        basePopupLayout.requestLayout();
                        // We are not in scrolling down mode anymore
                        isScrollingDown = false;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(!isClosing){
                        int currentYPosition = (int) basePopupLayout.getY();

                        // If we scroll up
                        if(previousFingerPosition >Y){
                            // First time android rise an event for "up" move
                            if(!isScrollingUp){
                                isScrollingUp = true;
                            }

                            // Has user scroll down before -> view is smaller than it's default size -> resize it instead of change it position
                            if(basePopupLayout.getHeight()<defaultViewHeight){
                                /*basePopupLayout.getLayoutParams().height = basePopupLayout.getHeight() - (Y - previousFingerPosition);
                                basePopupLayout.requestLayout();*/
                            }
                            else {
                                // Has user scroll enough to "auto close" popup ?
                                if ((baseLayoutPosition - currentYPosition) > defaultViewHeight / 4) {
                                    //closeUpAndDismissDialog(currentYPosition);
                                    return true;
                                }

                                //
                            }
                           // basePopupLayout.setY(basePopupLayout.getY() + (Y - previousFingerPosition));

                        }
                        // If we scroll down
                        else{

                            // First time android rise an event for "down" move
                            if(!isScrollingDown){
                                isScrollingDown = true;
                            }

                            // Has user scroll enough to "auto close" popup ?
                            if (Math.abs(baseLayoutPosition - currentYPosition) > defaultViewHeight / 2)
                            {
                                closeDownAndDismissDialog(currentYPosition);
                                return true;
                            }

                            // Change base layout size and position (must change position because view anchor is top left corner)
                            basePopupLayout.setY(basePopupLayout.getY() + (Y - previousFingerPosition));
                            basePopupLayout.getLayoutParams().height = basePopupLayout.getHeight() - (Y - previousFingerPosition);
                            basePopupLayout.requestLayout();
                        }

                        // Update position
                        previousFingerPosition = Y;
                    }
                    break;
            }
            return true;
        }
    };
    public void closeUpAndDismissDialog(int currentPosition){
        isClosing = true;
        ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(basePopupLayout, "y", currentPosition, -basePopupLayout.getHeight());
        positionAnimator.setDuration(300);
        positionAnimator.addListener(new Animator.AnimatorListener()
        {

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animator)
            {
                finish();
            }

        });
        positionAnimator.start();
    }

    public void closeDownAndDismissDialog(int currentPosition){
        isClosing = true;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenHeight = size.y;
        ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(basePopupLayout, "y", currentPosition, screenHeight+basePopupLayout.getHeight());
        positionAnimator.setDuration(300);
        positionAnimator.addListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationStart(Animator animation) {

            }
            @Override
            public void onAnimationEnd(Animator animator)
            {
                finish();
            }

        });
        positionAnimator.start();
    }
    public void checkIfAnswerAlreadyGiven() {

    }

    @Override
    protected void onPause() {

        Helper.v(tag,"OneFlow onPause called");
        overridePendingTransition(0,R.anim.slide_down_dialog);
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Helper.v(tag,"OneFlow onStop called");
        //overridePendingTransition(0,R.anim.slide_down_dialog);
    }

    /**
     * Record User inputs
     *
     * @param screenID
     * @param answerIndex
     * @param answerValue
     */
    public void addUserResponseToList(String screenID, String answerIndex, String answerValue) {
        SurveyUserResponseChild asrc = new SurveyUserResponseChild();
        asrc.setScreen_id(screenID);
        if (answerValue != null) {
            asrc.setAnswer_value(answerValue);
        } else {
            asrc.setAnswer_index(answerIndex);
        }

        surveyResponseChildren.add(asrc);
        initFragment();
    }

    public String themeColor = "";
    String selectedSurveyId;



    public void prepareAndSubmitUserResposne() {
        OneFlowSHP ofs = new OneFlowSHP(this);
        SurveyUserResponse sur = new SurveyUserResponse();
        sur.setAnswers(surveyResponseChildren);
        sur.setOs(Constants.os);
        sur.setAnalytic_user_id(ofs.getUserDetails().getAnalytic_user_id());
        sur.setSurvey_id(selectedSurveyId);
        sur.setSession_id(ofs.getStringValue(Constants.SESSIONDETAIL_IDSHP));
        //if internet available then send to api else store locally
        if(Helper.isConnected(this)) {

            Survey.submitUserResponse(this, sur);
        }else{
            //TODO Store data in db
            Helper.makeText(this,getString(R.string.no_network),1);
        }
    }

    public int position = 0;

    public void initFragment() {
        Helper.v(tag, "OneFlow position ["+position+"]screensize["+screens.size()+"]selected answers[" + new Gson().toJson(surveyResponseChildren) + "]");
        if (position >= screens.size()) {
            prepareAndSubmitUserResposne();
            SurveyActivity.this.finish();
           // overridePendingTransition(0,R.anim.slide_down_dialog);
            //slideDown();
        } else {
            loadFragments(screens.get(position));
        }

    }
    private void setProgressBarPosition(){

        int progressValue = ((Integer)100/screens.size())*(position+1);
        Helper.v(tag,"OneAxis progressValue["+progressValue+"]position["+position+"]");
        pagePositionPBar.setProgress(progressValue);

    }
    private void loadFragments(SurveyScreens screen) {
        setProgressBarPosition();
        Fragment frag;
        try {
            if (screen.getInput().getInput_type().equalsIgnoreCase("thank_you")) {
                frag = SurveyQueThankyouFragment.newInstance(screen);
            } else if (screen.getInput().getInput_type().equalsIgnoreCase("text")) {
                frag = SurveyQueTextFragment.newInstance(screen);
            } else {
                frag = SurveyQueFragment.newInstance(screen);
            }
        } catch (Exception ex) {
            Helper.e(tag, "OneFlow ERROR [" + ex.getMessage() + "]");
            frag = SurveyQueThankyouFragment.newInstance(screen);
        }


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (position == 0) {
            ft.add(R.id.fragment_view, frag).commit();
        } else {
            ft.replace(R.id.fragment_view, frag).commit();
        }
    }


}
