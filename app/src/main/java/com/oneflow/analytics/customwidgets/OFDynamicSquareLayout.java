package com.oneflow.analytics.customwidgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class OFDynamicSquareLayout extends RelativeLayout{

    public OFDynamicSquareLayout(Context context) {
        super(context);
    }


    public OFDynamicSquareLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public OFDynamicSquareLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


// here we are returning the width in place of height, so width = height
// you may modify further to create any proportion you like ie. height = 2*width etc

    @Override public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);

        //int size = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
    }


}