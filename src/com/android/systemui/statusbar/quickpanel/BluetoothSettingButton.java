package com.android.systemui.statusbar.quickpanel;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings.System;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.systemui.R;

public final class BluetoothSettingButton extends QuickSettingButton {
    private int isAirPlaneMode;
    private BluetoothAdapter mBluetoothAdapter;
    private BroadcastReceiver mIntentReceiver;
    private Toast toastAlert;

    /* renamed from: com.android.systemui.statusbar.quickpanel.BluetoothSettingButton.1 */
    class C00101 extends BroadcastReceiver {
        C00101() {
        }

        public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE);
            Log.e("BluetoothSettingButton", "onReceive()-S:" + state);
            BluetoothSettingButton.this.handleStateChanged(state);
        }
    }

    private void handleStateChanged(int state) {
        updateStatus(state);
    }

    public BluetoothSettingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mBluetoothAdapter = null;
        this.toastAlert = Toast.makeText(this.mContext, null, 0);
        this.mIntentReceiver = new C00101();
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e("BluetoothSettingButton", "onAttachedToWindow()");
        this.mContext.registerReceiver(this.mIntentReceiver, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"), null, null);
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (this.mBluetoothAdapter != null) {
            updateStatus(this.mBluetoothAdapter.getState());
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e("BluetoothSettingButton", "onDetachedFromWindow()");
        this.mContext.unregisterReceiver(this.mIntentReceiver);
    }

    private void updateStatus(int state) {
        switch (state) {
            case 10:
                setActivateStatus(0);
                setSoundEffectsEnabled(true);
                break;
            case 11:
                setActivateStatus(3);
                setSoundEffectsEnabled(false);
                break;
            case 12:
                setActivateStatus(1);
                setSoundEffectsEnabled(true);
                break;
            case 13:
                setActivateStatus(4);
                setSoundEffectsEnabled(false);
                break;
        }
        updateIconsAndTextColor();
    }

    private void updateIconsAndTextColor() {
        int iconRes = 0;
        int textColor = -1;
        ImageView icon = (ImageView) getRootView().findViewById(R.id.quickpanel_bt_btn_icon);
        switch (getActivateStatus()) {
            case 0:
                iconRes = R.drawable.quickpanel_icon_bluetooth_off;
                setText(R.string.quickpanel_bluetooth_text);
                textColor = -3355444;
                break;
            case 1:
                iconRes = R.drawable.quickpanel_icon_bluetooth_on;
                setText(R.string.quickpanel_bluetooth_text);
                break;
            case 3:
                iconRes = R.drawable.quickpanel_icon_bluetooth_off;
                textColor = -3355444;
                setText(R.string.capital_on);
                break;
            case 4:
                iconRes = R.drawable.quickpanel_icon_bluetooth_on;
                setText(R.string.capital_off);
                break;
        }
        icon.setImageResource(iconRes);
        setTextColor(textColor);
    }

    public void updateResources() {
        setText(R.string.quickpanel_bluetooth_text);
    }

    public void activate() {
        String str = "BluetoothSettingButton";
        if (couldClick()) {
            Log.e(str, "activate()");
            this.mBluetoothAdapter.enable();
            return;
        }
        Log.e(str, "activate() couldn't click");
        if (this.isAirPlaneMode == 1) {
            this.toastAlert.setText(R.string.wifi_in_airplane_mode);
            this.toastAlert.show();
        }
    }

    public void deactivate() {
        String str = "BluetoothSettingButton";
        if (couldClick()) {
            Log.e(str, "deactivate()");
            this.mBluetoothAdapter.disable();
            return;
        }
        Log.e(str, "deactivate() couldn't click");
    }

    private boolean couldClick() {
        this.isAirPlaneMode = System.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0);
        if (this.isAirPlaneMode == 1) {
            return false;
        }
        if (this.mBluetoothAdapter != null) {
            return true;
        }
        Log.e("BluetoothSettingButton", "mBluetoothAdapter is null");
        return false;
    }
}
