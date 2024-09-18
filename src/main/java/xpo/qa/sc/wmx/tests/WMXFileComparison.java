//package xpo.qa.sc.wmx.tests;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.lang.reflect.Method;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import org.apache.commons.io.FileUtils;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.testng.Assert;
//import org.testng.annotations.AfterTest;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.BeforeTest;
//import org.testng.annotations.DataProvider;
//import org.testng.annotations.Test;
//import org.testng.asserts.SoftAssert;
//import org.testng.util.Strings;
//
//import com.aventstack.extentreports.ExtentTest;
//import com.aventstack.extentreports.Status;
//import xpo.qa.common.DateUtil;
//import xpo.qa.common.databases.ConnectionException;
//import xpo.qa.common.databases.oracle.OracleDB;
//import xpo.qa.common.selenium.DriverManager;
//import xpo.qa.common.selenium.DriverManagerFactory;
//import xpo.qa.common.selenium.DriverManagerType;
//import xpo.qa.common.selenium.TestProperties;
//import xpo.qa.sc.wmx.data.CmxData;
//import xpo.qa.sc.wmx.utilities.FlatFileCompare;
//import xpo.qa.sc.wmx.utilities.WMXDataBaseUtils;
//
///**
// * @author Neha Kedari
// *
// */
//public class WMXFileComparison extends TestBase {
//	public static int dbRow = 0;
//	String filePath="C:\\QAPRODFiles\\"+DateUtil.getDateString(new Date(), "yyyyMMdd");
//	String failedRecordfilePath="C:\\QAPRODFiles\\FAILED"+"\\"+DateUtil.getDateString(new Date(), "yyyyMMdd");
//	String NoMaskfilePath="C:\\QAPRODFiles\\No_MASK"+"\\"+DateUtil.getDateString(new Date(), "yyyyMMdd");
//	String maskingFilePath=System.getProperty("user.dir")+ "\\src\\main\\resources\\filecomparision\\";
//	String dateTimestamp=DateUtil.getDateString(new Date(), "yyyyMMddhhmmss");
//	private DriverManager manager;
//	public List<Map<String, String>> dbData1;
//	public List<Map<String, String>> dbData;
//	String testTableName = "QA_COMPARE_DATA";
//	String maskingTableName="cmx_client";
//	String  interfaceID="XYZ";
//	boolean saveRecords=true,saveFailedRecord=true,saveAllFailedRecord=true,saveNoMaskRecord=true,saveAllNoMaskRecord=true,maskFlag;
//	int NoOfRecordsToBeSaved=10, NoOfFailedRecordsToBeSaved=5,NoOfNoMaskRecordsToBeSaved=5 ;
//	ResultSet rs1;
//	PreparedStatement pstmt;
//	int count=0,result,cnt=0,TotalPassed,TotalFailed,TotalXMLS,TotalReceiver,TotalNoMask,noMaskCount=0;
//	private WMXDataBaseUtils dbt;
//	private ExtentTest test;
//	public ResultSet rs=null;
//	public static String prodFile;
//	public static String qaFile;
//	public static String maskingFile;
//	public String diffString,QCD_status="READY";
//	CmxData cmxData;
//	File prod,qa,mask;
//	public static int  matchingFileCount=0,countres=1;
//	public static int  nonmatchingFileCount=0;
//	public static int prodOrphanFileCount=0;
//	public static int qaOrphanFileCount=0,maskOrphanFileCount,TotalNoOfRecords;
//	public static String executionStartTime;
//	public static String executionEndTime;
//	public ArrayList<String> failedIds=new ArrayList<String>();
//	SoftAssert softAssert ;
//	PreparedStatement preparedStatement;
//	@BeforeTest
//	public void setDBColumns()  throws Exception
//	{
//		dbt= new WMXDataBaseUtils();
//		dbt.createConnection();
//		fetchMaskedData();
//		//fetchComparisionData();
//		prepareMaskProdQaColumns();
//		executionStartTime=DateUtil.getDateString(new Date(), "dd-MM-yyyy" + "_" + "hh-mm-ssa");
//		
//	}
//	/**
//	 * It will check the file type, If file type is other than xml, Then it will Proceed for execution. 
//	 * If the file type is xml, It will skip the test as , We don't have xml comparison logic implemented till now for file comparision.
//	 * Once we have XML file comparison logic is in place, We can remove this before method validation
//	 * @throws SQLException 
//	 * @throws Exception 
//	 */
//	@BeforeMethod
//	public void checkFileType(Method method) throws SQLException
//	{
//		
//		test = TestProperties.getExtentReports().createTest((this.getClass().getSimpleName() + "::" + method.getName()),method.getName());
//		test.assignCategory("Smoke Testing");
//		test.assignAuthor("Neha Kedari");
//	}
//
//	/**
//	 * @param PROD_FILE
//	 * @param QA_FILE
//	 * @throws Exception
//	 * 
//	 * This perticular test will read data from database, TestNG data provider handles reading multiple sets of data from DB
//	 * dataprovider only reads two columns from db that is PROD_FILE and QA_FILE and provide the same to @Test. 
//	 * The Test will get executed as many number of times as number of records in database.
//	 * For example , if DB has got 10 Rows, this test  gets executed 10 times with each row of data
//	 * The given table name needs to get provided . eg tableName = "test_compare_dat_4"
//	 * 
//	 * 
//	 * Once It gets the data, Then setComparisionData() method will put the data into text file.
//	 * basically It will add the contents read from database as QA_FILE.txt and PROD_FILE.txt
//	 * The Masking file as well will get read from DB based on the interface name
//	 * then compare() method will take these informations as input and mask the content accordingly and Compare qa and prod masked data
//	 * 
//	 * 
//	 * 
//	 * If couple of records needs to be saved into Local for some references, then that can be achived by setting boolean saveRecords=true
//	 * And the number of records to be saved is decided by int NoOfRecordsToBeSaved=4
//	 * Accordingly those many number of QA files and corresponding Prod files will get saved on datetime basic into C:\\QAPRODFiles 
//	 * 
//	 * 
//	 * insertMaskedDataToDB() function will add the masked QA data and masked prod data for each dataset into database.
//	 * PROD_MASK_FILE and QA_MASK_FILE are the two column names where these masked data will get stored respectively in database.
//	 * Everytime before storing the data these two columns will be cleared, and once masked data is ready , then that will get added to these 2 columns
//	 * And If After masking , Both the data (prod and qa) are equal, The status in database will get updated as pass, Or else fail.
//	 * 
//	 * dbRow Parameter represents the number of Rows getting executed wrt DB . It keeps on incrementing wrt the number of iteration for database.
//	 */
//
//	@Test(dataProvider = "oracleDb-provider")
//	public void compareProdQAOutputFiles(String prod_file, String qa_file, String id,String format,String transmissionId,String ReceiverType)throws Exception {
//
//		softAssert=new SoftAssert();
//		
//
//		//Read data From DB and put it into text file
//		try{
//			setComparisionData(prod_file, qa_file,format,transmissionId,ReceiverType,id);
//			
//			//check if their exists qa file but the prod file is null
//			if(prod_file.equals(null) || prod.length()==0) {
//				prodOrphanFileCount++;
//				softAssert.assertTrue(false);
//			}
//			////check if their exists prod file but the qa file is null
//			else if(qa_file.equals(null) || qa.length()==0) {
//				qaOrphanFileCount++;
//				softAssert.assertTrue(false);
//			}
//			/*else if(mask.length()==0) {
//				maskOrphanFileCount++;
//				softAssert.assertTrue(false);
//			}*/
//
//			else
//			{
//				//Get the prod data and QA data and mask them and compare
//				diffString=FlatFileCompare.compare(prodFile,qaFile,maskingFile,"UTF-8");
//				System.out.println(diffString);
//				//Status is pass if there is no diff after masking
//				if(diffString.length()==0) {
//					updateStatusInDB("PASS",id);
//					matchingFileCount++;
//					
//				}
//				//Status is fail if masked Prod and QA has any differences
//				else
//				{
//					updateStatusInDB("FAIL",id);
//					failedIds.add(id);
//					nonmatchingFileCount++;
//					if(saveFailedRecord && cnt<NoOfFailedRecordsToBeSaved || saveAllFailedRecord)					
//					saveFailedRecordsToLocal(ReceiverType,transmissionId,id);
//					softAssert.assertTrue(false);
//				}
//				softAssert.assertTrue(diffString.length()==0);
//
//
//				//Insert the masked records to db and change the Status
//				//insertMaskedDataToDB(id,transmissionId,ReceiverType);
//			}
//
//
//			//Save records into local C:\\QAPRODFiles 
//			if(saveRecords && dbRow<NoOfRecordsToBeSaved )
//				saveRecordsToLocal();
//
//
//			//Increment the DBRow for each iteration
//			dbRow++;
//			System.out.println("row num="+dbRow);
//
//
//		}
//		catch(Exception e)
//		{
//			System.out.println(e);	
//		}
//
//		softAssert.assertAll();
//	}
//
//
//	/**
//	 * @param transmissionId
//	 * @param receiverType
//	 * @param status
//	 * This method counts number of passed and failed and update the result in hashMap.
//	 *//*
//	private void updateCount(String transmissionId, String receiverType,String status) {
//		// TODO Auto-generated method stub
//		if(status.equalsIgnoreCase("PASS")){
//			passResult.put(transmissionId+"_"+receiverType, (passResult.get(transmissionId+"_"+receiverType))+1);
//		}
//		else
//		{
//			failResult.put(transmissionId+"_"+receiverType, (failResult.get(transmissionId+"_"+receiverType))+1);
//		}
//	}*/
//
//	/**
//	 * @throws IOException
//	 * This method will run after all cases are executed.This method will publish as report with of passed and failed cases.
//	 */
//	@AfterTest
//	public void generateReport() throws IOException 
//	{
//		executionEndTime=DateUtil.getDateString(new Date(), "dd-MM-yyyy" + "_" + "hh-mm-ssa");
//		String reportDir=System.getProperty("user.dir")+"\\Reports";
//		File file=new File(reportDir);
//		if (!file.exists()) 
//		{
//			file.mkdir();
//		}
//		String currentExecution=reportDir+"\\"+executionStartTime;
//		new File(currentExecution).mkdir();
//		FileOutputStream outputReport= new FileOutputStream(new File(currentExecution+"\\TestSummaryReport.html"));
//		StringBuffer sb= new StringBuffer();
//		StringBuffer sb1= new StringBuffer();
//		sb.append("<tr><th>CLIENT ID</h2></th>"+
//				"<th>TRANSMISSION TYPE</h2></th>"+
//				"<th>PASS</th>"+
//				"<th>FAIL</th>");
//		String resultCount="select QCD_RECEIVERID_PR as RECEIVER_ID, QCD_TRANSMISSION_TYPE_PR as TRANSMISSION_TYPE , sum(decode(qcd_status,'PASS',1,0))as PASS, sum(decode(qcd_status,'FAIL',1,0))as FAIL from "+testTableName+" group by QCD_TRANSMISSION_TYPE_PR, QCD_RECEIVERID_PR";
//		try
//		{
//		rs=dbt.executeSelectAndFetchResultset(resultCount);
//		while(rs.next())
//		{
//		sb1.append("<tr><td>"+rs.getString(1)+"</td><td>"+rs.getString(2)+"</td><td>"+rs.getString(3)+"</td><td>"+rs.getString(4)+"</td></tr>");	
//		}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		/*for (String name : passResult.keySet())  
//		{
//			if(passResult.get(name)>0)
//			{
//				passResult1.put(name,passResult.get(name));
//				sb.append( "<tr><th>"+name+": </th><td>"+passResult.get(name)+"</td></tr>");
//			}
//		}
//		for (String name : failResult.keySet())  
//		{
//			if(failResult.get(name)>0)
//			{
//				failResult1.put(name,failResult.get(name));
//				sb1.append( "<tr><th>"+name+": </th><td>"+failResult.get(name)+"</td></tr>");
//			}
//		}*/
//		
//		String totalCount="Select sum(decode(qcd_status,'PASS',1,0))as PASS, sum(decode(qcd_status,'FAIL',1,0))as FAIL, count(*) from "+testTableName+"";
//		
//		try
//		{
//		rs=dbt.executeSelectAndFetchResultset(totalCount);
//		while(rs.next())
//		{
//		TotalPassed=rs.getInt(1);
//		TotalFailed=rs.getInt(2);
//		TotalNoOfRecords=rs.getInt(3);
//		}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		String header_Report="<html><head><style>"
//				+"@-ms-viewport {"
//				+"width: device-width;"
//				+"		}"
//				+"body {"
//				+"-ms-overflow-style: scrollbar;"
//				+"		}"
//				+"		@media screen and (max-width: 480px) {"
//				+"html, body {"
//				+" min-width: 320px;"
//				+"			}"
//				+"		}"
//				+"body {"
//				+"background: #fff;"
//				+"	font-family: verdana;"
//				+"	margin: 3;"
//				+"	padding: 3;"
//				+"	color: #666;"
//				+"	line-height: 1;"
//				+"}"
//				+"	hr {"
//				+"	border: 2;"
//				+"	border-bottom: solid 1px #dee1e3;"
//				+"	margin: 1em 0;"
//				+" }"
//				+"table {"
//				+"	border-collapse: collapse;"
//				+"	border-spacing: 0;"
//				+"}"
//				+"body {"
//				+"	-webkit-text-size-adjust: none;"
//				+"}"
//				+"h1, h3, h4, h5, h6 {"
//				+"	color: #25A2C3;"
//				+"  text-align: center; "
//				+"  margin-bottom: 0px;"
//				+"border-bottom: 2px solid #fff;"
//				+"}"
//				+"h2 {"
//				+"	color: #f6755e;"
//				+"  align :'center' "
//				+"  margin-bottom: 0px;"
//				+ " text-align: center;"
//				+"background-color: #fff;"
//				+"border-bottom: 2px solid #fff;"
//				+"}"
//				+" .table-wrapper { "
//			    +"	-webkit-overflow-scrolling: touch;"
//		        +"	overflow-x: auto;"
//				+"				}"
//		        +"     table {"
//				+"	margin: 0 0 2em 0;"
//				+"	width: 70%;"
//				+"				}"
//				+"	table tbody tr {"
//				+"		border: solid 1px #dee1e3;"
//				+"		border-left: 0;"
//				+"		border-right: 0;"
//				+"					}"
//				+"		table tbody tr:nth-child(2n + 1) {"
//				+"			background-color: rgba(144, 144, 144, 0.075);"
//				+"		}"
//				+"	table td {"
//				+"		padding: 0.75em 0.75em;"
//				+"					}"
//				+"	table th {"
//				+"		color: #7098B3;"
//				+"		font-size: 1em;"
//				+"		font-weight: 700;"
//				+"		padding: 0 0.75em 0.75em 0.75em;"
//				+"		text-align: center;"
//				+"					}"
//				+"	table thead {"
//				+"		border-bottom: solid 2px #dee1e3;"
//				+"					}"
//				+"	table tfoot {"
//				+"		border-top: solid 2px #dee1e3;"
//				+"					}"
//				+"	table.alt {"
//				+"		border-collapse: separate;"
//				+"					}"
//			    +"		table.alt tbody tr td {"
//				+"			border: solid 1px #dee1e3;"
//				+"			border-left-width: 0;"
//				+"							border-top-width: 0;"
//				+"						}"
//				+"			table.alt tbody tr td:first-child {"
//				+"				border-left-width: 1px;"
//				+"							}"
//				+"		table.alt tbody tr:first-child td {"
//				+"			border-top-width: 1px;"
//				+"						}"
//				+"		table.alt thead {"
//				+"			border-bottom: 0;"
//				+"		}"
//				+"		table.alt tfoot {"
//				+"			border-top: 0;"
//				+"		}"
//				+"}"
//				+"</style></head><body>"
//				+"<h1>SC IT QA Automation Team</h1><hr /><h2>Test Execution Report: CMX File Comparision</h2> <hr />"
//				+"<table>"
//				+ "<tr><th>Total Number of Files:</th><td>"+TotalNoOfRecords+"</td></tr>"
//				+"<tr><th>PASSED:</th><td>"+TotalPassed+"</td></tr>"
//				+"<tr><th>FAILED:</th><td>"+TotalFailed+"</td></tr>"
//				+"<tr><th>Date & Time: </th><td>"+executionStartTime+"</td></tr>"
//				+"<tr></tr></table><hr />"
//				+"<table Class='alt'>"
//				+sb.toString()
//				+sb1.toString()
//				+"</table>"
//				+"</body>"
//				+"</html>";
//		outputReport.write(header_Report.getBytes());	
//		dbt.closeConnection();
//		manager = DriverManagerFactory.getManager(DriverManagerType.CHROME);
//		ChromeOptions options = new ChromeOptions();
//		manager.createDriver(options);
//		TestProperties.getDriver().get(currentExecution+"\\TestSummaryReport.html");
//	}
//
//	/**
//	 * @param PROD_FILE
//	 * @param QA_FILE
//	 * @throws Exception 
//	 * 
//	 * 
//	 */
//	public void setComparisionData(String prod_file, String qa_file, String format,String transType,String receivertype,String id) throws Exception {
//		writeToFile("prod_data.txt", prod_file);
//		
//		writeToFile("qa_data.txt", qa_file);
//		qaFile = System.getProperty("user.dir")+ "\\src\\main\\resources\\filecomparision\\qa_data.txt";
//		prodFile = System.getProperty("user.dir")+ "\\src\\main\\resources\\filecomparision\\prod_data.txt";
//		prod=new File(prodFile);
//		qa=new File(qaFile);
//		String updateMaskedStatus=" update "+testTableName+" set qcd_status='NO MASK' where QCD_TRANSMISSION_TYPE_PR=? and QCD_RECEIVERID_PR=? and qcd_status='READY'";
//		
//		for (int row = 0; row < dbData1.size(); row++) 
//		{
//			
//			if(dbData1.get(row).get("MCC_TRANSMISSION_TYPE").equalsIgnoreCase(transType) && dbData1.get(row).get("MCC_RECEIVERID").equalsIgnoreCase(receivertype))
//			{
//				String maskingData=dbData1.get(row).get("MCC_MASK_DATA");
//				
//				
//				
//				if(maskingData.equalsIgnoreCase("null"))
//				{	maskingData="";
//					
//					writeToFile("masking_data.txt",maskingData);
//					maskingFile = System.getProperty("user.dir")+"\\src\\main\\resources\\filecomparision\\masking_data.txt";
//					mask=new File(maskingFile);
//					dbt.executeUpdateNoMaskStatus(updateMaskedStatus, transType, receivertype);
//					if(saveNoMaskRecord && noMaskCount<NoOfNoMaskRecordsToBeSaved ||saveAllNoMaskRecord)					
//					saveNoMaskRecordsToLocal(receivertype,transType,id);
//					break;
//				}
//				else
//				{
//				writeToFile("masking_data.txt",maskingData);
//				maskingFile = System.getProperty("user.dir")+"\\src\\main\\resources\\filecomparision\\masking_data.txt";
//				mask=new File(maskingFile);
//				break;
//				}
//			}
//		}
//		
//		
//	}
//
//	/**
//	 * @throws Exception
//	 * 
//	 * insertMaskedDataToDB() function will add the masked QA data and masked prod data for each dataset into database.
//	 * PROD_MASK_FILE and QA_MASK_FILE are the two column names where these masked data will get stored respectively in database.
//	 * Everytime before storing the data these two columns will be cleared, and once masked data is ready , then that will get added to these 2 columns
//	 * And If After masking , Both the data (prod and qa) are equal, The status in database will get updated as pass, Or else fail.
//	 *
//	 */
//
//	public void insertMaskedDataToDB(String control_id,String transType,String ReceiverID) throws Exception 
//	{
//		StringBuilder sb = new StringBuilder(readFile(qaFile).replaceAll("'", "''"));
//		StringBuilder sb1 = new StringBuilder(readFile(prodFile).replaceAll("'", "''"));
//		String updateSetQuery ="UPDATE " + testTableName + "\n" +
//				" SET QCD_PAYLOAD_MASK_QA = '"+sb.toString()+"',  QCD_PAYLOAD_MASK_PR=" + "'"+sb1.toString()+"'"
//				+ " WHERE QCD_ID_PR= "+ "'"+control_id+ "'";	
//
//		dbt.executeUpdate(updateSetQuery);
//		System.out.println("Done with="+countres);
//		countres++;
//	}
//
//	/**
//	 * @param status
//	 * 
//	 * Upadte the Status column in database as pass or fail based on post masking comparision
//	 * @throws Exception 
//	 */
//	public void updateStatusInDB(String status, String control_id) throws Exception 
//	{
//		
//		StringBuilder sb = new StringBuilder(readFile(qaFile).replaceAll("'", "''"));
//		StringBuilder sb1 = new StringBuilder(readFile(prodFile).replaceAll("'", "''"));
//		String updateSetQuery = "UPDATE " + testTableName + "\n" +
//				" SET QCD_STATUS= ?, QCD_PAYLOAD_MASK_QA = ?, QCD_PAYLOAD_MASK_PR=?"
//				+ " WHERE QCD_ID_PR= ?";
//		
//		dbt.executeUpdateStatusMAskedData(updateSetQuery,status,sb.toString(),sb1.toString(),control_id);
//		
//		
//	}
//
//	/**
//	 * @throws Exception
//	 * 
//	 * it will mark the masking coumns in DB that is QA_MASK_FILE and PROD_MASK_FILE as null at the beginning
//	 */
//	public void prepareMaskProdQaColumns() throws Exception {
//		OracleDB db = new OracleDB(cmxData.serverNameQA_CMXDB, cmxData.portQA_CMXDB, cmxData.databaseQA_CMXDB,cmxData.useridQA_CMXDB, cmxData.passwordQA_CMXDB,true);
//		
//		/*String xmlStatusUpdate=" update "+testTableName+" set qcd_status='XML' where QCD_TP_TRANSMISSION_FORMAT_PR like '%XML%' and qcd_status='READY'";
//		dbt.executeUpdate(xmlStatusUpdate);*/
//		
//		String updateCleanQuery = "UPDATE " + testTableName + "\n" +" SET QCD_PAYLOAD_MASK_QA = NULL ,  QCD_PAYLOAD_MASK_PR=NULL";
//		dbt.executeUpdate(updateCleanQuery);
//		
//	}
//	/**
//	 * @param fileName
//	 * @param content
//	 * @throws IOException
//	 * 
//	 * Write the contents to a text file. basically it is being called With the prod data contents and qa data contents to store them
//	 */
//	public void writeToFile(String fileName, String content) throws IOException 
//	{
//		File file = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\filecomparision\\" + fileName);
//		FileWriter fw = new FileWriter(file.getAbsoluteFile());
//		BufferedWriter bw = new BufferedWriter(fw);
//		bw.write(content);
//		bw.close();
//	}
//
//	/**
//	 * @param fileName
//	 * @return
//	 * @throws IOException
//	 * 
//	 *             Read the data from PROD_FILE.txt and QA_FILE.txt
//	 */
//	public String readFile(String fileName) throws IOException 
//	{
//		File file = new File(fileName);
//		return FileUtils.readFileToString(file);
//	}
//
//	/**
//	 * @param source
//	 * @param dest
//	 * @throws IOException
//	 * 
//	 * Copy the prod and qa data files into local along with date time stamps.
//	 *  being called where we need to save records into local
//	 */
//	public void copyFile(String source, String dest) throws IOException 
//	{
//		File srcFile = new File(source);
//		File destFile = new File(dest);
//		FileUtils.copyFile(srcFile, destFile);
//	}
//
//	/**
//	 * @throws IOException
//	 * 
//	 * 
//	 * 
//	 * If couple of records needs to be saved into Local for some references, then that can be achived by setting boolean saveRecords=true
//	 * And the number of records to be saved is decided by int NoOfRecordsToBeSaved=4
//	 * Accordingly those many number of QA files and corresponding Prod files will get saved on datetime basic into C:\\QAPRODFiles 
//	 * 
//	 */
//	public void saveRecordsToLocal() throws IOException 
//	{
//
//		String prodFilePath=filePath+"\\"+dateTimestamp+"\\PROD";
//		String qaFilePath=filePath+"\\"+dateTimestamp+"\\QA";
//		createDirectory(prodFilePath);
//		createDirectory(qaFilePath);
//		copyFile(qaFile, qaFilePath+"\\"+dbRow+".txt");
//		copyFile(prodFile, prodFilePath+"\\"+dbRow+".txt");
//	}
//
//	/**
//	 * @throws IOException
//	 * 
//	 * 
//	 * 
//	 * If couple of records needs to be saved into Local for some references, then that can be achived by setting boolean saveRecords=true
//	 * And the number of records to be saved is decided by int NoOfRecordsToBeSaved=4
//	 * Accordingly those many number of QA files and corresponding Prod files will get saved on datetime basic into C:\\QAPRODFiles 
//	 * 
//	 */
//	public void saveFailedRecordsToLocal(String receiverId,String transcationType,String id) throws IOException
//	
//	{
//		
//		String prodFilePath=failedRecordfilePath+"\\"+dateTimestamp+"\\PROD"+"\\"+receiverId+"\\"+transcationType;
//		String qaFilePath=failedRecordfilePath+"\\"+dateTimestamp+"\\QA"+"\\"+receiverId+"\\"+transcationType;
//		createDirectory(prodFilePath);
//		createDirectory(qaFilePath);
//		copyFile(qaFile, qaFilePath+"\\"+"QCD_ID_PR_"+id+".txt");
//		copyFile(prodFile, prodFilePath+"\\"+"QCD_ID_PR_"+id+".txt");
//		cnt++;
//	}
//	
//	/**
//	 * @throws IOException
//	 * 
//	 * 
//	 * 
//	 * If couple of records needs to be saved into Local for some references, then that can be achived by setting boolean saveRecords=true
//	 * And the number of records to be saved is decided by int NoOfRecordsToBeSaved=4
//	 * Accordingly those many number of QA files and corresponding Prod files will get saved on datetime basic into C:\\QAPRODFiles 
//	 * 
//	 */
//	public void saveNoMaskRecordsToLocal(String receiverId,String transcationType,String id) throws IOException
//	
//	{
//		
//		String prodFilePath=NoMaskfilePath+"\\"+dateTimestamp+"\\PROD"+"\\"+receiverId+"\\"+transcationType;
//		String qaFilePath=NoMaskfilePath+"\\"+dateTimestamp+"\\QA"+"\\"+receiverId+"\\"+transcationType;
//		createDirectory(prodFilePath);
//		createDirectory(qaFilePath);
//		copyFile(qaFile, qaFilePath+"\\"+"QCD_ID_PR_"+id+".txt");
//		copyFile(prodFile, prodFilePath+"\\"+"QCD_ID_PR_"+id+".txt");
//		noMaskCount++;
//	}
//	/**
//	 * @param filePath
//	 * 
//	 * Being called to create directory in order to save records to local
//	 */
//	public void createDirectory(String filePath) 
//	{
//		File file = new File(filePath);
//		if (!file.exists()) 
//		{
//			file.mkdirs();
//		}	
//	}
//
//	/**
//	 * @return String[][]
//	 * 
//	 * It is the dataprovider that feeds data @Test method.
//	 *1- It connect to oracle database.
//	 *2- Fires the select query
//	 *3- fetch PROD_FILE and QA_FILE 
//	 *4- Repeat same step for each data row in DB and finally puts the result into and two dimensional array
//	 *5- That two dimensional array has got as many rows as the number of rows in DB and 2 columns(one for QA_FILE and one for PROD_FILE)
//	 */
//
//	@DataProvider(name = "oracleDb-provider")
//	public synchronized String[][] oracleDB(Method m) 
//	{
//		String sqlSelectQueryValidation = "select * from  " + testTableName+" where QCD_STATUS='"+QCD_status+"'";
//		
//		String myData[][] = null;
//		try
//		{
//			dbData = dbt.selectSQL(sqlSelectQueryValidation);
//			
//			//Initialize data structure
//			myData = new String[(dbData.size())][6];
//
//			//populate data structure
//			for (int row = 0; row< dbData.size(); row++) 
//			{
//				myData[row][0] = dbData.get(row).get("QCD_PAYLOAD_PR");
//				myData[row][1] = dbData.get(row).get("QCD_PAYLOAD_QA");
//				myData[row][2] = dbData.get(row).get("QCD_ID_PR");
//				myData[row][3] = dbData.get(row).get("QCD_TP_TRANSMISSION_FORMAT_PR");
//				myData[row][4] = dbData.get(row).get("QCD_TRANSMISSION_TYPE_PR");
//				myData[row][5] = dbData.get(row).get("QCD_RECEIVERID_PR");
//			}
//		}
//		catch (Exception e) 
//		{	
//			e.printStackTrace();
//		}
//		return myData;
//	}
//	public void reportStatusFail(String failLog) 
//	{
//		test.log(Status.FAIL, failLog);
//		softAssert.fail(failLog);
//		softAssert.assertTrue(false);
//		captureScreenshot("failure");
//	}
//
//		/**
//	 * This method fetches masking information for unique receiver id and transcation type.
//	 */
//	public void fetchMaskedData()
//	{
//		String selectQueryToFetchMasking="select MCC_TRANSMISSION_TYPE, MCC_RECEIVERID,mcc_mask_data from "+maskingTableName+"";
//		try 
//		{
//			dbData1 = dbt.selectSQL(selectQueryToFetchMasking);
//		} 
//		catch (SQLException e) 
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		catch (ConnectionException e) 
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * This method fetches data for comparison in list.
//	 */
//	public void fetchComparisionData()
//	{
//		String sqlSelectQueryValidation = "select * from  " + testTableName+" where QCD_TP_TRANSMISSION_FORMAT_PR not like ('%XML%') and QCD_STATUS='READY' and rownum<=10";
//		try
//		{
//			dbData = dbt.selectSQL(sqlSelectQueryValidation);
//		}
//		catch (ConnectionException e) 
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		catch (SQLException e) 
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//}
