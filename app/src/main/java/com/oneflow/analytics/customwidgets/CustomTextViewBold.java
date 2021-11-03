package com.oneflow.analytics.customwidgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;


/**
 * Created by Hiral on 06-10-2017.
 */

public class CustomTextViewBold extends AppCompatTextView {
    public CustomTextViewBold(Context context) {
        super(context);
        init();
    }

    public CustomTextViewBold(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextViewBold(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomTextViewBold(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        //super(context, attrs, defStyleAttr, defStyleRes);
        super(context,attrs,defStyleAttr);
        init();
    }

    private void init()
    {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),"fonts/Lato-Bold.ttf");
        setTypeface(typeface);
    }
}
