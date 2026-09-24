package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;

public class AtlassianProfilePage extends BasePage{
    public AtlassianProfilePage(WebDriver driver){
        setDriver(driver);
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, ELEMENT_TIMEOUT_SECONDS), this);
    }

    private static final String PROFILE_PHOTO_XPATH = "//div[@data-test-selector='profile-hover-info']";

    @FindBy(xpath = PROFILE_PHOTO_XPATH)
    WebElement btnProfilePhoto;
    @FindBy(xpath = "//button[@data-testid='change-avatar']")
    WebElement btnChangeAvatar;
    @FindBy(xpath = "//input[@data-testid='image-navigator-input-file']")
    WebElement inputUploadPhoto;
    @FindBy(xpath = "//button/span[text()='Upload']")
    WebElement btnUploadPhoto;
    @FindBy(xpath = "//*[text()[contains(., 'uploaded your new avatar')]]")
    WebElement popupMessage;
    @FindBy(xpath = "//h2[contains(normalize-space(.), 'Upload a photo')]")
    WebElement popupWrongFormatFile;

    public void changeMyProfilePhoto(String photoPath){
        // the hover overlay is invisible until the mouse is over it, so wait for its presence
        // in the DOM (not visibility) instead of a fixed 2-second pause
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath(PROFILE_PHOTO_XPATH)));
        new Actions(driver).moveToElement(btnProfilePhoto).click().perform();
        clickWait(btnChangeAvatar, 10);
        File photo = new File(photoPath);
        inputUploadPhoto.sendKeys(photo.getAbsolutePath());
        clickWait(btnUploadPhoto, 10);
    }

    public boolean validateMessage(String text){
        return validateTextInElementWait(popupMessage, text, 10);
    }

    public boolean validateWrongFormatFileMessage(String text){
        return validateTextInElementWait(popupWrongFormatFile, text, 10);
    }
}
