package tests;

import manager.AppManager;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AtlassianProfilePage;
import pages.BoardsPage;
import utils.TestNGListener;

import java.time.Duration;
import java.util.Set;

@Listeners(TestNGListener.class)

public class ChangeProfilePhotoTests extends AppManager {

    private BoardsPage boardsPage;

    // one login per class (the browser stays open between tests)
    @BeforeClass(alwaysRun = true)
    public void loginOnce(){
        loginTrello();
    }

    // closes the Atlassian tab left by the previous test and returns to Trello
    @BeforeMethod(alwaysRun = true)
    public void goToTrello(){
        boardsPage = openTrello();
    }

    @Test(groups = "smoke")
    public void changeProfilePhotoPositiveTest(){
        AtlassianProfilePage atlassianProfilePage = changeProfilePhoto("src/main/resources/img.png");
        Assert.assertTrue(atlassianProfilePage
                .validateMessage("We've uploaded your new avatar. It may take a few minutes to display everywhere."));
    }

    @Test
    public void changeProfilePhotoNegativeTest_WrongFormatFile(){
        AtlassianProfilePage atlassianProfilePage = changeProfilePhoto("src/main/resources/Board1.csv");
        Assert.assertTrue(atlassianProfilePage
                .validateWrongFormatFileMessage("Upload a photo or select from some default options"));
    }

    // "Manage account" opens a new tab: wait for it and switch to the tab that wasn't there before
    private AtlassianProfilePage changeProfilePhoto(String photoPath){
        Set<String> tabsBefore = getDriver().getWindowHandles();
        boardsPage.openMyAccount();
        new WebDriverWait(getDriver(), Duration.ofSeconds(10))
                .until(ExpectedConditions.numberOfWindowsToBe(tabsBefore.size() + 1));
        for (String tab : getDriver().getWindowHandles()) {
            if (!tabsBefore.contains(tab)) {
                getDriver().switchTo().window(tab);
            }
        }
        AtlassianProfilePage atlassianProfilePage = new AtlassianProfilePage(getDriver());
        atlassianProfilePage.changeMyProfilePhoto(photoPath);
        return atlassianProfilePage;
    }
}
