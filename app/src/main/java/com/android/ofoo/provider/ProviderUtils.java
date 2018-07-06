/*
 * Tencent is pleased to support the open source community by making Tinker available.
 *
 * Copyright (C) 2016 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ofoo.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.android.ofoo.app.BuildInfo;
import com.android.ofoo.constant.Constant;
import com.android.ofoo.util.AppUtils;
import com.android.ofoo.util.DateTimeUtils;
import com.android.ofoo.util.OfooApplicationContext;
import com.android.ofoo.util.PreferenceUtils;
import com.tencent.tinker.lib.util.TinkerLog;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhangshaowen on 16/6/3.
 */
public class ProviderUtils{

    public static void insertPatchInfo(int code){
        insertPatchInfo(code, "");
    }

    public static void insertPatchInfo(String desc){
        insertPatchInfo(0, desc);
    }

    public static void insertPatchInfo(int code, String strDesc) {
        ContentValues values = new ContentValues();
        values.put(UserPatchInfo.DEVICE_NO, String.valueOf(AppUtils.getDeviceId()));
        values.put(UserPatchInfo.APP_SYSTEM_NAME, AppUtils.getOsName());
        values.put(UserPatchInfo.APP_NAME, AppUtils.getAppName());
        values.put(UserPatchInfo.CHANNEL, AppUtils.getChannel());
        values.put(UserPatchInfo.VERSION_RELEASE, AppUtils.getOsVersion());
        values.put(UserPatchInfo.MODEL_PRODUCT, AppUtils.getPhoneModel());
        values.put(UserPatchInfo.APP_VERSION, String.valueOf(BuildInfo.VERSION_CODE));
        values.put(UserPatchInfo.PATCH_VERSION, String.valueOf(BuildInfo.PATCH_CODE));
        values.put(UserPatchInfo.UPDATE_PATCH_VERSION, PreferenceUtils.getString(Constant.SP_DOWNLOAD_PATCH_VERSION, ""));
        values.put(UserPatchInfo.PATCH_CODE, code);
        values.put(UserPatchInfo.PATCH_DESC, strDesc);
        values.put(UserPatchInfo.CREATE_TIME, DateTimeUtils.parse(new Date(), "yyyy-MM-dd HH:mm:ss:SSS"));
        values.put(UserPatchInfo.UPDATE_TIME, DateTimeUtils.parse(new Date(), "yyyy-MM-dd HH:mm:ss:SSS"));
        OfooApplicationContext.context.getContentResolver().insert(UserPatchInfo.CONTENT_URI, values);
    }

    public static String getAllPatchDesc(){
        List<String> strList = queryAll();
        if(strList == null || strList.size() == 0)
            return "";
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < strList.size(); i++){
            stringBuilder.append(strList.get(i));
        }
        return stringBuilder.toString();
    }

    public static List<String> queryAll() {
        ContentValues values = new ContentValues();
        values.put(UserPatchInfo.DEVICE_NO, String.valueOf(AppUtils.getDeviceId()));
        values.put(UserPatchInfo.APP_SYSTEM_NAME, AppUtils.getOsName());
        values.put(UserPatchInfo.APP_NAME, AppUtils.getAppName());
        values.put(UserPatchInfo.CHANNEL, AppUtils.getChannel());
        values.put(UserPatchInfo.VERSION_RELEASE, AppUtils.getOsVersion());
        values.put(UserPatchInfo.MODEL_PRODUCT, AppUtils.getPhoneModel());
        values.put(UserPatchInfo.APP_VERSION, String.valueOf(BuildInfo.VERSION_CODE));
        values.put(UserPatchInfo.PATCH_VERSION, String.valueOf(BuildInfo.PATCH_CODE));
        values.put(UserPatchInfo.UPDATE_PATCH_VERSION, PreferenceUtils.getString(Constant.SP_DOWNLOAD_PATCH_VERSION, ""));

        LinkedList<String> patchDescList = new LinkedList<String>();

        String[] projection = new String[] {
                UserPatchInfo.PATCH_DESC,
                UserPatchInfo.CREATE_TIME,
        };

        Cursor cursor = OfooApplicationContext.context.getContentResolver().query(UserPatchInfo.CONTENT_URI, projection, null, null, UserPatchInfo.DEFAULT_SORT_ORDER);
        if (cursor.moveToFirst()) {
            do {
                String desc = cursor.getString(0);
                String createTime = cursor.getString(1);
                patchDescList.add(createTime + ": " + desc);
            } while(cursor.moveToNext());
        }
        return patchDescList;
    }

    public static void deleteAll() {
        OfooApplicationContext.context.getContentResolver().delete(UserPatchInfo.CONTENT_URI, null, null);
    }
}
