package xpo.qa.sc.wmx.tests;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import xpo.qa.common.databases.DatabaseUtil;
import xpo.qa.common.databases.oracle.OracleDB;
import xpo.qa.sc.omx.data.OmxData;
import xpo.qa.sc.wmx.utilities.WMXDataBaseUtils;

public class OMXProdToQaDataTransfer 
{
	static ResultSet rs1;
	PreparedStatement pstmt;
	public int count=0;
	public static String totalRows;
	static int result;
	public static Date minDateDB;
	public static Date maxDateDB;
	public static Date sysDate,DBsysDate;
	public static Date targetDate, DBtargetDate;
	
	
	WMXDataBaseUtils dbt= new WMXDataBaseUtils();
	
	public static Boolean copyDataRecordOMX() throws SQLException 
	{
		OmxData omxData= new OmxData();
		String sourceTableName=omxData.OMXSourceTableName;
		String targetTableName=omxData.OMXTargetTableName;
		
		WMXDataBaseUtils dbt= new WMXDataBaseUtils();
		try{
			
			// Create OMX QA connection below with server name, port, user name and password
			OracleDB db = new OracleDB(omxData.serverName_Prod_OMXDB, omxData.port_Prod_OMXDB, omxData.database_Prod_OMXDB,omxData.userid_Prod_OMXDB, omxData.password_Prod_OMXDB,true);
			
			//Query to fetch result from OMX QA mpaul.EXPORT_QUEUE_MERGE table and fetch result in Result set rs.
			
			String sqlSelectQuery ="select * from SCHEDULER.export_queue where addts <= (sysdate) and addts >= ((sysdate-1)-"+WMXProdToQADataTransfer.noOfDay+") AND STATUS = 'COMPLETE'";
			
			
			//This method create connection with Prod Database using server name, port, database name , username and password 
			dbt.connect(omxData.serverName_Prod_OMXDB, omxData.port_Prod_OMXDB, omxData.database_Prod_OMXDB,omxData.userid_Prod_OMXDB, omxData.password_Prod_OMXDB,true);
			
			//Execute query and save result in resultset
			rs1=dbt.executeSelectAndFetchResultset(sqlSelectQuery);
			
			
			// Create OMX QA connection below with server name, port, user name and password
			OracleDB db1 = new OracleDB(omxData.serverName_QA_OMXDB, omxData.port_QA_OMXDB, omxData.database_QA_OMXDB,omxData.userid_QA_OMXDB, omxData.password_QA_OMXDB,true);
			
			//This method create second connection with QA Database using server name, port, database name , username and password
			dbt.connect1(omxData.serverName_QA_OMXDB, omxData.port_QA_OMXDB, omxData.database_QA_OMXDB,omxData.userid_QA_OMXDB, omxData.password_QA_OMXDB,true);
					
			
			//Query to insert above result set data into QA mpaul.test table.Change QA table name in below query
			String sqlInsertQuery = "INSERT INTO "+targetTableName+" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					
			result=dbt.executeInsertCopyResultSetOMX(sqlInsertQuery,rs1);
			//Result set is inserted by no of Rows times and data is inserted into QA table row by row 
		
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		//Close both Production and QA connection
		dbt.conn.close();
		dbt.conn1.close();
		
		System.out.println("No of rows Added is="+result);
		return((rs1==null || result<=0) ? false : true );
		
	}
	
	public static Boolean copyGapDataRecordOMX() throws SQLException 
	{
		OmxData omxData= new OmxData();
		String sourceTableName=omxData.OMXSourceTableName;
		String targetTableName=omxData.OMXTargetTableName;
		
		WMXDataBaseUtils dbt= new WMXDataBaseUtils();
		try
		{
			
		  //Fetch current system date and calculate target date as no of days provided in omxData.properties
			
		  SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");  
		       
		  Calendar calendar = Calendar.getInstance();
		  sysDate=formatter.parse(formatter.format(calendar.getTime()));
		  calendar.add(Calendar.DATE, (-1));
		  DBsysDate = formatter.parse(formatter.format(calendar.getTime()));
		  System.out.println("system date="+formatter.format(DBsysDate));
		    
		  // Decrementing days by 2
		  calendar.setTime(DBsysDate);
		  calendar.add(Calendar.DATE, -(WMXProdToQADataTransfer.noOfDay-1));
		  targetDate=formatter.parse(formatter.format(calendar.getTime()));
		  System.out.println("Need to have data till Date = " + formatter.format(targetDate));
		  
		  
		  String sqlSelectQuery ="select min(addts), max(addts),count(*) from "+targetTableName+"";
			
		  // Create OMX QA connection below with server name, port, user name and password
		  OracleDB db1 = new OracleDB(omxData.serverName_QA_OMXDB, omxData.port_QA_OMXDB, omxData.database_QA_OMXDB,omxData.userid_QA_OMXDB, omxData.password_QA_OMXDB,true);
		 			
		  //This method create second connection with QA Database using server name, port, database name , username and password
		  dbt.connect(omxData.serverName_QA_OMXDB, omxData.port_QA_OMXDB, omxData.database_QA_OMXDB,omxData.userid_QA_OMXDB, omxData.password_QA_OMXDB,true); 
	      
		  //Execute query and save result in resultset
		  rs1=dbt.executeSelectAndFetchResultset(sqlSelectQuery);
			
		  while(rs1.next())
		  {
				minDateDB=rs1.getDate(1);
				maxDateDB=rs1.getDate(2);
				totalRows=rs1.getString(3);
				System.out.println("mindate="+minDateDB);
				System.out.println("maxdate="+maxDateDB);
				System.out.println("total="+totalRows);
		  }
		    
		  		  
		  //check if Max date in DB is greater than target date, if not then truncate full table and fetch fresh data as we don't have gap data.
		  
		 if( Integer.parseInt(totalRows)==0||maxDateDB.compareTo(targetDate)<0 || maxDateDB.compareTo(targetDate)==0)
			{
			System.out.println("There is no Gap data to download. truncate table and download fresh data");  
			String sqlDeleteQuery ="truncate table "+targetTableName+"";
			dbt.executeUpdate(sqlDeleteQuery);		
			copyDataRecordOMX();
			}
		  
		  else
		  {
		 	 
		  String deleteGapData="delete from "+targetTableName+" where trunc(addts) >= '"+formatter.format(minDateDB)+"' and  trunc(addts) <'"+formatter.format(targetDate)+"'";
		  System.out.println(deleteGapData);
		  dbt.executeUpdate(deleteGapData);
		  //Fetch Gap data from production. Max Date DB to system date data.
		  
		  // Create OMX QA connection below with server name, port, user name and password
		  OracleDB db = new OracleDB(omxData.serverName_Prod_OMXDB, omxData.port_Prod_OMXDB, omxData.database_Prod_OMXDB,omxData.userid_Prod_OMXDB, omxData.password_Prod_OMXDB,true);
			
		  //Query to fetch result from OMX QA mpaul.EXPORT_QUEUE_MERGE table and fetch result in Result set rs.
			
		  String sqlSelectGapDataQuery ="select * from "+sourceTableName+" where trunc(addts) > '"+formatter.format(maxDateDB)+"' and trunc(addts)< '"+formatter.format(DBsysDate)+"' AND STATUS = 'COMPLETE'";
		  System.out.println(sqlSelectGapDataQuery);
			
			
		  //This method create connection with Prod Database using server name, port, database name , username and password 
		  dbt.connect(omxData.serverName_Prod_OMXDB, omxData.port_Prod_OMXDB, omxData.database_Prod_OMXDB,omxData.userid_Prod_OMXDB, omxData.password_Prod_OMXDB,true);
			
		  //Execute query and save result in resultset
		  rs1=dbt.executeSelectAndFetchResultset(sqlSelectGapDataQuery);
			
				
		  // Create OMX QA connection below with server name, port, user name and password
			OracleDB db2 = new OracleDB(omxData.serverName_QA_OMXDB, omxData.port_QA_OMXDB, omxData.database_QA_OMXDB,omxData.userid_QA_OMXDB, omxData.password_QA_OMXDB,true);
			
			//This method create second connection with QA Database using server name, port, database name , username and password
			dbt.connect1(omxData.serverName_QA_OMXDB, omxData.port_QA_OMXDB, omxData.database_QA_OMXDB,omxData.userid_QA_OMXDB, omxData.password_QA_OMXDB,true);
					
			
			//Query to insert above result set data into QA mpaul.test table.Change QA table name in below query
			String sqlInsertQuery = "INSERT INTO "+targetTableName+" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					
			result=dbt.executeInsertCopyResultSetOMX(sqlInsertQuery,rs1);
			//Result set is inserted by no of Rows times and data is inserted into QA table row by row 
		
		}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		//Close both Production and QA connection
		dbt.conn.close();
		dbt.conn1.close();
		
		System.out.println("No of rows Added is="+result);
		return((rs1==null || result<=0) ? false : true );
		
	}

	public static void main(String args[])
	{
		OMXProdToQaDataTransfer omx= new OMXProdToQaDataTransfer();
		try {
			omx.copyGapDataRecordOMX();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}




}
