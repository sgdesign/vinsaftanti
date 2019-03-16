package com.saeedsoft.security.CallBlocker;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MenuItem;

//import com.saeedsoft.security.R;
import com.saeedsoft.security.R;
import com.saeedsoft.security.stuff.AppContext;
import com.saeedsoft.security.stuff.AppPreferences;

public class AllSettingsActivity extends PreferenceActivity {
    private static final String TAG = AllSettingsActivity.class.getSimpleName();
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //noinspection deprecation
        addPreferencesFromResource(R.xml.preferencess);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        @SuppressWarnings("deprecation") Preference submitDebugLog = this.findPreference
                ("pref_submit_debug_logs");

        submitDebugLog.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //final Intent intent = new Intent(AllSettingsActivity.this, SendLogActivity.class);
                //startActivity(intent);
                return true;
            }
        });
        submitDebugLog.setSummary(getVersion());


        preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                AppContext appContext = (AppContext) getApplicationContext();
                if (key.equals(AppPreferences.LESSON_1)) {
                    if (!appContext.getAppPreferences().isLesson1()) {
                        finish();
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
    protected void onResume() {
        super.onResume();
        //noinspection deprecation
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //noinspection deprecation
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }


    @NonNull
    private String getVersion() {
        try {
            String app = this.getString(R.string.app_name);
            String version = this.getPackageManager().getPackageInfo(this.getPackageName(),
                    0).versionName;

            return String.format("%s %s", app, version);
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, e);
            return this.getString(R.string.app_name);
        }
    }
}
