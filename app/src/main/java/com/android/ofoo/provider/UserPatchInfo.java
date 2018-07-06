package com.android.ofoo.provider;

import android.net.Uri;

import com.android.ofoo.bean.BaseBean;

public class UserPatchInfo extends BaseBean {

    /*Data Field*/
    public static final String ID = "_id";
    public static final String USER_ID = "userId";
    public static final String NAME = "name";
    public static final String DEVICE_NO = "deviceNo";
    public static final String APP_NAME = "appName";
    public static final String APP_SYSTEM_NAME = "appSystemName";
    public static final String CHANNEL = "channel";

    public static final String VERSION_RELEASE = "version_release";
    public static final String MODEL_PRODUCT = "model_product";

    public static final String APP_VERSION = "appVersion";
    public static final String PATCH_VERSION = "patchVersion";
    public static final String UPDATE_PATCH_VERSION = "updatePatchVersion";

    public static final String PATCH_CODE = "patchCode";
    public static final String PATCH_DESC = "patchDesc";
    public static final String CREATE_TIME = "createTime";
    public static final String UPDATE_TIME = "updateTime";

    /*Default sort order*/
    public static final String DEFAULT_SORT_ORDER = "_id asc";

    /*Call Method*/
    public static final String METHOD_GET_ITEM_COUNT = "METHOD_GET_ITEM_COUNT";
    public static final String KEY_ITEM_COUNT = "KEY_ITEM_COUNT";

    /*Authority*/
    public static final String AUTHORITY = "com.android.ofoo.provider.userpatchinfo";

    /*Match Code*/
    public static final int ITEM = 1;
    public static final int ITEM_ID = 2;
    public static final int ITEM_POS = 3;

    /*MIME*/
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.android.ofoo.userpatchinfo";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.android.ofoo.userpatchinfo";

    /*Content URI*/
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/item");
    public static final Uri CONTENT_POS_URI = Uri.parse("content://" + AUTHORITY + "/pos");

}
