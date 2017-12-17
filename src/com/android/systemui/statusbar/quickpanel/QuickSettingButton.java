package com.android.systemui.statusbar.quickpanel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public abstract class QuickSettingButton extends TextView implements OnClickListener {
    private int mActivateStatus;
    private BroadcastReceiver mIntentReceiver;
    private View mRootView;

    /* renamed from: com.android.systemui.statusbar.quickpanel.QuickSettingButton.1 */
    class C00111 extends BroadcastReceiver {
        C00111() {
        }

        public void onReceive(Context context, Intent intent) {
            QuickSettingButton.this.updateResources();
        }
    }

    abstract void activate();

    abstract void deactivate();

    abstract void updateResources();

    public QuickSettingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mRootView = null;
        this.mIntentReceiver = new C00111();
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e("QuickSettingButton", "onAttachedToWindow()");
        this.mRootView = getRootView();
        setOnClickListener(this);
        this.mContext.registerReceiver(this.mIntentReceiver, new IntentFilter("android.intent.action.CONFIGURATION_CHANGED"), null, null);
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e("QuickSettingButton", "onDetachedFromWindow()");
    }

    public void onClick(View v) {
        setEnabled(false);
        if (1 == this.mActivateStatus) {
            deactivate();
        } else if (this.mActivateStatus == 0) {
            activate();
        }
        setEnabled(true);
    }

    protected int getActivateStatus() {
        return this.mActivateStatus;
    }

    protected void setActivateStatus(int activateStatus) {
        this.mActivateStatus = activateStatus;
    }
}
