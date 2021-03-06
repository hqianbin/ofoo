package com.android.ofoo.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

import dalvik.system.PathClassLoader;

/**
 * introduction:应用全局相关工具类
 */
public class AppUtils {

	private static int mUid = -Integer.MAX_VALUE;

	/**
	 * 应用级Context对象，无Window
	 *
	 * @return
	 */
	public static Context getContext() {
		return OfooApplicationContext.context;
	}

	/**
	 * 获取系统资源
	 *
	 * @return
	 */
	public static Resources getResources() {
		return getContext().getResources();
	}

	private static final String ANDROID = "Android";
	private static final String OFOO = "ofoo";

	public static String getVersionCode() {
		PackageManager manager = getContext().getPackageManager();
		PackageInfo info = null;
		try {
			info = manager.getPackageInfo(getContext().getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			LogUtils.e(e);
		}
		if (info != null)
			return String.valueOf(info.versionCode);
		else
			return "get versionCode error";
	}

	public static int getVersionCodeInt() {
		PackageManager manager = getContext().getPackageManager();
		PackageInfo info = null;
		try {
			info = manager.getPackageInfo(getContext().getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			LogUtils.e(e);
		}
		if (info != null)
			return info.versionCode;
		else
			return -1;
	}

	public static String getVersionName() {
		PackageManager manager = getContext().getPackageManager();
		PackageInfo info = null;
		try {
			info = manager.getPackageInfo(getContext().getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			return "";
		}
		return String.valueOf(info.versionName);
	}

	protected static final String PREFS_FILE = "device_id.xml";
	protected static final String PREFS_DEVICE_ID = "device_id";
	protected static volatile UUID uuid;
	private static int mFromWhich;
	public static final int From_ANDROID_ID = 1;
	public static final int FROM_IMEI = 2;
	public static final int FROM_UUID = 3;

	/**
	 * Returns a unique UUID for the current android device. As with all UUIDs,
	 * this unique ID is "very highly likely" to be unique across all Android
	 * devices. Much more so than ANDROID_ID is.
	 * <p/>
	 * The UUID is generated by using ANDROID_ID as the base key if appropriate,
	 * falling back on TelephonyManager.getDeviceID() if ANDROID_ID is known to
	 * be incorrect, and finally falling back on a random UUID that's persisted
	 * to SharedPreferences if getDeviceID() does not return a usable value.
	 * <p/>
	 * In some rare circumstances, this ID may change. In particular, if the
	 * device is factory reset a new device ID may be generated. In addition, if
	 * a user upgrades their phone from certain buggy implementations of Android
	 * 2.2 to a newer, non-buggy version of Android, the device ID may change.
	 * Or, if a user uninstalls your app on a device that has neither a proper
	 * Android ID nor a Device ID, this ID may change on reinstallation.
	 * <p/>
	 * Note that if the code falls back on using TelephonyManager.getDeviceId(),
	 * the resulting ID will NOT change after a factory reset. Something to be
	 * aware of.
	 * <p/>
	 * Works around a bug in Android 2.2 for many devices when using ANDROID_ID
	 * directly.
	 *
	 * @return a UUID that may be used to uniquely identify your device for most
	 * purposes.
	 */
	public static UUID getDeviceId() {
		Context context = getContext();
		if (uuid == null) {
			synchronized (AppUtils.class) {
				if (uuid == null) {
					final SharedPreferences prefs = context
							.getSharedPreferences(PREFS_FILE, 0);
					final String id = prefs.getString(PREFS_DEVICE_ID, null);
					if (id != null) {
						uuid = UUID.fromString(id);
					} else {
						final String deviceId = ((TelephonyManager) context
								.getSystemService(Context.TELEPHONY_SERVICE))
								.getDeviceId();
						try {
							if (deviceId != null) {
								uuid = UUID.nameUUIDFromBytes(deviceId
										.getBytes("utf8"));
								mFromWhich = FROM_IMEI;
							} else {
								final String androidId = Settings.Secure.getString(
										context.getContentResolver(),
										Settings.Secure.ANDROID_ID);
								if (!"9774d56d682e549c".equals(androidId)) {
									uuid = UUID.nameUUIDFromBytes(androidId
											.getBytes("utf8"));
									mFromWhich = From_ANDROID_ID;
								} else {
									uuid = UUID.randomUUID();
									mFromWhich = FROM_UUID;
								}
							}
						} catch (UnsupportedEncodingException e) {
							throw new RuntimeException(e);
						}
						prefs.edit()
								.putString(PREFS_DEVICE_ID, uuid.toString())
								.commit();
					}
				}
			}
		}
		return uuid;
	}

	public static int getDeviceIdFromWhich() {
		return mFromWhich;
	}

	/**
	 * 返回"Android"
	 *
	 * @return
	 * @see AppUtils#ANDROID
	 */
	public static String getOsName() {
		return ANDROID;
	}

	public static String getAppName() {
		return OFOO;
	}

	/**
	 * Build.VERSION.RELEASE
	 *
	 * @return
	 */
	public static String getOsVersion() {
		return String.valueOf(Build.VERSION.RELEASE);
	}

	/**
	 * Build.MODEL + ":" + Build.PRODUCT
	 *
	 * @return
	 */
	public static String getPhoneModel() {
		return Build.MODEL + ":" + Build.PRODUCT;
	}

	public static String getDevToken() {
		return getDeviceId().toString();
	}

	private static String mUser = null;

	/**
	 * 返回手机用户名 用手机号+imsi 或者getPhoneModel()做标识 如果都拿不到就是admin
	 *
	 * @return
	 */
	public static String getUserPhoneName() {
		String telTag = "tel:";
		String imsiTag = "-imsi:";
		try {
			if (StringUtils.isEmpty(mUser)) {
				TelephonyManager tm = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
				String tel = tm.getLine1Number();//获取本机号码
				String imsi = tm.getSubscriberId();//得到用户Id
				mUser = telTag + tel + imsiTag + imsi;
			}
		} catch (Exception e) {
			LogUtils.e(e);
		} finally {
			if (StringUtils.isEmpty(mUser)) {
				mUser = getPhoneModel();
			}
			return mUser;
		}
	}

	/**
	 * 获取manifest中定义的meta data
	 * @return
	 */
	public static Bundle getMeta() {
		try {
			return getContext().getPackageManager()
					.getApplicationInfo(getContext().getPackageName(),
							PackageManager.GET_META_DATA).metaData;
		} catch (PackageManager.NameNotFoundException e) {
			return null;
		}
	}

	public static String getChannel() {
		Bundle bundle = getMeta();
		if(bundle == null){
			return "rel";
		}
		return bundle.getString("APP_CHANNEL", "rel");
	}

	/**
	 * 获取应用的uid号码
	 *
	 * @return
	 */
	public static int getUid() {
		if (mUid != -Integer.MAX_VALUE) {
			return mUid;
		} else {
			ActivityManager am = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
			ApplicationInfo appinfo = getContext().getApplicationInfo();
			List<ActivityManager.RunningAppProcessInfo> runningInfos = am.getRunningAppProcesses();
			for (ActivityManager.RunningAppProcessInfo runningProcess : runningInfos) {
				if ((runningProcess.processName != null) && runningProcess.processName.equals(appinfo.processName)) {
					mUid = runningProcess.uid;
					return mUid;
				}
			}
			mUid = appinfo.uid;
			return mUid;
		}
	}

	/**
	 * 获取应用的从开机到现在的总计流量
	 *
	 * @return
	 */
	private static long getTrafficInBytes() {
		int uid = getUid();
		return TrafficStats.getUidRxBytes(uid) + TrafficStats.getUidTxBytes(uid);
	}

	/**
	 * 判断是否主进程
	 *
	 * @return
	 */
	public static boolean isMainProcess() {
		ActivityManager am = ((ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE));
		List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
		if (processInfos == null) return false;
		//主进程名称
		String mainProcessName = getContext().getPackageName();
		//获取当前进程id
		int myPid = Process.myPid();
		for (ActivityManager.RunningAppProcessInfo info : processInfos) {
			//主进程名称应该和processName相等
			if (info.pid == myPid && mainProcessName.equals(info.processName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否安装目标应用
	 *
	 * @param packageName 目标应用安装后的包名
	 * @return 是否已安装目标应用
	 */
	public static boolean isPackageInstalled(String packageName) {
		final PackageManager packageManager = getContext().getPackageManager();// 获取packagemanager
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
		if (pinfo != null) {
			PackageInfo packageInfo;
			for (int i = 0; i < pinfo.size(); i++) {
				packageInfo = pinfo.get(i);
				if (packageInfo == null) continue;
				String pn = packageInfo.packageName;
				if (pn.startsWith(packageName)) {
					return true;
				}
			}
		}
		return false;
	}

	public static CharSequence getPackageName() {
		return getContext().getPackageName();
	}

	/**
	 * @param libname
	 * @return
	 */
	public static boolean hasNativeLibrary(final String libname) {
		return getPathClassLoader().findLibrary(libname) != null;
	}


	public static PathClassLoader getPathClassLoader() {
		return (PathClassLoader) AppUtils.class.getClassLoader();
	}

	public static boolean is64Only() {
		String pathList = System.getProperty("java.library.path", ".");
		String pathSep = System.getProperty("path.separator", ":");
		String fileSep = System.getProperty("file.separator", "/");
		String[] libPaths = pathList.split(pathSep);
		for (String path : libPaths) {
			path = path.replace(fileSep, "");
			if (path.toLowerCase().endsWith("lib64")) {
				return true;
			}
		}
		return false;
	}

	public static String getPackageNameTail() {
		String packageName = getPackageName().toString();
		return packageName.substring(packageName.lastIndexOf(".") + 1);
	}
}