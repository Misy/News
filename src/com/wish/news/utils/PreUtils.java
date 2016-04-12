package com.wish.news.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreUtils {
	public static final String PRE_NAME = "config";

	public static boolean getGuideShowedPrefs(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(PRE_NAME,
				Context.MODE_PRIVATE);
		return sp.getBoolean(key, false);

	}

	public static void setGuideShowedPrefs(Context context, String key,
			boolean value) {
		SharedPreferences sp = context.getSharedPreferences(PRE_NAME,
				Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
	}
}
