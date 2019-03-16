package com.saeedsoft.security.service;

import android.content.Context;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.saeedsoft.security.stuff.Util;

import java.util.regex.Pattern;

public final class ValidatorService {
    private final Pattern patternBeginWith = Pattern.compile("^(\\+|\\d)\\d*$");//^(\+|\d)\d*$
    private final Context context;

    public ValidatorService(Context context) {
        this.context = context;
    }

    boolean checkUserInput(String number, String defaultCountryISO) throws NumberParseException {
        PhoneNumber parsed = PhoneNumberUtil.getInstance().parse(number, defaultCountryISO);
        return PhoneNumberUtil.getInstance().isPossibleNumber(parsed);
    }

    public boolean checkUserInput(String number, boolean beginWith) {
        try {
            if (beginWith) {
                return patternBeginWith.matcher(number).matches();
            } else {
                String defaultCountryISO = Util.getDeviceCountryISO(context);
                return this.checkUserInput(number, defaultCountryISO);
            }
        } catch (NumberParseException e) {
            //Do nothing
        }
        return false;
    }
}
