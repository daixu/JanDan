package com.daixu.jandan.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import timber.log.Timber;

public class MaxImageView extends AppCompatImageView {

    private float mHeight = 0;

    public MaxImageView(Context context) {
        super(context);
    }

    public MaxImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MaxImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int withSpec = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpec = MeasureSpec.getSize(heightMeasureSpec);
        Timber.tag("onMeasure").e("设置 宽： " + withSpec + "高： " + heightSpec);
        Timber.tag("onMeasure").e(getMeasuredWidth() + "  " + getWidth());
        if (heightSpec == 0) {
            setMeasuredDimension(withSpec, 760);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        if (mHeight != 0) {
            int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
//            int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);   //当前高
            Timber.tag("onMeasure").d("设置宽高： " + sizeWidth + "   " + (int) mHeight);
            setMeasuredDimension(sizeWidth, (int) mHeight);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
