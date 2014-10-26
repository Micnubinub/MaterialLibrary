package com.micnubinub.materiallibrary;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by root on 20/10/14.
 */
public class MaterialSingleLineTextIcon extends ViewGroup {
    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final AccelerateInterpolator interpolator = new AccelerateInterpolator();
    private static int duration = 750;
    private final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
    private TextView textView;
    private ImageView imageView;
    private int secondaryTextSize, primaryTextSize,
            primaryTextColor, secondaryTextColor, secondaryTextMaxLines;
    private String text;
    private long tic;
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
    private boolean touchDown = false, animateRipple;
    private float ripple_animated_value = 0;
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
    private int rippleR;
    private int rippleColor = 0x25000000;

    public MaterialSingleLineTextIcon(Context context) {
        super(context);
        init();
    }

    public MaterialSingleLineTextIcon(Context context, AttributeSet attrs) {
        super(context, attrs);

        try {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaterialTwoLineText, 0, 0);
            setPrimaryTextColor(a.getInteger(R.attr.primaryTextColor, getResources().getColor(R.color.dark_dark_grey)));
            setPrimaryTextSize(a.getInteger(R.attr.primaryTextSize, 18));
            text = a.getString(R.attr.primaryText);
            a.recycle();
        } catch (Exception e) {
        }

        if (text == null)
            text = "";

        init();

        setText(text);
    }


    public MaterialSingleLineTextIcon(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        try {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaterialTwoLineText, 0, 0);
            setPrimaryTextColor(a.getInteger(R.attr.primaryTextColor, getResources().getColor(R.color.dark_dark_grey)));
            setPrimaryTextSize(a.getInteger(R.attr.primaryTextSize, 18));

            a.recycle();
        } catch (Exception e) {
        }

        if (text == null)
            text = "empty";

        init();

        setText(text);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                clickedX = (int) event.getX();
                clickedY = (int) event.getY();
                rippleR = (int) (Math.sqrt(Math.pow(Math.max(width - clickedX, clickedX), 2) + Math.pow(Math.max(height - clickedY, clickedY), 2)) * 1.15);

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

    public void setText(String text) {
        this.text = text;
        textView.setText(text);
    }

    public int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private void init() {
        //Todo consider 16 and 14 (in the guidelines)
        final int padding = dpToPixels(16);
        final int imageWidth = dpToPixels(72);
        final LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        textView = new TextView(getContext());
        textView.setTextColor(getResources().getColor(R.color.dark_dark_grey));
        textView.setTextSize(1);
        textView.setMaxLines(1);
        textView.setLayoutParams(params);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setPadding(padding, padding, padding, padding);


        imageView = new ImageView(getContext());
        imageView.setLayoutParams(new LayoutParams(imageWidth, imageWidth));
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setPadding(padding, padding, padding, padding);

        setIcon(getResources().getDrawable(R.drawable.test));
        textView.setText("Primary");

        setWillNotDraw(false);
        animator.setInterpolator(interpolator);
        animator.addUpdateListener(animatorUpdateListener);
        animator.addListener(animatorListener);
        animator.setDuration(duration);
        paint.setColor(0x25000000);


        addView(textView);
        addView(imageView);
    }

    public void setIcon(Drawable icon) {
        if (imageView != null)
            imageView.setImageDrawable(icon);
    }

    public void setIcon(Bitmap icon) {
        if (imageView != null)
            imageView.setImageBitmap(icon);
    }

    public void setPrimaryTextSize(int sp) {
        primaryTextSize = sp;
        if (textView != null)
            textView.setTextSize(sp);
    }

    public void setPrimaryTextColor(int color) {
        primaryTextColor = color;
        if (textView != null)
            textView.setTextColor(color);
    }

    private void scale(final float scale) {
        post(new Runnable() {
            @Override
            public void run() {
                MaterialSingleLineTextIcon.this.setScaleX(scale);
                MaterialSingleLineTextIcon.this.setScaleY(scale);
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
        }, 175);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        return false;

    }

    public void setDuration(int duration) {
        MaterialSingleLineTextIcon.duration = duration;
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
            }
        });
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
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    @Override
    public void addView(View child, int index, LayoutParams params) {
        if (getChildCount() >= 3)
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
        this.setPivotX(w / 2);
        this.setPivotY(h / 2);
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
        final int imageViewPadding = (getMeasuredHeight() - imageView.getMeasuredHeight()) / 2;
        imageView.layout(imageViewPadding,
                getPaddingTop(),
                getPaddingLeft() + imageView.getMeasuredWidth(),
                getMeasuredHeight() - imageViewPadding
        );

        final int textViewPadding = (getMeasuredHeight() - textView.getMeasuredHeight()) / 2;
        textView.layout(getPaddingLeft() + imageView.getMeasuredWidth(), textViewPadding,
                getMeasuredWidth() - getPaddingRight(),
                getMeasuredHeight() - textViewPadding);

        checkViewParams(textView);
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
/*
    public Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) // Don't do anything without a proper drawable
            return null;
        else if (drawable instanceof BitmapDrawable) // Use the getBitmap() method instead if BitmapDrawable
            return ((BitmapDrawable) drawable).getBitmap();
// Create Bitmap object out of the drawable
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
// Return the created Bitmap
        return bitmap;
    }


    public static Bitmap getCircleBitmap(Resources res, int sourceResId, int width) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeResource(res, sourceResId, options);
        bitmap.setHasAlpha(true);
        bitmap = Bitmap.createScaledBitmap(bitmap, width, width, true);
        Bitmap output = Bitmap.createBitmap(width,
                width, Bitmap.Config.ARGB_8888);
        final Rect rect = new Rect(0, 0, width,
                width);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xff000000);
        canvas.drawCircle(width / 2, width / 2, width / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        bitmap.recycle();
        return output;
    }
    */
}
