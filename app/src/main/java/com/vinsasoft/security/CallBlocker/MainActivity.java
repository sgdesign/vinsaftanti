package com.vinsasoft.security.CallBlocker;

import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.app.AlertDialog;
//import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.vinsasoft.security.BuildConfig;
import com.vinsasoft.security.Internetconnection;
import com.vinsasoft.security.R;
//import com.vinsasoft.security.service.CallBlockingService;
import com.vinsasoft.security.base.BaseSwipeBackActivity;
import com.vinsasoft.security.db.LogTable;
import com.vinsasoft.security.service.CallBlockingService;
import com.vinsasoft.security.stuff.AppContext;
import com.vinsasoft.security.stuff.AppPreferences;
import com.vinsasoft.security.stuff.PermUtil;
import com.vinsasoft.security.stuff.Util;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.ypyproductions.utils.ApplicationUtils;

import java.util.ArrayList;
import java.util.List;

import hugo.weaving.DebugLog;

import static com.vinsasoft.security.Constant.UpgradePro;

public class MainActivity extends BaseSwipeBackActivity implements ViewPager.OnPageChangeListener,
        View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERM_CODE_READ_CONTACTS_FOR_ADDING = 2;


    private View coordinatorLayout;
    private AppPreferences appPreferences;
    private FloatingActionButton fab;

    private LinearLayout ads1;
    private Toolbar toolbar;
    private TabLayout tabLayout;

    private AdView mAdMobAdView;

    InterstitialAd mInterstitial;

    @Override
    @DebugLog
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.main);
        getActionBar().setDisplayHomeAsUpEnabled(true);





        boolean strPref = false;
        SharedPreferences shf = this.getSharedPreferences("config", MODE_PRIVATE);
        strPref = shf.getBoolean(UpgradePro, false);

        if(strPref)
        {
            LinearLayout ads1 = (LinearLayout)findViewById(R.id.ads1);
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
                LinearLayout ads1 = (LinearLayout)findViewById(R.id.ads1);
                ads1.setVisibility(View.GONE);
            }





        }










        coordinatorLayout = findViewById(R.id.main_coordinator_layout);
        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        BlackListFragment blackListFragment = new BlackListFragment();
        LogListFragment logListFragment = new LogListFragment();
        adapter.addFragment(blackListFragment, getResources().getString(R.string
                .common_black_list));
        adapter.addFragment(logListFragment, getResources().getString(R.string.common_log));
        viewPager.addOnPageChangeListener(this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 1:
                        fab.hide();
                        break;

                    default:
                        fab.show();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        fab = (FloatingActionButton) findViewById(R.id.mainFloatingActionButton);
        fab.setOnClickListener(this);

        AppContext appContext = (AppContext) getApplicationContext();
        appPreferences = appContext.getAppPreferences();
        if (appPreferences.isFirstTime()) {
            appPreferences.setAllDefaults();
            appPreferences.setFirstTime(false);

            Intent i = new Intent(getApplicationContext(), CallBlockingService.class);
            i.putExtra(CallBlockingService.DRY, true);
            startService(i);
        }

    }


    @Override
    @DebugLog
    public void onResume() {
        super.onResume();
        if (appPreferences.isBlockingEnable() && !Util.isServiceRunning(this, CallBlockingService
                .class)) {
            if (BuildConfig.DEBUG)
                Log.d(TAG, "starting " + CallBlockingService.class.getSimpleName());
            Intent i = new Intent(getApplicationContext(), CallBlockingService.class);
            i.putExtra(CallBlockingService.DRY, true);
            startService(i);
        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        super.getMenuInflater().inflate(R.menu.mainnn, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.callLogDeleteAll:
                AlertDialog.Builder builder = new AlertDialog.Builder(this, 0);
                builder.setTitle(R.string.delete_log_title);
                builder.setMessage(R.string.delete_log_message);
                builder.setPositiveButton(R.string.common_delete, new DialogInterface.OnClickListener
                        () {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getContentResolver().delete(LogTable.CONTENT_URI, null, null);
                    }
                });
                builder.setNegativeButton(R.string.common_cancel, new DialogInterface
                        .OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
                return true;


            case R.id.mainMnAllSettings:
                startActivity(new Intent(MainActivity.this, AllSettingsActivity.class));
                return true;

            case android.R.id.home:
                finish();
                return true;

                case R.id.mainMnBlockingSettings:
                startActivity(new Intent(MainActivity.this, BlockingSettingsActivity.class));
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    @DebugLog
    public void onClick(final View v) {
        Object posObj = v.getTag();
        final int position = posObj == null ? 0 : (int) posObj;

        switch (position) {
            case 0:
                AlertDialog.Builder menuBuilder = new AlertDialog.Builder(this);
                menuBuilder.setTitle(R.string.main_menu_title_add_new)
                        .setItems(R.array.main_menu_add, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent newActivity = new Intent(MainActivity.this, AddActivity
                                        .class);
                                switch (which) {
                                    case 0:
                                        newActivity.putExtra(AddActivity.FRAGMENT_KEY,
                                                AddActivity.FRAGMENT_MANUAL);
                                        startActivity(newActivity);
                                        return;
                                    case 1:
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            if (!PermUtil.checkReadContacts(MainActivity.this)) {
                                                MainActivity.this.requestPermissions(new
                                                                String[]{PermUtil.READ_CONTACTS},
                                                        PERM_CODE_READ_CONTACTS_FOR_ADDING);
                                                return;
                                            }
                                        }
                                        doAddingFromContacts();
                                        return;
                                    case 2:
                                        newActivity.putExtra(AddActivity.FRAGMENT_KEY,
                                                AddActivity.FRAGMENT_CALLLOG);
                                        startActivity(newActivity);
                                        return;
                                    default:
                                        throw new RuntimeException("Should not be here");
                                }
                            }
                        });
                menuBuilder.show();
                return;
            default:
                throw new IllegalArgumentException("Should not be here");
        }


    }

    @DebugLog
    private void doAddingFromContacts() {
        Intent newActivity = new Intent(MainActivity.this, AddActivity.class);
        newActivity.putExtra(AddActivity.FRAGMENT_KEY, AddActivity.FRAGMENT_CONTACTS);
        startActivity(newActivity);
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }



}

