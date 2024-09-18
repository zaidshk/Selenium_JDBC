package xpo.qa.sc.wmx.utilities;



//package xpo.qa.common.databases.oracle;

import xpo.qa.common.Credentials;
//import xpo.qa.common.databases.DatabaseUtil;
import xpo.qa.sc.wmx.utilities.DatabaseUtil;
/**
 * @deprecated Please use other Oracle DB classes
 */
@Deprecated
public class OracleDB extends DatabaseUtil {
	private boolean serviceName = false;

	/**
	 * Create an Oracle DB connection
	 * 
	 * @param server Server name
	 * @param port Server port
	 * @param database Database SID
	 * @param user Username
	 * @param pwd Password
	 */
	public OracleDB(String server, String port, String database, String user, String pwd) {
		super(server, port, database, user, pwd);
	}
	
	/**
	 * Create an Oracle DB connection
	 * 
	 * @param server Server name
	 * @param port Server port
	 * @param database Database SID
	 * @param credentials Credentials object
	 */
	public OracleDB(String server, String port, String database, Credentials credentials) {
		super(server, port, database, credentials.getUsername(), credentials.getPassword());
	}
	
	/**
	 * Create an Oracle DB connection, and specify if it's connecting using service name or SID
	 * 
	 * @param server Server name
	 * @param port Server port
	 * @param database Database SID
	 * @param user Username
	 * @param pwd Password
	 * @param serviceNameConnection True if connecting using a service name, false if SID
	 */
	public OracleDB(String server, String port, String database, String user, String pwd, boolean serviceNameConnection) {
		super(server, port, database, user, pwd);
		
		serviceName = serviceNameConnection;
	}
	
	/**
	 * Create an Oracle DB connection, and specify if it's connecting using service name or SID
	 * 
	 * @param server Server name
	 * @param port Server port
	 * @param database Database SID
	 * @param credentials Credentials object
	 * @param serviceNameConnection True if connecting using a service name, false if SID
	 */
	public OracleDB(String server, String port, String database, Credentials credentials, boolean serviceNameConnection) {
		super(server, port, database, credentials.getUsername(), credentials.getPassword());
		
		serviceName = serviceNameConnection;
	}
	
	/**
	 * Open Oracle DB Connection
	 */
	public void connect() {
		String url = "";
		
	    try {
	    	Class.forName("oracle.jdbc.driver.OracleDriver"); 
	    	if (serviceName) {
	    		url = "jdbc:oracle:thin:@" + serverName + ":" + portNumber + ":" + databaseName;
	    	} else {
	    		url = "jdbc:oracle:thin:@//" + serverName + ":" + portNumber + "/" + databaseName;
	    	}
	        conn = java.sql.DriverManager.getConnection(url, userName, password);
	    } catch(Exception e) {
	        throw new RuntimeException(e.getMessage());
	    }
	}
	
}
