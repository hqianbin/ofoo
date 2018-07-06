package com.android.ofoo.network;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by hp on 2017/6/2.
 */

public interface ICallBack {
    void onFailure(Call call, Exception e);
    void onSuccess(Call call, JSONObject response);
    void onProgress(int progress);
    void onFinally();
}
