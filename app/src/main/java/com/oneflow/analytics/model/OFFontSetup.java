package com.oneflow.analytics.model;

import android.graphics.Typeface;

import com.google.gson.annotations.SerializedName;

public class OFFontSetup {

    @SerializedName("typeface")
    Typeface typeface;
    @SerializedName("fontSize")
    Float fontSize;

    public OFFontSetup(Typeface face, Float size){
        this.typeface = face;
        this.fontSize = size;

    }
    public Typeface getTypeface() {
        return typeface;
    }

   /* public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }*/

    public Float getFontSize() {
        return fontSize;
    }

    /*public void setFontSize(Float fontSize) {
        this.fontSize = fontSize;
    }*/
}
