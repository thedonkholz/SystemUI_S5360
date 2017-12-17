package com.android.systemui.statusbar.quickpanel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.provider.Settings.System;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import com.android.systemui.R;

public final class SoundSettingButton extends QuickSettingButton {
    private static int mSoundProfile;
    private static int mSoundText;
    private static int mVibProfile;
    private AudioManager mAudioManager;
    private BroadcastReceiver mIntentReceiver;

    /* renamed from: com.android.systemui.statusbar.quickpanel.SoundSettingButton.1 */
    class C00121 extends BroadcastReceiver {
        C00121() {
        }

        public void onReceive(Context context, Intent intent) {
            String str = "SoundSettingButton";
            String intentAction = intent.getAction();
            String str2;
            if ("android.media.RINGER_MODE_CHANGED".equals(intentAction)) {
                SoundSettingButton.mSoundProfile = intent.getIntExtra("android.media.EXTRA_RINGER_MODE", 2);
                str2 = "SoundSettingButton";
                Log.e(str, "onReceive()-S:" + SoundSettingButton.mSoundProfile);
            } else if ("android.media.VIBRATE_SETTING_CHANGED".equals(intentAction) && intent.getIntExtra("android.media.EXTRA_VIBRATE_TYPE", 0) == 0) {
                SoundSettingButton.mVibProfile = intent.getIntExtra("android.media.EXTRA_VIBRATE_SETTING", 2);
                str2 = "SoundSettingButton";
                Log.e(str, "onReceive()-V:" + SoundSettingButton.mVibProfile);
            }
            SoundSettingButton.this.updateStatus();
        }
    }

    public SoundSettingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mAudioManager = null;
        this.mIntentReceiver = new C00121();
    }

    protected void onAttachedToWindow() {
        String str = "SoundSettingButton";
        super.onAttachedToWindow();
        String str2 = "SoundSettingButton";
        Log.e(str, "onAttachedToWindow()");
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.RINGER_MODE_CHANGED");
        filter.addAction("android.media.VIBRATE_SETTING_CHANGED");
        this.mContext.registerReceiver(this.mIntentReceiver, filter, null, null);
        this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
        if (this.mAudioManager != null) {
            mSoundProfile = this.mAudioManager.getRingerMode();
            mVibProfile = this.mAudioManager.getVibrateSetting(0);
            updateStatus();
            return;
        }
        str2 = "SoundSettingButton";
        Log.e(str, "mAudioManager is null");
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e("SoundSettingButton", "onDetachedFromWindow()");
        this.mContext.unregisterReceiver(this.mIntentReceiver);
    }

    private void updateStatus() {
        if (2 == mSoundProfile) {
            setActivateStatus(1);
        } else {
            setActivateStatus(0);
        }
        updateIconsAndText();
    }

    private void updateIconsAndText() {
        int iconRes = 0;
        int soundText = 0;
        int textColor = -1;
        ImageView icon = (ImageView) getRootView().findViewById(R.id.quickpanel_sound_btn_icon);
        switch (getActivateStatus()) {
            case 0:
                if (mSoundProfile == 0) {
                    iconRes = R.drawable.quickpanel_icon_silent_off;
                    soundText = R.string.quickpanel_silent_text;
                } else if (1 == mSoundProfile) {
                    iconRes = R.drawable.quickpanel_icon_vibration_off;
                    soundText = R.string.quickpanel_vibration_text;
                }
                textColor = -3355444;
                break;
            case 1:
                if (1 != mVibProfile) {
                    iconRes = R.drawable.quickpanel_icon_sound_on_on;
                    soundText = R.string.quickpanel_sound_text;
                    break;
                }
                iconRes = R.drawable.quickpanel_icon_sound_vibration_on;
                soundText = R.string.quickpanel_sound_vibration_text;
                break;
        }
        icon.setImageResource(iconRes);
        setText(soundText);
        mSoundText = soundText;
        setTextColor(textColor);
    }

    public void updateResources() {
        setText(mSoundText);
    }

    public void activate() {
        Log.e("SoundSettingButton", "activate()");
        this.mAudioManager.setRingerMode(2);
    }

    public void deactivate() {
        int soundProfile = 0;
        switch (mVibProfile) {
            case 0:
                soundProfile = 0;
                break;
            case 1:
                if (System.getInt(this.mContext.getContentResolver(), "vibrate_in_silent", 0) != 1) {
                    soundProfile = 0;
                    break;
                } else {
                    soundProfile = 1;
                    break;
                }
            case 2:
                soundProfile = 1;
                break;
        }
        Log.e("SoundSettingButton", "deactivate()-S:" + soundProfile + " V:" + mVibProfile);
        this.mAudioManager.setRingerMode(soundProfile);
    }

    public boolean performClick() {
        sendAccessibilityEvent(1);
        if (this.mOnClickListener == null) {
            return false;
        }
        this.mOnClickListener.onClick(this);
        return true;
    }
}
