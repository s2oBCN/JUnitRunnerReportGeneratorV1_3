package com.junit.tools.dbops;

import java.io.File;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hsqldb.Server;

import com.junit.tools.config.AppConfig;

public class DBSetup {

	
	private Server hsqlServer = null;
    private Connection connection = null;
    
    /**
     * Initialize the HSQL database and create db connection.
     * 
     * @throws Exception
     */
	private void init() throws Exception 
	{
		check();
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
    
    /**
     * This method will check if database already exist at given location.
     * 
     * @throws Exception
     */

    private void check()throws Exception 
    {
		File f = new File(AppConfig.getProperty("databasePath")+".lck");
		
		if(f.exists())
		{
			throw new Exception("Caution: Data already exist, this operation will delete all existing data. If sure, first empty the database path");
		}
	}

    /**
     * This method will refresh the database ie drop the existing tables if exist and create the required tables.
     * 
     */

	private void setup()
    {
    	try {
            connection.prepareStatement("drop table JunitTestResult if exists;").execute();
            connection.prepareStatement("create table JunitTestResult (id integer, testcase varchar(100) not null, status varchar(6), rundate date, runid varchar(50));").execute();
            connection.prepareStatement("drop table JunitRunSummary if exists;").execute();
            connection.prepareStatement("create table JunitRunSummary (id integer, runid varchar(50) not null, status varchar(6), testcase_cnt integer, failed_cnt integer, error_cnt integer, rundate date);").execute();

            System.out.println("DB setup done successfully");
        } catch (SQLException e2) {
            e2.printStackTrace();
            System.exit(-1);
            
        } finally {
            hsqlServer.stop();
            hsqlServer = null;    	
        }
        
    }
    
	public void invokeSetup()
	{
    	try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
    	setup();
	}
	
/*
	public static void main (String a[])
    {
    	DBSetup ds = new DBSetup();
    	try {
			ds.init();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
    	ds.setup();
    	
    }
*/    
}
