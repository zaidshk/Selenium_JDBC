package xpo.qa.sc.wmx.tests;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
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
import com.gargoylesoftware.htmlunit.WebConsole.Formatter;

import xpo.qa.common.DateUtil;
import xpo.qa.common.FileUtils;
import xpo.qa.common.Utils;
//import xpo.qa.common.databases.oracle.OracleDB;
import xpo.qa.sc.wmx.utilities.OracleDB;
import xpo.qa.common.excel.ExcelUtil;
import xpo.qa.common.selenium.DriverManager;
import xpo.qa.common.selenium.DriverManagerFactory;
import xpo.qa.common.selenium.DriverManagerType;
import xpo.qa.common.selenium.TestProperties;
import xpo.qa.sc.omx.data.OmxData;
import xpo.qa.sc.omx.pages.OmxHomePage;
import xpo.qa.sc.omx.pages.OmxHomePage2;
import xpo.qa.sc.omx.pages.OmxJobsViewPage;
import xpo.qa.sc.omx.pages.OmxJobsViewPage2;
import xpo.qa.sc.omx.pages.OmxLoginPage;
import xpo.qa.sc.omx.pages.OmxOrderMaintenancePage;
import xpo.qa.sc.omx.pages.OmxOrderSearchPage;
//import xpo.qa.sc.omx.pages.OmxOrderSearchPage2;
import xpo.qa.sc.wmx.data.CmxData;
import xpo.qa.sc.wmx.data.WmxData;
import xpo.qa.sc.wmx.m.pages.WmxMDropIdInfoPage;
import xpo.qa.sc.wmx.m.pages.WmxMHomePage;
import xpo.qa.sc.wmx.m.pages.WmxMHomePage2;
import xpo.qa.sc.wmx.m.pages.WmxMLoginPage;
import xpo.qa.sc.wmx.m.pages.WmxMPickByOrderPage;
import xpo.qa.sc.wmx.m.pages.WmxMPickOrderToDropId;
import xpo.qa.sc.wmx.m.pages.WmxMPickPage;
import xpo.qa.sc.wmx.m.pages.WmxMPickPieceToDropID;
import xpo.qa.sc.wmx.pages.WmxEditMBOL;
import xpo.qa.sc.wmx.pages.WmxEditOrder;
import xpo.qa.sc.wmx.pages.WmxHomePage;
import xpo.qa.sc.wmx.pages.WmxLoginPage;
import xpo.qa.sc.wmx.pages.WmxMBOLPage;
import xpo.qa.sc.wmx.pages.WmxOrderSearchPage;
import xpo.qa.sc.wmx.pages.WmxVerificationPage;
import xpo.qa.sc.wmx.utilities.FlatFileCompare;
import xpo.qa.sc.wmx.utilities.WMXDBUtils;
import xpo.qa.sc.wmx.utilities.WMXDataBaseUtils;
import xpo.qa.sc.wmx.utilities.WmxUtil;

import xpo.qa.sc.wmx.pages.WmxContainerVerificationPage;



public class E2E extends TestBase {

	private DriverManager manager;
	private WmxData wmxData;
	private CmxData cmxData;
	private OmxData omxData;

	private OmxLoginPage loginOmx;
	private OmxHomePage2 homeOmx;
	private OmxJobsViewPage2 jobsViewOmx;
//	private OmxOrderSearchPage2 orderSearchOmx;
	private OmxOrderMaintenancePage om;

	private WmxLoginPage loginWmx;
	private WmxHomePage homeWmx;
	private WmxOrderSearchPage searchWmx;
	private WmxEditOrder editWmx;

	private WmxMLoginPage loginWmxM;
	private WmxMHomePage2 homeWmxM;
	private WmxMPickByOrderPage pickOrderWmxM;
	private WmxMPickOrderToDropId pickOrderToDropIdWmxM;
	private WmxMDropIdInfoPage dropIdInfoWmxM;
	private WmxMPickPage pickWmxM;
	
	private WmxMPickPieceToDropID pickPieceToDropID;
	

	private WmxVerificationPage verificationWmx;
	private WmxMBOLPage mbol;
	private WmxEditMBOL editMbol;

	private WmxUtil util;
	private ExtentTest test;
	private int failure_Steps;
	private SoftAssert softAssert;
	private WMXDBUtils db;
	
	
	private WmxContainerVerificationPage containerverification;
	private FlatFileCompare fileCompare;



