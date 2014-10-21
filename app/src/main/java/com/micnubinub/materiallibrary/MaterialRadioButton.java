package com.micnubinub.materiallibrary;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;


/**
 * Created by root on 30/09/14.
 */
public class MaterialRadioButton extends ViewGroup {
    private static final AccelerateInterpolator interpolator = new AccelerateInterpolator();
    private static int PADDING = 2;
    private static int duration = 750;
    private final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int textSize;
    private String text = "";
    private RadioButton radioButton;
    private final OnClickListener l = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (radioButton != null)
                toggle();
        }
    };
    private int cx, cy, r, width, color_on, color_off, hole_r, inner_hole_r, color_hole;
    private boolean checked = false;
    private boolean updating = false;
    private float animated_value = 0;
    private OnCheckedChangedListener listener;
    private TextView textView;

    public MaterialRadioButton(Context context) {
        super(context);
        init(context);
    }

    public MaterialRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaterialRadioButton, 0, 0);
        setChecked(a.getBoolean(R.styleable.MaterialRadioButton_checked, false));
        text = a.getString(R.styleable.MaterialRadioButton_text);
        textSize = a.getInt(R.styleable.MaterialRadioButton_textSize, 20);
        a.recycle();
        textSize = textSize < 20 ? 20 : textSize;
        init(context);
    }

    private int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onLayout(boolean b, int i, int i2, int i3, int i4) {
        radioButton.layout(
                getPaddingLeft(),
                getPaddingTop() + ((getMeasuredHeight() - radioButton.getMeasuredHeight()) / 2),
                getPaddingLeft() + radioButton.getMeasuredWidth(),
                getMeasuredHeight() - ((getMeasuredHeight() - radioButton.getMeasuredHeight()) / 2)
        );

        if (textView != null) {
            textView.layout(
                    getPaddingLeft() + radioButton.getMeasuredWidth() + PADDING,
                    getPaddingTop() + ((getMeasuredHeight() - textView.getMeasuredHeight()) / 2),
                    getMeasuredWidth() - getPaddingRight(),
                    getMeasuredHeight() - ((getMeasuredHeight() - radioButton.getMeasuredHeight()) / 2));
        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //Todo setMeasuredDimension();
        //Todo  measureChildWithMargins()
        //Todo xmlns:custom="http://schemas.android.com/apk/getResources()/com.packa..."
        // measureChildren(widthMeasureSpec, heightMeasureSpec);
        measureChild(radioButton, widthMeasureSpec, heightMeasureSpec);

        int measuredHeight = radioButton.getMeasuredHeight();
        int measuredWidth = radioButton.getMeasuredWidth();

        if (textView != null) {
            measureChild(textView, widthMeasureSpec, heightMeasureSpec);

            measuredHeight = Math.max(measuredHeight, textView.getMeasuredHeight());
            measuredWidth = textView.getMeasuredWidth();
        }


        setMeasuredDimension(resolveSizeAndState(measuredWidth, widthMeasureSpec, 0),
                resolveSizeAndState(measuredHeight, heightMeasureSpec, 0));

    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        animateSwitch();
        notifyListener();
    }

    public void toggle() {
        setChecked(!isChecked());
    }

    private void notifyListener() {
        if (listener != null)
            listener.onCheckedChange(this, isChecked());
    }

    public void setUpdating(boolean updating) {
        this.updating = updating;
    }

    private void setPaintColor(int color) {
        try {
            this.paint.setColor(color);
        } catch (Exception e) {
        }
    }

    public void setOffColor(int color_off) {
        this.color_off = color_off;
    }

    public void setOnColor(int color_on) {
        this.color_on = color_on;
    }

    public void setHoleColor(int hole_color) {
        this.color_hole = hole_color;
    }

    public void setAnimationDuration(int duration) {
        this.duration = duration;
        animator.setDuration(duration);
    }

    public void setText(String text) {
        if (text != null && text.length() > 0) {
            if (textView == null)
                textView = new TextView(getContext());
            PADDING = dpToPixels(5);
            textView.setPadding(PADDING, PADDING, PADDING, PADDING);
            textView.setTextColor(getResources().getColor(R.color.dark_grey_text));
            textView.setTextSize(textSize);
            textView.setText(text);
            addView(textView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        } else {
            try {
                removeView(textView);
            } catch (Exception e) {
            }
            textView = null;
        }
        invalidate();
    }

    private void init(Context context) {
        width = dpToPixels(32);
        radioButton = new RadioButton(context);
        PADDING = dpToPixels(2);

        radioButton.setLayoutParams(new LayoutParams(width, width));
        radioButton.setPadding(PADDING, PADDING, PADDING, PADDING);
        addView(radioButton);

        setText(text);

        setOffColor(getResources().getColor(R.color.lite_grey));
        setOnColor(getResources().getColor(R.color.material_green_light));
        setHoleColor(getResources().getColor(R.color.white));

        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(color_off);

        animator.setInterpolator(interpolator);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animated_value = ((Float) (animation.getAnimatedValue())).floatValue();
                MaterialRadioButton.this.invalidate();
            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                setUpdating(true);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setUpdating(false);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                setUpdating(false);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                setUpdating(true);
            }
        });
        setOnClickListener(l);
    }


    private void animateSwitch() {
        if (radioButton != null)
            radioButton.animateSwitch();
    }

    public void setOnCheckedChangeListener(OnCheckedChangedListener listener) {
        this.listener = listener;
    }

    public interface OnCheckedChangedListener {
        public void onCheckedChange(MaterialRadioButton materialRadioButton, boolean isChecked);
    }

    private class RadioButton extends View {

        public RadioButton(Context context) {
            super(context);
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (isChecked())
                animateOn(canvas);
            else
                animateOff(canvas);

            if (updating)
                invalidate();

        }

        private void animateOff(Canvas canvas) {
            //paint.setShadowLayer(7, 0, 0, Color.argb(255, 90, 90, 90));
            setPaintColor(color_hole);
            if (updating) {
                canvas.drawCircle(cx, cy, hole_r, paint);
                // paint.setShadowLayer(0, 0, 0, 0);
                setPaintColor(color_on);
                canvas.drawCircle(cx, cy, inner_hole_r * animated_value, paint);
            } else {
                //paint.setShadowLayer(0, 0, 0, 0);
                canvas.drawCircle(cx, cy, hole_r, paint);
            }
        }

        private void animateOn(Canvas canvas) {
            //paint.setShadowLayer(7, 0, 0, Color.argb(255, 90, 90, 90));
            setPaintColor(color_hole);
            if (updating) {
                canvas.drawCircle(cx, cy, hole_r, paint);
                //paint.setShadowLayer(0, 0, 0, 0);
                setPaintColor(color_on);
                canvas.drawCircle(cx, cy, inner_hole_r * animated_value, paint);
            } else {
                canvas.drawCircle(cx, cy, hole_r, paint);
                // paint.setShadowLayer(0, 0, 0, 0);
                setPaintColor(color_on);
                canvas.drawCircle(cx, cy, inner_hole_r, paint);

            }
        }


        public void animateSwitch() {
            invalidate();
            try {
                if (animator.isRunning())
                    animator.cancel();

                if (isChecked())
                    animator.start();
                else
                    animator.reverse();
            } catch (Exception e) {
            }

        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            r = (int) (0.7f * (Math.min((width - getPaddingLeft() - getPaddingRight()), (width - getPaddingBottom() - getPaddingTop())) / 2));
            cx = getPaddingLeft() + (width / 2);
            cy = getPaddingTop() + (width / 2);
            hole_r = (int) (r * 0.9f);
            inner_hole_r = (int) (r * 0.75f);
        }

        @Override
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            if (isChecked())
                animated_value = 1;
        }

    }
}
