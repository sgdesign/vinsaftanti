package com.saeedsoft.security.service;

import android.support.annotation.NonNull;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.saeedsoft.security.CallBlocker.BlackListNumberEntity;

import hugo.weaving.DebugLog;

public class MatcherService {
    private static final int NO_MATCH = 0;
    private static final int SOFT_MATCH = 1;
    private static final int STRICT_MATCH = 2;

    @DebugLog
    @SuppressWarnings("SimplifiableIfStatement")
    public boolean isNumberMatch(@NonNull Call call, BlackListNumberEntity blackListNumberEntity) {
        if (call.getNumber() == null || call.getNormalizedNumber() == null) {
            return false;
        }

        if (blackListNumberEntity.isBeginWith()) {
            //This is already checked on db query. If there is any 'beginWith' entity, it means a
            // matching number
            return true;
        }

        int res = check(call, blackListNumberEntity.getNormalizedNumber());
        return res == SOFT_MATCH || res == STRICT_MATCH;
    }

    @DebugLog
    private int check(Call call, String otherNumber) {
        PhoneNumberUtil.MatchType res = PhoneNumberUtil.getInstance().isNumberMatch(call
                .getNormalizedNumber(), otherNumber);
        if (res.equals(PhoneNumberUtil.MatchType.NSN_MATCH) || res.equals(PhoneNumberUtil
                .MatchType.EXACT_MATCH)) {
            return STRICT_MATCH;
        } else if (res.equals(PhoneNumberUtil.MatchType.SHORT_NSN_MATCH)) {
            return SOFT_MATCH;
        }

        return NO_MATCH;
    }
}
