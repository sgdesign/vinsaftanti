package com.saeedsoft.security.engine;

import android.content.Context;

import com.saeedsoft.security.db.dao.BlackListDAO;
import com.saeedsoft.security.db.dao.ContactDAO;
import com.saeedsoft.security.service.BlackListChecker;
import com.saeedsoft.security.service.BlockWrapper;
import com.saeedsoft.security.service.ContactChecker;
import com.saeedsoft.security.service.EndCallService;
import com.saeedsoft.security.service.MasterChecker;
import com.saeedsoft.security.service.MatcherService;
import com.saeedsoft.security.service.NormalizerService;
import com.saeedsoft.security.service.PrivateNumberChecker;
import com.saeedsoft.security.service.QuickBlackListChecker;
import com.saeedsoft.security.stuff.AppPreferences;

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
