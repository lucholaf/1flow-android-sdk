package com.oneflow.analytics.customwidgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by Mohini on 20-07-2016.
 */
public class OFCustomTextView extends AppCompatTextView {

    public OFCustomTextView(Context context) {
        super(context);
        init();
    }

    public OFCustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OFCustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init()
    {
        /*Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),"fonts/Lato-Regular.ttf");
        setTypeface(typeface);*/
    }
}
