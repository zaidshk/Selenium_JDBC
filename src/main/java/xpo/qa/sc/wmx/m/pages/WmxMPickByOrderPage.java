package xpo.qa.sc.wmx.m.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import xpo.qa.common.Utils;
import xpo.qa.common.selenium.PageElement;
import xpo.qa.common.selenium.WebPage;

public class WmxMPickByOrderPage extends WebPage {
	private PageElement orderKeyInput;
	private PageElement searchButton;

	public WmxMPickByOrderPage(WebDriver driver) {
		super(driver);

		orderKeyInput = new PageElement(By.xpath("//input[@placeholder='ORDERKEY']"));
		searchButton = new PageElement(By.xpath("//button[contains(.,'Search')]"));
	}

	public void enterOrderKey(String orderkey) {
		orderKeyInput.click();
		Utils.sleep(2000);
		orderKeyInput.sendKeys(orderkey);
		searchButton.click();
		Utils.sleep(8000);
	}
}
