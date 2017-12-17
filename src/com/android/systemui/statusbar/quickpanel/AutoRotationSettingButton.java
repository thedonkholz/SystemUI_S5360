package com.android.systemui.statusbar.quickpanel;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings.System;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import com.android.systemui.R;

public final class AutoRotationSettingButton extends QuickSettingButton {
    private AutoRotationObserver mAutoRotationObserver;

    private class AutoRotationObserver extends ContentObserver {
        public AutoRotationObserver() {
            super(new Handler());
        }

        public void onChange(boolean selfChange) {
            AutoRotationSettingButton.this.updateStatus();
        }
    }

    public AutoRotationSettingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mAutoRotationObserver = new AutoRotationObserver();
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e("AutoRotationSettingButton", "onAttachedToWindow()");
        this.mContext.getContentResolver().registerContentObserver(System.getUriFor("accelerometer_rotation"), false, this.mAutoRotationObserver);
        updateStatus();
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e("AutoRotationSettingButton", "onDetachedFromWindow()");
        this.mContext.getContentResolver().unregisterContentObserver(this.mAutoRotationObserver);
    }

    private void updateStatus() {
        if (1 == System.getInt(this.mContext.getContentResolver(), "accelerometer_rotation", 0)) {
            setActivateStatus(1);
        } else {
            setActivateStatus(0);
        }
        updateIcons();
    }

    private void updateIcons() {
        int iconRes = 0;
        int textColor = -1;
        ImageView icon = (ImageView) getRootView().findViewById(R.id.quickpanel_rotation_btn_icon);
        switch (getActivateStatus()) {
            case 0:
                iconRes = R.drawable.quickpanel_icon_rotation_off;
                textColor = -3355444;
                break;
            case 1:
                iconRes = R.drawable.quickpanel_icon_rotation_on;
                break;
        }
        icon.setImageResource(iconRes);
        setTextColor(textColor);
    }

    public void updateResources() {
        setText(R.string.quickpanel_rotation_text);
    }

    public void activate() {
        Log.e("AutoRotationSettingButton", "activate()");
        setRotationEnabled(1);
    }

    public void deactivate() {
        Log.e("AutoRotationSettingButton", "deactivate()");
        setRotationEnabled(0);
    }

    private void setRotationEnabled(int enabled) {
        System.putInt(this.mContext.getContentResolver(), "accelerometer_rotation", enabled);
    }
}
