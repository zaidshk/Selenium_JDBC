package xpo.qa.sc.wmx.pages;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

import xpo.qa.common.Utils;
import xpo.qa.common.selenium.PageElement;
import xpo.qa.common.selenium.WebPage;

public class WmxReceiptPage extends WebPage {
	
	private PageElement receiptMenu;
	private PageElement newButton;
	private PageElement clientIDDropDown;
	private PageElement docTypeDropDown;
	private PageElement externalkeyInput;
	private PageElement asnSearchResultTableEditOrderSymbol;
	private PageElement expectedQty;
	private PageElement uomQty;
	private PageElement saveButton;
	private PageElement closeButton1;
	
	private PageElement closeButton2;
	
	
	private PageElement saveButton1;
	
	private PageElement receiptButton;
	
	private PageElement confirmationYesButton;
	
	private PageElement closeButtonNoPrinterError;
	
	private PageElement statusFieldValue;
	
	private PageElement checkLPN;
	
	private PageElement lpnField;
	
	private PageElement lotAttr3;
	
	
	private PageElement editASN1;
	
	private PageElement editASN2;
	
	private PageElement editASN3;
	
	private PageElement editASN4;
	
	private PageElement serialNumber;
	
	public WmxReceiptPage(WebDriver driver) {
		
		super(driver);
		
//		receiptMenu=new PageElement(By.xpath(
//				"//input[@name='item.APP_TITLE']/following-sibling::a[contains(.,'Inbound')]/following-sibling::ul//li[starts-with(.,' Receipt')]"));
		
		receiptMenu=new PageElement(By.xpath("//span[contains(.,'Receipt')]"));
		
		
		newButton=new PageElement(By.xpath("//button[@class='wmx-button ng-star-inserted']")) ;
				
		clientIDDropDown= new PageElement(By.xpath("//*[@name='CLIENTID']"));
		
		docTypeDropDown= new PageElement(By.xpath("//*[@name='doctype']"));
		
		externalkeyInput = new PageElement(By.xpath("//label[contains(.,'Extern Key')]/following-sibling::input"));
		
//		asnSearchResultTableEditOrderSymbol= new PageElement(By.xpath("//tbody/tr/td[1]/i"));
		
		asnSearchResultTableEditOrderSymbol= new PageElement(By.xpath("//tbody/tr/td[1]"));
//		/xpo-icon/mat-icon/svg
		
//		asnSearchResultTableEditOrderSymbol= new PageElement(By.xpath("//*[@id='Color']"));
		
		expectedQty= new PageElement(By.xpath("\\input[@name='EXPECTEDQTY' ]"));
		
		uomQty= new PageElement(By.xpath("//input[@name='UOMQTY']"));
		
		saveButton=new PageElement(By.xpath("/html/body/modal-container/div/div/app-receiving-detail-new/div/div[2]/app-detail-popup-footer/div/div/div[3]/button[1]"));
		
//		closeButton1=new PageElement(By.xpath("/html/body/modal-container/div/div/app-receiving-detail-new/div/app-detail-popup-title/div/div[1]/div[3]/span/i"));
		
		closeButton1=new PageElement(By.xpath("/html/body/modal-container/div/div/app-receiving-detail-new/div/app-detail-popup-title/div/div[1]/div[3]/span"));
		
		
//		closeButton2=new PageElement(By.xpath("/html/body/modal-container/div/div/app-receiving-detail-new-grid/div[2]/app-detail-popup-title/div/div[1]/div[3]/span/i"));
		
		
		closeButton2=new PageElement(By.xpath("/html/body/modal-container/div/div/app-receiving-detail-new-grid/div[2]/app-detail-popup-title/div/div[1]/div[3]/span"));
		
		saveButton1= new PageElement(By.xpath("//button[contains(text(),'Save')]"));
		
		receiptButton=new PageElement(By.xpath("//button[contains(text(),'Receive')]"));
		
		confirmationYesButton = new PageElement(By.xpath("//button[contains(.,'Yes')]"));
		
		closeButtonNoPrinterError = new PageElement(By.xpath("//span[@class='wmx-close']"));
		
		statusFieldValue = new PageElement(By.xpath("//select[@name='STATUS']"));
		
		lpnField= new PageElement(By.xpath("//input[@name='LPN']"));
		
		checkLPN= new PageElement(By.xpath("/html/body/modal-container/div/div/app-receiving-detail-new/div/div[1]/div[2]/div/div/div/form/fieldset/tabset/div/tab[1]/div/div/div/div/div[2]/div/div[2]/div[4]/div/div[2]/label"));
		
		editASN1= new PageElement(By.xpath("//tbody/tr[1]/td[2]"));
		
		editASN2= new PageElement(By.xpath("//tbody/tr[2]/td[2]"));
		
		editASN3= new PageElement(By.xpath("//tbody/tr[3]/td[2]"));
		
		editASN4= new PageElement(By.xpath("//tbody/tr[4]/td[2]"));
		
//		lotAttr3= new PageElement(By.xpath("/html/body/modal-container/div/div/app-receiving-detail-new/div/div[1]/div[2]/div/div/div/form/fieldset/tabset/div/tab[1]/div/div/div/div/div[4]/div/div[2]/div[4]/app-wx-lot-attr-control/input"));
		
		
		lotAttr3=new PageElement (By.xpath("/html/body/modal-container/div/div/app-receiving-detail-new/div/div[1]/div[2]/xpo-card/div[2]/xpo-card-content/form/fieldset/div[1]/div/div/div/div[4]/div/div[2]/div[4]/app-wx-lot-attr-control/input"));
		
		serialNumber=new PageElement(By.xpath("/html/body/modal-container/div/div/app-receiving-detail-new/div/div[1]/div[2]/xpo-card/div[2]/xpo-card-content/form/fieldset/div[1]/div/div/div/div[4]/div/div[2]/div[3]/app-wx-lot-attr-control/input"));
		
	}
	
	
	public void selectReceiptMenu() {
		
		receiptMenu.moveClickSendKeys("");
		Utils.sleep(4000);
		
		
	}
	
	
	public void  createReceipt() {
		
		newButton.moveClickSendKeys("");
		Utils.sleep(50000);
		clientIDDropDown.click();
		clientIDDropDown.selectDropDownOptionByVisibleText("1616-L'OREAL USA S/D, INC.");
		Utils.sleep(10000);
		docTypeDropDown.selectDropDownOptionByVisibleText("ASN");
		
		
	}
	
	
public void  createReceiptForLaerdal() {
		
		newButton.moveClickSendKeys("");
		Utils.sleep(50000);
		clientIDDropDown.click();
		clientIDDropDown.selectDropDownOptionByVisibleText("1018-Laerdal Medical");
		Utils.sleep(10000);
		docTypeDropDown.selectDropDownOptionByVisibleText("ASN");
		
		
	}
	
