package com.narkhipov.narkhipovstocksfinal;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by nikolayarkhipov on 8/17/16.
 */
public class Constants {

    public static final String PREF_SHOULD_BE_HIGHLIGHTED_OR_NOT = "PREF_SHOULD_BE_HIGHLIGHTED_OR_NOT";
    public static final String PREF_MIN_PERCENTAGE = "PREF_MIN_PERCENTAGE";
    public static final String PREF_COLOR = "PREF_COLOR";

    public static final String PREF_MOST_RECENT_QUAKE = "PREF_MOST_RECENT_QUAKE";

    public static void savePrefs(Context context, String key, long value) {
        SharedPreferences sp =
                PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor ed = sp.edit();
        ed.putLong(key, value);
        ed.apply();
    }


}
