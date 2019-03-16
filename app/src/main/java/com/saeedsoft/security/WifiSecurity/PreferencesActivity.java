package com.saeedsoft.security.WifiSecurity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.saeedsoft.security.Internetconnection;
import com.saeedsoft.security.R;
import com.saeedsoft.security.base.BaseSwipeBackActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.ypyproductions.utils.ApplicationUtils;

import static com.saeedsoft.security.Constant.UpgradePro;

public class PreferencesActivity extends BaseSwipeBackActivity {

    private AdView mAdMobAdView;
    InterstitialAd mInterstitial;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_preferences);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        procheck();


        // Display the preferences fragment as the main content.
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PrefsFragment prefsFragment = new PrefsFragment();
        fragmentTransaction.replace(R.id.inflatable_prefs, prefsFragment);
        fragmentTransaction.commit();

        // Show the location notice if location is disabled
        TextView locationNotice = (TextView) findViewById(R.id.location_notice);
        LocationAccess locationAccess = new LocationAccess();
        if (!locationAccess.isNetworkLocationEnabled(getApplicationContext()))
            locationNotice.setVisibility(View.VISIBLE);
        else
            locationNotice.setVisibility(View.GONE);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void admobbanner(){
        mAdMobAdView = (AdView) findViewById(R.id.admob_adview);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdMobAdView.loadAd(adRequest);
    }

    private void admobint() {

        if (ApplicationUtils.isOnline(this)) {
            boolean b = true;
            if (b) {
                mInterstitial = new InterstitialAd(this);
                mInterstitial.setAdUnitId(getString(R.string.interstitial_ad_unit));
                mInterstitial.loadAd(new AdRequest.Builder().build());

                mInterstitial.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        // TODO Auto-generated method stub
                        super.onAdLoaded();
                        if (mInterstitial.isLoaded()) {
                            mInterstitial.show();
                        }
                    }
                });

            }
        }
    }

    public void procheck()
    {
        boolean strPref = false;
        SharedPreferences shf = this.getSharedPreferences("config", MODE_PRIVATE);
        strPref = shf.getBoolean(UpgradePro, false);

        if(strPref)
        {

            AdView ads1 = (AdView)findViewById(R.id.admob_adview);
            ads1.setVisibility(View.GONE);


        }
        else {

            if (Internetconnection.checkConnection(this)) {

                mAdMobAdView = (AdView) findViewById(R.id.admob_adview);
                AdRequest adRequest = new AdRequest.Builder()
                        .build();
                mAdMobAdView.loadAd(adRequest);

                if (ApplicationUtils.isOnline(this)) {
                    boolean b = true;
                    if (b) {
                        mInterstitial = new InterstitialAd(this);
                        mInterstitial.setAdUnitId(getString(R.string.interstitial_ad_unit));
                        mInterstitial.loadAd(new AdRequest.Builder().build());

                        mInterstitial.setAdListener(new AdListener() {
                            @Override
                            public void onAdLoaded() {
                                // TODO Auto-generated method stub
                                super.onAdLoaded();
                                if (mInterstitial.isLoaded()) {
                                    mInterstitial.show();
                                }
                            }
                        });

                    }
                }

            } else {

                AdView ads1 = (AdView)findViewById(R.id.admob_adview);
                ads1.setVisibility(View.GONE);
            }




        }
    }


    /**
     * Called when location notification is clicked
     */
    public void openLocationNotice(View view) {
        Intent intent = new Intent(this, LocationNoticeActivity.class);
        startActivity(intent);
    }


    /**
     * Fragment that is automatically filled with all preferences described in xml/preferences.xml
     */
    public static class PrefsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferencesss);
            try {
                SharedPreferences prefs = getPreferenceManager().getSharedPreferences();
                prefs.registerOnSharedPreferenceChangeListener(this);
            } catch (NullPointerException npe) {
                Log.e("WifiSecurity", "Null pointer exception when trying to register shared preference change listener");
            }

            // Allow modifying of allowed & blocked APs, via a separate button
            Preference modifyHotspotsPreference = findPreference("modifyHotspots");
            modifyHotspotsPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    modifyHotspots();
                    return true;
                }
            });
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
        {
            // Perform a rescan every time a preference has changed
            Log.v("WifiSecurity", "Initiating rescan because preference " + key + " changed");
            try {
                WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifiManager.startScan();
            } catch (NullPointerException npe) {
                Log.e("WifiSecurity", "Could not get WifiManager from within prefsFragment");
            }
        }

        /**
         * Launch the SSID manager activity
         */
        public void modifyHotspots() {
            Log.v("WifiSecurity", "Launching SSID manager");
            Intent intent = new Intent(getActivity(), SSIDManagerActivity.class);
            startActivity(intent);
        }
    }
    private void internet()
    {
        if (Internetconnection.checkConnection(this)) {
            admobbanner();
            admobint();

        } else {
            AdView ads1 = (AdView)findViewById(R.id.admob_adview);
            ads1.setVisibility(View.GONE);
        }
    }
}