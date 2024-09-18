package xpo.qa.sc.wmx.utilities;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import xpo.qa.common.databases.ConnectionException;

public abstract class DatabaseUtil {
	public Connection conn;
	protected String serverName;
	protected String portNumber;
	protected String databaseName;
	protected String userName;
	protected String password;

	public DatabaseUtil(String server, String port, String database, String user, String pwd) {
		serverName = server;
		portNumber = port;
		databaseName = database;
		userName = user;
		password = pwd;
	}

	public Connection getConnection() {
		return conn;
	}

	public String getPassword() {
		return password;
	}

	public String getServer() {
		return serverName;
	}

	public String getDatabase() {
		return databaseName;
	}

	public String getPort() {
		return portNumber;
	}

	public String getUser() {
		return userName;
	}

	/**
	 * Open DB Connection
	 * <p>
	 * This function is specific to the type of database Oracle/DB2/SQL Server.
	 * Needs to be implemented by the subclass.
	 */
	public abstract void connect();

	/**
	 * Close DB Connection
	 */
	public void close() throws SQLException {
		try {
			if (conn != null) {
				conn.close();
			}
			conn = null;
		} catch (SQLException e) {
			throw e;
		}
	}

	public void executeUpdate(String sql) throws Exception {
		Statement stmt = null;
		try {
			connect();
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);

		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				connect();
				stmt = conn.createStatement();
				stmt.executeUpdate(sql);

			} catch (Exception e) {
				e.printStackTrace();

			}
		}
	}

	public void executeInsert(String sql) throws Exception {
		Statement stmt = null;

		try {
			connect();
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);

		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Execute SQL query and return the results in a list of mapped data
	 * <p>
	 * Example Data
	 * <p>
	 * Table: COLUMN1 | COLUMN2 ----------------- apple | orange pear | grape
	 * <p>
	 * Output:
	 * <p>
	 * List(0) = Map("COLUMN1") = "apple", Map("COLUMN2") = "orange" List(1) =
	 * Map("COLUMN1") = "pear", Map("COLUMN2") = "grape"
	 *
	 * @param sql SQL query
	 * @return List of mapped SQL data
	 * @throws SQLException
	 */
	public List<Map<String, String>> selectSQL(String sql) throws SQLException {
		List<Map<String, String>> data = new ArrayList<>();
		List<String> columns = new ArrayList<>();
		ResultSet rs = null;
		Statement stmt = null;

		try {
			connect();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			if (rs != null) {
				// Get the column names so we can use those to build mapped data
				ResultSetMetaData metaData = rs.getMetaData();

				for (int i = 1; i <= metaData.getColumnCount(); i++) {
					columns.add(metaData.getColumnName(i));
				}

				// For each of the result rows add a new map with the data
				while (rs.next()) {
					Map<String, String> row = new HashMap<>();

					for (String column : columns) {
						String val = rs.getString(column);
						if (val == null) {
							val = "null";
						}
						row.put(column, val);
					}
					data.add(row);
				}
			}

		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return data;
	}

	
	
	//Takes two inputs,Script and Query (In case some scripts need to be excuted before executing actual SQL Query This Method can be called
		public List<Map<String, String>> select_scriptAndQuery(String  script,String query) throws ConnectionException, SQLException {
			List<Map<String, String>> data = new ArrayList<Map<String, String>>();
			List<String> columns = new ArrayList<String>();
			ResultSet rs = null;
			Statement stmt = null;

			try {
				connect();
				stmt = conn.createStatement();
				
					 stmt.execute(script);
					 rs =stmt.executeQuery(query);
				
				

				if (rs != null) {
					// Get the column names so we can use those to build mapped data
					ResultSetMetaData metaData = rs.getMetaData();

					for (int i = 1; i <= metaData.getColumnCount(); i++) {
						columns.add(metaData.getColumnName(i));
					}

					// For each of the result rows add a new map with the data
					while (rs.next()) {
						Map<String, String> row = new HashMap<String, String>();

						for (String column : columns) {
							String val = rs.getString(column);
							if (val == null) {
								val = "null";
							}
							row.put(column, val);
						}
						data.add(row);
					}
				}

			} catch (Exception e) {
				throw e;
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (stmt != null) {
						stmt.close();
					}
					close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			return data;
		}

		public Map<String, String> selectSQL(String sql, int row) throws ConnectionException, SQLException {
			List<Map<String, String>> data = selectSQL(sql);

			return data.get(row);
		}

		/**
		 * Execute an insert and get auto generated keys
		 * 
		 * @author Satish Reddy
		 * 
		 * @param sql
		 * @return
		 * @throws ConnectionException
		 * @throws SQLException
		 */
		public String executeInsertGetGeneratedKeys(String sql) throws ConnectionException, SQLException {

			ResultSet rs = null;
			Statement stmt = null;

			String autoId = null;

			try {
				connect();

				stmt = conn.createStatement();
				stmt.execute(sql, new String[] { "ID" });
				rs = stmt.getGeneratedKeys();
				rs.next();
				autoId = rs.getString(1);

			} catch (

			Exception e) {
				throw e;
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (stmt != null) {
						stmt.close();
					}
					close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			return autoId;
		}

}

	
	
	

