package com.android.ofoo.activities;

import android.app.Activity;
import android.os.Bundle;

import com.android.ofoo.manager.ActivitiesManager;

public abstract class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivitiesManager.addActivity(this);
		initializeView();
		initializeData();
		initializeEvent();
	}

	@Override
	protected void onResume() {
		super.onResume();
		ActivitiesManager.setForegroundActivity(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		ActivitiesManager.setForegroundActivity(null);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivitiesManager.removeActivity(this);
	}

	protected abstract void initializeView();

	protected void initializeEvent() {
	}

	/**
	 * 数据初始化，在onCreate中执行
	 */
	protected void initializeData() {
	}

}