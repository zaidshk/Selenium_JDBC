package xpo.qa.sc.wmx.utilities;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sql2o.logging.SysOutLogger;

//import xpo.qa.common.databases.ConnectionException;
import xpo.qa.common.databases.DatabaseUtil;
import xpo.qa.sc.wmx.data.CmxData;

public class WMXDBUtils
{
	public Statement stmt = null;
	public Statement stmt1 = null;
	public PreparedStatement pstmt = null;
	public ResultSet rs ;
	Boolean serviceName=true;
	public Connection conn1,conn;
	CmxData cmxData= new CmxData();
	
	
	/**
	 * This function establishes the connection with DB with the details provided as parameters.
	 * 
	 * @throws ConnectionException
	 */
	public void connect1(String serverName, String portNumber,String databaseName,String userName,String password) 
//			throws ConnectionException 
	{
		String url = "";
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			if (serviceName) 
			{
				url = "jdbc:oracle:thin:@" + serverName + ":" + portNumber + ":" + databaseName;
			}
			else 
			{
				url = "jdbc:oracle:thin:@//" + serverName + ":" + portNumber + "/" + databaseName;
			}
			conn1 = java.sql.DriverManager.getConnection(url, userName, password);
		}
		catch(Exception e) 
		{
			e.printStackTrace();
//			throw new ConnectionException(e.getMessage());
		}
	}
	/**
	 * @param sql
	 * @param rs
	 * @return
	 * @throws SQLException
	 * This function insert data from OMX test table(mpaul.EXPORT_QUEUE_MERGE) to CMX test table(mpaul.EXPORT_QUEUE_MERGE)
	 */
	public int executeInsertCopyResultSetOMXToCMX(String sql,ResultSet rs) throws SQLException 
	{		
		int count=0;	
		try 
		{
			pstmt=conn1.prepareStatement(sql);
			while(rs.next())
			{
				pstmt.setInt(1,rs.getInt(1));
				pstmt.setInt(2,rs.getInt(2));
				pstmt.setString(3,rs.getString(3));
				pstmt.setString(4,rs.getString(4));
				pstmt.setString(5,rs.getString(5));
				pstmt.setString(6,rs.getString(6));
				pstmt.setString(7,rs.getString(7));
				pstmt.setString(8,rs.getString(8));
				pstmt.setString(9,rs.getString(9));
				pstmt.executeUpdate();
				count++;
			}
			return count;
		} 
		catch (Exception e) 
		{
			throw new SQLException(e.getMessage());
		} /*finally {
				if (stmt != null) {
					stmt.close();
				}
				close();
			}*/

	}

	/**
	 * @param sql
	 * @param rs
	 * @return
	 * @throws SQLException
	 * This function perform insert operation from CMX production table(cmxdata.x_transmission) to CMX QA table (mpaul.EXPORT_QUEUE_MERGE)
	 */
	public int executeInsertCopyResultSetCMX(String sql,ResultSet rs) throws SQLException 
	{
		int count=0;	
		try 
		{
			pstmt=conn1.prepareStatement(sql);
			while(rs.next())
			{
				pstmt.setInt(1,rs.getInt(1)); 
				pstmt.setTimestamp(2,rs.getTimestamp(2));
				pstmt.setString(3,rs.getString(3));
				pstmt.setString(4,rs.getString(4));
				pstmt.setString(5,rs.getString(5));
				pstmt.setString(6,rs.getString(6));
				pstmt.setString(7,rs.getString(7));
				pstmt.setString(8,rs.getString(8));
				pstmt.setString(9,rs.getString(9));
				pstmt.setString(10,rs.getString(10));
				pstmt.setString(11,rs.getString(11));
				pstmt.setString(12,rs.getString(12));
				pstmt.setString(13,rs.getString(13));
				pstmt.setString(14,rs.getString(14));
				pstmt.setString(15,rs.getString(15));
				pstmt.setInt(16,rs.getInt(16));
				pstmt.setInt(17,rs.getInt(17));
				pstmt.setString(18,rs.getString(18));
				pstmt.setString(19,rs.getString(19));
				pstmt.setTimestamp(20,rs.getTimestamp(20));	
				pstmt.setString(21,rs.getString(21));
				pstmt.setTimestamp(22,rs.getTimestamp(22));	
				pstmt.setString(23,rs.getString(23));
				pstmt.setTimestamp(24,rs.getTimestamp(24));	
				pstmt.setString(25,rs.getString(25));
				pstmt.setString(26,rs.getString(26));
				pstmt.setString(27,rs.getString(27));
				pstmt.setString(28,rs.getString(28));
				pstmt.setString(29,rs.getString(29));
				pstmt.setString(30,rs.getString(30));	
				pstmt.setDate(31,rs.getDate(31));	
				pstmt.setInt(32,rs.getInt(32));
				pstmt.setInt(33,rs.getInt(33));
				pstmt.setInt(34,rs.getInt(34));
				pstmt.setInt(35,rs.getInt(35));	
				pstmt.setString(36,rs.getString(36));	
				pstmt.setDate(37,rs.getDate(37));	
				pstmt.setString(38,rs.getString(38));
				pstmt.setString(39,rs.getString(39));
				pstmt.setString(40,rs.getString(40));
				pstmt.setString(41,rs.getString(41));
				pstmt.setString(42,rs.getString(42));
				pstmt.setString(43,rs.getString(43));
				pstmt.setString(44,rs.getString(44));
				pstmt.setString(45,rs.getString(45));
				pstmt.setString(46,rs.getString(46));
				pstmt.setString(47,rs.getString(47));
				pstmt.executeUpdate();
				count++;
			}
			return count;
		} 
		catch (Exception e) 
		{
			throw new SQLException(e.getMessage());
		} finally {
				conn1.commit();
			}

	}

	/**
	 * @param sql
	 * @return
	 * @throws SQLException
	 * This function execute select query and return result set.
	 */
	public ResultSet executeSelectAndFetchResultset(String sql) throws SQLException 
	{
		try
		{
			stmt=conn.createStatement();
			rs=stmt.executeQuery(sql);
//			return rs;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
//			throw new SQLException(e.getMessage());
		} /*finally {
			if (stmt != null) {
				stmt.close();
			}
			close();
		}*/
		return rs;
	}
	/**
	 * @param serverName
	 * @param portNumber
	 * @param databaseName
	 * @param userName
	 * @param password
	 * @throws ConnectionException
	 * This function establishes connection to database with details provided as parameters.
	 */
	public void connect(String serverName, String portNumber,String databaseName,String userName,String password) 
