package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

public class MyBoardPage extends BasePage{
    public MyBoardPage(WebDriver driver) {
        setDriver(driver);
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, ELEMENT_TIMEOUT_SECONDS), this);
    }

    @FindBy(xpath = "//*[@data-testid='board-name-display']")
    WebElement boardName;
    @FindBy(xpath = "//button[@aria-label='Show menu']")
    WebElement btnDots;
    @FindBy(xpath = "//div[text()='Close board']")
    WebElement btnCloseBoard;
    @FindBy(xpath = "//button[@data-testid='popover-close-board-confirm']")
    WebElement btnCloseBoardConfirm;
    @FindBy(xpath = "//button[@data-testid='close-board-delete-board-button']")
    WebElement btnPermanentlyDeleteBoard;
    @FindBy(xpath = "//button[@data-testid='close-board-delete-board-confirm-button']" )
    WebElement btnDeleteBoardConfirm;

    public void deleteBoard(){
        clickWait(btnDots, 10);
        scrollTo(btnCloseBoard);
        clickWait(btnCloseBoard, 10);
        clickWait(btnCloseBoardConfirm, 10);
        clickWait(btnDots, 10);
        scrollTo(btnPermanentlyDeleteBoard);
        clickWait(btnPermanentlyDeleteBoard, 10);
        clickWait(btnDeleteBoardConfirm, 10);
    }

    public boolean validateBoardName(String text, int time){
        return validateTextInElementWait(boardName, text, time);
    }
}
