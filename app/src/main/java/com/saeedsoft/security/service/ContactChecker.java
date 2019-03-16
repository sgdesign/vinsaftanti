package com.saeedsoft.security.service;

import android.content.Context;

import com.saeedsoft.security.db.dao.ContactDAO;
import com.saeedsoft.security.stuff.AppPreferences;
import com.saeedsoft.security.stuff.BlockOrigin;
import com.saeedsoft.security.stuff.PermUtil;

import hugo.weaving.DebugLog;



public class ContactChecker implements IChecker {
    private final Context context;
    private final AppPreferences appPreferences;
    private final ContactDAO contactDAO;

    public ContactChecker(Context context, AppPreferences appPreferences, ContactDAO contactDAO) {
        this.context = context;
        this.appPreferences = appPreferences;
        this.contactDAO = contactDAO;
    }

    @Override
    @DebugLog
    public int isBlockable(Call call) {
        final boolean allowOnlyContacts = appPreferences.isAllowOnlyContacts();
        if (allowOnlyContacts && PermUtil.checkReadContacts(context)) {
            call.setBlockOrigin(BlockOrigin.CONTACTS);
            if (!call.isPrivateNumber()) {
                if (contactDAO.findContact(call.getNumber()) != null) {
                    return IChecker.NO;
                }
            }
            return IChecker.YES;
        }
        return IChecker.NONE;
    }

    @Override
    public void doLast() {
        //do nothing
    }

    @Override
    public void refresh() {
        //do nothing
    }
}
