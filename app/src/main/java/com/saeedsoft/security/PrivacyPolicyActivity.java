package com.saeedsoft.security;

import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

import com.saeedsoft.security.base.BaseSwipeBackActivity;

import static com.saeedsoft.security.Constant.PrivacyPolicyUrl;

/**
 * Created by dell on 12/18/2017.
 */

public class PrivacyPolicyActivity extends BaseSwipeBackActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacypolicy);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(PrivacyPolicyUrl);
        webView.setHorizontalScrollBarEnabled(false);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