	public void initialSetUp(String scriptName) {
		wmxData = new WmxData();
		cmxData = new CmxData();
		omxData = new OmxData();
		util = new WmxUtil();
		db=new WMXDBUtils();

		loginOmx = new OmxLoginPage(manager.getDriver());
		homeOmx = new OmxHomePage2(manager.getDriver());
		jobsViewOmx = new OmxJobsViewPage2(manager.getDriver());
//		orderSearchOmx = new OmxOrderSearchPage2(manager.getDriver());
		om = new OmxOrderMaintenancePage(manager.getDriver());

		loginWmx = new WmxLoginPage(manager.getDriver());
		homeWmx = new WmxHomePage(manager.getDriver());
		searchWmx = new WmxOrderSearchPage(manager.getDriver());
		editWmx = new WmxEditOrder(manager.getDriver());

		loginWmxM = new WmxMLoginPage(manager.getDriver());
		homeWmxM = new WmxMHomePage2(manager.getDriver());
		pickOrderWmxM = new WmxMPickByOrderPage(manager.getDriver());
		pickOrderToDropIdWmxM = new WmxMPickOrderToDropId(manager.getDriver());
		dropIdInfoWmxM = new WmxMDropIdInfoPage(manager.getDriver());
		pickWmxM = new WmxMPickPage(manager.getDriver());
		
		pickPieceToDropID= new WmxMPickPieceToDropID(manager.getDriver()); 

		verificationWmx = new WmxVerificationPage(manager.getDriver());
		mbol = new WmxMBOLPage(manager.getDriver());
		editMbol = new WmxEditMBOL(manager.getDriver());
		
		containerverification=new WmxContainerVerificationPage(manager.getDriver());
		fileCompare= new FlatFileCompare();
		

		test = TestProperties.getExtentReports().createTest((this.getClass().getSimpleName() + "::" + scriptName),
			scriptName);
		test.assignCategory("E2E L'Oreal");
		test.assignAuthor("SCITQA TEAM");
		softAssert = new SoftAssert();
	}

	

	
	Date date=new Date();
	
	SimpleDateFormat fm=new SimpleDateFormat("yyyyssMM");
	String dropIdDate=fm.format(date);
	
	String dropPrefix="22";
	
