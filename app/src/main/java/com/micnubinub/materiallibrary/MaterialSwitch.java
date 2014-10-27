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
@SuppressWarnings("ALL")
public class MaterialSwitch extends ViewGroup {

    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static int PADDING = 2;
    private static int duration = 600;
    private final DecelerateInterpolator interpolator = new DecelerateInterpolator();
    private final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
    private int height;
    private int rippleR;
    private float ripple_animated_value = 0;
    private int clickedX, clickedY;
    private boolean touchDown = false, animateRipple;
    private ValueAnimator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
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
    private int textSize;
    private String text = "";
    private Switch materialSwitch;

    private float line_pos;
    private int r, color_on, color_off, hole_r, color_hole;
    private boolean updating = false;
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
    private int width;
    private int rippleColor = 0x25000000;

    public MaterialSwitch(Context context) {
        super(context);
        init();
    }

    public MaterialSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaterialRadioButton, 0, 0);
        setChecked(a.getBoolean(R.styleable.MaterialRadioButton_checked, false));
        text = a.getString(R.styleable.MaterialRadioButton_text);
        textSize = a.getInt(R.styleable.MaterialRadioButton_textSize, 20);
        a.recycle();
        textSize = textSize < 20 ? 20 : textSize;
        init();
    }

    public MaterialSwitch(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaterialRadioButton, 0, 0);
        setChecked(a.getBoolean(R.styleable.MaterialRadioButton_checked, false));
        text = a.getString(R.styleable.MaterialRadioButton_text);
        textSize = a.getInt(R.styleable.MaterialRadioButton_textSize, 20);
        a.recycle();
        textSize = textSize < 20 ? 20 : textSize;
        init();
    }

    private int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onLayout(boolean b, int i, int i2, int i3, int i4) {
        final int textViewPaddingTop = ((getMeasuredHeight() - textView.getMeasuredHeight()) / 2);
        textView.layout(
                getPaddingLeft(),
                textViewPaddingTop,
                getMeasuredWidth() - getPaddingRight() - materialSwitch.getMeasuredWidth() - PADDING,
                getMeasuredHeight() - textViewPaddingTop);

        checkViewParams(textView);

        final int radioButtonPaddingTop = ((getMeasuredHeight() - materialSwitch.getMeasuredHeight()) / 2);
        materialSwitch.layout(
                getMeasuredWidth() - getPaddingRight() - materialSwitch.getMeasuredWidth(),
                radioButtonPaddingTop,
                getMeasuredWidth() - getPaddingRight(),
                getMeasuredHeight() - radioButtonPaddingTop
        );


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //Todo setMeasuredDimension();
        //Todo  measureChildWithMargins()
        //Todo xmlns:custom="http://schemas.android.com/apk/res/com.packa..."
        // measureChildren(widthMeasureSpec, heightMeasureSpec);

        int measuredHeight = 0;
        int measuredWidth = 0;

        try {
            for (int i = 0; i < getChildCount(); i++) {
                final View child = getChildAt(i);
                measureChild(child, widthMeasureSpec, heightMeasureSpec);

                measuredHeight = Math.max(measuredHeight, child.getMeasuredHeight());
                measuredWidth += child.getMeasuredWidth();
            }
        } catch (Exception ignored) {
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

    private void setPaintColor(int color) {
        try {
            paint.setColor(color);
        } catch (Exception ignored) {
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

    public void setUpdating(boolean updating) {
        this.updating = updating;
    }

    public void setAnimationDuration(int duration) {
        MaterialSwitch.duration = duration;
        animator.setDuration(duration);
    }

    public void setText(String text) {
        if (textView != null)
            textView.setText(text);
        invalidate();
    }

    private void init() {
        setWillNotDraw(false);

        setOffColor(getResources().getColor(R.color.lite_grey));
        setOnColor(getResources().getColor(R.color.material_green_light));
        setHoleColor(getResources().getColor(R.color.white));
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(color_off);

        PADDING = dpToPixels(4);

        materialSwitch = new Switch(getContext());

        materialSwitch.setLayoutParams(new LayoutParams(dpToPixels(35), dpToPixels(18)));
        setPadding(PADDING, PADDING, PADDING, PADDING);

        textView = new TextView(getContext());
        PADDING = dpToPixels(5);
        textView.setPadding(PADDING, PADDING, PADDING, PADDING);
        textView.setTextColor(getResources().getColor(R.color.dark_grey_text));
        textView.setTextSize(18);
        textView.setMaxLines(2);
        textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));


        setText(text);
        addView(textView);
        addView(materialSwitch);
        materialSwitch.invalidate();

        setText(text);

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

    public void setRippleColor(int color) {
        rippleColor = color;
    }

    public void setDuration(int duration) {
        MaterialSwitch.duration = duration;
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
        if (materialSwitch != null) {
            materialSwitch.invalidate();
        }
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
        height = h;
        width = w;
    }

    public interface OnCheckedChangedListener {
        public void onCheckedChange(MaterialSwitch materialSwitch, boolean isChecked);
    }

    private class Switch extends View {
        private int switchWidth;

        public Switch(Context context) {
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


        private void animateOn(Canvas canvas) {

            setPaintColor(color_on);
            canvas.drawLine(getPaddingLeft(), line_pos, (float) (switchWidth - getPaddingRight()), line_pos, paint);
            canvas.drawCircle((r + (animated_value * (switchWidth - r - r))), line_pos, r, paint);

            setPaintColor(color_hole);
            if (updating)
                canvas.drawCircle((r + (animated_value * (switchWidth - r - r))), line_pos, hole_r * (1 - animated_value), paint);


        }

        private void animateOff(Canvas canvas) {
            setPaintColor(color_off);
            canvas.drawLine(getPaddingLeft(), line_pos, (float) (switchWidth - getPaddingRight()), line_pos, paint);
            canvas.drawCircle((r + ((1 - animated_value) * (switchWidth - r - r))), line_pos, r, paint);

            setPaintColor(color_hole);

            if (updating)
                canvas.drawCircle((r + ((1 - animated_value) * (switchWidth - r - r))), line_pos, hole_r * animated_value, paint);
            else
                canvas.drawCircle((r + ((1 - animated_value) * (switchWidth - r - r))), line_pos, hole_r, paint);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            paint.setStrokeWidth(0.15f * Math.min(w, h));
            line_pos = h / 2;
            r = Math.min((w - getPaddingLeft() - getPaddingRight()), (h - getPaddingBottom() - getPaddingTop())) / 2;
            hole_r = (int) (r * 0.85f);
            switchWidth = w;
        }

        @Override
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            if (isChecked())
                animated_value = 1;
        }

    }


}
