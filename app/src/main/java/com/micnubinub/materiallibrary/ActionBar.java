package com.micnubinub.materiallibrary;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by root on 11/10/14.
 */
public class ActionBar extends ViewGroup {
    public static final int MODE_BACK_BUTTON = 0;
    public static final int MODE_RIBBON_MENU = 1;
    private static final DecelerateInterpolator interpolator = new DecelerateInterpolator();
    private final ValueAnimator animator = ValueAnimator.ofFloat(0.35f, 1);
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int textColor;
    private ImageView search, menu;
    private int mode;
    private int textSize;
    private String text = "";
    private boolean updating = false, showing = false;
    private View view;
    private float animated_value = 1, currentBarWidth;
    private TextView textView;
    private OnMenuBackButtonClicked onMenuBackButtonClicked;
    private OnClickListener showSideMenu;
    private final OnClickListener l = new OnClickListener() {
        @Override
        public void onClick(View v) {
            showing = !showing;

            if (mode == 1)
                ((MenuBars) view).animateBars();

            if (onMenuBackButtonClicked != null)
                onMenuBackButtonClicked.viewClicked(showing);

            if (showSideMenu != null)
                showSideMenu.onClick(menu);
        }
    };

    public ActionBar(Context context) {
        super(context);
        init();
    }

    public ActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaterialRadioButton, 0, 0);
        text = a.getString(R.styleable.MaterialRadioButton_text);
        textSize = a.getInt(R.styleable.MaterialRadioButton_textSize, 22);
        mode = a.getInt(R.styleable.MaterialRadioButton_mode, 1);
        textColor = getResources().getColor(R.color.white);
        a.recycle();
        textSize = textSize < 20 ? 20 : textSize;
        init();
    }

    private int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onLayout(boolean b, int i, int i2, int i3, int i4) {
        final int searchLeft = getMeasuredWidth() - menu.getMeasuredWidth() - search.getMeasuredWidth();

        view.layout(
                0,
                ((getMeasuredHeight() - view.getMeasuredHeight()) / 2),
                view.getMeasuredWidth(),
                getMeasuredHeight() - ((getMeasuredHeight() - view.getMeasuredHeight()) / 2)
        );

        final int textViewRight = view.getMeasuredWidth() + textView.getMeasuredWidth();

        textView.layout(
                view.getMeasuredWidth(),
                ((getMeasuredHeight() - textView.getMeasuredHeight()) / 2),
                textViewRight > searchLeft ? searchLeft : textViewRight,
                getMeasuredHeight() - ((getMeasuredHeight() - textView.getMeasuredHeight()) / 2));


        menu.layout(searchLeft + search.getMeasuredWidth(),
                getPaddingTop(),
                getMeasuredWidth(),
                getMeasuredHeight() - getPaddingBottom());

        search.layout(searchLeft,
                getPaddingTop(),
                searchLeft + search.getMeasuredWidth(),
                getMeasuredHeight() - getPaddingBottom());


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int measuredWidth = 0;
        int measuredHeight = 0;

        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            measuredWidth += child.getMeasuredWidth();
            measuredHeight = Math.max(measuredHeight, child.getMeasuredHeight());
        }

        setMeasuredDimension(resolveSizeAndState(measuredWidth, widthMeasureSpec, 0),
                resolveSizeAndState(measuredHeight, heightMeasureSpec, 0));

    }

    public boolean isShowing() {
        return !showing;
    }

    public void setUpdating(boolean updating) {
        this.updating = updating;
    }

    public void setText(String text) {
        textView.setText(text);
    }

    private void init() {
        final int PADDING = dpToPixels(12);

        textView = new TextView(getContext());
        textView.setPadding(PADDING, PADDING, PADDING, PADDING);
        textView.setTextColor(getResources().getColor(R.color.white));
        textView.setTextSize(textSize);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setText(text);
        textView.setMaxLines(1);
        textView.setTextColor(getResources().getColor(R.color.white));

        final LayoutParams params = new LayoutParams(dpToPixels(72), dpToPixels(72));

        search = new ImageView(getContext());
        search.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        search.setImageDrawable(getResources().getDrawable(R.drawable.back));
        search.setLayoutParams(params);
        search.setPadding(PADDING, PADDING, PADDING, PADDING);

        menu = new ImageView(getContext());
        menu.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        menu.setImageDrawable(getResources().getDrawable(R.drawable.block));
        menu.setLayoutParams(params);
        menu.setPadding(PADDING, PADDING, PADDING, PADDING);

        paint.setStrokeWidth(dpToPixels(4));

        addView(textView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        addView(menu);
        addView(search);

        setText(text);


        view = new MenuBars(getContext());
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.white));

        animator.setInterpolator(interpolator);
        animator.setDuration(450);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animated_value = ((Float) (animation.getAnimatedValue())).floatValue();
                view.invalidate();
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
        final int width = dpToPixels(45);
        view.setLayoutParams(new LayoutParams((width * 4) / 7, width));

        addView(view);
        view.setOnClickListener(l);
        textView.setOnClickListener(l);
    }

    public void setOnMenuBackButtonClicked(OnMenuBackButtonClicked onMenuBackButtonClicked) {
        this.onMenuBackButtonClicked = onMenuBackButtonClicked;
    }

    public void setOnMenuButtonClickedListener(OnClickListener l) {
        menu.setOnClickListener(l);
    }

    public void setOnSearchButtonClickedListener(OnClickListener l) {
        search.setOnClickListener(l);
    }

    public void setOnSideMenuButtonClickedListener(OnClickListener l) {
        showSideMenu = l;
    }

    public interface OnMenuBackButtonClicked {
        public void viewClicked(boolean show);
    }

    private class MenuBars extends View {
        private int width;
        private int[] linePs = new int[3];

        public MenuBars(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (isShowing())
                show(canvas);
            else
                hide(canvas);

            if (updating)
                invalidate();

        }

        private void hide(Canvas canvas) {
            currentBarWidth = width * (1 - animated_value + 0.35f);
            canvas.drawLine(0, linePs[0], currentBarWidth, linePs[0], paint);
            canvas.drawLine(0, linePs[1], currentBarWidth, linePs[1], paint);
            canvas.drawLine(0, linePs[2], currentBarWidth, linePs[2], paint);
        }

        private void show(Canvas canvas) {
            currentBarWidth = width * animated_value;
            canvas.drawLine(0, linePs[0], currentBarWidth, linePs[0], paint);
            canvas.drawLine(0, linePs[1], currentBarWidth, linePs[1], paint);
            canvas.drawLine(0, linePs[2], currentBarWidth, linePs[2], paint);
        }


        public void animateBars() {
            invalidate();
            try {
                if (animator.isRunning())
                    animator.cancel();

                animator.start();
            } catch (Exception e) {
            }

        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            width = w;
            calculateLinePos(h);
        }

        private void calculateLinePos(int h) {

            linePs[0] = h / 4;
            linePs[1] = h / 2;
            linePs[2] = 3 * h / 4;
        }


        @Override
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            if (isShowing())
                animated_value = 1;
        }
    }


}