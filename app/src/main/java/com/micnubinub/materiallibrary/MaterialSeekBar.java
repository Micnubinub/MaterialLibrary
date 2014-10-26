package com.micnubinub.materiallibrary;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by root on 24/08/14.
 */
public class MaterialSeekBar extends View {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int lineRight;
    private float line_pos, scaleTo = 1.22f;
    private int r, rDown, rUp, width, scrubberColor, progressColor, progressBackgroundColor;
    private int max;
    private float progress, scrubberPosition;
    private OnProgressChangedListener listener;

    public MaterialSeekBar(Context context) {
        super(context);
        init();
    }

    public MaterialSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MaterialSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    private void init() {
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeCap(Paint.Cap.ROUND);
        //setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        setMax(100);
        setScrubberColor(getResources().getColor(R.color.material_green_light));
        setProgressColor(getResources().getColor(R.color.material_green_light));
        //setShadowColor(res.getColor(R.color.black));
        setProgressBackgroundColor(getResources().getColor(R.color.lite_grey));
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //boolean drawShadow_tmp = drawShadow;
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        final int touch = (int) (event.getX());
                        if (touch < lineRight && touch > rUp)
                            setProgress(max * ((event.getX() - rUp) / (float) (width - 2 * rUp)));

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

        scrubberPosition = ((width - getPaddingRight() - getPaddingLeft()) * progress / (float) max);
        scrubberPosition = scrubberPosition < rUp ? rUp : scrubberPosition;
        scrubberPosition = scrubberPosition > lineRight ? lineRight : scrubberPosition;

        setPaintColor(progressBackgroundColor);
        canvas.drawLine(scrubberPosition, line_pos, lineRight, line_pos, paint);

        setPaintColor(progressColor);
        canvas.drawLine(rUp, line_pos, scrubberPosition, line_pos, paint);

        setPaintColor(scrubberColor);
        /*
        if (drawShadow) {
            paint.setShadowLayer(shadowRadius, 0, 0, shadowColor);
            canvas.drawCircle(scrubberPosition, line_pos, r, paint);
            paint.setShadowLayer(0, 0, 0, 0);
        } else*/
        canvas.drawCircle(scrubberPosition, line_pos, r, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        line_pos = h / 2;
        rDown = ((int) (Math.min((w - getPaddingLeft() - getPaddingRight()), (h - getPaddingBottom() - getPaddingTop())) / scaleTo)) / 2;
        rUp = Math.min((w - getPaddingLeft() - getPaddingRight()), (h - getPaddingBottom() - getPaddingTop())) / 2;

        lineRight = w - rUp;
        r = rDown;
        paint.setStrokeWidth(rDown / 2.3f);
        width = w;

    }

    private void notifyListener() {
        if (listener != null)
            listener.onProgressChanged(getMax(), getProgress());
    }

    private void setPaintColor(int color) {
        try {
            this.paint.setColor(color);
        } catch (Exception ignored) {
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

    public interface OnProgressChangedListener {
        public void onProgressChanged(int max, int progress);
    }
}
