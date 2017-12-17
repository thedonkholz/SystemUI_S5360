package com.android.systemui.statusbar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class Clock extends TextView {
    private boolean mAttached;
    private Calendar mCalendar;
    private SimpleDateFormat mClockFormat;
    private String mClockFormatString;
    private boolean mConfigChanged;
    private final BroadcastReceiver mIntentReceiver;

    /* renamed from: com.android.systemui.statusbar.Clock.1 */
    class C00011 extends BroadcastReceiver {
        C00011() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("android.intent.action.TIMEZONE_CHANGED")) {
                Clock.this.mCalendar = Calendar.getInstance(TimeZone.getTimeZone(intent.getStringExtra("time-zone")));
                if (Clock.this.mClockFormat != null) {
                    Clock.this.mClockFormat.setTimeZone(Clock.this.mCalendar.getTimeZone());
                }
            } else if (action.equals("android.intent.action.CONFIGURATION_CHANGED")) {
                Clock.this.mConfigChanged = true;
            }
            Clock.this.updateClock();
            Clock.this.mConfigChanged = false;
        }
    }

    public Clock(Context context) {
        this(context, null);
    }

    public Clock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Clock(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mIntentReceiver = new C00011();
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!this.mAttached) {
            this.mAttached = true;
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.intent.action.TIME_TICK");
            filter.addAction("android.intent.action.TIME_SET");
            filter.addAction("android.intent.action.TIMEZONE_CHANGED");
            filter.addAction("android.intent.action.CONFIGURATION_CHANGED");
            getContext().registerReceiver(this.mIntentReceiver, filter, null, getHandler());
        }
        this.mCalendar = Calendar.getInstance(TimeZone.getDefault());
        updateClock();
        setTextColor(-1);
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mAttached) {
            getContext().unregisterReceiver(this.mIntentReceiver);
            this.mAttached = false;
        }
    }

    final void updateClock() {
        this.mCalendar.setTimeInMillis(System.currentTimeMillis());
        setText(getSmallTime());
    }

    private final CharSequence getSmallTime() {
        int res;
        SimpleDateFormat sdf;
        Context context = getContext();
        boolean b24 = DateFormat.is24HourFormat(context);
        if (b24) {
            res = 17039478;
        } else {
            res = 17039477;
        }
        String format = context.getString(res);
        if (!format.equals(this.mClockFormatString) || (!b24 && this.mConfigChanged)) {
            sdf = new SimpleDateFormat(format);
            this.mClockFormat = sdf;
            this.mClockFormatString = format;
        } else {
            sdf = this.mClockFormat;
        }
        return sdf.format(this.mCalendar.getTime());
    }
}
