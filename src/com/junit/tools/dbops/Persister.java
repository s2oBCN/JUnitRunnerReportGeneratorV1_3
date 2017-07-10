package com.junit.tools.dbops;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hsqldb.Server;

import com.junit.tools.config.AppConfig;
import com.junit.tools.dto.DetailedResultDTO;
import com.junit.tools.dto.ResultSummaryDTO;

public class Persister {

	private Server hsqlServer = null;
	private Connection connection = null;
	private ResultSet rs = null;
	private PreparedStatement pstm;

	public Persister() {
		try {
			init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private final static String INSERTSQL = "insert into JunitTestResult (testcase, status, rundate, runid) values (?,?,?,?)";
	private final static String INSERTSUMMARYSQL = "INSERT INTO JunitRunSummary ( runid , status, testcase_cnt , failed_cnt , error_cnt , rundate) VALUES (?,?,?,?,?,?)";
	private final static String SELECTSUMMARYRESULT = "SELECT runid , status, testcase_cnt , failed_cnt , error_cnt FROM JunitRunSummary ORDER BY runid DESC";
	private final static String SELECTDETAILEDRESULT = "SELECT runid, testcase, status FROM JunitTestResult WHERE runid =?";
	

	private void init() throws Exception {
		hsqlServer = new Server();
		hsqlServer.setLogWriter(null);
		hsqlServer.setSilent(true);
		hsqlServer.setDatabaseName(0, "iva");
		hsqlServer.setDatabasePath(0, AppConfig.getProperty("databasePath"));
		// hsqlServer.setDatabasePath(0, "file:ivadb");
		hsqlServer.start();
		Class.forName("org.hsqldb.jdbcDriver");
		connection = DriverManager.getConnection(
				"jdbc:hsqldb:hsql://localhost/iva", "sa", ""); // can through
																// sql exception

	}

	public void save(List<DetailedResultDTO> resultLst, ResultSummaryDTO summary) throws SQLException
			 {
		String runId = String.valueOf(System.currentTimeMillis());
		java.sql.Date sqlDate = new java.sql.Date(
				new java.util.Date().getTime());

		try {
			pstm = connection.prepareStatement(INSERTSUMMARYSQL);

			pstm.setString(1, runId);
			pstm.setString(2, summary.getStatus());
			pstm.setInt(3, summary.getTestcaseCount());
			pstm.setInt(4, summary.getFailedCount());
			pstm.setInt(5, summary.getErrorCount());
			pstm.setDate(6, sqlDate);
			pstm.executeUpdate();

			pstm.close();

			pstm = connection.prepareStatement(INSERTSQL);

			for (DetailedResultDTO dto : resultLst) {
				pstm.setString(1, dto.getTestCase());
				pstm.setString(2, dto.getStatus());
				pstm.setDate(3, sqlDate);
				pstm.setString(4, runId);
				pstm.executeUpdate();
			}
			pstm.close();
//			testRetrieve();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			connection.close();
			hsqlServer.stop();
		}
		System.out.println("Data saved");
	}

	private void testRetrieve() throws SQLException {
		ResultSet rs = connection
				.prepareStatement(
						"select id, testcase, status,rundate, runid  from JunitTestResult;")
				.executeQuery();

		while (rs.next()) {
			System.out
					.println(String
							.format("ID: %1d, TestCase: %1s, Status: %1s, Rundate: %1s, RunId:%1s",
									rs.getInt(1), rs.getString(2),
									rs.getString(3), rs.getString(4),
									rs.getString(5)));
		}
		
		rs.close();
		rs = connection.prepareStatement("select runid , status, testcase_cnt , failed_cnt , error_cnt , rundate from JunitRunSummary").executeQuery();
		while (rs.next()) {
			System.out.println(String.format("RunID: %1s, Status: %1s, TestCaseCount: %1d, Failed Count: %1d, ErrorCount: %1d, Rundate: %1s", rs.getString(1),rs.getString(2), rs.getInt(3),rs.getInt(4),rs.getInt(5), rs.getString(6)));
		}
		rs.close();


	}
	
	public List<ResultSummaryDTO> fetchResult(int runCount)
	{
		List<ResultSummaryDTO> summaryDTOs = null;
		try {
			summaryDTOs = fetchSummaryOnly(runCount);
			List<DetailedResultDTO> detailedDTOs = null;
			for (ResultSummaryDTO summaryData : summaryDTOs)
			{
				detailedDTOs = fetchDetailed(summaryData.getRunid());
				summaryData.setDetailedResult(detailedDTOs);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			hsqlServer.stop();
		}
		return summaryDTOs;
	}

	private List<DetailedResultDTO> fetchDetailed(String runid) throws SQLException 
	{
		PreparedStatement pst = connection.prepareStatement(SELECTDETAILEDRESULT);
		pst.setString(1, runid);
		ResultSet rs = pst.executeQuery();
		List<DetailedResultDTO> detailedDTOs = new ArrayList<DetailedResultDTO>();
		DetailedResultDTO detailData = null;
		while (rs.next()) {
			detailData = new DetailedResultDTO(rs.getString(2), rs.getString(3));
			detailData.setRunId(rs.getString(1));
//			System.out.println(detailData);
			detailedDTOs.add(detailData);
		}
		return detailedDTOs;
	}

	private List<ResultSummaryDTO> fetchSummaryOnly(int runCount) throws SQLException {
		ResultSet rs = connection.prepareStatement(SELECTSUMMARYRESULT).executeQuery();
		List<ResultSummaryDTO> summaryDTOs = new ArrayList<ResultSummaryDTO>();
		ResultSummaryDTO summaryData = null;
		int i=0;
		while (rs.next() && i++ <runCount) {
			summaryData = new ResultSummaryDTO(rs.getString(1), rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getInt(5));
			summaryDTOs.add(summaryData);
		}
		return summaryDTOs;
	}

}
