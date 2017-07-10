package com.junit.tools.testcaserunner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import junit.framework.TestCase;
import junit.framework.TestFailure;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.junit.tools.dbops.Persister;
import com.junit.tools.dto.DetailedResultDTO;
import com.junit.tools.dto.ResultSummaryDTO;

public class ResultProcessor {

	private List<String> allTestCases;
	private List<String> failedTestCases;
	private List<String> errorTestCases;
	private ResultSummaryDTO resultSummary;
	
	private List <DetailedResultDTO> resultLst;
	
	public ResultProcessor()
	{
		init();
	}
	
	private void init()
	{
		allTestCases = new ArrayList<String>(50);
		failedTestCases = new ArrayList<String>(50);
		errorTestCases = new ArrayList<String>(50);
		resultLst = new ArrayList<DetailedResultDTO>(50);
	}
	
	public void processResult(TestSuite globalTestSuite, TestResult tResult) {

		System.err.println("Test Run Count > "+tResult.runCount());
		System.err.println("Test Failure Count > "+tResult.failureCount());
		System.err.println("Test Error Count > "+tResult.errorCount());
		System.err.println("Test Result > "+ (tResult.wasSuccessful() ? "Passed" : "Failed"));
		
		String status = tResult.wasSuccessful() ? "Passed" : "Failed";
		resultSummary = new ResultSummaryDTO("0",status,globalTestSuite.countTestCases(), tResult.failureCount(), tResult.errorCount());

		Enumeration<TestFailure> enu = tResult.failures();
		while (enu.hasMoreElements()) {
			failedTestCases.add(enu.nextElement().failedTest().toString());
		}

		enu = tResult.errors();


		while (enu.hasMoreElements()) {
			errorTestCases.add(enu.nextElement().failedTest().toString());
		}

		Enumeration<junit.framework.Test> enu2 = globalTestSuite.tests();
		populateAllTestCases(enu2, globalTestSuite.getName());
		persistResult();

	}

	private void populateAllTestCases(Enumeration<junit.framework.Test> enu,
			String tsName) {

		// System.out.println(tsName);
		// System.out.println(tsName.getCanonicalName());
		while (enu.hasMoreElements()) {
			junit.framework.Test t = enu.nextElement();
			if (t instanceof TestSuite)
				populateAllTestCases(((TestSuite) t).tests(),
						((TestSuite) t).getName());
			else
				allTestCases.add(((TestCase) t).getName() + "(" + tsName + ")");
		}

	}
	
	private void persistResult()
	{
		for (String testCase : allTestCases)
		{
			if (failedTestCases.contains(testCase))
				save (testCase, "Failed");
			else if (errorTestCases.contains(testCase))
				save (testCase,"Error");
			else
				save (testCase,"Passed");
		}
		Persister pers = new Persister();
		try {
			pers.save(resultLst, resultSummary);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void save(String testCase, String status) {
		DetailedResultDTO dto = new DetailedResultDTO(testCase, status);
		resultLst.add(dto);
//		Persister p = new Persister();
//		p.save (testCase, status );
		
	}
}

