package com.android.systemui.statusbar.quickpanel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import com.android.systemui.R;

public final class WifiSettingButton extends QuickSettingButton {
    private BroadcastReceiver mIntentReceiver;
    private WifiManager mWifiManager;

    /* renamed from: com.android.systemui.statusbar.quickpanel.WifiSettingButton.1 */
    class C00131 extends BroadcastReceiver {
        C00131() {
        }

        public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra("wifi_state", 4);
            Log.e("WifiSettingButton", "onReceive()-S:" + state);
            WifiSettingButton.this.handleStateChanged(state);
        }
    }

    private void handleStateChanged(int state) {
        updateStatus(state);
    }

    public WifiSettingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mWifiManager = null;
        this.mIntentReceiver = new C00131();
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e("WifiSettingButton", "onAttachedToWindow()");
        this.mContext.registerReceiver(this.mIntentReceiver, new IntentFilter("android.net.wifi.WIFI_STATE_CHANGED"), null, null);
        this.mWifiManager = (WifiManager) this.mContext.getSystemService("wifi");
        if (this.mWifiManager != null) {
            updateStatus(this.mWifiManager.getWifiState());
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e("WifiSettingButton", "onDetachedFromWindow()");
        this.mContext.unregisterReceiver(this.mIntentReceiver);
    }

    private void updateStatus(int state) {
        switch (state) {
            case 0:
                setActivateStatus(4);
                setSoundEffectsEnabled(false);
                break;
            case 1:
            case 4:
                setActivateStatus(0);
                setSoundEffectsEnabled(true);
                break;
            case 2:
                setActivateStatus(3);
                setSoundEffectsEnabled(false);
                break;
            case 3:
                setActivateStatus(1);
                setSoundEffectsEnabled(true);
                break;
        }
        updateIconsAndTextColor();
    }

    private void updateIconsAndTextColor() {
        int iconRes = 0;
        int textColor = -1;
        ImageView icon = (ImageView) getRootView().findViewById(R.id.quickpanel_wifi_btn_icon);
        switch (getActivateStatus()) {
            case 0:
                iconRes = R.drawable.quickpanel_icon_wifi_off;
                setText(R.string.quickpanel_wifi_text);
                textColor = -3355444;
                break;
            case 1:
                iconRes = R.drawable.quickpanel_icon_wifi_on;
                setText(R.string.quickpanel_wifi_text);
                break;
            case 3:
                iconRes = R.drawable.quickpanel_icon_wifi_off;
                textColor = -3355444;
                setText(R.string.capital_on);
                break;
            case 4:
                iconRes = R.drawable.quickpanel_icon_wifi_on;
                setText(R.string.capital_off);
                break;
        }
        icon.setImageResource(iconRes);
        setTextColor(textColor);
    }

    public void updateResources() {
        setText(R.string.quickpanel_wifi_text);
    }

    public void activate() {
        String str = "WifiSettingButton";
        String str2 = "WifiSettingButton";
        Log.e(str, "activate()");
        if (this.mWifiManager != null) {
            if (this.mWifiManager.isWifiEnabled()) {
                str2 = "WifiSettingButton";
                Log.d(str, "Wifi is already enabled.");
            } else {
                str2 = "WifiSettingButton";
                Log.d(str, "Set wifi enable.");
                this.mWifiManager.setWifiEnabledDialog(true);
            }
        }
        if (this.mWifiManager == null) {
        }
    }

    public void deactivate() {
        Log.e("WifiSettingButton", "deactivate()");
        if (this.mWifiManager != null) {
            this.mWifiManager.setWifiEnabledDialog(false);
        }
    }
}
