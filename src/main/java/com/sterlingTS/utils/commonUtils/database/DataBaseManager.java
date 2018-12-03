package com.sterlingTS.utils.commonUtils.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.sterlingTS.utils.commonUtils.Email;
import com.sterlingTS.utils.commonVariables.Globals;


public class DataBaseManager {
	
	protected static Logger log = Logger.getLogger(DataBaseManager.class);
	
	public PreparedStatement ps = 	null;
	public Statement stmt 		=  	null;
	public ResultSet rs 		= 	null;
	public static Connection conn = null;
	
	public DataBaseManager(String dbURL, String userName, String pwd)
	{
		try{
			openConnection(dbURL, userName, pwd);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public DataBaseManager(String dbURL)
	{
		try
		{
			openConnection(dbURL);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public  Connection openConnection(String dbURL, String userName, String pwd){
		try {
			if(dbURL!=null && userName!=null && pwd!=null)
			{
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				String pathdll = System.getProperty("user.dir")+File.separator+"jars"+File.separator+"FrameworkJar"+File.separator+"sqljdbc_auth.dll";
				System.setProperty("java.library.path", pathdll);
				log.debug("......Connecting to Database-"+dbURL);
				conn = DriverManager.getConnection(dbURL, userName, pwd);
			}
			
		
		} catch (Exception e) {
			
			e.printStackTrace();
			log.info("Database Connection-Unable to establish connection.Exception: - "+ e.toString());
			if (Globals.SendEmails.equalsIgnoreCase("true")) {
				String toList = Globals.StatusEmailToList+","+Globals.fromEmailID;
				String ccList = Globals.StatusEmailCCList;
				String subject = "***Selenium Execution => " + dbURL +" DB Connectivity Issue *****";
				String msgBody = "Unable to connect to the database..Exception - " + e.toString() + "<br><br><br>";
				Email.send(toList, ccList, subject, "html", msgBody);
			}
			
			
			conn = null;
		}
		
		return conn;
	}
	
	public  Connection openConnection(String dbURL){
		try {
			
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				String pathdll = System.getProperty("user.dir")+File.separator+"jars"+File.separator+"FrameworkJar"+File.separator+"sqljdbc_auth.dll";
				System.setProperty("java.library.path", pathdll);
				log.debug("......Connecting to Database-"+dbURL);
				conn = DriverManager.getConnection(dbURL);
		
		} catch (Exception e) {
			
			e.printStackTrace();
			log.info("Database Connection-Unable to establish connection.Exception: - "+ e.toString());
			if (Globals.SendEmails.equalsIgnoreCase("true")) {
				String toList = Globals.StatusEmailToList+","+Globals.fromEmailID;
				String ccList = Globals.StatusEmailCCList;
				String subject = "***Selenium Execution => " + dbURL +" DB Connectivity Issue *****";
				String msgBody = "Unable to connect to the database..Exception - " + e.toString() + "<br><br><br>";
				Email.send(toList, ccList, subject, "html", msgBody);
			}
			
			
			conn = null;
		}
		
		return conn;
	}
	
	public Object[][] executeSelectQuery(String query) throws SQLException
	{
		ResultSetMetaData rsMetaData = null;
		Object[][] finalResult = null;
		try
		{
			stmt = getConn().createStatement();
			rs = stmt.executeQuery(query);
			rsMetaData = rs.getMetaData();
			int columnCount = rsMetaData.getColumnCount();
			ArrayList<Object[]> data = new ArrayList<Object[]>();
			Object[] header = new Object[columnCount];
			for(int i=1;i<=columnCount;i++)
			{
				Object label =rsMetaData.getColumnLabel(i);
				header[i-1]=label;
			}
			while(rs.next()){
				Object[] str = new Object[columnCount];
				for(int i=1;i<=columnCount;i++)
				{
					Object obj;
					obj = rs.getObject(i);
					str[i-1]=obj;
				}
				data.add(str);
			}
			int resultSetLength = data.size();
			finalResult = new Object[resultSetLength][columnCount];
			for(int i=0;i<resultSetLength;i++)
			{
				Object[] row = data.get(i);
				finalResult[i] = row;  
			}
		}catch(Exception ie)
		{
			log.error("Getting Exception While Execution of Query :"+"\t"+ie.toString());
			ie.printStackTrace();
			if (Globals.SendEmails.equalsIgnoreCase("true")) {
				String toList = Globals.StatusEmailToList+","+Globals.fromEmailID;
				String ccList = Globals.StatusEmailCCList;
				String subject = "***Selenium Execution => DB Query Execution Issue *****";
				String msgBody = "Unable to connect to the database..Exception - " + ie.toString() + "<br><br><br>";
				Email.send(toList, ccList, subject, "html", msgBody);
			}
		}
		return finalResult;
	}
	
	public int executeUpdateQuery(String query) throws SQLException
	{
		stmt = getConn().createStatement();
		int result = stmt.executeUpdate(query);
		
		if(result>=0)
		{
			log.info("Upade Query has been updated "+ result +" Rows");
		}else
		{
			log.info("Error has been occured during Update query Execution");
		}
		return result;
	}
	
	public void closeConnection(){
		try {
	        if (getConn() != null && !getConn().isClosed()) {
	        	getConn().close();
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        log.info("Automation DB Connection-Unable to close connection.Exception: - "+ ex.toString());
	    }
	}


	public static Connection getConn() {
		return conn;
	}

	public static void setConn(Connection conn) {
		DataBaseManager.conn = conn;
	}
	

}
