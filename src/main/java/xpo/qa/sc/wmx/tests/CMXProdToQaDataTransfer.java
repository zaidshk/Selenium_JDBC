package xpo.qa.sc.wmx.tests;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.sql2o.logging.SysOutLogger;
import oracle.sql.CLOB;
import xpo.qa.sc.omx.data.OmxData;
import xpo.qa.sc.wmx.data.CmxData;
import xpo.qa.sc.wmx.utilities.WMXDataBaseUtils;

import xpo.qa.common.databases.DatabaseUtil;
import xpo.qa.common.databases.oracle.OracleDB;
public class CMXProdToQaDataTransfer 
{
	static ResultSet rs1;
	static int result;
	public static Date minDateDB;
	public static Date maxDateDB;
	public static Date sysDate,DBsysDate;
	public static Date targetDate, DBtargetDate;
	public static String totalRows;
	
	WMXDataBaseUtils dbt= new WMXDataBaseUtils();
	public static boolean copyDataRecordCMX() throws SQLException  
	{	
		CmxData cmxData= new CmxData();
		String sourceTableName=cmxData.CMXSourceTableName;
		String targetTableName=cmxData.CMXTargetTableName;
		
		WMXDataBaseUtils dbt= new WMXDataBaseUtils();
		try
		{
			// Create CMX production connection below with server name, port, user name and password
			OracleDB db = new OracleDB(cmxData.serverNameProd_CMXDB, cmxData.portProd_CMXDB, cmxData.databaseProd_CMXDB,cmxData.useridProd_CMXDB, cmxData.passwordProd_CMXDB,true);
			
			//Query to fetch result from OMX prod Scmxdata.x_transmission table and fetch result in Result set rs.
			String sqlSelectQuery = "SELECT * FROM "+sourceTableName+" WHERE TRANSMISSION_TIME <= (sysdate) and TRANSMISSION_TIME >= ((sysdate-1)-"+WMXProdToQADataTransfer.noOfDay+") and STATUS = 'COMPLETED' AND DIRECTION ='OUTGOING' ";
			//String sqlSelectQuery ="SELECT * FROM cmxdata.x_transmission WHERE TRANSMISSION_TIME <= (sysdate-5) and TRANSMISSION_TIME >= ((sysdate-6)-10) and STATUS = 'COMPLETED' AND DIRECTION ='OUTGOING'";
			//This method create connection with Prod Database using server name, port, database name , username and password 
			dbt.connect(cmxData.serverNameProd_CMXDB, cmxData.portProd_CMXDB, cmxData.databaseProd_CMXDB,cmxData.useridProd_CMXDB, cmxData.passwordProd_CMXDB,true);
			
			//Execute query and save result in resultset
			rs1=dbt.executeSelectAndFetchResultset(sqlSelectQuery);
			
			// Create CMX QA connection below with server name, port, user name and password
			OracleDB db1 = new OracleDB(cmxData.serverNameQA_CMXDB, cmxData.portQA_CMXDB, cmxData.databaseQA_CMXDB,cmxData.useridQA_CMXDB, cmxData.passwordQA_CMXDB,true);
			
			//This method create second connection with QA Database using server name, port, database name , username and password
			dbt.connect1(cmxData.serverNameQA_CMXDB, cmxData.portQA_CMXDB, cmxData.databaseQA_CMXDB,cmxData.useridQA_CMXDB, cmxData.passwordQA_CMXDB,true);
			
			//Query to insert above result set data into QA mpaul.X_TRANSMISSION_TEMP_1.Change QA table name in below query.
			String sqlInsertQuery = "INSERT INTO "+targetTableName+" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			//Run insert query and get no of rows inserted
			result=dbt.executeInsertCopyResultSetCMX(sqlInsertQuery,rs1);
			 
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
	
	public static Boolean copyGapDataRecordCMX() throws SQLException 
	{
		CmxData cmxData= new CmxData();
		String sourceTableName=cmxData.CMXSourceTableName;
		String targetTableName=cmxData.CMXTargetTableName;
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
		  System.out.println(sqlSelectQuery);
			
		  // Create OMX QA connection below with server name, port, user name and password
		  
		  OracleDB db1 = new OracleDB(cmxData.serverNameQA_CMXDB, cmxData.portQA_CMXDB, cmxData.databaseQA_CMXDB,cmxData.useridQA_CMXDB, cmxData.passwordQA_CMXDB,true);
					
		  //This method create second connection with QA Database using server name, port, database name , username and password
		 dbt.connect(cmxData.serverNameQA_CMXDB, cmxData.portQA_CMXDB, cmxData.databaseQA_CMXDB,cmxData.useridQA_CMXDB, cmxData.passwordQA_CMXDB,true); 
	      
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
			copyDataRecordCMX();
			}
		  
		  else
		  {
		 	 
		  String deleteGapData="delete from "+targetTableName+" where trunc(TRANSMISSION_TIME) >= '"+formatter.format(minDateDB)+"' and  trunc(TRANSMISSION_TIME) <'"+formatter.format(targetDate)+"'";
		  System.out.println(deleteGapData);
		  dbt.executeUpdate(deleteGapData);
		  //Fetch Gap data from production. Max Date DB to system date data.
		  
		  // Create OMX QA connection below with server name, port, user name and password
		  OracleDB db = new OracleDB(cmxData.serverNameProd_CMXDB, cmxData.portProd_CMXDB, cmxData.databaseProd_CMXDB,cmxData.useridProd_CMXDB, cmxData.passwordProd_CMXDB,true);
			
		  //Query to fetch result from OMX QA mpaul.EXPORT_QUEUE_MERGE table and fetch result in Result set rs.
			
		  String sqlSelectGapDataQuery ="select * from "+sourceTableName+" where trunc(TRANSMISSION_TIME) >= '"+formatter.format(maxDateDB)+"' and trunc(TRANSMISSION_TIME)< '"+formatter.format(DBsysDate)+"' AND STATUS = 'COMPLETED' AND DIRECTION ='OUTGOING'";
		  System.out.println(sqlSelectGapDataQuery);
			
			
		  //This method create connection with Prod Database using server name, port, database name , username and password 
		   dbt.connect(cmxData.serverNameProd_CMXDB, cmxData.portProd_CMXDB, cmxData.databaseProd_CMXDB,cmxData.useridProd_CMXDB, cmxData.passwordProd_CMXDB,true);
			
		  //Execute query and save result in resultset
		  rs1=dbt.executeSelectAndFetchResultset(sqlSelectGapDataQuery);
			
		  		  				
		  // Create OMX QA connection below with server name, port, user name and password
			OracleDB db2 = new OracleDB(cmxData.serverNameQA_CMXDB, cmxData.portQA_CMXDB, cmxData.databaseQA_CMXDB,cmxData.useridQA_CMXDB, cmxData.passwordQA_CMXDB,true);
			
			//This method create second connection with QA Database using server name, port, database name , username and password
			dbt.connect1(cmxData.serverNameQA_CMXDB, cmxData.portQA_CMXDB, cmxData.databaseQA_CMXDB,cmxData.useridQA_CMXDB, cmxData.passwordQA_CMXDB,true);
					
			
			//Query to insert above result set data into QA mpaul.test table.Change QA table name in below query
			String sqlInsertQuery = "INSERT INTO "+targetTableName+" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			//Run insert query and get no of rows inserted
			result=dbt.executeInsertCopyResultSetCMX(sqlInsertQuery,rs1);
		
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
		CMXProdToQaDataTransfer cmx= new CMXProdToQaDataTransfer();
		try
		{
			cmx.copyGapDataRecordCMX();
		}
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}




}