	String dropID= dropPrefix + dropIdDate;
	


	
	
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

//	@Test(priority = 1)
//	public void cmxInsertOrderDB() throws IOException {
//		try {
//			if (insertDataRecordCMX()) {
//				test.log(Status.PASS, "Successfully Inserted Record into CMXDATA.X_TRANSMISSION");
//			} else {
//				reportStatusFail("Error in Inserting  Record into CMXDATA.X_TRANSMISSION");
//			}
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//		softAssert.assertAll();
//	}
//
//	@Test(priority = 2, dependsOnMethods = { "cmxInsertOrderDB" })
//	public void omxTestRunInboundOutboundJobs() {
//		try {
//			Utils.sleep(7000);
//			
//			db.runCMxInboundImportLorealJob();
//			
//			System.out.println("Executed Inbound Import");
//			test.log(Status.PASS, "Successfully ran Loreal Inbound JOB ");
//			
//			Utils.sleep(120000);
//			
//			
//			db.runLorealOutboundOrderImportJob();
//			
//			test.log(Status.PASS, "Successfully ran Loreal Order Import JOB");
//			
//			Utils.sleep(120000);
//			
//			db.runLorealBOPJob();
//			
//			test.log(Status.PASS, "Successfully ran Outbound BOP Job");
//			
//				
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//		softAssert.assertAll();
//	}
//
//	@Test(priority = 3)
//	public void omxTestsearchOrder() {
//		try {
//			
//			Utils.sleep(60000);
//			
//			if (searchOrderInOmx()==true)
//			{
//						
//						test.log(Status.PASS, "Expected Order is created in OMX");
//			}else
//			{
//						reportStatusFail("Expected Order is not there in OMX");
//						
//			}
//					
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//		softAssert.assertAll();
//	}
//
//	
//	
//	
//	
//	
//	@Test(priority=4)
//	public void runLOrealBOPjob() {
//		try {
//			
//			
//			Utils.sleep(120000);
//			
//			db.runLorealBOPJob();
//			
//			test.log(Status.PASS, "Successfully ran Outbound BOP Job");
//			Utils.sleep(120000);
//				
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//		softAssert.assertAll();
//		
//	}
//	
//	
//	
//	
//	
//	@Test(priority=5)
//	public void omxTestSearchOrderWMx() {
//		
//		try {
//			
//			Utils.sleep(60000);
//			
//			if (searchOrderWMx()==true)
//			{
//						
//						test.log(Status.PASS, "Expected Order is propagted to  WMX");
//			}else
//			{
//						reportStatusFail("Expected Order is not there in WMX");
//						
//			}
//					
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//		softAssert.assertAll();
//		
//		
//	}
//
//	@Test(priority = 5)
//	public void wmxTestProcessJob() throws IOException {
//		try {
//			Utils.sleep(30000);
//			loginToWMX();
//			Utils.sleep(30000);
////			homeWmx.selectOrder();
//			test.log(Status.PASS, "Logged into the application");
//			Utils.sleep(5000);
//			processJobWmx();
//			test.log(Status.PASS, "Searched order key on Order Grid screen ");
//			wmxAllocateOrder();
//			test.log(Status.PASS, "Allocated order");
//			Utils.sleep(5000);
//			wmxReleaseOrder();
//			test.log(Status.PASS, "Released order");
//			Utils.sleep(5000);
//			wmxProcessOrder();
//			test.log(Status.PASS, "Processed order");
//			Utils.sleep(5000);
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//		softAssert.assertAll();
//	}
////
//   @Test(priority = 6 )
//
//	public void wmxMobileTestExecutePickWmxM() throws IOException {
//		try {
//			
//			String orderKey=getOrderKey();
//				
//			loginToWmxM();
//			Utils.sleep(50000);
//			test.log(Status.PASS, "Logged into WMx mobile application");
//			Utils.sleep(9000);
//			homeWmxM.selectPickEachToDropID();
//			test.log(Status.PASS, "Navigated to Piece Pick To Drop ID");
//			pickPieceToDropID.setPickOrderToDropId(dropID, orderKey);
//			Utils.sleep(10000);
//			test.log(Status.PASS, "Searched pick task for given order");
//			pickPieceToDropID.processOrderPick(getCaseID(getOrderKey()));
//			test.log(Status.PASS, "Successfully Picked Order in WMXM");
//		
//
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//		softAssert.assertAll();
//	}

//	@Test(priority = 7)
//	public void wmxVerifyPickOrder() throws IOException {
//		try {
//			if (
//				getOrderStatusWmxDB().equals("140")) {
//				test.log(Status.PASS,
//					"Status of Order with Orderkey " + WmxData.orderkey_WMX + " Changed to 140-PICKED");
//			} else
//
//			{
//				reportStatusFail("Expected Order Status is not 140-PICKED ");
//			}
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//		softAssert.assertAll();
//	}

//	@Test(priority = 8)
//	public void wmxInventoryVerfication() throws IOException {
//		try {
//			loginToWMX();
//			test.log(Status.PASS, "Logged into application");
//			homeWmx.selectVerification();
//			test.log(Status.PASS, "Navigated to the verification screen");
//			verificationWmx.enterVerificationOrderkey(getOrderKey());
//			test.log(Status.PASS, "Entered order key in verification screen");
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//		softAssert.assertAll();
//	}
//	
//	@Test(priority = 9)
//	public void wmxVerifyOrderVerification() throws IOException {
//		try {
//			
//			if (
//				getOrderStatusWmxDB().equals("150")) {
//				test.log(Status.PASS,
//					"Status of Order with Orderkey " + WmxData.orderkey_WMX + " Changed to 150-VERIFIED");
//			} else {
//				reportStatusFail("Expected Order Status is 150-VERIFIED , But Actual one is "
//					+ searchWmx.getOrderSearchResultTableStatus().trim());
//			}
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//		softAssert.assertAll();
//	}
//
	@Test(priority=10)
	public void wmxContainerVerificationAndClose()throws IOException{
		try {
		loginToWMX();
		test.log(Status.PASS, "Logged into the application");
		Utils.sleep(5000);
		containerverification.selectContainerVerification();
		test.log(Status.PASS, "Naviagted to Container verification screen");
		
		Utils.sleep(30000);
		
//		containerverification.setDropIdInput(dropID);
		
		
		
		containerverification.setDropIdInput("2220200052");
		test.log(Status.PASS, "Set drop id");
		containerverification.verifyContainer();
		if(containerverification.isOrderVerificationSuccessful()) {
			
			test.log(Status.PASS,
					"Container is verified");
		}else {
			
			reportStatusFail("Verification Failed");
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
//	@Test(priority = 11)
//
//	public void wmxMBOLStageAndShipOrder() {
//		try {
//			loginToWMX();
//			test.log(Status.PASS, "Logged into the application");
//			selectEditOrderMBOL();
//			test.log(Status.PASS, "Selected order key on MBOL screen");
//			editMbol.selectCarrierKeyDropdown("CEVA");
//			test.log(Status.PASS, "Set carrier to CEVA");
//			editMbol.setProNumberInput("PRO");
//			test.log(Status.PASS, "Set Pro Number to PRO");
//			editMbol.clickSaveButton();
//			test.log(Status.PASS, "Clicked on Save");
//			editMbol.clickActionsButton();
//			test.log(Status.PASS, "Clicked on Action button");
//			editMbol.selectStageAction();
//			test.log(Status.PASS, "Clicked on MBOL Manifest button");
//			editMbol.clickCloseButtonNoPrinterError();
//			
//
//			if (getOrderStatusWmxDB().equals("190")) {
//				test.log(Status.PASS,
//					"MBOL Status of Order with Orderkey " + WmxData.orderkey_WMX + " Changed to 190-STAGED");
//			} else {
//				reportStatusFail("Expected Order Status in MBOL is 170-STAGED  , But Actual one is "
//					+ editWmx.getStatusFieldValue());
//			}
//
//			editMbol.clickActionsButton();
//			test.log(Status.PASS, "Clicked on Action button");
//			
//			editMbol.selectShipAction();
//			test.log(Status.PASS, "Clicked on Ship button");
//			if (
//				getOrderStatusWmxDB().equals("200")) {
//				test.log(Status.PASS,
//					"MBOL Status of Order with Orderkey " + WmxData.orderkey_WMX + " Changed to  200-SHIPPED");
//			} else {
//				reportStatusFail("Expected Order Status in MBOL is  200-SHIPPED  , But Actual one is "
//					+ editWmx.getStatusFieldValue());
//			}
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//		softAssert.assertAll();
//	}
////	
////	
////
////	@Test(priority = 12)
////	public void omxVerifyOrderShipped() {
////		try {
////
////			if (
////			 
////				getOrderStatusOmxDB().equals("200")) 
////				{
////				test.log(Status.PASS, "Expected Order Status is Propagated and Status changed  to SHIPPED in OMX");
////			} else {
////				reportStatusFail("Expected SHIPPED Order Status is not Propagated to  OMX");
////			}
////
////		} catch (Exception e) {
////			reportStatusFail(e.getMessage());
////			e.printStackTrace();
////		}
////		softAssert.assertAll();
////	}
////
////	@Test(priority = 13)
////	public void omxOutboundRunShipConfirmationJob() {
////		try {
////			db.runOutboundWarehouseLorealJob();
////			Utils.sleep(10000);
////			db.runCMxOutboundWarehouseLorealJob();
////			
////			test.log(Status.PASS, "Successfully run Warehouse Order Confirmation JOB");
////		} catch (Exception e) {
////			reportStatusFail(e.getMessage());
////			e.printStackTrace();
////		}
////		softAssert.assertAll();
////	}
////
////	@Test(priority = 14)
////
////	public void validateOrderStatus() {
////		try {
////		
////	
////			if (
////				getOrderStatusOmxDB().equals("200")) {
////				test.log(Status.PASS, "Expected Order Status is Propagated and Status changed  to SHIPPED in OMX");
////			} else {
////				reportStatusFail("Expected SHIPPED Order Status is not Propagated to  OMX");
////			}
////		} catch (Exception e) {
////			reportStatusFail(e.getMessage());
////			e.printStackTrace();
////		}
////		softAssert.assertAll();
////	}
//
////	@Test(priority = 15)
////	public void cmxVerifyOrderConfirmationStatus() {
////		try {
////			
////			
////			OracleDB db = new OracleDB(CmxData.serverName_CMXDB, CmxData.port_CMXDB, CmxData.database_CMXDB,
////				CmxData.userid_CMXDB, "Lucky!23",
////				true);
////			
////			
////				
////			
////			String sqlCMXValidationQuery = "select id,file_name,status,transmission_type,tp_transmission_type,tp_unique_num from cmxdata.x_transmission  where  TP_UNIQUE_NUM='"
////				+ cmxData.externalKey + "' order by id desc";
////
////			
////			List<Map<String, String>> dbData = db.selectSQL(sqlCMXValidationQuery);
////			System.out.println(dbData.get(0).get("TRANSMISSION_TYPE"));
////			if (dbData.get(0).get("TRANSMISSION_TYPE").equals("WAREHOUSE_CONFIRMATION")) {
////				test.log(Status.PASS,
////					"Expected Order Status is Propagated to CMX and Status is WAREHOUSE_CONFIRMATION");
////			} else {
////				reportStatusFail("Expected Order is not there in CMX with Status WAREHOUSE_CONFIRMATION");
////			}
////			
////			
////			
////			
////			
////			String str=dbData.get(0).get("PAYLOAD");
////			
////			System.out.println(str);
////			
////			
////			File file = new File("C:\\Users\\hsawant\\Desktop\\BeforeCommit\\sc-qa-wmx\\src\\main\\resources\\LorealOrder6_WHConfirmationPaylaod.txt");
////			FileWriter fr = new FileWriter(file, false);
////			BufferedWriter br = new BufferedWriter(fr);
////			br.write(str);
////
////			br.close();
////			fr.close();
////			
////			
////			
////			String OrderConfirmationPaylaod="C:\\Users\\hsawant\\Desktop\\BeforeCommit\\sc-qa-wmx\\src\\main\\resources\\LorealOrder6_WHConfirmationPaylaod.txt";
////			
////			String maskPath="C:\\Users\\hsawant\\Desktop\\BeforeCommit\\sc-qa-wmx\\src\\main\\resources\\MaskForLorealOrder6.txt";
////			
////			String OrderConfirmationGoldPaylaod="C:\\Users\\hsawant\\Desktop\\BeforeCommit\\sc-qa-wmx\\src\\main\\resources\\LorealOrder6_WHConfirmationGoldPayload.txt";
////			
////			fileCompare.mask(OrderConfirmationPaylaod, maskPath, "FLAT", true);
////			
////			test.log(Status.PASS,
////					"Masked file");
////			
////			
////			if(comaprePayload(OrderConfirmationPaylaod, OrderConfirmationGoldPaylaod)==true) {
////				
////				System.out.println("Two files have same content.");
////		        test.log(Status.PASS,
////						"Payload file comparison successful");
////				
////				
////				
////			}
////			else {
////				
////				reportStatusFail("File Comparison Failed");
////			}
////			
////			
////			
////		} catch (Exception e) {
////			reportStatusFail(e.getMessage());
////			e.printStackTrace();
////		}
////		softAssert.assertAll();
////	}
//
//	public String getOrderStatusOmxDB() {
//		String status = "";
//		try {
//			OracleDB db = new OracleDB(OmxData.serverName_OMXDB, OmxData.port_OMXDB, OmxData.database_OMXDB,
//				OmxData.userid_OMXDB, OmxData.password_OMXDB,
//				true);
////			String omxStatusQuery = "SELECT STATUS from ORDER_A WHERE EXTERNKEY='" + CmxData.externalKey + "'";
//			String omxStatusQuery = "SELECT STATUS from ORDER_A WHERE EXTERNKEY='" + CmxData.externalKey + "'";
//			List<Map<String, String>> dbData = db.selectSQL(omxStatusQuery);
//			status = dbData.get(0).get("STATUS");
//			System.out.println("Status in OMX DB: " + status);
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//		return status;
//	}
//	
//	
//	public boolean comaprePayload(String fileUnderTest, String goldFile) {
//		
//		 boolean comparisonSuccess=false;
//		
//		
//		try {
//			
//			BufferedReader reader1 = new BufferedReader(new FileReader(fileUnderTest));
//		    
//		    BufferedReader reader2 = new BufferedReader(new FileReader(goldFile));
//		     
//		    String line1 = reader1.readLine();
//		     
//		    String line2 = reader2.readLine();
//		     
//		    boolean areEqual = true;
//		     
//		    int lineNum = 1;
//		    
//		    
//		    while (line1 != null || line2 != null)
//		    {
//		        if(line1 == null || line2 == null)
//		        {
//		            areEqual = false;
//		             
//		            break;
//		        }
//		        else if(! line1.equalsIgnoreCase(line2))
//		        {
//		            areEqual = false;
//		             
//		            break;
//		        }
//		         
//		        line1 = reader1.readLine();
//		         
//		        line2 = reader2.readLine();
//		         
//		        lineNum++;
//		    }
//		     
//		    if(areEqual)
//		    {
//		        System.out.println("Two files have same content.");
//		        test.log(Status.PASS,
//						"Payload file comparison successful");
//		        
//		        comparisonSuccess=true;
//				
//		    }
//		    else
//		    {
//		        System.out.println("Two files have different content. They differ at line "+lineNum);
//		         
//		        System.out.println("File1 has "+line1+" and File2 has "+line2+" at line "+lineNum);
//		        
//		        reportStatusFail("File comparison failed");
//		    }
//		     
//		    reader1.close();
//		     
//		    reader2.close();
//			
//			
//		}catch (Exception e) {
//			
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//		
//		return comparisonSuccess;
//	}
	

//	public void validateOmxOrderHeaderDB() throws Exception {
//		String workbook = System.getProperty("user.dir") + "\\src\\main\\resources\\omx_db_expected_data.xlsx";
//		InputStream is = FileUtils.getInputStream(WMXEndToEndTest.class, workbook);
//		List<Map<String, String>> excelData = ExcelUtil.getExcelSheet(is, "omx_order_header");
//		boolean allDataCorrect = true;
//		try {
//			OracleDB db = new OracleDB(OmxData.serverName_OMXDB, OmxData.port_OMXDB, OmxData.database_OMXDB,
//				OmxData.userid_OMXDB, OmxData.password_OMXDB,
//				true);
//			String omxStatusQuery = "SELECT * from OMXOUTBOUND.ORDER_A WHERE EXTERNKEY='" + "QAXZ90730MA" + "'"
//				+ " AND CLIENT_ID =1616";
//			List<Map<String, String>> dbData = db.selectSQL(omxStatusQuery);
//			for (String key : dbData.get(0).keySet()) {
//				if (!Arrays.asList(omxMaskKeysOrderHeader)
//					.contains(key)) {
//
//					if (excelData.get(0).get(key).equals("")) {
//						excelData.get(0).put(key, "null");
//					}
//					if (key.equals("EXTERNKEY")) {
//						excelData.get(0).put(key, CmxData.externalKey);
//					}
//					if (!dbData.get(0).get(key).equals(excelData.get(0).get(key))) {
//						allDataCorrect = false;
//						System.out.println(key);
//						System.out.println("DB ******* " + dbData.get(0).get(key));
//						System.out.println("EXCEL***** " + excelData.get(0).get(key));
//					}
//				}
//			}
//			if (allDataCorrect)
//				test.log(Status.PASS, "Validated OMX OrderHeader Database");
//			//Add else once all expected data is ready
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//	}

//	public void validateOmxOrderDetailsDB(String shipQty) throws Exception {
//		String workbook = System.getProperty("user.dir") + "\\src\\main\\resources\\omx_db_expected_data.xlsx";
//		InputStream is = FileUtils.getInputStream(WMXEndToEndTest.class, workbook);
//		List<Map<String, String>> excelData = ExcelUtil.getExcelSheet(is, "omx_order_details");
//		boolean allDataCorrect = true;
//		try {
//			OracleDB db = new OracleDB(OmxData.serverName_OMXDB, OmxData.port_OMXDB, OmxData.database_OMXDB,
//				OmxData.userid_OMXDB, OmxData.password_OMXDB,
//				true);
//			String omxStatusQuery = "select * from omxoutbound.orderdtl_a where EXTERNKEY='" + CmxData.externalKey + "'"
//				+ " AND CLIENT_ID =1560";
//			List<Map<String, String>> dbData = db.selectSQL(omxStatusQuery);
//			for (String key : dbData.get(0).keySet()) {
//				if (!Arrays.asList(omxMaskKeysOrderDeatils)
//					.contains(key)) {
//
//					if (excelData.get(0).get(key).equals("")) {
//						excelData.get(0).put(key, "null");
//					}
//					if (key.equals("EXTERNKEY")) {
//						excelData.get(0).put(key, CmxData.externalKey);
//					}
//					if (key.equals("SHIPQTY")) {
//						excelData.get(0).put(key, shipQty);
//					}
//					if (!dbData.get(0).get(key).equals(excelData.get(0).get(key))) {
//						allDataCorrect = false;
//						System.out.println(key);
//						System.out.println("DB ******* " + dbData.get(0).get(key));
//						System.out.println("EXCEL***** " + excelData.get(0).get(key));
//					}
//				}
//			}
//			if (allDataCorrect)
//				test.log(Status.PASS, "Validated OMX Order Details  Database");
//			//Add else once all expected data is ready
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//	}

//	public void validateOmxOrderHeaderAdditionalAttributes() throws Exception {
//		String workbook = System.getProperty("user.dir") + "\\src\\main\\resources\\omx_db_expected_data.xlsx";
//		InputStream is = FileUtils.getInputStream(WMXEndToEndTest.class, workbook);
//		List<Map<String, String>> excelData = ExcelUtil.getExcelSheet(is, "omx_order_header_additional_att");
//		boolean allDataCorrect = true;
//		try {
//			OracleDB db = new OracleDB(OmxData.serverName_OMXDB, OmxData.port_OMXDB, OmxData.database_OMXDB,
//				OmxData.userid_OMXDB, OmxData.password_OMXDB,
//				true);
//			String omxStatusQuery = "select a.* from omxoutbound.order_attr a, omxoutbound.order_a o where o.externkey='"
//				+ CmxData.externalKey + "'"
//				+ " AND o.client_id = 1560 and a.id = o.attr_doc_id";
//			List<Map<String, String>> dbData = db.selectSQL(omxStatusQuery);
//			for (String key : dbData.get(0).keySet()) {
//				ObjectMapper mapper = new ObjectMapper();
//				Map<String, String> jsonDataMap = mapper.readValue(dbData.get(0).get("JSON_DATA"), Map.class);
//				for (String jsonDatakey : jsonDataMap.keySet()) {
//					if (jsonDatakey.equals("DELIVER_NOT_AFTER_DATE") || jsonDatakey.equals("SHIP_NOT_BEFORE_DATE")
//						|| jsonDatakey.equals("DELIVER_NOT_BEFORE_DATE") || jsonDatakey.equals("EXPECTED_SHIP_DATE")) {
//						excelData.get(0).put(jsonDatakey, DateUtil.getDateString(new Date(), "yyyyMMdd"));
//					}
//					if (!String.valueOf(jsonDataMap.get(jsonDatakey))
//						.equals(String.valueOf(excelData.get(0).get(jsonDatakey)))) {
//						allDataCorrect = false;
//						System.out.println(jsonDatakey);
//						System.out.println("DB ******* " + jsonDataMap.get(jsonDatakey));
//						System.out.println("EXCEL***** " + excelData.get(0).get(jsonDatakey));
//					}
//
//				}
//			}
//			if (allDataCorrect)
//				test.log(Status.PASS, "Validated OMX Header Additional Attributes   Database");
//			//Add else once all expected data is ready
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//	}

//	public void validateOmxOrderDetailsAdditionalAttributes() throws Exception {
//		try {
//			OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
//				WmxData.userid_WMXDB, WmxData.password_WMXDB,
//				true);
//			String sqlSelectQueryValidation = "select a.* from omxoutbound.orderdtl_attr a, omxoutbound.orderdtl_a o where o.externkey = '"
//				+ CmxData.externalKey + "'"
//				+ " AND o.client_id = 1560 and a.id = o.attr_doc_id";
//			List<Map<String, String>> dbData = db.selectSQL(sqlSelectQueryValidation);
//			if (dbData.size() == 0)
//				test.log(Status.PASS,
//					"Validated 0 records are records are retuned in  OMX Order Details Additional Attributes   Database");
//			//Add else once all expected data is ready
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//	}

	public String getOrderStatusWmxDB() {
		String status = "";
		try {
			OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
				WmxData.userid_WMXDB, WmxData.password_WMXDB,
				true);
			String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'ONT004'); COMMIT; END; ";
			String wmxStatusQuery = "select STATUS from WX_ORDER WHERE EXTERNKEY='" + CmxData.externalKey + "'";
			List<Map<String, String>> dbData = db.select_scriptAndQuery(wmxScript, wmxStatusQuery);
			status = dbData.get(0).get("STATUS");
			System.out.println("Status in WMX DB: " + status);
		} catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		return status;
	}
	
	
	/**
	 * Code to get order key - Harshad
	 */
	
	public String getOrderKey() {
		String orderkey="";
		try {
			OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
					WmxData.userid_WMXDB, WmxData.password_WMXDB,
					true);
			String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'ONT004'); COMMIT; END; ";
			
			String wmxOrderKeyQuery= "Select ORDERKEY FROM WX_ORDER WHERE EXTERNKEY='" + CmxData.externalKey + "'";
			List<Map<String, String>> dbData=db.select_scriptAndQuery(wmxScript, wmxOrderKeyQuery);
			orderkey=dbData.get(0).get("ORDERKEY");
			
		}catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		return orderkey;
	
		
		
	} 
	
	
	public String getCaseID(String Orderkey) {
		String caseID="";
		try {
			OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
					WmxData.userid_WMXDB, WmxData.password_WMXDB,
					true);
			String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'ONT004'); COMMIT; END; ";
			
			String wmxOrderKeyQuery= "Select CASEID FROM WX_ORDER_CASE WHERE ORDERKEY='" + Orderkey + "'";
			List<Map<String, String>> dbData=db.select_scriptAndQuery(wmxScript, wmxOrderKeyQuery);
			caseID=dbData.get(0).get("CASEID");
			
		}catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		return caseID;
		
	}
	
	
	public String getLoc() {
		
		String loc="";
		
		try {
			
			OracleDB db= new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
					WmxData.userid_WMXDB, WmxData.password_WMXDB,
					true);
			
			String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'ONT004'); COMMIT; END; ";
			
			
			String wmxLocQuery= "Select LOC FROM WX_ORDER WHERE EXTERNKEY='" + CmxData.externalKey + "'";
			
			List<Map<String, String>> dbData=db.select_scriptAndQuery(wmxScript, wmxLocQuery);
			
			loc=dbData.get(0).get("LOC");
			
		}catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		return loc;
		
		
	}
	
	
	
	public String getLot() {
		
		
        String lot="";
		
		try {
			
			OracleDB db= new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
					WmxData.userid_WMXDB, WmxData.password_WMXDB,
					true);
			
			String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'ONT004'); COMMIT; END; ";
			
			
			String wmxLotQuery= "Select LOT FROM WX_ORDER WHERE EXTERNKEY='" + CmxData.externalKey + "'";
			
			List<Map<String, String>> dbData=db.select_scriptAndQuery(wmxScript, wmxLotQuery);
			
			lot=dbData.get(0).get("LOT");
			
		}catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		return lot;
		
		
	}
	
	public String getPickQty() {
		
		String pickqty="";
try {
			
			OracleDB db= new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
					WmxData.userid_WMXDB, WmxData.password_WMXDB,
					true);
			
			String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'ONT004'); COMMIT; END; ";
			
			
			String wmxPickQtyQuery= "Select ORDERQTY FROM WX_ORDER WHERE EXTERNKEY='" + CmxData.externalKey + "'";
			
			List<Map<String, String>> dbData=db.select_scriptAndQuery(wmxScript, wmxPickQtyQuery);
			
			pickqty=dbData.get(0).get("ORDERQTY");
			
		}catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
		return pickqty;
		
		
		
		
	}
	
	
	
	
	

	/**
	 * Insert the Given Query in CMX Data base and Verify The Same Record jas
	 * been Inserted Successfully
	 * 
	 * @return
	 */
	public boolean insertDataRecordCMX() {
		boolean insertionSuccess = false;
		try {
			OracleDB db = new OracleDB(CmxData.serverName_CMXDB, CmxData.port_CMXDB, CmxData.database_CMXDB,
				CmxData.userid_CMXDB, CmxData.password_CMXDB,
				true);

			String sqlInsertQuery = util.setWmxQuery();
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

	public void loginToOMX() {
		try {
			TestProperties.getDriver().get(getApplicationURLOMX());
			loginOmx.loginToOMX(OmxData.userId_OMXLogin, OmxData.password_OMXLogin);
			homeOmx.selectOMXClient(OmxData.client_OMX);
		} catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
	}

//	public void tempSolutionForError() {
//		try {
//			homeOmx.clickOrderLink();
//			if (orderSearchOmx.getOrderKeySymbolLink().exists()) {
//				orderSearchOmx.click_orderKeySymbolLink();
//				om.setShipToInput(om.getBillToInputIdText());
//				om.clickSaveButton();
//				homeOmx.clickOrderLink();
//			}
//			orderSearchOmx.searchOrder(CmxData.externalKey);
//
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//	}

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

	public void processJobWmx() throws IOException {
		try {
			homeWmx.selectOrder();
			searchWmx.searchOrderWmx(CmxData.externalKey);

//			if (searchWmx.orderSearchResultTableRows() &&
////				searchWmx.getOrderSearchResultTableExternalkey().trim().equals(CmxData.externalKey) &&
//				searchWmx.getOrderSearchResultTableExternalkey().equals(CmxData.externalKey) &&
//				searchWmx.getOrderSearchResultTableStatus().trim().contains("101") &&
//				searchWmx.getOrderSearchResultTableOrderKey().length() > 0 &&
//				getOrderStatusWmxDB().equals("101")) {
//				test.log(Status.PASS,
//					"Successfully propagated Order to WMX With order Key :"
//						+ searchWmx.getOrderSearchResultTableOrderKey());
//			} else {
//				reportStatusFail("OrderPropagation to WMX unsuccessful");
//			}

//			validateOmxOrderHeaderDB();
//			validateOmxOrderDetailsDB("0");
//			validateOmxOrderHeaderAdditionalAttributes();
//			validateOmxOrderDetailsAdditionalAttributes();
//			WmxData.setOrderKey(searchWmx.getOrderSearchResultTableOrderKey());
			searchWmx.clickOrderSearchResultTableEditOrderSymbol();
		} catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
	}

	public void wmxAllocateOrder() {
		try {
			editWmx.clickActionsButton();
			editWmx.clickAllocateDropdown();
			editWmx.clickconfirmationYesButton();

			if (editWmx.getStatusFieldValue().trim().contains("120") &&
				getOrderStatusWmxDB().equals("120")) {
				test.log(Status.PASS,
					"Status of Order with Orderkey " + WmxData.orderkey_WMX + " Changed to 120-ALLOCATED");
			} else {
				reportStatusFail(
					"Expected Order Status is 120-ALLOCATED , But Actual one is " + editWmx.getStatusFieldValue());
			}
		} catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
	}

	public void wmxReleaseOrder() {
		try {
			editWmx.clickActionsButton();
			editWmx.clickReleaseDropdown();
			editWmx.clickconfirmationYesButton();

			if (editWmx.getStatusFieldValue().trim().equals("125-RELEASED") &&
				getOrderStatusWmxDB().equals("125")) {
				test.log(Status.PASS,
					"Status of Order with Orderkey " + WmxData.orderkey_WMX + " Changed to 125-RELEASED");
			} else {
				reportStatusFail(
					"Expected Order Status is 125-RELEASED , But Actual one is " + editWmx.getStatusFieldValue());
			}
		} catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
	}

	public void wmxProcessOrder() {
		try {
			editWmx.clickActionsButton();
			editWmx.clickProcessDropdown();
			editWmx.clickconfirmationYesButton();

			if (editWmx.getStatusFieldValue().trim().equals("130-PRINTED") &&
				getOrderStatusWmxDB().equals("130")) {
				test.log(Status.PASS,
					"Status of Order with Orderkey " + WmxData.orderkey_WMX + " Changed to 130-PRINTED");
			} else {
				reportStatusFail(
					"Expected Order Status is 130-PRINTED , But Actual one is " + editWmx.getStatusFieldValue());
			}
		} catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
	}

	public void loginToWmxM() {
		try {
			TestProperties.getDriver().get(getApplicationURLWMXM());
			loginWmxM.loginToWMXM(WmxData.userId_WMXLogin, WmxData.password_WMXLogin);

//			if (homeWmxM.getTextWarehouse().equals("WAREHOUSE")) {
//				test.log(Status.PASS, "Successfully Logged into WMX Mobile Application ");
//			} else {
//				reportStatusFail("Not able to login WMX Mobile App");
//			}
		} catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
	}

	public void selectEditOrderMBOL() {
		try {
			String orderKey=getOrderKey();
			homeWmx.selectMBOL();
			mbol.clickNewButton();
			editMbol.selecDocTypeDropdown();
			editMbol.searchOrderKeyInput(orderKey);
			editMbol.clickEditSymbolOrderTable();
//			editMbol.selectCarrierKeyDropdown(carrierKey);

//			if (editMbol.orderSearchResultTableRows() &&
//				editMbol.getOrderKeyOrderSearchTable().trim().equals(WmxData.orderkey_WMX)) {
//				test.log(Status.PASS,
//					"Orderkey " + WmxData.orderkey_WMX + " found in MBOL");
//				editMbol.clickEditSymbolOrderTable();
//			} else {
//				reportStatusFail("Orderkey " + WmxData.orderkey_WMX + " is not found in MBOL");
//			}
		} catch (Exception e) {
			reportStatusFail(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public boolean searchOrderInOmx() {
		boolean flag=false;
		try {
			OracleDB db = new OracleDB(OmxData.serverName_OMXDB, OmxData.port_OMXDB, OmxData.database_OMXDB,
				OmxData.userid_OMXDB, OmxData.password_OMXDB,
				true);
//			String omxStatusQuery = "SELECT STATUS from ORDER_A WHERE EXTERNKEY='" + CmxData.externalKey + "'";
			String orderSearchQuery = "SELECT * from ORDER_A WHERE EXTERNKEY='" + CmxData.externalKey + "'";
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
	
	
	
	public boolean searchOrderWMx() {
		boolean flag=false;
		try {
			OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
					WmxData.userid_WMXDB, WmxData.password_WMXDB,
					true);
						
			String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'ONT004'); COMMIT; END; ";
			
			String wmxOrderSearchQuery= "Select * FROM WX_ORDER WHERE EXTERNKEY='" + CmxData.externalKey + "'";
			
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
	
	

	public void reportStatusFail(String failLog) {
		test.log(Status.FAIL, failLog);
		failure_Steps = failure_Steps + 1;
		softAssert.fail(failLog);
		softAssert.assertTrue(false);
		captureScreenshot("failure");
	}
}
