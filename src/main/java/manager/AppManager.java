package manager;

import dto.User;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import pages.BoardsPage;
import pages.HomePage;
import utils.WDListener;

import java.time.Duration;

public class AppManager {
    protected Logger logger = LoggerFactory.getLogger(AppManager.class);

    @Getter
    private WebDriver driver;
    private String mainTab;

    // one browser per test class: lets logged-in tests share a single login
    @BeforeClass(alwaysRun = true)
    public void setup() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--lang=en");
        driver = new ChromeDriver(chromeOptions);
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));

        WebDriverListener webDriverListener = new WDListener();
        driver = new EventFiringDecorator<>(webDriverListener).decorate(driver);

        mainTab = driver.getWindowHandle();
        logger.info("start testing class --> {}", getClass().getSimpleName());
    }

    public void loginTrello() {
        new HomePage(getDriver())
                .clickBtnLogin()
                .login(User.getValidUser());
    }

    // brings every test to the same starting point: the boards page in the main tab
    public BoardsPage openTrello() {
        closeExtraTabs();
        driver.get("https://trello.com/");
        return new BoardsPage(driver);
    }

    private void closeExtraTabs() {
        for (String tab : driver.getWindowHandles()) {
            if (!tab.equals(mainTab)) {
                driver.switchTo().window(tab).close();
            }
        }
        driver.switchTo().window(mainTab);
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        logger.info("stop testing class --> {}", getClass().getSimpleName());
    }
}
