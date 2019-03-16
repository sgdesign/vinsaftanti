package com.vinsasoft.security.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.vinsasoft.security.stuff.Util;

import hugo.weaving.DebugLog;


public class ContactDAO {
    private final Context context;

    public ContactDAO(Context context) {
        this.context = context;
    }

    @DebugLog
    public String[] findContact(String number) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri
                .encode(number));
        Cursor cursor = context.getContentResolver().query(uri,
                new String[]{ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup
                        .DISPLAY_NAME},
                null, null, null);

        try {
            String contact[] = null;
            if (cursor != null && cursor.moveToNext()) {
                int displayNameIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup
                        .DISPLAY_NAME);
                int numberIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.NUMBER);
                contact = new String[]{cursor.getString(numberIndex), cursor.getString
                        (displayNameIndex)};
            }
            return contact;
        } finally {
            Util.close(cursor);
        }
    }
}
