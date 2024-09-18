package xpo.qa.sc.wmx.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import xpo.qa.common.Utils;
import xpo.qa.common.selenium.PageElement;
import xpo.qa.common.selenium.WebPage;

/**
 * Landing Page for WMX
 * 
 * @author acharya.priyanka
 */

public class WmxHomePage extends WebPage {

	private PageElement wareHouseManagerText;
	private PageElement hamburgermanuWmx;
	private PageElement orderProcessmenu;
	private PageElement orderSubmenu;
	private PageElement outboundMenu;
	private PageElement verificationSubMenu;
	private PageElement mBOLSubMenu;
	
	private PageElement inboundMenu;
	
	private PageElement order;

	public WmxHomePage(WebDriver driver) {
		super(driver);

		wareHouseManagerText = new PageElement(By.xpath("//span[contains(.,'WAREHOUSE')]"));
//		hamburgermanuWmx = new PageElement(By.xpath("//button[@title='Open sidenav']//i"));
		
		hamburgermanuWmx = new PageElement(By.xpath("/html/body/app-root/xpo-shell/header/div[1]/xpo-header-hamburger/mat-icon"));
		
		orderProcessmenu = new PageElement(
//			By.xpath("//input[@name='item.APP_TITLE']/following-sibling::a[contains(.,'Order Process')]"));
			By.xpath("/html/body/app-root/xpo-shell/xpo-tab-drawer/mat-drawer-container/mat-drawer/div/div/nav/xpo-vertical-tab-group[4]/a"));
		orderSubmenu = new PageElement(
//			"//input[@name='item.APP_TITLE']/following-sibling::a[contains(.,'Order Process')]/following-sibling::ul//li[starts-with(.,' Order')]"));
				By.xpath("/html/body/app-root/xpo-shell/xpo-tab-drawer/mat-drawer-container/mat-drawer/div/div/nav/xpo-vertical-tab-group[4]/div/a[1]"));
//		outboundMenu = new PageElement(
//			By.xpath("//span[contains(@class,'xpo-VerticalTab-title')]  [contains(text(),'Outbound')] "));
		verificationSubMenu = new PageElement(By.xpath(
			"//span[contains(@class,'xpo-VerticalTab-title')]  [contains(text(),'Verification')] "));
		mBOLSubMenu = new PageElement(
			By.xpath("//span[contains(@class,'xpo-VerticalTab-title')]  [contains(text(),'MBOL')] "));
		
//		inboundMenu= new PageElement(By.xpath("//input[@name='item.APP_TITLE']/following-sibling::a[contains(.,'Inbound')]"));
		
		inboundMenu= new PageElement(By.xpath("/html/body/app-root/xpo-shell/xpo-tab-drawer/mat-drawer-container/mat-drawer/div/div/nav/xpo-vertical-tab-group[1]/a"));
		
		
		order=new PageElement(By.xpath("/html/body/app-root/xpo-shell/xpo-tab-drawer/mat-drawer-container/mat-drawer/div/div/nav/xpo-vertical-tab-group[4]/div/a[1]"));
		
		
		outboundMenu=new PageElement(
				By.xpath("//span[contains(.,'Outbound')]"));

	}

	public String getTextWarehouse() {
		return wareHouseManagerText.getText();
	}

	public void selectOrder() {
		hamburgermanuWmx.click();
		Utils.sleep(2000);
//		orderProcessmenu.moveClickSendKeys("");
		orderProcessmenu.click();
		Utils.sleep(2000);
//		orderSubmenu.moveClickSendKeys("");
		order.click();
		System.out.println("clicked order");
		Utils.sleep(5000);
	}

	public void selectVerification() {
		hamburgermanuWmx.click();
		Utils.sleep(2000);
		outboundMenu.moveClickSendKeys("");
		Utils.sleep(2000);
		verificationSubMenu.moveClickSendKeys("");
		Utils.sleep(5000);
	}

	public void selectMBOL() {
		hamburgermanuWmx.click();
		Utils.sleep(2000);
		outboundMenu.moveClickSendKeys("");
		Utils.sleep(2000);
		mBOLSubMenu.moveClickSendKeys("");
		Utils.sleep(5000);
	}
	
	
	public void selectInboundMenu() {
		
		hamburgermanuWmx.click();
		Utils.sleep(2000);
		inboundMenu.moveClickSendKeys("");
		Utils.sleep(2000);
		
	}
	
	
	
}