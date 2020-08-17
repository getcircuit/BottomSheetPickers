package com.philliphsu.bottomsheetpickers.time.numberpad;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;

import java.util.Locale;

/**
 * Utilities for text formatting of dates and times.
 */
final class DateTimeFormatUtils {
    /**
     * @return The time separator used in the user's locale.
     */
    static String getTimeSeparator(@NonNull Context context, boolean is24Hour) {
        // The time separator is defined in the Unicode CLDR and cannot be supposed to be ":".
        // See http://unicode.org/cldr/trac/browser/trunk/common/main.
        // We pass the correct "skeleton" depending on 12 or 24 hours view and then extract the
        // separator, which is the character just after the hour marker in the returned pattern.
        final String bestDateTimePattern = DateFormat.getBestDateTimePattern(
                getPrimaryLocale(context), (is24Hour) ? "Hm" : "hm");
        final String separatorText;
        // See http://www.unicode.org/reports/tr35/tr35-dates.html for hour formats
        final char[] hourFormats = {'H', 'h', 'K', 'k'};
        int hIndex = lastIndexOfAny(bestDateTimePattern, hourFormats);
        if (hIndex == -1) {
            // Default case
            separatorText = ":";
        } else {
            separatorText = Character.toString(bestDateTimePattern.charAt(hIndex + 1));
        }
        return separatorText;
    }

    /**
     * @return {@code true} if the AM or PM label is written before the time
     *         in the user's locale.
     */
    static boolean isAmPmWrittenBeforeTime(@NonNull Context context) {
        final String dateTimePattern = DateFormat.getBestDateTimePattern(
                getPrimaryLocale(context), "hm");
        return dateTimePattern.startsWith("a");
    }

    private static Locale getPrimaryLocale(@NonNull Context context) {
        final Configuration config = context.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= 24) {
            return config.getLocales().get(0);
        } else {
            return config.locale;
        }
    }

    private static int lastIndexOfAny(String str, char[] any) {
        final int lengthAny = any.length;
        if (lengthAny > 0) {
            for (int i = str.length() - 1; i >= 0; i--) {
                char c = str.charAt(i);
                for (int j = 0; j < lengthAny; j++) {
                    if (c == any[j]) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    private DateTimeFormatUtils() {}
}
