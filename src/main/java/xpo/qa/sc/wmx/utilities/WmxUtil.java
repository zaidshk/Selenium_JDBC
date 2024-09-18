package xpo.qa.sc.wmx.utilities;

import java.io.File;
import java.io.IOException;
//import java.security.Timestamp;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import xpo.qa.common.DateUtil;
import xpo.qa.sc.wmx.data.CmxData;

import java.sql.Timestamp;    
import java.util.Date;    
import java.text.SimpleDateFormat; 

public class WmxUtil {
	private String wmxInsertQuery;
	private String wmxQueryFilePath;
	private String asnQueryFilePath;
	private String asnInsertQuery;
	private String brInsertQuery;
	private String lorealASNQueryPath;
	private String lorealASNQuery;
	
	
	private String asnLaerdalQAXAQuery;
	private String asnLaerdalQAXAQueryFilePath;
	
	
	private String asnLaerdalQAXBQuery;
     private String asnLaerdalQAXBQueryFilePath;
     
     
     private String asnLaerdalQAXCQuery;
     private String asnLaerdalQAXCQueryFilePath;
     
     private String asnLaerdalQAXDQuery;
     private String asnLaerdalQAXDQueryFilePath;

     

     private String asnLaerdalQAXEQuery;
     private String asnLaerdalQAXEQueryFilePath;
     

	/**
	 * It reads the given SQL query from the file (Basically Query is being
	 * written in a text file , It will read the whole query and return the same
	 * in String format)
	 * 
	 * @return
	 * @throws IOException
	 */
	public String getWmxQuery() throws IOException {
		wmxQueryFilePath = System.getProperty("user.dir") + "/src/main/resources/CMX_Query.txt";
		File file = new File(wmxQueryFilePath);
		String wmxInsertQuery = FileUtils.readFileToString(file);
		return wmxInsertQuery;

	}
	
	
	
	
	public String getBRQuery()throws IOException{
		asnQueryFilePath=System.getProperty("user.dir")+"/src/main/resources/Loreal_BR_Query.txt";
		File file=new File(asnQueryFilePath);
		String brQuery=FileUtils.readFileToString(file);
		System.out.println("BR");
		return brQuery;
		
		
		
	}
	
	
	public String getAsnQuery()throws IOException{
		asnQueryFilePath=System.getProperty("user.dir")+"/src/main/resources/ASN_CMx_Query.txt";
		File file=new File(asnQueryFilePath);
		String asnInsertQuery=FileUtils.readFileToString(file);
		System.out.println("ASN");
		return asnInsertQuery;
		
		
		
	}
	
	
	
	public String getLorealASN1_2_Query()throws IOException{
		
		lorealASNQueryPath=System.getProperty("user.dir")+"/src/main/resources/Loreal_ASN1_2_CMX_Query.txt";
		File file=new File(lorealASNQueryPath);
		String asnQuery=FileUtils.readFileToString(file);
		System.out.println("Loreal_ASN1_2");
		return asnQuery;
		
		
	}
	
	
	
	public String getLaerdalAsnQAXAQuery() throws IOException {
		asnLaerdalQAXAQueryFilePath = System.getProperty("user.dir") + "/src/main/resources/Laerdal_ASN_QAXA.txt";
		File file = new File(asnLaerdalQAXAQueryFilePath);
		String wmxInsertQuery = FileUtils.readFileToString(file);
		return wmxInsertQuery;

	}
	
	
	
	public String getLaerdalAsnQAXBQuery() throws IOException {
		asnLaerdalQAXBQueryFilePath = System.getProperty("user.dir") + "/src/main/resources/Laerdal_ASN_QAXB.txt";
		File file = new File(asnLaerdalQAXBQueryFilePath);
		String wmxInsertQuery = FileUtils.readFileToString(file);
		return wmxInsertQuery;

	}
	
	
	public String getLaerdalAsnQAXCQuery() throws IOException {
		asnLaerdalQAXCQueryFilePath = System.getProperty("user.dir") + "/src/main/resources/Laerdal_ASN_QAXC.txt";
		File file = new File(asnLaerdalQAXCQueryFilePath);
		String wmxInsertQuery = FileUtils.readFileToString(file);
		return wmxInsertQuery;

	}
	
	
	public String getLaerdalAsnQAXDQuery() throws IOException {
		asnLaerdalQAXDQueryFilePath = System.getProperty("user.dir") + "/src/main/resources/Laerdal_ASN_QAXD.txt";
		File file = new File(asnLaerdalQAXDQueryFilePath);
		String wmxInsertQuery = FileUtils.readFileToString(file);
		return wmxInsertQuery;

	}
	
	
	public String getLaerdalAsnQAXEQuery() throws IOException {
		asnLaerdalQAXEQueryFilePath = System.getProperty("user.dir") + "/src/main/resources/Laerdal_ASN_QAXE.txt";
		File file = new File(asnLaerdalQAXEQueryFilePath);
		String wmxInsertQuery = FileUtils.readFileToString(file);
		return wmxInsertQuery;

	}
	

