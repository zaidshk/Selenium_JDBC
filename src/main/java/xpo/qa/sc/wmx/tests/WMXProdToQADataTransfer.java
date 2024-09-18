package xpo.qa.sc.wmx.tests;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.databind.ObjectMapper;

import xpo.qa.common.DateUtil;
import xpo.qa.common.FileUtils;
import xpo.qa.common.databases.oracle.OracleDB;
import xpo.qa.common.excel.ExcelUtil;
import xpo.qa.common.selenium.DriverManager;
import xpo.qa.common.selenium.DriverManagerFactory;
import xpo.qa.common.selenium.DriverManagerType;
import xpo.qa.common.selenium.TestProperties;
import xpo.qa.sc.omx.data.OmxData;
import xpo.qa.sc.omx.pages.OmxHomePage;
import xpo.qa.sc.omx.pages.OmxJobsViewPage;
import xpo.qa.sc.omx.pages.OmxLoginPage;
import xpo.qa.sc.omx.pages.OmxOrderMaintenancePage;
import xpo.qa.sc.omx.pages.OmxOrderSearchPage;
import xpo.qa.sc.wmx.data.CmxData;
import xpo.qa.sc.wmx.data.WmxData;
import xpo.qa.sc.wmx.m.pages.WmxMDropIdInfoPage;
import xpo.qa.sc.wmx.m.pages.WmxMHomePage;
import xpo.qa.sc.wmx.m.pages.WmxMLoginPage;
import xpo.qa.sc.wmx.m.pages.WmxMPickByOrderPage;
import xpo.qa.sc.wmx.m.pages.WmxMPickOrderToDropId;
import xpo.qa.sc.wmx.m.pages.WmxMPickPage;
import xpo.qa.sc.wmx.pages.WmxEditMBOL;
import xpo.qa.sc.wmx.pages.WmxEditOrder;
import xpo.qa.sc.wmx.pages.WmxHomePage;
import xpo.qa.sc.wmx.pages.WmxLoginPage;
import xpo.qa.sc.wmx.pages.WmxMBOLPage;
import xpo.qa.sc.wmx.pages.WmxOrderSearchPage;
import xpo.qa.sc.wmx.pages.WmxVerificationPage;
import xpo.qa.sc.wmx.utilities.WMXDataBaseUtils;
import xpo.qa.sc.wmx.utilities.WmxUtil;

/**
 * @author Neha Kedari
 *
 */
public class WMXProdToQADataTransfer extends TestBase {

	private DriverManager manager;
	private WmxData wmxData;
	private CmxData cmxData;
	private OmxData omxData;
	private WmxUtil util;
	private ExtentTest test;
	private int failure_Steps;
	private SoftAssert softAssert;
	ResultSet rs1;
	PreparedStatement pstmt;
	public static int noOfDay=07;
	int count=0,result;

	
	public void initialSetUp(String scriptName) {
		wmxData = new WmxData();
		cmxData = new CmxData();
		omxData = new OmxData();
		util = new WmxUtil();

		

		test = TestProperties.getExtentReports().createTest((this.getClass().getSimpleName() + "::" + scriptName),
			scriptName);
		test.assignCategory("DataBase Testing");
		test.assignAuthor("Neha Kedari");
		softAssert = new SoftAssert();
	}

	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) 
	{
		
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

	
	/**
	 * Test to download data from OMX production table SCHEDULER.export_queue and insert into OMX QA table MPAUL.export_queue_temp
	 * * @throws IOException
	 */
	@Test()
	public void omxDBDownloadProdToQa() throws IOException 
	{
		
		try {
			if (OMXProdToQaDataTransfer.copyGapDataRecordOMX()) {
				test.log(Status.PASS, "Successfully Inserted Record from OMX Prod(SCHEDULER.export_queue) to OMX QA(MPAUL.export_queue_temp)");
			} else {
				reportStatusFail("Error in downloading Records into OMX QA(MPAUL.export_queue_temp)");
			}
		} catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		softAssert.assertAll();
	}
	
	
	/**
	 * Test to download data from CMX production table cmxdata.x_transmission and insert into OMX QA table MPAUL.x_transmission_temp.
	 * * @throws IOException
	 */
	@Test()
	public void cmxDBDownloadProdToQa() throws IOException 
	{
		
		try {
			if (CMXProdToQaDataTransfer.copyGapDataRecordCMX()) {
				test.log(Status.PASS, "Successfully Inserted Record from CMX Prod(cmxdata.x_transmission) to CMX QA(MPAUL.x_transmission_temp)");
			} else {
				reportStatusFail("Error in downloading Records into CMX QA(MPAUL.x_transmission_temp)");
			}
		} catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		softAssert.assertAll();
	}
	
	/**
	 * Test to download data from OMX QA table mpaul.EXPORT_QUEUE_MERGE and insert into CMX QA table mpaul.EXPORT_QUEUE_MERGE.
	 * * @throws IOException
	 */
	@Test()
	public void omxToCmxDBTransfer() throws IOException 
	{
		
		try {
			if (OmxToCmxDataTransfer.copyDataRecordOmxToCmx()) {
				test.log(Status.PASS, "Successfully Inserted Record from OMX QA(mpaul.EXPORT_QUEUE_MERGE) to CMX QA(MPAUL.EXPORT_QUEUE_MERGE)");
			} else {
				reportStatusFail("Error in downloading Records into CMX QA(MPAUL.EXPORT_QUEUE_MERGE)");
			}
		} catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		softAssert.assertAll();
	}
	
	public void reportStatusFail(String failLog) {
		test.log(Status.FAIL, failLog);
		failure_Steps = failure_Steps + 1;
		softAssert.fail(failLog);
		softAssert.assertTrue(false);
		captureScreenshot("failure");
	}


}
