package xpo.qa.sc.wmx.m.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import xpo.qa.common.Utils;
import xpo.qa.common.selenium.PageElement;
import xpo.qa.common.selenium.WebPage;

public class WmxMDropIdInfoPage extends WebPage {
	private PageElement dropIdInput;
	private PageElement processButton;

	public WmxMDropIdInfoPage(WebDriver driver) {
		super(driver);

		dropIdInput = new PageElement(By.xpath("//input[@placeholder='Drop Id']"));
		processButton = new PageElement(By.xpath("//button/span[contains(.,'Process')]"));
	}

	public void processComplteDropId(String dropId) {
		dropIdInput.click();
		Utils.sleep(2000);
		dropIdInput.sendKeys(dropId);
		processButton.click();
		Utils.sleep(8000);
	}
}
