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

public class WmxMHomePage extends WebPage {

	private PageElement wareHouseManagerText;
	private PageElement hamburgermanuWmxM;
	private PageElement outboundMenu;
	private PageElement pickByOrderSubmenu;
	private PageElement pickOrderToDropIdSubmenu;
	private PageElement completeDropIdSubmenu;

	public WmxMHomePage(WebDriver driver) {
		super(driver);

		wareHouseManagerText = new PageElement(By.xpath("//span[contains(.,'WAREHOUSE')]"));
		hamburgermanuWmxM = new PageElement(By.cssSelector("mat-icon[title='Menu']"));
		outboundMenu = new PageElement(
			By.xpath("//button[contains(.,'Outbound')]"));
		pickByOrderSubmenu = new PageElement(By.xpath(
			"//button[contains(.,'Pick By Order')]"));
		pickOrderToDropIdSubmenu = new PageElement(By.xpath(
			"//button[contains(.,'Pick Order to Drop ID')]"));
		completeDropIdSubmenu = new PageElement(By.xpath(
			"//button[contains(.,'Complete Drop ID')]"));
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

	public void selectComplteDropId() {
		hamburgermanuWmxM.click();
		Utils.sleep(2000);
		outboundMenu.moveClickSendKeys("");
		Utils.sleep(2000);
		completeDropIdSubmenu.moveClickSendKeys("");
		Utils.sleep(5000);
	}

}