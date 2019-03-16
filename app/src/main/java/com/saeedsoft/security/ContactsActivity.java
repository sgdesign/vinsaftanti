package com.saeedsoft.security;

import android.Manifest;

import com.saeedsoft.security.base.BaseContactsActivity;
import com.saeedsoft.security.engine.ContactBean;
import com.saeedsoft.security.engine.ContactsEngine;

import java.util.List;


public class ContactsActivity extends BaseContactsActivity {

    @Override
    protected String[] getPermissions() {
        return new String[]{
                Manifest.permission.READ_CONTACTS
        };
    }

    @Override
    protected List<ContactBean> getContactDatas() {
        return ContactsEngine.readContacts(ContactsActivity.this);
    }
}
