package com.saeedsoft.security.service;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
//import com.saeedsoft.security.R;
import com.saeedsoft.security.R;
import com.saeedsoft.security.CallBlocker.PhoneNumberException;
import com.saeedsoft.security.CallBlocker.TooShortNumberException;
import com.saeedsoft.security.stuff.Util;


public class NormalizerService {
    private final Context context;
    private PhoneNumber holder;

    public NormalizerService(@NonNull Context context) {
        this.context = context;
        String number = null;
        try {
            number = Util.getNumberExample(context);
            //This holder is reused to decrease object creation
            this.holder = PhoneNumberUtil.getInstance().parseAndKeepRawInput(number, null);
        } catch (NumberParseException e) {
            throw new RuntimeException("Error parsing number: " + number, e);
        }
    }

    String[] normalizeUserInput(String number) {
        String defaultCountryISO = Util.getDeviceCountryISO(context);
        try {
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            PhoneNumber phoneNumber = phoneNumberUtil.parse(number, defaultCountryISO);
            String e164 = phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat
                    .E164);
            String international = phoneNumberUtil.format(phoneNumber, PhoneNumberUtil
                    .PhoneNumberFormat.INTERNATIONAL);
            return new String[]{e164, international};
        } catch (NumberParseException e) {
            throw new IllegalArgumentException("Should be validated before call this method", e);
        }
    }

    public void normalizeCall(@NonNull Call call) throws PhoneNumberException,
            TooShortNumberException {

        if (call.isPrivateNumber() || call.getNormalizedNumber() != null) {
            return;
        }

        if (call.getCountryISO() == null) {
            call.setCountryISO(Util.getDeviceCountryISO(context));
        }

        String number = call.getNumber();
        if (number.length() < TooShortNumberException.MINIMUM_LENGTH) {
            throw new TooShortNumberException("Too short number: " + number + ". Minimum length" +
                    " is " + TooShortNumberException.MINIMUM_LENGTH);
        }

        try {
            PhoneNumberUtil.getInstance().parseAndKeepRawInput(number, call.getCountryISO(),
                    holder);
            call.setNormalizedNumber(holder);
        } catch (NumberParseException e) {
            throw new PhoneNumberException("Error parsing number: " + number, e);
        }

    }

    public String getDisplayNumber(Call call) {
        if (call.isPrivateNumber()) {
            return context.getString(R.string.common_private);
        } else {
            return PhoneNumberUtil.getInstance().format(call.getNormalizedNumber(), PhoneNumberUtil
                    .PhoneNumberFormat.INTERNATIONAL);
        }
    }

}
