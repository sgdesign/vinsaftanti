package com.saeedsoft.security;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import com.fastaccess.permission.base.activity.BasePermissionActivity;
import com.fastaccess.permission.base.model.PermissionModel;
import com.fastaccess.permission.base.model.PermissionModelBuilder;

import com.saeedsoft.security.utils.WindowsUtils;

import java.util.ArrayList;
import java.util.List;


public class PermissionActivity extends BasePermissionActivity {
    private List<PermissionModel> permissions = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // add all needed permission. need run before super.onCreate
        addPermissions();

        super.onCreate(savedInstanceState);

        // check whether granted
        isPermissionsGranted();

        // set no action bar
        WindowsUtils.hideActionBar(this);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        System.out.println(permissionHelper);
//
//    }

 
    private void addPermissions(){


        permissions.add(PermissionModelBuilder.withContext(this)
                .withTitle(R.string.title_storage)
                .withCanSkip(false)
                .withPermissionName(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withMessage(R.string.message_storage)
                .withExplanationMessage(R.string.explanation_message)
                .withLayoutColorRes(R.color.colorAccent)
                .withImageResourceId(R.drawable.permission_two)
                .build());


        permissions.add(PermissionModelBuilder.withContext(this)
                .withCanSkip(false)
                .withTitle(R.string.title_contacts)
                .withPermissionName(Manifest.permission.WRITE_CONTACTS)
                .withMessage(R.string.message_contacts)
                .withExplanationMessage(R.string.explanation_message)
                .withLayoutColorRes(R.color.blue)
                .withImageResourceId(R.drawable.permission_one)
                .build());



        permissions.add(PermissionModelBuilder.withContext(this)
                .withCanSkip(false)
                .withTitle(R.string.title_sms)
                .withPermissionName(Manifest.permission.SEND_SMS)
                .withMessage(R.string.message_sms)
                .withExplanationMessage(R.string.explanation_message)
                .withLayoutColorRes(R.color.colorPrimary)
                .withImageResourceId(R.drawable.permission_three)
                .build());



        permissions.add(PermissionModelBuilder.withContext(this)
                .withCanSkip(false)
                .withTitle(R.string.title_phone)
                .withPermissionName(Manifest.permission.READ_CALL_LOG)
                .withMessage(R.string.message_phone)
                .withExplanationMessage(R.string.explanation_message)
                .withLayoutColorRes(R.color.colorPrimaryDark)
                .withImageResourceId(R.drawable.permission_two).build());

    }

    private void isPermissionsGranted() {
        int count = 0;
        for (PermissionModel permission: permissions) {
            if(permission.getPermissionName().equals(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
                if (permissionHelper.isSystemAlertGranted()) {
                    count++;
                }
            } else if (permissionHelper.isPermissionGranted(permission.getPermissionName())) {
                count++;
            }
        }
        if(count == permissions.size()) {
			
            finish();
        }
    }

    @NonNull
    @Override
    protected List<PermissionModel> permissions() {
        return permissions;
    }

    @Override
    protected int theme() {
        return R.style.AppTheme;
    }

    @Override
    protected void onIntroFinished() {
        Toast.makeText(this, R.string.tips_thank_you_for_your_use, Toast.LENGTH_SHORT).show();
        Log.i("onIntroFinished", "Intro has finished");
		
        finish();
    }

    @Nullable
    @Override
    protected ViewPager.PageTransformer pagerTransformer() {
        return null;
    }

    @Override
    protected boolean backPressIsEnabled() {
        return false;
    }

    @Override
    protected void permissionIsPermanentlyDenied(@NonNull String permissionName) {
        Log.e("DANGER", "Permission ( " + permissionName + " ) is permanentlyDenied and can only be granted via settings screen");
        /** {@link com.fastaccess.permission.base.PermissionHelper#openSettingsScreen(Context)} can help you to open it if you like */
    }

    @Override
    protected void onUserDeclinePermission(@NonNull String permissionName) {
        Log.w("Warning", "Permission ( " + permissionName + " ) is skipped you can request it again by calling doing such\n " +
                "if (permissionHelper.isExplanationNeeded(permissionName)) {\n" +
                "        permissionHelper.requestAfterExplanation(permissionName);\n" +
                "    }\n" +
                "    if (permissionHelper.isPermissionPermanentlyDenied(permissionName)) {\n" +
                "        /** read {@link #permissionIsPermanentlyDenied(String)} **/\n" +
                "    }");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
