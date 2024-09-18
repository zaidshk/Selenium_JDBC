package xpo.qa.sc.wmx.m.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import xpo.qa.common.Utils;
import xpo.qa.common.selenium.PageElement;
import xpo.qa.common.selenium.WebPage;

/**
 * Login Page for WMX Mobile
 * 
 * @author acharya.priyanka
 */

public class WmxMLoginPage extends WebPage {
	private PageElement userIdInput;
	private PageElement nextButton;
	private PageElement passwordInput;

	public WmxMLoginPage(WebDriver driver) {
		super(driver);
		userIdInput = new PageElement(By.id("UserId"));
		nextButton = new PageElement(By.xpath("//button[contains(., 'Log In')]"));
		passwordInput = new PageElement(By.id("Psw"));
	}

	public void loginToWMXM(String userId, String password) {
		userIdInput.sendKeys(userId);
//		nextButton.click();
		Utils.sleep(3000);
		passwordInput.sendKeys(password);
		nextButton.click();
		Utils.sleep(8000);
	}

}

