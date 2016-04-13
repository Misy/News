package com.wish.news.utils;

import android.content.Context;

public class CacheUtils {

	public static void setCache(String key, String value, Context ctx) {
		PreUtils.setStringPrefs(ctx, key, value);
	}

	public static String getCache(Context ctx, String key) {
		return PreUtils.getStringPrefs(ctx, key);
	}
}