	/**
	 * setWmxQuery() will get the given Query and do below formatting to get
	 * used for CMX as an insert query
	 * 
	 * 1- Replace $YDAT with today's date in YMMDD format. So if today is
	 * 10/25/2019, replace 91025
	 * 
	 * 2- If you need to process multiple version of this template on one day,
	 * replace $YDATA with $YDAT plus a running letter, e.g. $YDATB, $YDATC,
	 * etc. Then replace $YDAT with today's date in YMMDD format
	 * 
	 * 3- Replace $/DAT wuth today's date in MM/DD format. So if today is
	 * 10/25/2019, replace 10/25 --
	 * 
	 * @return
	 * @throws IOException
	 */
	public String setWmxQuery() throws IOException {
		wmxInsertQuery = getWmxQuery();
		Character newLastChar;
		String updatedYDate;
		String yDate = DateUtil.getDateString(new Date(), "yyMMdd").substring(1);
		if (CmxData.yearDate_$YDAT.contains(yDate)) {
			Character currentLastChar = CmxData.yearDate_$YDAT.charAt(CmxData.yearDate_$YDAT.length() - 1);
			if (currentLastChar.equals('Z')) {
				newLastChar = 'A';
			} else {
				newLastChar = (char) (currentLastChar + 1);
			}
			updatedYDate = CmxData.yearDate_$YDAT.replace(currentLastChar, newLastChar);
		} else {
			updatedYDate = yDate + "A";
		}
		CmxData.setYearDate_$YDAT(updatedYDate);
		String updatedQuery = wmxInsertQuery.replace("$YDAT", CmxData.yearDate_$YDAT).replace("$/DAT",
			DateUtil.getDateString(new Date(), "MM/dd"));
		System.out.println(updatedQuery);
		return updatedQuery;
	}
	
	public String setBRQuery()throws IOException{
		
		 brInsertQuery=getBRQuery();
		Character newLastChar;
		String updatedYDate;
		String yDate = DateUtil.getDateString(new Date(), "yyMMdd").substring(1);
		if (CmxData.yearDate_$YDAT.contains(yDate)) {
			Character currentLastChar = CmxData.yearDate_$YDAT.charAt(CmxData.yearDate_$YDAT.length() - 1);
			if (currentLastChar.equals('Z')) {
				newLastChar = 'A';
			} else {
				newLastChar = (char) (currentLastChar + 1);
			}
			updatedYDate = CmxData.yearDate_$YDAT.replace(currentLastChar, newLastChar);
		} else {
			updatedYDate = yDate + "A";
		}
		CmxData.setYearDate_$YDAT(updatedYDate);
		String updatedQuery = brInsertQuery.replace("$YDAT", CmxData.yearDate_$YDAT).replace("$/DAT",
			DateUtil.getDateString(new Date(), "MM/dd"));
		System.out.println(updatedQuery);
		return updatedQuery;
		
	}
	
