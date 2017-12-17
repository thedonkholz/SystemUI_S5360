package com.android.systemui.statusbar.quickpanel;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class QuickSettingsView extends LinearLayout {
    public QuickSettingsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public int getSuggestedMinimumHeight() {
        return 0;
    }
}
