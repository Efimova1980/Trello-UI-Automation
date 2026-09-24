package tests;

import dto.User;
import manager.AppManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.BoardsPage;
import pages.HomePage;
import pages.LoginPage;
import utils.TestNGListener;

@Listeners(TestNGListener.class)

public class LoginTest extends AppManager {

    LoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    public void goToLoginPage() {
        loginPage = new HomePage(getDriver()).clickBtnLogin();
    }

    @Test(groups = "smoke")
    public void loginPositiveTest(){
        loginPage.login(User.getValidUser());
        Assert.assertTrue(new BoardsPage(getDriver())
                .validateURL("boards"));
    }

    @Test(enabled = false)
    public void loginNegativeTest_WrongEmail(){
        loginPage.submitEmail(User.getUserWithWrongEmail().getEmail());
        Assert.assertTrue(loginPage.validateIncorrectEmailError(5));
    }

    @Test(enabled = false)
    public void loginNegativeTest_WrongPassword(){
        User user = User.getUserWithWrongPassword();
        loginPage.submitEmail(user.getEmail());
        loginPage.submitPassword(user.getPassword());
        Assert.assertTrue(loginPage.validateIncorrectPasswordError(5));
    }

    @Test(enabled = false)
    public void loginNegativeTest_WrongTotpSecret(){
        loginPage.login(User.getUserWithWrongTotpSecret());
        Assert.assertTrue(loginPage.validateIncorrectTotpError(5));
    }
}
