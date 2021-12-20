package com.oneflow.analytics.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.oneflow.analytics.fragment.OFSurveyQueFragment;
import com.oneflow.analytics.fragment.OFSurveyQueTextFragment;
import com.oneflow.analytics.fragment.OFSurveyQueThankyouFragment;
import com.oneflow.analytics.model.survey.OFSurveyScreens;
import com.oneflow.analytics.utils.OFHelper;

import java.util.ArrayList;

public class OFSurveyAdapter extends FragmentStatePagerAdapter {
    ArrayList<OFSurveyScreens> data;
    String tag = this.getClass().getName();
    public OFSurveyAdapter(FragmentManager manager, ArrayList<OFSurveyScreens> data){
        super(manager);
        this.data = data;
    }



    @Override
    public Fragment getItem(int i) {
        try {
            if (data.get(i).getInput().getInput_type().equalsIgnoreCase("thank_you")) {
                return OFSurveyQueThankyouFragment.newInstance(data.get(i));
            } else if (data.get(i).getInput().getInput_type().equalsIgnoreCase("text")) {
                return OFSurveyQueTextFragment.newInstance(data.get(i));
            } else {
                return OFSurveyQueFragment.newInstance(data.get(i));
            }
        }catch(Exception ex){
            OFHelper.e(tag,"OneFlow i["+i+"]ERROR ["+ex.getMessage()+"]");
            return OFSurveyQueThankyouFragment.newInstance(data.get(i));
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }
}
