package com.micnubinub.materiallibrary;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
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
 * Created by root on 21/10/14.
 */
public class MaterialSingleLineTextAvatar extends ViewGroup {
    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final AccelerateInterpolator interpolator = new AccelerateInterpolator();
    private static int duration = 750;
    private final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
    private TextView textView;
    private ImageView imageView;
    private int textSize, textColor, secondaryTextMaxLines;
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
    private boolean touchDown = false;
    private ValueAnimator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
            tic = System.currentTimeMillis();
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            if (!touchDown)
                animated_value = 0;

            invalidatePoster();
        }

        @Override
        public void onAnimationCancel(Animator animator) {


        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }
    };

    public MaterialSingleLineTextAvatar(Context context) {
        super(context);
        init();
    }

    public MaterialSingleLineTextAvatar(Context context, AttributeSet attrs) {
        super(context, attrs);

        try {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaterialTwoLineText, 0, 0);
            setTextColor(a.getInteger(R.attr.primaryTextColor, getResources().getColor(R.color.dark_dark_grey)));
            setTextSize(a.getInteger(R.attr.primaryTextSize, 18));
            setTextSize(a.getInteger(R.attr.primaryTextSize, 16));
            text = a.getString(R.attr.primaryText);
            a.recycle();
        } catch (Exception e) {
        }
        init();

        if (text == null)
            text = "";

        init();

        setText(text);
    }


    public MaterialSingleLineTextAvatar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        try {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaterialTwoLineText, 0, 0);
            setTextColor(a.getInteger(R.attr.primaryTextColor, getResources().getColor(R.color.dark_dark_grey)));
            setTextSize(a.getInteger(R.attr.primaryTextSize, 16));
            a.recycle();
        } catch (Exception e) {
        }

        if (text == null)
            text = "";

        init();

        setText(text);

    }

    public void setText(String text) {
        this.text = text;
        textView.setText(text);
    }

    public int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }


    private void init() {
        final int padding = dpToPixels(16);
        final int imageWidth = dpToPixels(72);
        final LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        textView = new TextView(getContext());
        textView.setTextColor(getResources().getColor(R.color.dark_dark_grey));
        textView.setTextSize(18);
        textView.setMaxLines(1);
        textView.setLayoutParams(params);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setPadding(padding, padding / 2, padding, padding / 2);


        imageView = new ImageView(getContext());
        imageView.setLayoutParams(new LayoutParams(imageWidth, imageWidth));
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setPadding(padding, padding, padding, padding);

        setIcon(getResources().getDrawable(R.drawable.test));
        textView.setText("Primary this is a two line text test");

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
            imageView.setImageBitmap(getCircleBitmap(icon, dpToPixels(40)));
    }

    public void setIcon(Bitmap icon) {
        if (imageView != null)
            imageView.setImageBitmap(getCircleBitmap(icon, dpToPixels(40)));
    }

    public void setTextSize(int sp) {
        textSize = sp;
        if (textView != null)
            textView.setTextSize(sp);
    }


    public void setTextColor(int color) {
        textColor = color;
        if (textView != null)
            textView.setTextColor(color);
    }


    private void scale(final float scale) {
        post(new Runnable() {
            @Override
            public void run() {
                MaterialSingleLineTextAvatar.this.setScaleX(scale);
                MaterialSingleLineTextAvatar.this.setScaleY(scale);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (scaleOnTouch)
                    scaleLater();

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

                if (scaleOnTouch)
                    scale(1);

                touchDown = false;

                if (!animator.isRunning()) {
                    animated_value = 0;
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
        MaterialSingleLineTextAvatar.duration = duration;
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

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.drawCircle(clickedX, clickedY, r * animated_value, paint);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    @Override
    public void addView(View child, int index, LayoutParams params) {
        if (getChildCount() >= 2)
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
            measuredHeight = Math.max(child.getMeasuredHeight(), measuredHeight);
            measuredWidth = Math.max(child.getMeasuredWidth(), measuredWidth);
        }

        setMeasuredDimension(resolveSizeAndState(measuredWidth, widthMeasureSpec, 0),
                resolveSizeAndState(measuredHeight, heightMeasureSpec, 0));


    }

    @Override
    protected void onLayout(boolean b, int i, int i2, int i3, int i4) {
        final int imageViewPaddingTop = (getMeasuredHeight() - imageView.getMeasuredHeight()) / 2;
        imageView.layout(getPaddingLeft(),
                imageViewPaddingTop,
                getPaddingLeft() + imageView.getMeasuredWidth(),
                getMeasuredHeight() - imageViewPaddingTop
        );

        //Todo check alll the views for t(+get PaddingTiop)
        final int textViewPaddingTop = (getMeasuredHeight() - textView.getMeasuredHeight()) / 2;
        textView.layout(getPaddingLeft() + imageView.getMeasuredWidth(), textViewPaddingTop,
                getMeasuredWidth() - getPaddingRight(),
                textView.getMeasuredHeight() - textViewPaddingTop);

        checkViewParams(textView);
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
        return bitmap;
    }

    public Bitmap getCircleBitmap(final Drawable drawable, final int width) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = drawableToBitmap(drawable);

        return getCircleBitmap(bitmap, width);
    }

    public Bitmap getCircleBitmap(Bitmap bitmap, final int width) {
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


}
