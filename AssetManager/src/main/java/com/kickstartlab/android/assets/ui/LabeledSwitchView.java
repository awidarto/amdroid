package com.kickstartlab.android.assets.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kickstartlab.android.assets.R;

/**
 * Created by awidarto on 2/10/15.
 */
public class LabeledSwitchView extends LinearLayout {
    private String label = "";
    private String switchval = "";
    private String positive = getResources().getString(R.string.label_yes);
    private String negative = getResources().getString(R.string.label_no);
    private TextView labelView, bodyView;
    private SwitchCompat switchView;

    public LabeledSwitchView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.labeled_switch_view,this);

        labelView = (TextView) findViewById(R.id.label);
        bodyView = (TextView) findViewById(R.id.switch_value);
        switchView = (SwitchCompat) findViewById(R.id.switch_view);

        switchView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchCompat sv =  (SwitchCompat) v;
                if(sv.isChecked()){
                    bodyView.setText(positive);
                }else{
                    bodyView.setText(negative);
                }
            }
        });

    }

    public LabeledSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
    }

    public LabeledSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context, attrs);
    }

    private void initViews(Context context, AttributeSet attrs){
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LabeledTextView,0,0);
        try{
            label = a.getString(R.styleable.LabeledTextView_labelText);
        }finally{
            a.recycle();
        }

        LayoutInflater.from(context).inflate(R.layout.labeled_text_view,this);
        labelView = (TextView) findViewById(R.id.label);
        bodyView = (TextView) findViewById(R.id.body);

        labelView.setText(label);
    }

    public void setPositive(String positive) {
        this.positive = positive;
    }

    public void setNegative(String negative) {
        this.negative = negative;
    }

    public void setChecked(boolean checked){
        switchView.setChecked(checked);
        if(switchView.isChecked()){
            bodyView.setText(this.positive);
        }else{
            bodyView.setText(this.negative);
        }
    }

    public boolean isChecked(){
        return this.switchView.isChecked();
    }

    public void setLabel(String label){
        this.label = label;
        if(labelView != null){
            labelView.setText(label);
        }
    }

}
