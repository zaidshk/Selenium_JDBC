package xpo.qa.sc.wmx.tests;


import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import xpo.qa.common.Utils;

import org.openqa.selenium.chrome.ChromeOptions;
import org.sql2o.logging.SysOutLogger;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rallydev.rest.RallyRestApi;
import com.rallydev.rest.request.CreateRequest;
import com.rallydev.rest.request.QueryRequest;
import com.rallydev.rest.response.CreateResponse;
import com.rallydev.rest.response.QueryResponse;
import com.rallydev.rest.util.Fetch;
import com.rallydev.rest.util.QueryFilter;

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
import xpo.qa.sc.wmx.utilities.WMXDBUtils;
import xpo.qa.sc.wmx.utilities.WmxUtil;
import xpo.qa.sc.wmx.utilities.OracleDB;




public class BusinessRule  extends TestBase{
	
	
	
	
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
	public void afterMethod(ITestResult result) throws IOException, URISyntaxException {
		if (result.getStatus() == ITestResult.FAILURE) {
			captureScreenshot("failure");
		}
		if (failure_Steps > 0) {
			test.log(Status.FAIL, "Test is Failed ");
			
		}
		failure_Steps = 0;
		try {
			
//			
			
			
			
		
			
			TestProperties.getExtentReports().flush();
			
		} catch (Exception e) {
			test.log(Status.FAIL, "Error during close, no tests executed to report.  No ExtentReport generated.");
		}
		close();
	}
	
	
	@AfterTest(alwaysRun = true)
	public void dispalyExecutionResult() throws IOException, URISyntaxException {
		manager = DriverManagerFactory.getManager(DriverManagerType.CHROME);
		ChromeOptions options = new ChromeOptions();
		manager.createDriver(options);
		TestProperties.getDriver().get(System.getProperty("user.dir") + "\\test-output\\log\\ExtentReports.html");
		
		ITestResult result = Reporter.getCurrentTestResult();
		
		System.out.println("Status" + result.getStatus());

		
		
		
//		
			
			
		
		
		
		
		
	}
	

	
	
	
//	@AfterClass
//	public void updateRally() throws IOException, URISyntaxException {
//		
////		ITestResult result = Reporter.getCurrentTestResult();
////		
////		System.out.println("Status" + result.getStatus());
//		
////		
//			
//			
////		String host = "https://rally.xposc.com";
////        String username = "hsawant";
////        String password = "Harsh12!";
////        String wsapiVersion = "v2.0";
//////        String projectRef = "/project/2222";      
////        String workspaceRef = "/workspace/39200891d"; 
////        String applicationName = "RestExample_AddTCR";
////		
////        RallyRestApi restApi = new RallyRestApi(
////        		new URI(host),
////        		username,
////        		password);
////        restApi.setWsapiVersion(wsapiVersion);
////        restApi.setApplicationName(applicationName);   
////        
////        
//////        39200891d -project
////        
////        
////        
////        QueryRequest userRequest = new QueryRequest("User");
////        userRequest.setFetch(new Fetch("UserName", "Subscription", "DisplayName", "SubscriptionAdmin"));
////        userRequest.setQueryFilter(new QueryFilter("UserName", "=", "harshad.sawant@xpo.com"));
////        QueryResponse userQueryResponse = restApi.query(userRequest);
////        JsonArray userQueryResults = userQueryResponse.getResults();
////        JsonElement userQueryElement = userQueryResults.get(0);
////        JsonObject userQueryObject = userQueryElement.getAsJsonObject();
////        String userRef = userQueryObject.get("_ref").getAsString();  
////        System.out.println(userRef);
////        
////        
////        
////        
////        
////        QueryRequest testCaseRequest = new QueryRequest("TestCase");
////        testCaseRequest.setFetch(new Fetch("FormattedID","Name"));
////        testCaseRequest.setWorkspace(workspaceRef);
////        testCaseRequest.setQueryFilter(new QueryFilter("FormattedID", "=", "TC17673"));
////        QueryResponse testCaseQueryResponse = restApi.query(testCaseRequest);
////        JsonObject testCaseJsonObject = testCaseQueryResponse.getResults().get(0).getAsJsonObject();
////        String testCaseRef = testCaseQueryResponse.getResults().get(0).getAsJsonObject().get("_ref").getAsString(); 
////        System.out.println(testCaseRef);
////       
////        
////        JsonObject newTestCaseResult = new JsonObject();
////        newTestCaseResult.addProperty("Verdict", "Pass");
////        java.util.Date date= new java.util.Date();
////        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
////        String timestamp = sdf.format(date);
////
////        newTestCaseResult.addProperty("Date", timestamp);
////        newTestCaseResult.addProperty("Build", 19.8);
////        newTestCaseResult.addProperty("Notes", "Selenium Automated Test Run");
////        newTestCaseResult.addProperty("TestCase", testCaseRef);
////                       
////        CreateRequest createRequest = new CreateRequest("testcaseresult", newTestCaseResult);
////        CreateResponse createResponse = restApi.create(createRequest);
////		
////		
////		restApi.close();
//			
//		
//	}
//	
	
