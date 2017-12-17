package com.android.systemui.statusbar.quickpanel;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import com.android.systemui.R;

public final class GpsSettingButton extends QuickSettingButton {
    private GpsObserver mGpsObserver;

    private class GpsObserver extends ContentObserver {
        public GpsObserver() {
            super(new Handler());
        }

        public void onChange(boolean selfChange) {
            GpsSettingButton.this.updateStatus();
        }
    }

    public GpsSettingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mGpsObserver = new GpsObserver();
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e("GpsSettingButton", "onAttachedToWindow()");
        this.mContext.getContentResolver().registerContentObserver(Secure.getUriFor("location_providers_allowed"), false, this.mGpsObserver);
        updateStatus();
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e("GpsSettingButton", "onDetachedFromWindow()");
        this.mContext.getContentResolver().unregisterContentObserver(this.mGpsObserver);
    }

    private void updateStatus() {
        if (Secure.isLocationProviderEnabled(this.mContext.getContentResolver(), "gps")) {
            setActivateStatus(1);
        } else {
            setActivateStatus(0);
        }
        updateIcons();
    }

    private void updateIcons() {
        int iconRes = 0;
        int textColor = -1;
        ImageView icon = (ImageView) getRootView().findViewById(R.id.quickpanel_gps_btn_icon);
        switch (getActivateStatus()) {
            case 0:
                iconRes = R.drawable.quickpanel_icon_gps_off;
                textColor = -3355444;
                break;
            case 1:
                iconRes = R.drawable.quickpanel_icon_gps_on;
                break;
        }
        icon.setImageResource(iconRes);
        setTextColor(textColor);
    }

    public void updateResources() {
        setText(R.string.quickpanel_gps_text);
    }

    public void activate() {
        Log.e("GpsSettingButton", "activate()");
        setGPSEnabled(true);
    }

    public void deactivate() {
        Log.e("GpsSettingButton", "deactivate()");
        setGPSEnabled(false);
    }

    private void setGPSEnabled(boolean enabled) {
        Secure.setLocationProviderEnabled(this.mContext.getContentResolver(), "gps", enabled);
    }
}
