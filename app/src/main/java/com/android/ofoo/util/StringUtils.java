package com.android.ofoo.util;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;

import java.util.Collection;
import java.util.Iterator;

/**
 * 字符串相关的工具类
 */
public class StringUtils {
	public final static String ENCODING = "utf-8";

	/**
	 * 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false
	 */
	public static boolean isEmpty(CharSequence value) {
		if (value != null && !"".equalsIgnoreCase(value.toString().trim()) && !"null".equalsIgnoreCase(value.toString().trim())) {
			return false;
		} else {
			return true;
		}
	}

	public static int string2Int(String intStr) {
		try {
			return isEmpty(intStr) ? 0 : Integer.parseInt(intStr);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * 判断多个字符串是否相等，如果其中有一个为空字符串或者
	 * ...null，则返回false，只有全相等才返回true
	 */
	public static boolean equalsTo(String... agrs) {
		String last = null;
		for (int i = 0; i < agrs.length; i++) {
			String str = agrs[i];
			if (isEmpty(str)) {
				return false;
			}
			if (last != null && !str.equalsIgnoreCase(last)) {
				return false;
			}
			last = str;
		}
		return true;
	}

	/**
	 * 拼接一个array为String
	 *
	 * @param selectedItemCodeList
	 * @param separator
	 * @return
	 */
	public static String array2String(Collection selectedItemCodeList, String separator) {
		if (selectedItemCodeList == null || selectedItemCodeList.size() <= 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		Iterator it = selectedItemCodeList.iterator();
		while (it.hasNext()) {
			sb.append(it.next()).append(separator);
		}
		return sb.substring(0, sb.length() - separator.length());
	}

	/**
	 * 返回一个高亮spannable
	 *
	 * @param content 文本内容
	 * @param color   高亮颜色
	 * @param start   起始位置
	 * @param end     结束位置
	 * @return 高亮spannable
	 */
	public static CharSequence getHighLightText(String content, int color, int start, int end) {
		if (TextUtils.isEmpty(content)) {
			return "";
		}
		start = start >= 0 ? start : 0;
		end = end <= content.length() ? end : content.length();
		SpannableString spannable = new SpannableString(content);
		CharacterStyle span = new ForegroundColorSpan(color);
		spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannable;
	}
}
