package com.net.burgesslee.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * 文件处理工具类
 * @author Administrator
 */
public class FileUtils {
	
	private static final List<String> FILENAMES = new ArrayList<String>();
	
	/**
	 * 读取某个目录下所有文件、文件夹
	 * @param path
	 * @return
	 */
	public static ArrayList<String> getFiles(String path) {
	    ArrayList<String> files = new ArrayList<String>();
	    File file = new File(path);
	    File[] tempList = file.listFiles();

	    for (int i = 0; i < tempList.length; i++) {
	        if (tempList[i].isFile()) {
	            files.add(tempList[i].toString());
	        }
	        if (tempList[i].isDirectory()) {
	        }
	    }
	    return files;
	}
	
	/**
	 * 获取文件名称
	 */
	public static String getFileName(File file){
		return file.getName();
	}
	
	/**
	 * 获取制定目录下所有的.xlsx结尾的文件名称
	 * @param path
	 */
	public static List<String> getAllFileName(String path) {
		File file = new File(path);
		if(file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				//判断是否是文件对象
				if(f.isFile()) {
					if(f.getName().endsWith(".xlsx") || f.getName().endsWith(".xls")) {
//						FILENAMES.add(f.getName());
						FILENAMES.add(f.getAbsolutePath());
					}
				}
				else if(f.isDirectory()){
					//是一个目录对象
					String absolutePath = f.getAbsolutePath();
					getAllFileName(absolutePath);
				}
			}
		}
		return FILENAMES;
	}
}