	public String searchExternKey(String ExternKey) {
		
		externalkeyInput.clear();
		externalkeyInput.sendKeys(ExternKey);
		Utils.sleep(3000);
		externalkeyInput.sendKeys(Keys.ENTER);
		Utils.sleep(5000);
		
		return ExternKey;
	} 

	public void clickOnEditButton() {
		asnSearchResultTableEditOrderSymbol.click();
		Utils.sleep(5000);
	}
	
  
	
	public void clickOnNewButton() {
		
		newButton.click();
		
	}
	
    public void setQty(String qty) {
    	
    	Date date=new Date();
    	
    	SimpleDateFormat fm=new SimpleDateFormat("yyyyssMM");
    	String dropIdDate=fm.format(date);
    	
    	String dropPrefix="22";
    	
    	String lpn= dropPrefix + dropIdDate;
    	
    	uomQty.sendKeys(Keys.BACK_SPACE);
    	
    	Utils.sleep(3000);
    	
    	uomQty.sendKeys(qty);
    	
    	Utils.sleep(3000);
    	
    	lpnField.clear();
    	
    	Utils.sleep(3000);
    	
    	lpnField.sendKeys(lpn);
    	
    	Utils.sleep(3000);
    	
    	lotAttr3.sendKeys("LOREAL");
    	
    	Utils.sleep(3000);
    	
    	saveButton.click();
    	
    	Utils.sleep(4000);
    	
    	closeButton1.click();
    	
    	Utils.sleep(4000);
    	
    	closeButton2.click();
    	
    	Utils.sleep(4000);
    	
    	
    	
    }
    
    
    
