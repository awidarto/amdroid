package com.kickstartlab.android.assets.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.kickstartlab.android.assets.R;

/**
 * TODO: document your custom view class.
 */
public class RelativeHeightImageView extends ImageView {

    float multiplier;

    public RelativeHeightImageView(Context context) {
        super(context);
    }

    public RelativeHeightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RelativeHeightImageView );

        multiplier = a.getFloat(R.styleable.RelativeHeightImageView_height_multiplier, 0.5f);
    }

    public RelativeHeightImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = (int) (getMeasuredWidth() * multiplier);
        setMeasuredDimension(width, height);
    }
}
