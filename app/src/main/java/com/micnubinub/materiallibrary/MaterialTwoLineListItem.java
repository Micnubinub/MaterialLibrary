package com.micnubinub.materiallibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by root on 20/10/14.
 */
public class MaterialTwoLineListItem extends ViewGroup {
    private TextView primaryTextView, secondaryTextView;
    private int secondaryTextSize, primaryTextSize,
            primaryTextColor, secondaryTextColor, secondaryTextMaxLines;
    private String primaryText, secondaryText;


    public MaterialTwoLineListItem(Context context) {
        super(context);
        init();
    }

    public MaterialTwoLineListItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        try {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaterialTwoLineListItem, 0, 0);
            setPrimaryTextColor(a.getInteger(R.attr.primaryTextColor, R.color.dark_dark_grey));
            setSecondaryTextColor(a.getInteger(R.attr.secondaryTextColor, R.color.dark_grey));
            setPrimaryTextSize(a.getInteger(R.attr.primaryTextSize, 18));
            setSecondaryTextSize(a.getInteger(R.attr.primaryTextSize, 16));
            setSecondaryTextMaxLines(a.getInteger(R.attr.secondaryTextMaxLines, 1));
            secondaryText = a.getString(R.attr.secondaryText);
            primaryText = a.getString(R.attr.primaryText);
            a.recycle();
        } catch (Exception e) {
        }
        init();

    }

    public MaterialTwoLineListItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        try {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaterialTwoLineListItem, 0, 0);
            setPrimaryTextColor(a.getInteger(R.attr.primaryTextColor, R.color.dark_dark_grey));
            setSecondaryTextColor(a.getInteger(R.attr.secondaryTextColor, R.color.dark_grey));
            setPrimaryTextSize(a.getInteger(R.attr.primaryTextSize, 18));
            setSecondaryTextSize(a.getInteger(R.attr.primaryTextSize, 16));
            setSecondaryTextMaxLines(a.getInteger(R.attr.secondaryTextMaxLines, 1));

            a.recycle();
        } catch (Exception e) {
        }
        init();

    }

    public int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private void init() {
        //Todo consider 16 and 14 (in the guidelines)

        primaryTextView = new TextView(getContext());
        primaryTextView.setTextColor(primaryTextColor);
        primaryTextView.setTypeface(null, Typeface.BOLD);
        primaryTextView.setTextSize(18);
        setPadding(primaryTextView, 16, -1);

        secondaryTextView = new TextView(getContext());
        secondaryTextView.setTextColor(secondaryTextColor);
        secondaryTextView.setTextSize(16);
        setPadding(secondaryTextView, 16, 1);

        primaryTextView.setText("Primary");
        secondaryTextView.setText("Secondary");

        final LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(primaryTextView, params);
        addView(secondaryTextView, params);
    }

    private void setPadding(final View view, final int dp, int div) {
        final int padding = dpToPixels(dp);
        switch (div) {
            case 1:
                view.setPadding(padding, padding, padding, padding / 2);
                break;
            case 0:
                view.setPadding(padding, padding, padding, padding);
                break;
            case -1:
                view.setPadding(padding, padding / 2, padding, padding);
                break;
        }


    }

    public void setSecondaryTextColor(int color) {
        secondaryTextColor = color;
        if (secondaryTextView != null)

            secondaryTextView.setTextColor(color);
    }

    public void setSecondaryTextMaxLines(int maxLines) {
        secondaryTextMaxLines = maxLines;
        if (secondaryTextView != null)
            secondaryTextView.setMaxLines(maxLines);
    }

    public void setSecondaryTextSize(int sp) {
        secondaryTextSize = sp;
        if (secondaryTextView != null)
            secondaryTextView.setTextSize(sp);
    }

    public void setPrimaryTextSize(int sp) {
        primaryTextSize = sp;
        if (primaryTextView != null)
            primaryTextView.setTextSize(sp);
    }


    public void setPrimaryTextColor(int color) {
        primaryTextColor = color;
        if (primaryTextView != null)
            primaryTextView.setTextColor(color);
    }

    @Override
    protected void onLayout(boolean b, int i, int i2, int i3, int i4) {
        primaryTextView.layout(getPaddingLeft(), getPaddingTop(),
                getMeasuredWidth() - getPaddingRight(),
                primaryTextView.getMeasuredHeight() + getPaddingTop());

        secondaryTextView.layout(getPaddingLeft(),
                getMeasuredHeight() - getPaddingBottom() - secondaryTextView.getMeasuredHeight(),
                getMeasuredWidth() - getPaddingRight(),
                getMeasuredHeight() - getPaddingBottom()
        );
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredHeight = 0;
        int measuredWidth = 0;

        for (int i = 0; i < getChildCount(); i++) {

            final View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            measuredHeight += child.getMeasuredHeight();
            measuredWidth = Math.max(child.getMeasuredWidth(), measuredWidth);
        }

        setMeasuredDimension(resolveSizeAndState(measuredWidth, widthMeasureSpec, 0),
                resolveSizeAndState(measuredHeight, heightMeasureSpec, 0));


    }
}
