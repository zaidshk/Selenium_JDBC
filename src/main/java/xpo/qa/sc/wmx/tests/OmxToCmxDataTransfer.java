package xpo.qa.sc.wmx.tests;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import org.sql2o.logging.SysOutLogger;
import oracle.sql.CLOB;
import xpo.qa.sc.omx.data.OmxData;
import xpo.qa.sc.wmx.data.CmxData;

import xpo.qa.sc.wmx.utilities.WMXDataBaseUtils;

import xpo.qa.common.databases.DatabaseUtil;
import xpo.qa.common.databases.oracle.OracleDB;

public class OmxToCmxDataTransfer
{
	static ResultSet rs1;
	static int result;
	
	static WMXDataBaseUtils dbt= new WMXDataBaseUtils();

	public static boolean copyDataRecordOmxToCmx() throws SQLException  {
		OmxData omxt= new OmxData();
		CmxData cmxt= new CmxData();

		String sourceTableName=omxt.OmxToCmxSourceTable;
		String targetTableName=cmxt.OmxToCmxTargetTable;
		
		try {
			
			// Create OMX QA connection below with server name, port, user name and password
			OracleDB db = new OracleDB(omxt.serverName_QA_OMXDB, omxt.port_QA_OMXDB, omxt.database_QA_OMXDB,omxt.userid_QA_OMXDB, omxt.password_QA_OMXDB,true);
			
			//Query to fetch result from OMX QA mpaul.EXPORT_QUEUE_MERGE table and fetch result in Result set rs.
			String sqlSelectQuery = "select * from "+sourceTableName+"";
			
			//This method create connection with Database using server name, port, database name , username and password 
			dbt.connect(omxt.serverName_QA_OMXDB, omxt.port_QA_OMXDB, omxt.database_QA_OMXDB,omxt.userid_QA_OMXDB, omxt.password_QA_OMXDB,true);
			
			//Execute query and save result in resultset
			rs1=dbt.executeSelectAndFetchResultset(sqlSelectQuery);
			
			
			// Create CMX QA connection below with server name, port, user name and password
			OracleDB db1 = new OracleDB(cmxt.serverNameQA_CMXDB, cmxt.portQA_CMXDB, cmxt.databaseQA_CMXDB,cmxt.useridQA_CMXDB, cmxt.passwordQA_CMXDB,true);
			
			//This method create second connection with QA Database using server name, port, database name , username and password
			dbt.connect1(cmxt.serverNameQA_CMXDB, cmxt.portQA_CMXDB, cmxt.databaseQA_CMXDB,cmxt.useridQA_CMXDB, cmxt.passwordQA_CMXDB,true);

			//Query to insert above result set data into QA mpaul.EXPORT_QUEUE_MERGE_TEMP.Change QA table name in below query.
			String sqlInsertQuery = "INSERT INTO "+targetTableName+" VALUES (?,?,?,?,?,?,?,?,?)";

			//Run insert query and get no of rows inserted
			result=dbt.executeInsertCopyResultSetOMXToCMX(sqlInsertQuery,rs1);
			
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
		OmxToCmxDataTransfer omx= new OmxToCmxDataTransfer();
		try {
			omx.copyDataRecordOmxToCmx();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}




}


