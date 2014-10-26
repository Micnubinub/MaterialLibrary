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
    private final MaterialCheckBox.OnCheckedChangedListener listener = new MaterialCheckBox.OnCheckedChangedListener() {
        @Override
        public void onCheckedChange(MaterialCheckBox materialCheckBox, boolean isChecked) {
            final int indexOfChild = indexOfChild(materialCheckBox);

            if (isChecked && !(selectedRadioButton == indexOfChild)) {
                if (selectedRadioButton >= 0) {
                    MaterialCheckBox materialCheckBox1 = (MaterialCheckBox) getChildAt(selectedRadioButton);
                    materialCheckBox1.setChecked(false);
                    materialCheckBox1.invalidate();
                }
            }
            if (selectedRadioButton == indexOfChild) {
                selectedRadioButton = -1;
            } else {
                selectedRadioButton = indexOfChild;
            }

            if (selectionChanged != null)
                selectionChanged.onSelectionChanged((MaterialCheckBox) getChildAt(selectedRadioButton), selectedRadioButton);
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


        int measuredHeight = 0;
        int measuredWidth = 0;

        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE && child != null) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                measuredHeight += child.getMeasuredHeight();
                measuredWidth = Math.max(measuredWidth, child.getMeasuredWidth());
            }
        }
        setMeasuredDimension(resolveSizeAndState(measuredWidth, widthMeasureSpec, 0),
                resolveSizeAndState(measuredHeight, heightMeasureSpec, 0));

    }
    //Todo check if everything works fine, cause you removed all the other addView(...)s

    @Override
    public void addView(View child, int index, LayoutParams params) {
        addListener((MaterialCheckBox) child);
        super.addView(child, index, params);
    }

    private void addListener(MaterialCheckBox materialCheckBox) {
        try {
            materialCheckBox.setOnCheckedChangeListener(listener);
            materialCheckBox.setChecked(false);
        } catch (Exception e) {
        }
    }

    public void setOnSelectionChanged(OnSelectionChangedListener selectionChanged) {
        this.selectionChanged = selectionChanged;
    }

    public interface OnSelectionChangedListener {
        public void onSelectionChanged(MaterialCheckBox radioButton, int selectedChild);
    }
}