	public String setCMXInsertQuery(String query) throws IOException {
		
		
		Character newLastChar;
		String updatedYDate;
		String yDate = DateUtil.getDateString(new Date(), "yyMMdd").substring(1);
		
		if (CmxData.yearDate_$YDAT.contains(yDate)) {
			Character currentLastChar = CmxData.yearDate_$YDAT.charAt(CmxData.yearDate_$YDAT.length() - 1);
			if (currentLastChar.equals('Z')) {
				newLastChar = 'A';
			} else {
				newLastChar = (char) (currentLastChar + 1);
			}
			updatedYDate = CmxData.yearDate_$YDAT.replace(currentLastChar, newLastChar);
		} else {
			updatedYDate = yDate + "A";
		}
		CmxData.setYearDate_$YDAT(updatedYDate);
		System.out.println("insert query="+CmxData.yearDate_$YDAT);
		String updatedQuery = query.replace("$YDATA", CmxData.yearDate_$YDAT).replace("$YDAT", yDate).replace("$/DAT",
		DateUtil.getDateString(new Date(), "MM/dd"));
		
		return updatedQuery;
	}
public String setCMXSelectQuery(String query) throws IOException {
		
		
		
		/*String yDate = DateUtil.getDateString(new Date(), "yyMMdd").substring(1);
		
		if (CmxData.yearDate_$YDAT.contains(yDate)) {
			Character currentLastChar = CmxData.yearDate_$YDAT.charAt(CmxData.yearDate_$YDAT.length() - 1);
			if (currentLastChar.equals('Z')) {
				newLastChar = 'A';
			} else {
				newLastChar = (char) (currentLastChar + 1);
			}
			updatedYDate = CmxData.yearDate_$YDAT.replace(currentLastChar, newLastChar);
		} else {
			updatedYDate = yDate + "A";
		}
		CmxData.setYearDate_$YDAT(updatedYDate);*/
		System.out.println("select query="+CmxData.yearDate_$YDAT);
		String updatedQuery = query.replace("$YDATA", CmxData.yearDate_$YDAT);
		System.out.println(updatedQuery);
		return updatedQuery;
	}



public String setAsnQuery()throws IOException{
	
	
	asnInsertQuery=getAsnQuery();
	Character newLastChar;
	String updatedYDate;
	String yDate = DateUtil.getDateString(new Date(), "yyMMdd").substring(1);
	if (CmxData.yearDate_$YDAT.contains(yDate)) {
		Character currentLastChar = CmxData.yearDate_$YDAT.charAt(CmxData.yearDate_$YDAT.length() - 1);
		if (currentLastChar.equals('Z')) {
			newLastChar = 'A';
		} else {
			newLastChar = (char) (currentLastChar + 1);
		}
		updatedYDate = CmxData.yearDate_$YDAT.replace(currentLastChar, newLastChar);
	} else {
		updatedYDate = yDate + "A";
	}
	CmxData.setYearDate_$YDAT(updatedYDate);
	String updatedQuery = asnInsertQuery.replace("$YDAT", CmxData.yearDate_$YDAT).replace("$/DAT",
		DateUtil.getDateString(new Date(), "MM/dd"));
	System.out.println(updatedQuery);
	return updatedQuery;
	
	
	
	
	
}


public String setLoreal_ASN1_2Query()throws IOException{
	
	
	
	lorealASNQuery=getLorealASN1_2_Query();
	
	Character newLastChar;
	String updatedYDate;
	String yDate = DateUtil.getDateString(new Date(), "yyMMdd").substring(1);
	if (CmxData.yearDate_$YDAT.contains(yDate)) {
		Character currentLastChar = CmxData.yearDate_$YDAT.charAt(CmxData.yearDate_$YDAT.length() - 1);
		if (currentLastChar.equals('Z')) {
			newLastChar = 'A';
		} else {
			newLastChar = (char) (currentLastChar + 1);
		}
		updatedYDate = CmxData.yearDate_$YDAT.replace(currentLastChar, newLastChar);
	} else {
		updatedYDate = yDate + "A";
	}
	CmxData.setYearDate_$YDAT(updatedYDate);
	String updatedQuery = lorealASNQuery.replace("$YDAT", CmxData.yearDate_$YDAT).replace("$/DAT",
		DateUtil.getDateString(new Date(), "MM/dd"));
	System.out.println(updatedQuery);
	return updatedQuery;
	
	
	
	
}

//public String setLaerdal_QAXA_Query() throws IOException{
//	
//	
//	
//	asnLaerdalQAXAQuery=getLaerdalAsnQAXAQuery();
//	
//	Character newLastChar;
//	String updatedYDate;
//	String yDate = DateUtil.getDateString(new Date(), "yyMMdd").substring(1);
//	
//	if (CmxData.yearDate_$YDAT.contains(yDate)) {
//		Character currentLastChar = CmxData.yearDate_$YDAT.charAt(CmxData.yearDate_$YDAT.length() - 1);
//		if (currentLastChar.equals('Z')) {
//			newLastChar = 'A';
//		} else {
//			newLastChar = (char) (currentLastChar + 1);
//		}
//		updatedYDate = CmxData.yearDate_$YDAT.replace(currentLastChar, newLastChar);
//	} else {
//		updatedYDate = yDate + "A";
//	}
//	CmxData.setYearDate_$YDAT(updatedYDate);
//	String updatedQuery = asnLaerdalQAXAQuery.replace("$YDAT", CmxData.yearDate_$YDAT).replace("$/DAT",
//		DateUtil.getDateString(new Date(), "MM/dd"));
//	System.out.println(updatedQuery);
//	return updatedQuery;
//	
//	
//	
//	
//}



public String setLaerdal_QAXA_Query() throws IOException{
	
	
	
	asnLaerdalQAXAQuery=getLaerdalAsnQAXAQuery();
	
	Character newLastChar;
	String updatedYDate;
	String yDate = DateUtil.getDateString(new Date(), "yyMMdd").substring(1);
	String test= CmxData.yearDate_$YDAT.substring(0, 5);
	System.out.println(test);
	Character ch=CmxData.yearDate_$YDAT.charAt(CmxData.yearDate_$YDAT.length() - 1);
	
	if (CmxData.yearDate_$YDAT.contains(yDate)) {
		Character currentLastChar = CmxData.yearDate_$YDAT.charAt(CmxData.yearDate_$YDAT.length()-1 );
		
		System.out.println(currentLastChar);
		if (currentLastChar.equals('Z')) {
			newLastChar = 'A';
		} else {
			newLastChar = (char) (currentLastChar + 1);
		}
		updatedYDate = CmxData.yearDate_$YDAT.replace(currentLastChar, newLastChar);
		System.out.println(updatedYDate);
	
	} else {
		updatedYDate = yDate + "A";
	}
	CmxData.setYearDate_$YDAT( updatedYDate);
	
	Date date = new Date();  
    Timestamp ts=new Timestamp(date.getTime());  
    SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
	
	
    String s= formatter.format(ts);
    
    System.out.println(s);
	String updatedQuery = asnLaerdalQAXAQuery.replace("$YDAT",yDate).replace("$/DAT",
		DateUtil.getDateString(new Date(), "MM/dd")).replace("hhmmss",s );
//	String n
	
	System.out.println(updatedQuery);
	return updatedQuery;
	
	
	
	
}



public String setLaerdal_QAXB_Query() throws IOException{
	
	
	
	asnLaerdalQAXBQuery=getLaerdalAsnQAXBQuery();
	
	Character newLastChar;
	String updatedYDate;
	String yDate = DateUtil.getDateString(new Date(), "yyMMdd").substring(1);
	String test= CmxData.yearDate_$YDAT.substring(0, 5);
	System.out.println(test);
	Character ch=CmxData.yearDate_$YDAT.charAt(CmxData.yearDate_$YDAT.length() - 1);
	
	if (CmxData.yearDate_$YDAT.contains(yDate)) {
		Character currentLastChar = CmxData.yearDate_$YDAT.charAt(CmxData.yearDate_$YDAT.length()-1 );
		
		System.out.println(currentLastChar);
		if (currentLastChar.equals('Z')) {
			newLastChar = 'A';
		} else {
			newLastChar = (char) (currentLastChar + 1);
		}
		updatedYDate = CmxData.yearDate_$YDAT.replace(currentLastChar, newLastChar);
		System.out.println(updatedYDate);
	
	} else {
		updatedYDate = yDate + "A";
	}
	CmxData.setYearDate_$YDAT( updatedYDate);
	
	Date date = new Date();  
    Timestamp ts=new Timestamp(date.getTime());  
    SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
	
	
    String s= formatter.format(ts);
    
    System.out.println(s);
	String updatedQuery = asnLaerdalQAXBQuery.replace("$YDAT",yDate).replace("$/DAT",
		DateUtil.getDateString(new Date(), "MM/dd")).replace("hhmmss",s );
//	String n
	
	System.out.println(updatedQuery);
	return updatedQuery;
	
	
	
	
}

public String setLaerdal_QAXC_Query() throws IOException{
	
	
	
	asnLaerdalQAXCQuery=getLaerdalAsnQAXCQuery();
	
	Character newLastChar;
	String updatedYDate;
	String yDate = DateUtil.getDateString(new Date(), "yyMMdd").substring(1);
	String test= CmxData.yearDate_$YDAT.substring(0, 5);
	System.out.println(test);
	Character ch=CmxData.yearDate_$YDAT.charAt(CmxData.yearDate_$YDAT.length() - 1);
	
	if (CmxData.yearDate_$YDAT.contains(yDate)) {
		Character currentLastChar = CmxData.yearDate_$YDAT.charAt(CmxData.yearDate_$YDAT.length()-1 );
		
		System.out.println(currentLastChar);
		if (currentLastChar.equals('Z')) {
			newLastChar = 'A';
		} else {
			newLastChar = (char) (currentLastChar + 1);
		}
		updatedYDate = CmxData.yearDate_$YDAT.replace(currentLastChar, newLastChar);
		System.out.println(updatedYDate);
	
	} else {
		updatedYDate = yDate + "A";
	}
	CmxData.setYearDate_$YDAT( updatedYDate);
	
	Date date = new Date();  
    Timestamp ts=new Timestamp(date.getTime());  
    SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
	
	
    String s= formatter.format(ts);
    
    System.out.println(s);
	String updatedQuery = asnLaerdalQAXCQuery.replace("$YDAT",yDate).replace("$/DAT",
		DateUtil.getDateString(new Date(), "MM/dd")).replace("hhmmss",s );
//	String n
	
	System.out.println(updatedQuery);
	return updatedQuery;
	
	
	
	
}



public String setLaerdal_QAXD_Query() throws IOException{
	
	
	
	asnLaerdalQAXDQuery=getLaerdalAsnQAXDQuery();
	
	Character newLastChar;
	String updatedYDate;
	String yDate = DateUtil.getDateString(new Date(), "yyMMdd").substring(1);
	String test= CmxData.yearDate_$YDAT.substring(0, 5);
	System.out.println(test);
	Character ch=CmxData.yearDate_$YDAT.charAt(CmxData.yearDate_$YDAT.length() - 1);
	
	if (CmxData.yearDate_$YDAT.contains(yDate)) {
		Character currentLastChar = CmxData.yearDate_$YDAT.charAt(CmxData.yearDate_$YDAT.length()-1 );
		
		System.out.println(currentLastChar);
		if (currentLastChar.equals('Z')) {
			newLastChar = 'A';
		} else {
			newLastChar = (char) (currentLastChar + 1);
		}
		updatedYDate = CmxData.yearDate_$YDAT.replace(currentLastChar, newLastChar);
		System.out.println(updatedYDate);
	
	} else {
		updatedYDate = yDate + "A";
	}
	CmxData.setYearDate_$YDAT( updatedYDate);
	
	Date date = new Date();  
    Timestamp ts=new Timestamp(date.getTime());  
    SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
	
	
    String s= formatter.format(ts);
    
    System.out.println(s);
	String updatedQuery = asnLaerdalQAXDQuery.replace("$YDAT",yDate).replace("$/DAT",
		DateUtil.getDateString(new Date(), "MM/dd")).replace("hhmmss",s );
//	String n
	
	System.out.println(updatedQuery);
	return updatedQuery;
	
	
	
	
}



public String setLaerdal_QAXE_Query() throws IOException{
	
	
	
	asnLaerdalQAXEQuery=getLaerdalAsnQAXEQuery();
	
	Character newLastChar;
	String updatedYDate;
	String yDate = DateUtil.getDateString(new Date(), "yyMMdd").substring(1);
	String test= CmxData.yearDate_$YDAT.substring(0, 5);
	System.out.println(test);
	Character ch=CmxData.yearDate_$YDAT.charAt(CmxData.yearDate_$YDAT.length() - 1);
	
	if (CmxData.yearDate_$YDAT.contains(yDate)) {
		Character currentLastChar = CmxData.yearDate_$YDAT.charAt(CmxData.yearDate_$YDAT.length()-1 );
		
		System.out.println(currentLastChar);
		if (currentLastChar.equals('Z')) {
			newLastChar = 'A';
		} else {
			newLastChar = (char) (currentLastChar + 1);
		}
		updatedYDate = CmxData.yearDate_$YDAT.replace(currentLastChar, newLastChar);
		System.out.println(updatedYDate);
	
	} else {
		updatedYDate = yDate + "A";
	}
	CmxData.setYearDate_$YDAT( updatedYDate);
	
	Date date = new Date();  
    Timestamp ts=new Timestamp(date.getTime());  
    SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
	
	
    String s= formatter.format(ts);
    
    System.out.println(s);
	String updatedQuery = asnLaerdalQAXEQuery.replace("$YDAT",yDate).replace("$/DAT",
		DateUtil.getDateString(new Date(), "MM/dd")).replace("hhmmss",s );
//	String n
	
	System.out.println(updatedQuery);
	return updatedQuery;
	
	
	
	
}


}
