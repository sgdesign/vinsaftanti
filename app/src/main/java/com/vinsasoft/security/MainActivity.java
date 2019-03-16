package com.vinsasoft.security;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.ronash.pushe.Pushe;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.vinsasoft.security.AntiVirus.VirusScan;
import com.vinsasoft.security.Battery.BatterySaver;
import com.vinsasoft.security.WifiSecurity.PreferencesActivity;
import com.vinsasoft.security.applocker.EnterActivity;
import com.vinsasoft.security.utils.NotificationUtils;
import com.github.martarodriguezm.rateme.OnRatingListener;
import com.github.martarodriguezm.rateme.RateMeDialog;
import com.github.martarodriguezm.rateme.RateMeDialogTimer;
import com.google.firebase.messaging.FirebaseMessaging;

//import butterknife.InjectView;

import static com.vinsasoft.security.Constant.UpgradePro;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private BroadcastReceiver mRegistrationBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Pushe.initialize(this,true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int launchTimes = 8;
        final int installDate = 4;

        RateMeDialogTimer.onStart(this);
        if (RateMeDialogTimer.shouldShowRateDialog(this, installDate, launchTimes)) {
            showCustomRateMeDialog();
        }


        final String PREFS_NAME = "notification";

        SharedPreferences noti = getSharedPreferences(PREFS_NAME, 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(getResources().getColor(R.color.HighRiskColor));


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (noti.getBoolean("first_notificationn", true)) {
            workwithfirebase();
            //finish();
        } else {


        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.header, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.Setting:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    private void workwithfirebase()
    {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(Constant.REGISTRATION_COMPLETE)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(Constant.TOPIC_GLOBAL);

                    final String PREFS_NAME = "notification";
                    SharedPreferences noti = getSharedPreferences(PREFS_NAME, 0);
                    noti.edit().putBoolean("first_notificationn", false).apply();
                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Constant.PUSH_NOTIFICATION)) {

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                }
            }
        };

        displayFirebaseRegId();
    }


    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constant.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(Constant.TAG, "Firebase reg id: " + regId);

    }

    @Override
    public void onResume() {
        super.onResume();
        procheck();
        fillData();

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constant.REGISTRATION_COMPLETE));

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constant.PUSH_NOTIFICATION));

        NotificationUtils.clearNotifications(this);


    }
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private void fillData() {
        Button ClickUpgrade = (Button) findViewById(R.id.upgrade);
        ClickUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                launchupgrade();
            }
        });

        RelativeLayout ClickVirus = (RelativeLayout) findViewById(R.id.card1);
        ClickVirus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                launchantivirus();
            }
        });

        RelativeLayout antiTheft = (RelativeLayout) findViewById(R.id.card2);
        antiTheft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                launchantiTheft();
            }
        });

        RelativeLayout appLocker = (RelativeLayout) findViewById(R.id.card3);
        appLocker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                launchappLocker();
            }
        });

        RelativeLayout wifiSecurity = (RelativeLayout) findViewById(R.id.card4);
        wifiSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                launchwifiSecurity();
            }
        });

        RelativeLayout callBlocker = (RelativeLayout) findViewById(R.id.card5);
        callBlocker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                launchcallBlocker();
            }
        });

        RelativeLayout settingActivity = (RelativeLayout) findViewById(R.id.card6);
        settingActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                launchsettingActivity();
            }
        });
    }

    private void launchupgrade() {

        Intent intent = new Intent(this, ProActivity.class);
        startActivity(intent);
    }

    private void launchantivirus() {

        Intent intent = new Intent(this, VirusScan.class);
        startActivity(intent);
    }
    private void launchantiTheft() {

        Intent intent = new Intent(this, PhoneSafeActivity.class);
        startActivity(intent);
    }
    private void launchappLocker() {

        Intent intent = new Intent(this, EnterActivity.class);
        startActivity(intent);
    }
    private void launchwifiSecurity() {

        Intent intent = new Intent(this, PreferencesActivity.class);
        startActivity(intent);
    }
    private void launchcallBlocker() {

        Intent intent = new Intent(this, com.vinsasoft.security.CallBlocker.MainActivity.class);
        startActivity(intent);
    }
    private void launchsettingActivity() {

        Intent intent = new Intent(this, BatterySaver.class);
        startActivity(intent);
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
       }
    }


    @Override
    public void onDestroy() {

        super.onDestroy();
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_virusscan) {
            startActivity(new Intent(this, VirusScan.class));
        } else if (id == R.id.nav_antitheft) {
            startActivity(new Intent(this, PhoneSafeActivity.class));
        } else if (id == R.id.nav_applock) {
            startActivity(new Intent(this, EnterActivity.class));
        } else if (id == R.id.nav_wifisecurity) {
            startActivity(new Intent(this, PreferencesActivity.class));
        } else if (id == R.id.nav_callblock) {
            startActivity(new Intent(this, com.vinsasoft.security.CallBlocker.MainActivity.class));
        } else if (id == R.id.nav_battersaver) {
            startActivity(new Intent(this, BatterySaver.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, AboutActivity.class));
        } else if (id == R.id.nav_terms) {
            startActivity(new Intent(this, PrivacyPolicyActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void procheck()
    {
        boolean strPref = false;
        SharedPreferences shf = this.getSharedPreferences("config", MODE_PRIVATE);
        strPref = shf.getBoolean(UpgradePro, false);

        if(strPref)
        {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            ImageView iconmain = (ImageView) findViewById(R.id.iconmain);
            iconmain.setImageResource(R.drawable.shield_protected_icon);
            LinearLayout background = (LinearLayout) findViewById(R.id.background);
            background.setBackgroundColor(getResources().getColor(R.color.mainEnd));
            TextView textmain = (TextView) findViewById(R.id.textmain);
            textmain.setText(R.string.protected_home);
            Button upgrade = (Button) findViewById(R.id.upgrade);

            toolbar.setBackgroundColor(getResources().getColor(R.color.mainEnd));
            upgrade.setVisibility(View.GONE);


        }
        else {


        }
    }

    private void showCustomRateMeDialog() {
        new RateMeDialog.Builder(getPackageName(), getString(R.string.app_name))
                .setHeaderBackgroundColor(getResources().getColor(R.color.mainEnd))
                .setBodyBackgroundColor(getResources().getColor(R.color.black_gray))
                .setBodyTextColor(getResources().getColor(R.color.white))
                .showAppIcon(R.mipmap.ic_launcher)
                .setDefaultNumberOfStars(5)
                .setShowShareButton(true)
                //.setShowOKButtonByDefault(false)
                .setLineDividerColor(getResources().getColor(R.color.white55))
                .setRateButtonBackgroundColor(getResources().getColor(R.color.mainEnd))
                .setRateButtonPressedBackgroundColor(getResources().getColor(R.color.mainStart))
                .setOnRatingListener(new OnRatingListener() {
                    @Override
                    public void onRating(RatingAction action, float rating) {
                       // Toast.makeText(MainActivity.this,
                         //       "Rate Me action: " + action + " (rating: " + rating + ")", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags) {
                        // Nothing to write
                    }
                })
                .build()
                .show(getFragmentManager(), "custom-dialog");
    }

}