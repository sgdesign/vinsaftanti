package com.vinsasoft.security.service;

import android.content.Context;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.vinsasoft.security.stuff.ManagerUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import hugo.weaving.DebugLog;



@SuppressWarnings("TryWithIdenticalCatches")
public class EndCallService {
    private static final String GET_I_TELEPHONY = "getITelephony";
    private static final String TAG = EndCallService.class.getSimpleName();
    private final Context context;
    private final Method method;

    @SuppressWarnings("unchecked")
    public EndCallService(Context context) {
        this.context = context;
        try {
            Class clazz = Class.forName(TelephonyManager.class.getName());
            method = clazz.getDeclaredMethod(GET_I_TELEPHONY);
            method.setAccessible(true);
        } catch (ClassNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @DebugLog
    public boolean endCall() {
        try {
            TelephonyManager tm = ManagerUtil.getTelephonyManager(context);
            ITelephony telephonyService = (ITelephony) method.invoke(tm);
            return telephonyService.endCall();
        } catch (IllegalAccessException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
