package xpo.qa.sc.wmx.m.pages;


import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

import xpo.qa.common.Utils;
import xpo.qa.common.databases.oracle.OracleDB;
import xpo.qa.common.selenium.PageElement;
import xpo.qa.common.selenium.WebPage;
import xpo.qa.sc.wmx.data.CmxData;
import xpo.qa.sc.wmx.data.WmxData;
public class WmxMPickPieceToDropID extends WebPage {
	
	private PageElement dropIdInput;
	private PageElement orderKeyInput;
	private PageElement searchButton;
	
	private PageElement location;
	private PageElement lot;
	private PageElement pickQty;
	private PageElement caseId;
	private PageElement dropIdEnable;
	
	private PageElement locValue;
	private PageElement lotValue;
	private PageElement caseIDValue;
	private PageElement pickQtyValue;
	private PageElement processButton;
	private PageElement sku;
	private PageElement skuValue;
	String orderkey="";
	
	

	
	
	public WmxMPickPieceToDropID(WebDriver driver){
		super(driver);

		dropIdInput = new PageElement(By.xpath("//input[@id='DROPID']"));
		orderKeyInput = new PageElement(By.xpath("//input[@placeholder='ORDERKEY']"));
		searchButton = new PageElement(By.xpath("//button[contains(.,'Search')]"));
		
		location=new PageElement(By.xpath("//*[@id='Locconf'] "));
		lot= new PageElement(By.xpath("//*[@id='Lotconf']"));
		pickQty=new PageElement(By.xpath("//*[@id='Quantityconf']"));
		caseId= new PageElement(By.xpath("//*[@id='CaseidConf']"));
		
		dropIdEnable= new PageElement(By.xpath("//*[@name='Dropidcmpflag']"));
		
		
		
		locValue = new PageElement(
				By.xpath("//span[contains(.,'LOC:')]/parent::div/following-sibling::div"));
		
		lotValue= new PageElement(
				By.xpath("//span[contains(.,'LOT:')]/parent::div/following-sibling::div"));
		
		
		caseIDValue= new PageElement(
				By.xpath("/html/body/app-root/mat-sidenav-container/mat-sidenav-content/div/main/app-rfpick/app-rf-pick-search/div/app-rf-pick-details-std/div/div/form/div[10]/div[2]"));
		//span[contains(.,'To Case ID:')]/parent::div/following-sibling::div/div[1]/span
		
		
//		By.xpath();
		
		pickQtyValue= new PageElement(
				By.xpath("//span[contains(.,'Pick Qty')]/parent::div/following-sibling::div/div[1]/span"));
		
		
		processButton = new PageElement(
				By.xpath("//span[contains(.,'Process')]"));
		
		
		sku=new PageElement(By.xpath("//*[@id='Skuconf']"));
		
		skuValue=new PageElement(By.xpath("/html/body/app-root/xpo-shell/xpo-tab-drawer/mat-drawer-container/mat-drawer-content/xpo-tab-drawer-content/app-rfpick/app-rf-pick-search/div/app-rf-pick-details-std/div/div/form/div[4]/div[2]/span[2]"));
		
	}
	
	
	
	public void setPickOrderToDropId(String dropId, String orderKey) {
		orderKeyInput.click();
		Utils.sleep(2000);
		orderKeyInput.sendKeys(orderKey);
		Utils.sleep(2000);
		orderKeyInput.sendKeys(Keys.TAB);
//		Utils.sleep(3000);
//		dropIdInput.click();
//		Utils.sleep(2000);
//		dropIdInput.sendKeys(dropId);
//		Utils.sleep(2000);
//		dropIdInput.sendKeys(Keys.TAB);
//		Utils.sleep(8000);
//		
		
		
		
//		dropIdInput.click();
//		Utils.sleep(2000);
//		dropIdInput.sendKeys(dropId);
//		Utils.sleep(2000);
//		orderKeyInput.click();
//		Utils.sleep(2000);
//		orderKeyInput.sendKeys(orderKey);
//		Utils.sleep(5000);
		
		
//        searchButton.click();
		Utils.sleep(8000);
	}

	public String getDropIdText() {
		return dropIdInput.getElementText();
	}
	
	
	
	public void processOrderPick(String caseID) {
		
		
		location.click();
		Utils.sleep(2000);
		location.sendKeys(locValue.getElementText());
		location.sendKeys(Keys.BACK_SPACE);
		location.sendKeys(Keys.BACK_SPACE);
		location.sendKeys(Keys.TAB);
		
		Utils.sleep(2000);
		sku.click();
		Utils.sleep(2000);
		sku.sendKeys("01QARE01            10QAR   ");
//		sku.sendKeys(Keys.BACK_SPACE);
//		sku.sendKeys(Keys.BACK_SPACE);
		sku.sendKeys(Keys.TAB);
		
		Utils.sleep(2000);
		lot.click();
		Utils.sleep(2000);
		lot.sendKeys("01QARE01            10QAR   ");
//		lot.sendKeys(Keys.BACK_SPACE);
//		lot.sendKeys(Keys.BACK_SPACE);
		lot.sendKeys(Keys.TAB);
		
		Utils.sleep(2000);
		pickQty.click();
		Utils.sleep(2000);
		pickQty.sendKeys(Keys.BACK_SPACE);
		pickQty.sendKeys(pickQtyValue.getElementText());
		pickQty.sendKeys(Keys.TAB);
		
		Utils.sleep(2000);
		dropIdEnable.click();
		Utils.sleep(2000);
		caseId.click();
		Utils.sleep(2000);
		caseId.sendKeys(caseIDValue.getElementText());
//		caseId.sendKeys(Keys.BACK_SPACE);
//		caseId.sendKeys(Keys.BACK_SPACE);
		caseId.sendKeys(Keys.TAB);
		Utils.sleep(3000);
		
		
		
		
		
		
//		processButton.waitClickable();
//		processButton.click();
		
		
//		processButton.waitClickable();
//		processButton.click();
		Utils.sleep(8000);
	}

	
	

	

	
	public void setLoc(String Loc) {
		
		location.click();
		Utils.sleep(2000);
		location.sendKeys(Loc);
		Utils.sleep(2000);
		
	}
	
	
	public void setLot(String Lot) {
		
		lot.click();
		Utils.sleep(2000);
		lot.sendKeys(Lot);
		Utils.sleep(2000);
		
	}

	
	public void setPickQty(String qty) {
		
		pickQty.click();
		Utils.sleep(2000);
		pickQty.sendKeys(qty);
		Utils.sleep(2000);
	
	}
	
	public void setCaseID(String caseID) {
		
		caseId.click();
		Utils.sleep(2000);
		caseId.sendKeys(caseID);
		
		
	}
	
	public void enableCompleteDropID() {
		dropIdEnable.click();
		Utils.sleep(2000);
		
	}
	
	
	
	
	


	private void reportStatusFail(String message) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}
