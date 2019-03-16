package com.vinsasoft.security.service;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.vinsasoft.security.db.BlackListTable;
import com.vinsasoft.security.db.dao.BlackListDAO;
import com.vinsasoft.security.db.dao.ContactDAO;
import com.vinsasoft.security.CallBlocker.BlackListNumberEntity;
import com.vinsasoft.security.stuff.AppPreferences;
import com.vinsasoft.security.stuff.PermUtil;

import hugo.weaving.DebugLog;



public class BlackListWrapper {
    private static final String TAG = BlackListWrapper.class.getSimpleName();
    private final ValidatorService validatorService;
    private final NormalizerService normalizerService;
    private final Context context;
    private final ContactDAO contactDAO;
    private final BlackListDAO blackListDAO;
    private final AppPreferences appPreferences;

    public BlackListWrapper(Context context, ValidatorService validatorService, NormalizerService
            normalizerService, ContactDAO contactDAO, BlackListDAO blackListDAO, AppPreferences
                                    appPreferences) {
        this.context = context;
        this.normalizerService = normalizerService;
        this.validatorService = validatorService;
        this.contactDAO = contactDAO;
        this.blackListDAO = blackListDAO;
        this.appPreferences = appPreferences;
    }

    @DebugLog
    public boolean checkUserInput(String number, boolean beginWith) {
        return validatorService.checkUserInput(number, beginWith);
    }

    @DebugLog
    public int addNumberToBlackList(@NonNull String number, @Nullable String displayName, boolean
            beginWith) {
        if (this.validatorService.checkUserInput(number, beginWith)) {
            BlackListNumberEntity entity = new BlackListNumberEntity();
            entity.setBeginWith(beginWith);
            entity.setEnabled(true);
            entity.setDisplayName(displayName);

            if (beginWith) {
                entity.setNormalizedNumber(number);
                entity.setDisplayNumber(number + BlackListTable.BEGIN_WITH_SYMBOL);
            } else {
                String formattedNumber[] = normalizerService.normalizeUserInput(number);
                entity.setNormalizedNumber(formattedNumber[0]);
                entity.setDisplayNumber(formattedNumber[1]);
            }
            if (!blackListDAO.existByNormalizedNumber(entity.getNormalizedNumber())) {
                if (!entity.isBeginWith() && PermUtil.checkReadContacts(context)) {
                    String[] contact = contactDAO.findContact(entity.getNormalizedNumber());
                    if (contact != null) {
                        entity.setDisplayName(contact[1]);
                    }
                }

                ContentValues contentValue = new ContentValues();
                entity.toContentValues(contentValue);
                Uri newUri = context.getContentResolver().insert(BlackListTable.CONTENT_URI,
                        contentValue);
                if (newUri == null) {
                    Log.e(TAG, "Couldn't insert BlackListNumberEntity: " + entity.toString());
                }
                return 1;
            }
        }
        return 0;
    }

    @DebugLog
    public void delete(BlackListNumberEntity entity) {
        blackListDAO.delete(entity);
        refreshService();
    }

    @DebugLog
    public void updateEnabled(BlackListNumberEntity entity, boolean enabled) {
        blackListDAO.updateEnabled(entity, enabled);
        refreshService();
    }

    @DebugLog
    public void deleteAll() {
        context.getContentResolver().delete(BlackListTable.CONTENT_URI, null, null);
        refreshService();
    }

    private void refreshService() {
        if (appPreferences.isBlockingEnable()) {
            Intent i = new Intent(context, CallBlockingService.class);
            i.putExtra(CallBlockingService.DRY, true);
            context.startService(i);
        }
    }
}