//			throws ConnectionException 
	{
		String url = "";
		try 
		{
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			if (serviceName) 
			{
				url = "jdbc:oracle:thin:@" + serverName + ":" + portNumber + ":" + databaseName;
			}
			else 
			{
				url = "jdbc:oracle:thin:@//" + serverName + ":" + portNumber + "/" + databaseName;
			}
			conn = java.sql.DriverManager.getConnection(url, userName, password);
			System.out.println("Connection is established");
		}
		catch(Exception e) 
		{
			e.printStackTrace();
//			throw new ConnectionException(e.getMessage());
		}
	}

	/**
	 * @param sql
	 * @param rs
	 * @return
	 * @throws SQLException
	 * *This function perform insert operation from OMX production table(SCHEDULER.export_queue) to OMX QA table (mpaul.Export_queue_temp)
	 */
	public int executeInsertCopyResultSetOMX(String sql,ResultSet rs) throws SQLException 
	{
		int count=0;	
		try 
		{
			pstmt=conn1.prepareStatement(sql);
			while(rs.next())
			{
				pstmt.setInt(1,rs.getInt(1));
				pstmt.setString(2,rs.getString(2));
				pstmt.setString(3,rs.getString(3));
				pstmt.setString(4,rs.getString(4));
				pstmt.setString(5,rs.getString(5));
				pstmt.setString(6,rs.getString(6));
				pstmt.setString(7,rs.getString(7));
				pstmt.setString(8,rs.getString(8));
				pstmt.setString(9,rs.getString(9));
				pstmt.setTimestamp(10,rs.getTimestamp(10));
				pstmt.setTimestamp(11,rs.getTimestamp(11));
				pstmt.setTimestamp(12,rs.getTimestamp(12));
				pstmt.setString(13,rs.getString(13));
				pstmt.setString(14,rs.getString(14));
				pstmt.setString(15,rs.getString(15));
				pstmt.setString(16,rs.getString(16));
				pstmt.setString(17,rs.getString(17));
				pstmt.setString(18,rs.getString(18));
				pstmt.setString(19,rs.getString(19));
				pstmt.setTimestamp(20,rs.getTimestamp(20));
				pstmt.setInt(21,rs.getInt(21));
				pstmt.setInt(22,rs.getInt(22));
				pstmt.setInt(23,rs.getInt(23));
				pstmt.setInt(24,rs.getInt(24));
				pstmt.setString(25,rs.getString(25));
				pstmt.setTimestamp(26,rs.getTimestamp(26));
				pstmt.setString(27,rs.getString(27));
				pstmt.setTimestamp(28,rs.getTimestamp(28));
				pstmt.setString(29,rs.getString(29));
				pstmt.setInt(30,rs.getInt(30));
				pstmt.setInt(31,rs.getInt(31));
				pstmt.setString(32,rs.getString(32));
				pstmt.setString(33,rs.getString(33));
				pstmt.setString(34,rs.getString(34));
				pstmt.setString(35,rs.getString(35));
				pstmt.setString(36,rs.getString(36));
				pstmt.setString(37,rs.getString(37));
				pstmt.setString(38,rs.getString(38));
				pstmt.setString(39,rs.getString(39));
				pstmt.setString(40,rs.getString(40));
				pstmt.setString(41,rs.getString(41));
				pstmt.setString(42,rs.getString(42));
				pstmt.executeUpdate();
				count++;
			}
			return count;
		} 
		catch (Exception e) 
		{
			throw new SQLException(e.getMessage());
		} finally {
				conn1.commit();
			}

	}

	/**
	 * @throws SQLException
	 * This function open connection with required database and table.This function is called in before test method
	 * and open the connection only once before test started.
	 */
