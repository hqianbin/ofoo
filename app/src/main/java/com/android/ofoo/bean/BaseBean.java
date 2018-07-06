/**
 * Copyright (c) 2013-2014 YunZhongXiaoNiao Tech
 */
package com.android.ofoo.bean;

import java.io.Serializable;

/**
 * 所有bean的基本类
 */
public class BaseBean implements Serializable {

	public static int integer2Int(Integer integer) {
		if (integer == null) {
			return 0;
		} else {
			return integer;
		}
	}

	public static boolean boolean2Bool(Boolean o) {
		if (o == null) {
			return false;
		} else {
			return o;
		}
	}

	public static double double2D(Double o) {
		if (o == null) {
			return 0.0;
		} else {
			return o;
		}
	}

	public static float float2F(Float o) {
		if (o == null) {
			return 0.00f;
		} else {
			return o;
		}
	}
}
