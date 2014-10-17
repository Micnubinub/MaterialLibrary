package com.micnubinub.materiallibrary;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by root on 24/08/14.
 */
public class MaterialSeekBar extends View {
    private static Resources res;
    private static boolean drawShadow;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int maxLineLength;
    private float line_pos, shadowRadius;
    private int r, rDown, rUp, width, scrubberColor, progressColor, shadowColor, progressBackgroundColor;
    private int max;
    private float progress, scrubberPosition;
    private OnProgressChangedListener listener;

    public MaterialSeekBar(Context context) {
        super(context);
        init(context);
    }

    public MaterialSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MaterialSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    private void init(Context context) {
        res = context.getResources();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeCap(Paint.Cap.ROUND);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        setMax(100);
        drawShadow(true);
        setScrubberColor(res.getColor(R.color.material_green_light));
        setProgressColor(res.getColor(R.color.material_green_light));
        setShadowColor(res.getColor(R.color.black));
        setProgressBackgroundColor(res.getColor(R.color.lite_grey));
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //boolean drawShadow_tmp = drawShadow;
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        final int touch = (int) (event.getX());
                        if (touch < width - rUp && touch > rUp)
                            setProgress(max * (event.getX() / (float) width));


                        r = rUp;
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_OUTSIDE:
                        r = rDown;
                        break;

                }
                invalidate();
                return true;
            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        scrubberPosition = rUp + ((width - getPaddingRight() - getPaddingLeft()) * progress / (float) max);

        setPaintColor(progressBackgroundColor);
        canvas.drawLine(scrubberPosition, line_pos, (float) (width - getPaddingRight()), line_pos, paint);

        setPaintColor(progressColor);
        canvas.drawLine(getPaddingLeft() + rUp, line_pos, scrubberPosition, line_pos, paint);

        setPaintColor(scrubberColor);
        if (drawShadow) {
            paint.setShadowLayer(shadowRadius, 0, 0, shadowColor);
            canvas.drawCircle(scrubberPosition, line_pos, r, paint);
            paint.setShadowLayer(0, 0, 0, 0);
        } else
            canvas.drawCircle(scrubberPosition, line_pos, r, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        paint.setStrokeWidth(0.15f * Math.min(w, h));
        line_pos = h / 2;
        rDown = (int) (Math.min((w - getPaddingLeft() - getPaddingRight()), (h - getPaddingBottom() - getPaddingTop())) / 2.5f);
        rUp = (int) (Math.min((w - getPaddingLeft() - getPaddingRight()), (h - getPaddingBottom() - getPaddingTop())) / 1.8);
        maxLineLength = w - getPaddingLeft() - getPaddingRight() - rUp - rUp;
        r = rDown;
        shadowRadius = 0.2f * rDown;
        width = w;

    }

    private void notifyListener() {
        if (listener != null)
            listener.onProgressChanged(getMax(), getProgress());
    }

    private void setPaintColor(int color) {
        try {
            this.paint.setColor(color);
        } catch (Exception e) {
        }
    }

    public void setOnProgressChangedListener(OnProgressChangedListener listener) {
        this.listener = listener;
    }

    public int getProgress() {
        return Math.round(progress);
    }

    public void setProgress(float progress) {
        progress = progress > max ? max : progress;
        progress = progress < 0 ? 0 : progress;
        this.progress = progress;
        notifyListener();
    }

    public void setProgress(int progress) {
        this.progress = progress;
        notifyListener();
    }

    public void setScrubberRadius(int r) {
        this.rDown = r;
    }

    public void setScrubberColor(int scrubberColor) {
        this.scrubberColor = scrubberColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }

    public void setProgressBackgroundColor(int progressBackgroundColor) {
        this.progressBackgroundColor = progressBackgroundColor;
    }

    public void drawShadow(boolean drawShadow) {
        this.drawShadow = drawShadow;
    }

    public void setShadowColor(int shadowColor) {
        this.shadowColor = shadowColor;
    }

    public interface OnProgressChangedListener {
        public void onProgressChanged(int max, int progress);
    }
}
