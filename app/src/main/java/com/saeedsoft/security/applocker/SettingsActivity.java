package com.saeedsoft.security.applocker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saeedsoft.security.R;


public class SettingsActivity extends AppCompatActivity {

    ListView listemiz;
    Context context;
    TextView vName;
    RelativeLayout settings_layout;
    ColorManager colorManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        context = SettingsActivity.this;
        listemiz=(ListView) findViewById(R.id.settings_list);
        colorManager = new ColorManager(this);
        settings_layout = (RelativeLayout) findViewById(R.id.s_layout);

        vName = (TextView) findViewById(R.id.vName);
        String[] settings =
                {context.getString(R.string.reset_password),"Facebook",context.getString(R.string.reset_locked_apps)};

        vName.setText(getString(R.string.app_name)+" v"+GetVersion.getVersion(context));

        ArrayAdapter<String> veriAdaptoru=new ArrayAdapter<String>
                (this, R.layout.settings_list, R.id.settingsTV, settings);
        listemiz.setAdapter(veriAdaptoru);

        listemiz.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                if (position==0){
                    Intent i = new Intent(SettingsActivity.this,SetLockTypeActivity.class);
                    startActivity(i);
                }
                if (position==1){
                    Intent facebookIntent = getOpenFacebookIntent(context);
                    startActivity(facebookIntent);
                }
                if (position==2){
                    ManageLockedApps.resetLockedApps(context);
                }
            }
        });
    }
    private Intent getOpenFacebookIntent(Context context) {
        try {
            context.getPackageManager()
                    .getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("fb://profile/631585937018645"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/pharidofficial"));
        }
    }
}
