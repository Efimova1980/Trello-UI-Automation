package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

public class HomePage extends BasePage{
    public HomePage(WebDriver driver) {
        setDriver(driver);
        driver.get("https://trello.com/");
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, ELEMENT_TIMEOUT_SECONDS), this);
    }

    @FindBy(xpath = "//a[normalize-space()='Log in' and contains(@href,'id.atlassian.com/login')]")
    WebElement btnLogin;

    public LoginPage clickBtnLogin(){
        btnLogin.click();
        return new LoginPage(driver);
    }
}
