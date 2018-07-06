package com.android.ofoo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

/**
 * introduction:可以通过getParam取得,无需判断类型
 */
public class PreferenceUtils {
	/**
	 * 注意此处定义了sharedPreference的文件名！
	 * 用此工具类都会存在此文件中
	 */
	protected static final String PREF_NAME = "OFOO_PREF";
	protected static SharedPreferences mSharedPref;

	public static void remove(String key){
		SharedPreferences.Editor editor = getSharedPref().edit();
		editor.remove(key);
		editor.commit();
	}

	public static void putString(String key, String value) {
		SharedPreferences.Editor editor = getSharedPref().edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getString(String key, String defValue) {
		return getSharedPref().getString(key, defValue);
	}

	public static boolean getBoolean(String key, boolean defValue) {
		return getSharedPref().getBoolean(key, defValue);
	}

	public static void putBoolean(String key, boolean value) {
		SharedPreferences.Editor editor = getSharedPref().edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	protected static SharedPreferences getSharedPref() {
		if (mSharedPref == null) {
			synchronized (PreferenceUtils.class) {
				if (mSharedPref == null) {
					mSharedPref = OfooApplicationContext.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
				}
			}
		}
		return mSharedPref;
	}

	/**
	 * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
	 *
	 * @param key
	 * @param object
	 */
	public static void putParam(String key, Object object) {

		String type = object.getClass().getSimpleName();
		SharedPreferences sp = getSharedPref();
		SharedPreferences.Editor editor = sp.edit();
		if ("String".equals(type)) {
			editor.putString(key, (String) object);
		} else if ("Integer".equals(type)) {
			editor.putInt(key, (Integer) object);
		} else if ("Boolean".equals(type)) {
			editor.putBoolean(key, (Boolean) object);
		} else if ("Float".equals(type)) {
			editor.putFloat(key, (Float) object);
		} else if ("Long".equals(type)) {
			editor.putLong(key, (Long) object);
		}

		editor.commit();
	}


	/**
	 * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
	 *
	 * @param key
	 * @param defaultObject
	 * @return
	 */
	public static Object getParam(String key, Object defaultObject) {
		String type = defaultObject.getClass().getSimpleName();
		SharedPreferences sp = getSharedPref();
		if ("String".equals(type)) {
			return sp.getString(key, (String) defaultObject);
		} else if ("Integer".equals(type)) {
			return sp.getInt(key, (Integer) defaultObject);
		} else if ("Boolean".equals(type)) {
			return sp.getBoolean(key, (Boolean) defaultObject);
		} else if ("Float".equals(type)) {
			return sp.getFloat(key, (Float) defaultObject);
		} else if ("Long".equals(type)) {
			return sp.getLong(key, (Long) defaultObject);
		}
		return null;
	}

	/**
	 * 用于保存整个对象。
	 * 注意存入对象的字段变动
	 *
	 * @param key
	 * @param obj
	 */
	public static void putObject(String key, Object obj) {
		if (obj == null) {
			putString(key, null);
			return;
		}
		SharedPreferences.Editor editor = getSharedPref().edit();
		editor.putString(key, SerializeUtils.writeObjToString(obj));
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			editor.apply();
		} else {
			editor.commit();
		}
	}

	/**
	 * 用于根据key获取保存的对象。
	 * 注意存入对象的字段变动
	 *
	 * @param key
	 * @return
	 */
	public static Object getObject(String key) {
		String string = getSharedPref().getString(key, "");
		if (string == null || string.trim().length() == 0) {
			return null;
		}
		try {
			return SerializeUtils.readObjFromeString(string);
		} catch (Exception e) {
			putString(key, null);
			return null;
		}
	}

}
