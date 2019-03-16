package com.saeedsoft.security.CallBlocker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
//import android.support.annotation.RequiresApi;
import android.view.MenuItem;

//import com.saeedsoft.security.R;
import com.saeedsoft.security.R;
import com.saeedsoft.security.service.CallBlockingService;
import com.saeedsoft.security.stuff.AppContext;
import com.saeedsoft.security.stuff.AppPreferences;
import com.saeedsoft.security.stuff.PermUtil;

@SuppressWarnings("deprecation")
public class BlockingSettingsActivity extends PreferenceActivity implements Preference
        .OnPreferenceChangeListener {
    private CheckBoxPreference pref;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.blocking_preferences);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        pref = (CheckBoxPreference) getPreferenceManager().findPreference(AppPreferences
                .ALLOW_ONLY_CONTACTS);
        pref.setOnPreferenceChangeListener(this);

        preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                AppContext appContext = (AppContext) getApplicationContext();

                if (key.equals(AppPreferences.BLOCKING_ENABLED)) {
                    Intent i = new Intent(BlockingSettingsActivity.this, CallBlockingService.class);
                    if (appContext.getAppPreferences().isBlockingEnable()) {
                        i.putExtra(CallBlockingService.DRY, true);
                        startService(i);
                    } else {
                        stopService(i);
                    }
                }
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        boolean newVal = (boolean) newValue;
        if (newVal && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (!PermUtil.checkReadContacts(this)) {
                requestPermissions(new String[]{PermUtil.READ_CONTACTS}, 0);
                return false;
            }
        }

        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }
}
