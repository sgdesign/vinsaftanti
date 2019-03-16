package com.vinsasoft.security.service;

import android.text.TextUtils;

import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.vinsasoft.security.stuff.BlockOrigin;

import java.util.HashMap;
import java.util.Map;


public class Call {
    public static final String PRIVATE_NUMBER = "private";
    private final Map<String, String> extraData = new HashMap<>();
    private String number;
    private BlockOrigin blockOrigin;
    private PhoneNumber normalizedNumber;
    private String countryISO;

    public BlockOrigin getBlockOrigin() {
        return blockOrigin;
    }

    public void setBlockOrigin(BlockOrigin blockOrigin) {
        this.blockOrigin = blockOrigin;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public PhoneNumber getNormalizedNumber() {
        return normalizedNumber;
    }

    public void setNormalizedNumber(PhoneNumber normalizedNumber) {
        this.normalizedNumber = normalizedNumber;
    }

    public boolean isPrivateNumber() {
        return TextUtils.isEmpty(number);
    }

    public Map<String, String> getExtraData() {
        return extraData;
    }

    public String getCountryISO() {
        return countryISO;
    }

    public void setCountryISO(String countryISO) {
        this.countryISO = countryISO;
    }

    @Override
    public String toString() {
        return "Call{" +
                "blockOrigin=" + blockOrigin +
                ", number='" + number + '\'' +
                ", normalizedNumber=" + normalizedNumber +
                ", extraData=" + extraData +
                ", countryISO='" + countryISO + '\'' +
                '}';
    }
}
