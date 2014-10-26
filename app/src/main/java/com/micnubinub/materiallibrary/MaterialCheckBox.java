package com.micnubinub.materiallibrary;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by root on 26/10/14.
 */
public class MaterialCheckBox extends View {
    private static final AccelerateInterpolator interpolator = new AccelerateInterpolator();
    private static int duration = 750;
    private final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected boolean checked = false;
    int segments;
    float leftX, leftY, midX, midY, rightX, rightY;
    private int inR, cx, cy, outR;
    private RectF rectF = new RectF(0, 0, 100, 100);
    private float animated_value = 1;
    private ValueAnimator.AnimatorUpdateListener animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            animated_value = ((Float) (animation.getAnimatedValue())).floatValue();
            invalidate();
        }
    };
    private float scaleTo = 1.065f;
    private int clickedX, clickedY;
    private boolean scaleOnTouch = true;
    private boolean touchDown = false;
    private boolean animationDone = true;
    private ValueAnimator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
            animationDone = false;

        }

        @Override
        public void onAnimationEnd(Animator animator) {
            animationDone = true;
            animated_value = 1;
            invalidate();
        }

        @Override
        public void onAnimationCancel(Animator animator) {


        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }
    };
    private OnCheckedChangedListener listener;

    public MaterialCheckBox(Context context) {
        super(context);
        init();
    }

    public MaterialCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MaterialCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public static int dpToPixels(int dp, Resources res) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
    }

    private void init() {
        paint.setStyle(Paint.Style.FILL);

        animator.setInterpolator(interpolator);
        setWillNotDraw(false);
        animator.setInterpolator(interpolator);
        animator.addUpdateListener(animatorUpdateListener);
        animator.addListener(animatorListener);
        animator.setDuration(duration);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        notifyListener();
        startAnimator();
    }

    public void setOnCheckedChangeListener(OnCheckedChangedListener listener) {
        this.listener = listener;
    }

    public void toggle() {
        setChecked(!isChecked());
    }

    private void notifyListener() {
        if (listener != null)
            listener.onCheckedChange(this, isChecked());
    }

    public void startAnimator() {
        invalidate();
        try {
            if (animator.isRunning())
                animator.cancel();

            animator.start();
        } catch (Exception e) {
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(isChecked() ? getResources().getColor(R.color.material_green_light) : getResources().getColor(R.color.white));
        final int sweepAngle = (int) ((animated_value < 0.75f ? animated_value / 0.75f : 1) * 360);
        canvas.drawArc(rectF, -90, sweepAngle, true, paint);

        paint.setColor(getResources().getColor(R.color.dark_grey_text));
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

    public interface OnCheckedChangedListener {
        public void onCheckedChange(MaterialCheckBox materialCheckBox, boolean isChecked);
    }
}
