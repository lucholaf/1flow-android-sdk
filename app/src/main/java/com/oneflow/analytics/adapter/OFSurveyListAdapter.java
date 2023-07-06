
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

package com.oneflow.analytics.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.oneflow.analytics.R;
import com.oneflow.analytics.customwidgets.OFCustomTextView;
import com.oneflow.analytics.model.survey.OFGetSurveyListResponse;
import com.oneflow.analytics.utils.OFHelper;

import java.util.ArrayList;

/**
 * Created by tp00026816 on 3/5/2019.
 */

public class OFSurveyListAdapter extends RecyclerView.Adapter<OFSurveyListAdapter.MyViewHolder> {
    private ArrayList<OFGetSurveyListResponse> itemsList;
    private Context mContext;
    private View.OnClickListener gch;
    private int lastPosition = -1;

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        //OFHelper.v(this.getClass().getName(), "OneFlow adapter postion[" + position + "]");

        String triggerName = itemsList.get(position).getTrigger_event_name();
        if(OFHelper.validateString(triggerName).equalsIgnoreCase("NA")) {
            OFHelper.v(this.getClass().getName(), "OneFlow adapter postion[" + new Gson().toJson(itemsList.get(position).getSurveySettings().getTriggerFilters()) + "]");
            if(itemsList.get(position).getSurveySettings().getTriggerFilters()!=null && itemsList.get(position).getSurveySettings().getTriggerFilters().size()>0) {
                triggerName = itemsList.get(position).getSurveySettings().getTriggerFilters().get(0).getField();
            }
        }
        holder.txtSurveyKey.setText(triggerName + " (" + itemsList.get(position).getName() + ")");
        holder.txtSurveyKey.setTag(itemsList.get(position).get_id());
        holder.txtSurveyKey.setOnClickListener(gch);

        if (itemsList.get(position).getScreens() != null) {
            holder.txtSurveyData.setText("" + (itemsList.get(position).getScreens().size()));
        }


        // setAnimation(holder.itemView,position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        OFCustomTextView txtSurveyKey, txtSurveyData;

        public MyViewHolder(View view) {
            super(view);
            txtSurveyKey = view.findViewById(R.id.survey_trigger_name);
            txtSurveyData = view.findViewById(R.id.survey_trigger_data);
        }

    }

    public OFSurveyListAdapter(Context mContext, ArrayList<OFGetSurveyListResponse> arrayList, View.OnClickListener gch) {
        this.mContext = mContext;
        this.itemsList = arrayList;
        this.gch = gch;
        OFHelper.v(this.getClass().getName(), "OneFlow size[" + itemsList.size() + "]");
    }

    public void notifyMyList(ArrayList<OFGetSurveyListResponse> arrayList) {
        this.itemsList = arrayList;
        this.notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.survey_list_single_item, parent, false);


        return new MyViewHolder(itemView);
    }


    @Override
    public int getItemCount() {

        return itemsList.size();
    }
}