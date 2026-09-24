package pages;

import dto.Board;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BoardsPage extends BasePage{
    public BoardsPage(WebDriver driver) {
        setDriver(driver);
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, ELEMENT_TIMEOUT_SECONDS), this);
    }

    @FindBy(xpath = "//button[@data-testid='create-board-tile']")
    WebElement btnCreateNewBoard;
    @FindBy(xpath = "//button[@data-testid='create-board-button']")
    WebElement btnCreateBoardMenuItem;
    @FindBy(xpath = "//input[@data-testid='create-board-title-input']")
    WebElement inputBoardTitle;
    @FindBy(xpath = "//button[@data-testid='create-board-submit-button']")
    WebElement btnCreateNewBoardSubmit;
    @FindBy(xpath = "//button[@data-testid='header-member-menu-button']")
    WebElement btnAccount;
    @FindBy(xpath = "//span[text()='Manage account']")
    WebElement btnManageAccountLink;

    public MyBoardPage createNewBoard(Board board){
        clickWait(btnCreateNewBoard, 10);
        clickWait(btnCreateBoardMenuItem, 10);
        inputBoardTitle.sendKeys(board.getBoardTitle());
        clickWait(btnCreateNewBoardSubmit, 10);
        validateURL("/b/");
        return new MyBoardPage(driver);
    }

    public void openMyAccount(){
        clickWait(btnAccount, 20);
        clickWait(btnManageAccountLink,10);
    }

    public boolean validateBoardNotPresent(String boardTitle, int time){
        return new WebDriverWait(driver, Duration.ofSeconds(time))
                .until(ExpectedConditions
                        .invisibilityOfElementLocated(boardTile(boardTitle)));
    }

    public void openBoard(String boardTitle){
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions
                        .elementToBeClickable(boardTile(boardTitle)))
                .click();
    }

    // board tile link: the title is in a child element of <a>, not in <a> itself
    private By boardTile(String boardTitle){
        return By.xpath("//a[.//*[normalize-space(text())='" + boardTitle + "']]");
    }

}
