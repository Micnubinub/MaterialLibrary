package com.micnubinub.materiallibrary;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;


/**
 * Created by root on 30/09/14.
 */
public class MaterialRadioButton extends ViewGroup {

    private static final DecelerateInterpolator interpolator = new DecelerateInterpolator();
    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static int PADDING = 2;
    private static int duration = 600;
    private final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
    private int height;
    private int clickedX, clickedY;
    private boolean touchDown = false, animateRipple;
    private float ripple_animated_value = 0;
    private final ValueAnimator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {

        }

        @Override
        public void onAnimationEnd(Animator animator) {
            if (!touchDown)
                ripple_animated_value = 0;

            animateRipple = false;
            invalidatePoster();
        }

        @Override
        public void onAnimationCancel(Animator animator) {


        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }
    };
    private int rippleR;
    private int textSize;
    private String text = "";
    private RadioButton radioButton;
    private int cx, cy, r, width, color_on, color_off, hole_r, inner_hole_r, color_hole;
    private boolean checked = false;
    private float animated_value = 0;
    private final ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            animated_value = (Float) (animation.getAnimatedValue());
            ripple_animated_value = animated_value;
            invalidatePoster();
        }
    };
    private OnCheckedChangedListener listener;
    private TextView textView;
    private int rippleColor = 0x25000000;

    public MaterialRadioButton(Context context) {
        super(context);
        init();
    }

    public MaterialRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaterialRadioButton, 0, 0);
        setChecked(a.getBoolean(R.styleable.MaterialRadioButton_checked, false));
        text = a.getString(R.styleable.MaterialRadioButton_text);
        textSize = a.getInt(R.styleable.MaterialRadioButton_textSize, 20);
        a.recycle();
        textSize = textSize < 20 ? 20 : textSize;
        init();
    }

    public MaterialRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaterialRadioButton, 0, 0);
        setChecked(a.getBoolean(R.styleable.MaterialRadioButton_checked, false));
        text = a.getString(R.styleable.MaterialRadioButton_text);
        textSize = a.getInt(R.styleable.MaterialRadioButton_textSize, 20);
        a.recycle();
        textSize = textSize < 20 ? 20 : textSize;
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                clickedX = (int) event.getX();
                clickedY = (int) event.getY();
                rippleR = (int) (Math.sqrt(Math.pow(Math.max(width - clickedX, clickedX), 2) + Math.pow(Math.max(height - clickedY, clickedY), 2)) * 1.15);

                toggle();

                touchDown = true;
                animateRipple = true;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                touchDown = false;

                if (!animator.isRunning()) {
                    ripple_animated_value = 0;
                    invalidatePoster();
                }
                break;
        }
        return true;
    }

    private int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onLayout(boolean b, int i, int i2, int i3, int i4) {
        final int radioButtonPaddingTop = ((getMeasuredHeight() - radioButton.getMeasuredHeight()) / 2);
        radioButton.layout(
                getPaddingLeft(),
                radioButtonPaddingTop,
                getPaddingLeft() + radioButton.getMeasuredWidth(),
                getMeasuredHeight() - radioButtonPaddingTop
        );

        final int textViewPaddingTop = ((getMeasuredHeight() - textView.getMeasuredHeight()) / 2);
        textView.layout(
                getPaddingLeft() + radioButton.getMeasuredWidth() + PADDING,
                textViewPaddingTop,
                getMeasuredWidth() - getPaddingRight(),
                getMeasuredHeight() - textViewPaddingTop);
        checkViewParams(textView);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredHeight = 0;
        int measuredWidth = 0;

        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            measuredHeight = Math.max(measuredHeight, child.getMeasuredHeight());
            measuredWidth += child.getMeasuredWidth();
        }

        setMeasuredDimension(resolveSizeAndState(measuredWidth, widthMeasureSpec, 0),
                resolveSizeAndState(measuredHeight, heightMeasureSpec, 0));
    }

    private void checkViewParams(final View view, final int layoutWidth, final int layoutHeight) {
        final int width = view.getMeasuredWidth();
        final int height = view.getMeasuredHeight();
        if ((width > layoutWidth) || (height > layoutHeight)) {
            view.setLayoutParams(new LayoutParams(layoutWidth, layoutHeight));
            measureChild(view, MeasureSpec.AT_MOST, MeasureSpec.AT_MOST);
            view.requestLayout();
            view.invalidate();
            requestLayout();

        }
    }

    private void checkViewParams(final View view) {
        final int layoutWidth = view.getRight() - view.getLeft();
        final int layoutHeight = view.getBottom() - view.getTop();

        checkViewParams(view, layoutWidth, layoutHeight);

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
        MaterialRadioButton.duration = duration;
        animator.setDuration(duration);
    }

    public void setText(String text) {
        if (textView != null)
            textView.setText(text);
        invalidate();
    }

    private void init() {
        setWillNotDraw(false);

        color_off = 0xff0c0c0c;
        color_on = 0xff42bd41;
        color_hole = 0xffffffff;

        width = dpToPixels(28);
        PADDING = dpToPixels(4);

        radioButton = new RadioButton(getContext());
        radioButton.setLayoutParams(new LayoutParams(width, width));
        radioButton.setPadding(PADDING, PADDING, PADDING, PADDING);

        textView = new TextView(getContext());
        PADDING = dpToPixels(5);
        textView.setPadding(PADDING, PADDING, PADDING, PADDING);
        textView.setTextColor(getResources().getColor(R.color.dark_grey_text));
        textView.setTextSize(textSize);
        textView.setMaxLines(2);
        textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        addView(textView);
        addView(radioButton);

        setText(text);


        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color_off);

        animator.setInterpolator(interpolator);
        animator.setDuration(duration);
        animator.addListener(animatorListener);
        animator.addUpdateListener(updateListener);
    }

    private void animateSwitch() {
        if (animator.isRunning() || animator.isStarted())
            animator.cancel();
        animator.start();
    }

    public void setOnCheckedChangeListener(OnCheckedChangedListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }

    public void setDuration(int duration) {
        MaterialRadioButton.duration = duration;
        animator.setDuration(duration);
    }

    private void invalidatePoster() {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        this.post(runnable);
        if (radioButton != null) {
            radioButton.invalidate();
        }
    }

    public void setRippleColor(int color) {
        rippleColor = color;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (animateRipple) {
            paint.setColor(rippleColor);
            canvas.drawCircle(clickedX, clickedY, rippleR * ripple_animated_value, paint);
        }
    }


    @Override
    protected void onSizeChanged(final int w, final int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    public interface OnCheckedChangedListener {
        public void onCheckedChange(MaterialRadioButton materialRadioButton, boolean isChecked);
    }

    private final class RadioButton extends View {
        public RadioButton(Context context) {
            super(context);
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if (isChecked())
                animateOn(canvas);
            else
                animateOff(canvas);
        }


        private void animateOff(Canvas canvas) {
            paint.setColor(color_hole);
            if (animator.isRunning()) {
                canvas.drawCircle(cx, cy, hole_r, paint);
                paint.setColor(color_on);

                float circleAnimatedValue = (animated_value / 0.85f);
                circleAnimatedValue = circleAnimatedValue > 1 ? 1 : circleAnimatedValue;

                canvas.drawCircle(cx, cy, inner_hole_r * (1 - circleAnimatedValue), paint);
            } else {
                canvas.drawCircle(cx, cy, hole_r, paint);
            }
        }

        private void animateOn(Canvas canvas) {
            paint.setColor(color_hole);
            if (animator.isRunning()) {
                canvas.drawCircle(cx, cy, hole_r, paint);
                paint.setColor(color_on);

                float circleAnimatedValue = (animated_value / 0.85f);
                circleAnimatedValue = circleAnimatedValue > 1 ? 1 : circleAnimatedValue;

                canvas.drawCircle(cx, cy, inner_hole_r * circleAnimatedValue, paint);
            } else {
                canvas.drawCircle(cx, cy, hole_r, paint);
                paint.setColor(color_on);
                canvas.drawCircle(cx, cy, inner_hole_r, paint);
            }
        }


        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            r = Math.min(w - getPaddingLeft() - getPaddingRight(), h - getPaddingTop() - getPaddingBottom()) / 2;
            cx = w / 2;
            cy = h / 2;
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
