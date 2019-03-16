package com.saeedsoft.security.engine;

import android.content.Context;

import com.saeedsoft.security.db.dao.BlackListDAO;
import com.saeedsoft.security.db.dao.ContactDAO;
import com.saeedsoft.security.service.BlackListWrapper;
import com.saeedsoft.security.service.ImportExportWrapper;
import com.saeedsoft.security.service.NormalizerService;
import com.saeedsoft.security.service.ValidatorService;
import com.saeedsoft.security.stuff.AppPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    AppPreferences providesSettings() {
        return new AppPreferences(context);
    }

    @Provides
    @Singleton
    ValidatorService providerValidatorService() {
        return new ValidatorService(context);
    }

    @Provides
    @Singleton
    NormalizerService providerNormalizerService() {
        return new NormalizerService(context);
    }

    @Provides
    @Singleton
    ImportExportWrapper provideImportExportWrapper(BlackListWrapper blackListWrapper) {
        return new ImportExportWrapper(context, blackListWrapper);
    }

    @Provides
    @Singleton
    BlackListWrapper provideBlackListWrapper(ValidatorService validatorService, NormalizerService
            normalizerService, ContactDAO contactDAO, BlackListDAO blackListDAO, AppPreferences
                                                     appPreferences) {
        return new BlackListWrapper(context, validatorService, normalizerService, contactDAO,
                blackListDAO, appPreferences);
    }
}
