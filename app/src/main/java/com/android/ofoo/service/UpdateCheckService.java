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

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;

import com.alibaba.fastjson.JSONObject;
import com.android.ofoo.app.BuildInfo;
import com.android.ofoo.constant.NetConstant;
import com.android.ofoo.manager.AppActivityManager;
import com.android.ofoo.network.BaseCallBack;
import com.android.ofoo.network.OKHttp;
import com.android.ofoo.util.AppUtils;
import com.android.ofoo.util.LogUtils;

import java.io.IOException;

import okhttp3.Call;

public class UpdateCheckService extends IntentService {

    public UpdateCheckService() {
        super(UpdateCheckService.class.getSimpleName());
    }


    public UpdateCheckService(String serviceName){
        super(serviceName);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            String url = NetConstant.URL + NetConstant.PATH_GET_PATCH_BY_VERSION
                    + "?app_version="+ BuildInfo.VERSION_CODE
                    + "&patch_version=" + BuildInfo.PATCH_CODE
                    + "&channel=" + AppUtils.getMeta().getString("APP_CHANNEL")
                    + "&version_release=" + AppUtils.getOsVersion()
                    + "&model_product=" + AppUtils.getPhoneModel();

            LogUtils.d("====UpdateCheckService----" + url);
            OKHttp.get(url, new BaseCallBack() {
                @Override
                public void onFailure(Call call, Exception e) {
                    LogUtils.d("====updateService----onFailure: " + e.toString());
                }

                @Override
                public void onSuccess(Call call, JSONObject jsonObject) {
                    if(jsonObject != null) {
                        LogUtils.d("====updateService----onSuccess: " + jsonObject.toString());
                    }
                    int code = jsonObject.getIntValue("code");
                    if(code == 0) {
                        int type = jsonObject.getIntValue("type");
                        if(type == 1){

                        }else if(type == 2){
                            JSONObject object = jsonObject.getJSONObject("result");
                            String path = object.getString("download_path");
                            String version = object.getString("version_code");
                            Activity foregroundActivity = AppActivityManager.getInstance().getForegroundActivity();
                            if(foregroundActivity != null) {
                                Intent intent = new Intent(AppActivityManager.getInstance().getForegroundActivity(), DownloadService.class);
                                intent.putExtra("path", path);
                                intent.putExtra("version", version);
                                foregroundActivity.startService(intent);
                            }
                        }
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