//	public void createConnection() throws SQLException
//	{
//		try 
//		{
//			connect(cmxData.serverNameQA_CMXDB, cmxData.portQA_CMXDB, cmxData.databaseQA_CMXDB,cmxData.useridQA_CMXDB, cmxData.passwordQA_CMXDB);
//			stmt = conn.createStatement();
//			
//		}
//		catch (Exception e) 
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	
//	}

	/**
	 * This method is to close database connection. Method is called in after test and hence executed only once when
	 * all test cases done with execution.
	 */
	public void closeConnection() 
	{
		try
		{
			//conn.commit();
			conn.close();
		}
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * @param sql
	 * @return
	 * @throws ConnectionException
	 * @throws SQLException
	 * This method run select query and fetch result in List.
	 */
//	public List<Map<String, String>> selectSQL(String sql) throws ConnectionException, SQLException 
	public List<Map<String, String>> selectSQL(String sql) throws SQLException{
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		List<String> columns = new ArrayList<String>();
		ResultSet rs1= null;

		try 
		{
			rs1 = stmt.executeQuery(sql);
			if (rs1 != null) 
			{
				// Get the column names so we can use those to build mapped data
				ResultSetMetaData metaData = rs1.getMetaData();
				for (int i = 1; i <= metaData.getColumnCount(); i++) 
				{
					columns.add(metaData.getColumnName(i));
				}

				// For each of the result rows add a new map with the data
				while (rs1.next()) 
				{
					Map<String, String> row = new HashMap<String, String>();
					for (String column : columns) 
					{
						String val = rs1.getString(column);
						if (val == null) 
						{
							val = "null";
						}
						row.put(column, val);
					}
					data.add(row);
				}
			}
		} 
		catch (Exception e) 
		{
			throw e;
		}
		return data;
	}

	/**
	 * @param sql
	 * @throws Exception
	 * This method executes the update query.
	 */
	public void executeUpdate(String sql) throws Exception 
	{
		try 
		{
			stmt.executeUpdate(sql);
		}
		catch (Exception e) 
		{
			throw e;
		} 
	}
	
	public void executeUpdateStatusMAskedData(String sql,String status,String qaData,String prodData,String id) throws Exception 
	{
		
		try 
		{
				pstmt=conn.prepareStatement(sql);
				pstmt.setString(1,status);
				pstmt.setString(2,qaData);
				pstmt.setString(3,prodData);
				pstmt.setString(4,id);
				pstmt.executeUpdate();	
				pstmt.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}

}
	public void executeUpdateNoMaskStatus(String sql,String trans_type, String receiverId) throws Exception 
	{
		
		try 
		{
				pstmt=conn.prepareStatement(sql);
				pstmt.setString(1,trans_type);
				pstmt.setString(2,receiverId);
				
				pstmt.executeUpdate();	
				pstmt.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}

}	
	
	
	//Method to execute Loreal CMX inbound import
	public void  runCMxInboundImportLorealJob() {
//		String lorealInboundJobQuery=util.getLorealCMxInboundImport();
//		boolean jobSucess=false;
		try {
			 String url = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(COMMUNITY=tcp.world)(PROTOCOL=TCP)(HOST=omxt001ip.newbreed.com)(PORT=36001))(CONNECT_DATA=(SID=OMXT001)))";
			 String username="mpaul";
			 String password="Scitqa1";
			 Connection con=null;
			 
			 
			 con=DriverManager.getConnection(url, username, password);
			 
			 con.setAutoCommit(false);
			 
			 
			 Statement st=con.createStatement();
			 System.out.println("Connection");
			 
//			 
			 
			 
			 String q="declare\r\n" + 
			 		"    p_queue_id scheduler.job_queue.queue_id%TYPE;\r\n" + 
			 		"begin\r\n" + 
			 		"        scheduler.Job.enqueue_job_on_demand( \r\n" + 
			 		"            16014, --from select in for loop   \r\n" + 
			 		"            'AUTOMATION_USER', \r\n" + 
			 		"            p_queue_id \r\n" + 
			 		"        );\r\n" + 
			 		"        dbms_output.put_line(p_queue_id);\r\n" + 
			 		"end;\r";
			 
			 
//			 ResultSet rs=st.exec
			 
//			 ResultSet rs=st.executeQuery(lorealInboundJobQuery);
			 
		st.executeUpdate(q);	 
//			 ResultSet rs=st.exe
			 
			 con.commit();
			 System.out.println("Commited");
			 
			 con.close();
			 System.out.println("Closed");
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	} 
	
	
	
	//Method to execute Loreal Inbound Asn import
	
	public void  runInboundAsnImportLorealJob() {

		try {
			 String url = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(COMMUNITY=tcp.world)(PROTOCOL=TCP)(HOST=omxt001ip.newbreed.com)(PORT=36001))(CONNECT_DATA=(SID=OMXT001)))";
			 String username="mpaul";
			 String password="Scitqa1";
			 Connection con=null;
			 
			 
			 con=DriverManager.getConnection(url, username, password);
			 
			 con.setAutoCommit(false);
			 
			 
			 Statement st=con.createStatement();
			 System.out.println("Connection");
			 

			 
			 
			 String q="declare\r\n" + 
			 		"    p_queue_id scheduler.job_queue.queue_id%TYPE;\r\n" + 
			 		"begin\r\n" + 
			 		"        scheduler.Job.enqueue_job_on_demand( \r\n" + 
			 		"            16003, --from select in for loop   \r\n" + 
			 		"            'AUTOMATION_USER', \r\n" + 
			 		"            p_queue_id \r\n" + 
			 		"        );\r\n" + 
			 		"        dbms_output.put_line(p_queue_id);\r\n" + 
			 		"end;\r";
			 
			 
			 
			 st.executeUpdate(q);	 

			 
			 con.commit();
			 System.out.println("Commited");
			 
			 con.close();
			 System.out.println("Closed");
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	} 
	
	
//	Method to execute Loreal Inbound receipt confirmation message
	
	
	public void  runInboundReceiptConfirmationLorealJob() {

		try {
			 String url = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(COMMUNITY=tcp.world)(PROTOCOL=TCP)(HOST=omxt001ip.newbreed.com)(PORT=36001))(CONNECT_DATA=(SID=OMXT001)))";
			 String username="mpaul";
			 String password="Scitqa1";
			 Connection con=null;
			 
			 
			 con=DriverManager.getConnection(url, username, password);
			 
			 con.setAutoCommit(false);
			 
			 
			 Statement st=con.createStatement();
			 System.out.println("Connection");
			 

			 
			 
			 String q="declare\r\n" + 
			 		"    p_queue_id scheduler.job_queue.queue_id%TYPE;\r\n" + 
			 		"begin\r\n" + 
			 		"        scheduler.Job.enqueue_job_on_demand( \r\n" + 
			 		"            16001, --from select in for loop   \r\n" + 
			 		"            'AUTOMATION_USER', \r\n" + 
			 		"            p_queue_id \r\n" + 
			 		"        );\r\n" + 
			 		"        dbms_output.put_line(p_queue_id);\r\n" + 
			 		"end;\r";
			 
			 
			 
			 st.executeUpdate(q);	 

			 
			 con.commit();
			 System.out.println("Commited");
			 
			 con.close();
			 System.out.println("Closed");
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	} 

	
	//Method to execute CMx Outbound Receipt Confirmation
	
	
	public void  runCmxOutboundReceiptConfirmationLorealJob() {

		try {
			 String url = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(COMMUNITY=tcp.world)(PROTOCOL=TCP)(HOST=omxt001ip.newbreed.com)(PORT=36001))(CONNECT_DATA=(SID=OMXT001)))";
			 String username="mpaul";
			 String password="Scitqa1";
			 Connection con=null;
			 
			 
			 con=DriverManager.getConnection(url, username, password);
			 
			 con.setAutoCommit(false);
			 
			 
			 Statement st=con.createStatement();
			 System.out.println("Connection");
			 

			 
			 
			 String q="declare\r\n" + 
			 		"    p_queue_id scheduler.job_queue.queue_id%TYPE;\r\n" + 
			 		"begin\r\n" + 
			 		"        scheduler.Job.enqueue_job_on_demand( \r\n" + 
			 		"            98002, --from select in for loop   \r\n" + 
			 		"            'AUTOMATION_USER', \r\n" + 
			 		"            p_queue_id \r\n" + 
			 		"        );\r\n" + 
			 		"        dbms_output.put_line(p_queue_id);\r\n" + 
			 		"end;\r";
			 
			 
			 
			 st.executeUpdate(q);	 

			 
			 con.commit();
			 System.out.println("Commited");
			 
			 con.close();
			 System.out.println("Closed");
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}

	
	
	
	// Method to execute  Outbound Order Import
	
	
	
	public void  runLorealOutboundOrderImportJob() {

		try {
			 String url = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(COMMUNITY=tcp.world)(PROTOCOL=TCP)(HOST=omxt001ip.newbreed.com)(PORT=36001))(CONNECT_DATA=(SID=OMXT001)))";
			 String username="mpaul";
			 String password="Scitqa1";
			 Connection con=null;
			 
			 
			 con=DriverManager.getConnection(url, username, password);
			 
			 con.setAutoCommit(false);
			 
			 
			 Statement st=con.createStatement();
			 System.out.println("Connection");
			 

			 
			 
			 String q="declare\r\n" + 
			 		"    p_queue_id scheduler.job_queue.queue_id%TYPE;\r\n" + 
			 		"begin\r\n" + 
			 		"        scheduler.Job.enqueue_job_on_demand( \r\n" + 
			 		"            16005, --from select in for loop   \r\n" + 
			 		"            'AUTOMATION_USER', \r\n" + 
			 		"            p_queue_id \r\n" + 
			 		"        );\r\n" + 
			 		"        dbms_output.put_line(p_queue_id);\r\n" + 
			 		"end;\r";
			 
			 
			 
			 st.executeUpdate(q);	 

			 
			 con.commit();
			 System.out.println("Commited");
			 
			 con.close();
			 System.out.println("Closed");
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	
	//	Method to execute BOP job
	public void  runLorealBOPJob() {

		try {
			 String url = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(COMMUNITY=tcp.world)(PROTOCOL=TCP)(HOST=omxt001ip.newbreed.com)(PORT=36001))(CONNECT_DATA=(SID=OMXT001)))";
			 String username="mpaul";
			 String password="Scitqa1";
			 Connection con=null;
			 
			 
			 con=DriverManager.getConnection(url, username, password);
			 
			 con.setAutoCommit(false);
			 
			 
			 Statement st=con.createStatement();
			 System.out.println("Connection");
			 

			 
			 
			 String q="declare\r\n" + 
			 		"    p_queue_id scheduler.job_queue.queue_id%TYPE;\r\n" + 
			 		"begin\r\n" + 
			 		"        scheduler.Job.enqueue_job_on_demand( \r\n" + 
			 		"            16006, --from select in for loop   \r\n" + 
			 		"            'AUTOMATION_USER', \r\n" + 
			 		"            p_queue_id \r\n" + 
			 		"        );\r\n" + 
			 		"        dbms_output.put_line(p_queue_id);\r\n" + 
			 		"end;\r";
			 
			 
			 
			 st.executeUpdate(q);	 

			 
			 con.commit();
			 System.out.println("Commited");
			 
			 con.close();
			 System.out.println("Closed");
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	
	
	//Method to execute Loreal Order import
	
		public void  runOrderImportLorealJob() {

			try {
				 String url = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(COMMUNITY=tcp.world)(PROTOCOL=TCP)(HOST=omxt001ip.newbreed.com)(PORT=36001))(CONNECT_DATA=(SID=OMXT001)))";
				 String username="mpaul";
				 String password="Scitqa1";
				 Connection con=null;
				 
				 
				 con=DriverManager.getConnection(url, username, password);
				 
				 con.setAutoCommit(false);
				 
				 
				 Statement st=con.createStatement();
				 System.out.println("Connection");
				 

				 
				 
				 String q="declare\r\n" + 
				 		"    p_queue_id scheduler.job_queue.queue_id%TYPE;\r\n" + 
				 		"begin\r\n" + 
				 		"        scheduler.Job.enqueue_job_on_demand( \r\n" + 
				 		"            16005, --from select in for loop   \r\n" + 
				 		"            'AUTOMATION_USER', \r\n" + 
				 		"            p_queue_id \r\n" + 
				 		"        );\r\n" + 
				 		"        dbms_output.put_line(p_queue_id);\r\n" + 
				 		"end;\r";
				 
				 
				 
				 st.executeUpdate(q);	 

				 
				 con.commit();
				 System.out.println("Commited");
				 
				 con.close();
				 System.out.println("Closed");
				
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			
			
		} 
	
		//Method to execute Loreal BOP
		
			public void  runBOPLorealJob() {

				try {
					 String url = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(COMMUNITY=tcp.world)(PROTOCOL=TCP)(HOST=omxt001ip.newbreed.com)(PORT=36001))(CONNECT_DATA=(SID=OMXT001)))";
					 String username="mpaul";
					 String password="Scitqa1";
					 Connection con=null;
					 
					 
					 con=DriverManager.getConnection(url, username, password);
					 
					 con.setAutoCommit(false);
					 
					 
					 Statement st=con.createStatement();
					 System.out.println("Connection");
					 

					 
					 
					 String q="declare\r\n" + 
					 		"    p_queue_id scheduler.job_queue.queue_id%TYPE;\r\n" + 
					 		"begin\r\n" + 
					 		"        scheduler.Job.enqueue_job_on_demand( \r\n" + 
					 		"            16006, --from select in for loop   \r\n" + 
					 		"            'AUTOMATION_USER', \r\n" + 
					 		"            p_queue_id \r\n" + 
					 		"        );\r\n" + 
					 		"        dbms_output.put_line(p_queue_id);\r\n" + 
					 		"end;\r";
					 
					 
					 
					 st.executeUpdate(q);	 

					 
					 con.commit();
					 System.out.println("Commited");
					 
					 con.close();
					 System.out.println("Closed");
					
				}catch (Exception e) {
					e.printStackTrace();
				}
				
				
				
			} 
		
	
			//Method to execute Laerdal CMX inbound import
			public void  runCMxInboundImportLaerdalJob() {
//				String lorealInboundJobQuery=util.getLorealCMxInboundImport();
//				boolean jobSucess=false;
				try {
					 String url = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(COMMUNITY=tcp.world)(PROTOCOL=TCP)(HOST=omxt001ip.newbreed.com)(PORT=36001))(CONNECT_DATA=(SID=OMXT001)))";
					 String username="mpaul";
					 String password="Scitqa1";
					 Connection con=null;
					 
					 
					 con=DriverManager.getConnection(url, username, password);
					 
					 con.setAutoCommit(false);
					 
					 
					 Statement st=con.createStatement();
					 System.out.println("Connection");
					 
