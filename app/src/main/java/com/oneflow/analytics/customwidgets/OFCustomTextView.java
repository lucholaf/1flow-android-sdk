/*
 *  Copyright 2021 1Flow, Inc.
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.oneflow.analytics.customwidgets;

import android.content.Context;
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
