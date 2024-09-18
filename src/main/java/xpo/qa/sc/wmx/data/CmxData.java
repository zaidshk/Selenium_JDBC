package xpo.qa.sc.wmx.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class CmxData {

	/**
	 * CMX DATA inputs
	 */
	public static String yearDate_$YDAT;
	public static String serverName_CMXDB,serverNameQA_CMXDB,serverNameProd_CMXDB;
	public static String port_CMXDB,portQA_CMXDB,portProd_CMXDB;
	public static String database_CMXDB,databaseQA_CMXDB,databaseProd_CMXDB;
	public static String userid_CMXDB,useridQA_CMXDB,useridProd_CMXDB;
	public static String password_CMXDB,passwordQA_CMXDB,passwordProd_CMXDB;
	public static String CMXSourceTableName,CMXTargetTableName,CMXNoOfDays,OmxToCmxTargetTable;
	public static String externalKey;

	public static Properties cmxProp;
	public static String propertiesFilePath = System.getProperty("user.dir") + "/src/main/resources/cmxData.properties";

	/**
	 * Constructor to initialize and load the properties file , Where test data
	 * for being CMX is being saved
	 */
	public CmxData() {
		
		cmxProp = new Properties();
		try {
			cmxProp.load(new FileInputStream(propertiesFilePath));
		} catch (Exception e) {
		}

		yearDate_$YDAT = cmxProp.getProperty("$YDAT");
		serverName_CMXDB = cmxProp.getProperty("CMXDB_server");
		port_CMXDB = cmxProp.getProperty("CMXDB_port");
		database_CMXDB = cmxProp.getProperty("CMXDB_database");
		userid_CMXDB = cmxProp.getProperty("CMXDB_userid");
		password_CMXDB = cmxProp.getProperty("CMXDB_password");
		externalKey = cmxProp.getProperty("CMX_FILE_NAME");
		
		//Fetch Prod details from CmxData.properties file
		
				serverNameProd_CMXDB = cmxProp.getProperty("CMXDBProd_server");
				portProd_CMXDB = cmxProp.getProperty("CMXDBProd_port");
				databaseProd_CMXDB = cmxProp.getProperty("CMXDBProd_database");
				useridProd_CMXDB = cmxProp.getProperty("CMXDBProd_userid");
				passwordProd_CMXDB = cmxProp.getProperty("CMXDBPod_password");
				CMXSourceTableName = cmxProp.getProperty("CMXSourceTableName");
				CMXTargetTableName = cmxProp.getProperty("CMXTargetTableName");
				CMXNoOfDays = cmxProp.getProperty("CMXNoOfDay");
				
				//Fetch QA details from CmxData.properties file
				
				serverNameQA_CMXDB = cmxProp.getProperty("CMXDBQA_server");
				portQA_CMXDB = cmxProp.getProperty("CMXDBQA_port");
				databaseQA_CMXDB = cmxProp.getProperty("CMXDBQA_database");
				useridQA_CMXDB = cmxProp.getProperty("CMXDBQA_userid");
				passwordQA_CMXDB = cmxProp.getProperty("CMXDBQA_password");
				OmxToCmxTargetTable = cmxProp.getProperty("OMXToCMXTargetTable");
				

	}

	/**
	 * Set/Store the value in both properties file and CMX DATA class level
	 * varibales
	 * 
	 * @param value
	 * @throws IOException
	 */
	public static void setYearDate_$YDAT(String value) throws IOException {
		yearDate_$YDAT = value;
		cmxProp.setProperty("$YDAT", yearDate_$YDAT);
		storeProperties();
	}

	public static void setExternalKey(String value) throws IOException {
		externalKey = value;
		cmxProp.setProperty("CMX_FILE_NAME", externalKey);
		storeProperties();
	}

	public static void storeProperties() throws IOException {
		File file = new File(propertiesFilePath);
		FileOutputStream fileOut = new FileOutputStream(file);
		cmxProp.store(fileOut, null);
		fileOut.close();
	}
}
