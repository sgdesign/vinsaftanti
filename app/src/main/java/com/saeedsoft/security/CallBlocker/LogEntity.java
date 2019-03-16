package com.saeedsoft.security.CallBlocker;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.saeedsoft.security.db.LogTable;
import com.saeedsoft.security.stuff.BlockOrigin;

public class LogEntity {
    private Long uid;
    private String callerID;
    private String displayNumber;
    private String displayName;
    private long time;
    private BlockOrigin blockOrigin;

    public LogEntity() {
    }

    public LogEntity(Cursor cur) {
        for (String colName : cur.getColumnNames()) {
            int colIndex = cur.getColumnIndexOrThrow(colName);
            switch (colName) {
                case LogTable.CALLER_ID:
                    this.setCallerID(cur.getString(colIndex));
                    break;
                case LogTable.DISPLAY_NUMBER:
                    this.setDisplayNumber(cur.getString(colIndex));
                    break;
                case LogTable.DATE:
                    this.setTime(cur.getLong(colIndex));
                    break;
                case LogTable.BLOCK_ORIGIN:
                    this.setBlockOrigin(BlockOrigin.valueOf(cur.getString(colIndex)));
                    break;
                case LogTable.UID:
                    this.setUid(cur.getLong(colIndex));
                    break;
                case LogTable.DISPLAY_NAME:
                    this.setDisplayName(cur.getString(colIndex));
                    break;
            }
        }
    }

    public String getCallerID() {
        return callerID;
    }

    public void setCallerID(String callerID) {
        this.callerID = callerID;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDisplayNumber() {
        return displayNumber;
    }

    public void setDisplayNumber(String displayNumber) {
        this.displayNumber = displayNumber;
    }

    public BlockOrigin getBlockOrigin() {
        return blockOrigin;
    }

    public void setBlockOrigin(BlockOrigin blockOrigin) {
        this.blockOrigin = blockOrigin;
    }

    @SuppressWarnings("unused")
    public Long getUid() {
        return uid;
    }

    @SuppressWarnings("WeakerAccess")
    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void toContentValues(@NonNull ContentValues contentValue) {
        if (uid != null) contentValue.put(LogTable.UID, uid);
        contentValue.put(LogTable.CALLER_ID, callerID);
        contentValue.put(LogTable.DISPLAY_NUMBER, displayNumber);
        contentValue.put(LogTable.DISPLAY_NAME, displayName);
        contentValue.put(LogTable.DATE, Long.toString(time));
        contentValue.put(LogTable.BLOCK_ORIGIN, blockOrigin.toString());
    }

    @Override
    public String toString() {
        return "LogEntity{" +
                "blockOrigin=" + blockOrigin +
                ", uid=" + uid +
                ", callerID='" + callerID + '\'' +
                ", displayNumber='" + displayNumber + '\'' +
                ", displayName='" + displayName + '\'' +
                ", time=" + time +
                '}';
    }
}