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

package com.android.ofoo.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.alibaba.fastjson.JSONObject;
import com.android.ofoo.app.BuildInfo;
import com.android.ofoo.constant.Constant;
import com.android.ofoo.constant.NetConstant;
import com.android.ofoo.network.ICallBack;
import com.android.ofoo.network.OKHttp;
import com.android.ofoo.util.AppUtils;
import com.android.ofoo.util.LogUtils;
import com.android.ofoo.util.PreferenceUtils;
import com.tencent.tinker.lib.tinker.TinkerInstaller;

import java.io.IOException;

import okhttp3.Call;

public class DownloadService extends IntentService {

    public DownloadService() {
        super(DownloadService.class.getSimpleName());
    }


    public DownloadService(String serviceName){
        super(serviceName);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String download_path = intent.getStringExtra("path");
        final String version_code = intent.getStringExtra("version");
        if(PreferenceUtils.getString(Constant.SP_DOWNLOAD_PATCH_VERSION, "").equals(version_code)){
            new Handler(Looper.getMainLooper()){
                public void handleMessage(Message msg) {
                    TinkerInstaller.onReceiveUpgradePatch(getApplication(), PreferenceUtils.getString(Constant.SP_DOWNLOAD_PATCH_PATH, ""));
                };
            }.sendEmptyMessage(0);
            return;
        }
        String url = NetConstant.URL + NetConstant.PATH_APP_DOWNLOAD
                + "?app_version=" + BuildInfo.VERSION_CODE
                + "&filename=" + download_path
                + "&channel=" + AppUtils.getMeta().getString("APP_CHANNEL")
                + "&version_release=" + AppUtils.getOsVersion()
                + "&model_product=" + AppUtils.getPhoneModel();

        LogUtils.d("====DownloadService----" + url);
        OKHttp.downloadFile(url, "ofoo/" + BuildInfo.VERSION_CODE, download_path, new ICallBack() {
            @Override
            public void onFailure(Call call, Exception e) {
                LogUtils.d("====DownloadService----onFailure: " + e.toString());
            }

            @Override
            public void onSuccess(Call call, JSONObject jsonObject) {
                LogUtils.d("====DownloadService----onSuccess: " + jsonObject.toString());
                String path = jsonObject.getString("download_path");
                TinkerInstaller.onReceiveUpgradePatch(getApplication(), path);
                PreferenceUtils.putString(Constant.SP_DOWNLOAD_PATCH_VERSION, version_code);
                PreferenceUtils.putString(Constant.SP_DOWNLOAD_PATCH_PATH, path);
            }
            @Override
            public void onFinally() {

            }
            @Override
            public void onProgress(int percent) {

            }
        });
    }
}