//					 
					 
					 
					 String q="declare\r\n" + 
					 		"    p_queue_id scheduler.job_queue.queue_id%TYPE;\r\n" + 
					 		"begin\r\n" + 
					 		"        scheduler.Job.enqueue_job_on_demand( \r\n" + 
					 		"            9100, --from select in for loop   \r\n" + 
					 		"            'AUTOMATION_USER', \r\n" + 
					 		"            p_queue_id \r\n" + 
					 		"        );\r\n" + 
					 		"        dbms_output.put_line(p_queue_id);\r\n" + 
					 		"end;\r";
					 
					 
//					 ResultSet rs=st.exec
					 
//					 ResultSet rs=st.executeQuery(lorealInboundJobQuery);
					 
				st.executeUpdate(q);	 
//					 ResultSet rs=st.exe
					 
					 con.commit();
					 System.out.println("Commited");
					 
					 con.close();
					 System.out.println("Closed");
					
				}catch (Exception e) {
					e.printStackTrace();
				}
				
				
				
			} 
	
			
			//Method to execute Laerdal Inbound Asn import
			
			public void  runInboundAsnImportLaerdalJob() {

				try {
					 String url = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(COMMUNITY=tcp.world)(PROTOCOL=TCP)(HOST=omxt001ip.newbreed.com)(PORT=36001))(CONNECT_DATA=(SID=OMXT001)))";
					 String username="mpaul";
					 String password="Scitqa1";
					 Connection con=null;
					 
					 
					 con=DriverManager.getConnection(url, username, password);
					 
					 con.setAutoCommit(false);
					 
					 
					 Statement st=con.createStatement();
					 System.out.println("Connection");
					 

					 
					 
					 String q="declare\r\n" + 
					 		"    p_queue_id scheduler.job_queue.queue_id%TYPE;\r\n" + 
					 		"begin\r\n" + 
					 		"        scheduler.Job.enqueue_job_on_demand( \r\n" + 
					 		"            9004, --from select in for loop   \r\n" + 
					 		"            'AUTOMATION_USER', \r\n" + 
					 		"            p_queue_id \r\n" + 
					 		"        );\r\n" + 
					 		"        dbms_output.put_line(p_queue_id);\r\n" + 
					 		"end;\r";
					 
					 
					 
					 st.executeUpdate(q);	 

					 
					 con.commit();
					 System.out.println("Commited");
					 
					 con.close();
					 System.out.println("Closed");
					
				}catch (Exception e) {
					e.printStackTrace();
				}
				
				
				
			} 
			
			
			
			
