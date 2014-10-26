package com.micnubinub.materiallibrary;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;


/**
 * Created by root on 30/09/14.
 */
public class MaterialCheckBox extends ViewGroup {

    private static final AccelerateInterpolator interpolator = new AccelerateInterpolator();
    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static int PADDING = 2;
    private static int duration = 650;
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
    private CheckBox materialCheckBox;
    private int width, color_off;
    private boolean checked = false;
    private float animated_value = 0;
    private final ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            animated_value = ((Float) (animation.getAnimatedValue())).floatValue();
            ripple_animated_value = animated_value;
            invalidatePoster();
        }
    };
    private OnCheckedChangedListener listener;
    private TextView textView;

    public MaterialCheckBox(Context context) {
        super(context);
        init();
    }

    public MaterialCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaterialCheckBox, 0, 0);
        setChecked(a.getBoolean(R.styleable.MaterialCheckBox_checked, false));
        text = a.getString(R.styleable.MaterialCheckBox_text);
        textSize = a.getInt(R.styleable.MaterialCheckBox_textSize, 20);
        a.recycle();
        textSize = textSize < 20 ? 20 : textSize;
        init();
    }

    public MaterialCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaterialCheckBox, 0, 0);
        setChecked(a.getBoolean(R.styleable.MaterialCheckBox_checked, false));
        text = a.getString(R.styleable.MaterialCheckBox_text);
        textSize = a.getInt(R.styleable.MaterialCheckBox_textSize, 20);
        a.recycle();
        textSize = textSize < 20 ? 20 : textSize;
        init();
    }

    private int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onLayout(boolean b, int i, int i2, int i3, int i4) {
        final int checkBoxButtonPaddingTop = ((getMeasuredHeight() - materialCheckBox.getMeasuredHeight()) / 2);
        materialCheckBox.layout(
                getPaddingLeft(),
                checkBoxButtonPaddingTop,
                getPaddingLeft() + materialCheckBox.getMeasuredWidth(),
                getMeasuredHeight() - checkBoxButtonPaddingTop
        );

        final int textViewPaddingTop = ((getMeasuredHeight() - textView.getMeasuredHeight()) / 2);
        textView.layout(
                getPaddingLeft() + materialCheckBox.getMeasuredWidth() + PADDING,
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

    private void setPaintColor(int color) {
        try {
            paint.setColor(color);
        } catch (Exception e) {
        }
    }

    public void setOffColor(int color_off) {
        this.color_off = color_off;
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

        color_off = 0xff0c0c0c;

        width = dpToPixels(28);
        PADDING = dpToPixels(4);

        materialCheckBox = new CheckBox(getContext());
        materialCheckBox.setLayoutParams(new LayoutParams(width, width));
        materialCheckBox.setPadding(PADDING, PADDING, PADDING, PADDING);

        textView = new TextView(getContext());
        PADDING = dpToPixels(5);
        textView.setPadding(PADDING, PADDING, PADDING, PADDING);
        textView.setTextColor(getResources().getColor(R.color.dark_grey_text));
        textView.setTextSize(textSize);
        textView.setMaxLines(2);
        textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        addView(textView);
        addView(materialCheckBox);

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
        paint.setColor(color);
    }

    public void setRippleAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    public void setDuration(int duration) {
        MaterialCheckBox.duration = duration;
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
        if (materialCheckBox != null) {
            materialCheckBox.post(runnable);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (animateRipple) {
            paint.setColor(0x25000000);
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
        public void onCheckedChange(MaterialCheckBox materialCheckBox, boolean isChecked);
    }

    private class CheckBox extends View {

        private int segments;
        private float leftX, leftY, midX, midY, rightX, rightY;
        private int inR, cx, cy, outR;
        private RectF rectF = new RectF(0, 0, 100, 100);

        public CheckBox(Context context) {
            super(context);
        }


        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            paint.setColor(isChecked() ? getResources().getColor(R.color.material_green) : getResources().getColor(R.color.material_red));
            final int sweepAngle = (int) ((animated_value < 0.75f ? animated_value / 0.75f : 1) * 360);
            canvas.drawArc(rectF, -90, sweepAngle, true, paint);

            paint.setColor(getResources().getColor(R.color.white));
            canvas.drawCircle(cx, cy, inR, paint);

            if (isChecked()) {
                paint.setColor(getResources().getColor(R.color.material_green_light));
                drawLines(canvas);
            }
        }

        private void drawLines(Canvas canvas) {
            if (animated_value > 0.25f) {

                final float leftProg = ((animated_value > 0.5f ? 0.5f : animated_value) - 0.25f) / 0.25f;
                canvas.drawLine(
                        leftX,
                        leftY,
                        leftX + ((midX - leftX) * leftProg),
                        leftY + ((midY - leftY) * leftProg),
                        paint
                );
                if (animated_value > 0.5) {
                    final float rightProg = (animated_value - 0.5f) / 0.5f;

                    canvas.drawLine(
                            midX - (paint.getStrokeWidth() / 4),
                            midY + (paint.getStrokeWidth() / 4),
                            midX + ((rightX - midX) * rightProg),
                            midY + ((rightY - midY) * rightProg),
                            paint
                    );
                }
            }
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            cx = w / 2;
            cy = h / 2;
            outR = Math.min(w, h) / 2;
            inR = (int) (0.9f * outR);
            rectF.set(cx - outR, cy - outR, cx + outR, cy + outR);
            paint.setStrokeWidth(0.1f * outR);

            segments = ((int) (rectF.right - rectF.left) / 19);
            leftX = rectF.left + (4 * segments);
            leftY = rectF.top + (10 * segments);

            midX = rectF.left + (8 * segments);
            midY = rectF.top + (14 * segments);

            rightX = rectF.left + (15 * segments);
            rightY = rectF.top + (6 * segments);

        }


    }


}
