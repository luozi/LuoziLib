package com.luozi.lib;

import android.content.Context;
import android.content.SharedPreferences;

public class GeneralPreferences {

	// The name of the shared preferences file. This name must be maintained for historical
	// reasons, as it's what PreferenceManager assigned the first time the file was created.
	static final String SHARED_PREFS_NAME = "com.luozi.lib._preferences";

	public static final int NO_REMINDER = -1;
	public static final String NO_REMINDER_STRING = null;
	public static final long NO_REMINDER_LONG = -1L;

	/** Return a properly configured SharedPreferences instance */
	public static SharedPreferences getSharedPreferences(Context context) {
		return context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
	}

}
