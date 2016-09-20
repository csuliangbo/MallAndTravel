package com.ych.mall.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SharedPreferencesUtil {

	private static SharedPreferences preferences;

	public SharedPreferences init(Context context) {
		if (preferences == null) {
			preferences = context.getSharedPreferences("preference",
					Context.MODE_PRIVATE);
		}
		return preferences;
	}

	public static void putString(String key, String value) {
		Log.i("sutil", key+":"+value);
		Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getString(String key) {

		return preferences.getString(key, "");
	}

	public static void putBoolean(String key, boolean value) {
		Editor editor = preferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static Boolean getBoolean(String key, boolean flag) {
		return preferences.getBoolean(key, flag);
	}

	public static Integer getInteger(String key, int callBack) {
		return preferences.getInt(key, callBack);
	}

	public static void putInteger(String key, Integer value) {
		Log.i("sutil", key+":"+value);
		Editor editor = preferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

}
