package com.net.burgesslee.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类用于字符串操作
 * @author Burgess
 */
public class StringTools {

	/**
	 * 判断是否存在汉字
	 * @param countname
	 * @return
	 */
	public static boolean checkcountname(String countname){
         Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
            Matcher m = p.matcher(countname);
            if (m.find()) {
                return true;
            }
            return false;
    }
	
	/**
	 * 判断整个字符串是否都是由汉字组成
	 * @param name
	 * @return
	 */
	public static boolean checkname(String name){
        int n = 0;
        for(int i = 0; i < name.length(); i++) {
            n = (int)name.charAt(i);
            if(!(19968 <= n && n <40869)) {
                return false;
            }
        }
        return true;
    }
	
}
