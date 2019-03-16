package com.vinsasoft.security.service;

import com.vinsasoft.security.db.dao.BlackListDAO;
import com.vinsasoft.security.CallBlocker.BlackListNumberEntity;
import com.vinsasoft.security.CallBlocker.PhoneNumberException;
import com.vinsasoft.security.CallBlocker.TooShortNumberException;
import com.vinsasoft.security.stuff.AppPreferences;
import com.vinsasoft.security.stuff.BlockOrigin;

import java.util.List;

import hugo.weaving.DebugLog;



public class BlackListChecker implements IChecker {
    private final AppPreferences appPreferences;
    private final MatcherService matcherService;
    private final BlackListDAO blackListDAO;
    private final NormalizerService normalizerService;

    public BlackListChecker(AppPreferences appPreferences, MatcherService matcherService,
                            BlackListDAO blackListDAO, NormalizerService normalizerService) {
        this.appPreferences = appPreferences;
        this.matcherService = matcherService;
        this.blackListDAO = blackListDAO;
        this.normalizerService = normalizerService;
    }

    @Override
    @DebugLog
    public int isBlockable(Call call) throws TooShortNumberException, PhoneNumberException {
        final boolean enableBlackList = appPreferences.isEnableBlackList();
        if (!call.isPrivateNumber() && enableBlackList) {
            normalizerService.normalizeCall(call);
            final List<BlackListNumberEntity> list = blackListDAO.findForBlock(call);
            for (BlackListNumberEntity entity : list) {
                boolean match = matcherService.isNumberMatch(call, entity);
                if (match) {
                    call.getExtraData().put("displayName", entity.getDisplayName());
                    call.setBlockOrigin(BlockOrigin.BLACK_LIST);
                    appPreferences.setQuick(entity.getUid() + ":" + call.getNumber());
                    return IChecker.YES;
                }
            }
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
