package xpo.qa.sc.wmx.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class TestData {

	/**
	 * Test DATA inputs
	 */

	public static String serverName_TESTDB;
	public static String port_TESTDB;
	public static String database_TESTDB;
	public static String userid_TESTDB;
	public static String password_TESTDB;

	public static Properties testProp;
	public static String propertiesFilePath = System.getProperty("user.dir") + "/src/main/resources/TestDB.properties";

	/**
	 * Constructor to initialize and load the properties file , Where test data
	 * for being TEST is being saved
	 */
	public TestData() {
		testProp = new Properties();
		try {
			testProp.load(new FileInputStream(propertiesFilePath));
		} catch (Exception e) {
		}

		serverName_TESTDB = testProp.getProperty("TESTDB_server");
		port_TESTDB = testProp.getProperty("TESTDB_port");
		database_TESTDB = testProp.getProperty("TESTDB_database");
		userid_TESTDB = testProp.getProperty("TESTDB_userid");
		password_TESTDB = testProp.getProperty("TESTDB_password");

	}

	public static void storeProperties() throws IOException {
		File file = new File(propertiesFilePath);
		FileOutputStream fileOut = new FileOutputStream(file);
		testProp.store(fileOut, null);
		fileOut.close();
	}
}
