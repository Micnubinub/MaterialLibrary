package com.micnubinub.materiallibrary;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by root on 11/10/14.
 */
public class ActionBar extends ViewGroup {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private ImageView search, menu;
    private int textSize;
    private String text = "";
    private boolean updating = false, showing = false;
    private float animated_value = 1, currentBarWidth;
    private TextView textView;
    private Popup popup;
    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (popup != null) {
                popup.show();
            }
        }
    };


    public ActionBar(Context context) {
        super(context);
        init();
    }

    public ActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onLayout(boolean b, int i, int i2, int i3, int i4) {
        final int searchLeft = getMeasuredWidth() - menu.getMeasuredWidth() - search.getMeasuredWidth();
        final int textViewRight = textView.getMeasuredWidth();

        textView.layout(
                0,
                ((getMeasuredHeight() - textView.getMeasuredHeight()) / 2),
                textViewRight > searchLeft ? searchLeft : textViewRight,
                getMeasuredHeight() - ((getMeasuredHeight() - textView.getMeasuredHeight()) / 2));

        checkViewParams(textView);

        menu.layout(searchLeft + search.getMeasuredWidth(),
                getPaddingTop(),
                getMeasuredWidth(),
                getMeasuredHeight() - getPaddingBottom());

        search.layout(searchLeft,
                getPaddingTop(),
                searchLeft + search.getMeasuredWidth(),
                getMeasuredHeight() - getPaddingBottom());


    }

    private void checkViewParams(final View view, final int layoutWidth, final int layoutHeight) {
        Log.e("params", String.format("h, w : %d, %d", view.getLayoutParams().height, view.getLayoutParams().width));
        int width = view.getMeasuredWidth();
        int height = view.getMeasuredHeight();

        if ((width > layoutWidth) || (height > layoutHeight)) {
            view.setLayoutParams(new LayoutParams(layoutWidth, layoutHeight));
            view.invalidate();
            invalidate();
        }

    }

    private void checkViewParams(final View view) {
        final int layoutWidth = view.getRight() - view.getLeft();
        final int layoutHeight = view.getBottom() - view.getTop();

        checkViewParams(view, layoutWidth, layoutHeight);

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
        int PADDING = dpToPixels(16);
        popup = new Popup(getContext());

        textView = new TextView(getContext());
        textView.setPadding(PADDING, PADDING, PADDING, PADDING);
        textView.setTextSize(textSize);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setText(text);
        textView.setMaxLines(1);
        textView.setTextColor(getResources().getColor(R.color.white));
        textSize = textSize < 22 ? 22 : textSize;

        final LayoutParams params = new LayoutParams(dpToPixels(56), dpToPixels(56));

        search = new ImageView(getContext());
        search.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        search.setImageDrawable(getResources().getDrawable(R.drawable.search));
        search.setLayoutParams(params);
        PADDING = (int) (PADDING / 1.3f);
        search.setPadding(PADDING, PADDING, PADDING, PADDING);

        menu = new ImageView(getContext());
        menu.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        menu.setImageDrawable(getResources().getDrawable(R.drawable.menu));
        menu.setLayoutParams(params);
        PADDING = (PADDING * 3) / 2;
        menu.setPadding(PADDING, PADDING, PADDING, PADDING);
        menu.setOnClickListener(onClickListener);

        paint.setStrokeWidth(dpToPixels(4));

        addView(textView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        addView(menu);
        addView(search);

        setText("Need something?");


        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.white));


    }

    private class Popup extends PopupWindow {
        private final OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.settings:
                        Toast.makeText(getContext(), "Settings", Toast.LENGTH_SHORT).show();
                        dismiss();
                        break;
                    case R.id.logout:
                        Toast.makeText(getContext(), "Settings", Toast.LENGTH_SHORT).show();
                        dismiss();
                        break;
                    case R.id.menu:
                        dismiss();
                        break;
                }
            }
        };
        int padding = (int) ((dpToPixels(16) * 3) / 2.6f);
        private View contentView;

        public Popup(Context context) {
            super(context);
            contentView = View.inflate(getContext(), R.layout.popup_menu, null);
            contentView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            final ImageView imageView = (ImageView) contentView.findViewById(R.id.menu);
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.menu_selected));
            imageView.setOnClickListener(listener);
            contentView.findViewById(R.id.logout).setOnClickListener(listener);
            contentView.findViewById(R.id.settings).setOnClickListener(listener);

            this.setWidth(dpToPixels(110));
            this.setHeight(dpToPixels(56 * 3));
            this.setBackgroundDrawable(getResources().getDrawable(R.drawable.white_background));
            setContentView(contentView);
            this.setOutsideTouchable(true);

        }

        public void show() {
            showAsDropDown(menu, 0, -dpToPixels(56));
        }


    }
}