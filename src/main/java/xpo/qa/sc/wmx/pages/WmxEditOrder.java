package xpo.qa.sc.wmx.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import xpo.qa.common.Utils;
import xpo.qa.common.selenium.PageElement;
import xpo.qa.common.selenium.WebPage;

public class WmxEditOrder extends WebPage {
	private PageElement actionsButton;
	private PageElement allocateDropdown;
	private PageElement releaseDropdown;
	private PageElement processDropdown;
	private PageElement confirmationYesButton;
	private PageElement statusFieldValue;

	public WmxEditOrder(WebDriver driver) {
		super(driver);
		actionsButton = new PageElement(By.xpath("//button[contains(.,'Actions')]"));
		allocateDropdown = new PageElement(By.xpath("//a[contains(.,'Allocate')]"));
		releaseDropdown = new PageElement(By.xpath("//a[contains(.,'Release')]"));
		processDropdown = new PageElement(By.xpath("//li[@role='menuitem']/a[starts-with(.,' Process')]"));
		confirmationYesButton = new PageElement(By.xpath("//button[contains(.,'Yes')]"));
		statusFieldValue = new PageElement(By.xpath("//select[@name='STATUS']"));

	}

	public void clickActionsButton() {
		actionsButton.click();
	}

	public void clickAllocateDropdown() {
		allocateDropdown.click();
	}

	public void clickProcessDropdown() {
		processDropdown.click();
	}

	public void clickReleaseDropdown() {
		releaseDropdown.click();
	}

	public void clickconfirmationYesButton() {
		confirmationYesButton.click();
		Utils.sleep(7000);
	}

	public String getStatusFieldValue() {
		return statusFieldValue.getFirstSelectedOptionText();
	}
}
