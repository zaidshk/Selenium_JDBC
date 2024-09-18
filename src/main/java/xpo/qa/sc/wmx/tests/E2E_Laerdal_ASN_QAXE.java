package xpo.qa.sc.wmx.tests;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import xpo.qa.common.Utils;

import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

//import xpo.qa.common.databases.oracle.OracleDB;
import xpo.qa.common.selenium.DriverManager;
import xpo.qa.common.selenium.DriverManagerFactory;
import xpo.qa.common.selenium.DriverManagerType;
import xpo.qa.common.selenium.TestProperties;
import xpo.qa.sc.omx.data.OmxData;
import xpo.qa.sc.omx.pages.OmxAsnSearchPage;
import xpo.qa.sc.omx.pages.OmxHomePage2;
import xpo.qa.sc.omx.pages.OmxJobsViewPage2;
import xpo.qa.sc.omx.pages.OmxLoginPage;
import xpo.qa.sc.wmx.data.CmxData;
import xpo.qa.sc.wmx.data.WmxData;
import xpo.qa.sc.wmx.m.pages.WmxMHomePage2;
import xpo.qa.sc.wmx.m.pages.WmxMLoginPage;
import xpo.qa.sc.wmx.m.pages.WmxMPutaway;
import xpo.qa.sc.wmx.pages.WmxAsnPage;
import xpo.qa.sc.wmx.pages.WmxHomePage;
import xpo.qa.sc.wmx.pages.WmxLoginPage;
import xpo.qa.sc.wmx.pages.WmxReceiptPage;
import xpo.qa.sc.wmx.utilities.FlatFileCompare;
import xpo.qa.sc.wmx.utilities.WMXDBUtils;
import xpo.qa.sc.wmx.utilities.WmxUtil;
import xpo.qa.sc.wmx.utilities.OracleDB;



public class E2E_Laerdal_ASN_QAXE extends TestBase {
	
	private DriverManager manager;
	private CmxData cmxData;	
	private WmxData wmxData;
	private ExtentTest test;
	private SoftAssert softAssert;
	private int failure_Steps;
	private WmxUtil util;
	private OmxData omxData;
	
	private OmxLoginPage loginOmx;
	private OmxHomePage2 homeOmx;
	private OmxJobsViewPage2 jobsViewOmx;
	
	private OmxAsnSearchPage omxAsnSearch;
	private WmxLoginPage loginWmx;
	private WmxHomePage homeWmx;
	private WmxAsnPage asnPage;
	private WmxReceiptPage receiptPage;
	private WmxMLoginPage loginWmxM;
	private WmxMHomePage2 homeWmxM;
	
	private WmxMPutaway wmxputaway;
	
	private WMXDBUtils db;
	
