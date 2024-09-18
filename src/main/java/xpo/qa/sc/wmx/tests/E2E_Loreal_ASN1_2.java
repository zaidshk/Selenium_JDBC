


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
import xpo.qa.sc.wmx.utilities.OracleDB;
import xpo.qa.sc.wmx.utilities.WMXDBUtils;
import xpo.qa.sc.wmx.utilities.WmxUtil;


public class E2E_Loreal_ASN1_2 extends TestBase {
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
		
		fileCompare=new FlatFileCompare();
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
	
	
	
	
	
	@Test(priority=1)
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
			
			Utils.sleep(7000);
			
			db.runCMxInboundImportLorealJob();
			
			System.out.println("Executed Inbound Import");
			
			
			Utils.sleep(120000);
			
			db.runInboundAsnImportLorealJob();
			
			Utils.sleep(120000);
			
			System.out.println("Executed ASN Import");
			
		
			test.log(Status.PASS, "Successfully ran Loreal Inbound JOB and Loreal ASN Import JOB");
			
		}catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		softAssert.assertAll();
		
	}
	
	
	
	@Test(priority=3)
	public void checkASN1() {
		
		String externkey1=CmxData.externalKey+"1";
		
		System.out.println(externkey1);
		
		try {
			
			Utils.sleep(60000);
			if(searchASN(externkey1)) {
				
				
				test.log(Status.PASS, "Expected ASN with externkey " +  externkey1 + "loaded in OMx " );
				
				
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
	public void checkASNDetailsforASN1() {
		String externkey1=CmxData.externalKey+"1";
		
		System.out.println(externkey1);
		
		
		try {
			OracleDB db = new OracleDB(OmxData.serverName_OMXDB, OmxData.port_OMXDB, OmxData.database_OMXDB,
				OmxData.userid_OMXDB, OmxData.password_OMXDB,
				true);
////			String omxStatusQuery = "SELECT STATUS from ORDER_A WHERE EXTERNKEY='" + CmxData.externalKey + "'";
			String orderSearchQuery = "SELECT * from ASNDTL_A WHERE EXTERNKEY='" + externkey1 + "'";
			List<Map<String, String>> dbData = db.selectSQL(orderSearchQuery);
			
			
			
			int size=dbData.size();
			System.out.println(size);
			
			if(dbData.size()==1) {
				test.log(Status.PASS, "Expected ASN with externkey " +  externkey1 + "has 1 ASN details line " );
			
				
			}else {
				reportStatusFail("Failed");
				
			}
			
			
			
		} catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	@Test(priority=5)
	public void checkASNStatusForASN1() {
		
	String externkey1=CmxData.externalKey+"1";
		
		System.out.println(externkey1);
		
		try {
			if(validateASNStatus(externkey1)) {
				
				
				test.log(Status.PASS, "Status of ASN 1 in OMx is 101 " );
				
				
			}else {
				
				reportStatusFail("Failed");
				
			}
		}catch(Exception e) {
				reportStatusFail(e.getMessage());
				e.printStackTrace();
			}
		
		softAssert.assertAll();
		
		
	}
	
	
	
	@Test(priority=6)
	public void checkASN2() {
		
		String externkey2=CmxData.externalKey+"2";
		
		System.out.println(externkey2);
		
		try {
			if(searchASN(externkey2)) {
				
				
				test.log(Status.PASS, "Expected ASN with externkey " +  externkey2 + " "+  "loaded in OMx " );
				
				
			}else {
				
				reportStatusFail("Failed");
				
			}
			
			
			}catch(Exception e) {
				reportStatusFail(e.getMessage());
				e.printStackTrace();
			}
			softAssert.assertAll();
		}

	
	
	
	
	
	
	@Test(priority=7)
	public void checkASNDetailsforASN2() {
		String externkey2=CmxData.externalKey+"2";
		
		System.out.println(externkey2);
		
		
		try {
			OracleDB db = new OracleDB(OmxData.serverName_OMXDB, OmxData.port_OMXDB, OmxData.database_OMXDB,
				OmxData.userid_OMXDB, OmxData.password_OMXDB,
				true);
//////			String omxStatusQuery = "SELECT STATUS from ORDER_A WHERE EXTERNKEY='" + CmxData.externalKey + "'";
			String orderSearchQuery = "SELECT * from ASNDTL_A WHERE EXTERNKEY='" + externkey2 + "'";
			List<Map<String, String>> dbData = db.selectSQL(orderSearchQuery);
			
			
			
			int size=dbData.size();
			System.out.println(size);
			
			if(dbData.size()==3) {
				test.log(Status.PASS, "Expected ASN with externkey " +  externkey2 + "has 3 ASN details line " );
			
				
			}else {
				reportStatusFail("Failed");
				
			}
			
			
			
		} catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		
		
	}
	
	@Test(priority=8)
	public void checkASNStatusForASN2() {
		
	String externkey2=CmxData.externalKey+"2";
		
		System.out.println(externkey2);
		
		try {
			if(validateASNStatus(externkey2)) {
				
				
				test.log(Status.PASS, "Status of ASN 1 in OMx is either 101 " );
				
				
			}else {
				
				reportStatusFail("Failed");
				
			}
		}catch(Exception e) {
				reportStatusFail(e.getMessage());
				e.printStackTrace();
			}
		
		softAssert.assertAll();
		
		
		
	}
	
	
	
	@Test(priority=9)
	public void validateASN1inWMx() {
		
		String externkey1=CmxData.externalKey+"1";
		try {
			if(searchASNWMx(externkey1)) {
				
				
				test.log(Status.PASS, "ASN 1 propagated to WMx" );	
				
				
			}else {
				
				test.log(Status.PASS, "failed" );
			}
			
			
		}catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		softAssert.assertAll();
			
		
	}
	
	@Test(priority=10)
	public void validateASN1DetailsInWMx() {
		String externkey1=CmxData.externalKey+"1";
		try {
			OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
					WmxData.userid_WMXDB, WmxData.password_WMXDB,
					true);
						
			String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'ONT004'); COMMIT; END; ";
			
			String wmxASNDetailSearchQuery= "Select * FROM WX_ASNDTL WHERE EXTERNKEY='" + externkey1 + "'";
			
			
			
			List<Map<String, String>> dbData=db.select_scriptAndQuery(wmxScript, wmxASNDetailSearchQuery);
			
			if (dbData.size()==1) {
				 
				 System.out.println(dbData.size());
				 test.log(Status.PASS, "ASN 1 has 1 details line" );	
				 
			}else {
				
				System.out.println("fail");
			}
			
			
			
		} catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		softAssert.assertAll();
		
	}
	
	
	@Test(priority=11)
	public void validateASN2inWMx() {
		
		String externkey2=CmxData.externalKey+"2";
		try {
			if(searchASNWMx(externkey2)) {
				
				
				test.log(Status.PASS, "ASN 2 propagated to WMx" );	
				
				
			}else {
				
				test.log(Status.PASS, "failed" );
			}
			
			
		}catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		softAssert.assertAll();
		
		
		
		
	}
	
	
	@Test(priority=12)
	public void validateASN2DetailsInWMx() {
		String externkey2=CmxData.externalKey+"2";
		try {
			OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
					WmxData.userid_WMXDB, WmxData.password_WMXDB,
					true);
						
			String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'ONT004'); COMMIT; END; ";
			
			String wmxASNDetailSearchQuery= "Select * FROM WX_ASNDTL WHERE EXTERNKEY='" + externkey2 + "'";
			
			
			
			List<Map<String, String>> dbData=db.select_scriptAndQuery(wmxScript, wmxASNDetailSearchQuery);
			
			if (dbData.size()==3) {
				 
				 System.out.println(dbData.size());
				 test.log(Status.PASS, "ASN 2 has 3 details line" );	
				 
			}else {
				
				System.out.println("fail");
			}
			
			
			
		} catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		softAssert.assertAll();
		
	}

	@Test(priority=13)
	
	public void wmxTestCreateReceiptForASN1() {
		String externkey1=CmxData.externalKey+"1";
		
		try {
			
		Utils.sleep(30000);	
		loginToWMX();
		test.log(Status.PASS, "Logged into the WMx application");
		homeWmx.selectInboundMenu();
		test.log(Status.PASS, "Selected Inbound menu");
		receiptPage.selectReceiptMenu();
		test.log(Status.PASS, "Selected Receipt");
		receiptPage.createReceipt();
		Utils.sleep(5000);
		receiptPage.searchExternKey(externkey1);
		receiptPage.clickOnEditButton();
		Utils.sleep(5000);
		receiptPage.clickOnNewButton();
		Utils.sleep(9000);
		
		receiptPage.setQty(getAsnExpectedQty(externkey1));	
		
		Utils.sleep(5000);
		
		receiptPage.clickOnSaveButton();
		
		Utils.sleep(5000);
		
		receiptPage.receive();
		
		Utils.sleep(60000);
		
		
		
		if(getReceiptStatus(externkey1).equalsIgnoreCase("200") 
			
//////				receiptPage.getStatusFieldValue().contains("200")
				
				
			) {
			System.out.println(getReceiptStatus(externkey1));
			test.log(Status.PASS, "Expected receipt is created and received ");
		}	else{
			reportStatusFail("Expected receipt is not created and received");
		}
		
		
		
		Utils.sleep(3000);
		
		if(getInvRecord(getLPN(externkey1))==true)
	
		
		{
			
			test.log(Status.PASS, "Inventory record is created");
			
		}else {
			reportStatusFail("Expected inv record is not created and received");
			
		}
		
		
//		if(getInvRecord(getDocKey(getLPN(externkey1)))==true)
		
		
		
//		System.out.println(getDocKey("2220205203"))
		
		Utils.sleep(3000);
		
		if(getTaskRecord(getDocKey(getLPN(externkey1)))==true) {
			
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
	
	
	
	
	@Test(priority=14)
	public void testInventoryAfterPutawayForASN1() {
	String externkey1=CmxData.externalKey+"1";
	
	try {
		Utils.sleep(30000);
		loginToWmxM();
		test.log(Status.PASS, "Logged into WMx mobile application");
		Utils.sleep(9000);
		homeWmxM.selectPutaway();
		test.log(Status.PASS, "Selected putaway");
		wmxputaway.selectPutawaySearch();
		test.log(Status.PASS, "Selected putaway search");
		Utils.sleep(5000);
		wmxputaway.setLpn(getLPN(externkey1));
		Utils.sleep(9000);
		wmxputaway.executePutaway();
		Utils.sleep(60000);
		
		
//		OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
//				WmxData.userid_WMXDB, WmxData.password_WMXDB,
//				true);
//		String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'ONT004'); COMMIT; END; ";
//		
//		String wmxInvIdQuery= "Select * FROM WX_INVENTORY WHERE LPN='" + getLPN(externkey1) + "'  " ;
//		
//		List<Map<String, String>> dbData=db.select_scriptAndQuery(wmxScript, wmxInvIdQuery);
//		
//		
//		String qtyAfterPutaway=dbData.get(1).get("QTY");
//		
//		String putawayLoc=dbData.get(1).get("LOC");
//		
//		if(
//				putawayLoc.equalsIgnoreCase("QARE03")&&
//				qtyAfterPutaway.equalsIgnoreCase("10")
//				) {
//			
//			test.log(Status.PASS, "Qty after putaway" + qtyAfterPutaway );
//			test.log(Status.PASS, "Expected inventory created after putaway" );
//			
//		}else {
//			
//			reportStatusFail("Expected inventory is not created after putaway");
//		}
//		
		
	}catch(Exception e) {
		reportStatusFail(e.getMessage());
		e.printStackTrace();
	}
	
	softAssert.assertAll();
	
	
	
	}
	
//
//	
	@Test(priority=15)
	public void checkTaskAfterputaway() {
		String externkey1=CmxData.externalKey+"1";
		
//		getDocKey(getLPN(externkey1)))
		try {
			if(validateTaskRecordAfterPutaway(getDocKey(getLPN(externkey1)))==true) {
				
				test.log(Status.PASS, "Expected task has been removed");
				
				
			}else {
				reportStatusFail("Expected task not removed");
			}
			
		}catch(Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		
		softAssert.assertAll();
		
		
		
	}
	
	
	@Test(priority=16)
	
	public void wmxTestCreateReceiptForASN2() {
		String externkey2=CmxData.externalKey+"2";
		
		try {
			
		Utils.sleep(30000);	
		loginToWMX();
		test.log(Status.PASS, "Logged into the WMx application");
		Utils.sleep(9000);
		homeWmx.selectInboundMenu();
		test.log(Status.PASS, "Selected Inbound menu");
		receiptPage.selectReceiptMenu();
		Utils.sleep(3000);
		test.log(Status.PASS, "Selected Receipt");
		receiptPage.createReceipt();
		Utils.sleep(5000);
////		receiptPage.searchExternKey(externkey1);
		receiptPage.searchExternKey(externkey2);
		receiptPage.clickOnEditButton();
		Utils.sleep(5000);
		receiptPage.clickOnNewButton();
		Utils.sleep(9000);
		
////		receiptPage.setQty(getAsnExpectedQty(externkey1));
		
		receiptPage.createReceiptForASN("10");	
		
		Utils.sleep(5000);
		
		receiptPage.clickOnSaveButton();
		
		Utils.sleep(5000);
		
		receiptPage.receive();
		
		Utils.sleep(60000);
		
		
		
		if(getReceiptStatus(externkey2).equalsIgnoreCase("200") 
			
////////				receiptPage.getStatusFieldValue().contains("200")
				
				
			) {
			System.out.println(getReceiptStatus(externkey2));
			test.log(Status.PASS, "Expected receipt is created and received ");
		}	else{
			reportStatusFail("Expected receipt is not created and received");
		}
		
		
		
		Utils.sleep(3000);
		
	
		
	
	if(getInvRecordforASN2(getLPN(externkey2))==true) {
		
		test.log(Status.PASS, "Inventory record is created");
		
	}else {
		reportStatusFail("Expected inv record is not created and received");
		
	}
	
	
		Utils.sleep(60000);
		
		
		
		if(getTaskRecordforASN2(getDocKey(getLPN(externkey2)))==true) {
			
			test.log(Status.PASS, "Expected task is created");
			
		}else {
			reportStatusFail("Expected task record is not created");
			
		}
		
		
	
	
//	if(getTaskRecordforASN2(getLPN(externkey2))==true) {
//		
//		test.log(Status.PASS, "Expected task is created");
//		
//	}else {
//		reportStatusFail("Expected task record is not created ");
//		
//	}
//		
		
		}catch(Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		softAssert.assertAll();
	}
	
	
	
	@Test(priority=17)
	public void testInventoryAfterPutawayForASN2() {
	String externkey2=CmxData.externalKey+"2";
	
	try {
		Utils.sleep(30000);
		loginToWmxM();
		test.log(Status.PASS, "Logged into WMx mobile application");
		Utils.sleep(9000);
		homeWmxM.selectPutaway();
		test.log(Status.PASS, "Selected putaway");
		wmxputaway.selectPutawaySearch();
		test.log(Status.PASS, "Selected putaway search");
		Utils.sleep(5000);
		wmxputaway.setLpn(getLPN(externkey2));
		Utils.sleep(9000);
		wmxputaway.executePutawayForMultipleLines(getLPN(externkey2),"QARE03");
		Utils.sleep(60000);
		
		
//		OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
//				WmxData.userid_WMXDB, WmxData.password_WMXDB,
//				true);
//		String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'ONT004'); COMMIT; END; ";
//		
//		String wmxInvIdQuery= "Select * FROM WX_INVENTORY WHERE LPN='" + getLPN(externkey2) + "' AND LOC='"+ "QARE03" +"' " ;
//		
//		List<Map<String, String>> dbData=db.select_scriptAndQuery(wmxScript, wmxInvIdQuery);
//		
//		String qtyAfterPutaway0=dbData.get(0).get("QTY");
//		String qtyAfterPutaway1=dbData.get(1).get("QTY");
//		String qtyAfterPutaway2=dbData.get(2).get("QTY");
//		
//		String putawayLoc0=dbData.get(0).get("LOC");
//		String putawayLoc1=dbData.get(1).get("LOC");
//		String putawayLoc2=dbData.get(2).get("LOC");
//		
//		
//		
//		
//		if(
//				qtyAfterPutaway0.equalsIgnoreCase("10")&&
//				qtyAfterPutaway1.equalsIgnoreCase("10")&&
//				qtyAfterPutaway2.equalsIgnoreCase("10")&&
//				putawayLoc0.equalsIgnoreCase("QARE03") &&
//				putawayLoc1.equalsIgnoreCase("QARE03") &&
//				putawayLoc2.equalsIgnoreCase("QARE03") 
//				
//				
//				) {
//			
////			test.log(Status.PASS, "Qty after putaway" + qtyAfterPutaway );
//			test.log(Status.PASS, "Expected inventory created after putaway" );
//			
//		}else {
//			
//			reportStatusFail("Expected inventory is not created after putaway");
//		}
//		
//		
	}catch(Exception e) {
		reportStatusFail(e.getMessage());
		e.printStackTrace();
	}
	
	softAssert.assertAll();
	
	
	
	}
//	
	@Test(priority=18)
	public void checkTaskAfterputaway2() {
		String externkey2=CmxData.externalKey+"2";
		
		
		try {
			if(validateTaskRecordAfterPutaway(getDocKey(getLPN(externkey2)))==true) {
				
				test.log(Status.PASS, "Expected task has been removed");
				
				
			}else {
				reportStatusFail("Expected task not removed");
			}
			
		}catch(Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		
		softAssert.assertAll();
		
		
		
	}
	
	@Test(priority=19)
	public void checkAsnStatusInOMx() {
		String externkey1=CmxData.externalKey+"1";
		String externkey2=CmxData.externalKey+"2";
		Utils.sleep(60000);
		
		try {
			if(
					getAsnStatusOmxDB(externkey1).equalsIgnoreCase("200")&&
					getAsnStatusOmxDB(externkey2).equalsIgnoreCase("200")
					
					) {
				
				test.log(Status.PASS, "ASNs propagated back to OMx and have status 200");
				
				
			}else {
				reportStatusFail("ASN status not set to 200");
			}
			
		}catch(Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		
		softAssert.assertAll();
		
		
		
	}
//
//	@Test(priority=20)
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
//		db.runCmxOutboundReceiptConfirmationLorealJob();
//		
//		System.out.println("Executed CMX OUTBOUND Receipt Confirmation Job through DB");
//		
//		Utils.sleep(7000);
//		
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
//	
//	
//	
//	@Test(priority = 21)
//	public void cmxVerifyOrderConfirmationStatus() {
//		String externkey1=CmxData.externalKey+"1";
//		String externkey2=CmxData.externalKey+"2";
//		
//		try {
//			
//			if(getConfirmtaionMessage(externkey1)==true &&
//					
//					getConfirmtaionMessage(externkey2)==true
//					
//					) {
//				test.log(Status.PASS, "Expected receipt confirmation messages generated in CMX");
//				
//				
//			}else {
//				
//				test.log(Status.PASS, "Expected receipt confirmation messages not generated in CMX");
//				
//			}
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
//	@Test(priority=22)
//	public void comparePayload() {
//		
//		String externkey1=CmxData.externalKey+"1";
//		String externkey2=CmxData.externalKey+"2";
//		
//		
//		try {
//			
//			OracleDB db = new OracleDB(CmxData.serverName_CMXDB, CmxData.port_CMXDB, CmxData.database_CMXDB,
//					CmxData.userid_CMXDB, CmxData.password_CMXDB,
//					true);
//			
//			
//			String sqlCMXValidationQuery = "select id,file_name,status,transmission_type,tp_transmission_type,tp_unique_num, payload from cmxdata.x_transmission  where  TP_UNIQUE_NUM='"
//					+ externkey1 + "' order by id desc";
//			
//			List<Map<String, String>> dbData = db.selectSQL(sqlCMXValidationQuery);
//			
//			String str=dbData.get(0).get("PAYLOAD");
//			
////			File file = new File("C:\\Users\\hsawant\\Desktop\\BeforeCommit\\sc-qa-wmx\\src\\main\\resources\\LorealASN1_ReceiptConfirmationPaylaod.txt");
//			
//			File file = new File(System.getProperty("user.dir") + "/src/main/resources/LorealASN1_ReceiptConfirmationPaylaod.txt");
//			
//			
//			FileWriter fr = new FileWriter(file, false);
//			BufferedWriter br = new BufferedWriter(fr);
//			br.write(str);
//
//			br.close();
//			fr.close();
//			
////			String ReceiptConfirmationPaylaodASN1="C:\\Users\\hsawant\\Desktop\\BeforeCommit\\sc-qa-wmx\\src\\main\\resources\\LorealASN1_ReceiptConfirmationPaylaod.txt";
//			
//			String ReceiptConfirmationPaylaodASN1=System.getProperty("user.dir") + "/src/main/resources/LorealASN1_ReceiptConfirmationPaylaod.txt";
//			
//			
////			String maskPathForASN1="C:\\Users\\hsawant\\Desktop\\BeforeCommit\\sc-qa-wmx\\src\\main\\resources\\MaskForLorealASN1.txt";
//			
//			String maskPathForASN1=System.getProperty("user.dir") + "/src/main/resources/MaskForLorealASN1.txt";
//			
////			String ReceiptConfirmationGoldPaylaodASN1="C:\\Users\\hsawant\\Desktop\\BeforeCommit\\sc-qa-wmx\\src\\main\\resources\\LorealASN1_ReceiptConfirmationGoldPayload.txt";
//			
//			String ReceiptConfirmationGoldPaylaodASN1=System.getProperty("user.dir") + "/src/main/resources/LorealASN1_ReceiptConfirmationGoldPayload.txt";;
//
//			fileCompare.mask(ReceiptConfirmationPaylaodASN1, maskPathForASN1, "FLAT", true);
//			
//			
//			test.log(Status.PASS,
//					"Masked payload file for ASN 1");
//			
//			if(comaprePayload(ReceiptConfirmationPaylaodASN1, ReceiptConfirmationGoldPaylaodASN1)==true) {
//				
//				System.out.println("Two files have same content.");
//		        test.log(Status.PASS,
//						"Payload file comparison successful for ASN1");
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
//			
//			String sqlCMXValidationQuery1 = "select id,file_name,status,transmission_type,tp_transmission_type,tp_unique_num, payload from cmxdata.x_transmission  where  TP_UNIQUE_NUM='"
//					+ externkey2 + "' order by id desc";
//			
//			List<Map<String, String>> dbData1 = db.selectSQL(sqlCMXValidationQuery1);
//			
//			String str2=dbData1.get(0).get("PAYLOAD");
//			
//			File file1 = new File(System.getProperty("user.dir") + "/src/main/resources/LorealASN2_ReceiptConfirmationPaylaod.txt");
//			FileWriter fr1 = new FileWriter(file1, false);
//			BufferedWriter br1 = new BufferedWriter(fr1);
//			br1.write(str2);
//
//			br1.close();
//			fr1.close();
//			
//			String ReceiptConfirmationPaylaodASN2=System.getProperty("user.dir") + "/src/main/resources/LorealASN2_ReceiptConfirmationPaylaod.txt";
//			
//			String maskPathForASN2=System.getProperty("user.dir") + "/src/main/resources/MaskForLorealASN2.txt";
//			
//			String ReceiptConfirmationGoldPaylaodASN2=System.getProperty("user.dir") + "/src/main/resources/LorealASN2_ReceiptConfirmationGoldPayload.txt";
//
//			fileCompare.mask(ReceiptConfirmationPaylaodASN2, maskPathForASN2, "FLAT", true);
//			
//			
//			test.log(Status.PASS,
//					"Masked payload file for ASN 2");
//			
//			if(comaprePayload(ReceiptConfirmationPaylaodASN2, ReceiptConfirmationGoldPaylaodASN2)==true) {
//				
//				System.out.println("Two files have same content.");
//		        test.log(Status.PASS,
//						"Payload file comparison successful for ASN2");
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
//			
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
	
	
//	@Test
//	public void test() {
//		String externkey2=CmxData.externalKey+"2";
//		try {
//			if(getTaskRecordforASN2("2220190210")==true) {
//				
//				test.log(Status.PASS, "Inventory record is created");
//				
//			}else {
//				reportStatusFail("Expected inv record is not created and received");
//				
//			}
//			
//			
//		}catch(Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//		softAssert.assertAll();
//		
//	}
	
	
	public boolean comaprePayload(String fileUnderTest, String goldFile) {
		
		 boolean comparisonSuccess=false;
		
		
		try {
			
			BufferedReader reader1 = new BufferedReader(new FileReader(fileUnderTest));
		    
		    BufferedReader reader2 = new BufferedReader(new FileReader(goldFile));
		     
		    String line1 = reader1.readLine();
		     
		    String line2 = reader2.readLine();
		     
		    boolean areEqual = true;
		     
		    int lineNum = 1;
		    
		    
		    while (line1 != null || line2 != null)
		    {
		        if(line1 == null || line2 == null)
		        {
		            areEqual = false;
		             
		            break;
		        }
		        else if(! line1.equalsIgnoreCase(line2))
		        {
		            areEqual = false;
		             
		            break;
		        }
		         
		        line1 = reader1.readLine();
		         
		        line2 = reader2.readLine();
		         
		        lineNum++;
		    }
		     
		    if(areEqual)
		    {
		        System.out.println("Two files have same content.");
//		        test.log(Status.PASS,
//						"Payload file comparison successful");
		        
		        comparisonSuccess=true;
				
		    }
		    else
		    {
		        System.out.println("Two files have different content. They differ at line "+lineNum);
		         
		        System.out.println("File1 has "+line1+" and File2 has "+line2+" at line "+lineNum);
		        
		        reportStatusFail("File comparison failed");
		    }
		     
		    reader1.close();
		     
		    reader2.close();
			
			
		}catch (Exception e) {
			
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		
		return comparisonSuccess;
	}
	
	
	
	
	
	public boolean insertASNDataRecordCMX() {
		boolean insertionSuccess = false;
		try {
			OracleDB db = new OracleDB(CmxData.serverName_CMXDB, CmxData.port_CMXDB, CmxData.database_CMXDB,
				CmxData.userid_CMXDB, CmxData.password_CMXDB,
				true);

			String sqlInsertQuery = util.setLoreal_ASN1_2Query();
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
	
	
	
	public boolean searchASN(String externkey) {
		boolean flag=false;
		try {
			OracleDB db = new OracleDB(OmxData.serverName_OMXDB, OmxData.port_OMXDB, OmxData.database_OMXDB,
				OmxData.userid_OMXDB, OmxData.password_OMXDB,
				true);
//			String omxStatusQuery = "SELECT STATUS from ORDER_A WHERE EXTERNKEY='" + CmxData.externalKey + "'";
			String orderSearchQuery = "SELECT * from ASN_A WHERE EXTERNKEY='" + externkey + "'";
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
	
	
	
	
	public boolean  validateASNStatus(String externkey) {
		
		boolean flag=false;
		
		try {
			OracleDB db = new OracleDB(OmxData.serverName_OMXDB, OmxData.port_OMXDB, OmxData.database_OMXDB,
				OmxData.userid_OMXDB, OmxData.password_OMXDB,
				true);
//			String omxStatusQuery = "SELECT STATUS from ORDER_A WHERE EXTERNKEY='" + CmxData.externalKey + "'";
			String orderSearchQuery = "SELECT STATUS from ASNDTL_A WHERE EXTERNKEY='" + externkey + "'";
			List<Map<String, String>> dbData = db.selectSQL(orderSearchQuery);
			
			String status = dbData.get(0).get("STATUS"); 
			
			
			System.out.println(status);
			
			if(status.equalsIgnoreCase("101")) {
				
			
				flag=true;
			}else {
				reportStatusFail("Failed");
				
			}
			
			
			
		} catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		return flag;
		
		
	}
	
	

	public boolean searchASNWMx(String externkey) {
	boolean flag=false;
	try {
		OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
				WmxData.userid_WMXDB, WmxData.password_WMXDB,
				true);
					
		String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'ONT004'); COMMIT; END; ";
		
		String wmxOrderSearchQuery= "Select * FROM WX_ASN WHERE EXTERNKEY='" + externkey + "'";
		
		
		
//		String omxStatusQuery = "SELECT STATUS from ORDER_A WHERE EXTERNKEY='" + CmxData.externalKey + "'";
//		String orderSearchQuery = "SELECT * from ORDER_A WHERE EXTERNKEY='" + "externkey" + "'";
//		List<Map<String, String>> dbData = db.selectSQL(orderSearchQuery);
		
		List<Map<String, String>> dbData=db.select_scriptAndQuery(wmxScript, wmxOrderSearchQuery);
		
		if (dbData.size() > 0) {
			 flag= true;
			 
		}else {
			
			System.out.println("fail");
		}
		
		
		
	} catch (Exception e) {
		reportStatusFail(e.getMessage());
		e.printStackTrace();
	}
	return flag;
}

	public boolean searchASNDetails(String externkey) {
		boolean flag=false;
		try {
			OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
					WmxData.userid_WMXDB, WmxData.password_WMXDB,
					true);
						
			String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'ONT004'); COMMIT; END; ";
			
			String wmxOrderDetailSearchQuery= "Select * FROM WX_ASNDTL WHERE EXTERNKEY='" + externkey + "'";
			
			
			
//			String omxStatusQuery = "SELECT STATUS from ORDER_A WHERE EXTERNKEY='" + CmxData.externalKey + "'";
//			String orderSearchQuery = "SELECT * from ORDER_A WHERE EXTERNKEY='" + "externkey" + "'";
//			List<Map<String, String>> dbData = db.selectSQL(orderSearchQuery);
			
			List<Map<String, String>> dbData=db.select_scriptAndQuery(wmxScript, wmxOrderDetailSearchQuery);
			
			if (dbData.size()==6) {
				 flag= true;
				 System.out.println(dbData.size());
				 
			}else {
				
				System.out.println("fail");
			}
			
			
			
		} catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}
	
	
	public void loginToWMX() {
		try {
			TestProperties.getDriver().get(getApplicationURLWMX());
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

	public String getAsnExpectedQty(String externkey) {
		String qty = "";
		try {
			OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
					WmxData.userid_WMXDB, WmxData.password_WMXDB,
					true);
			String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'ONT004'); COMMIT; END; ";
			
			String wmxAsnDtlQtyQuery= "Select EXPECTEDQTY FROM WX_ASNDTL WHERE EXTERNKEY='" + externkey + "'";
			List<Map<String, String>> dbData=db.select_scriptAndQuery(wmxScript, wmxAsnDtlQtyQuery);
			qty=dbData.get(0).get("EXPECTEDQTY");
			
			System.out.println(qty);
			
		}catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		return qty;
	}
	
	
	
public String getReceiptStatus(String externkey) {
		
		String STATUS="";
		try {
			OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
					WmxData.userid_WMXDB, WmxData.password_WMXDB,
					true);
			String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'ONT004'); COMMIT; END; ";
			
			String wmxReceiptStatusQuery= "Select STATUS FROM WX_RECEIPT WHERE EXTERNKEY='" + externkey + "'";
			List<Map<String, String>> dbData=db.select_scriptAndQuery(wmxScript, wmxReceiptStatusQuery);
			STATUS=dbData.get(0).get("STATUS");
			
			System.out.println(STATUS);
			
		}catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		
		return STATUS;
		
	} 



public boolean getInvRecord(String lpn) {
	boolean invRecord=false;
	
	try {
		
//		String LPN=getLPN();
		
		OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
				WmxData.userid_WMXDB, WmxData.password_WMXDB,
				true);
		String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'ONT004'); COMMIT; END; ";
		
		String wmxInvRecordStatusQuery= "Select * FROM WX_INVENTORY WHERE LPN='" + lpn + "'";
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



//
//public String getLPN(String externkey) {
//	
//	String LPN="";
//	try {
//		OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
//				WmxData.userid_WMXDB, WmxData.password_WMXDB,
//				true);
//		String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'ONT004'); COMMIT; END; ";
//
//
//		String wmxLPNQuery= "Select LPN FROM WX_RECEIPTDTL WHERE EXTERNKEY='" + externkey + "'";
//		List<Map<String, String>> dbData=db.select_scriptAndQuery(wmxScript, wmxLPNQuery);
//		LPN=dbData.get(0).get("LPN");
//		
//		System.out.println(LPN);
//		
//	}catch (Exception e) {
//		reportStatusFail(e.getMessage());
//		e.printStackTrace();
//	}
//	
//	return LPN;
//	
//} 





public String getLPN(String externkey) {
	
	String LPN="";
	try {
		OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
				WmxData.userid_WMXDB, WmxData.password_WMXDB,
				true);
		String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'ONT004'); COMMIT; END; ";


		String wmxLPNQuery= "Select LPN FROM WX_RECEIPTDTL WHERE EXTERNKEY='" + externkey+ "'";
		List<Map<String, String>> dbData=db.select_scriptAndQuery(wmxScript, wmxLPNQuery);
		LPN=dbData.get(0).get("LPN");
		
		System.out.println(LPN);
		
	}catch (Exception e) {
		reportStatusFail(e.getMessage());
		e.printStackTrace();
	}
	
	return LPN;
	
} 

	

//public boolean getTaskRecord(String docKey) {
//	boolean taskRecord=false;
//	
//	try {
//		
////		String LPN=getLPN();
//		
//		OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
//				WmxData.userid_WMXDB, WmxData.password_WMXDB,
//				true);
//		String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'ONT004'); COMMIT; END; ";
//		
//		String wmxTaskRecordStatusQuery= "Select * FROM WX_TASK WHERE DOCKEY='" + docKey + "'";
//		List<Map<String, String>> dbData=db.select_scriptAndQuery(wmxScript, wmxTaskRecordStatusQuery);
//		
//		if (dbData.size() > 0) {
//			taskRecord = true;
//		}else {
//			
//			System.out.println("fail");
//		}
//		
//		
//		
//	}catch (Exception e) {
//		reportStatusFail(e.getMessage());
//		e.printStackTrace();
//		
//	}
//	
//	return taskRecord;
//	
//}




public boolean getTaskRecord(String docKey) {
	boolean taskRecord=false;
	
	try {
		
//		String LPN=getLPN();
		
		OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
				WmxData.userid_WMXDB, WmxData.password_WMXDB,
				true);
		String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'ONT004'); COMMIT; END; ";
		
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
		TestProperties.getDriver().get(getApplicationURLWMXM());
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




public boolean validateTaskRecordAfterPutaway(String docKey) {
	boolean taskRecord=false;
	
	try {
		
//		String LPN=getLPN();
		
		OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
				WmxData.userid_WMXDB, WmxData.password_WMXDB,
				true);
		String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'ONT004'); COMMIT; END; ";
		
		String wmxTaskRecordStatusQuery= "Select * FROM WX_TASK WHERE DOCKEY='" + docKey + "'";
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


public boolean getInvRecordforASN2(String lpn) {
	boolean invRecord=false;
	
	try {
		
//		String LPN=getLPN();
		
		OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
				WmxData.userid_WMXDB, WmxData.password_WMXDB,
				true);
		String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'ONT004'); COMMIT; END; ";
		
		String wmxInvRecordStatusQuery= "Select * FROM WX_INVENTORY WHERE LPN='" + lpn + "'AND LOC='"+ "STAGE" +"'";
		List<Map<String, String>> dbData=db.select_scriptAndQuery(wmxScript, wmxInvRecordStatusQuery);
		
		
		
		if (dbData.size()==3) {
			System.out.println(dbData.size());
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




//public boolean getTaskRecordforASN2(String DOCKey) {
//	boolean taskRecord=false;
//	
//	try {
//		
////		String LPN=getLPN();
//		
//		OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
//				WmxData.userid_WMXDB, WmxData.password_WMXDB,
//				true);
//		String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'ONT004'); COMMIT; END; ";
//		
//		String wmxTaskRecordStatusQuery= "Select DOCKEY FROM WX_TASK WHERE DOCKEY='" + DOCKey + "'";
//		List<Map<String, String>> dbData=db.select_scriptAndQuery(wmxScript, wmxTaskRecordStatusQuery);
//		
//		if (dbData.size() == 3) {
//			
//			System.out.println(dbData.size());
//			taskRecord = true;
//		}else {
//			
//			System.out.println("fail");
//		}
//		
//		
//		
//	}catch (Exception e) {
//		reportStatusFail(e.getMessage());
//		e.printStackTrace();
//		
//	}
//	
//	return taskRecord;
//	
//}





public boolean getTaskRecordforASN2(String docKey) {
	boolean taskRecord=false;
	
	try {
		
//		String LPN=getLPN();
		
		OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
				WmxData.userid_WMXDB, WmxData.password_WMXDB,
				true);
		String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'ONT004'); COMMIT; END; ";
		
		String wmxTaskRecordStatusQuery= "Select DOCKEY FROM WX_TASK WHERE DOCKEY='" + docKey + "'";
		List<Map<String, String>> dbData=db.select_scriptAndQuery(wmxScript, wmxTaskRecordStatusQuery);
		
		if (dbData.size() == 3) {
			
			System.out.println(dbData.size());
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



public String getAsnStatusOmxDB(String externkey) {
	String status = "";
	try {
		OracleDB db = new OracleDB(OmxData.serverName_OMXDB, OmxData.port_OMXDB, OmxData.database_OMXDB,
			OmxData.userid_OMXDB, OmxData.password_OMXDB,
			true);
//		String omxStatusQuery = "SELECT STATUS from ORDER_A WHERE EXTERNKEY='" + CmxData.externalKey + "'";
		String asnStatusQuery = "SELECT STATUS from ASN_A WHERE EXTERNKEY='" + externkey + "'";
		List<Map<String, String>> dbData = db.selectSQL(asnStatusQuery);
		status = dbData.get(0).get("STATUS");
		System.out.println("Status in OMX DB: " + status);
	} catch (Exception e) {
		reportStatusFail(e.getMessage());
		e.printStackTrace();
	}
	return status;
}


public boolean getConfirmtaionMessage(String externkey) {
	boolean flag=false;
	
	
	try {
		
		OracleDB db = new OracleDB(CmxData.serverName_CMXDB, CmxData.port_CMXDB, CmxData.database_CMXDB,
				CmxData.userid_CMXDB, CmxData.password_CMXDB,
				true);
			
			Utils.sleep(120000);
				
			
			String sqlCMXValidationQuery = "select id,file_name,status,transmission_type,tp_transmission_type,tp_unique_num from cmxdata.x_transmission  where  TP_UNIQUE_NUM='"
				+ externkey + "' order by id desc";

			
			List<Map<String, String>> dbData = db.selectSQL(sqlCMXValidationQuery);
			System.out.println(dbData.get(0).get("TRANSMISSION_TYPE"));
			if (dbData.get(0).get("TRANSMISSION_TYPE").equals("RECEIPT_CONFIRMATION")) {
				
				
				test.log(Status.PASS,
					"Expected ASN Receipt Confirmation record is generated in CMx DB");
				
				flag=true;
			} else {
				reportStatusFail("Expected ASN Receipt Confirmation record is not generated in CMx DB");
			}
		} catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
	
	return flag;
	}



	
	
//public String getDocKey(String lpn) {
//		
//		String DockKey="";
//		try {
//			OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
//					WmxData.userid_WMXDB, WmxData.password_WMXDB,
//					true);
//			String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'ONT004'); COMMIT; END; ";
//
//
//			String wmxLPNQuery= "Select RECEIPTKEY FROM WX_RECEIPTDTL WHERE LPN='" + lpn + "'";
//			List<Map<String, String>> dbData=db.select_scriptAndQuery(wmxScript, wmxLPNQuery);
//			DockKey=dbData.get(0).get("receiptkey");
//			
//			System.out.println(DockKey);
//			
//		}catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//		
//		return DockKey;
//		
//	}	



public String getDocKey(String lpn) {
		
		String DockKey="";
		try {
			OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
					WmxData.userid_WMXDB, WmxData.password_WMXDB,
					true);
			String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'ONT004'); COMMIT; END; ";


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

	






	
	public void reportStatusFail(String failLog) {
		test.log(Status.FAIL, failLog);
		failure_Steps = failure_Steps + 1;
		softAssert.fail(failLog);
		softAssert.assertTrue(false);
		captureScreenshot("failure");
	}

	
	
	
	}