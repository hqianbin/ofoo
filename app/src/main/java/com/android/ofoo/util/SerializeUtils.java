package com.android.ofoo.util;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 用于对象序列化的实现
 */
public class SerializeUtils {

	private static final String TAG = "SerializeUtils";
	/**
	 * 写入对象到字符串。
	 * 注意存入对象的字段变动
	 * @param object
	 * @return
	 */
	public static String writeObjToString(Object object) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			String base64 = new String(Base64.encode(baos.toByteArray(), 0));
			return base64;
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 从字符串读取对象。
	 * 注意存入对象的字段变动。
	 * @param objstring
	 * @return
	 */
	public static Object readObjFromeString(String objstring) {
		try {
			byte[] base64Bytes = Base64.decode(objstring.getBytes(), 0);
			ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (ClassNotFoundException e) {
			LogUtils.e("类名发生了变化");
			return null;
		} catch (Exception e) {
			LogUtils.e(e);
			return null;
		}
	}
}