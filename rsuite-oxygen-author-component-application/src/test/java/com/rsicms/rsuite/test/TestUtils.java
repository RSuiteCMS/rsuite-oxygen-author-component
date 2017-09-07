package com.rsicms.rsuite.test;

import java.io.InputStream;

public class TestUtils {

	public static InputStream getResource(Class<?> clazz, String fileName){
		String packageName = clazz.getPackage().getName();
		String packagePath = "/" + packageName.replace(".", "/");
		return clazz.getResourceAsStream(packagePath + "/" + fileName);
	}
}
