package com.junit.tools.util;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.junit.tools.config.AppConfig;
import com.junit.tools.dbops.Persister;
import com.junit.tools.dto.DetailedResultDTO;
import com.junit.tools.dto.ResultSummaryDTO;

public class ReportGenerator {
	
	private Persister persister;
	private List<ResultSummaryDTO> result;
	
	private StringBuilder sbHTML;
	String newLineChar;
	PrintStream outputFile;
	
	private void init()
	{
		persister = new Persister();
		int runCount = 1;
		try {
		runCount = Integer.parseInt(AppConfig.getProperty("report.numberOfRuns"));
		} catch (NumberFormatException e)
		{
			runCount = 1;
		}
		result = persister.fetchResult(runCount);
		
		//HashMap<String, List<String>> resultMatrix = createResultMatrix(result);
		
		//printMap(resultMatrix);
		
		sbHTML = new StringBuilder();
		newLineChar = System.getProperty("line.separator");
		try {
			outputFile = new PrintStream(AppConfig.getProperty("report.location")+System.getProperty("file.separator")+"JUnitReport.html");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	private void printMap(HashMap<String, List<String>> resultMatrix) {

		for (String s:resultMatrix.keySet())
		{
			System.out.println(s +"<<>>"+resultMatrix.get(s));
		}
	}

	private HashMap<String, List<String>> createResultMatrix(List<ResultSummaryDTO> result0) 
	{
		HashMap<String, List<String>> resultMatrix = new HashMap<String, List<String>>();
//		System.out.println(result0);
		List<String> resultStatusLst = null;
		int pass=0;
		for (ResultSummaryDTO rsl : result0)
		{
//			System.out.println(rsl.getDetailedResult());
//			System.out.println(rsl.getTestcaseCount());
			for(DetailedResultDTO detailDTO : rsl.getDetailedResult())
			{
//				System.out.println(detailDTO.getTestCase()+ "  pass>> "+pass);
				resultStatusLst = resultMatrix.get(detailDTO.getTestCase());
				if (resultStatusLst == null){
					resultStatusLst = new ArrayList<String>(5);
				}
				resultStatusLst = addNoRunData(resultStatusLst, pass);
				resultStatusLst.add(detailDTO.getStatus());
				resultMatrix.put(detailDTO.getTestCase(), resultStatusLst);
			}
			pass++;
		}
		addNoRunDataForLastRun(resultMatrix,pass);
		return resultMatrix;
	}

	private void addNoRunDataForLastRun(HashMap<String, List<String>> resultMatrix, int pass) 
	{
		List<String> resultStatusLst = null;
		for (String resultKey: resultMatrix.keySet())
		{
			resultStatusLst = resultMatrix.get(resultKey);
			int len = resultStatusLst.size();
			for (int i=0; i<( pass- len) ; i++)
			{
				resultStatusLst.add("No Run");
			}
		}
	}

	private List<String> addNoRunData(List<String> resultStatusLst, int pass) 
	{
		int len = resultStatusLst.size();
		for (int i=0; i<pass-len;i++)
		{
			resultStatusLst.add("No Run");
		}
		return resultStatusLst;
		
	}

	public void generateMatrixReport()
	{
		init();
		HashMap<String, List<String>> resultMatrix = createResultMatrix(result);
//		printMap(resultMatrix);
		generateHTMLHeaderPart();
		generateSummaryHeader();
		
		sbHTML.append("<div class=\"scrollit\">");
		sbHTML.append("<table><tr>");
		generateDetailedHeading();
		sbHTML.append("</tr>");
		generateDetailedMatrixHTML(resultMatrix);
		sbHTML.append("</table></div>");
		sbHTML.append("</body></html>");
		
		generateFooter();
		outputFile.println(sbHTML);
		System.out.println("Matrix Report generated successfully");
		
	}
	
	private void generateDetailedMatrixHTML(HashMap<String, List<String>> resultMatrix) 
	{
		for (String testCaseName:resultMatrix.keySet())
		{
			sbHTML.append("<tr><td>"+testCaseName+"</td>");
			for (String status:resultMatrix.get(testCaseName))
			{
				sbHTML.append(appendStatusWithClass(status));
			}
			sbHTML.append("</tr>");
		}

	}

	private void generateDetailedHeading() {
		sbHTML.append("<th> Test Case Name </th> ");
		for (ResultSummaryDTO rsl : result)
		{
			sbHTML.append("<th>"+convertRunIdToDate(rsl.getRunid())+"</th>");
		}
		
	}

	private void generateHTMLHeaderPart()
	{
		sbHTML.append("<html><Title>").append(AppConfig.getProperty("report.title")).append("</Title>");
		sbHTML.append("	<style>").append(newLineChar);
		sbHTML.append("table { border-collapse: collapse;}").append(newLineChar);
		sbHTML.append("table, td, th { border: 1px solid #013ADF;font-size:95%;}").append(newLineChar);
		sbHTML.append("th {background-color: #2E9AFE; color: white;}").append(newLineChar);
		sbHTML.append(".fail  { background: red }");
		sbHTML.append(".error  { background: orange }");
		sbHTML.append(".pass  { background: green }");
		sbHTML.append(".norun  { background: gray }");
		sbHTML.append(".red  { color:red; background: red }");
		sbHTML.append(".orange  { color:orange;background: orange }");
		sbHTML.append(".green  { color:green;background: green }");
		sbHTML.append(".scrollit {overflow:scroll;height:72%;}");
		sbHTML.append(".lbl {border: 1px solid white; background-color: #2E9AFE; color: white;}");
		sbHTML.append(".hd { font-weight: bold; }");
		sbHTML.append("</style></head>").append(newLineChar);
		sbHTML.append("<body><H2>").append(AppConfig.getProperty("report.title")).append("</H2>").append(newLineChar);

	}
	
	public void generateReport()
	{
		init();
		generateHTMLHeaderPart();
		generateSummaryHeader();
		
		sbHTML.append("<div class=\"scrollit\">");
		sbHTML.append("<table><tr>");
		
		for (ResultSummaryDTO summary : result)
		{
			sbHTML.append("<td>");
			generateDetailedBody(summary.getDetailedResult());
			sbHTML.append("</td>");
		}
		sbHTML.append("</tr></table>");
		sbHTML.append("</div>");
		sbHTML.append("</body></html>");
						
		generateFooter();
		outputFile.println(sbHTML);
		System.out.println("Report generated successfully");
	}

	private void generateSummaryHeader() {
		
		ResultSummaryDTO summary = result.get(0);
		
//		sbHTML.append("<tr><td class=lbl>Last Run date </td><td>").append(convertRunIdToDate(summary.getRunid())).append("</td></tr>").append(newLineChar);
//		sbHTML.append("<tr><td class=lbl>Tests Run </td><td>").append(summary.getTestcaseCount()).append("</td></tr>").append(newLineChar);
//		sbHTML.append("<tr><td class=lbl>Failure</td><td>").append(summary.getFailedCount()).append("</td></tr>").append(newLineChar);
//		sbHTML.append("<tr><td class=lbl>Errors</td><td>").append(summary.getErrorCount()).append("</td></tr>").append(newLineChar);
//		sbHTML.append("<tr><td class=lbl>Final Status</td>").append(apendStatusWithClass (summary.getStatus())).append("</tr>").append(newLineChar);
		sbHTML.append("<table>");
		sbHTML.append("<tr><td class=hd>Last Run date </td><td>").append(convertRunIdToDate(summary.getRunid())).append("</td></tr>").append(newLineChar);
		sbHTML.append("<tr><td class=hd>Tests Run </td><td>").append(summary.getTestcaseCount()).append("</td></tr>").append(newLineChar);
		sbHTML.append("<tr><td class=hd>Failure</td><td>").append(summary.getFailedCount()).append("</td></tr>").append(newLineChar);
		sbHTML.append("<tr><td class=hd>Errors</td><td>").append(summary.getErrorCount()).append("</td></tr>").append(newLineChar);
		//sbHTML.append("<tr><td class=hd>Final Status</td>").append(apendStatusWithClass (summary.getStatus())).append("</tr>").append(newLineChar);
		sbHTML.append("<tr><td class=hd>Test Status</td><td>");
		apendColorBar(summary.getTestcaseCount(),summary.getFailedCount(), summary.getErrorCount());
		sbHTML.append("</td></tr>");
		sbHTML.append("</table>");
	}

	private void apendColorBar(float testcaseCount, float failedCount,
			float errorCount) {
		float passedPercentage = (testcaseCount - failedCount - errorCount)*100/testcaseCount;
		float errorPercentage =  (errorCount * 100) / testcaseCount;
		float failedPercentage = (failedCount * 100 )/testcaseCount;
//		System.out.println(testcaseCount+"<<<<<>>>>"+errorCount+"<<<<<>>>>>"+failedCount);
//		System.out.println(passedPercentage+"<<<<<>>>>"+errorPercentage+"<<<<<>>>>>"+failedPercentage);
		//sbHTML.append("<table width=74% border=none><tr><td width=10% class=hd>Test Status</td> <td>");
		sbHTML.append("<table width=100% border=none>");
		sbHTML.append("<tr><td width=").append(passedPercentage).append("% class=pass font-size:80% >").append(Math.round(passedPercentage)).append("% </td>");
		sbHTML.append("<td width=").append(errorPercentage).append("% class=error font-size:80%>").append(Math.round(errorPercentage)).append("% </td>");
		sbHTML.append("<td width=").append(failedPercentage).append("% class=fail font-size:80%>").append(Math.round(failedPercentage)).append("% </td>");
		sbHTML.append("</tr></table>");
		//sbHTML.append("</td></tr></table>");
		
	}


	private String convertRunIdToDate(String runId)
	{
		String frmt = "dd-MMM-yy HH:mm";
		SimpleDateFormat sdf = new SimpleDateFormat(frmt);
		return sdf.format(new java.util.Date(Long.parseLong(runId)));
	}
	
//	private String getStatus(String statusFlag)
//	{
//	}
	
	private void generateDetailedBody(List<DetailedResultDTO> lstDetailData) 
	{
		String runId = null;
		StringBuilder sbtemp = new StringBuilder();
		
		for (DetailedResultDTO data : lstDetailData)
		{
			sbtemp.append("<tr><td>").append(data.getTestCase()).append("</td>").append(appendStatusWithClass(data.getStatus())).append("</tr>").append(newLineChar);
			runId = data.getRunId();
		}
		
		sbHTML.append("<table border=2><tr><td colspan=2 class=hd> Run Date :").append(convertRunIdToDate(runId)).append("</td></tr>");
		sbHTML.append("<tr><th>TestCase Name </th><th>RunStatus </th></tr>");
		sbHTML.append(sbtemp);
		sbHTML.append("</table>");
	}

	private String appendStatusWithClass(String status) 
	{
		String s = null;
		if(status.equals("Passed"))
			s="<td class=pass>";
		if(status.equals("Failed"))
			s="<td class=fail>";
		if(status.equals("Error"))
			s="<td class=error>";
		if(status.equals("No Run"))
			s="<td class=norun>";
		s+= status + " </td> ";
		return s;
	}


	private void generateFooter() {
		// TODO Auto-generated method stub
		
	}
	
	
	public static void main (String a[])
	{
		ReportGenerator rg = new ReportGenerator();
		rg.generateMatrixReport();
//		rg.generateReport();
	}

}
