package com.junit.tools.Main;

import com.junit.tools.util.ReportGenerator;

public class ReportUtil {
	public static void main (String a[])
	{
		ReportGenerator reportGenerator = new ReportGenerator();
		reportGenerator.generateMatrixReport();
	}

}
