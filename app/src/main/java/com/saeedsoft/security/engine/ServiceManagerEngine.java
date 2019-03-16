package com.saeedsoft.security.engine;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.saeedsoft.security.Constant;
//import com.saeedsoft.security.service.BlacklistInterceptService;
import com.saeedsoft.security.service.IncomingLocationService;
import com.saeedsoft.security.service.PhoneSafeService;
import com.saeedsoft.security.utils.ActivityManagerUtils;

public class ServiceManagerEngine {

 
    public static void checkAndAutoStart(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
    
        if(sp.getBoolean(Constant.KEY_CB_PHONE_SAFE, false))
            ActivityManagerUtils.checkService(context, PhoneSafeService.class);
     
       // if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
          //  if(sp.getBoolean(Constant.KEY_CB_BLACKLIST_INTERCEPT, true))
           //     ActivityManagerUtils.checkService(context, BlacklistInterceptService.class);
       // }
        if(sp.getBoolean(Constant.KEY_CB_SHOW_INCOMING_LOCATION, true))
            ActivityManagerUtils.checkService(context, IncomingLocationService.class);
      

    }

}
