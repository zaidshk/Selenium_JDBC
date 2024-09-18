package xpo.qa.sc.wmx.m.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import xpo.qa.common.Utils;
import xpo.qa.common.selenium.PageElement;
import xpo.qa.common.selenium.WebPage;

public class WmxMPickOrderToDropId extends WebPage {

	private PageElement dropIdInput;
	private PageElement orderKeyInput;
	private PageElement searchButton;

	public WmxMPickOrderToDropId(WebDriver driver) {
		super(driver);

		dropIdInput = new PageElement(By.xpath("//input[@id='DROPID']"));
		orderKeyInput = new PageElement(By.xpath("//input[@placeholder='ORDERKEY']"));
		searchButton = new PageElement(By.xpath("//button[contains(.,'Search')]"));
	}

	public void setPickOrderToDropId(String dropId, String orderKey) {
		dropIdInput.click();
		Utils.sleep(2000);
		dropIdInput.sendKeys(dropId);
		Utils.sleep(2000);
		orderKeyInput.click();
		Utils.sleep(2000);
		orderKeyInput.sendKeys(orderKey);
		searchButton.click();
		Utils.sleep(8000);
	}

	public String getDropIdText() {
		return dropIdInput.getElementText();
	}
}
