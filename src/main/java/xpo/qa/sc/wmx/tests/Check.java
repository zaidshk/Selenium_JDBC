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
public class Check extends TestBase{
	
	
	
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
	
	
		
		public boolean insertASNDataRecordCMX() {
			boolean insertionSuccess = false;
			try {
				OracleDB db = new OracleDB(CmxData.serverName_CMXDB, CmxData.port_CMXDB, CmxData.database_CMXDB,
					CmxData.userid_CMXDB, CmxData.password_CMXDB,
					true);

				String sqlInsertQuery = util.setLaerdal_QAXA_Query();
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
	

}
