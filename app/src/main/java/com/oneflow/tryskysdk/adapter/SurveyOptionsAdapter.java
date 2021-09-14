package com.oneflow.tryskysdk.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.oneflow.tryskysdk.R;
import com.oneflow.tryskysdk.customwidgets.CustomTextView;
import com.oneflow.tryskysdk.model.survey.SurveyInputs;
import com.oneflow.tryskysdk.utils.Helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SurveyOptionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private int listSize = 0;
    private int viewType = -1;
    private SurveyInputs surveyInputs;
    private Calendar calendar;
    String tag = this.getClass().getName();
    private String cDateFromFragment;
    private LayoutInflater mInflater;

    public class RatingsViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView title;

        public RatingsViewHolder(View view) {
            super(view);
            title = (CustomTextView) view.findViewById(R.id.ratings_list_child_tv);
        }
    }

    public class TextViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView title;
        RelativeLayout innerView;
        View viewLocal;
        CustomTextView txtFestivalName;
        RelativeLayout rlDate;


        public TextViewHolder(View view) {
            super(view);

            title = (CustomTextView) view.findViewById(R.id.child_title);

        }
    }

    public class MCWRadioViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView title;

        public MCWRadioViewHolder(View view) {
            super(view);

            title = (CustomTextView) view.findViewById(R.id.child_title);

        }
    }

    public class MCWCheckBoxViewHolder extends RecyclerView.ViewHolder {
        public CheckBox title;
        RelativeLayout innerView;
        View viewLocal;
        CustomTextView txtFestivalName;
        RelativeLayout rlDate;


        public MCWCheckBoxViewHolder(View view) {
            super(view);
            title = (CheckBox) view.findViewById(R.id.child_title);

        }
    }

    public SurveyOptionsAdapter(Context mContext, SurveyInputs surveyInputs) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;

        this.surveyInputs = surveyInputs;

        Helper.v(tag,"OneFlow input type ["+surveyInputs.getInput_type()+"]");
        if (surveyInputs.getInput_type().equalsIgnoreCase("rating")) {
            listSize = surveyInputs.getMax_val()+1;
            viewType = 0;
        } else if (surveyInputs.getInput_type().equalsIgnoreCase("mcq")) {
            listSize = surveyInputs.getChoices().size();
            viewType = 1;
        } else if (surveyInputs.getInput_type().equalsIgnoreCase("text")) {
            listSize = surveyInputs.getChoices().size();
            viewType = 2;
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        Helper.v(tag,"OneFlow createViewHolder called viewtype ["+viewType+"]");
        switch (viewType) {
            case 0:
                view = mInflater.inflate(R.layout.ratings_list_child, parent, false);
                return new RatingsViewHolder(view);
            case 1:
                view = mInflater.inflate(R.layout.mcq_list_child, parent, false);
                return new MCWCheckBoxViewHolder(view);
            case 2:
                view = mInflater.inflate(R.layout.text_list_child, parent, false);
                return new TextViewHolder(view);

            case 3:
                /*view = mInflater.inflate(R.layout.approval_inner_push_row_item_new, parent, false);
                return new PushViewHolder(view);*/

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Helper.v(tag,"OneFlow viewtype ["+viewType+"]");
        switch (viewType) {
            case 0:
                ((RatingsViewHolder) holder).title.setText(String.valueOf(position));
                break;
            case 1:
                ((MCWCheckBoxViewHolder) holder).title.setText(surveyInputs.getChoices().get(position).getTitle());
                ((MCWCheckBoxViewHolder) holder).title.setTag(surveyInputs.getChoices().get(position).getId());
                break;
            case 2:
                ((TextViewHolder) holder).title.setText(String.valueOf(position));
                break;

        }

    }

    @Override
    public int getItemCount() {
//        Helper.v(tag, "OneAxis attendance Calender adapter getItemCount() :::: " + String.valueOf(calendar.getActualMaximum(Calendar.DAY_OF_MONTH) + startPosition));
        return listSize;
    }

    @Override
    public int getItemViewType(int position) {

        return viewType;
    }
}
