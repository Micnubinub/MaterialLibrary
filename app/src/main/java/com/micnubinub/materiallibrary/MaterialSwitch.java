package com.micnubinub.materiallibrary;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;


/**
 * Created by root on 24/08/14.
 */
public class MaterialSwitch extends ViewGroup {
    private static int PADDING = 10;
    private static Resources res;
    private static int duration = 600;
    private final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
    private final DecelerateInterpolator interpolator = new DecelerateInterpolator();
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected boolean checked = false;
    private int textSize;
    private String text = "";
    private Switch materialSwitch;
    private final OnClickListener l = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (materialSwitch != null)
                toggle();
        }
    };
    private float line_pos;
    private OnCheckedChangedListener listener;
    private int r, color_on, color_off, hole_r, color_hole;
    private boolean updating = false;
    private float animated_value = 0;
    private TextView textView;

    public MaterialSwitch(Context context) {
        super(context);
        init(context);
    }

    public MaterialSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaterialRadioButton, 0, 0);
        setChecked(a.getBoolean(R.styleable.MaterialRadioButton_checked, false));
        text = a.getString(R.styleable.MaterialRadioButton_text);
        textSize = a.getInt(R.styleable.MaterialRadioButton_textSize, 20);
        a.recycle();
        textSize = textSize < 20 ? 20 : textSize;
        init(context);
    }

    public static int dpToPixels(int dp, Resources res) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
    }

    @Override
    protected void onLayout(boolean b, int i, int i2, int i3, int i4) {
        materialSwitch.layout(
                getMeasuredWidth() - materialSwitch.getMeasuredWidth() - getPaddingRight(),
                getPaddingTop() + ((getMeasuredHeight() - materialSwitch.getMeasuredHeight()) / 2),
                getMeasuredWidth() - getPaddingRight(),
                getPaddingBottom() + getMeasuredHeight() - ((getMeasuredHeight() - materialSwitch.getMeasuredHeight()) / 2)
        );

        if (textView != null) {
            textView.layout(
                    getPaddingLeft(),
                    getPaddingTop() + ((getMeasuredHeight() - textView.getMeasuredHeight()) / 2),
                    getMeasuredWidth() - materialSwitch.getMeasuredWidth() - getPaddingRight(),
                    getMeasuredHeight() - ((getMeasuredHeight() - textView.getMeasuredHeight()) / 2)
            );

            checkViewParams(textView);
        }


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
        } catch (Exception w) {
        }

        setMeasuredDimension(resolveSizeAndState(measuredWidth, widthMeasureSpec, 0),
                resolveSizeAndState(measuredHeight, heightMeasureSpec, 0));

    }

    public void setOnCheckedChangeListener(OnCheckedChangedListener listener) {
        this.listener = listener;
    }

    public void setText(String text) {
        if (text != null && text.length() > 0) {

            if (textView == null)
                textView = new TextView(getContext());

            textView.setTextSize(textSize);
            textView.setTextColor(res.getColor(R.color.dark_grey_text));
            textView.setPadding(PADDING, PADDING, PADDING, PADDING);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setText(text);

            if (indexOfChild(textView) >= 0)
                removeView(textView);

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

    private void init(Context context) {
        res = context.getResources();
        setOffColor(res.getColor(R.color.lite_grey));
        setOnColor(res.getColor(R.color.material_green_light));
        setHoleColor(res.getColor(R.color.white));
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(color_off);

        PADDING = dpToPixels(3, res);

        materialSwitch = new Switch(context);

        materialSwitch.setLayoutParams(new LayoutParams(dpToPixels(35, res), dpToPixels(25, res)));
        materialSwitch.setPadding(PADDING, PADDING, PADDING, PADDING);
        setPadding(PADDING, PADDING, PADDING, PADDING);

        addView(materialSwitch);
        materialSwitch.invalidate();

        setText(text);

        animator.setInterpolator(interpolator);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animated_value = ((Float) (animation.getAnimatedValue())).floatValue();
                materialSwitch.invalidate();
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

    private void checkViewParams(final View view, final int layoutWidth, final int layoutHeight) {
        int width = view.getMeasuredWidth();
        int height = view.getMeasuredHeight();

        if ((width > layoutWidth) || (height > layoutHeight)) {
            view.setLayoutParams(new LayoutParams(layoutWidth, layoutHeight));
            view.invalidate();
        }

    }

    private void checkViewParams(final View view) {
        final int layoutWidth = view.getLeft() - view.getRight();
        final int layoutHeight = view.getTop() - view.getBottom();

        int width = view.getMeasuredWidth();
        int height = view.getMeasuredHeight();

        if ((width > layoutWidth) || (height > layoutHeight)) {
            view.setLayoutParams(new LayoutParams(layoutWidth, layoutHeight));
            view.invalidate();
        }
    }

    public void animateSwitch() {
        invalidate();
        try {
            if (animator.isRunning())
                animator.cancel();

            animator.start();
        } catch (Exception e) {
        }

    }


    public interface OnCheckedChangedListener {
        public void onCheckedChange(MaterialSwitch materialSwitch, boolean isChecked);
    }

    private class Switch extends View {
        private int width;

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
            canvas.drawLine(getPaddingLeft(), line_pos, (float) (width - getPaddingRight()), line_pos, paint);
            canvas.drawCircle((r + (animated_value * (width - r - r))), line_pos, r, paint);

            setPaintColor(color_hole);
            if (updating)
                canvas.drawCircle((r + (animated_value * (width - r - r))), line_pos, hole_r * (1 - animated_value), paint);
            else {
            }

        }

        private void animateOff(Canvas canvas) {
            setPaintColor(color_off);
            canvas.drawLine(getPaddingLeft(), line_pos, (float) (width - getPaddingRight()), line_pos, paint);
            canvas.drawCircle((r + ((1 - animated_value) * (width - r - r))), line_pos, r, paint);

            setPaintColor(color_hole);

            if (updating)
                canvas.drawCircle((r + ((1 - animated_value) * (width - r - r))), line_pos, hole_r * animated_value, paint);
            else
                canvas.drawCircle((r + ((1 - animated_value) * (width - r - r))), line_pos, hole_r, paint);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            paint.setStrokeWidth(0.15f * Math.min(w, h));
            line_pos = h / 2;
            r = Math.min((w - getPaddingLeft() - getPaddingRight()), (h - getPaddingBottom() - getPaddingTop())) / 2;
            hole_r = (int) (r * 0.85f);
            width = w;
        }

        @Override
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            if (isChecked())
                animated_value = 1;
        }

    }

}
