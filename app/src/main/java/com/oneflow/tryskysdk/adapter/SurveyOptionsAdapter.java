package com.oneflow.tryskysdk.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import androidx.core.widget.CompoundButtonCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.oneflow.tryskysdk.R;
import com.oneflow.tryskysdk.customwidgets.CustomTextView;
import com.oneflow.tryskysdk.customwidgets.DynamicSquareLayout;
import com.oneflow.tryskysdk.model.survey.SurveyInputs;
import com.oneflow.tryskysdk.utils.Helper;

import java.util.Calendar;

public class SurveyOptionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    View.OnClickListener onClickListener;
    private int listSize = 0;
    private int viewType = -1;
    private SurveyInputs surveyInputs;
    String tag = this.getClass().getName();
    private String themeColor;
    private LayoutInflater mInflater;
    private Integer[] emojis = new Integer[]{R.drawable.smiley1, R.drawable.smiley2, R.drawable.smiley3, R.drawable.smiley4, R.drawable.smiley5};

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

    public class MCQRadioViewHolder extends RecyclerView.ViewHolder {
        public RadioButton title;

        public MCQRadioViewHolder(View view) {
            super(view);

            title = (RadioButton) view.findViewById(R.id.child_title);

        }
    }

    public class MCQCheckBoxViewHolder extends RecyclerView.ViewHolder {
        public CheckBox title;

        public MCQCheckBoxViewHolder(View view) {
            super(view);
            title = (CheckBox) view.findViewById(R.id.child_title);

        }
    }

    public class RatingsStar extends RecyclerView.ViewHolder {
        public ImageView stars;

        public RatingsStar(View view) {
            super(view);
            stars = (ImageView) view.findViewById(R.id.ratings_list_child_image_view);
        }
    }

    public class RatingsEmojis extends RecyclerView.ViewHolder {
        public ImageView emojis;

        public RatingsEmojis(View view) {
            super(view);
            emojis = (ImageView) view.findViewById(R.id.ratings_list_child_tv);

        }
    }

    public SurveyOptionsAdapter(Context mContext, SurveyInputs surveyInputs, View.OnClickListener onClickListener, String themeColor) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.onClickListener = onClickListener;
        this.surveyInputs = surveyInputs;
        this.themeColor = themeColor;
        Helper.v(tag, "OneFlow input type [" + surveyInputs.getInput_type() + "]childSize[" + new Gson().toJson(surveyInputs.getRatingsList()) + "]");
        if (surveyInputs.getInput_type().contains("rating")) {
            if (surveyInputs.getEmoji()) {
                viewType = 4;
            } else if (surveyInputs.getStars()) {
                viewType = 5;
            } else {
                viewType = 0;
            }
            listSize = surveyInputs.getRatingsList().size();
        } else if (surveyInputs.getInput_type().contains("nps")) {
            viewType = 0;
            listSize = surveyInputs.getRatingsList().size();
        } else if (surveyInputs.getInput_type().equalsIgnoreCase("mcq")) {
            listSize = surveyInputs.getChoices().size();
            viewType = 1;
        } else if (surveyInputs.getInput_type().equalsIgnoreCase("checkbox")) {
            listSize = surveyInputs.getChoices().size();
            viewType = 3;
        } else if (surveyInputs.getInput_type().equalsIgnoreCase("text")) {
            listSize = surveyInputs.getChoices().size();
            viewType = 2;
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        Helper.v(tag, "OneFlow createViewHolder called viewtype [" + viewType + "]");
        switch (viewType) {
            case 0:
                //numericals
                view = mInflater.inflate(R.layout.ratings_list_child, parent, false);
                return new RatingsViewHolder(view);
            case 1:
                //Radio
                view = mInflater.inflate(R.layout.mcq_radio_list_child, parent, false);
                return new MCQRadioViewHolder(view);
            case 2:
                //Text
                view = mInflater.inflate(R.layout.text_list_child, parent, false);
                return new TextViewHolder(view);

            case 3:
                //Checkboxs
                view = mInflater.inflate(R.layout.mcq_checkbox_list_child, parent, false);
                return new MCQCheckBoxViewHolder(view);
            case 4:
                //Emojis
                view = mInflater.inflate(R.layout.ratings_emoji_child, parent, false);
                return new RatingsEmojis(view);
            case 5:
                //Stars
                view = mInflater.inflate(R.layout.ratings_star_list_child, parent, false);
                return new RatingsStar(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Helper.v(tag, "OneFlow viewtype [" + viewType + "]");
        switch (viewType) {
            case 0:
                Helper.v(tag, "OneFlow position[" + position + "]value [" + surveyInputs.getRatingsList().get(position).getId() + "]isSelected[" + surveyInputs.getRatingsList().get(position).getSelected() + "]");
                ((RatingsViewHolder) holder).title.setText(surveyInputs.getRatingsList().get(position).getId() + "");
                ((RatingsViewHolder) holder).title.setTag(surveyInputs.getRatingsList().get(position).getId());
                if (position == 0) {
                    if (surveyInputs.getRatingsList().get(position).getSelected()) {
                        ((RelativeLayout) (((RatingsViewHolder) holder).title).getParent()).setBackground(mContext.getResources().getDrawable(R.drawable.left_ratings_pressed_selector));
                        ((RatingsViewHolder) holder).title.setTextColor(mContext.getResources().getColor(R.color.txtwhite));
                    } else {
                        ((RelativeLayout) (((RatingsViewHolder) holder).title).getParent()).setBackground(mContext.getResources().getDrawable(R.drawable.left_gray_rectangle));
                        ((RatingsViewHolder) holder).title.setTextColor(mContext.getResources().getColor(R.color.txtblack));
                    }
                } else if (position == surveyInputs.getRatingsList().size() - 1) {
                    if (surveyInputs.getRatingsList().get(position).getSelected()) {
                        ((RelativeLayout) (((RatingsViewHolder) holder).title).getParent()).setBackground(mContext.getResources().getDrawable(R.drawable.right_ratings_pressed_selector));
                        ((RatingsViewHolder) holder).title.setTextColor(mContext.getResources().getColor(R.color.txtwhite));
                    } else {
                        ((RelativeLayout) (((RatingsViewHolder) holder).title).getParent()).setBackground(mContext.getResources().getDrawable(R.drawable.right_gray_rectangle));
                        ((RatingsViewHolder) holder).title.setTextColor(mContext.getResources().getColor(R.color.txtblack));
                    }
                } else {
                    if (surveyInputs.getRatingsList().get(position).getSelected()) {
                        ((RelativeLayout) (((RatingsViewHolder) holder).title).getParent()).setBackground(mContext.getResources().getDrawable(R.drawable.ratings_pressed_selector));
                        ((RatingsViewHolder) holder).title.setTextColor(mContext.getResources().getColor(R.color.txtwhite));
                    } else {
                        ((RelativeLayout) (((RatingsViewHolder) holder).title).getParent()).setBackground(mContext.getResources().getDrawable(R.drawable.ratings_selected));
                        ((RatingsViewHolder) holder).title.setTextColor(mContext.getResources().getColor(R.color.txtblack));
                    }
                }
                ((RatingsViewHolder) holder).title.setOnClickListener(onClickListener);
                break;
            case 1:
                Helper.v(tag, "OneFlow title [" + surveyInputs.getChoices().get(position).getTitle() + "]tag[" + surveyInputs.getChoices().get(position).getId() + "]");

                ((MCQRadioViewHolder) holder).title.setText(surveyInputs.getChoices().get(position).getTitle());
                ((MCQRadioViewHolder) holder).title.setTag(surveyInputs.getChoices().get(position).getId() == null ? String.valueOf(position) : surveyInputs.getChoices().get(position).getId());
                ((MCQRadioViewHolder) holder).title.setOnClickListener(onClickListener);
                /*int statesRadio[][] = {{android.R.attr.state_checked}, {}};
                // int colors[] = {Color.parseColor(themeColor), R.color.txtlightgray};
                int colorsRadio[] = {R.color.selector_color, R.color.colorPrimaryDark};
                CompoundButtonCompat.setButtonTintList(((MCQRadioViewHolder) holder).title, new ColorStateList(statesRadio, colorsRadio));*/


                ((MCQRadioViewHolder) holder).title.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        RadioButton rb = (RadioButton) v;
                        switch (event.getAction()) {

                            case MotionEvent.ACTION_DOWN:
                                // touch down code
                               // Helper.makeText(mContext, "Pressed", 1);
                                ((View) v.getParent()).setBackgroundResource(R.drawable.rounded_rectangle_with_border_selector);

                                CompoundButtonCompat.setButtonTintList(((MCQRadioViewHolder) holder).title, ColorStateList.valueOf(mContext.getResources().getColor(R.color.selector_color)));
                                break;

                            case MotionEvent.ACTION_MOVE:
                                // touch move code
                                //Helper.makeText(mContext,"Moved",1);
                                break;

                            case MotionEvent.ACTION_UP:
                                // touch up code
                                //Helper.makeText(mContext, "Released", 1);
                                if (!rb.isChecked()) {

                                    CompoundButtonCompat.setButtonTintList(((MCQRadioViewHolder) holder).title, ColorStateList.valueOf(mContext.getResources().getColor(R.color.ratings_selected)));

                                    ((View) rb.getParent()).setBackgroundResource(R.drawable.rounded_rectangle_with_border_blue);

                                } else {

                                    CompoundButtonCompat.setButtonTintList(((MCQRadioViewHolder) holder).title, ColorStateList.valueOf(mContext.getResources().getColor(R.color.ratings_focused)));

                                    ((View) rb.getParent()).setBackgroundResource(R.drawable.rounded_rectangle_with_border_gray);
                                }
                                break;
                        }
                        return false;
                    }
                });
                break;
            case 2:
                ((TextViewHolder) holder).title.setText(String.valueOf(position));
                break;
            case 3:
                Helper.v(tag, "OneFlow title [" + surveyInputs.getChoices().get(position).getTitle() + "]tag[" + surveyInputs.getChoices().get(position).getId() + "]");
                ((MCQCheckBoxViewHolder) holder).title.setText(surveyInputs.getChoices().get(position).getTitle());
                ((MCQCheckBoxViewHolder) holder).title.setTag(surveyInputs.getChoices().get(position).getId() == null ? String.valueOf(position) : surveyInputs.getChoices().get(position).getId());
                ((MCQCheckBoxViewHolder) holder).title.setOnClickListener(onClickListener);

                /*int states[][] = {{android.R.attr.state_checked}, {}};
                // int colors[] = {Color.parseColor(themeColor), R.color.txtlightgray};
                int colors[] = {R.color.selector_color, R.color.colorPrimaryDark};
                CompoundButtonCompat.setButtonTintList(((MCQCheckBoxViewHolder) holder).title, new ColorStateList(states, colors));*/
                ((MCQCheckBoxViewHolder) holder).title.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        CheckBox cb = (CheckBox) v;
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                // touch down code
                                //Helper.makeText(mContext, "Pressed", 1);
                                ((View) v.getParent()).setBackgroundResource(R.drawable.rounded_rectangle_with_border_selector);

                                CompoundButtonCompat.setButtonTintList(((MCQCheckBoxViewHolder) holder).title, ColorStateList.valueOf(mContext.getResources().getColor(R.color.selector_color)));

                                break;

                            case MotionEvent.ACTION_MOVE:
                                // touch move code
                                //Helper.makeText(mContext,"Moved",1);
                                break;

                            case MotionEvent.ACTION_UP:
                                // touch up code
                                //Helper.makeText(mContext, "Released", 1);
                                if (!cb.isChecked()) {

                                    CompoundButtonCompat.setButtonTintList(((MCQCheckBoxViewHolder) holder).title, ColorStateList.valueOf(mContext.getResources().getColor(R.color.ratings_selected)));

                                    ((View) cb.getParent()).setBackgroundResource(R.drawable.rounded_rectangle_with_border_blue);

                                } else {

                                    CompoundButtonCompat.setButtonTintList(((MCQCheckBoxViewHolder) holder).title, ColorStateList.valueOf(mContext.getResources().getColor(R.color.ratings_focused)));

                                    ((View) cb.getParent()).setBackgroundResource(R.drawable.rounded_rectangle_with_border_gray);
                                }
                                break;
                        }
                        return false;
                    }
                });
                break;
            case 4:
                ((RatingsEmojis) holder).emojis.setImageDrawable(mContext.getResources().getDrawable(emojis[position]));
                if (position == 0) {
                    if (surveyInputs.getRatingsList().get(position).getSelected()) {
                        ((DynamicSquareLayout) (((RatingsEmojis) holder).emojis).getParent()).setBackground(mContext.getResources().getDrawable(R.drawable.left_ratings_pressed_selector));
                    } else {
                        ((DynamicSquareLayout) (((RatingsEmojis) holder).emojis).getParent()).setBackground(mContext.getResources().getDrawable(R.drawable.left_gray_rectangle));
                    }
                } else if (position == surveyInputs.getRatingsList().size() - 1) {
                    if (surveyInputs.getRatingsList().get(position).getSelected()) {
                        ((DynamicSquareLayout) (((RatingsEmojis) holder).emojis).getParent()).setBackground(mContext.getResources().getDrawable(R.drawable.right_ratings_pressed_selector));
                    } else {
                        ((DynamicSquareLayout) (((RatingsEmojis) holder).emojis).getParent()).setBackground(mContext.getResources().getDrawable(R.drawable.right_gray_rectangle));
                    }
                } else {
                    if (surveyInputs.getRatingsList().get(position).getSelected()) {
                        ((DynamicSquareLayout) (((RatingsEmojis) holder).emojis).getParent()).setBackgroundResource(R.drawable.ratings_pressed_selector);
                    } else {
                        ((DynamicSquareLayout) (((RatingsEmojis) holder).emojis).getParent()).setBackgroundResource(R.drawable.gray_rectangle);
                    }
                }
                ((RatingsEmojis) holder).emojis.setTag(position);
                ((RatingsEmojis) holder).emojis.setOnClickListener(onClickListener);
                break;
            case 5:
                Helper.v(tag, "OneFlow binding views");
                if (surveyInputs.getRatingsList().get(position).getSelected()) {
                    ((RatingsStar) holder).stars.setImageDrawable(mContext.getResources().getDrawable(R.drawable.selected_star));
                } else {
                    ((RatingsStar) holder).stars.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ratings_star));
                }
                ((RatingsStar) holder).stars.setTag(position);
                ((RatingsStar) holder).stars.setOnClickListener(onClickListener);
                break;

        }

    }

    @Override
    public int getItemCount() {
//        Helper.v(tag, "OneAxis attendance Calender adapter getItemCount() :::: " + String.valueOf(calendar.getActualMaximum(Calendar.DAY_OF_MONTH) + startPosition));
        return listSize;
    }

    public void notifyMyList(SurveyInputs surveyInputs) {
        this.surveyInputs = null;
        this.surveyInputs = surveyInputs;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {

        return viewType;
    }
}
