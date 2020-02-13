package com.minister.pm.util;

import java.io.File;
import java.net.URL;

/**
 * @date 2020年2月13日 下午9:39:30
 * @author jianxinliu
 */
public class PMFileIOUtil {

	/**
	 * 读取 jar 包所在环境文件地址
	 * 
	 * @param fileName
	 * @return null if not fount
	 */
	public static String getUserEnvFilePath(String fileName) {
		String ret = null;
		String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		File file = new File(path);
		if (!file.exists()) {
			return null;
		}
		String[] files = file.list();
		if (files.length == 0) {
			return null;
		}
		for (int i = 0; i < files.length; i++) {
//			System.out.println(files[i]);
			if (files[i] != null && fileName.equals(files[i])) {
				ret = path + fileName;
				break;
			}
		}
		return ret;
	}

	/**
	 * 用于读取 PM 自身的文件
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getPMFilePath(String fileName) {
		return PMFileIOUtil.class.getResource(fileName).getPath();
	}

	public static void main(String[] args) {
		// System.out.println(getUserEnvFilePath("banner_alpha.txt"));
		getPMFilePath("/defaultConfig.yml");
	}
}
