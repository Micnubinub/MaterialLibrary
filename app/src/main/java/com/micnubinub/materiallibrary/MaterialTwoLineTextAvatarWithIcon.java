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
import android.graphics.Typeface;
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
public class MaterialTwoLineTextAvatarWithIcon extends ViewGroup {
    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final AccelerateInterpolator interpolator = new AccelerateInterpolator();
    private static int duration = 750;
    private final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
    private TextView primaryTextView, secondaryTextView;
    private ImageView avatar, icon;
    private int secondaryTextSize, primaryTextSize,
            primaryTextColor, secondaryTextColor, secondaryTextMaxLines;
    private String primaryText, secondaryText;
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

    public MaterialTwoLineTextAvatarWithIcon(Context context) {
        super(context);
        init();
    }

    public MaterialTwoLineTextAvatarWithIcon(Context context, AttributeSet attrs) {
        super(context, attrs);

        try {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaterialTwoLineText, 0, 0);
            setPrimaryTextColor(a.getInteger(R.attr.primaryTextColor, getResources().getColor(R.color.dark_dark_grey)));
            setSecondaryTextColor(a.getInteger(R.attr.secondaryTextColor, getResources().getColor(R.color.dark_grey)));
            setPrimaryTextSize(a.getInteger(R.attr.primaryTextSize, 18));
            setSecondaryTextSize(a.getInteger(R.attr.primaryTextSize, 16));
            setSecondaryTextMaxLines(a.getInteger(R.attr.secondaryTextMaxLines, 1));
            secondaryText = a.getString(R.attr.secondaryText);
            primaryText = a.getString(R.attr.primaryText);
            a.recycle();
        } catch (Exception e) {
        }

        if (secondaryText == null)
            secondaryText = "";

        if (primaryText == null)
            primaryText = "";

        init();

