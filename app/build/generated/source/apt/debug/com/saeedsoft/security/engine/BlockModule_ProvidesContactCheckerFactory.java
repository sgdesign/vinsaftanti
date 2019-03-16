// Generated by dagger.internal.codegen.ComponentProcessor (https://google.github.io/dagger).
package com.saeedsoft.security.engine;

import com.saeedsoft.security.db.dao.ContactDAO;
import com.saeedsoft.security.service.ContactChecker;
import com.saeedsoft.security.stuff.AppPreferences;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class BlockModule_ProvidesContactCheckerFactory implements Factory<ContactChecker> {
  private final BlockModule module;

  private final Provider<AppPreferences> preferencesProvider;

  private final Provider<ContactDAO> contactDAOProvider;

  public BlockModule_ProvidesContactCheckerFactory(
      BlockModule module,
      Provider<AppPreferences> preferencesProvider,
      Provider<ContactDAO> contactDAOProvider) {
    assert module != null;
    this.module = module;
    assert preferencesProvider != null;
    this.preferencesProvider = preferencesProvider;
    assert contactDAOProvider != null;
    this.contactDAOProvider = contactDAOProvider;
  }

  @Override
  public ContactChecker get() {
    return Preconditions.checkNotNull(
        module.providesContactChecker(preferencesProvider.get(), contactDAOProvider.get()),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<ContactChecker> create(
      BlockModule module,
      Provider<AppPreferences> preferencesProvider,
      Provider<ContactDAO> contactDAOProvider) {
    return new BlockModule_ProvidesContactCheckerFactory(
        module, preferencesProvider, contactDAOProvider);
  }
}
