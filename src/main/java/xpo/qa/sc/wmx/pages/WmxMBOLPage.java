package xpo.qa.sc.wmx.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import xpo.qa.common.Utils;
import xpo.qa.common.selenium.PageElement;
import xpo.qa.common.selenium.WebPage;

public class WmxMBOLPage extends WebPage {
	private PageElement newButton;

	public WmxMBOLPage(WebDriver driver) {
		super(driver);

		newButton = new PageElement(By.xpath("//button[contains(.,'New')]"));
	}
	public void clickNewButton() {
		newButton.click();
		Utils.sleep(6000);
	}
}