        setPrimaryText(primaryText);
        setSecondaryText(secondaryText);
    }


    public MaterialTwoLineTextAvatarWithIcon(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        try {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaterialTwoLineText, 0, 0);
            setPrimaryTextColor(a.getInteger(R.attr.primaryTextColor, getResources().getColor(R.color.dark_dark_grey)));
            setSecondaryTextColor(a.getInteger(R.attr.secondaryTextColor, getResources().getColor(R.color.dark_grey)));
            setPrimaryTextSize(a.getInteger(R.attr.primaryTextSize, 18));
            setSecondaryTextSize(a.getInteger(R.attr.primaryTextSize, 16));
            setSecondaryTextMaxLines(a.getInteger(R.attr.secondaryTextMaxLines, 1));

            a.recycle();
        } catch (Exception e) {
        }

        if (secondaryText == null)
            secondaryText = "";

        if (primaryText == null)
            primaryText = "";

        init();

        setPrimaryText(primaryText);
        setSecondaryText(secondaryText);
    }

    public void setPrimaryText(String text) {
        primaryText = text;
        primaryTextView.setText(text);
    }

    public void setSecondaryText(String text) {
        secondaryText = text;
        secondaryTextView.setText(text);
    }

    public int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }


    private void init() {
        //Todo consider 16 and 14 (in the guidelines)
        final int padding = dpToPixels(16);
        final int imageWidth = dpToPixels(72);
        final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        primaryTextView = new TextView(getContext());
        primaryTextView.setTextColor(getResources().getColor(R.color.dark_dark_grey));
        primaryTextView.setTypeface(null, Typeface.BOLD);
        primaryTextView.setTextSize(18);
        primaryTextView.setMaxLines(1);
        primaryTextView.setLayoutParams(params);
        primaryTextView.setEllipsize(TextUtils.TruncateAt.END);
        primaryTextView.setPadding(padding, padding / 2, padding, padding / 2);

        secondaryTextView = new TextView(getContext());
        secondaryTextView.setTextColor(getResources().getColor(R.color.dark_grey));
        secondaryTextView.setTextSize(16);
        secondaryTextView.setMaxLines(1);
        secondaryTextView.setLayoutParams(params);
        secondaryTextView.setEllipsize(TextUtils.TruncateAt.END);
        secondaryTextView.setPadding(padding, padding / 2, padding, padding);


        avatar = new ImageView(getContext());
        avatar.setLayoutParams(new LayoutParams(imageWidth, imageWidth));
        avatar.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        avatar.setPadding(padding, padding, padding, padding);

        icon = new ImageView(getContext());
        icon.setLayoutParams(new LayoutParams(imageWidth, imageWidth));
        icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        icon.setPadding(padding, padding, padding, padding);

        setLeftIcon(getResources().getDrawable(R.drawable.test));
        setRightIcon(getResources().getDrawable(R.drawable.test));

        setWillNotDraw(false);
        animator.setInterpolator(interpolator);
        animator.addUpdateListener(animatorUpdateListener);
        animator.addListener(animatorListener);
        animator.setDuration(duration);
        paint.setColor(0x25000000);


        addView(primaryTextView);
        addView(secondaryTextView);
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

    public void setSecondaryTextColor(int color) {
        secondaryTextColor = color;
        if (secondaryTextView != null)
            secondaryTextView.setTextColor(color);
    }

    public void setSecondaryTextMaxLines(int maxLines) {
        secondaryTextMaxLines = maxLines;
        if (secondaryTextView != null)
            secondaryTextView.setMaxLines(maxLines);
    }

    public void setSecondaryTextSize(int sp) {
        secondaryTextSize = sp;
        if (secondaryTextView != null)
            secondaryTextView.setTextSize(sp);
    }

    public void setPrimaryTextSize(int sp) {
        primaryTextSize = sp;
        if (primaryTextView != null)
            primaryTextView.setTextSize(sp);
    }

    public void setPrimaryTextColor(int color) {
        primaryTextColor = color;
        if (primaryTextView != null)
            primaryTextView.setTextColor(color);
    }


    private void scale(final float scale) {
        post(new Runnable() {
            @Override
            public void run() {
                MaterialTwoLineTextAvatarWithIcon.this.setScaleX(scale);
                MaterialTwoLineTextAvatarWithIcon.this.setScaleY(scale);
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
        MaterialTwoLineTextAvatarWithIcon.duration = duration;
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
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() >= 4)
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
        int measuredWidth = 0;

        for (int i = 0; i < getChildCount(); i++) {

            final View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            measuredWidth = Math.max(child.getMeasuredWidth(), measuredWidth);
        }

        final int measuredHeight = Math.max(Math.max(avatar.getMeasuredHeight(), primaryTextView.getMeasuredHeight() + secondaryTextView.getMeasuredHeight()), icon.getMeasuredHeight());

        setMeasuredDimension(resolveSizeAndState(measuredWidth, widthMeasureSpec, 0),
                resolveSizeAndState(measuredHeight, heightMeasureSpec, 0));


    }

    @Override
    protected void onLayout(boolean b, int i, int i2, int i3, int i4) {
        avatar.layout(getPaddingLeft(),
                getPaddingTop(),
                getPaddingLeft() + avatar.getMeasuredWidth(),
                getMeasuredHeight() - getPaddingBottom()
        );

        primaryTextView.layout(getPaddingLeft() + avatar.getMeasuredWidth(), getPaddingTop(),
                getMeasuredWidth() - getPaddingRight() - icon.getMeasuredWidth(),
                primaryTextView.getMeasuredHeight() + getPaddingTop());

        checkViewParams(primaryTextView);

        secondaryTextView.layout(getPaddingLeft() + avatar.getMeasuredWidth(),
                getMeasuredHeight() - getPaddingTop() - secondaryTextView.getMeasuredHeight(),
                getMeasuredWidth() - getPaddingRight() - icon.getMeasuredWidth(),
                getMeasuredHeight() - getPaddingBottom()
        );

        checkViewParams(secondaryTextView);

        icon.layout(getMeasuredWidth() - icon.getMeasuredWidth() - getPaddingRight(),
                getPaddingTop(),
                getMeasuredWidth() - getPaddingRight(),
                getMeasuredHeight() - getPaddingBottom());


    }

    private void checkViewParams(final View view, final int layoutWidth, final int layoutHeight) {
        int width = view.getMeasuredWidth();
        int height = view.getMeasuredHeight();

        if ((width > layoutWidth) || (height > layoutHeight)) {
            view.setLayoutParams(new LayoutParams(layoutWidth, layoutHeight));
            view.invalidate();
            requestLayout();
        }

    }

    private void checkViewParams(final View view) {
        final int layoutWidth = view.getLeft() - view.getRight();
        final int layoutHeight = view.getTop() - view.getBottom();

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
