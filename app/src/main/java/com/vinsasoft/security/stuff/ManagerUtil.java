package com.vinsasoft.security.stuff;

import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioManager;
import android.telephony.TelephonyManager;

import hugo.weaving.DebugLog;

public class ManagerUtil {
    @DebugLog
    public static TelephonyManager getTelephonyManager(Context context) {
        return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    @DebugLog
    public static AudioManager getAudioManager(Context context) {
        return (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    @DebugLog
    public static NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }
}
