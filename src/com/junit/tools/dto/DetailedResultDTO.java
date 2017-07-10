package com.junit.tools.dto;

import java.text.SimpleDateFormat;

public class DetailedResultDTO
{

	private String testCase;
	private String status;
	private String runId;
	
	public DetailedResultDTO (String testCase0, String status0)
	{
		testCase = testCase0;
		status = status0;
	}
	public String getTestCase() {
		return testCase;
	}
	public void setTestCase(String testCase) {
		this.testCase = testCase;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRunId() {
		return runId;
	}
	public void setRunId(String runId) {
		this.runId = runId;
	}
	
	public String toString()
	{
		return "runId > "+runId+"   "+new java.util.Date(Long.parseLong(runId))+" Status > "+status+ "  "+testCase;
	}
	
	private String convertRunIdToDate(String runId)
	{
		String frmt = "dd-MMM-yy HH:mm";
		SimpleDateFormat sdf = new SimpleDateFormat(frmt);
		return sdf.format(new java.util.Date(Long.parseLong(runId)));
	}
	
}
