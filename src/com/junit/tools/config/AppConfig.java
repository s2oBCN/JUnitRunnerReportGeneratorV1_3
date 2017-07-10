package com.junit.tools.config;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class AppConfig {
	
	static ResourceBundle rb ;
	
	static {
		rb = ResourceBundle.getBundle("junitTest");
	}

	
	public static String getProperty (String propKey)
	{
		try {
			return rb.getString(propKey);
		} catch (MissingResourceException e) {
			return String.valueOf("");
		}
	}
}
