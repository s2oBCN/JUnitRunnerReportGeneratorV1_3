package com.junit.tools.Main;

import com.junit.tools.dbops.DBSetup;

public class Setup {
	public static void main (String a[])
	{
		DBSetup dbSetup = new DBSetup();
		dbSetup.invokeSetup();
	}

}