    public void setSerialNumber(String serialnumber) {
    	
    	Utils.sleep(5000);
    	
    	serialNumber.sendKeys(serialnumber);
    	
    	
    }
    
    
    
public void setQtyforLaerdalASN(String qty) {
    	
    	Date date=new Date();
    	
    	SimpleDateFormat fm=new SimpleDateFormat("yyyyssMM");
    	String dropIdDate=fm.format(date);
    	
    	String dropPrefix="22";
    	
    	String lpn= dropPrefix + dropIdDate;
    	
    	uomQty.sendKeys(Keys.BACK_SPACE);
    	
    	Utils.sleep(3000);
    	
    	uomQty.sendKeys(qty);
    	
    	Utils.sleep(3000);
    	
//    	lpnField.clear();
//    	
//    	Utils.sleep(3000);
//    	
//    	lpnField.sendKeys(lpn);
//    	
//    	Utils.sleep(3000);
//    	
//    	lotAttr3.sendKeys("LOREAL");
    	
    	Utils.sleep(3000);
    	
    	saveButton.click();
    	
    	Utils.sleep(4000);
    	
    	closeButton1.click();
    	
    	Utils.sleep(4000);
    	
    	closeButton2.click();
    	
    	Utils.sleep(4000);
    	
    	
    	
    }
    
    
    
