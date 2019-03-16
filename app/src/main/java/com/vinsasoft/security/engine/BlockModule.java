package com.vinsasoft.security.engine;

import android.content.Context;

import com.vinsasoft.security.db.dao.BlackListDAO;
import com.vinsasoft.security.db.dao.ContactDAO;
import com.vinsasoft.security.service.BlackListChecker;
import com.vinsasoft.security.service.BlockWrapper;
import com.vinsasoft.security.service.ContactChecker;
import com.vinsasoft.security.service.EndCallService;
import com.vinsasoft.security.service.MasterChecker;
import com.vinsasoft.security.service.MatcherService;
import com.vinsasoft.security.service.NormalizerService;
import com.vinsasoft.security.service.PrivateNumberChecker;
import com.vinsasoft.security.service.QuickBlackListChecker;
import com.vinsasoft.security.stuff.AppPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class BlockModule {
    private final Context context;

    public BlockModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    PrivateNumberChecker providesPrivateNumberChecker(AppPreferences preferences) {
        return new PrivateNumberChecker(preferences);
    }

    @Provides
    @Singleton
    QuickBlackListChecker providesQuickBlackListChecker(AppPreferences preferences) {
        return new QuickBlackListChecker(context, preferences);
    }

    @Provides
    @Singleton
    BlackListChecker providesBlackListChecker(AppPreferences preferences, MatcherService
            matcherService, BlackListDAO blackListDAO, NormalizerService normalizerService) {
        return new BlackListChecker(preferences, matcherService, blackListDAO, normalizerService);
    }

    @Provides
    @Singleton
    ContactChecker providesContactChecker(AppPreferences preferences, ContactDAO contactDAO) {
        return new ContactChecker(context, preferences, contactDAO);
    }

    @Provides
    @Singleton
    MasterChecker providesMasterChecker(PrivateNumberChecker privateNumberChecker,
                                        QuickBlackListChecker quickBlackListChecker,
                                        BlackListChecker blackListChecker,
                                        ContactChecker contactChecker) {
        return new MasterChecker(privateNumberChecker, quickBlackListChecker, blackListChecker,
                contactChecker);
    }

    @Provides
    @Singleton
    MatcherService providerMatcherService() {
        return new MatcherService();
    }

    @Provides
    @Singleton
    EndCallService provideEndCallService() {
        return new EndCallService(context);
    }

    @Provides
    @Singleton
    BlockWrapper provideBlockWrapper(MasterChecker masterChecker, EndCallService
            endCallService, NormalizerService normalizerService, AppPreferences appPreferences) {
        return new BlockWrapper(context, masterChecker, endCallService, normalizerService,
                appPreferences);
    }

}
