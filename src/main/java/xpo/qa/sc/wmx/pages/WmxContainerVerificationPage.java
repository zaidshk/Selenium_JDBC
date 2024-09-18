package xpo.qa.sc.wmx.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

import xpo.qa.common.Utils;
import xpo.qa.common.selenium.PageElement;
import xpo.qa.common.selenium.WebPage;

public class WmxContainerVerificationPage extends WebPage{
	
	private PageElement docTypeDropDown;
	private PageElement dropIdInput;
	private PageElement containerDetails;
	private PageElement caseID;
	private PageElement caseIDInput;
	private PageElement length;
	private PageElement height;
	private PageElement width;
	private PageElement saveButton;
	private PageElement closeButton;
	private PageElement closeError;
	private PageElement hamburgermanuWmx;
	private PageElement outBoundMenu;
	private PageElement containerMenu;
	private PageElement verificationMenu;
	private PageElement closeButtonNoPrinterError;
	
	
	
	
	
	
	
	
	
	public WmxContainerVerificationPage(WebDriver driver) {
		super(driver);
		docTypeDropDown=new PageElement(By.xpath("//*[@name='docType']"));
//		docTypeDropDown =new PageElement(By.xpath("/html/body/app-root/mat-sidenav-container/mat-sidenav-content/div/main/app-parent-container-verification/app-container-verification/div/div/div[2]/div/div/div/div/div/div[1]/div[2]/div[1]/div/select"));
		
		dropIdInput= new PageElement(By.xpath("//*[@id='str_dockey']"));
		
//		containerDetails= new PageElement(By.xpath("//span[contains(.,'Verify Detail')]/following-sibling::i"));
		
		containerDetails=new PageElement(By.xpath("/html/body/app-root/mat-sidenav-container/mat-sidenav-content/div/main/app-parent-container-verification/app-container-verification/div/div/div[2]/div/div/div/div/div/div[3]/div/div/accordion/accordion-group/div/div[1]/div/div/div/span"));
		
		caseID=new PageElement(By.xpath("//table/tbody/tr/td[2]/span"));
		caseIDInput= new PageElement (By.xpath("//*[@name='scanCaseID']"));
	    length=new 	PageElement (By.xpath("//*[@name='LENGTH']"));
	    width=new PageElement (By.xpath("//*[@name='WIDTH']"));
	    height=new PageElement (By.xpath("//*[@name='HEIGHT']"));
	    saveButton= new PageElement(By.xpath("/html/body/app-root/mat-sidenav-container/mat-sidenav-content/div/main/app-parent-container-verification/app-container-verification/div/div/div[2]/div/div/div/div/div/div[2]/app-sub-header/div/div/div/div/div[1]/button"));
	    closeButton=new PageElement(By.xpath("/html/body/app-root/mat-sidenav-container/mat-sidenav-content/div/main/app-parent-container-verification/app-container-verification/div/div/div[2]/div/div/div/div/div/div[2]/app-sub-header/div/div/div/div/div[2]/button"));
	    closeError= new PageElement(By.xpath("/html/body/app-root/mat-sidenav-container/mat-sidenav-content/div/main/app-parent-container-verification/app-container-verification/div/div/div[1]/app-notification/div/div/div/app-detail-popup-title/div/div[1]/div[3]/span/"));
	    hamburgermanuWmx = new PageElement(By.xpath("/html/body/app-root/xpo-shell/header/div[1]/xpo-header-hamburger/mat-icon"));
//	    outBoundMenu= new PageElement(
//				By.xpath("/html/body/app-root/mat-sidenav-container/mat-sidenav/div/div[1]/app-wmx-menu/div/ul/app-wmx-menu-dyn/div[5]/li/input"));
	    outBoundMenu=new PageElement(
				By.xpath("//span[contains(.,'Outbound')]"));
	    
	    closeButtonNoPrinterError = new PageElement(By.xpath("//span[@class='wmx-close']"));
	    
	    containerMenu =new PageElement(
				By.xpath("//span[contains(.,'Container')]"));
	    
	    verificationMenu= new PageElement(
				By.xpath("//*[@href='/containerverification']"));
	    
//	    verificationMenu=new PageElement(By.partialLinkText("/containerverification"));
	}
	
	
	public void selectContainerVerification() {
		hamburgermanuWmx.click();
		Utils.sleep(2000);
		outBoundMenu.click();
		Utils.sleep(2000);
		containerMenu.click();
		Utils.sleep(2000);
		verificationMenu.click();
		Utils.sleep(2000);
	}
	
	
	public void selectDocType() {
		
		docTypeDropDown.selectDropDownOptionByVisibleText("Drop ID");
		
		
	}
	
	public void setDropIdInput(String DropID) {
		dropIdInput.clear();
		dropIdInput.sendKeys(DropID);
		Utils.sleep(2000);
		dropIdInput.sendKeys(Keys.ENTER);
		Utils.sleep(3000);
		
	}
	
	public void verifyContainer() {
		
		containerDetails.scrollToElement();
		containerDetails.moveClickSendKeys("");
		
		
		Utils.sleep(2000);
		
////		String caseIdValue=caseID.getElementText();
////		
////		caseIDInput.sendKeys(caseIdValue);
////		
////		Utils.sleep(2000);
////		
////		
////		caseIDInput.sendKeys(Keys.ENTER);
//		
		Utils.sleep(2000);
		
		
		
		length.sendKeys(Keys.BACK_SPACE);
		
		length.sendKeys("1");
		
		Utils.sleep(2000);

		width.sendKeys(Keys.BACK_SPACE);
		
		width.sendKeys("1");
		
		Utils.sleep(2000);
		
        height.sendKeys(Keys.BACK_SPACE);
		
        height.sendKeys("1");
		
		Utils.sleep(2000);
		
		saveButton.click();
		
		Utils.sleep(8000);
		
		closeButton.click();
		
		Utils.sleep(5000);
		
		
		closeButtonNoPrinterError.click();
		
		Utils.sleep(2000);
		
		
//		closeError.click();
//		
//		Utils.sleep(2000);
		
	}
	
	public boolean isOrderVerificationSuccessful() {
		return getPageSubElements(By.xpath("//tr[@class='LightGreen ng-star-inserted']")).size() > 0;
	}

}
