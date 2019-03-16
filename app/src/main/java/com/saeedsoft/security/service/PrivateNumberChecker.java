package com.saeedsoft.security.service;

import com.saeedsoft.security.stuff.AppPreferences;
import com.saeedsoft.security.stuff.BlockOrigin;

import hugo.weaving.DebugLog;
public class PrivateNumberChecker implements IChecker {
    private final AppPreferences appPreferences;

    public PrivateNumberChecker(AppPreferences appPreferences) {
        this.appPreferences = appPreferences;
    }

    @Override
    @DebugLog
    public int isBlockable(Call call) {
        final boolean block = appPreferences.isBlockPrivateNumbers();
        if (call.isPrivateNumber() && block) {
            call.setBlockOrigin(BlockOrigin.PRIVATE);
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
