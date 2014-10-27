package com.micnubinub.materiallibrary;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import java.util.Random;

/**
 * Created by root on 27/10/14.
 */
public class DeathScreen extends ViewGroup {
    private static final Random rand = new Random();
    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final DecelerateInterpolator interpolator = new DecelerateInterpolator();
    private static final int[] particle1StopPos = new int[2], particle3StopPos = new int[2], particle2StopPos = new int[2];
    private static int duration = 1200;
    private final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
    private int width, height;
    private int[] particle1StartPos, particle2StartPos,
            particle3StartPos;
    private float animated_value;
    private final ValueAnimator.AnimatorUpdateListener animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            animated_value = (Float) (animation.getAnimatedValue());
            invalidatePoster();
        }
    };
    private boolean animateCircles = false;
    private final ValueAnimator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
            animateCircles = true;
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            animateCircles = false;
        }

        @Override
        public void onAnimationCancel(Animator animator) {


        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }
    };

    public DeathScreen(Context context) {
        super(context);
        init();
    }

    public int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
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

    @Override
    protected void onLayout(boolean b, int i, int i2, int i3, int i4) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("drawing", "");

        if (animateCircles) {
            if (animated_value < 0.5f) {
                final float anim = animated_value / 0.5f;
                canvas.drawCircle(
                        particle1StartPos[0] + (anim * (particle1StopPos[0] - particle1StartPos[0])),
                        particle1StartPos[1] + (anim * (particle1StopPos[1] - particle1StartPos[1])),
                        particle1StartPos[2],
                        paint
                );

                canvas.drawCircle(
                        particle2StartPos[0] + (anim * (particle2StopPos[0] - particle2StartPos[0])),
                        particle2StartPos[1] + (anim * (particle2StopPos[1] - particle2StartPos[1])),
                        particle2StartPos[2],
                        paint
                );

                canvas.drawCircle(
                        particle3StartPos[0] + (anim * (particle3StopPos[0] - particle3StartPos[0])),
                        particle3StartPos[1] + (anim * (particle3StopPos[1] - particle3StartPos[1])),
                        particle3StartPos[2],
                        paint
                );

            } else {
                final float anim = (animated_value - 0.5f) / 0.5f;
//Todo fix anim
                canvas.drawCircle(
                        particle1StopPos[0],
                        particle1StopPos[1],
                        particle1StartPos[2] + (width * anim),
                        paint
                );

                canvas.drawCircle(
                        particle2StopPos[0],
                        particle2StopPos[1],
                        particle2StartPos[2] + (width * anim),
                        paint
                );

                canvas.drawCircle(
                        particle3StopPos[0],
                        particle3StopPos[1],
                        particle3StartPos[2] + (width * anim),
                        paint
                );


            }
        } else {
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);
        }
    }

    private void invalidatePoster() {
        post(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
    }

    private void generateRandomPos() {
        particle1StopPos[0] = rand.nextInt(width);
        particle1StopPos[1] = rand.nextInt(height);

        particle2StopPos[0] = rand.nextInt(width);
        particle2StopPos[1] = rand.nextInt(height);

        particle3StopPos[0] = rand.nextInt(width);
        particle3StopPos[1] = rand.nextInt(height);
    }

    private void animateCircles() {
        generateRandomPos();
        animator.start();
    }

    private void init() {
        setWillNotDraw(false);
        animator.setInterpolator(interpolator);
        animator.addUpdateListener(animatorUpdateListener);
        animator.addListener(animatorListener);
        animator.setDuration(duration);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("touch", "");
        show(0xffffbb00, new int[]{0, 10, 15},
                new int[]{width, height / 2, 12},
                new int[]{width, height / 4, 12});
        return true;
    }

    public void show(final int circleColor, final int[] particle1StartPos, final int[] particle2StartPos, final int[] particle3StartPos) {
        paint.setColor(circleColor);
        this.particle1StartPos = particle1StartPos;
        this.particle2StartPos = particle2StartPos;
        this.particle3StartPos = particle3StartPos;
        animateCircles();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }
}
