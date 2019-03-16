package com.vinsasoft.security.CallBlocker;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;


import com.vinsasoft.security.BuildConfig;
import com.vinsasoft.security.R;
import com.vinsasoft.security.base.BaseSwipeBackActivity;
import com.vinsasoft.security.stuff.PermUtil;

import hugo.weaving.DebugLog;


public class AddActivity extends BaseSwipeBackActivity {
    public static final String FRAGMENT_CONTACTS = "contacts";
    public static final String FRAGMENT_MANUAL = "manual";
    public static final String FRAGMENT_CALLLOG = "calllog";
    public static final String FRAGMENT_KEY = "fragment_key";

    private static final String TAG = AddActivity.class.getSimpleName();
    private static final String TAG_FRAGMENT = "TAG_FRAGMENT";

    private Fragment fragment;

    @Override
    @DebugLog
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!hasGrantedPermissions()) {
            if (BuildConfig.DEBUG) Log.d(TAG, "Finish activity due to lack of permissions");
            finish();
            return;
        }
        setContentView(R.layout.add_main);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            String val = getIntent().getExtras().getString(FRAGMENT_KEY);
            switch (val) {
                case FRAGMENT_CONTACTS:
                    fragment = new AddContactFragment();
                    setTitle(R.string.contactTitle);
                    break;
                case FRAGMENT_MANUAL:
                    fragment = new AddManualFragment();
                    setTitle(R.string.add_manual_title);
                    break;
                case FRAGMENT_CALLLOG:
                    fragment = new AddCallLogFragment();
                    setTitle(R.string.call_log_title);
                    break;
                default:
                    throw new RuntimeException("Should not be here");
            }
        } else {
            fragment = getSupportFragmentManager().findFragmentByTag(savedInstanceState.getString
                    (TAG_FRAGMENT));
        }
        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
        tr.replace(R.id.fragment_container, fragment, TAG_FRAGMENT);
        tr.commit();

    }


    @Override
    @DebugLog
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (null != fragment) {
            outState.putString(TAG_FRAGMENT, fragment.getTag());
        }
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
    @DebugLog
    protected void onRestart() {
        super.onRestart();
        if (!hasGrantedPermissions()) {
            if (BuildConfig.DEBUG) Log.d(TAG, "Finish activity due to contact permission revoked");
            finish();
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean hasGrantedPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String val = getIntent().getExtras().getString(FRAGMENT_KEY);
            if (val.equals(FRAGMENT_CONTACTS)) {
                return PermUtil.checkReadContacts(this);
            } else if (val.equals(FRAGMENT_CALLLOG)) {
                return PermUtil.checkCallPhone(this);
            }
        }
        return true;
    }
}