    public void createReceiptForASN(String qty) {
    	
    	Date date=new Date();
    	
    	SimpleDateFormat fm=new SimpleDateFormat("yyyyssMM");
    	String dropIdDate=fm.format(date);
    	
    	String dropPrefix="22";
    	
    	String lpn= dropPrefix + dropIdDate;
    	
    	editASN1.click();
    	
    	Utils.sleep(5000);
    	
    	uomQty.sendKeys(Keys.BACK_SPACE);
    	
    	Utils.sleep(3000);
    	
    	uomQty.sendKeys(qty);
    	
    	Utils.sleep(3000);
    	
    	lpnField.clear();
    	
    	Utils.sleep(3000);
    	
    	lpnField.sendKeys(lpn);
    	
    	Utils.sleep(3000);
    	
    	// New Addition
    	
    	lotAttr3.sendKeys("LOREAL");
    	
    	
    	Utils.sleep(3000);
    	
    	
    	saveButton.click();
    	
    	Utils.sleep(4000);
    	
    	closeButton1.click();
    	
    	Utils.sleep(8000);
    	
    	editASN2.click();
    	
    	Utils.sleep(5000);
    	
    	uomQty.sendKeys(Keys.BACK_SPACE);
    	
    	Utils.sleep(3000);
    	
    	uomQty.sendKeys(qty);
    	
    	Utils.sleep(3000);
    	
    	lpnField.clear();
    	
    	Utils.sleep(3000);
    	
    	lpnField.sendKeys(lpn);
    	
    	Utils.sleep(3000);
    	
    	lotAttr3.sendKeys("LOREAL");
    	
    	
    	
    	
    	Utils.sleep(3000);
    	
    	saveButton.click();
    	
    	Utils.sleep(4000);
    	
    	closeButton1.click();
    	
    	Utils.sleep(8000);
    	
    	
    	editASN3.click();
    	
    	Utils.sleep(5000);
    	
    	uomQty.sendKeys(Keys.BACK_SPACE);
    	
    	Utils.sleep(3000);
    	
    	uomQty.sendKeys(qty);
    	
    	Utils.sleep(3000);
    	
    	lpnField.clear();
    	
    	Utils.sleep(3000);
    	
    	lpnField.sendKeys(lpn);
    	
    	Utils.sleep(3000);
    	
    	lotAttr3.sendKeys("LOREAL");
    	
    	Utils.sleep(3000);
    	
    	saveButton.click();
    	
    	Utils.sleep(4000);
    	
    	closeButton1.click();
    	
    	Utils.sleep(8000);
    	
    	
    	closeButton2.click();
    	
    	Utils.sleep(4000);
    	
    	
    }
    
    
public void createReceiptForLaerdalASNQAXD() {
    	
    	Date date=new Date();
    	
    	SimpleDateFormat fm=new SimpleDateFormat("yyyyssMM");
    	String dropIdDate=fm.format(date);
    	
    	String dropPrefix="22";
    	
    	String lpn= dropPrefix + dropIdDate;
    	
    	editASN1.click();
    	
    	Utils.sleep(5000);
    	
    	uomQty.sendKeys(Keys.BACK_SPACE);
    	
    	Utils.sleep(3000);
    	
    	uomQty.sendKeys("1");
    	
    	Utils.sleep(3000);
    	
    	lpnField.clear();
    	
    	Utils.sleep(3000);
    	
    	lpnField.sendKeys(lpn);
    	
    	Utils.sleep(3000);
    	
    	// New Addition
    	
    	lotAttr3.sendKeys("LOREAL");
    	
    	
    	Utils.sleep(3000);
    	
    	
    	saveButton.click();
    	
    	Utils.sleep(4000);
    	
    	closeButton1.click();
    	
    	Utils.sleep(8000);
    	
    	editASN2.click();
    	
    	Utils.sleep(5000);
    	
    	uomQty.sendKeys(Keys.BACK_SPACE);
    	
    	Utils.sleep(3000);
    	
    	uomQty.sendKeys("2");
    	
    	Utils.sleep(3000);
    	
    	lpnField.clear();
    	
    	Utils.sleep(3000);
    	
    	lpnField.sendKeys(lpn);
    	
    	Utils.sleep(3000);
    	
    	lotAttr3.sendKeys("LOREAL");
    	
    	
    	
    	
    	Utils.sleep(3000);
    	
    	saveButton.click();
    	
    	Utils.sleep(4000);
    	
    	closeButton1.click();
    	
    	Utils.sleep(8000);
    	
    	
    	editASN3.click();
    	
    	Utils.sleep(5000);
    	
    	uomQty.sendKeys(Keys.BACK_SPACE);
    	
    	Utils.sleep(3000);
    	
    	uomQty.sendKeys("1");
    	
    	Utils.sleep(3000);
    	
    	lpnField.clear();
    	
    	Utils.sleep(3000);
    	
    	lpnField.sendKeys(lpn);
    	
    	Utils.sleep(3000);
    	
    	lotAttr3.sendKeys("LOREAL");
    	
    	Utils.sleep(3000);
    	
    	saveButton.click();
    	
    	Utils.sleep(4000);
    	
    	closeButton1.click();
    	
    	Utils.sleep(8000);
    	
    	
    	
    	
    	
    	editASN4.click();
    	
    	Utils.sleep(5000);
    	
    	uomQty.sendKeys(Keys.BACK_SPACE);
    	
    	Utils.sleep(3000);
    	
    	uomQty.sendKeys("2");
    	
    	Utils.sleep(3000);
    	
    	lpnField.clear();
    	
    	Utils.sleep(3000);
    	
    	lpnField.sendKeys(lpn);
    	
    	Utils.sleep(3000);
    	
    	lotAttr3.sendKeys("LOREAL");
    	
    	Utils.sleep(3000);
    	
    	saveButton.click();
    	
    	Utils.sleep(4000);
    	
    	closeButton1.click();
    	
    	Utils.sleep(8000);
    	
    	
    	closeButton2.click();
    	
    	Utils.sleep(4000);
    	
    	
    }
    
    
    
    
    public void clickOnSaveButton() {
    	
    	saveButton1.click();
    	Utils.sleep(60000);
    	
    }
    
    public void receive() {
    	receiptButton.click();
    	Utils.sleep(60000);
    	confirmationYesButton.click();
    	Utils.sleep(30000);
    	closeButtonNoPrinterError.click();
    	Utils.sleep(9000);
    	
    }
    
    public String getStatusFieldValue() {
		return statusFieldValue.getFirstSelectedOptionText();
	}
	
}
