package com.vinsasoft.security.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.vinsasoft.security.service.CallBlockingService;
import com.vinsasoft.security.stuff.AppContext;
import com.vinsasoft.security.stuff.Util;

import hugo.weaving.DebugLog;

import static com.vinsasoft.security.BuildConfig.DEBUG;


public class CallReceiver extends BroadcastReceiver {
    private static final String TAG = CallReceiver.class.getSimpleName();

    @Override
    @DebugLog
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {

                boolean blockingEnabled = ((AppContext) context.getApplicationContext())
                        .getAppPreferences().isBlockingEnable();
                if (blockingEnabled) {
                    String incomingNumber = intent.getStringExtra(TelephonyManager
                            .EXTRA_INCOMING_NUMBER);

                    Log.i(TAG, "Incoming number: {" + incomingNumber + "}");
                    if (DEBUG) {
                        Log.d(TAG, "Running " + CallBlockingService.class.getSimpleName() + ": " +
                                Util.isServiceRunning(context, CallBlockingService.class));
                    }
                    Intent i = new Intent(context, CallBlockingService.class);
                    i.putExtra(CallBlockingService.INCOMING_NUMBER, incomingNumber);
                    context.startService(i);
                }
            }
        }
    }
}