//Method to execute Laerdal Receipt Confirmation
			
			public void  runReceiptConfirmationLaerdalJob() {

				try {
					 String url = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(COMMUNITY=tcp.world)(PROTOCOL=TCP)(HOST=omxt001ip.newbreed.com)(PORT=36001))(CONNECT_DATA=(SID=OMXT001)))";
					 String username="mpaul";
					 String password="Scitqa1";
					 Connection con=null;
					 
					 
					 con=DriverManager.getConnection(url, username, password);
					 
					 con.setAutoCommit(false);
					 
					 
					 Statement st=con.createStatement();
					 System.out.println("Connection");
					 

					 
					 
					 String q="declare\r\n" + 
					 		"    p_queue_id scheduler.job_queue.queue_id%TYPE;\r\n" + 
					 		"begin\r\n" + 
					 		"        scheduler.Job.enqueue_job_on_demand( \r\n" + 
					 		"            9103, --from select in for loop   \r\n" + 
					 		"            'AUTOMATION_USER', \r\n" + 
					 		"            p_queue_id \r\n" + 
					 		"        );\r\n" + 
					 		"        dbms_output.put_line(p_queue_id);\r\n" + 
					 		"end;\r";
					 
					 
					 
					 st.executeUpdate(q);	 

					 
					 con.commit();
					 System.out.println("Commited");
					 
					 con.close();
					 System.out.println("Closed");
					
				}catch (Exception e) {
					e.printStackTrace();
				}
				
				
				
			} 
			
			
			
