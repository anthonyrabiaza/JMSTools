package org.antsoftware.messaging.wrapper;

import java.util.Properties;

public class Helper {
	
	static public Properties getProperties(String filename) throws Exception {
		Properties prop = new Properties();
		System.out.println("INFO: Looking for " + filename + " ...");
		prop.load(Helper.class.getClassLoader().getResourceAsStream(filename));
		System.out.println("INFO: File found");
		return prop;
	}
	
	static public Object newInstance(String className) throws Exception {
		System.out.println("INFO: Trying to load " + className);
		Class<?> cls = Class.forName(className);
		System.out.println("INFO: Class Loaded");
		return cls.newInstance(); 
	}
}
