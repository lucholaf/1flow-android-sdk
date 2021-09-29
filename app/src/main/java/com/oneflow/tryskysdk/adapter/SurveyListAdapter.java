package com.oneflow.tryskysdk.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import com.oneflow.tryskysdk.R;
import com.oneflow.tryskysdk.customwidgets.CustomTextView;
import com.oneflow.tryskysdk.model.survey.GetSurveyListResponse;
import com.oneflow.tryskysdk.utils.Helper;

import java.util.ArrayList;

/**
 * Created by tp00026816 on 3/5/2019.
 */

public class SurveyListAdapter extends RecyclerView.Adapter<SurveyListAdapter.MyViewHolder> {
    private ArrayList<GetSurveyListResponse> itemsList;
    private Context mContext;
    private View.OnClickListener gch;
    private int lastPosition = -1;
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Helper.v(this.getClass().getName(),"OneAxis adapter postion["+position+"]");

        holder.txtSurveyKey.setText(itemsList.get(position).getTrigger_event_name());
        holder.txtSurveyKey.setTag(itemsList.get(position).getTrigger_event_name());
        holder.txtSurveyKey.setOnClickListener(gch);

        if(itemsList.get(position).getScreens()!=null) {
            holder.txtSurveyData.setText(""+itemsList.get(position).getScreens().size());
        }


       // setAnimation(holder.itemView,position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CustomTextView txtSurveyKey,txtSurveyData;

        public MyViewHolder(View view) {
            super(view);
            txtSurveyKey = view.findViewById(R.id.survey_trigger_name);
            txtSurveyData = view.findViewById(R.id.survey_trigger_data);
        }

    }

    public SurveyListAdapter(Context mContext, ArrayList<GetSurveyListResponse> arrayList, View.OnClickListener gch) {
        this.mContext = mContext;
        this.itemsList = arrayList;
        this.gch = gch;
        Helper.v(this.getClass().getName(), "OneAxis size[" + itemsList.size() + "]");
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