package com.android.ofoo.util;

import android.content.res.Resources;

public class UIHelper {

    /**
     * sp转px
     * @param spValue
     * @return
     */
    public static int sp2px(float spValue) {
        float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转sp
     * @param pxValue
     * @return
     */
    public static int px2sp(float pxValue) {
        float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * dip转px
     * @param dipValue
     * @return
     */
    public static int dp2px(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * px转dip
     * @param pxValue
     * @return
     */
    public static int px2dp(float pxValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);

    }

    public static int getStatusBarHeight() {
        Resources rcs = Resources.getSystem();
        int result = 0;
        int resourceId = rcs.getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            result = rcs.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 得到设备屏幕的高度
     */
    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


    /**
     * 得到设备屏幕的宽度
     */
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    /**
     * 得到设备的密度
     */
    public static float getScreenDensity() {
        return Resources.getSystem().getDisplayMetrics().density;
    }
}
