package com.micnubinub.materiallibrary;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;


/**
 * Created by root on 30/09/14.
 */
public class MaterialRadioButton extends ViewGroup {

    private static final AccelerateInterpolator interpolator = new AccelerateInterpolator();
    private static final Paint ripplePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static int PADDING = 2;
    private static int duration = 750;
    private final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int height;
    private int rippleR;
    private float ripple_animated_value = 0;
    private int clickedX, clickedY;
    private boolean touchDown = false;
    private ValueAnimator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {

        }

        @Override
        public void onAnimationEnd(Animator animator) {
            if (!touchDown)
                ripple_animated_value = 0;

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
    private RadioButton radioButton;
    private int cx, cy, r, width, color_on, color_off, hole_r, inner_hole_r, color_hole;
    private boolean checked = false;
    private float animated_value = 0;
    private OnCheckedChangedListener listener;
    private TextView textView;
    private boolean viewSizeChanged = false;

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
        int width = view.getMeasuredWidth();
        int height = view.getMeasuredHeight();
        if ((width > layoutWidth) || (height > layoutHeight))
            view.setLayoutParams(new LayoutParams(layoutWidth, layoutHeight));
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
        if (textView != null)
            textView.setText(text);

        invalidate();
    }

    private void init() {
        setWillNotDraw(false);

        ripplePaint.setColor(0x25000000);

        width = dpToPixels(32);
        radioButton = new RadioButton(getContext());
        PADDING = dpToPixels(2);

        radioButton.setLayoutParams(new LayoutParams(width, width));
        radioButton.setPadding(PADDING, PADDING, PADDING, PADDING);

        textView = new TextView(getContext());
        PADDING = dpToPixels(5);
        textView.setPadding(PADDING, PADDING, PADDING, PADDING);
        textView.setTextColor(getResources().getColor(R.color.dark_grey_text));
        textView.setTextSize(textSize);
        textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        addView(textView);
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
        animator.addListener(animatorListener);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animated_value = ((Float) (animation.getAnimatedValue())).floatValue();
                ripple_animated_value = animated_value;
                Log.e("Animator runin", String.format("val : %f", animated_value));
                invalidatePoster();
            }
        });


    }


    private void animateSwitch() {
        if (radioButton != null)
            radioButton.animateSwitch();
        invalidatePoster();
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

                if (animator.isRunning() || animator.isStarted())
                    animator.cancel();
                animator.start();
                toggle();

                touchDown = true;
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
        ripplePaint.setColor(color);
    }


    public void setRippleAlpha(int alpha) {
        ripplePaint.setAlpha(alpha);
    }

    public void setDuration(int duration) {
        MaterialRadioButton.duration = duration;
        animator.setDuration(duration);
    }


    private void invalidatePoster() {
        this.post(new Runnable() {
            @Override
            public void run() {
                radioButton.invalidate();
                invalidate();
            }
        });
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.drawCircle(clickedX, clickedY, rippleR * ripple_animated_value, ripplePaint);
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

            Log.e("drawing", String.format("isCheced : %b", isChecked()));

        }

        private void animateOff(Canvas canvas) {
            //paint.setShadowLayer(7, 0, 0, Color.argb(255, 90, 90, 90));
            setPaintColor(color_hole);
            if (animator.isRunning()) {
                canvas.drawCircle(cx, cy, hole_r, paint);
                // paint.setShadowLayer(0, 0, 0, 0);
                setPaintColor(color_on);
                canvas.drawCircle(cx, cy, inner_hole_r * (1 - animated_value), paint);
            } else {
                //paint.setShadowLayer(0, 0, 0, 0);
                canvas.drawCircle(cx, cy, hole_r, paint);
            }
        }

        private void animateOn(Canvas canvas) {
            //paint.setShadowLayer(7, 0, 0, Color.argb(255, 90, 90, 90));
            setPaintColor(color_hole);
            if (animator.isRunning()) {
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

            try {
                if (animator.isRunning())
                    animator.cancel();
                    animator.start();

            } catch (Exception e) {
            }

            invalidate();

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
