package com.saeedsoft.security.service;

import com.saeedsoft.security.CallBlocker.PhoneNumberException;
import com.saeedsoft.security.CallBlocker.TooShortNumberException;

import hugo.weaving.DebugLog;

public class MasterChecker {
    private final IChecker[] checkers;

    public MasterChecker(PrivateNumberChecker privateNumberChecker, QuickBlackListChecker
            quickBlackListChecker, BlackListChecker blackListChecker, ContactChecker
                                 contactChecker) {
        this.checkers = new IChecker[]{privateNumberChecker, quickBlackListChecker,
                blackListChecker, contactChecker};
    }

    @DebugLog
    public boolean isBlockable(Call call) throws TooShortNumberException, PhoneNumberException {
        for (IChecker checker : checkers) {
            int res = checker.isBlockable(call);
            switch (res) {
                case IChecker.YES:
                    return true;
                case IChecker.NO:
                    return false;
            }
        }
        return false;
    }

    @DebugLog
    public void doLast() {
        for (IChecker checker : checkers) {
            checker.doLast();
        }
    }

    @DebugLog
    public void refresh() {
        for (IChecker checker : checkers) {
            checker.refresh();
        }
    }
}