//Method to execute Laerdal Receipt Confirmation
			
			public void  runOutboundReceiptConfirmationLaerdalJob() {

				try {
					 String url = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(COMMUNITY=tcp.world)(PROTOCOL=TCP)(HOST=omxt001ip.newbreed.com)(PORT=36001))(CONNECT_DATA=(SID=OMXT001)))";
					 String username="mpaul";
					 String password="Welcome123";
					 Connection con=null;
					 
					 
					 con=DriverManager.getConnection(url, username, password);
					 
					 con.setAutoCommit(false);
					 
					 
					 Statement st=con.createStatement();
					 System.out.println("Connection");
					 

					 
					 
					 String q="declare\r\n" + 
					 		"    p_queue_id scheduler.job_queue.queue_id%TYPE;\r\n" + 
					 		"begin\r\n" + 
					 		"        scheduler.Job.enqueue_job_on_demand( \r\n" + 
					 		"            9103, --from select in for loop   \r\n" + 
					 		"            'AUTOMATION_USER', \r\n" + 
					 		"            p_queue_id \r\n" + 
					 		"        );\r\n" + 
					 		"        dbms_output.put_line(p_queue_id);\r\n" + 
					 		"end;\r";
					 
					 
					 
					 st.executeUpdate(q);	 

					 
					 con.commit();
					 System.out.println("Commited");
					 
					 con.close();
					 System.out.println("Closed");
					
				}catch (Exception e) {
					e.printStackTrace();
				}
				
				
				
			} 
			
			
	
}
