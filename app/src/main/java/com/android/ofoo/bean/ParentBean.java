package com.android.ofoo.bean;

/**
 * Created by qianbin on 2018/6/7.
 */

public class ParentBean {
	public static String s = "static_parent";
	public String m = "parent";

	public static void staticTest(){
		System.out.println("====parent static " + s);
	}
}
