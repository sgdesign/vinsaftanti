package com.saeedsoft.security.broadcasts;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.saeedsoft.security.BuildConfig;
import com.saeedsoft.security.service.CallBlockingService;
import com.saeedsoft.security.stuff.AppContext;

import hugo.weaving.DebugLog;

public class WakeUpReceiver extends WakefulBroadcastReceiver {
    private static final String TAG = WakeUpReceiver.class.getSimpleName();

    @Override
    @DebugLog
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            boolean blockingEnabled = ((AppContext) context.getApplicationContext())
                    .getAppPreferences().isBlockingEnable();
            if (blockingEnabled) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "Starting service " + CallBlockingService.class.getSimpleName());
                }
                Intent startServiceIntent = new Intent(context, CallBlockingService.class);
                startServiceIntent.putExtra(CallBlockingService.WAKEUP, 1);
                startServiceIntent.putExtra(CallBlockingService.DRY, true);
                startWakefulService(context, startServiceIntent);
            }
        }
    }
}