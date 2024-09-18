package xpo.qa.sc.wmx.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * This class reprsents all the test data that is being used in WMX Application.
 * Each key-value pair in WmxData.properties data is being mapped varibale of
 * this class, And these variables we use in Test Scripts as part of test data
 * 
 * @author acharya.priyanka
 *
 */
public class WmxData {

	/**
	 * WMX DATA inputs
	 */
	public static String serverName_WMXDB;
	public static String port_WMXDB;
	public static String database_WMXDB;
	public static String userid_WMXDB;
	public static String password_WMXDB;

	public static String url_WMX;
	public static String url_m_WMX;;

	public static String userId_WMXLogin;
	public static String password_WMXLogin;
	public static String orderkey_WMX;
	public static Properties wmxProp;
	public static String propertiesFilePath = System.getProperty("user.dir") + "/src/main/resources/wmxData.properties";

	/**
	 * Constructor to initialize and load the properties file , Where test data
	 * for being WMX is being saved
	 */
	public WmxData() {
		wmxProp = new Properties();
		try {
			wmxProp.load(new FileInputStream(propertiesFilePath));
		} catch (Exception e) {
		}

		serverName_WMXDB = wmxProp.getProperty("WMXDB_server");
		port_WMXDB = wmxProp.getProperty("WMXDB_port");
		database_WMXDB = wmxProp.getProperty("WMXDB_database");
		userid_WMXDB = wmxProp.getProperty("WMXDB_userid");
		password_WMXDB = wmxProp.getProperty("WMXDB_password");

		url_WMX = wmxProp.getProperty("WMX_url");
		url_m_WMX = wmxProp.getProperty("WMX_M_url");

		userId_WMXLogin = wmxProp.getProperty("WMXLogin_userId");
		password_WMXLogin = wmxProp.getProperty("WMXLogin_password");
		orderkey_WMX = wmxProp.getProperty("WMXOrderKey");

	}

	public static void setOrderKey(String value) throws IOException {
		orderkey_WMX = value;
		wmxProp.setProperty("WMXOrderKey", orderkey_WMX);
		storeProperties();
	}

	public static void storeProperties() throws IOException {
		File file = new File(propertiesFilePath);
		FileOutputStream fileOut = new FileOutputStream(file);
		wmxProp.store(fileOut, null);
		fileOut.close();
	}

}
