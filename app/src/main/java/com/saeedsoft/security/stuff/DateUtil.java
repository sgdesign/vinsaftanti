package com.saeedsoft.security.stuff;

import android.content.Context;
import android.os.Build;
import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateUtil {

    public static SimpleDateFormat getShortDateFormatter(Context context, Locale locale) {
        String dateFormatPattern;

        if (DateFormat.is24HourFormat(context)) {
            dateFormatPattern = getLocalizedPattern("MMM d", locale);
        } else {
            dateFormatPattern = getLocalizedPattern("MMM d", locale);
        }

        return new SimpleDateFormat(dateFormatPattern, locale);
    }

    public static SimpleDateFormat getDetailedDateFormatter(Context context, Locale locale) {
        String dateFormatPattern;

        if (DateFormat.is24HourFormat(context)) {
            dateFormatPattern = getLocalizedPattern("MMM d, yyyy HH:mm", locale);
        } else {
            dateFormatPattern = getLocalizedPattern("MMM d, yyyy hh:mm a", locale);
        }

        return new SimpleDateFormat(dateFormatPattern, locale);
    }

    private static String getLocalizedPattern(String template, Locale locale) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return DateFormat.getBestDateTimePattern(locale, template);
        } else {
            return new SimpleDateFormat(template, locale).toLocalizedPattern();
        }
    }
}
