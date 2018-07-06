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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;

import com.alibaba.fastjson.JSONObject;
import com.android.ofoo.app.BuildInfo;
import com.android.ofoo.constant.Constant;
import com.android.ofoo.constant.NetConstant;
import com.android.ofoo.network.BaseCallBack;
import com.android.ofoo.network.OKHttp;
import com.android.ofoo.provider.ProviderUtils;
import com.android.ofoo.util.AppUtils;
import com.android.ofoo.util.LogUtils;
import com.android.ofoo.util.PreferenceUtils;
import com.android.ofoo.util.Utils;
import com.tencent.tinker.lib.service.DefaultTinkerResultService;
import com.tencent.tinker.lib.service.PatchResult;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.lib.util.TinkerServiceInternals;

import java.io.File;

import okhttp3.Call;


/**
 * optional, you can just use DefaultTinkerResultService
 * we can restart process when we are at background or screen off
 * Created by zhangshaowen on 16/4/13.
 */
public class SampleResultService extends DefaultTinkerResultService {
    private static final String TAG = "Tinker.SampleResultService";


    @Override
    public void onPatchResult(final PatchResult result) {
        if (result == null) {
            TinkerLog.e(TAG, "SampleResultService received null result!!!!");
            return;
        }
        TinkerLog.i(TAG, "SampleResultService receive result: %s", result.toString());

        //first, we want to kill the recover process
        TinkerServiceInternals.killTinkerPatchServiceProcess(getApplicationContext());

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                uploadTinkerLog(result);
            }
        });
        // is success and newPatch, it is nice to delete the raw file, and restart at once
        // for old patch, you can't delete the patch file
        if (result.isSuccess) {
            deleteRawPatchFile(new File(result.rawPatchFilePath));

            //not like TinkerResultService, I want to restart just when I am at background!
            //if you have not install tinker this moment, you can use TinkerApplicationHelper api
            if (checkIfNeedKill(result)) {
                if (Utils.isBackground()) {
                    TinkerLog.i(TAG, "it is in background, just restart process");
                    restartProcess();
                } else {
                    //we can wait process at background, such as onAppBackground
                    //or we can restart when the screen off
                    TinkerLog.i(TAG, "tinker wait screen to restart process");
                    new ScreenState(getApplicationContext(), new ScreenState.IOnScreenOff() {
                        @Override
                        public void onScreenOff() {
                            restartProcess();
                        }
                    });

                }
            } else {
                TinkerLog.i(TAG, "I have already install the newly patch version!");
            }
        }
    }

    private void uploadTinkerLog(PatchResult result){
        String url = NetConstant.URL + NetConstant.PATH_APP_REPORT_PATCH_RESULT;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("device_no", AppUtils.getDeviceId());
        jsonObject.put("app_name", AppUtils.getAppName());
        jsonObject.put("app_system_name", AppUtils.getOsName());
        jsonObject.put("channel", AppUtils.getChannel());
        jsonObject.put("version_release", AppUtils.getOsVersion());
        jsonObject.put("model_product", AppUtils.getPhoneModel());
        jsonObject.put("app_version", BuildInfo.VERSION_CODE);
        jsonObject.put("patch_version", BuildInfo.PATCH_CODE);
        jsonObject.put("update_patch_version", PreferenceUtils.getString(Constant.SP_DOWNLOAD_PATCH_VERSION, ""));

        LogUtils.d("======" + jsonObject.toString());
        if (result.isSuccess) {
            jsonObject.put("patch_code", 0);
            jsonObject.put("patch_desc", "patch_success");
        } else {
            String desc = ProviderUtils.getAllPatchDesc();
            jsonObject.put("patch_code", -9999);
            jsonObject.put("patch_desc", desc);
        }

        OKHttp.post(url, jsonObject.toString(), new BaseCallBack() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onSuccess(Call call, JSONObject response) {
                ProviderUtils.deleteAll();
            }
        });
    }

    /**
     * you can restart your process through service or broadcast
     */
    private void restartProcess() {
        TinkerLog.i(TAG, "app is background now, i can kill quietly");
        //you can send service or broadcast intent to restart your process
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    static class ScreenState {
        interface IOnScreenOff {
            void onScreenOff();
        }

        ScreenState(Context context, final IOnScreenOff onScreenOffInterface) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            context.registerReceiver(new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent in) {
                    String action = in == null ? "" : in.getAction();
                    TinkerLog.i(TAG, "ScreenReceiver action [%s] ", action);
                    if (Intent.ACTION_SCREEN_OFF.equals(action)) {

                        context.unregisterReceiver(this);

                        if (onScreenOffInterface != null) {
                            onScreenOffInterface.onScreenOff();
                        }
                    }
                }
            }, filter);
        }
    }

}
