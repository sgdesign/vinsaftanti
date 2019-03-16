// Generated by dagger.internal.codegen.ComponentProcessor (https://google.github.io/dagger).
package com.saeedsoft.security.engine;

import com.saeedsoft.security.service.MatcherService;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class BlockModule_ProviderMatcherServiceFactory implements Factory<MatcherService> {
  private final BlockModule module;

  public BlockModule_ProviderMatcherServiceFactory(BlockModule module) {
    assert module != null;
    this.module = module;
  }

  @Override
  public MatcherService get() {
    return Preconditions.checkNotNull(
        module.providerMatcherService(),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<MatcherService> create(BlockModule module) {
    return new BlockModule_ProviderMatcherServiceFactory(module);
  }
}
