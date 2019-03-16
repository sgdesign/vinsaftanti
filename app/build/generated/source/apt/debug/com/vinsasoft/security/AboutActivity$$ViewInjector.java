// Generated code from Butter Knife. Do not modify!
package com.vinsasoft.security;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class AboutActivity$$ViewInjector {
  public static void inject( Finder finder, final com.vinsasoft.security.AboutActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296733, "field 'subVersion'");
    target.subVersion = (android.widget.TextView) view;
  }

  public static void reset(com.vinsasoft.security.AboutActivity target) {
    target.subVersion = null;
  }
}
