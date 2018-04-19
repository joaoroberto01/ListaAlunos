package com.jmp.listaalunos.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomListAdapter extends BaseAdapter {

    private Context context;

    private List<String> labelList = new ArrayList<>();
    private List<String> subLabelList = new ArrayList<>();
    private List<String> thirdLabelList = new ArrayList<>();

    private int[] labelPadding = {16, 16, 16, 16};
    private int[] subLabelPadding = {16, 0, 16, 16};
    private int[] containerPadding = {0, 0, 0, 0};

    private int textSize = 17, subTextSize = 16;
    private boolean isBold,isCentered;

    public CustomListAdapter(Context context, List<String> labelList){
        this.context = context;
        this.labelList = labelList;
    }

    public CustomListAdapter(Context context, List<String> labelList, List<String> subLabelList){
        this.context = context;
        this.labelList = labelList;
        this.subLabelList = subLabelList;
    }

    public void setTextSize(int textSize){
        this.textSize = textSize;
    }

    public void setSubTextSize(int subTextSize) {
        this.subTextSize = subTextSize;
    }

    public void setTextCentered(){
        this.isCentered = true;
    }

    public void setContainerPadding(int[] containerPadding){
        this.containerPadding = containerPadding;
    }

    public void setSubLabelPadding(int[] subLabelPadding){
        this.subLabelPadding = subLabelPadding;
    }

    public void setTextBold(){
        this.isBold = true;
    }

    public void setThirdLabel(List<String> thirdLabelList){
        this.thirdLabelList = thirdLabelList;
    }

    private TextView createLabel(String text, int[] padding,int textSize, int textColor){
        TextView label = new TextView(context);
        label.setPadding(padding[0],padding[1],padding[2],padding[3]);
        label.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
        if(textColor != 0)
            label.setTextColor(textColor);
        if(isBold)
            label.setTypeface(Typeface.SANS_SERIF,Typeface.BOLD);
        else
            label.setTypeface(Typeface.SANS_SERIF);
        label.setText(text);

        return label;
    }

    @Override
    public int getCount() {
        return labelList.size();
    }

    @Override
    public Object getItem(int i) {
        return labelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LinearLayout container = new LinearLayout(context);

        LinearLayout firstLine = new LinearLayout(context);
        firstLine.setOrientation(LinearLayout.HORIZONTAL);
        if(isCentered)
            firstLine.setGravity(Gravity.CENTER);

        TextView listTextView = createLabel(labelList.get(i),labelPadding,textSize,Color.BLACK);
        firstLine.addView(listTextView);

        container.setOrientation(LinearLayout.VERTICAL);
        container.addView(firstLine);

        RelativeLayout secondLine = new RelativeLayout(context);

        if(subLabelList.size() > 0){
            RelativeLayout.LayoutParams params = new
                    RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

            TextView subListTextView = createLabel(subLabelList.get(i),subLabelPadding,subTextSize,0);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            subListTextView.setLayoutParams(params);
            secondLine.setGravity(Gravity.CENTER);
            secondLine.addView(subListTextView);
        }

        if(thirdLabelList.size() > 0){
            RelativeLayout.LayoutParams params = new
                    RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView thirdListTextView = createLabel(thirdLabelList.get(i),subLabelPadding,subTextSize,Color.BLACK);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            thirdListTextView.setLayoutParams(params);
            secondLine.addView(thirdListTextView);
        }

        container.setPadding(containerPadding[0],containerPadding[1],containerPadding[2],containerPadding[3]);
        container.addView(secondLine);

        return container;
    }
}
