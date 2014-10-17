package com.micnubinub.materiallibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by root on 18/09/14.
 */
public class MaterialRadioGroup extends ViewGroup {
    private int selectedRadioButton = -1;
    private OnSelectionChangedListener selectionChanged;
    private final MaterialRadioButton.OnCheckedChangedListener listener = new MaterialRadioButton.OnCheckedChangedListener() {
        @Override
        public void onCheckedChange(MaterialRadioButton materialRadioButton, boolean isChecked) {
            final int indexOfChild = indexOfChild(materialRadioButton);

            if (isChecked && !(selectedRadioButton == indexOfChild)) {
                if (selectedRadioButton >= 0) {
                    MaterialRadioButton materialRadioButton1 = (MaterialRadioButton) getChildAt(selectedRadioButton);
                    materialRadioButton1.setChecked(false);
                    materialRadioButton1.invalidate();
                }
            }
            if (selectedRadioButton == indexOfChild) {
                selectedRadioButton = -1;
            } else {
                selectedRadioButton = indexOfChild;
            }

            if (selectionChanged != null)
                selectionChanged.onSelectionChanged(selectedRadioButton);
        }
    };

    public MaterialRadioGroup(Context context) {
        super(context);
    }

    public MaterialRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public MaterialRadioGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean b, int i, int i2, int i3, int i4) {

        int currentChildRight;
        int currentChildBottom;
        int currentChildTop = 0;
        final int currentChildLeft = getPaddingLeft();

        for (int j = 0; j < getChildCount(); j++) {
            final View child = getChildAt(j);

            if (j > 0) {
                currentChildTop += getChildAt(j - 1).getHeight();
                currentChildBottom = currentChildTop + child.getMeasuredHeight();
            } else {
                currentChildTop = getPaddingTop();
                currentChildBottom = child.getMeasuredHeight() - getPaddingBottom();
            }
            currentChildRight = child.getMeasuredWidth() - getPaddingRight();
            child.layout(currentChildLeft, currentChildTop, currentChildRight, currentChildBottom);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //Todo setMeasuredDimension();
        //Todo  measureChildWithMargins()
        //Todo xmlns:custom="http://schemas.android.com/apk/res/com.packa..."

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int measuredHeight = 0;
        int measuredWidth = 0;

        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                //   child.measure(widthMeasureSpec, heightMeasureSpec);
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                measuredHeight += child.getMeasuredHeight();
                // measuredHeight = Math.max(measuredHeight, child.getMeasuredHeight());
                measuredWidth = Math.max(measuredWidth, child.getMeasuredWidth());
            }
        }
        setMeasuredDimension(resolveSizeAndState(measuredWidth, widthMeasureSpec, 0),
                resolveSizeAndState(measuredHeight, heightMeasureSpec, 0));

    }

    @Override
    public void addView(View child) {
        addListener((MaterialRadioButton) child);
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        addListener((MaterialRadioButton) child);
        super.addView(child, index);
    }

    @Override
    public void addView(View child, int width, int height) {
        addListener((MaterialRadioButton) child);
        super.addView(child, width, height);

    }

    @Override
    public void addView(View child, LayoutParams params) {
        addListener((MaterialRadioButton) child);
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, LayoutParams params) {
        addListener((MaterialRadioButton) child);
        super.addView(child, index, params);
    }

    private void addListener(MaterialRadioButton materialRadioButton) {
        try {
            materialRadioButton.setOnCheckedChangeListener(listener);
            materialRadioButton.setChecked(false);
        } catch (Exception e) {
        }
    }

    public void setOnSelectionChanged(OnSelectionChangedListener selectionChanged) {
        this.selectionChanged = selectionChanged;
    }

    public interface OnSelectionChangedListener {
        public void onSelectionChanged(int selectedChild);
    }
}