	private FlatFileCompare fileCompare;
	
public void initialSetUp(String scriptName) {
		
		cmxData = new CmxData();
		omxData = new OmxData();
		wmxData=new WmxData();
		util = new WmxUtil();
		db=new  WMXDBUtils();
		
		loginOmx = new OmxLoginPage(manager.getDriver());
		homeOmx= new OmxHomePage2(manager.getDriver());
		jobsViewOmx = new OmxJobsViewPage2(manager.getDriver());
		omxAsnSearch= new OmxAsnSearchPage(manager.getDriver());
		asnPage=new WmxAsnPage(manager.getDriver());
		receiptPage= new WmxReceiptPage(manager.getDriver());
		
		loginWmx=new WmxLoginPage(manager.getDriver());
		homeWmx= new WmxHomePage(manager.getDriver());
		
		loginWmxM = new WmxMLoginPage(manager.getDriver());
		homeWmxM = new WmxMHomePage2(manager.getDriver());
		wmxputaway=new WmxMPutaway(manager.getDriver());
		test = TestProperties.getExtentReports().createTest((this.getClass().getSimpleName() + "::" + scriptName),
				scriptName);
		test.assignCategory("E2E L'Oreal");
		test.assignAuthor("SCITQA TEAM");
		softAssert = new SoftAssert();
		
	}
	
	
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		manager = DriverManagerFactory.getManager(DriverManagerType.CHROME);
		ChromeOptions options = new ChromeOptions();
		if (getHeadless()) {
			options.addArguments("window-position=0,0");
			options.addArguments("window-size=1920,1080");
			options.addArguments("headless");
		}
		manager.createDriver(options);
		TestProperties.initialize();
		initialSetUp(method.getName());

	}

	@AfterMethod(alwaysRun = true)
	public void afterMethod(ITestResult result) {
		if (result.getStatus() == ITestResult.FAILURE) {
			captureScreenshot("failure");
		}
		if (failure_Steps > 0) {
			test.log(Status.FAIL, "Test is Failed ");
		}
		failure_Steps = 0;
		try {
			TestProperties.getExtentReports().flush();
		} catch (Exception e) {
			test.log(Status.FAIL, "Error during close, no tests executed to report.  No ExtentReport generated.");
		}
		close();
	}
	
	
	@AfterTest(alwaysRun = true)
	public void dispalyExecutionResult() {
		manager = DriverManagerFactory.getManager(DriverManagerType.CHROME);
		ChromeOptions options = new ChromeOptions();
		manager.createDriver(options);
		TestProperties.getDriver().get(System.getProperty("user.dir") + "\\test-output\\log\\ExtentReports.html");
	}
	
	
	@Test(priority = 1)
	public void cmxInsertLorealASNDB() throws IOException {
		try {
			if (insertASNDataRecordCMX()) {
				test.log(Status.PASS, "Successfully Inserted ASN Record into CMXDATA.X_TRANSMISSION");
			} else {
				reportStatusFail("Error in Inserting  Record into CMXDATA.X_TRANSMISSION");
			}
		} catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		softAssert.assertAll();
	}
	
	
	@Test(priority=2)
	public void omxAsnInboundImport() {
		try {
			db.runCMxInboundImportLaerdalJob();
			
			System.out.println("Executed Inbound Import");
			
			Utils.sleep(60000);
			
			db.runInboundAsnImportLaerdalJob();
			
			System.out.println("Executed ASN Import");
			
			Utils.sleep(60000);
				
			
			Utils.sleep(5000);
			test.log(Status.PASS, "Successfully ran Laerdal Inbound JOB and Laerdal ASN Import JOB");
		}catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		softAssert.assertAll();
		
	}

	@Test(priority=3)
	public void checkASN1() {
		

		
		try {
			
			Utils.sleep(60000);
			if(searchASN()) {
				
				
				test.log(Status.PASS, "Expected ASN with externkey " +  CmxData.externalKey + "loaded in OMx " );
				
				
			}else {
				
				reportStatusFail("Failed");
				
			}
			
			
			}catch(Exception e) {
				reportStatusFail(e.getMessage());
				e.printStackTrace();
			}
			softAssert.assertAll();
		}
	
	
	
	@Test(priority=4)
	public void wmxTestSearchASN() {
		try {
			Utils.sleep(60000);

			
			if(
					getAsnStatusWmxDB().equals("101")) {
				
				test.log(Status.PASS, "Expected ASN is propagated to WMx from OMx ");
			}else {
			reportStatusFail("Expected ASN is not propagated to WMx from OMx");
			}
					
			
		}catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		softAssert.assertAll();
		
		
	}

	
	
	
	@Test(priority=5)
	public void wmxTestCreateReceipt() {
		try {
			
		Utils.sleep(60000);
		loginToWMX();
		test.log(Status.PASS, "Logged into the WMx application");
		Utils.sleep(120000);
		homeWmx.selectInboundMenu();
		test.log(Status.PASS, "Selected Inbound menu");
		receiptPage.selectReceiptMenu();
		test.log(Status.PASS, "Selected Receipt");
		receiptPage.createReceiptForLaerdal();
		Utils.sleep(5000);
		receiptPage.searchExternKey(cmxData.externalKey);
		receiptPage.clickOnEditButton();
		Utils.sleep(5000);
		receiptPage.clickOnNewButton();
		Utils.sleep(9000);
		
		receiptPage.setQtyforLaerdalASN(getAsnExpectedQty());	
		
		Utils.sleep(5000);
		
		receiptPage.setSerialNumber("serialnumber");
		
		receiptPage.clickOnSaveButton();
		
		Utils.sleep(5000);
		
		receiptPage.receive();
		
		Utils.sleep(5000);
		
		
		
		if(getReceiptStatus().equalsIgnoreCase("200") 
			

				
				
			) {
			System.out.println(getReceiptStatus());
			test.log(Status.PASS, "Expected receipt is created and received ");
		}	else{
			reportStatusFail("Expected receipt is not created and received");
		}
		
		
		
		Utils.sleep(3000);
		
		if(getInvRecord()==true) {
			
			test.log(Status.PASS, "Inventory record is created");
			
		}else {
			reportStatusFail("Expected inv record is not created and received");
			
		}
		
		
		
		
		Utils.sleep(120000);
		
		if(getTaskRecord(getDocKey(getLPN()))==true) {
			
			test.log(Status.PASS, "Expected task is created");
			
		}else {
			reportStatusFail("Expected task record is not created");
			
		}
		
		
		
		
		}catch(Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		softAssert.assertAll();
	}



	@Test(priority=6)
	public void testPutaway() {
		
		try {
			
			
			if(checkInvBeforePutaway()==true) {
				
				test.log(Status.PASS, "Exepected inventory is created");
				
			}
			else {
				reportStatusFail("Expected inventory is not created");
				
			}
			
			loginToWmxM();
			test.log(Status.PASS, "Logged into WMx mobile application");
			Utils.sleep(9000);
			homeWmxM.selectPutaway();
			test.log(Status.PASS, "Selected putaway");
			wmxputaway.selectPutawaySearch();
			test.log(Status.PASS, "Selected putaway search");
			Utils.sleep(5000);
			wmxputaway.setLpn(getLPN());
			Utils.sleep(9000);
			wmxputaway.executePutawayForLaerdal(getToLoc(getLPN()));
			Utils.sleep(60000);		
//			if(getInvQtyAfterPutaway()==true) {
//				test.log(Status.PASS, "Exepected inventory is created after putaway");
//				
//			}
//			else {
//				reportStatusFail("Expected inventory is not created after putaway");
//				
//			}
			
			
		}catch(Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		
		softAssert.assertAll();
		
	}
	
	
	
	
	@Test(priority=7)
	public void testTaskRecordAfterPutaway() {
		
		try {
			
			Utils.sleep(120000);
			if(validateTaskRecordAfterPutaway()==true) {
				
				test.log(Status.PASS, "Task record has been removed after putaway execution");
			}else {
				
				reportStatusFail("Expected task record is not removed");
			}
			
			
			
			
		}catch(Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		softAssert.assertAll();
		
		
	}
	
	
	
	@Test(priority=8)
	public void testAsnStatusOmx() {
		
		try {
			Utils.sleep(2000);
			
			if(
					
					getAsnStatusOmxDB().equalsIgnoreCase("200")
					
					
					) {
				test.log(Status.PASS, "ASN status is updated to 200 ");
		}	else{
			reportStatusFail("ASN status is not updated to 200");
		}
		
			
			
		}catch(Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		softAssert.assertAll();
		
	}
	
	
	
//	@Test(priority=9)
//	public void runOutboundJobs() {
//		
//		try {
//			
//		db.runInboundReceiptConfirmationLorealJob();
//		
//		System.out.println("Executed Inbound Receipt Confirmation Job through DB");
//		
//		Utils.sleep(7000);
//		
//		db.runReceiptConfirmationLaerdalJob();
//		
//		System.out.println("Executed CMX OUTBOUND Receipt Confirmation Job through DB");
//		
//		Utils.sleep(7000);
//		
//		test.log(Status.PASS, "Ran outbound receipt confirmation job");
//		
//		
//		}catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//		softAssert.assertAll();
//		
//		
//	}
//	
//	
//	
//	@Test(priority = 10)
//	public void cmxVerifyOrderConfirmationStatus() {
//		try {
//			
//			
//			OracleDB db = new OracleDB(CmxData.serverName_CMXDB, CmxData.port_CMXDB, CmxData.database_CMXDB,
//				CmxData.userid_CMXDB, CmxData.password_CMXDB,
//				true);
//			
//			Utils.sleep(120000);
//				
//			
//
//
//			
//			String sqlCMXValidationQuery = "select id,file_name,status,transmission_type,tp_transmission_type,tp_unique_num, payload from cmxdata.x_transmission  where  TP_UNIQUE_NUM='"
//					+ cmxData.externalKey + "' order by id desc";
//			
//			List<Map<String, String>> dbData = db.selectSQL(sqlCMXValidationQuery);
//			System.out.println(dbData.get(0).get("TRANSMISSION_TYPE"));
//			if (dbData.get(0).get("TRANSMISSION_TYPE").equals("RECEIPT_CONFIRMATION")) {
//				test.log(Status.PASS,
//					"Expected ASN Receipt Confirmation record is generated in CMx DB");
//			} else {
//				reportStatusFail("Expected ASN Receipt Confirmation record is not generated in CMx DB");
//			}
//			
//			
//			String str=dbData.get(0).get("PAYLOAD");
//			
//			System.out.println(str);
//			
//			
//			File file = new File("C:\\Users\\hsawant\\eclipse-workspace\\sc-qa-wmx\\src\\main\\resources\\LaerdalAsnQAXB_ReceiptConfirmationPaylaod.txt");
//			FileWriter fr = new FileWriter(file, false);
//			BufferedWriter br = new BufferedWriter(fr);
//			br.write(str);
//
//			br.close();
//			fr.close();
//			
//			
//			
//			String ReceiptConfirmationPaylaod="C:\\Users\\hsawant\\eclipse-workspace\\sc-qa-wmx\\src\\main\\resources\\LaerdalAsnQAXB_ReceiptConfirmationPaylaod.txt";
//			
//			String maskPath="C:\\Users\\hsawant\\eclipse-workspace\\sc-qa-wmx\\src\\main\\resources\\MaskForLaerdalAsnQAXB.txt";
//			
//			String ReceiptConfirmationGoldPaylaod="C:\\Users\\hsawant\\eclipse-workspace\\sc-qa-wmx\\src\\main\\resources\\LaerdalAsnQAXB_ReceiptConfirmationGoldPayload.txt";
//			
//			fileCompare.mask(ReceiptConfirmationPaylaod, maskPath, "FLAT", true);
//			
//			test.log(Status.PASS,
//					"Masked file");
//			
//			
//			if(comaprePayload(ReceiptConfirmationPaylaod, ReceiptConfirmationGoldPaylaod)==true) {
//				
//				System.out.println("Two files have same content.");
//		        test.log(Status.PASS,
//						"Payload file comparison successful");
//				
//				
//				
//			}
//			else {
//				
//				reportStatusFail("File Comparison Failed");
//			}
//			
//
//			
//			
//			
//			
//			
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//		softAssert.assertAll();
//	}
	

	public boolean insertASNDataRecordCMX() {
		boolean insertionSuccess = false;
		try {
			OracleDB db = new OracleDB(CmxData.serverName_CMXDB, CmxData.port_CMXDB, CmxData.database_CMXDB,
				CmxData.userid_CMXDB, CmxData.password_CMXDB,
				true);

			String sqlInsertQuery = util.setLaerdal_QAXE_Query();
			String insertedRecordID = db.executeInsertGetGeneratedKeys(sqlInsertQuery);
			System.out.println("Inserted Record ID: " + insertedRecordID);
			String sqlSelectQueryValidation = "select * from CMXDATA.X_TRANSMISSION WHERE ID=" + insertedRecordID;
			List<Map<String, String>> dbData = db.selectSQL(sqlSelectQueryValidation);
			System.out.println(dbData.get(0));
			String externalKey = dbData.get(0).get("FILE_NAME").trim();
			System.out.println("External Key : " + externalKey);
			CmxData.setExternalKey(externalKey);

			if (dbData.size() > 0) {
				insertionSuccess = true;
			}
		} catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		return insertionSuccess;

	}
	
	
	public void reportStatusFail(String failLog) {
		test.log(Status.FAIL, failLog);
		failure_Steps = failure_Steps + 1;
		softAssert.fail(failLog);
		softAssert.assertTrue(false);
		captureScreenshot("failure");
	}

	
	
	public boolean searchASN() {
		boolean flag=false;
		try {
			OracleDB db = new OracleDB(OmxData.serverName_OMXDB, OmxData.port_OMXDB, OmxData.database_OMXDB,
				OmxData.userid_OMXDB, OmxData.password_OMXDB,
				true);
//			String omxStatusQuery = "SELECT STATUS from ORDER_A WHERE EXTERNKEY='" + CmxData.externalKey + "'";
			String orderSearchQuery = "SELECT * from ASN_A WHERE EXTERNKEY='" + CmxData.externalKey + "'";
			List<Map<String, String>> dbData = db.selectSQL(orderSearchQuery);
			
			if(dbData.size()>0) {
				
				flag=true;
			}
			
			
		} catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}
	
	
	public String getAsnStatusWmxDB() {
		String status = "";
		try {
			OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
					WmxData.userid_WMXDB, WmxData.password_WMXDB,
					true);
			String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'MEM001'); COMMIT; END; ";
			
			String wmxAsnKeyQuery= "Select STATUS FROM WX_ASN WHERE EXTERNKEY='" + cmxData.externalKey + "'";
			List<Map<String, String>> dbData=db.select_scriptAndQuery(wmxScript, wmxAsnKeyQuery);
			status=dbData.get(0).get("STATUS");
			
			System.out.println(status);
			
		}catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		return status;
	}
	
	
	public void loginToWMX() {
		try {
			TestProperties.getDriver().get("http://mem001.qa.wmx.sc.xpo.com/login");
			loginWmx.loginToWMX(WmxData.userId_WMXLogin, WmxData.password_WMXLogin);
//			if (homeWmx.getTextWarehouse().equals("WAREHOUSE")) {
//				test.log(Status.PASS, "Successfully Logged into WMX web Application ");
//			} else {
//				reportStatusFail("Not able to login WMX Web App");
//			}
		} catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	
	
	public String getAsnExpectedQty() {
		String qty = "";
		try {
			OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
					WmxData.userid_WMXDB, WmxData.password_WMXDB,
					true);
			String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'MEM001'); COMMIT; END; ";
			
			String wmxAsnDtlQtyQuery= "Select EXPECTEDQTY FROM WX_ASNDTL WHERE EXTERNKEY='" + cmxData.externalKey + "'";
			List<Map<String, String>> dbData=db.select_scriptAndQuery(wmxScript, wmxAsnDtlQtyQuery);
			qty=dbData.get(0).get("EXPECTEDQTY");
			
			System.out.println(qty);
			
		}catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		return qty;
	}
	
	
	
	
	
	
public String getReceiptStatus() {
		
		String STATUS="";
		try {
			OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
					WmxData.userid_WMXDB, WmxData.password_WMXDB,
					true);
			String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'MEM001'); COMMIT; END; ";
			
			String wmxReceiptStatusQuery= "Select STATUS FROM WX_RECEIPT WHERE EXTERNKEY='" + cmxData.externalKey + "'";
			List<Map<String, String>> dbData=db.select_scriptAndQuery(wmxScript, wmxReceiptStatusQuery);
			STATUS=dbData.get(0).get("STATUS");
			
			System.out.println(STATUS);
			
		}catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		
		return STATUS;
		
	}


