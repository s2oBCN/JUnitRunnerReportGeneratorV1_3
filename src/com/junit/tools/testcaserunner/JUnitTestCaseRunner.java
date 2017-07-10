package com.junit.tools.testcaserunner;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.ResultPrinter;
import junit.textui.TestRunner;

import org.junit.Test;

import com.junit.tools.config.AppConfig;
import com.junit.tools.util.ClassFinder;

public class JUnitTestCaseRunner {

	static {
		try {
			PrintStream sysoutP = new PrintStream(AppConfig.getProperty("consoleoutputloc"));
			System.setOut(sysoutP);
			System.setErr(sysoutP);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	
	}
	private TestSuite globalTestSuite;
	private ResultProcessor resultProc;

	private void init() {
		globalTestSuite = new TestSuite();
		resultProc = new ResultProcessor();
	}

	private void runTestCases() throws Exception {
		TestRunner runner = new TestRunner(new ResultPrinter(new PrintStream(AppConfig.getProperty("outputfile"))));
		TestResult tResult=runner.doRun(globalTestSuite);
		resultProc.processResult(globalTestSuite,tResult);
	}	
	
	
	public void getAllTestCases() throws Exception {
		ClassFinder clsFinder = new ClassFinder();
		TestSuite tSuiteLocal;
		Vector<Class<?>> v = clsFinder.getAllTestCases();
		if (v != null && v.size() > 0) {
			for (Class<?> cls : v) {
				tSuiteLocal = new TestSuite(cls.getName());
				tSuiteLocal = addToTestSuite(tSuiteLocal, cls);
				globalTestSuite.addTest(tSuiteLocal);
			}
		}

		else {
			System.out.println("No subclasses of junit.framework.TestCase found.");
		}
	}

	private TestSuite addToTestSuite(TestSuite ts, Class cls) throws Exception {
		for (Method method : cls.getMethods()) {
			if (method.isAnnotationPresent(Test.class)) {
//				System.out.println(cls.getName()+" <<>> "+method.getName());
				junit.framework.TestCase tstLocal = (junit.framework.TestCase) cls
						.newInstance();
				tstLocal.setName(method.getName());
				ts.addTest(tstLocal);
			}
		}
		return ts;

	}
	
	public void start()
	{
		 init();
		 try {
			getAllTestCases();
			runTestCases();
			System.out.println("Test case run completed");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

/*
	public static void main(String a[]) {
		
		 JUnitTestCaseRunner sdmRunner = new JUnitTestCaseRunner();
		 sdmRunner.init();
		 try {
			sdmRunner.getAllTestCases();
			sdmRunner.runTestCases();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 

	}
*/
}
