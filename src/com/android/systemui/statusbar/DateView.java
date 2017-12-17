package com.android.systemui.statusbar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.widget.TextView;
import java.util.Date;

public final class DateView extends TextView {
    private BroadcastReceiver mIntentReceiver;
    private boolean mUpdating;

    /* renamed from: com.android.systemui.statusbar.DateView.1 */
    class C00041 extends BroadcastReceiver {
        C00041() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("android.intent.action.TIME_TICK") || action.equals("android.intent.action.TIMEZONE_CHANGED")) {
                DateView.this.updateClock();
            }
        }
    }

    public DateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mUpdating = false;
        this.mIntentReceiver = new C00041();
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setUpdates(false);
    }

    protected int getSuggestedMinimumWidth() {
        return 0;
    }

    private final void updateClock() {
        setText(DateFormat.getDateFormat(this.mContext).format(new Date()));
    }

    void setUpdates(boolean update) {
        if (update != this.mUpdating) {
            this.mUpdating = update;
            if (update) {
                IntentFilter filter = new IntentFilter();
                filter.addAction("android.intent.action.TIME_TICK");
                filter.addAction("android.intent.action.TIMEZONE_CHANGED");
                this.mContext.registerReceiver(this.mIntentReceiver, filter, null, null);
                updateClock();
                return;
            }
            this.mContext.unregisterReceiver(this.mIntentReceiver);
        }
    }
}
