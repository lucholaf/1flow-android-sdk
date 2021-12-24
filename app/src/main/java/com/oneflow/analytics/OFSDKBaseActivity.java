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

package com.oneflow.analytics;

import androidx.appcompat.app.AppCompatActivity;

import com.oneflow.analytics.model.survey.OFGetSurveyListResponse;
import com.oneflow.analytics.sdkdb.OFOneFlowSHP;
import com.oneflow.analytics.utils.OFHelper;

import java.util.ArrayList;

public class OFSDKBaseActivity extends AppCompatActivity {
    /**
     * This method will check if trigger name is available in the list or not
     *
     * @param type
     * @return
     */
    //private ArrayList<SurveyScreens> checkSurveyTitleAndScreens(String type){
    public OFGetSurveyListResponse checkSurveyTitleAndScreens(String type) {
        ArrayList<OFGetSurveyListResponse> slr = new OFOneFlowSHP(this).getSurveyList();
        OFGetSurveyListResponse gslr = null;
        //ArrayList<SurveyScreens> surveyScreens = null;
        int counter = 0;
        String tag = this.getClass().getName();
        OFHelper.v(tag, "OneFlow list size[" + slr.size() + "]type[" + type + "]");
        for (OFGetSurveyListResponse item : slr) {
            if (item.getTrigger_event_name().equalsIgnoreCase(type)) {
                gslr = item;
                /*surveyScreens = item.getScreens();
                selectedSurveyId = item.get_id();
                themeColor = item.getThemeColor();*/
                // Helper.v(tag,"OneFlow survey found at ["+(counter++)+"]triggerName["+item.getTrigger_event_name()+"]queSize["+item.getScreens().size()+"]");
                // Helper.v(tag,"OneFlow survey queSize["+new Gson().toJson(item.getScreens())+"]");
                /*int i=0;
                while(i<item.getScreens().size()) {
                    try {
                        if(item.getScreens().get(i).getInput()!=null) {
                            Helper.v(tag, "OneFlow input type["+i+"][" + item.getScreens().get(i).getInput().getInput_type() + "]");
                        }else{
                            Helper.v(tag,"OneFlow found null");
                        }
                    }catch(Exception ex){

                    }
                i++;
                }*/
                break;
            }
        }
        return gslr;
    }
}
