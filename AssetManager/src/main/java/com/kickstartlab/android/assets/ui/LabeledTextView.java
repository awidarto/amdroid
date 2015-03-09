package com.kickstartlab.android.assets.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kickstartlab.android.assets.R;

/**
 * Created by awidarto on 2/10/15.
 */
public class LabeledTextView extends LinearLayout {
    private String label = "";
    private String body = "";
    private TextView labelView, bodyView;

    public LabeledTextView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.labeled_text_view,this);

        labelView = (TextView) findViewById(R.id.label);
        bodyView = (TextView) findViewById(R.id.body);
    }

    public LabeledTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
    }

    public LabeledTextView(Context context, AttributeSet attrs, int defStyleAttr) {
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

    public void setLabel(String label){
        this.label = label;
        if(labelView != null){
            labelView.setText(label);
        }
    }

    public void setBody(String body){
        this.body = body;
        if(bodyView != null){
            bodyView.setText(body);
        }
    }

}
