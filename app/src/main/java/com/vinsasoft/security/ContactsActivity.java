package com.vinsasoft.security;

import android.Manifest;

import com.vinsasoft.security.base.BaseContactsActivity;
import com.vinsasoft.security.engine.ContactBean;
import com.vinsasoft.security.engine.ContactsEngine;

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
