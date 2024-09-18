//package xpo.qa.sc.wmx.tests;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.lang.reflect.Method;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.testng.ITestResult;
//import org.testng.annotations.AfterMethod;
//import org.testng.annotations.AfterTest;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Test;
//import org.testng.asserts.SoftAssert;
//
//import com.aventstack.extentreports.ExtentTest;
//import com.aventstack.extentreports.Status;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import xpo.qa.common.DateUtil;
//import xpo.qa.common.FileUtils;
//import xpo.qa.common.databases.oracle.OracleDB;
//import xpo.qa.common.excel.ExcelUtil;
//import xpo.qa.common.selenium.DriverManager;
//import xpo.qa.common.selenium.DriverManagerFactory;
//import xpo.qa.common.selenium.DriverManagerType;
//import xpo.qa.common.selenium.TestProperties;
//import xpo.qa.sc.omx.data.OmxData;
//import xpo.qa.sc.omx.pages.OmxHomePage;
//import xpo.qa.sc.omx.pages.OmxJobsViewPage;
//import xpo.qa.sc.omx.pages.OmxLoginPage;
//import xpo.qa.sc.omx.pages.OmxOrderMaintenancePage;
//import xpo.qa.sc.omx.pages.OmxOrderSearchPage;
//import xpo.qa.sc.wmx.data.CmxData;
//import xpo.qa.sc.wmx.data.WmxData;
//import xpo.qa.sc.wmx.m.pages.WmxMDropIdInfoPage;
//import xpo.qa.sc.wmx.m.pages.WmxMHomePage;
//import xpo.qa.sc.wmx.m.pages.WmxMLoginPage;
//import xpo.qa.sc.wmx.m.pages.WmxMPickByOrderPage;
//import xpo.qa.sc.wmx.m.pages.WmxMPickOrderToDropId;
//import xpo.qa.sc.wmx.m.pages.WmxMPickPage;
//import xpo.qa.sc.wmx.pages.WmxEditMBOL;
//import xpo.qa.sc.wmx.pages.WmxEditOrder;
//import xpo.qa.sc.wmx.pages.WmxHomePage;
//import xpo.qa.sc.wmx.pages.WmxLoginPage;
//import xpo.qa.sc.wmx.pages.WmxMBOLPage;
//import xpo.qa.sc.wmx.pages.WmxOrderSearchPage;
//import xpo.qa.sc.wmx.pages.WmxVerificationPage;
//import xpo.qa.sc.wmx.utilities.WMXDataBaseUtils;
//import xpo.qa.sc.wmx.utilities.WmxUtil;
//
//public class WMXEndToEndTest extends TestBase {
//
//	private DriverManager manager;
//	private WmxData wmxData;
//	private CmxData cmxData;
//	private OmxData omxData;
//
//	private OmxLoginPage loginOmx;
//	private OmxHomePage homeOmx;
//	private OmxJobsViewPage jobsViewOmx;
//	private OmxOrderSearchPage orderSearchOmx;
//	private OmxOrderMaintenancePage om;
//
//	private WmxLoginPage loginWmx;
//	private WmxHomePage homeWmx;
//	private WmxOrderSearchPage searchWmx;
//	private WmxEditOrder editWmx;
//
//	private WmxMLoginPage loginWmxM;
//	private WmxMHomePage homeWmxM;
//	private WmxMPickByOrderPage pickOrderWmxM;
//	private WmxMPickOrderToDropId pickOrderToDropIdWmxM;
//	private WmxMDropIdInfoPage dropIdInfoWmxM;
//	private WmxMPickPage pickWmxM;
//
//	private WmxVerificationPage verificationWmx;
//	private WmxMBOLPage mbol;
//	private WmxEditMBOL editMbol;
//
//	private WmxUtil util;
//	private ExtentTest test;
//	private int failure_Steps;
//	private SoftAssert softAssert;
//	ResultSet rs1;
//	PreparedStatement pstmt;
//	int count=0,result;
//
//	private String[] omxMaskKeysOrderHeader = {
//		"CRN",
//		"ORDERKEY",
//		"ORDERDATE",
//		"PLANSHIPDATE",
//		"ACTUALSHIPDATE",
//		"PLANDELIVERYDATE",
//		"NOTES",
//		"STATUS",
//		"STATUSTS",
//		"ATTR_DOC_ID",
//		"ADDTS",
//		"EDITTS",
//		"EDITWHO",
//		"LOCK_TS",
//		"TRANSMISSIONID" };
//
//	private String[] omxMaskKeysOrderDeatils = {
//		"CRN",
//		"ORDERKEY",
//		"STATUS",
//		"STATUSTS",
//		"ATTR_DOC_ID",
//		"ADDTS",
//		"EDITTS" };
//
//	public void initialSetUp(String scriptName) {
//		wmxData = new WmxData();
//		cmxData = new CmxData();
//		omxData = new OmxData();
//		util = new WmxUtil();
//
//		loginOmx = new OmxLoginPage(manager.getDriver());
//		homeOmx = new OmxHomePage(manager.getDriver());
//		jobsViewOmx = new OmxJobsViewPage(manager.getDriver());
//		orderSearchOmx = new OmxOrderSearchPage(manager.getDriver());
//		om = new OmxOrderMaintenancePage(manager.getDriver());
//
//		loginWmx = new WmxLoginPage(manager.getDriver());
//		homeWmx = new WmxHomePage(manager.getDriver());
//		searchWmx = new WmxOrderSearchPage(manager.getDriver());
//		editWmx = new WmxEditOrder(manager.getDriver());
//
//		loginWmxM = new WmxMLoginPage(manager.getDriver());
//		homeWmxM = new WmxMHomePage(manager.getDriver());
//		pickOrderWmxM = new WmxMPickByOrderPage(manager.getDriver());
//		pickOrderToDropIdWmxM = new WmxMPickOrderToDropId(manager.getDriver());
//		dropIdInfoWmxM = new WmxMDropIdInfoPage(manager.getDriver());
//		pickWmxM = new WmxMPickPage(manager.getDriver());
//
//		verificationWmx = new WmxVerificationPage(manager.getDriver());
//		mbol = new WmxMBOLPage(manager.getDriver());
//		editMbol = new WmxEditMBOL(manager.getDriver());
//
//		test = TestProperties.getExtentReports().createTest((this.getClass().getSimpleName() + "::" + scriptName),
//			scriptName);
//		test.assignCategory("Smoke Testing");
//		test.assignAuthor("Priyanka Acharya");
//		softAssert = new SoftAssert();
//	}
//
//	@BeforeMethod(alwaysRun = true)
//	public void beforeMethod(Method method) {
//		manager = DriverManagerFactory.getManager(DriverManagerType.CHROME);
//		ChromeOptions options = new ChromeOptions();
//		if (getHeadless()) {
//			options.addArguments("window-position=0,0");
//			options.addArguments("window-size=1920,1080");
//			options.addArguments("headless");
//		}
//		manager.createDriver(options);
//		TestProperties.initialize();
//		initialSetUp(method.getName());
//
//	}
//
//	@AfterMethod(alwaysRun = true)
//	public void afterMethod(ITestResult result) {
//		if (result.getStatus() == ITestResult.FAILURE) {
//			captureScreenshot("failure");
//		}
//		if (failure_Steps > 0) {
//			test.log(Status.FAIL, "Test is Failed ");
//		}
//		failure_Steps = 0;
//		try {
//			TestProperties.getExtentReports().flush();
//		} catch (Exception e) {
//			test.log(Status.FAIL, "Error during close, no tests executed to report.  No ExtentReport generated.");
//		}
//		close();
//	}
//
//	@AfterTest(alwaysRun = true)
//	public void dispalyExecutionResult() {
//		manager = DriverManagerFactory.getManager(DriverManagerType.CHROME);
//		ChromeOptions options = new ChromeOptions();
//		manager.createDriver(options);
//		TestProperties.getDriver().get(System.getProperty("user.dir") + "\\test-output\\log\\ExtentReports.html");
//	}
//
//	
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
//			loginToOMX();
//			homeOmx.showJobView();
//			jobsViewOmx.runLorealInboundJob();
//			jobsViewOmx.runLorealOrderImportOutboundJob();
//			test.log(Status.PASS, "Successfully run Loreal Inbound JOB and Loreal Order Import JOB");
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//		softAssert.assertAll();
//	}
//
//	@Test(priority = 3, dependsOnMethods = { "cmxInsertOrderDB" })
//	public void omxTestsearchOrder() {
//		try {
//			loginToOMX();
//			homeOmx.clickOrderLink();
//			orderSearchOmx.searchOrder(CmxData.externalKey);
//
//			tempSolutionForError();
//			if (orderSearchOmx.orderSearchResultTableRows() &&
//				orderSearchOmx.getTextOrderSearchResultTableExternalkey().equals(CmxData.externalKey) &&
//				orderSearchOmx.getTextOrderSearchResultTableStatus().equals("PLACED") &&
//				getOrderStatusOmxDB().equals("50")) {
//				test.log(Status.PASS, "Expected Order is created in OMX");
//			} else {
//				//reportStatusFail("Expected Order is not there in OMX");
//			}
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//		softAssert.assertAll();
//	}
//
//	@Test(priority = 4, dependsOnMethods = { "cmxInsertOrderDB" })
//	public void omxTestRunBOPJobs() throws IOException {
//		try {
//			loginToOMX();
//			homeOmx.showJobView();
//			jobsViewOmx.runBatchOrderProcessing();
//			homeOmx.clickOrderLink();
//			orderSearchOmx.searchOrder(CmxData.externalKey);
//
//			if (orderSearchOmx.orderSearchResultTableRows() &&
//				orderSearchOmx.getTextOrderSearchResultTableExternalkey().equals(CmxData.externalKey) &&
//				orderSearchOmx.getTextOrderSearchResultTableStatus().equals("OM-ALLOCATED") &&
//				getOrderStatusOmxDB().equals("90")) {
//				test.log(Status.PASS, "Expected Order Status is changed  to OM-ALLOCATED in OMX");
//			} else {
//				//reportStatusFail("Expected Order Status in OMX is OM-ALLOCATED  , But Actual one is "
//				//+ orderSearchOmx.getTextOrderSearchResultTableStatus());
//			}
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//		softAssert.assertAll();
//	}
//
//	@Test(priority = 5, dependsOnMethods = { "cmxInsertOrderDB" })
//	public void wmxTestProcessJob() throws IOException {
//		try {
//			loginToWMX();
//			processJobWmx();
//			wmxAllocateOrder();
//			wmxReleaseOrder();
//			wmxProcessOrder();
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//		softAssert.assertAll();
//	}
//
//	@Test(priority = 6, dependsOnMethods = { "cmxInsertOrderDB" })
//	public void wmxMobileTestExecutePickWmxM() throws IOException {
//		try {
//			String dropId = DateUtil.getDateString(new Date(), "MMddHHmmss");
//			loginToWmxM();
//			//homeWmxM.selectPickByOrder();
//			//pickOrderWmxM.enterOrderKey(WmxData.orderkey_WMX);
//			//pickWmxM.processOrderPick();
//			homeWmxM.selectPickOrderToDropId();
//			String existingDropid = pickOrderToDropIdWmxM.getDropIdText();
//			if (existingDropid.length() != 0) {
//				homeWmxM.selectComplteDropId();
//				dropIdInfoWmxM.processComplteDropId(existingDropid);
//			}
//			homeWmxM.selectPickOrderToDropId();
//			pickOrderToDropIdWmxM.setPickOrderToDropId(dropId,
//				WmxData.orderkey_WMX);
//			pickWmxM.processOrderPick();
//			homeWmxM.selectComplteDropId();
//			dropIdInfoWmxM.processComplteDropId(dropId);
//			test.log(Status.PASS, "Successfully Picked Order in WMXM");
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//		softAssert.assertAll();
//	}
//
//	@Test(priority = 7, dependsOnMethods = { "cmxInsertOrderDB", "wmxMobileTestExecutePickWmxM" })
//	public void wmxVerifyPickOrder() throws IOException {
//		try {
//			loginToWMX();
//			homeWmx.selectOrder();
//			searchWmx.searchOrderWmx(CmxData.externalKey);
//
//			if (searchWmx.orderSearchResultTableRows() &&
//				searchWmx.getOrderSearchResultTableExternalkey().trim().equals(CmxData.externalKey) &&
//				searchWmx.getOrderSearchResultTableStatus().trim().equals("140-PICKED") &&
//				getOrderStatusWmxDB().equals("140")) {
//				test.log(Status.PASS,
//					"Status of Order with Orderkey " + WmxData.orderkey_WMX + " Changed to 140-PICKED");
//			} else
//
//			{
//				reportStatusFail("Expected Order Status is 140-PICKED , But Actual one is "
//					+ searchWmx.getOrderSearchResultTableStatus().trim());
//			}
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//		softAssert.assertAll();
//	}
//
//	@Test(priority = 8, dependsOnMethods = { "cmxInsertOrderDB" })
//	public void wmxInventoryVerfication() throws IOException {
//		try {
//			loginToWMX();
//			homeWmx.selectVerification();
//			verificationWmx.enterVerificationOrderkey(WmxData.orderkey_WMX);
//			verificationWmx.verifyOrderDetail();
//			if (verificationWmx.isOrderVerificationSuccessful()) {
//				test.log(Status.PASS, "Verified Order in WMX ");
//			} else {
//				reportStatusFail("Order Verification Unsuccessful");
//			}
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//		softAssert.assertAll();
//	}
//
//	@Test(priority = 9, dependsOnMethods = { "cmxInsertOrderDB", "wmxInventoryVerfication" })
//	public void wmxVerifyOrderVerification() throws IOException {
//		try {
//			loginToWMX();
//			homeWmx.selectOrder();
//			searchWmx.searchOrderWmx(CmxData.externalKey);
//
//			if (searchWmx.orderSearchResultTableRows() &&
//				searchWmx.getOrderSearchResultTableExternalkey().trim().equals(CmxData.externalKey) &&
//				searchWmx.getOrderSearchResultTableStatus().trim().equals("150-VERIFIED") &&
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
//	@Test(priority = 10, dependsOnMethods = { "cmxInsertOrderDB" })
//	public void wmxMBOLStageAndShipOrder() {
//		try {
//			loginToWMX();
//			selectEditOrderMBOL();
//			editMbol.selectCarrierKeyDropdown("CEVA");
//			editMbol.setProNumberInput("PRO");
//			editMbol.clickSaveButton();
//
//			editMbol.clickActionsButton();
//			editMbol.selectStageAction();
//			editMbol.clickCloseButtonNoPrinterError();
//
//			if (editMbol.getStatusFieldValue().trim().equals("170-STAGED") &&
//				getOrderStatusWmxDB().equals("170")) {
//				test.log(Status.PASS,
//					"MBOL Status of Order with Orderkey " + WmxData.orderkey_WMX + " Changed to 170-STAGED");
//			} else {
//				reportStatusFail("Expected Order Status in MBOL is 170-STAGED  , But Actual one is "
//					+ editWmx.getStatusFieldValue());
//			}
//
//			editMbol.clickActionsButton();
//			editMbol.selectShipAction();
//			if (editMbol.getStatusFieldValue().trim().equals("200-SHIPPED") &&
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
//
//	@Test(priority = 11, dependsOnMethods = { "cmxInsertOrderDB", "wmxMBOLStageAndShipOrder" })
//	public void wmxMBOLVerifyShipOrder() {
//		try {
//			loginToWMX();
//			homeWmx.selectOrder();
//			searchWmx.searchOrderWmx(CmxData.externalKey);
//
//			if (searchWmx.orderSearchResultTableRows() &&
//				searchWmx.getOrderSearchResultTableExternalkey().trim().equals(CmxData.externalKey) &&
//				searchWmx.getOrderSearchResultTableStatus().trim().equals("200-SHIPPED") &&
//				getOrderStatusWmxDB().equals("200")) {
//				test.log(Status.PASS,
//					"Status of Order with Orderkey " + WmxData.orderkey_WMX + " Changed to 200-SHIPPED");
//			} else {
//				reportStatusFail("Expected Order Status is 200-SHIPPED , But Actual one is "
//					+ searchWmx.getOrderSearchResultTableStatus().trim());
//			}
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//		softAssert.assertAll();
//	}
//
//	@Test(priority = 12, dependsOnMethods = { "cmxInsertOrderDB", "wmxMBOLStageAndShipOrder" })
//	public void omxVerifyOrderShipped() {
//		try {
//			loginToOMX();
//			homeOmx.clickOrderLink();
//			orderSearchOmx.searchOrder(CmxData.externalKey);
//
//			if (orderSearchOmx.orderSearchResultTableRows() &&
//				orderSearchOmx.getTextOrderSearchResultTableExternalkey().equals(CmxData.externalKey) &&
//				orderSearchOmx.getTextOrderSearchResultTableStatus().equals("SHIPPED") &&
//				getOrderStatusOmxDB().equals("200")) {
//				test.log(Status.PASS, "Expected Order Status is Propagated and Status changed  to SHIPPED in OMX");
//			} else {
//				reportStatusFail("Expected SHIPPED Order Status is not Propagated to  OMX");
//			}
//			validateOmxOrderHeaderDB();
//			validateOmxOrderDetailsDB("5");
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//		softAssert.assertAll();
//	}
//
//	@Test(priority = 13, dependsOnMethods = { "cmxInsertOrderDB" })
//	public void omxOutboundRunShipConfirmationJob() {
//		try {
//			loginToOMX();
//			homeOmx.showJobView();
//			jobsViewOmx.runWarehouseOrderConfirmation();
//			test.log(Status.PASS, "Successfully run Warehouse Order Confirmation JOB");
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//		softAssert.assertAll();
//	}
//
//	@Test(priority = 14, dependsOnMethods = { "cmxInsertOrderDB" })
//	public void omxRunCmxShipConfirmationJob() {
//		try {
//			loginToOMX();
//			homeOmx.showJobView();
//			jobsViewOmx.runCmxOutboundLorealWHConf();
//			test.log(Status.PASS, "Successfully run CMX Loreal Warehouse  Confirmation JOB");
//			homeOmx.clickOrderLink();
//			orderSearchOmx.searchOrder(CmxData.externalKey);
//
//			if (orderSearchOmx.orderSearchResultTableRows() &&
//				orderSearchOmx.getTextOrderSearchResultTableExternalkey().equals(CmxData.externalKey) &&
//				orderSearchOmx.getTextOrderSearchResultTableStatus().equals("SHIPPED") &&
//				getOrderStatusOmxDB().equals("200")) {
//				test.log(Status.PASS, "Expected Order Status is Propagated and Status changed  to SHIPPED in OMX");
//			} else {
//				reportStatusFail("Expected SHIPPED Order Status is not Propagated to  OMX");
//			}
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//		softAssert.assertAll();
//	}
//
//	@Test(priority = 15, dependsOnMethods = { "cmxInsertOrderDB" })
//	public void cmxVerifyOrderConfirmationStatus() {
//		try {
//			OracleDB db = new OracleDB(CmxData.serverName_CMXDB, CmxData.port_CMXDB, CmxData.database_CMXDB,
//				CmxData.userid_CMXDB, CmxData.password_CMXDB,
//				false);
//			String sqlCMXValidationQuery = "select id,file_name,status,transmission_type,tp_transmission_type,tp_unique_num from cmxdata.x_transmission  where  TP_UNIQUE_NUM='"
//				+ CmxData.externalKey + "' order by id desc";
//			List<Map<String, String>> dbData = db.selectSQL(sqlCMXValidationQuery);
//			System.out.println(dbData.get(0).get("TRANSMISSION_TYPE"));
//			if (dbData.get(0).get("TRANSMISSION_TYPE").equals("WAREHOUSE_CONFIRMATION")) {
//				test.log(Status.PASS,
//					"Expected Order Status is Propagated to CMX and Status is WAREHOUSE_CONFIRMATION");
//			} else {
//				reportStatusFail("Expected Order is not there in CMX with Status WAREHOUSE_CONFIRMATION");
//			}
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//		softAssert.assertAll();
//	}
//
//	public String getOrderStatusOmxDB() {
//		String status = "";
//		try {
//			OracleDB db = new OracleDB(OmxData.serverName_OMXDB, OmxData.port_OMXDB, OmxData.database_OMXDB,
//				OmxData.userid_OMXDB, OmxData.password_OMXDB,
//				true);
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
//	public void validateOmxOrderHeaderDB() throws Exception {
//		String workbook = System.getProperty("user.dir") + "\\src\\main\\resources\\omx_db_expected_data.xlsx";
//		InputStream is = FileUtils.getInputStream(WMXEndToEndTest.class, workbook);
//		List<Map<String, String>> excelData = ExcelUtil.getExcelSheet(is, "omx_order_header");
//		boolean allDataCorrect = true;
//		try {
//			OracleDB db = new OracleDB(OmxData.serverName_OMXDB, OmxData.port_OMXDB, OmxData.database_OMXDB,
//				OmxData.userid_OMXDB, OmxData.password_OMXDB,
//				true);
//			String omxStatusQuery = "SELECT * from OMXOUTBOUND.ORDER_A WHERE EXTERNKEY='" + CmxData.externalKey + "'"
//				+ " AND CLIENT_ID =1560";
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
//
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
//
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
//
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
//
//	public String getOrderStatusWmxDB() {
//		String status = "";
//		try {
//			OracleDB db = new OracleDB(WmxData.serverName_WMXDB, WmxData.port_WMXDB, WmxData.database_WMXDB,
//				WmxData.userid_WMXDB, WmxData.password_WMXDB,
//				true);
//			String wmxScript = "BEGIN WMXVPD.COREUTL_VPD_SECURITY.SET_SITEID (PV_SITEID => 'ONT001'); COMMIT; END; ";
//			String wmxStatusQuery = "select STATUS from WX_ORDER WHERE EXTERNKEY='" + CmxData.externalKey + "'";
//			List<Map<String, String>> dbData = db.select_scriptAndQuery(wmxScript, wmxStatusQuery);
//			status = dbData.get(0).get("STATUS");
//			System.out.println("Status in WMX DB: " + status);
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//		return status;
//	}
//
//	/**
//	 * Insert the Given Query in CMX Data base and Verify The Same Record jas
//	 * been Inserted Successfully
//	 * 
//	 * @return
//	 */
//	public boolean insertDataRecordCMX() {
//		boolean insertionSuccess = false;
//		try {
//			OracleDB db = new OracleDB(CmxData.serverName_CMXDB, CmxData.port_CMXDB, CmxData.database_CMXDB,
//				CmxData.userid_CMXDB, CmxData.password_CMXDB,
//				true);
//
//			String sqlInsertQuery = util.setWmxQuery();
//			String insertedRecordID = db.executeInsertGetGeneratedKeys(sqlInsertQuery);
//			System.out.println("Inserted Record ID: " + insertedRecordID);
//			String sqlSelectQueryValidation = "select * from CMXDATA.X_TRANSMISSION WHERE ID=" + insertedRecordID;
//			List<Map<String, String>> dbData = db.selectSQL(sqlSelectQueryValidation);
//			System.out.println(dbData.get(0));
//			String externalKey = dbData.get(0).get("FILE_NAME").trim();
//			System.out.println("External Key : " + externalKey);
//			CmxData.setExternalKey(externalKey);
//
//			if (dbData.size() > 0) {
//				insertionSuccess = true;
//			}
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//		return insertionSuccess;
//
//	}
//
//	public void loginToOMX() {
//		try {
//			TestProperties.getDriver().get(getApplicationURLOMX());
//			loginOmx.loginToOMX(OmxData.userId_OMXLogin, OmxData.password_OMXLogin);
//			homeOmx.selectOMXClient(OmxData.client_OMX);
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//	}
//
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
//
//	public void loginToWMX() {
//		try {
//			TestProperties.getDriver().get(getApplicationURLWMX());
//			loginWmx.loginToWMX(WmxData.userId_WMXLogin, WmxData.password_WMXLogin);
//			if (homeWmx.getTextWarehouse().equals("WAREHOUSE")) {
//				test.log(Status.PASS, "Successfully Logged into WMX web Application ");
//			} else {
//				reportStatusFail("Not able to login WMX Web App");
//			}
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//	}
//
//	public void processJobWmx() throws IOException {
//		try {
//			homeWmx.selectOrder();
//			searchWmx.searchOrderWmx(CmxData.externalKey);
//
//			if (searchWmx.orderSearchResultTableRows() &&
//				searchWmx.getOrderSearchResultTableExternalkey().trim().equals(CmxData.externalKey) &&
//				searchWmx.getOrderSearchResultTableStatus().trim().equals("101-NEW") &&
//				searchWmx.getOrderSearchResultTableOrderKey().length() > 0 &&
//				getOrderStatusWmxDB().equals("101")) {
//				test.log(Status.PASS,
//					"Successfully propagated Order to WMX With order Key :"
//						+ searchWmx.getOrderSearchResultTableOrderKey());
//			} else {
//				reportStatusFail("OrderPropagation to WMX unsuccessful");
//			}
//
//			validateOmxOrderHeaderDB();
//			validateOmxOrderDetailsDB("0");
//			validateOmxOrderHeaderAdditionalAttributes();
//			validateOmxOrderDetailsAdditionalAttributes();
//			WmxData.setOrderKey(searchWmx.getOrderSearchResultTableOrderKey());
//			searchWmx.clickOrderSearchResultTableEditOrderSymbol();
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//	}
//
//	public void wmxAllocateOrder() {
//		try {
//			editWmx.clickActionsButton();
//			editWmx.clickAllocateDropdown();
//			editWmx.clickconfirmationYesButton();
//
//			if (editWmx.getStatusFieldValue().trim().equals("120-ALLOCATED") &&
//				getOrderStatusWmxDB().equals("120")) {
//				test.log(Status.PASS,
//					"Status of Order with Orderkey " + WmxData.orderkey_WMX + " Changed to 120-ALLOCATED");
//			} else {
//				reportStatusFail(
//					"Expected Order Status is 120-ALLOCATED , But Actual one is " + editWmx.getStatusFieldValue());
//			}
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//	}
//
//	public void wmxReleaseOrder() {
//		try {
//			editWmx.clickActionsButton();
//			editWmx.clickReleaseDropdown();
//			editWmx.clickconfirmationYesButton();
//
//			if (editWmx.getStatusFieldValue().trim().equals("125-RELEASED") &&
//				getOrderStatusWmxDB().equals("125")) {
//				test.log(Status.PASS,
//					"Status of Order with Orderkey " + WmxData.orderkey_WMX + " Changed to 125-RELEASED");
//			} else {
//				reportStatusFail(
//					"Expected Order Status is 125-RELEASED , But Actual one is " + editWmx.getStatusFieldValue());
//			}
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//	}
//
//	public void wmxProcessOrder() {
//		try {
//			editWmx.clickActionsButton();
//			editWmx.clickProcessDropdown();
//			editWmx.clickconfirmationYesButton();
//
//			if (editWmx.getStatusFieldValue().trim().equals("130-PRINTED") &&
//				getOrderStatusWmxDB().equals("130")) {
//				test.log(Status.PASS,
//					"Status of Order with Orderkey " + WmxData.orderkey_WMX + " Changed to 130-PRINTED");
//			} else {
//				reportStatusFail(
//					"Expected Order Status is 130-PRINTED , But Actual one is " + editWmx.getStatusFieldValue());
//			}
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//	}
//
//	public void loginToWmxM() {
//		try {
//			TestProperties.getDriver().get(getApplicationURLWMXM());
//			loginWmxM.loginToWMXM(WmxData.userId_WMXLogin, WmxData.password_WMXLogin);
//
//			if (homeWmxM.getTextWarehouse().equals("WAREHOUSE")) {
//				test.log(Status.PASS, "Successfully Logged into WMX Mobile Application ");
//			} else {
//				reportStatusFail("Not able to login WMX Mobile App");
//			}
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//	}
//
//	public void selectEditOrderMBOL() {
//		try {
//			homeWmx.selectMBOL();
//			mbol.clickNewButton();
//			editMbol.selecDocTypeDropdown();
//			editMbol.searchOrderKeyInput(WmxData.orderkey_WMX);
//
//			if (editMbol.orderSearchResultTableRows() &&
//				editMbol.getOrderKeyOrderSearchTable().trim().equals(WmxData.orderkey_WMX)) {
//				test.log(Status.PASS,
//					"Orderkey " + WmxData.orderkey_WMX + " found in MBOL");
//				editMbol.clickEditSymbolOrderTable();
//			} else {
//				reportStatusFail("Orderkey " + WmxData.orderkey_WMX + " is not found in MBOL");
//			}
//		} catch (Exception e) {
//			reportStatusFail(e.getMessage());
//			e.printStackTrace();
//		}
//	}
//
//	public void reportStatusFail(String failLog) {
//		test.log(Status.FAIL, failLog);
//		failure_Steps = failure_Steps + 1;
//		softAssert.fail(failLog);
//		softAssert.assertTrue(false);
//		captureScreenshot("failure");
//	}
//
//
//}