public boolean getInvRecord() {
	boolean invRecord=false;
	
	try {
		
//		String LPN=getLPN();
		
		OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
				WmxData.userid_WMXDB, WmxData.password_WMXDB,
				true);
		String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'MEM001'); COMMIT; END; ";
		
		String wmxInvRecordStatusQuery= "Select * FROM WX_INVENTORY WHERE LPN='" + getLPN() + "'";
		List<Map<String, String>> dbData=db.select_scriptAndQuery(wmxScript, wmxInvRecordStatusQuery);
		
		if (dbData.size() > 0) {
			invRecord = true;
		}else {
			
			System.out.println("fail");
		}
		
		
		
	}catch (Exception e) {
		reportStatusFail(e.getMessage());
		e.printStackTrace();
		
	}
	
	return invRecord;
	
}




public String getLPN() {
	
	String LPN="";
	try {
		OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
				WmxData.userid_WMXDB, WmxData.password_WMXDB,
				true);
		String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'MEM001'); COMMIT; END; ";


		String wmxLPNQuery= "Select LPN FROM WX_RECEIPTDTL WHERE EXTERNKEY='" + cmxData.externalKey + "'";
		List<Map<String, String>> dbData=db.select_scriptAndQuery(wmxScript, wmxLPNQuery);
		LPN=dbData.get(0).get("LPN");
		
		System.out.println(LPN);
		
	}catch (Exception e) {
		reportStatusFail(e.getMessage());
		e.printStackTrace();
	
	}
	return LPN;
}
	

