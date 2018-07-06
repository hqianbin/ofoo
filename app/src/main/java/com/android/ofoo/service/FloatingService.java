package com.android.ofoo.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.android.ofoo.R;
import com.android.ofoo.activities.TestActivity;
import com.android.ofoo.util.UIHelper;


/**
 * Created by PF0ZYM2B on 2018/3/12.
 */
public class FloatingService extends Service {

    private static final String TAG = "FloatingService";

    LinearLayout mLinearLayout;
    //布局参数.
    WindowManager.LayoutParams params;
    //实例化的WindowManager.
    WindowManager windowManager;

    //状态栏高度.（接下来会用到）
    int statusBarHeight = -1;
    int windowWidth = -1;
    float initX = 0, initY = 0, paramX, paramY;

    @Override
    public void onCreate() {
        super.onCreate();
        createFloatingWindow();
    }

    private void createFloatingWindow() {
        //赋值WindowManager&LayoutParam.
        params = new WindowManager.LayoutParams();
        windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        //设置type.系统提示型窗口，一般都在应用程序窗口之上.
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        //设置效果为背景透明.
        params.format = PixelFormat.RGBA_8888;
        //设置flags.不可聚焦及不可使用按钮对悬浮窗进行操控.
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        //设置窗口初始停靠位置.
        params.gravity = Gravity.LEFT | Gravity.TOP;

        params.width = UIHelper.dp2px(60);
        params.height = UIHelper.dp2px(60);

        statusBarHeight = UIHelper.getStatusBarHeight();
        windowWidth = UIHelper.getScreenWidth();

        params.x = windowWidth - statusBarHeight - params.width;
        params.y = statusBarHeight * 2;

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局.
        mLinearLayout = (LinearLayout) inflater.inflate(R.layout.desktop_widget,null);
        //添加toucherlayout
        windowManager.addView(mLinearLayout, params);

        //主动计算出当前View的宽高信息.
        mLinearLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        mLinearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    initX = event.getRawX();
                    initY = event.getRawY();
                    paramX = params.x;
                    paramY = params.y;
                }else if(event.getAction() == MotionEvent.ACTION_MOVE){
                    params.x = (int)(paramX + event.getRawX() - initX);
                    params.y = (int)(paramY + event.getRawY() - initY);
                }else if(event.getAction() == MotionEvent.ACTION_UP ||
                        event.getAction() == MotionEvent.ACTION_CANCEL){
                    if(event.getRawX() < windowWidth / 2.0F){
                        params.x = statusBarHeight;
                    }else {
                        params.x = windowWidth - statusBarHeight - params.width;
                    }

                    if(params.y < statusBarHeight){
                        params.y = statusBarHeight;
                    }else if(params.y > UIHelper.getScreenHeight() - statusBarHeight){
                        params.y = UIHelper.getScreenHeight() - statusBarHeight;
                    }
                }

                windowManager.updateViewLayout(mLinearLayout, params);
                return false;
            }
        });
        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), TestActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplication().startActivity(intent);
            }
        });

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if(mLinearLayout != null){
            windowManager.removeView(mLinearLayout);
        }
        super.onDestroy();
    }

}