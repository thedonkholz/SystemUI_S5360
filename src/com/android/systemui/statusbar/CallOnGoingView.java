package com.android.systemui.statusbar;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Slog;
import android.view.MotionEvent;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import com.android.systemui.R;

public class CallOnGoingView extends FrameLayout {
    StatusBarService mService;
    private Chronometer mTimer;

    public CallOnGoingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mTimer = (Chronometer) findViewById(R.id.status_bar_call_ongoing_duration);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mTimer.start();
        Slog.i("CallOnGoingView", "onAttachedToWindow");
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mTimer.stop();
        Slog.i("CallOnGoingView", "onDetachedFromWindow");
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != 0) {
            this.mService.interceptTouchEvent(event);
        }
        return true;
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.mService.interceptTouchEvent(event) ? true : super.onInterceptTouchEvent(event);
    }
}
