package com.android.ofoo.bean;

/**
 * Created by qianbin on 2018/6/7.
 */

public class SubBean extends ParentBean{
	public static String s = "static_sub";
	public String m = "sub";

	public static void staticTest(){
		System.out.println("====sub static " + s);
	}
}
