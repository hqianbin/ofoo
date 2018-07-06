package com.android.ofoo.network;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.android.ofoo.constant.Constant;
import com.android.ofoo.util.AppUtils;
import com.android.ofoo.util.LogUtils;
import com.android.ofoo.util.PreferenceUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OKHttp {
	private static final String CHARSET_NAME = "UTF-8";
	private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	private static OkHttpClient okHttpClient;

	public OKHttp() {
		if(okHttpClient == null){
			okHttpClient = new OkHttpClient();
		}
	}

	public static void init() {
		if(okHttpClient == null){
//			X509TrustManager xtm = new X509TrustManager() {
//				@Override
//				public void checkClientTrusted(X509Certificate[] chain,
//						String authType) {
//				}
//
//				@Override
//				public void checkServerTrusted(X509Certificate[] chain,
//						String authType) {
//				}
//
//				@Override
//				public X509Certificate[] getAcceptedIssuers() {
//					X509Certificate[] x509Certificates = new X509Certificate[0];
//					return x509Certificates;
//				}
//			};
//
//			SSLContext sslContext = null;
//			try {
//				sslContext = SSLContext.getInstance("SSL");
//
//				sslContext.init(null, new TrustManager[] { xtm },
//						new SecureRandom());
//
//			} catch (NoSuchAlgorithmException e) {
//				e.printStackTrace();
//			} catch (KeyManagementException e) {
//				e.printStackTrace();
//			}
//			HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
//				@Override
//				public boolean verify(String hostname, SSLSession session) {
//					return true;
//				}
//			};

			okHttpClient = new OkHttpClient();
//			.hostnameVerifier(DO_NOT_VERIFY)
		}
	}

	private static String addQueryUrl(String url, String query) throws MalformedURLException {
		if (TextUtils.isEmpty(url) || TextUtils.isEmpty(query)) {
			return url;
		}
		if (hasQuery(url)) {
			if (url.endsWith("&")) {
				return url + query;
			} else {
				return url + "&" + query;
			}
		} else {
			if (url.endsWith("?")) {
				return url + query;
			} else {
				return url + "?" + query;
			}
		}
	}

	/**
	 * 判断一个url中是否有query
	 * @param url
	 * @return
	 */
	protected static boolean hasQuery(String url) throws MalformedURLException {
		URL urlObj = new URL(url);
		String query = urlObj.getQuery();
		return query != null && !TextUtils.isEmpty(query.trim());
	}

	/**
	 * 通用异步请求
	 * @param request
	 * @param responseCallback
	 */
	private static void enqueue(Request request, Callback responseCallback) {
		init();
		okHttpClient.newCall(request).enqueue(responseCallback);
	}

	public static class BasicAuthInterceptor implements Interceptor {

		private String credentials;
		public BasicAuthInterceptor(String user, String password) {
			this.credentials = Credentials.basic(user, password);
		}

		@Override
		public Response intercept(Chain chain) throws IOException {
			Request request = chain.request();
			Request authenticatedRequest = request.newBuilder()
					.header("Authorization", credentials).build();
			return chain.proceed(authenticatedRequest);
		}

	}


	public static void exec(final String url, final Map<String,String> params, final ICallBack callBack)
			throws IOException {

		FormBody.Builder formBuilder = new FormBody.Builder();
		Set<String> keys = params.keySet();
		for(String key:keys){
			formBuilder.add(key, params.get(key));
		}
		RequestBody body = formBuilder.build();
		Request request = new Request.Builder().url(url).post(body).build();

		enqueue(request, new Callback() {
			@Override
			public void onResponse(final Call arg0,final Response response) throws IOException {
				try {
					final String data = response.body().string();
					new Handler(Looper.getMainLooper()){
						public void handleMessage(Message msg) {

						};
					}.sendEmptyMessage(0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {

			}

		});
	}

	private static String getUrlCommonParams(){
		String str = "device_no=" + AppUtils.getDeviceId()
				+ "&app_system_name=" + AppUtils.getOsName()
				+ "&app_name=" + AppUtils.getAppName()
				+ "&channel=" + AppUtils.getMeta().getString("APP_CHANNEL");
		PreferenceUtils.putString(Constant.SP_URL_COMMON_PARAMAS, str);
		return str;
	}

	public static void get(String url, final ICallBack callBack)throws IOException {
		String commonParams = PreferenceUtils.getString(Constant.SP_URL_COMMON_PARAMAS, "");
		if(commonParams == null || commonParams.equals("")){
			commonParams = getUrlCommonParams();
		}
		if(url.endsWith("&")){
			url = url + commonParams;
		}else {
			url = url + "&" + commonParams;
		}
		LogUtils.d("====url----" + url);
		Request request = new Request.Builder().url(url).build();
		enqueue(request, new Callback() {
			@Override
			public void onResponse(final Call call, final Response response) throws IOException {
				try {
					final String data = response.body().string();
					final com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(data);
					new Handler(Looper.getMainLooper()){
						public void handleMessage(Message msg) {
							callBack.onSuccess(call, jsonObject);
							callBack.onFinally();
						};
					}.sendEmptyMessage(0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(final Call call, final IOException e) {
				new Handler(Looper.getMainLooper()){
					public void handleMessage(Message msg) {
						callBack.onFailure(call, e);
						callBack.onFinally();
					};
				}.sendEmptyMessage(-1);
			}
		});
	}

	public static void post(final String url, String json, final ICallBack callBack) {
		LogUtils.d("====url----" + url + "    " + json);
		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder().url(url).post(body).build();

		enqueue(request, new Callback() {
			@Override
			public void onFailure(final Call call, final IOException e) {
				new Handler(Looper.getMainLooper()){
					public void handleMessage(Message msg) {
						callBack.onFailure(call, e);
						callBack.onFinally();
					};
				}.sendEmptyMessage(-1);
			}

			@Override
			public void onResponse(final Call call, Response response) throws IOException {
				try {
					final String data = response.body().string();
					final com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(data);
					new Handler(Looper.getMainLooper()){
						public void handleMessage(Message msg) {
							callBack.onSuccess(call, jsonObject);
							callBack.onFinally();
						};
					}.sendEmptyMessage(0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * @param url 下载连接
	 * @param saveDir 储存下载文件的SDCard目录
	 * @param callBack 下载监听
	 */
	public static void downloadFile(String url, final String saveDir, final String fileName, final ICallBack callBack) {
		String commonParams = PreferenceUtils.getString(Constant.SP_URL_COMMON_PARAMAS, "");
		if(commonParams == null || commonParams.equals("")){
			commonParams = getUrlCommonParams();
		}
		if(url.endsWith("&")){
			url = url + commonParams;
		}else {
			url = url + "&" + commonParams;
		}
		LogUtils.d("====url----" + url);
		Request request = new Request.Builder().url(url).build();
		okHttpClient.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(final Call call, final IOException e) {
				// 下载失败
				new Handler(Looper.getMainLooper()){
					public void handleMessage(Message msg) {
						callBack.onFailure(call, e);
						callBack.onFinally();
					};
				}.sendEmptyMessage(-1);
			}
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				InputStream is = null;
				byte[] buf = new byte[2048];
				int len = 0;
				FileOutputStream fos = null;
				// 储存下载文件的目录
				String savePath = isExistDir(saveDir);
				try {
					is = response.body().byteStream();
					long total = response.body().contentLength();
					File file = new File(savePath, fileName);
					fos = new FileOutputStream(file);
					long sum = 0;
					while ((len = is.read(buf)) != -1) {
						fos.write(buf, 0, len);
						sum += len;
						final int progress = (int) (sum * 1.0f / total * 100);

						new Handler(Looper.getMainLooper()){
							public void handleMessage(Message msg) {
								callBack.onProgress(progress);
							};
						}.sendEmptyMessage(1);
					}
					fos.flush();
					// 下载完成
					String download_path = savePath + "/" + fileName;
					final JSONObject jsonObject = new JSONObject();
					jsonObject.put("download_path", download_path);
					new Handler(Looper.getMainLooper()){
						public void handleMessage(Message msg) {
							callBack.onSuccess(null, jsonObject);
						};
					}.sendEmptyMessage(0);

				} catch (Exception e) {
					callBack.onFailure(null, e);
				} finally {
					try {
						if (is != null)
							is.close();
					} catch (IOException e) {
					}
					try {
						if (fos != null)
							fos.close();
					} catch (IOException e) {
					}
					callBack.onFinally();
				}
			}
		});
	}

	/**
	 * @param saveDir
	 * @return
	 * @throws IOException
	 * 判断下载目录是否存在
	 */
	private static String isExistDir(String saveDir) throws IOException {
		// 下载位置
		File downloadFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), saveDir);
		if (!downloadFile.mkdirs()) {
			downloadFile.createNewFile();
		}
		String savePath = downloadFile.getAbsolutePath();
		return savePath;
	}

	/**
	 * @param url
	 * @return
	 * 从下载连接中解析出文件名
	 */
	private static String getNameFromUrl(String url) {
		return url.substring(url.lastIndexOf("/") + 1);
	}

}
