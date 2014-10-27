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
public class MaterialSingleLineTextAvatarWithIcon extends ViewGroup {
    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final AccelerateInterpolator interpolator = new AccelerateInterpolator();
    private static int duration = 600;
    private final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
    private TextView textView;
    private ImageView avatar, icon;
    private int width;
    private int height;
    private float animated_value = 0;
    private float scaleTo = 1.065f;
    private int clickedX, clickedY;
    private boolean touchDown = false, animateRipple;
    private float ripple_animated_value = 0;
    private final ValueAnimator.AnimatorUpdateListener animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            animated_value = (Float) (animation.getAnimatedValue());
            ripple_animated_value = animated_value;
            invalidatePoster();
        }
    };
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
    private int rippleColor = 0x25000000;

    public MaterialSingleLineTextAvatarWithIcon(Context context) {
        super(context);
        init();
    }

    public MaterialSingleLineTextAvatarWithIcon(Context context, AttributeSet attrs) {
        super(context, attrs);

        try {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaterialTwoLineText, 0, 0);
            setTextColor(a.getInteger(R.attr.textColor, getResources().getColor(R.color.dark_grey)));
            setTextSize(a.getInteger(R.attr.primaryTextSize, 16));
            setTextMaxLines(a.getInteger(R.attr.secondaryTextMaxLines, 1));
            a.recycle();
        } catch (Exception ignored) {
        }

        init();

    }


    public MaterialSingleLineTextAvatarWithIcon(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        try {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaterialTwoLineText, 0, 0);
            setTextColor(a.getInteger(R.attr.secondaryTextColor, getResources().getColor(R.color.dark_grey)));
            setTextSize(a.getInteger(R.attr.primaryTextSize, 16));
            setTextMaxLines(a.getInteger(R.attr.secondaryTextMaxLines, 1));

            a.recycle();
        } catch (Exception ignored) {
        }

        init();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                clickedX = (int) event.getX();
                clickedY = (int) event.getY();
                rippleR = (int) (Math.sqrt(Math.pow(Math.max(width - clickedX, clickedX), 2) + Math.pow(Math.max(height - clickedY, clickedY), 2)) * 1.15);

                animator.start();

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
        textView.setText(text);
        invalidatePoster();
    }

    public int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private void init() {
        //Todo consider 16 and 14 (in the guidelines)
        final int padding = dpToPixels(16);
        final int imageWidth = dpToPixels(72);


        textView = new TextView(getContext());
        textView.setTextColor(getResources().getColor(R.color.dark_grey));
        textView.setTextSize(18);
        textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        textView.setMaxLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setPadding(padding, padding, padding, padding);


        avatar = new ImageView(getContext());
        avatar.setLayoutParams(new LayoutParams(imageWidth, imageWidth));
        avatar.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        avatar.setPadding(padding, padding, padding, padding);

        final int iconPadding = dpToPixels(4);
        final int iconWidth = dpToPixels(48);
        icon = new ImageView(getContext());
        icon.setLayoutParams(new LayoutParams(iconWidth, iconWidth));
        icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        icon.setPadding(iconPadding, iconPadding, iconPadding, iconPadding);

        setWillNotDraw(false);
        animator.setInterpolator(interpolator);
        animator.addUpdateListener(animatorUpdateListener);
        animator.addListener(animatorListener);
        animator.setDuration(duration);
        paint.setColor(0x25000000);


        addView(textView);
        addView(avatar);
        addView(icon);
    }

    public void setLeftIcon(Drawable leftIcon) {
        if (avatar != null)
            avatar.setImageBitmap(getCircleBitmap(leftIcon, dpToPixels(40)));
    }

    public void setLeftIcon(Bitmap leftIcon) {
        if (avatar != null)
            avatar.setImageBitmap(getCircleBitmap(leftIcon, dpToPixels(40)));
    }

    public void setRightIcon(Drawable rightIcon) {
        if (icon != null)
            icon.setImageDrawable(rightIcon);
    }

    public void setRightIcon(Bitmap icon) {
        if (this.icon != null)
            this.icon.setImageBitmap(icon);
    }

    public void setTextColor(int color) {
        textView.setTextColor(color);
    }

    public void setTextMaxLines(int maxLines) {
        textView.setMaxLines(maxLines);
    }

    public void setTextSize(int sp) {
        textView.setTextSize(sp);
    }

    private void scale(final float scale) {
        post(new Runnable() {
            @Override
            public void run() {
                MaterialSingleLineTextAvatarWithIcon.this.setScaleX(scale);
                MaterialSingleLineTextAvatarWithIcon.this.setScaleY(scale);
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
        MaterialSingleLineTextAvatarWithIcon.duration = duration;
        animator.setDuration(duration);
    }

    public void setScaleTo(float scaleTo) {
        this.scaleTo = scaleTo;
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
    public void addView(View child, int index, LayoutParams params) {
        if (getChildCount() >= 4)
            return;
        super.addView(child, index, params);
    }


    @Override
    protected void onSizeChanged(final int w, final int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
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
        final int avatarPaddingTop = (getMeasuredHeight() - avatar.getMeasuredHeight()) / 2;
        avatar.layout(avatarPaddingTop,
                getPaddingTop(),
                getPaddingLeft() + avatar.getMeasuredWidth(),
                getMeasuredHeight() - avatarPaddingTop
        );

        final int textViewPaddingTop = (getMeasuredHeight() - textView.getMeasuredHeight()) / 2;
        textView.layout(getPaddingLeft() + avatar.getMeasuredWidth(),
                textViewPaddingTop,
                getMeasuredWidth() - getPaddingRight() - icon.getMeasuredWidth(),
                getMeasuredHeight() - textViewPaddingTop
        );

        checkViewParams(textView);

        icon.layout(getMeasuredWidth() - icon.getMeasuredWidth() - getPaddingRight(),
                getPaddingTop(),
                getMeasuredWidth() - getPaddingRight(),
                getMeasuredHeight() - getPaddingBottom());


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
