package com.micnubinub.materiallibrary;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by root on 17/10/14.
 */
public class MaterialRippleView extends ViewGroup {
    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final AccelerateInterpolator interpolator = new AccelerateInterpolator();
    private static int duration = 750;
    private final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
    private int width;
    private int height;
    private int r;
    private int paddingX, paddingY;
    private float animated_value = 0;
    private ValueAnimator.AnimatorUpdateListener animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            animated_value = ((Float) (animation.getAnimatedValue())).floatValue();
            invalidatePoster();
        }
    };

    private float scaleTo = 1.065f;
    private int clickedX, clickedY;
    private boolean scaleOnTouch = true;
    private boolean touchDown = false;

    public MaterialRippleView(Context context) {
        super(context);
        init();
    }

    public MaterialRippleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MaterialRippleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void postAnimatedValueReset(int delay) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!touchDown)
                    animated_value = 0;

                invalidatePoster();
            }
        }, delay);
    }

    private void scale(final float scale) {
        post(new Runnable() {
            @Override
            public void run() {
                getChildAt(0).setScaleX(scale);
                getChildAt(0).setScaleY(scale);
                invalidatePoster();
            }
        });

    }

    private void scaleLater() {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (touchDown)
                    scale(scaleTo);
                else
                    scale(1);
            }
        }, 100);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (scaleOnTouch) {
                    scaleLater();
                }

                clickedX = (int) event.getX();
                clickedY = (int) event.getY();
                r = (int) (Math.sqrt(Math.pow(Math.max(width - clickedX, clickedX), 2) + Math.pow(Math.max(height - clickedY, clickedY), 2)) * 1.15);

                if (animator.isRunning())
                    animator.cancel();
                animator.start();

                touchDown = true;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:

                if (scaleOnTouch) {
                    scale(1);
                }

                touchDown = false;

                if (animator.isRunning())
                    postAnimatedValueReset((int) (animator.getDuration() - animator.getCurrentPlayTime() + 15));
                else {
                    animated_value = 0;
                    invalidatePoster();
                }

                break;

        }
        return super.onInterceptTouchEvent(event);

    }

    public void setRippleColor(int color) {
        paint.setColor(color);
    }

    public void setRippleAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    private void init() {
        setWillNotDraw(false);
        animator.setInterpolator(interpolator);
        animator.addUpdateListener(animatorUpdateListener);
        animator.setDuration(duration);
        paint.setColor(0x25000000);
    }

    public void setDuration(int duration) {
        MaterialRippleView.duration = duration;
        animator.setDuration(duration);
    }

    public void setScaleTo(float scaleTo) {
        this.scaleTo = scaleTo;
    }

    public void setScaleOnTouch(boolean scaleOnTouch) {
        this.scaleOnTouch = scaleOnTouch;
    }

    private void invalidatePoster() {
        this.post(new Runnable() {
            @Override
            public void run() {
                invalidate();
                Log.e("invalidate", String.format("r : %f", animated_value));
            }
        });
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.drawCircle(clickedX, clickedY, r * animated_value, paint);
    }

    @Override
    protected void onLayout(boolean b, int i, int i2, int i3, int i4) {
        getChildAt(0).layout(paddingX, paddingY, getMeasuredWidth() - paddingX, getMeasuredHeight() - paddingY);
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


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    @Override
    public void addView(View child, int index, LayoutParams params) {
        if (getChildCount() >= 1)
            return;
        super.addView(child, index, params);
    }


    @Override
    protected void onSizeChanged(final int w, final int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        paddingX = (int) ((w - (w / scaleTo)) / 2);
        paddingY = (int) ((h - (h / scaleTo)) / 2);
        getChildAt(0).setPivotX(w / 2);
        getChildAt(0).setPivotY(h / 2);
    }
}


