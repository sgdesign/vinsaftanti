package com.vinsasoft.security.service;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

//import com.vinsasoft.security.BuildConfig;
import com.vinsasoft.security.BuildConfig;
import com.vinsasoft.security.db.LogTable;
import com.vinsasoft.security.CallBlocker.LogEntity;
import com.vinsasoft.security.CallBlocker.PhoneNumberException;
import com.vinsasoft.security.CallBlocker.TooShortNumberException;
import com.vinsasoft.security.stuff.AppPreferences;
import com.vinsasoft.security.stuff.ManagerUtil;

import java.util.Date;

import hugo.weaving.DebugLog;


public class BlockWrapper {

    private static final String TAG = BlockWrapper.class.getSimpleName();
    private final NormalizerService normalizerService;
    private final MasterChecker masterChecker;
    private final Context context;
    private final EndCallService endCallService;
    private final AppPreferences appPreferences;

    public BlockWrapper(Context context, MasterChecker masterChecker, EndCallService
            endCallService, NormalizerService normalizerService, AppPreferences appPreferences) {
        this.context = context;
        this.masterChecker = masterChecker;
        this.normalizerService = normalizerService;
        this.endCallService = endCallService;
        this.appPreferences = appPreferences;
    }

    @DebugLog
    public String checkAndBlock(boolean dry, String number) throws TooShortNumberException,
            PhoneNumberException {
        Call call = new Call();
        call.setNumber(number);

        boolean blockable = masterChecker.isBlockable(call);
        String formattedNumber = null;
        if (blockable && !dry) {
            int prev = mute();
            boolean res = endCallService.endCall();
            unMute(prev);

            normalizerService.normalizeCall(call);
            formattedNumber = normalizerService.getDisplayNumber(call);
            if (res && appPreferences.isEnableLog()) {
                saveLog(call);
            }
            masterChecker.doLast();
        }
        if (dry) {
            masterChecker.refresh();
        }
        return formattedNumber;
    }

    @DebugLog
    private void saveLog(Call call) {
        LogEntity logEntity = new LogEntity();
        logEntity.setCallerID(call.getNumber());
        logEntity.setDisplayNumber(normalizerService.getDisplayNumber(call));
        if (call.getExtraData().containsKey("displayName")) {
            logEntity.setDisplayName(call.getExtraData().get("displayName"));
        }
        logEntity.setTime((new Date()).getTime());
        logEntity.setBlockOrigin(call.getBlockOrigin());

        ContentValues contentValue = new ContentValues();
        logEntity.toContentValues(contentValue);

        Uri newUri = context.getContentResolver().insert(LogTable.CONTENT_URI, contentValue);
        if (newUri == null) {
            Log.e(TAG, "Couldn't insert LogEntity: " + logEntity.toString());
            throw new RuntimeException("Couldn't insert LogEntity: " + logEntity.toString());
        }
    }

    @DebugLog
    private int mute() {
        NotificationManager notificationManager = ManagerUtil.getNotificationManager(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !notificationManager
                .isNotificationPolicyAccessGranted()) {
            return -1;
        }

        AudioManager audioManager = ManagerUtil.getAudioManager(context);
        int ringerMode = audioManager.getRingerMode();
        if (ringerMode != AudioManager.RINGER_MODE_SILENT) {
            if (BuildConfig.DEBUG)
                Log.d(TAG, "Actual ringer mode: " + ringerMode);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            return ringerMode;
        }
        return -1;
    }

    @DebugLog
    private void unMute(int previousMode) {
        if (previousMode == -1) return;

        try {
            //A delay is introduced as sometimes Android does not restore the previous mode
            //correctly. This delay seems to work.
            Thread.sleep(500);
        } catch (InterruptedException e) {
            //do nothing
        }

        AudioManager audioManager = ManagerUtil.getAudioManager(context);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Should be silent ringer mode: " + audioManager.getRingerMode());
            Log.d(TAG, "Restituting ringer mode: " + previousMode);
        }

        audioManager.setRingerMode(previousMode);
    }

}
