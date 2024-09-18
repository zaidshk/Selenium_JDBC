package xpo.qa.sc.wmx.m.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import xpo.qa.common.Utils;
import xpo.qa.common.selenium.PageElement;
import xpo.qa.common.selenium.WebPage;

public class WmxMPickPage extends WebPage {
	private PageElement productInfoLOCValue;
	private PageElement productInfoLOTValue;
	private PageElement productInfoPickQuantityValue;
	private PageElement pickInToCaseIdValue;

	private PageElement confirmationLOCInput;
	private PageElement confirmationLOTInput;
	private PageElement confirmationPickQuantityInput;
	private PageElement InputToCaseIdInput;

	private PageElement processButton;

	public WmxMPickPage(WebDriver driver) {
		super(driver);

		productInfoLOCValue = new PageElement(
			By.xpath("//span[contains(.,'LOC:')]/parent::div/following-sibling::div"));
		productInfoLOTValue = new PageElement(
			By.xpath("//span[contains(.,'LOT:')]/parent::div/following-sibling::div"));
		productInfoPickQuantityValue = new PageElement(
			By.xpath("//span[contains(.,'Pick Qty')]/parent::div/following-sibling::div/div[1]/span"));
		pickInToCaseIdValue = new PageElement(
			By.xpath("//span[contains(.,'To Case ID:')]/parent::div/following-sibling::div"));

		confirmationLOCInput = new PageElement(
			By.xpath("//input[@name='Locconf']"));
		confirmationLOTInput = new PageElement(
			By.xpath("//input[@name='Lotconf']"));
		confirmationPickQuantityInput = new PageElement(
			By.xpath("//input[@name='Quantityconf']"));
		InputToCaseIdInput = new PageElement(
			By.xpath("//input[@name='Eqptconf']"));

		processButton = new PageElement(
			By.xpath("//span[contains(.,'Process')]"));

	}

	public void processOrderPick() {
		confirmationLOCInput.click();
		Utils.sleep(2000);
		confirmationLOCInput.sendKeys(productInfoLOCValue.getElementText());
		confirmationLOTInput.click();
		Utils.sleep(2000);
		confirmationLOTInput.sendKeys(productInfoLOTValue.getElementText());
		confirmationPickQuantityInput.click();
		Utils.sleep(2000);
		confirmationPickQuantityInput.sendKeys(productInfoPickQuantityValue.getElementText());
		InputToCaseIdInput.click();
		Utils.sleep(2000);
		InputToCaseIdInput.sendKeys(pickInToCaseIdValue.getElementText());
		Utils.sleep(3000);
		processButton.waitClickable();
		processButton.click();
		Utils.sleep(8000);
	}

}
