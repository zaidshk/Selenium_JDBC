package xpo.qa.sc.wmx.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

import xpo.qa.common.Utils;
import xpo.qa.common.selenium.PageElement;
import xpo.qa.common.selenium.WebPage;

public class WmxEditMBOL extends WebPage {
	private PageElement docTypeDropdown;
	private PageElement orderKeyInput;
	private PageElement orderKeyOrderSearchTable;
	private PageElement editSymbolOrderTable;

	private PageElement carrierKeyDropdown;
	private PageElement proNumberInput;
	private PageElement saveButton;

	private PageElement actionsButton;
	private PageElement stageAction;
	private PageElement shipAction;
	private PageElement confirmationYesButton;

	private PageElement closeButtonNoPrinterError;
	private PageElement statusFieldValue;

	public WmxEditMBOL(WebDriver driver) {
		super(driver);

		docTypeDropdown = new PageElement(By.name("doctype"));
		orderKeyInput = new PageElement(By.xpath("//table/thead/tr/th[2]/div/input"));
		orderKeyOrderSearchTable = new PageElement(By.xpath("//table/tbody/tr/td[2]/span"));
		editSymbolOrderTable = new PageElement(By.xpath("//table/tbody/tr/td[1]/i"));

		carrierKeyDropdown = new PageElement(By.xpath("//select[@name='CARRIERKEY']"));
		proNumberInput = new PageElement(By.xpath("//input[@name='PRONUMBER']"));

		saveButton = new PageElement(By.xpath("//button[contains(.,'Save')]"));
		actionsButton = new PageElement(By.xpath("//button[contains(.,'Actions')]"));
		stageAction = new PageElement(By.xpath("//li[@role='menuitem']/a[text()=' Stage ']"));
		shipAction = new PageElement(By.xpath("//li[@role='menuitem']/a[text()=' Ship ']"));
		confirmationYesButton = new PageElement(By.xpath("//button[contains(.,'Yes')]"));
		closeButtonNoPrinterError = new PageElement(By.xpath("//span[@class='wmx-close']"));
		statusFieldValue = new PageElement(By.xpath("//select[@name='STATUS']"));

	}

	public boolean orderSearchResultTableRows() {
		if (getPageSubElements(By.xpath("//tbody/tr")).size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void selecDocTypeDropdown() {
		docTypeDropdown.selectDropDownOptionByVisibleText("ORDER");
		Utils.sleep(5000);
	}

	public void searchOrderKeyInput(String orderKey) {
		orderKeyInput.clear();
		orderKeyInput.sendKeys(orderKey);
		Utils.sleep(3000);
		orderKeyInput.sendKeys(Keys.ENTER);
		Utils.sleep(8000);
	}

	public String getOrderKeyOrderSearchTable() {
		return orderKeyOrderSearchTable.getElementText();
	}

	public void clickEditSymbolOrderTable() {
		editSymbolOrderTable.click();
		Utils.sleep(8000);
	}

	public void selectCarrierKeyDropdown(String carrierKey) {
		carrierKeyDropdown.selectDropDownOptionByValue(carrierKey);
	}

	public void setProNumberInput(String proNumber) {
		proNumberInput.clear();
		proNumberInput.sendKeys(proNumber);
	}

	public void clickSaveButton() {
		saveButton.click();
		Utils.sleep(8000);
	}

	public void clickActionsButton() {
		actionsButton.click();
	}

	public void selectStageAction() {
		stageAction.click();
		confirmationYesButton.click();
		Utils.sleep(7000);
	}

	public void selectShipAction() {
		shipAction.click();
		confirmationYesButton.click();
		Utils.sleep(7000);
	}

	public void clickCloseButtonNoPrinterError() {
		closeButtonNoPrinterError.click();
		Utils.sleep(5000);
	}

	public String getStatusFieldValue() {
		return statusFieldValue.getFirstSelectedOptionText();
	}
}
