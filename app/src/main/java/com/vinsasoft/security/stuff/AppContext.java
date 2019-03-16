package com.vinsasoft.security.stuff;

import android.app.Application;

//import com.vinsasoft.security.base.BaseApplication;
import com.vinsasoft.security.engine.AllComponent;
import com.vinsasoft.security.engine.AppModule;
import com.vinsasoft.security.engine.BlockModule;
import com.vinsasoft.security.engine.DAOModule;
import com.vinsasoft.security.engine.DaggerAllComponent;
import com.vinsasoft.security.service.BlackListWrapper;
import com.vinsasoft.security.service.ImportExportWrapper;

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
