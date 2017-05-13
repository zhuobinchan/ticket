package com.tjing.frame.util;

import java.text.SimpleDateFormat;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.joda.time.DateTime;

public class EncryptUtils {
	public static final String encrypt(String source) {
		if (source == null) {
			source = "";
		}
		String pwd = new Md5Hash(source).toString();
		pwd = pwd.substring(8,24); 
		return pwd;
	}
	public static void main(String[] args){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateTime d = new DateTime(1393976137544L);
		
		System.out.println(d.toString("yyyy-MM-dd HH:mm:ss"));
		//d8a92cc19d6a0017
		System.out.println(EncryptUtils.encrypt("admin123"));
	}
}

