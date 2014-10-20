package com.micnubinub.materiallibrary;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by root on 20/10/14.
 */
public class MaterialTwoLineListItem extends ViewGroup {
    private TextView primaryText, secondaryText;
    private int secondaryTextSize, primaryTextSize,
            primaryTextColor, secondaryTextColor, secondaryTextMaxLines;

    public MaterialTwoLineListItem(Context context) {
        super(context);
        init();
    }

    public MaterialTwoLineListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MaterialTwoLineListItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public static int dpToPixels(int dp, Resources res) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
    }

    private void init() {
        primaryText = new TextView(getContext());


        secondaryText = new TextView(getContext());

    }

    public void setSecondaryTextColor(int color) {
        secondaryTextColor = color;
        secondaryText.setTextColor(color);
    }

    public void setSecondaryTextMaxLines(int maxLines) {
        secondaryText.setMaxLines(maxLines);
    }

    public void setSecondaryTextSize(int sp) {
        secondaryText.setTextSize(sp);
    }

    public void setPrimaryTextSize(int sp) {
        primaryText.setTextSize(sp);
    }


    public void setPrimaryTextColor(int color) {
        primaryText.setTextColor(color);
    }

    @Override
    protected void onLayout(boolean b, int i, int i2, int i3, int i4) {
        getChildAt(0).layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        try {
            final View child = getChildAt(0);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            final int measuredHeight = child.getMeasuredHeight();
            final int measuredWidth = child.getMeasuredWidth();
            setMeasuredDimension(resolveSizeAndState(measuredWidth, widthMeasureSpec, 0),
                    resolveSizeAndState(measuredHeight, heightMeasureSpec, 0));
        } catch (Exception e) {
        }

    }
}
