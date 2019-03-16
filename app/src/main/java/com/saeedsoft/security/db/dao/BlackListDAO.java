package com.saeedsoft.security.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

//import com.saeedsoft.security.BuildConfig;
import com.saeedsoft.security.BuildConfig;
import com.saeedsoft.security.db.BlackListTable;
import com.saeedsoft.security.CallBlocker.BlackListNumberEntity;
import com.saeedsoft.security.CallBlocker.TooShortNumberException;
import com.saeedsoft.security.service.Call;
import com.saeedsoft.security.stuff.Util;

import java.util.ArrayList;
import java.util.List;

import hugo.weaving.DebugLog;



public class BlackListDAO {
    private static final String TAG = BlackListDAO.class.getSimpleName();
    private final Context context;

    public BlackListDAO(Context context) {
        this.context = context;
    }

    @DebugLog
    public List<BlackListNumberEntity> findForBlock(Call call) {

        String nationalNumber = Long.toString(call.getNormalizedNumber().getNationalNumber());
        String rawNumber = call.getNormalizedNumber().getRawInput();
        int beginIndex = nationalNumber.length() - TooShortNumberException.MINIMUM_LENGTH;
        String lastDigits = nationalNumber.substring(beginIndex, nationalNumber.length());

        String selectFormalNumber = BlackListTable.NORMALIZED_NUMBER + " LIKE ? AND " +
                BlackListTable.NORMALIZED_NUMBER + " LIKE ? AND " + BlackListTable.BEGIN_WITH +
                "=0";
        String selectBeginWith = BlackListTable.BEGIN_WITH + "=1 AND " + BlackListTable
                .NORMALIZED_NUMBER + " = substr(?,0, " +
                "length(" + BlackListTable.NORMALIZED_NUMBER + ")+1)";

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Parameter 1:" + "+" + call.getNormalizedNumber().getCountryCode() + "%");
            Log.d(TAG, "Parameter 2:" + "%" + lastDigits);
            Log.d(TAG, "Parameter 3:" + rawNumber);
        }
        Cursor cursor = context.getContentResolver().query(
                BlackListTable.CONTENT_URI, null,
                BlackListTable.ENABLED + "=1 AND ((" + selectFormalNumber + ") OR (" +
                        selectBeginWith + "))",
                new String[]{"+" + call.getNormalizedNumber().getCountryCode() + "%", "%" +
                        lastDigits, rawNumber},
                BlackListTable.BEGIN_WITH + " DESC");

        try {
            if (cursor != null) {
                List<BlackListNumberEntity> list = new ArrayList<>(cursor.getCount());
                while (cursor.moveToNext()) {
                    list.add(new BlackListNumberEntity(cursor));
                }
                return list;
            }
            return new ArrayList<>(0);
        } finally {
            Util.close(cursor);
        }
    }

    @DebugLog
    public boolean existByNormalizedNumber(String number) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(BlackListTable.CONTENT_URI, new
                            String[]{BlackListTable.UID},
                    BlackListTable.NORMALIZED_NUMBER + " = '" + number + "'", null, null);
            return cursor != null && cursor.getCount() > 0;
        } finally {
            Util.close(cursor);
        }
    }

    @DebugLog
    public void delete(BlackListNumberEntity entity) {
        int rows = context.getContentResolver().delete(entity.getUniqueUri(), null, null);
        if (rows != 1) {
            String error = "Could not delete row in " + BlackListTable.TABLE + " table: ";
            Log.e(TAG, error + entity.toString());
            throw new RuntimeException(error + entity.toString());
        }
    }

    public void updateEnabled(BlackListNumberEntity entity, boolean enabled) {
        ContentValues cv = new ContentValues();
        cv.put(BlackListTable.ENABLED, enabled ? 1 : 0);
        int rows = context.getContentResolver().update(entity.getUniqueUri(), cv, null, null);
        if (rows != 1) {
            String error = "Could not update row in " + BlackListTable.TABLE + " table: ";
            Log.e(TAG, error + entity.toString());
            throw new RuntimeException(error + entity.toString());
        }
    }
}
