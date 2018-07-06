/**
 * Copyright (c) 2013-2014 YunZhongXiaoNiao Tech
 */
package com.android.ofoo.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.android.ofoo.activities.MainActivity;
import com.android.ofoo.util.OfooApplicationContext;

/**
 * 本类用于管理activity
 */
public class ActivitiesManager {

	public static void addActivity(Activity activity) {
		AppActivityManager.getInstance().addActivity(activity);
	}

	public static Activity getForegroundActivity() {
		return AppActivityManager.getInstance().getForegroundActivity();
	}

	public static void setForegroundActivity(Activity activity) {
		AppActivityManager.getInstance().setForegroundActivity(activity);
	}

	public static void removeActivity(Activity activity) {
		AppActivityManager.getInstance().finishActivity(activity);
	}

	public static void finishAllActivity() {
		AppActivityManager.getInstance().finishAllActivity();
	}

    public static int getActivityCount() {
        return AppActivityManager.getInstance().getActivityCount();
    }

	/**
	 * 注销跳转到登陆界面
	 */
	public static void finishAllToMain() {
		AppActivityManager.getInstance().finishAllActivity();
		Context context = OfooApplicationContext.context;
		Intent i = new Intent(context, MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}
}
