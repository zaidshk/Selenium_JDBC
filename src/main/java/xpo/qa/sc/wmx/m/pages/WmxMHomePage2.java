package xpo.qa.sc.wmx.m.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import xpo.qa.common.Utils;
import xpo.qa.common.selenium.PageElement;
import xpo.qa.common.selenium.WebPage;

/**
 * Landing Page for WMX Mobile
 * 
 * @author acharya.priyanka
 */

public class WmxMHomePage2 extends WebPage {

	private PageElement wareHouseManagerText;
	private PageElement hamburgermanuWmxM;
	private PageElement outboundMenu;
	private PageElement pickByOrderSubmenu;
	private PageElement pickOrderToDropIdSubmenu;
	private PageElement completeDropIdSubmenu;
	private PageElement orderPick;
	private PageElement piecePickToDropID;
	private PageElement inboundMenu;
	private PageElement putawayMenu;
	private PageElement orderPickMenu;

	public WmxMHomePage2(WebDriver driver) {
		super(driver);

		wareHouseManagerText = new PageElement(By.xpath("//span1[contains(.,'WAREHOUSE')]"));
//		hamburgermanuWmxM = new PageElement(By.cssSelector("mat-icon[title='menu']"));
		hamburgermanuWmxM = new PageElement(By.xpath("//mat-icon[.='menu']"));
		
		outboundMenu = new PageElement(
			By.xpath("//button[contains(.,'Outbound')]"));
		pickByOrderSubmenu = new PageElement(By.xpath(
			"//button[contains(.,'Pick By Order')]"));
		pickOrderToDropIdSubmenu = new PageElement(By.xpath(
			"//button[contains(.,'Pick Order to Drop ID')]"));
		completeDropIdSubmenu = new PageElement(By.xpath(
			"//button[contains(.,'Complete Drop ID')]"));
		
//		orderPick= new PageElement(By.xpath("/html/body/app-root/xpo-shell/xpo-tab-drawer/mat-drawer-container/mat-drawer/div/div/nav/xpo-vertical-tab-group[2]/a"));
		
		
		piecePickToDropID= new PageElement(By.xpath("//span[contains(.,'Piece Pick To Drop ID')]"));
		
//		inboundMenu=new PageElement(
//				By.xpath("//button[contains(.,'Inbound')]"));
		
		inboundMenu=new PageElement(
				By.xpath("//span[contains(.,'Inbound')]"));
		
		
		
		orderPickMenu=new PageElement(
				By.xpath("//span[contains(.,'Order Pick')]"));
		
		
		putawayMenu= new PageElement(
				By.xpath("//span[contains(.,'Putaway')]"));
		
		
	}

	public String getTextWarehouse() {
		return wareHouseManagerText.getText();
	}
	
	

	public void selectPickByOrder() {
		hamburgermanuWmxM.click();
		Utils.sleep(2000);
		outboundMenu.moveClickSendKeys("");
		Utils.sleep(2000);
		pickByOrderSubmenu.moveClickSendKeys("");
		Utils.sleep(5000);
	}

	public void selectPickOrderToDropId() {
		hamburgermanuWmxM.click();
		Utils.sleep(2000);
		outboundMenu.moveClickSendKeys("");
		Utils.sleep(2000);
		pickOrderToDropIdSubmenu.moveClickSendKeys("");
		Utils.sleep(5000);
	}
	
	public void selectPickEachToDropID() {
		
		hamburgermanuWmxM.click();
		Utils.sleep(2000);
		orderPickMenu.moveClickSendKeys("");
		Utils.sleep(2000);
		piecePickToDropID.click();
		Utils.sleep(2000);
		
		
		
	}

	public void selectComplteDropId() {
		hamburgermanuWmxM.click();
		Utils.sleep(2000);
		outboundMenu.moveClickSendKeys("");
		Utils.sleep(2000);
		completeDropIdSubmenu.moveClickSendKeys("");
		Utils.sleep(5000);
	}
	
	
	public void selectPutaway() {
		hamburgermanuWmxM.click();
		Utils.sleep(2000);
		inboundMenu.click();
		Utils.sleep(2000);
		putawayMenu.click();
		Utils.sleep(2000);
		
		
		
	}

}