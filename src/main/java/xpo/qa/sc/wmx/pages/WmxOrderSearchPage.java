package xpo.qa.sc.wmx.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

import xpo.qa.common.Utils;
import xpo.qa.common.selenium.PageElement;
import xpo.qa.common.selenium.WebPage;

public class WmxOrderSearchPage extends WebPage {
	private PageElement externalkeyInput;
	private PageElement orderSearchResultTableExternalkey;
	private PageElement orderSearchResultTableStatus;
	private PageElement orderSearchResultTableOrderKey;
	private PageElement orderSearchResultTableEditOrderSymbol;

	public WmxOrderSearchPage(WebDriver driver) {
		super(driver);
//		externalkeyInput = new PageElement(By.xpath("//label[contains(.,'Extern Key')]/following-sibling::input"));
		externalkeyInput = new PageElement(By.xpath("/html/body/app-root/xpo-shell/xpo-tab-drawer/mat-drawer-container/mat-drawer-content/xpo-tab-drawer-content/app-order-parent/app-order-search/div/div/div/div/div/wx-search-controller/div[2]/div/form/div/table/thead/tr/th[4]/div/input"));
		orderSearchResultTableExternalkey = new PageElement(By.xpath("//tbody/tr/td[4]/span"));
		orderSearchResultTableStatus = new PageElement(By.xpath("//tbody/tr/td[5]/span"));
		orderSearchResultTableOrderKey = new PageElement(By.xpath("//tbody/tr/td[2]/span"));
		orderSearchResultTableEditOrderSymbol = new PageElement(By.xpath("//tbody/tr/td[1]"));

	}

	public void searchOrderWmx(String externKey) {
		externalkeyInput.clear();
		externalkeyInput.sendKeys(externKey);
		Utils.sleep(3000);
		externalkeyInput.sendKeys(Keys.ENTER);
		Utils.sleep(5000);
	}

	public boolean orderSearchResultTableRows() {
		if (getPageSubElements(By.xpath("//tbody/tr")).size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public String getOrderSearchResultTableExternalkey() {
		return orderSearchResultTableExternalkey.getElementText();
	}

	public String getOrderSearchResultTableStatus() {
		orderSearchResultTableStatus.highlight("lightgreen");
		return orderSearchResultTableStatus.getElementText();
	}

	public String getOrderSearchResultTableOrderKey() {
		return orderSearchResultTableOrderKey.getElementText();
	}

	public void clickOrderSearchResultTableEditOrderSymbol() {
		orderSearchResultTableEditOrderSymbol.click();
		Utils.sleep(5000);
	}
}
