package xpo.qa.sc.wmx.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

import xpo.qa.common.Utils;
import xpo.qa.common.selenium.PageElement;
import xpo.qa.common.selenium.WebPage;

public class WmxVerificationPage extends WebPage {
	private PageElement dropTypeDropdown;
	private PageElement docKeyInput;
	private PageElement searchButton;
	private PageElement verifyDetailLink;
	private PageElement skuValue;
	private PageElement verifyQuantityValue;
	private PageElement pickQuantityValue;
	private PageElement skuInput;

	public WmxVerificationPage(WebDriver driver) {
		super(driver);

		dropTypeDropdown = new PageElement(By.xpath("//select[@name='docType']"));
		docKeyInput = new PageElement(By.xpath("//input[@name='dockey']"));
		searchButton = new PageElement(By.xpath("//button[contains(.,'Search')]"));
		verifyDetailLink = new PageElement(By.xpath("//span[contains(.,'Verify Detail')]/following-sibling::i"));
		skuValue = new PageElement(By.xpath("//table/tbody/tr/td[5]/span"));
		verifyQuantityValue = new PageElement(By.xpath("//table/tbody/tr/td[8]/span"));
		pickQuantityValue = new PageElement(By.xpath("//table/tbody/tr/td[7]/span"));
		skuInput = new PageElement(By.xpath("//input[@name='sku']"));
	}

	public void enterVerificationOrderkey(String orderkey) {
		dropTypeDropdown.selectDropDownOptionByVisibleText("ORDER");
		Utils.sleep(2000);
		docKeyInput.sendKeys(orderkey);
		searchButton.click();
		Utils.sleep(10000);
	}

	public void verifyOrderDetail() {
		verifyDetailLink.scrollToElement();
		verifyDetailLink.moveClickSendKeys("");
		Utils.sleep(2000);
		skuValue.waitVisible();
		int pickQuantity = Integer.parseInt(pickQuantityValue.getElementText());
		for (int i = 1; i <= pickQuantity; i++) {
			skuInput.clear();
			skuInput.sendKeys(skuValue.getElementText());
			skuInput.sendKeys(Keys.ENTER);
			Utils.sleep(3000);
			if (Integer.parseInt(verifyQuantityValue.getElementText()) != i) {
				skuInput.sendKeys(Keys.ENTER);
			}
		}
	}

	public boolean isOrderVerificationSuccessful() {
		return getPageSubElements(By.xpath("//tr[@class='LightGreen ng-star-inserted']")).size() > 0;
	}
}