public String getDocKey(String lpn) {
	
	String DockKey="";
	try {
		OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
				WmxData.userid_WMXDB, WmxData.password_WMXDB,
				true);
		String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'MEM001'); COMMIT; END; ";


		String wmxLPNQuery= "Select RECEIPTKEY FROM WX_RECEIPTDTL WHERE LPN='" + lpn + "'";
		List<Map<String, String>> dbData=db.select_scriptAndQuery(wmxScript, wmxLPNQuery);
		DockKey=dbData.get(0).get("RECEIPTKEY");
		
		System.out.println(DockKey);
		
	}catch (Exception e) {
		reportStatusFail(e.getMessage());
		e.printStackTrace();
	}
	
	return DockKey;
	
}



public boolean getTaskRecord(String docKey) {
	boolean taskRecord=false;
	
	try {
		
//		String LPN=getLPN();
		
		OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
				WmxData.userid_WMXDB, WmxData.password_WMXDB,
				true);
		String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'MEM001'); COMMIT; END; ";
		
		String wmxTaskRecordStatusQuery= "Select * FROM WX_TASK WHERE DOCKEY='" + docKey + "'";
		List<Map<String, String>> dbData=db.select_scriptAndQuery(wmxScript, wmxTaskRecordStatusQuery);
		
		if (dbData.size() > 0) {
			taskRecord = true;
		}else {
			
			System.out.println("fail");
		}
		
		
		
	}catch (Exception e) {
		reportStatusFail(e.getMessage());
		e.printStackTrace();
		
	}
	
	return taskRecord;
	
}


