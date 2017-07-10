package com.junit.tools.dto;

import java.util.List;



public class ResultSummaryDTO {

	private String runid;
	private int testcaseCount;
	private int failedCount;
	private int errorCount;
	private String status;
	private List<DetailedResultDTO> detailedResult;
	
	public ResultSummaryDTO(String runid0, String status0, int testcaseCount0, int failedCount0, int errorCount0)
	{
		runid = runid0;
		status = status0;
		testcaseCount = testcaseCount0;
		failedCount = failedCount0;
		errorCount = errorCount0;
	}
	
	public String getRunid() {
		return runid;
	}
	public void setRunid(String runid) {
		this.runid = runid;
	}
	public int getTestcaseCount() {
		return testcaseCount;
	}
	public void setTestcaseCount(int testcaseCount) {
		this.testcaseCount = testcaseCount;
	}
	public int getFailedCount() {
		return failedCount;
	}
	public void setFailedCount(int failedCount) {
		this.failedCount = failedCount;
	}
	public int getErrorCount() {
		return errorCount;
	}
	public void setErrorCount(int errorCount) {
		this.errorCount = errorCount;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public List<DetailedResultDTO> getDetailedResult() {
		return detailedResult;
	}

	public void setDetailedResult(List<DetailedResultDTO> detailedResult) {
		this.detailedResult = detailedResult;
	}
	

}
