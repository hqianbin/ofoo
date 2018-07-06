package com.android.ofoo.provider;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.android.ofoo.service.FloatingService;

/**
 * 桌面小部件
 */

public class OfooAppWidgetProvider extends AppWidgetProvider {
    /**
     * 当小组件被添加到屏幕上时回调
     */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        context.startService(new Intent(context, FloatingService.class));
    }

    /**
     * 当小组件被刷新时回调
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
    /**
     * 当widget小组件从屏幕移除时回调
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    /**
     * 当最后一个小组件被从屏幕中移除时回调
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        context.stopService(new Intent(context, FloatingService.class));
    }
}
