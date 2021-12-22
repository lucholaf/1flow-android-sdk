package com.oneflow.analytics.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import androidx.core.graphics.ColorUtils;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.CompoundButtonCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.oneflow.analytics.OneFlow;
import com.oneflow.analytics.R;
import com.oneflow.analytics.customwidgets.OFCustomTextView;
import com.oneflow.analytics.customwidgets.OFDynamicSquareLayout;
import com.oneflow.analytics.model.survey.OFSurveyInputs;
import com.oneflow.analytics.utils.OFHelper;

public class OFSurveyOptionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    View.OnClickListener onClickListener;
    private int listSize = 0;
    private int strokeWidth = 4;
    private int viewType = -1;
    private OFSurveyInputs surveyInputs;
    String tag = this.getClass().getName();
    private String themeColor;
    private LayoutInflater mInflater;
    private Integer[] emojis = new Integer[]{R.drawable.smiley1, R.drawable.smiley2, R.drawable.smiley3, R.drawable.smiley4, R.drawable.smiley5};

    public class RatingsViewHolder extends RecyclerView.ViewHolder {
        public OFCustomTextView title;

        public RatingsViewHolder(View view) {
            super(view);
            title = (OFCustomTextView) view.findViewById(R.id.ratings_list_child_tv);
        }
    }

    public class TextViewHolder extends RecyclerView.ViewHolder {
        public OFCustomTextView title;
        RelativeLayout innerView;
        View viewLocal;
        OFCustomTextView txtFestivalName;
        RelativeLayout rlDate;


        public TextViewHolder(View view) {
            super(view);

            title = (OFCustomTextView) view.findViewById(R.id.child_title);

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

    public OFSurveyOptionsAdapter(Context mContext, OFSurveyInputs surveyInputs, View.OnClickListener onClickListener, String themeColor) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.onClickListener = onClickListener;
        this.surveyInputs = surveyInputs;
        this.themeColor = themeColor;
        OFHelper.v(tag, "OneFlow theme color ["+themeColor+"]input type [" + surveyInputs.getInput_type() + "]childSize[" + new Gson().toJson(surveyInputs.getRatingsList()) + "]");
        if (surveyInputs.getInput_type().equalsIgnoreCase("rating-emojis")) {
            viewType = 4;
            listSize = surveyInputs.getRatingsList().size();
        }
        else if (surveyInputs.getInput_type().equalsIgnoreCase("rating") || surveyInputs.getInput_type().equalsIgnoreCase("rating-5-star")) {
            viewType = 5;
            listSize = surveyInputs.getRatingsList().size();
        } else if (surveyInputs.getInput_type().equalsIgnoreCase("nps") || surveyInputs.getInput_type().equalsIgnoreCase("rating-numerical")) {
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
       /* if (surveyInputs.getInput_type().contains("rating")) {
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
        }*/
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        OFHelper.v(tag, "OneFlow createViewHolder called viewtype [" + viewType + "]");
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
        OFHelper.v(tag, "OneFlow viewtype [" + viewType + "]");
        int colorAlpha = ColorUtils.setAlphaComponent(Color.parseColor(themeColor), 125);
        int statesRadio[][] = {{android.R.attr.state_checked}, {}};
        int colorsRadio[] = {Color.parseColor(themeColor),mContext.getResources().getColor(R.color.ratings_focused)};
        switch (viewType) {
            case 0:
                OFHelper.v(tag, "OneFlow position[" + position + "]value [" + surveyInputs.getRatingsList().get(position).getId() + "]isSelected[" + surveyInputs.getRatingsList().get(position).getSelected() + "]");
                ((RatingsViewHolder) holder).title.setText(surveyInputs.getRatingsList().get(position).getId() + "");
                ((RatingsViewHolder) holder).title.setTag(surveyInputs.getRatingsList().get(position).getId());

                if (position == 0) {

                        ((RelativeLayout) (((RatingsViewHolder) holder).title).getParent()).setBackground(mContext.getResources().getDrawable(R.drawable.left_gray_rectangle));
                        ((RatingsViewHolder) holder).title.setTextColor(mContext.getResources().getColor(R.color.txtblack));

                } else if (position == surveyInputs.getRatingsList().size() - 1) {

                        ((RelativeLayout) (((RatingsViewHolder) holder).title).getParent()).setBackground(mContext.getResources().getDrawable(R.drawable.right_gray_rectangle));
                        ((RatingsViewHolder) holder).title.setTextColor(mContext.getResources().getColor(R.color.txtblack));

                } else {

                        ((RelativeLayout) (((RatingsViewHolder) holder).title).getParent()).setBackground(mContext.getResources().getDrawable(R.drawable.gray_rectangle));
                        ((RatingsViewHolder) holder).title.setTextColor(mContext.getResources().getColor(R.color.txtblack));

                }
                GradientDrawable gd = (GradientDrawable) ((RelativeLayout) (((RatingsViewHolder) holder).title).getParent()).getBackground();

                ((RatingsViewHolder) holder).title.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        switch (event.getAction()) {

                            case MotionEvent.ACTION_DOWN:
                                gd.setColor(colorAlpha);
                                break;

                            case MotionEvent.ACTION_MOVE:
                                // touch move code
                                //Helper.makeText(mContext,"Moved",1);
                                break;

                            case MotionEvent.ACTION_UP:

                                    gd.setColor(Color.parseColor(themeColor));
                                    ((RatingsViewHolder) holder).title.setTextColor(mContext.getResources().getColor(R.color.txtwhite));

                                break;
                        }
                        return false;
                    }
                });
                if (surveyInputs.getRatingsList().get(position).getSelected()) {
                    gd.setColor(Color.parseColor(themeColor));
                    ((RatingsViewHolder) holder).title.setTextColor(mContext.getResources().getColor(R.color.txtwhite));
                }else{
                    gd.setColor(mContext.getResources().getColor(R.color.white));
                    ((RatingsViewHolder) holder).title.setTextColor(mContext.getResources().getColor(R.color.txtblack));
                }

                ((RatingsViewHolder) holder).title.setOnClickListener(onClickListener);
                break;
            case 1:
                OFHelper.v(tag, "OneFlow title [" + surveyInputs.getChoices().get(position).getTitle() + "]tag[" + surveyInputs.getChoices().get(position).getId() + "]");

                if(OneFlow.optionsFace!=null) {
                    if(OneFlow.optionsFace.getTypeface()!=null) {
                        ((MCQRadioViewHolder) holder).title.setTypeface(OneFlow.optionsFace.getTypeface());
                    }
                    if(OneFlow.optionsFace.getFontSize()!=null) {
                        OFHelper.v(tag,"OneFlow changing font size");
                        ((MCQRadioViewHolder) holder).title.setTextSize(OneFlow.optionsFace.getFontSize());
                    }
                }
                ((MCQRadioViewHolder) holder).title.setText(surveyInputs.getChoices().get(position).getTitle());
                ((MCQRadioViewHolder) holder).title.setTag(surveyInputs.getChoices().get(position).getId() == null ? String.valueOf(position) : surveyInputs.getChoices().get(position).getId());
                ((MCQRadioViewHolder) holder).title.setOnClickListener(onClickListener);

                GradientDrawable gdRadio = (GradientDrawable) ((RelativeLayout) (((MCQRadioViewHolder) holder).title).getParent()).getBackground();
                gdRadio.setStroke(strokeWidth,mContext.getResources().getColor(R.color.ratings_focused));

                ((MCQRadioViewHolder) holder).title.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        RadioButton rb = (RadioButton) v;
                        switch (event.getAction()) {

                            case MotionEvent.ACTION_DOWN:

                                int colorsRadioAlpha[] = {Color.parseColor(themeColor),colorAlpha};
                                CompoundButtonCompat.setButtonTintList(((MCQRadioViewHolder) holder).title,  new ColorStateList(statesRadio, colorsRadioAlpha));
                                gdRadio.setStroke(strokeWidth,colorAlpha);
                                break;

                            case MotionEvent.ACTION_MOVE:
                                // touch move code
                                //Helper.makeText(mContext,"Moved",1);
                                break;

                            case MotionEvent.ACTION_UP:
                                // touch up code
                               CompoundButtonCompat.setButtonTintList(((MCQRadioViewHolder) holder).title,  new ColorStateList(statesRadio, colorsRadio));//ColorStateList.valueOf(Color.parseColor(themeColor)));
                                if (!rb.isChecked()) {

                                   gdRadio.setStroke(strokeWidth,Color.parseColor(themeColor));
                                } else {

                                    gdRadio.setStroke(strokeWidth,mContext.getResources().getColor(R.color.ratings_focused));
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
                OFHelper.v(tag, "OneFlow title [" + surveyInputs.getChoices().get(position).getTitle() + "]tag[" + surveyInputs.getChoices().get(position).getId() + "]");
                if(OneFlow.optionsFace!=null) {
                    if(OneFlow.optionsFace.getTypeface()!=null) {
                        ((MCQCheckBoxViewHolder) holder).title.setTypeface(OneFlow.optionsFace.getTypeface());
                    }
                    if(OneFlow.optionsFace.getFontSize()!=null) {
                        ((MCQCheckBoxViewHolder) holder).title.setTextSize(OneFlow.optionsFace.getFontSize());
                    }
                }
                ((MCQCheckBoxViewHolder) holder).title.setText(surveyInputs.getChoices().get(position).getTitle());
                ((MCQCheckBoxViewHolder) holder).title.setTag(surveyInputs.getChoices().get(position).getId() == null ? String.valueOf(position) : surveyInputs.getChoices().get(position).getId());
                ((MCQCheckBoxViewHolder) holder).title.setOnClickListener(onClickListener);
                GradientDrawable gdCheckbox = (GradientDrawable) ((RelativeLayout) (((MCQCheckBoxViewHolder) holder).title).getParent()).getBackground();
                gdCheckbox.setStroke(strokeWidth,mContext.getResources().getColor(R.color.ratings_focused));
               ((MCQCheckBoxViewHolder) holder).title.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        CheckBox cb = (CheckBox) v;
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                // touch down code

                                int colorsRadioAlpha[] = {Color.parseColor(themeColor),colorAlpha};
                                CompoundButtonCompat.setButtonTintList(((MCQCheckBoxViewHolder) holder).title,  new ColorStateList(statesRadio, colorsRadioAlpha));
                                gdCheckbox.setStroke(strokeWidth,colorAlpha);
                                break;

                            case MotionEvent.ACTION_MOVE:
                                // touch move code
                                //Helper.makeText(mContext,"Moved",1);
                                break;

                            case MotionEvent.ACTION_UP:
                                // touch up code
                                //Helper.makeText(mContext, "Released", 1);
                                CompoundButtonCompat.setButtonTintList(((MCQCheckBoxViewHolder) holder).title,  new ColorStateList(statesRadio, colorsRadio));//ColorStateList.valueOf(Color.parseColor(themeColor)));
                                if (!cb.isChecked()) {
                                        gdCheckbox.setStroke(strokeWidth,Color.parseColor(themeColor));
                                } else {

                                    gdCheckbox.setStroke(strokeWidth,mContext.getResources().getColor(R.color.ratings_focused));
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

                        ((OFDynamicSquareLayout) ((((RatingsEmojis) holder).emojis).getParent()).getParent()).setBackground(mContext.getResources().getDrawable(R.drawable.left_gray_rectangle));

                } else if (position == surveyInputs.getRatingsList().size() - 1) {

                        ((OFDynamicSquareLayout) ((((RatingsEmojis) holder).emojis).getParent()).getParent()).setBackground(mContext.getResources().getDrawable(R.drawable.right_gray_rectangle));

                } else {

                        ((OFDynamicSquareLayout) ((((RatingsEmojis) holder).emojis).getParent()).getParent()).setBackgroundResource(R.drawable.gray_rectangle);

                }
                GradientDrawable gdEmojis = (GradientDrawable) ((OFDynamicSquareLayout) ((((RatingsEmojis) holder).emojis).getParent()).getParent()).getBackground();


                ((RatingsEmojis) holder).emojis.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                //Helper.makeText(mContext,"Down called",0);
                                gdEmojis.setColor(colorAlpha);
                                break;

                            case MotionEvent.ACTION_MOVE:
                                // touch move code
                                //Helper.makeText(mContext,"Moved",1);
                                break;

                            case MotionEvent.ACTION_UP:
                                gdEmojis.setColor(Color.parseColor(themeColor));
                                break;
                        }
                        return false;
                    }
                });

                if (surveyInputs.getRatingsList().get(position).getSelected()) {
                    gdEmojis.setColor(Color.parseColor(themeColor));
                }else{
                    gdEmojis.setColor(mContext.getResources().getColor(R.color.white));
                }
                ((RatingsEmojis) holder).emojis.setTag(position);
                ((RatingsEmojis) holder).emojis.setOnClickListener(onClickListener);
                break;
            case 5:
                OFHelper.v(tag, "OneFlow binding views");

                if (surveyInputs.getRatingsList().get(position).getSelected()) {
                    ((RatingsStar) holder).stars.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_star_92));
                    DrawableCompat.setTint(((RatingsStar) holder).stars.getDrawable(), Color.parseColor(themeColor));
                } else {
                    ((RatingsStar) holder).stars.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_star_outline_92));
                    DrawableCompat.setTint(((RatingsStar) holder).stars.getDrawable(), Color.parseColor(themeColor));
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

    public void notifyMyList(OFSurveyInputs surveyInputs) {
        this.surveyInputs = null;
        this.surveyInputs = surveyInputs;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {

        return viewType;
    }
}
