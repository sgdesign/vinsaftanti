package com.saeedsoft.security.stuff;

import android.app.Application;

//import com.saeedsoft.security.base.BaseApplication;
import com.saeedsoft.security.engine.AllComponent;
import com.saeedsoft.security.engine.AppModule;
import com.saeedsoft.security.engine.BlockModule;
import com.saeedsoft.security.engine.DAOModule;
import com.saeedsoft.security.engine.DaggerAllComponent;
import com.saeedsoft.security.service.BlackListWrapper;
import com.saeedsoft.security.service.ImportExportWrapper;

import javax.inject.Inject;

import dagger.Lazy;

public class AppContext extends Application {
    public static AllComponent dagger;
    @Inject
    AppPreferences appPreferences;
    @Inject
    Lazy<ImportExportWrapper> importExportWrapper;
    @Inject
    Lazy<BlackListWrapper> blackListWrapper;

    @Override
    public void onCreate() {
        super.onCreate();
        dagger = DaggerAllComponent.builder().blockModule(new BlockModule(this)).appModule(new
                AppModule(this)).dAOModule(new DAOModule(this)).build();
        dagger.inject(this);

    }

    public AppPreferences getAppPreferences() {
        return appPreferences;
    }

    public ImportExportWrapper getImportExportWrapper() {
        return importExportWrapper.get();
    }

    public BlackListWrapper getBlackListWrapper() {
        return blackListWrapper.get();
    }




}
