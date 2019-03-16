// Generated by dagger.internal.codegen.ComponentProcessor (https://google.github.io/dagger).
package com.saeedsoft.security.engine;

import com.saeedsoft.security.db.dao.BlackListDAO;
import com.saeedsoft.security.db.dao.ContactDAO;
import com.saeedsoft.security.service.BlackListChecker;
import com.saeedsoft.security.service.BlackListWrapper;
import com.saeedsoft.security.service.BlockWrapper;
import com.saeedsoft.security.service.CallBlockingService;
import com.saeedsoft.security.service.CallBlockingService_MembersInjector;
import com.saeedsoft.security.service.ContactChecker;
import com.saeedsoft.security.service.EndCallService;
import com.saeedsoft.security.service.ImportExportWrapper;
import com.saeedsoft.security.service.MasterChecker;
import com.saeedsoft.security.service.MatcherService;
import com.saeedsoft.security.service.NormalizerService;
import com.saeedsoft.security.service.PrivateNumberChecker;
import com.saeedsoft.security.service.QuickBlackListChecker;
import com.saeedsoft.security.service.ValidatorService;
import com.saeedsoft.security.stuff.AppContext;
import com.saeedsoft.security.stuff.AppContext_MembersInjector;
import com.saeedsoft.security.stuff.AppPreferences;
import dagger.MembersInjector;
import dagger.internal.DoubleCheck;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class DaggerAllComponent implements AllComponent {
  private Provider<AppPreferences> providesSettingsProvider;

  private Provider<ValidatorService> providerValidatorServiceProvider;

  private Provider<NormalizerService> providerNormalizerServiceProvider;

  private Provider<ContactDAO> providesContactDAOProvider;

  private Provider<BlackListDAO> providesBlackListDAOProvider;

  private Provider<BlackListWrapper> provideBlackListWrapperProvider;

  private Provider<ImportExportWrapper> provideImportExportWrapperProvider;

  private MembersInjector<AppContext> appContextMembersInjector;

  private Provider<PrivateNumberChecker> providesPrivateNumberCheckerProvider;

  private Provider<QuickBlackListChecker> providesQuickBlackListCheckerProvider;

  private Provider<MatcherService> providerMatcherServiceProvider;

  private Provider<BlackListChecker> providesBlackListCheckerProvider;

  private Provider<ContactChecker> providesContactCheckerProvider;

  private Provider<MasterChecker> providesMasterCheckerProvider;

  private Provider<EndCallService> provideEndCallServiceProvider;

  private Provider<BlockWrapper> provideBlockWrapperProvider;

  private MembersInjector<CallBlockingService> callBlockingServiceMembersInjector;

  private DaggerAllComponent(Builder builder) {
    assert builder != null;
    initialize(builder);
  }

  public static Builder builder() {
    return new Builder();
  }

  @SuppressWarnings("unchecked")
  private void initialize(final Builder builder) {

    this.providesSettingsProvider =
        DoubleCheck.provider(AppModule_ProvidesSettingsFactory.create(builder.appModule));

    this.providerValidatorServiceProvider =
        DoubleCheck.provider(AppModule_ProviderValidatorServiceFactory.create(builder.appModule));

    this.providerNormalizerServiceProvider =
        DoubleCheck.provider(AppModule_ProviderNormalizerServiceFactory.create(builder.appModule));

    this.providesContactDAOProvider =
        DoubleCheck.provider(DAOModule_ProvidesContactDAOFactory.create(builder.dAOModule));

    this.providesBlackListDAOProvider =
        DoubleCheck.provider(DAOModule_ProvidesBlackListDAOFactory.create(builder.dAOModule));

    this.provideBlackListWrapperProvider =
        DoubleCheck.provider(
            AppModule_ProvideBlackListWrapperFactory.create(
                builder.appModule,
                providerValidatorServiceProvider,
                providerNormalizerServiceProvider,
                providesContactDAOProvider,
                providesBlackListDAOProvider,
                providesSettingsProvider));

    this.provideImportExportWrapperProvider =
        DoubleCheck.provider(
            AppModule_ProvideImportExportWrapperFactory.create(
                builder.appModule, provideBlackListWrapperProvider));

    this.appContextMembersInjector =
        AppContext_MembersInjector.create(
            providesSettingsProvider,
            provideImportExportWrapperProvider,
            provideBlackListWrapperProvider);

    this.providesPrivateNumberCheckerProvider =
        DoubleCheck.provider(
            BlockModule_ProvidesPrivateNumberCheckerFactory.create(
                builder.blockModule, providesSettingsProvider));

    this.providesQuickBlackListCheckerProvider =
        DoubleCheck.provider(
            BlockModule_ProvidesQuickBlackListCheckerFactory.create(
                builder.blockModule, providesSettingsProvider));

    this.providerMatcherServiceProvider =
        DoubleCheck.provider(BlockModule_ProviderMatcherServiceFactory.create(builder.blockModule));

    this.providesBlackListCheckerProvider =
        DoubleCheck.provider(
            BlockModule_ProvidesBlackListCheckerFactory.create(
                builder.blockModule,
                providesSettingsProvider,
                providerMatcherServiceProvider,
                providesBlackListDAOProvider,
                providerNormalizerServiceProvider));

    this.providesContactCheckerProvider =
        DoubleCheck.provider(
            BlockModule_ProvidesContactCheckerFactory.create(
                builder.blockModule, providesSettingsProvider, providesContactDAOProvider));

    this.providesMasterCheckerProvider =
        DoubleCheck.provider(
            BlockModule_ProvidesMasterCheckerFactory.create(
                builder.blockModule,
                providesPrivateNumberCheckerProvider,
                providesQuickBlackListCheckerProvider,
                providesBlackListCheckerProvider,
                providesContactCheckerProvider));

    this.provideEndCallServiceProvider =
        DoubleCheck.provider(BlockModule_ProvideEndCallServiceFactory.create(builder.blockModule));

    this.provideBlockWrapperProvider =
        DoubleCheck.provider(
            BlockModule_ProvideBlockWrapperFactory.create(
                builder.blockModule,
                providesMasterCheckerProvider,
                provideEndCallServiceProvider,
                providerNormalizerServiceProvider,
                providesSettingsProvider));

    this.callBlockingServiceMembersInjector =
        CallBlockingService_MembersInjector.create(provideBlockWrapperProvider);
  }

  @Override
  public void inject(AppContext appContext) {
    appContextMembersInjector.injectMembers(appContext);
  }

  @Override
  public void inject(CallBlockingService callBlockingService) {
    callBlockingServiceMembersInjector.injectMembers(callBlockingService);
  }

  public static final class Builder {
    private AppModule appModule;

    private DAOModule dAOModule;

    private BlockModule blockModule;

    private Builder() {}

    public AllComponent build() {
      if (appModule == null) {
        throw new IllegalStateException(AppModule.class.getCanonicalName() + " must be set");
      }
      if (dAOModule == null) {
        throw new IllegalStateException(DAOModule.class.getCanonicalName() + " must be set");
      }
      if (blockModule == null) {
        throw new IllegalStateException(BlockModule.class.getCanonicalName() + " must be set");
      }
      return new DaggerAllComponent(this);
    }

    public Builder blockModule(BlockModule blockModule) {
      this.blockModule = Preconditions.checkNotNull(blockModule);
      return this;
    }

    public Builder appModule(AppModule appModule) {
      this.appModule = Preconditions.checkNotNull(appModule);
      return this;
    }

    public Builder dAOModule(DAOModule dAOModule) {
      this.dAOModule = Preconditions.checkNotNull(dAOModule);
      return this;
    }
  }
}
