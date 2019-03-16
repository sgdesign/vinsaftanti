package com.vinsasoft.security.engine;

import com.vinsasoft.security.service.CallBlockingService;
import com.vinsasoft.security.stuff.AppContext;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {BlockModule.class, AppModule.class, DAOModule.class})
public interface AllComponent {
    void inject(AppContext appContext);

    void inject(CallBlockingService callBlockingService);
}