public void loginToWmxM() {
	try {
		TestProperties.getDriver().get("http://m.mem001.qa.wmx.sc.xpo.com/login");
		loginWmxM.loginToWMXM(WmxData.userId_WMXLogin, WmxData.password_WMXLogin);

//		if (homeWmxM.getTextWarehouse().equals("WAREHOUSE")) {
//			test.log(Status.PASS, "Successfully Logged into WMX Mobile Application ");
//		} else {
//			reportStatusFail("Not able to login WMX Mobile App");
//		}
	} catch (Exception e) {
		reportStatusFail(e.getMessage());
		e.printStackTrace();
	}
}







public boolean checkInvBeforePutaway() {
	boolean checkflag=false;
	
	try {
		
		
		OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
				WmxData.userid_WMXDB, WmxData.password_WMXDB,
				true);
		String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'MEM001'); COMMIT; END; ";
		
//		String wmxInvIdQuery= "Select INVENTORYID FROM WX_INVENTORY WHERE LPN='" + getLPN() + "' AND LOC= ' "+ "STAGE "+"' ";
		
//		String wmxInvIdQuery= "Select * FROM WX_INVENTORY WHERE LPN='" + "2220195209" + "' AND LOC= ' "+ "STAGE "+"' ";
		
		String wmxInvIdQuery= "Select * FROM WX_INVENTORY WHERE LPN='" + getLPN() + "'  " ;
		
		
		
		List<Map<String, String>> dbData=db.select_scriptAndQuery(wmxScript, wmxInvIdQuery);
		
		 if(dbData.size()>0) {
			 
			 if(dbData.get(0).get("LOC").equalsIgnoreCase("STAGE")) {
				 
				 
			 }
			 
			  checkflag=true; 
			  
			  System.out.println(dbData.get(0).get("INVENTORYID"));
		 }
		 
		 else {
			 
			 reportStatusFail("Failed");
		 }
		
		
	}catch (Exception e) {
		reportStatusFail(e.getMessage());
		e.printStackTrace();
		
	}
	
	return checkflag;
	
	
}

