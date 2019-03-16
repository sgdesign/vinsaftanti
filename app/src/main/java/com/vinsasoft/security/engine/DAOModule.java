package com.vinsasoft.security.engine;

import android.content.Context;

import com.vinsasoft.security.db.dao.BlackListDAO;
import com.vinsasoft.security.db.dao.ContactDAO;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class DAOModule {

    private final Context context;

    public DAOModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    ContactDAO providesContactDAO() {
        return new ContactDAO(context);
    }

    @Provides
    @Singleton
    BlackListDAO providesBlackListDAO() {
        return new BlackListDAO(context);
    }
}
