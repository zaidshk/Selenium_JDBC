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



public class WmxMPutaway extends WebPage {
	
	private PageElement putawaySearch;
	private PageElement lpnField;
	private PageElement processButton;
	
	private PageElement destValue;
	private PageElement lpnValue;
	
	private PageElement locationField;
	
	private PageElement lpnConfField;
	
	private PageElement locationFieldMultiPutaway;
	
	private PageElement toLPN;
	
	private PageElement yesButton;
	
	
	
	public WmxMPutaway(WebDriver driver) {
		
		super(driver);
		
		
		
		putawaySearch= new PageElement(By.xpath("//a[@href='/rf-putaway/search']"));
		lpnField= new PageElement(By.xpath("//input[@placeholder='LPN']"));
//		orderKeyInput = new PageElement(By.xpath(
		
		processButton = new PageElement(By.xpath("//button[contains(.,'Process')]"));
		
		
		destValue = new PageElement(
				By.xpath("/html/body/app-root/mat-sidenav-container/mat-sidenav-content/div/main/app-rfputaway/app-rfputaway-search/div/app-rfputaway-details/div/div/form/div[3]/div/div/span[2]"));
		
		lpnValue = new PageElement(
				By.xpath("/html/body/app-root/xpo-shell/xpo-tab-drawer/mat-drawer-container/mat-drawer-content/xpo-tab-drawer-content/app-rfputaway/app-rfputaway-search/div/app-rfputaway-details/div/div/form/div[2]/div/div/span[2]"));
		
		
		locationField= new PageElement(By.xpath("//*[@id='mat-input-1']"));
		
		lpnConfField= new PageElement(By.xpath("//input[@placeholder='To LPN']"));
		
		locationFieldMultiPutaway= new PageElement(By.xpath("//*[@id=\"mat-input-1\"]"));
		
		toLPN= new PageElement(By.xpath("//*[@id=\"mat-input-2\"]"));
		
		yesButton = new PageElement(By.xpath("/html/body/div[2]/div[2]/div/mat-dialog-container/app-confirm-dialog/mat-dialog-actions/button[1]/span"));
		
	}
	
  public void selectPutawaySearch() {
	  
	  putawaySearch.click();
	  
	  Utils.sleep(5000);
  } 
  
  
  public void setLpn(String lpn) {
	  
	  lpnField.displayed();
	  Utils.sleep(3000);
	  lpnField.click();
	  Utils.sleep(3000);
	  lpnField.sendKeys(lpn);
	  Utils.sleep(3000);
	  processButton.click();
	  Utils.sleep(3000);
	  
	  
  }
  
  public void executePutaway() {
	  locationField.click();
	  Utils.sleep(3000);
//	  locationField.sendKeys(destValue.getText()); 
	  locationField.sendKeys("QARE03");
//	  Utils.sleep(3000);
//	  lpnConfField.click();
//	  Utils.sleep(3000);
//	  lpnConfField.sendKeys(lpnValue.getText());
//	  Utils.sleep(3000);
	  processButton.click();
	  
	  
	  
  }
  
  
  public void executePutawayForLaerdal(String loc) {
	  locationField.click();
	  Utils.sleep(3000);
//	  locationField.sendKeys(destValue.getText()); 
	  locationField.sendKeys(loc);
//	  Utils.sleep(3000);
//	  lpnConfField.click();
//	  Utils.sleep(3000);
//	  lpnConfField.sendKeys(lpnValue.getText());
//	  Utils.sleep(3000);
	  processButton.click();
	  Utils.sleep(9000);
	  yesButton.click();
	  
	    
	  
  }
  
  
  
  
  public void executePutawayForMultipleLines(String lpn, String loc) {
	  locationFieldMultiPutaway.click();
	  Utils.sleep(3000);
	  locationFieldMultiPutaway.sendKeys(loc); 
	  Utils.sleep(3000);
	  toLPN.click();
	  Utils.sleep(3000);
	  toLPN.sendKeys(lpn);
	  Utils.sleep(3000);
	  processButton.click();
	  
	  
	  
  }
  
	
}