	@Test(priority=1)
	public void insertBusinessRuleTestTemplate() {
		
		try {
			
//			if ( false) {
			if (insertBusinessRuleRecordCMX()) {
				test.log(Status.PASS, "Successfully Inserted template into CMXDATA.X_TRANSMISSION");
			} else {
				reportStatusFail("Error in Inserting  Record into CMXDATA.X_TRANSMISSION");
			}
		} catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		
		
//		ITestResult result= Reporter.getCurrentTestResult();
//		
//		System.out.println(result.getStatus());
		
		softAssert.assertAll();
		
	}

	@Test(priority=2)
	public void runJobs() {
		
		try {
			
			db.runCMxInboundImportLorealJob();
			
			test.log(Status.PASS, "Successfully ran CMx Inbound Import job");
			
			Utils.sleep(9000);
			
			db.runLorealOutboundOrderImportJob();
			
			test.log(Status.PASS, "Successfully ran Outbound Order Import Job");
			
			Utils.sleep(120000);
			
			db.runLorealBOPJob();
			
			Utils.sleep(120000);
			
			test.log(Status.PASS, "Successfully ran Outbound BOP Job");
			
			Utils.sleep(9000);
			
		}catch(Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		softAssert.assertAll();
	}
//	
	@Test(priority=3)
	public void checkOrder1() {
		
		Utils.sleep(9000);
		
		String externkey1=CmxData.externalKey+"1";
		
		System.out.println(externkey1);
		try {
		if(searchOrder(externkey1)) {
			
			
			test.log(Status.PASS, "Expected order with externkey " +  externkey1 + "loaded in OMx " );
			
			
		}else {
			
			reportStatusFail("Failed");
			
		}
		
		
		}catch(Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		softAssert.assertAll();
	}
//
		@Test(priority=4)
	public void checkOrderDetailsLineforOrder1() {
		String externkey1=CmxData.externalKey+"1";
		try {
			if(searchOrderDetailforOrder(externkey1)) {
				
				
				test.log(Status.PASS, "Order 1 has 6 order details lines" );
				
				
			}else {
				
				reportStatusFail("Failed");
				
			}
		}catch(Exception e) {
				reportStatusFail(e.getMessage());
				e.printStackTrace();
			}
		
		softAssert.assertAll();
	
	}	
//	
//	
//	
//		
	@Test(priority=5)
	public void validateOrderStatusforOrder1() {
		String externkey1=CmxData.externalKey+"1";
		
		Utils.sleep(180000);
		
		try {
			if(validateOrderStatus(externkey1)) {
				
				
				test.log(Status.PASS, "Status of order 1 in OMx is either 101 or 80" );
				
				
			}else {
				
				reportStatusFail("Failed");
				
			}
		}catch(Exception e) {
				reportStatusFail(e.getMessage());
				e.printStackTrace();
			}
		
		softAssert.assertAll();
	
	}	
//	
//	
	@Test(priority=6)
	public void validateAllocCodeforOrder1() {
		String externkey1=CmxData.externalKey+"1";
		try {
			if(
					getAllocCode(externkey1,1).equalsIgnoreCase("1560_AMZ") &&
					getAllocCode(externkey1,2).equalsIgnoreCase("STD") &&
					getAllocCode(externkey1,3).equalsIgnoreCase("1560_AMZ") &&
					getAllocCode(externkey1,4).equalsIgnoreCase("STD") &&
					getAllocCode(externkey1,5).equalsIgnoreCase("STD") &&
					getAllocCode(externkey1,6).equalsIgnoreCase("1560_AMZ")
					)
			
			
			{
				
				
				test.log(Status.PASS, "Alloc Codes for details line 1,3,6 are set to 1560_AMZ and for 2,4,5 are set to STD " );
				
				
			}else {
				
				reportStatusFail("Failed");
				
			}
		}catch(Exception e) {
				reportStatusFail(e.getMessage());
				e.printStackTrace();
			}
		
		softAssert.assertAll();
	
	}	
//	
//	
//	
//	
//	
//	
	@Test(priority=7)
	public void checkOrder2() {
		
		String externkey2=CmxData.externalKey+"2";
		
		System.out.println(externkey2);
		try {
		if(searchOrder(externkey2)) {
			
			
			test.log(Status.PASS, "Expected order with externkey " +  externkey2 + " loaded in OMx" );
		}else {
			
			reportStatusFail("Failed");
			
		}
		
		
		
		}catch(Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		softAssert.assertAll();
	}
//	
//	
	@Test(priority=8)
	public void validateOrderStatusforOrder2() {
		String externkey2=CmxData.externalKey+"2";
		try {
			if(validateOrderStatus(externkey2)) {
				
				
				test.log(Status.PASS, "Status of order 2  in OMx is either 101 or 80" );
				
				
			}else {
				
				reportStatusFail("Failed");
				
			}
		}catch(Exception e) {
				reportStatusFail(e.getMessage());
				e.printStackTrace();
			}
		
		softAssert.assertAll();
	
	}	
//	
//	
//	
//
	@Test(priority=9)
	public void checkOrderDetailsLineforOrder2() {
		
		String externkey2=CmxData.externalKey+"2";
		
		System.out.println(externkey2);
		try {
			if(searchOrderDetailforOrder(externkey2)==true) {
				
				
				test.log(Status.PASS, "Order 2 has 6 order details lines" );	
				
				
			}else {
				
				reportStatusFail("Failed");
			}
		
		
		
		}catch(Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		softAssert.assertAll();
	}
////	
//	
//	
//	
	@Test(priority=10)
	public void validateAllocCodeforOrder2() {
		String externkey1=CmxData.externalKey+"2";
		try {
			if(
					getAllocCode(externkey1,1).equalsIgnoreCase("STD") &&
					getAllocCode(externkey1,2).equalsIgnoreCase("STD") &&
					getAllocCode(externkey1,3).equalsIgnoreCase("STD") &&
					getAllocCode(externkey1,4).equalsIgnoreCase("STD") &&
					getAllocCode(externkey1,5).equalsIgnoreCase("STD") &&
					getAllocCode(externkey1,6).equalsIgnoreCase("STD")
					)
			
			
			{
				
				
				test.log(Status.PASS, "Alloc Codes for details line 1,2,3,4,5,6 are set to STD " );
				
				
			}else {
				
				reportStatusFail("Failed");
				
			}
		}catch(Exception e) {
				reportStatusFail(e.getMessage());
				e.printStackTrace();
			}
		
		softAssert.assertAll();
	
	}	
//	
//	
//	
	@Test(priority=11)
	public void validateOrder1InWMx() {
		String externkey1=CmxData.externalKey+"1";
		try {
			if(searchOrderWMx(externkey1)) {
				
				
				test.log(Status.PASS, "Order 1 propagated to WMx" );	
				
				
			}else {
				
				test.log(Status.PASS, "failed" );
			}
			
			
		}catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		softAssert.assertAll();
		
	}
//	
//	
//	
//	
//	
//	
//	
	@Test(priority=12)
	public void validateOrder1DetailsInWMx() {
		String externkey1=CmxData.externalKey+"1";
		try {
			if(searchOrderDetails(externkey1)) {
				
				
				test.log(Status.PASS, "Order 1 has 6 order details" );	
				
				
			}else {
				
				test.log(Status.PASS, "failed" );
			}
			
			
		}catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		softAssert.assertAll();
		
	}
//	
//	
//	
////	getAllocCodeWMx
//	
//	
	@Test(priority=13)
	public void validateOrder1DetailsAllocCodeInWMx() {
		String externkey1=CmxData.externalKey+"1";
		try {
			if(
					getAllocCodeWMx(externkey1,1).equalsIgnoreCase("1560_AMZ") &&
					getAllocCodeWMx(externkey1,2).equalsIgnoreCase("STD") &&
					getAllocCodeWMx(externkey1,3).equalsIgnoreCase("1560_AMZ") &&
					getAllocCodeWMx(externkey1,4).equalsIgnoreCase("STD") &&
					getAllocCodeWMx(externkey1,5).equalsIgnoreCase("STD") &&
					getAllocCodeWMx(externkey1,6).equalsIgnoreCase("1560_AMZ")
					)
			
			
			{
				
				
				test.log(Status.PASS, "Alloc Codes for details line 1,3,6 are set to 1560_AMZ and for 2,4,5 are set to STD " );
				
				
			}else {
				
				reportStatusFail("Failed");
				
			}
			
			
		}catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		softAssert.assertAll();
		
	}
	
//	
//	
//	
//	
	@Test(priority=14)
	public void validateOrder2InWMx() {
		String externkey2=CmxData.externalKey+"2";
		try {
			if(searchOrderWMx(externkey2)) {
				
				
				test.log(Status.PASS, "Order 2 propagated to WMx" );	
				
				
			}else {
				
				test.log(Status.PASS, "failed" );
			}
			
			
		}catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		softAssert.assertAll();
		
	}
//	
//		
	@Test(priority=15)
	public void validateOrder2DetailsInWMx() {
		String externkey2=CmxData.externalKey+"2";
		try {
			if(searchOrderDetails(externkey2)) {
				
				
				test.log(Status.PASS, "Order 2 has 6 order details" );	
				
				
			}else {
				
				test.log(Status.PASS, "failed" );
			}
			
			
		}catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		softAssert.assertAll();
		
	}
//	
//	
//	
//	
	@Test(priority=16)
	public void validateOrder2DetailsAllocCodeInWMx() {
		String externkey2=CmxData.externalKey+"2";
		try {
			if(
					getAllocCodeWMx(externkey2,1).equalsIgnoreCase("STD") &&
					getAllocCodeWMx(externkey2,2).equalsIgnoreCase("STD") &&
					getAllocCodeWMx(externkey2,3).equalsIgnoreCase("STD") &&
					getAllocCodeWMx(externkey2,4).equalsIgnoreCase("STD") &&
					getAllocCodeWMx(externkey2,5).equalsIgnoreCase("STD") &&
					getAllocCodeWMx(externkey2,6).equalsIgnoreCase("STD")
					)
			
			
			{
				
				
				test.log(Status.PASS, "Alloc Codes for details line 1,2,3,4,5,6 are set to STD" );
				
				
			}else {
				
				reportStatusFail("Failed");
				
			}
			
			
		}catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		softAssert.assertAll();
		
	}
	
	
	
	
	
//	@Test
//	public void test() {
//		
//		String externkey2=CmxData.externalKey+"2";
//		
//		try {
//			if(searchOrderWMx(externkey2)) {
//				
//				
//				test.log(Status.PASS, "Order loaded in WMx" );	
//				
//				
//			}else {
//				
//				test.log(Status.PASS, "failed" );
//			}
//			
//			
//		}catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//		softAssert.assertAll();
//		
//	}
//	
	
//	@Test
//	public void testA() {
//		
//		
//		String  code=getAllocCode("QAXF91001MA1", "1");
//		
//		System.out.println(code);
//		
//		
//		
//		
//		
//	}
	

	
	
	public boolean insertBusinessRuleRecordCMX() {
		boolean insertionSuccess = false;
		try {
			OracleDB db = new OracleDB(CmxData.serverName_CMXDB, CmxData.port_CMXDB, CmxData.database_CMXDB,
				CmxData.userid_CMXDB, CmxData.password_CMXDB,
				true);

			String sqlInsertQuery = util.setBRQuery();
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
	

	
	
	public boolean searchOrder(String externkey) {
		boolean flag=false;
		try {
			OracleDB db = new OracleDB(OmxData.serverName_OMXDB, OmxData.port_OMXDB, OmxData.database_OMXDB,
				OmxData.userid_OMXDB, OmxData.password_OMXDB,
				true);
//			String omxStatusQuery = "SELECT STATUS from ORDER_A WHERE EXTERNKEY='" + CmxData.externalKey + "'";
			String orderSearchQuery = "SELECT * from ORDER_A WHERE EXTERNKEY='" + externkey + "'";
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
	
	


	
	
	public boolean searchOrderDetailforOrder(String externkey) {
		boolean flag=false;
		try {
			OracleDB db = new OracleDB(OmxData.serverName_OMXDB, OmxData.port_OMXDB, OmxData.database_OMXDB,
				OmxData.userid_OMXDB, OmxData.password_OMXDB,
				true);
//			String omxStatusQuery = "SELECT STATUS from ORDER_A WHERE EXTERNKEY='" + CmxData.externalKey + "'";
			String orderSearchQuery = "SELECT * from ORDERDTL_A WHERE EXTERNKEY='" + externkey + "'";
			List<Map<String, String>> dbData = db.selectSQL(orderSearchQuery);
			
			
			
			int size=dbData.size();
			System.out.println(size);
			
			if(dbData.size()==6) {
				
			
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
	
	
	public boolean  validateOrderStatus(String externkey) {
		
		boolean flag=false;
		
		try {
			OracleDB db = new OracleDB(OmxData.serverName_OMXDB, OmxData.port_OMXDB, OmxData.database_OMXDB,
				OmxData.userid_OMXDB, OmxData.password_OMXDB,
				true);
//			String omxStatusQuery = "SELECT STATUS from ORDER_A WHERE EXTERNKEY='" + CmxData.externalKey + "'";
			String orderSearchQuery = "SELECT STATUS from ORDERDTL_A WHERE EXTERNKEY='" + externkey + "'";
			List<Map<String, String>> dbData = db.selectSQL(orderSearchQuery);
			
			String status = dbData.get(0).get("STATUS"); 
			
			
			System.out.println(status);
			
			if(status.equalsIgnoreCase("101")||status.equalsIgnoreCase("80")) {
				
			
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
	
	
	
	public String getAllocCode(String externkey, int lineNumber) {
		
		String allocCode=null;
		
		
		try {
			OracleDB db = new OracleDB(OmxData.serverName_OMXDB, OmxData.port_OMXDB, OmxData.database_OMXDB,
				OmxData.userid_OMXDB, OmxData.password_OMXDB,
				true);
			
			
			
			String allocSearchQuery = "SELECT ALLOCCODE from ORDERDTL_A WHERE EXTERNKEY='" + externkey + "'   AND orderlineno=' " + lineNumber +" ' ";
			
			List<Map<String, String>> dbData = db.selectSQL(allocSearchQuery);
			
			allocCode=dbData.get(0).get("ALLOCCODE");
			
			System.out.println(allocCode);
			
			
		}
		
		
		catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		return allocCode;
		
		
		
	}
	
	
	public boolean searchOrderWMx(String externkey) {
		boolean flag=false;
		try {
			OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
					WmxData.userid_WMXDB, WmxData.password_WMXDB,
					true);
						
			String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'ONT004'); COMMIT; END; ";
			
			String wmxOrderSearchQuery= "Select * FROM WX_ORDER WHERE EXTERNKEY='" + externkey + "'";
			
			
			
//			String omxStatusQuery = "SELECT STATUS from ORDER_A WHERE EXTERNKEY='" + CmxData.externalKey + "'";
//			String orderSearchQuery = "SELECT * from ORDER_A WHERE EXTERNKEY='" + "externkey" + "'";
//			List<Map<String, String>> dbData = db.selectSQL(orderSearchQuery);
			
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
	
	
	public boolean searchOrderDetails(String externkey) {
		boolean flag=false;
		try {
			OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
					WmxData.userid_WMXDB, WmxData.password_WMXDB,
					true);
						
			String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'ONT004'); COMMIT; END; ";
			
			String wmxOrderDetailSearchQuery= "Select * FROM WX_ORDERDTL WHERE EXTERNKEY='" + externkey + "'";
			
			
			
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
	
	
	public String getAllocCodeWMx(String externkey, int lineNumber) {
		
		String allocCode=null;
		
		
		try {
			OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
					WmxData.userid_WMXDB, WmxData.password_WMXDB,
					true);
						
			String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'ONT004'); COMMIT; END; ";
			
			String allocSearchQuery = "SELECT ALLOCCODE from WX_ORDERDTL WHERE EXTERNKEY='" + externkey + "'   AND orderlineno=' " + lineNumber +" ' ";
			
			List<Map<String, String>> dbData=db.select_scriptAndQuery(wmxScript, allocSearchQuery);
			
			allocCode=dbData.get(0).get("ALLOCCODE");
			
			System.out.println(allocCode);
			
			
		}
		
		
		catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		return allocCode;
		
		
		
	}
	
	
	
	
	
	
//	public boolean searchOrderDetailforOrder2(String externkey) {
//		boolean flag=false;
//		try {
//			OracleDB db = new OracleDB(OmxData.serverName_OMXDB, OmxData.port_OMXDB, OmxData.database_OMXDB,
//				OmxData.userid_OMXDB, OmxData.password_OMXDB,
//				true);
////			String omxStatusQuery = "SELECT STATUS from ORDER_A WHERE EXTERNKEY='" + CmxData.externalKey + "'";
//			String orderSearchQuery = "SELECT * from ORDERDTL_A WHERE EXTERNKEY='" + "externkey" + "'";
//			List<Map<String, String>> dbData = db.selectSQL(orderSearchQuery);
//			
//			if(		
////					dbData.get(0).get("ALLOCCODE").equalsIgnoreCase("STD") &&
////					dbData.get(1).get("ALLOCCODE").equalsIgnoreCase("STD")&& 
//				
////					dbData.get(2).get("ALLOCCODE").equalsIgnoreCase("STD")
////					&&
//					dbData.get(3).get("ALLOCCODE").equalsIgnoreCase("STD") &&
//					dbData.get(4).get("ALLOCCODE").equalsIgnoreCase("STD") &&
//					dbData.get(5).get("ALLOCCODE").equalsIgnoreCase("STD") ) 
//				{
//				
//				test.log(Status.PASS, "Alloc codes for all order details are set to STD");
//				
//			}
//		
//			
//			
//			
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//		return flag;
//	}
//	
	
	
	
//	public boolean searchOrderDetailforOrder(String externkey) {
//		boolean flag=false;
//		try {
//			OracleDB db = new OracleDB(OmxData.serverName_OMXDB, OmxData.port_OMXDB, OmxData.database_OMXDB,
//				OmxData.userid_OMXDB, OmxData.password_OMXDB,
//				true);
////			String omxStatusQuery = "SELECT STATUS from ORDER_A WHERE EXTERNKEY='" + CmxData.externalKey + "'";
//			String orderSearchQuery = "SELECT * from ORDERDTL_A WHERE EXTERNKEY='" + "QAXF91001LA2" + "'";
//			List<Map<String, String>> dbData = db.selectSQL(orderSearchQuery);
//			
//			
//			
//			int size=dbData.size();
//			System.out.println(size);
//			
//			if(dbData.size()==6) {
//				
//			
//				flag=true;
//			}
//			
//			
//			
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//		return flag;
//	}
	
	
	
	
	
	

}