public String getToLoc(String lpn) {
	
	String TOLOC="";
	try {
		OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
				WmxData.userid_WMXDB, WmxData.password_WMXDB,
				true);
		String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'MEM001'); COMMIT; END; ";


		String wmxLPNQuery= "Select TOLOC FROM WX_TASK WHERE LPN='" + lpn + "'";
		List<Map<String, String>> dbData=db.select_scriptAndQuery(wmxScript, wmxLPNQuery);
		TOLOC=dbData.get(0).get("TOLOC");
		
		System.out.println(TOLOC);
		
	}catch (Exception e) {
		reportStatusFail(e.getMessage());
		e.printStackTrace();
	}
	
	return TOLOC;
}




public boolean validateTaskRecordAfterPutaway() {
	boolean taskRecord=false;
	
	try {
		
//		String LPN=getLPN();
		
		OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
				WmxData.userid_WMXDB, WmxData.password_WMXDB,
				true);
		String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'MEM001'); COMMIT; END; ";
		
		String wmxTaskRecordStatusQuery= "Select * FROM WX_TASK WHERE LPN='" + getLPN() + "'";
		List<Map<String, String>> dbData=db.select_scriptAndQuery(wmxScript, wmxTaskRecordStatusQuery);
		
		if (dbData.size() == 0) {
			taskRecord = true;
		}else {
			
			System.out.println("fail");
		}
		
		
		
	}catch (Exception e) {
		reportStatusFail(e.getMessage());
		e.printStackTrace();
		
	}
	
	return taskRecord;
	
}


public String getAsnStatusOmxDB() {
	String status = "";
	try {
		OracleDB db = new OracleDB(OmxData.serverName_OMXDB, OmxData.port_OMXDB, OmxData.database_OMXDB,
			OmxData.userid_OMXDB, OmxData.password_OMXDB,
			true);
//		String omxStatusQuery = "SELECT STATUS from ORDER_A WHERE EXTERNKEY='" + CmxData.externalKey + "'";
		String asnStatusQuery = "SELECT STATUS from ASN_A WHERE EXTERNKEY='" + cmxData.externalKey + "'";
		List<Map<String, String>> dbData = db.selectSQL(asnStatusQuery);
		status = dbData.get(0).get("STATUS");
		System.out.println("Status in OMX DB: " + status);
	} catch (Exception e) {
		reportStatusFail(e.getMessage());
		e.printStackTrace();
	}
	return status;
}



}



