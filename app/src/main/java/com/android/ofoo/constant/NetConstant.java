package com.android.ofoo.constant;

/**
 * 用于存放跟网络相关的常量
 * API_RESPONSE_CODE, API_URL...
 */
public interface NetConstant {
	String URL = "http://192.168.1.7:3000";
	/**
	 * version // 版本号
	 * channel // 渠道号
	 * osname // 系统名 e.g. @"iOS"
	 * osversion // 系统版本号 e.g. @"4.0"
	 * model // 设备的型号 e.g. @"iPhone", @"iPod touch"
	 * udid // 设备的uuid
	 * devtoken // 用于推送的token
	 * user_phone_name // 用户的手机名 e.g. @"fengyiyi's iPhone"
	 */
	String VERSION = "version";
	String CHANNEL = "channel";
	String OSNAME = "osname";
	String OSVERSION = "osversion";
	String OS = "os";
	String ANDROID = "Android";
	String UDID = "udid";
	String MODEL = "model";
	String DEVTOKEN = "devtoken";
	String USER_PHONE_NAME = "user_phone_name";

	//提交API请求的类型
	String GET = "get";
	String POST = "post";

	String PATH_GET_APP_BY_VERSION = "/app/get_app_by_version";//
	String PATH_GET_PATCH_BY_VERSION = "/app/get_patch_by_version";//
	String PATH_APP_REPORT_PATCH_RESULT = "/app/report_patch_result";//
	String PATH_APP_DOWNLOAD = "/app/download";//
	String PATH_APP_UPLOAD = "/app/upload";//
	String PATH_APP_UPDATE_USER_INFO = "/users/app/update_user_info";//
}
