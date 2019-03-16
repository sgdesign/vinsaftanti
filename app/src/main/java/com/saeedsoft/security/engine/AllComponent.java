package com.saeedsoft.security.engine;

import com.saeedsoft.security.service.CallBlockingService;
import com.saeedsoft.security.stuff.AppContext;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {BlockModule.class, AppModule.class, DAOModule.class})
public interface AllComponent {
    void inject(AppContext appContext);

    void inject(CallBlockingService callBlockingService);
}
