package com.android.systemui.statusbar.quickpanel;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import com.android.systemui.R;

public final class DataConnectionSettingButton extends QuickSettingButton {
    SettingsDBObserver co;
    Handler mCallHandler;
    private ConnectivityManager mConnectivityManager;
    private TelephonyManager mTelephonyManager;

    private class SettingsDBObserver extends ContentObserver {
        public SettingsDBObserver(Handler h) {
            super(h);
        }

        public void onChange(boolean b) {
            if (DataConnectionSettingButton.this.mConnectivityManager.getMobileDataEnabled()) {
                DataConnectionSettingButton.this.setActivateStatus(1);
            } else {
                DataConnectionSettingButton.this.setActivateStatus(0);
            }
            DataConnectionSettingButton.this.updateIcons();
        }
    }

    private void setDBObserver() {
        ContentResolver cr = this.mContext.getContentResolver();
        this.co = new SettingsDBObserver(this.mCallHandler);
        cr.registerContentObserver(Secure.CONTENT_URI, true, this.co);
    }

    public DataConnectionSettingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mCallHandler = new Handler();
    }

    protected void onAttachedToWindow() {
        String str = "DataConnectionSettingButton";
        super.onAttachedToWindow();
        String str2 = "DataConnectionSettingButton";
        Log.v(str, "onAttachedToWindow() - entered");
        this.mConnectivityManager = (ConnectivityManager) this.mContext.getSystemService("connectivity");
        this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        setActivateStatus(0);
        if (this.mConnectivityManager.getMobileDataEnabled()) {
            setActivateStatus(1);
        }
        str2 = "DataConnectionSettingButton";
        Log.v(str, "OnAttachedWindow, calling updateIcons and updateResources");
        updateIcons();
        updateResources();
        setDBObserver();
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e("DataConnectionSettingButton", "onDetachedFromWindow()");
    }

    public void updateIcons() {
        String str = "DataConnectionSettingButton";
        String str2 = "DataConnectionSettingButton";
        Log.e(str, "updateIcons( " + this.mConnectivityManager.getMobileDataEnabled() + " data state " + this.mTelephonyManager.getDataState() + " )");
        int iconRes = 0;
        ImageView icon = (ImageView) getRootView().findViewById(R.id.quickpanel_data_btn_icon);
        str2 = "DataConnectionSettingButton";
        Log.e(str, "getActivateStatus() : " + getActivateStatus());
        switch (getActivateStatus()) {
            case 0:
                iconRes = R.drawable.quickpanel_icon_data_off;
                break;
            case 1:
                iconRes = R.drawable.quickpanel_icon_data_on;
                break;
        }
        icon.setImageResource(iconRes);
    }

    public void updateResources() {
        Log.e("DataConnectionSettingButton", "updateResources");
        setText(R.string.quickpanel_dc_text);
    }

    public void activate() {
        Log.v("DataConnectionSettingButton", "activate()");
        this.mConnectivityManager.setMobileDataEnabled(true);
    }

    public void deactivate() {
        Log.v("DataConnectionSettingButton", "deactivate()");
        this.mConnectivityManager.setMobileDataEnabled(false);
    }
}
