package com.oneflow.analytics.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.oneflow.analytics.fragment.SurveyQueFragment;
import com.oneflow.analytics.fragment.SurveyQueTextFragment;
import com.oneflow.analytics.fragment.SurveyQueThankyouFragment;
import com.oneflow.analytics.model.survey.SurveyScreens;
import com.oneflow.analytics.utils.Helper;

import java.util.ArrayList;

public class SurveyAdapter extends FragmentStatePagerAdapter {
    ArrayList<SurveyScreens> data;
    String tag = this.getClass().getName();
    public SurveyAdapter(FragmentManager manager, ArrayList<SurveyScreens> data){
        super(manager);
        this.data = data;
    }



    @Override
    public Fragment getItem(int i) {
        try {
            if (data.get(i).getInput().getInput_type().equalsIgnoreCase("thank_you")) {
                return SurveyQueThankyouFragment.newInstance(data.get(i));
            } else if (data.get(i).getInput().getInput_type().equalsIgnoreCase("text")) {
                return SurveyQueTextFragment.newInstance(data.get(i));
            } else {
                return SurveyQueFragment.newInstance(data.get(i));
            }
        }catch(Exception ex){
            Helper.e(tag,"OneFlow i["+i+"]ERROR ["+ex.getMessage()+"]");
            return SurveyQueThankyouFragment.newInstance(data.get(i));
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }
}
