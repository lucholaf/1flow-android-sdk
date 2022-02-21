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

package com.oneflow.analytics.controller;

import android.content.Context;

import com.oneflow.analytics.repositories.OFEventDBRepo;
import com.oneflow.analytics.sdkdb.OFSDKDB;
import com.oneflow.analytics.utils.OFConstants;
import com.oneflow.analytics.utils.OFHelper;
import com.oneflow.analytics.utils.OFMyResponseHandler;

import java.util.HashMap;

public class OFEventController implements OFMyResponseHandler {

    Context mContext;
    OFSDKDB sdkdb;
    String tag = this.getClass().getName();

    private static OFEventController ec;

    public static OFEventController getInstance(Context mContext){
        if(ec==null){
            ec = new OFEventController(mContext);
        }
        return ec;
    }
    private OFEventController(Context mContext){
        this.mContext = mContext;
        sdkdb = OFSDKDB.getInstance(mContext);

    }

    public void storeEventsInDB(String eventName, HashMap<String, String> eventValue,int value){

        //String data = new Gson().toJson(eventValue);
        OFHelper.v(tag,"Oneflow records inserted ["+eventName+"]value["+eventValue+"]");
        OFEventDBRepo.insertEvents(mContext,eventName,eventValue,value,this, OFConstants.ApiHitType.insertEventsInDB);

    }

    @Override
    public void onResponseReceived(OFConstants.ApiHitType hitType, Object obj, Long reserve, String reserved) {

        switch(hitType){
            case insertEventsInDB:
                OFHelper.v(tag,"Oneflow records inserted ["+((Integer)obj)+"]");
                break;
        }
    }
}
