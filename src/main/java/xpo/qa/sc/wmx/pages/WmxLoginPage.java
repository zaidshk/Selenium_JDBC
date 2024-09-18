package xpo.qa.sc.wmx.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import xpo.qa.common.Utils;
import xpo.qa.common.selenium.PageElement;
import xpo.qa.common.selenium.WebPage;

/**
 * Login Page for WMX
 * 
 * @author acharya.priyanka
 */

public class WmxLoginPage extends WebPage {
	private PageElement userIdInput;
	private PageElement loginButton;
	private PageElement passwordInput;

	public WmxLoginPage(WebDriver driver) {
		super(driver);
		userIdInput = new PageElement(By.id("UserId"));
		loginButton = new PageElement(By.xpath("//button[contains(., 'Log In')]"));
		passwordInput = new PageElement(By.id("Psw"));
	}

	public void loginToWMX(String userId, String password) {
		userIdInput.sendKeys(userId);
//		nextButton.click();
		Utils.sleep(3000);
		passwordInput.sendKeys(password);
		loginButton.click();
		Utils.sleep(10000);
	}

}
