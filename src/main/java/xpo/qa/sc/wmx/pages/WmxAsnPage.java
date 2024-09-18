package xpo.qa.sc.wmx.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

import xpo.qa.common.Utils;
import xpo.qa.common.selenium.PageElement;
import xpo.qa.common.selenium.WebPage;

public class WmxAsnPage extends WebPage{
	
	private PageElement asnMenu;
	private PageElement externalkeyInput;
	private PageElement asnSearchResultTableExternalkey;
	private PageElement asnSearchResultTableStatus;
	private PageElement asnSearchResultTableOrderKey;
	public WmxAsnPage(WebDriver driver) {
		
		
		super(driver);
		
//		asnMenu=new PageElement(By.xpath(
//				"//input[@name='item.APP_TITLE']/following-sibling::a[contains(.,'Inbound')]/following-sibling::ul//li[starts-with(.,' ASN')]"));
		
		
		
		asnMenu=new PageElement(By.xpath("/html/body/app-root/xpo-shell/xpo-tab-drawer/mat-drawer-container/mat-drawer/div/div/nav/xpo-vertical-tab-group[1]/div/a[3]/span[2]"));
		externalkeyInput = new PageElement(By.xpath("//label[contains(.,'Extern Key')]/following-sibling::input"));
		asnSearchResultTableExternalkey = new PageElement(By.xpath("//tbody/tr/td[4]/span"));
		asnSearchResultTableStatus = new PageElement(By.xpath("//tbody/tr/td[5]/span"));
		asnSearchResultTableOrderKey = new PageElement(By.xpath("//tbody/tr/td[2]/span"));
		
		
		
	}
	
	
	public void selectASN() {
		
		asnMenu.moveClickSendKeys("");
		Utils.sleep(2000);
		
	}
	
	public void searchASNWmx(String externKey) {
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
		return asnSearchResultTableExternalkey.getElementText();
	}
	
	public String getOrderSearchResultTableStatus() {
		asnSearchResultTableStatus.highlight("lightgreen");
		return asnSearchResultTableStatus.getElementText();
	}

	public String getOrderSearchResultTableOrderKey() {
		return asnSearchResultTableOrderKey.getElementText();
	}

	

}
