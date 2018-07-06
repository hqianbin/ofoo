/**
 * Copyright (c) 2013-2014 YunZhongXiaoNiao Tech
 */
package com.android.ofoo.manager;

import android.app.Activity;

import java.util.Stack;

/**
 * author lxf on 15/7/15
 * 下午8:46.
 * 本类用于管理activity
 */
public class AppActivityManager {
	private Stack<Activity> activityStack = new Stack<Activity>();
	private static AppActivityManager instance;

	private AppActivityManager() {
	}

	public static AppActivityManager getInstance() {
		if (instance == null) {
			instance = new AppActivityManager();
		}
		return instance;
	}

	public int getActivityCount() {
		return activityStack == null ? 0 : activityStack.size();
	}

	/**
	 * onCreate中调用
	 *
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public Activity getCurrentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
//            activity.finish();
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * 结束所有activity,清空栈
	 */
	public void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).getClass();
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * 正在显示的activity
	 */
	protected Activity mForegroundActivity;

	/**
	 * 需要判断该对象是否为空后使用，不然会抛空指针异常
	 *
	 * @return
	 */
	public Activity getForegroundActivity() {
		return mForegroundActivity;
	}

	/**
	 * 在onResume和onPause（或者onStart和onStop）中调用此方法，分别在两个方法中为其赋值和置空，保证mForegroundActivity为正在显示的activity
	 *
	 * @param foregroundActivity
	 */
	public void setForegroundActivity(Activity foregroundActivity) {
		mForegroundActivity = foregroundActivity;
	}

}
